package troubleshoot.event.handler;

import spot.message.models.SpotModel;
import troubleshoot.model.pojo.IncomingData;
import troubleshoot.model.pojo.SocketAttributes;
import troubleshoot.socket.io.SpotSock;

public class EventManager implements Runnable
{
	private static EventManager eventManager = null;
	private SpotSock spotSock = null;
	private boolean isStopped;
	
	static Thread swp = null;
	
	
	public EventManager() 
	{
		eventManager = this;
		isStopped = false;
		start();
	}

	public static synchronized EventManager getInstance()
	{
		if(eventManager == null)
		{
			eventManager = new EventManager();
		}
		
		return eventManager;
	}
	
	public boolean doConnect(SocketAttributes sockAttr)
	{
		boolean bSucceeded = false;
		
		if(spotSock == null)
		{
			spotSock = new SpotSock(sockAttr);
		}
		else
		{
			spotSock.setSockAttributes(sockAttr);
		}
		
		if(sockAttr.isTlsMode())
		{
			bSucceeded = spotSock.doConnectTLS();
		}
		else
		{
			bSucceeded = spotSock.doConnect();
		}
		
		return bSucceeded;
	}
	
	public void doClose()
	{
		spotSock.close("closing socket");
	}
	
	public boolean send(byte[] data)
	{
		return spotSock.sendData(data);
	}
	
	public void start()
	{
		if (swp == null) 
		{
			swp = new Thread(this, this.getClass().getName());
			swp.setPriority(Thread.MIN_PRIORITY);
			swp.start();
		}
	}
	
	public boolean isStopped() 
	{
		return isStopped;
	}

	public void setStopped(boolean isStopped) 
	{
		this.isStopped = isStopped;
	}

	@Override
	public void run() 
	{
		while (!isStopped) 
		{
			if(spotSock != null)
			{
				IncomingData data = spotSock.getSpotMessage();
				if(data != null)
				{
					SpotModel.getInstance().ProcessIncomingMessage(data);
				}
			}
			
			try 
			{
				Thread.sleep(10);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
}
