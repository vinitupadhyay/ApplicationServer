package troubleshoot.models;

import java.util.Arrays;
import java.util.Vector;

import org.apache.log4j.Logger;

import spot.message.models.SpotApdu;
import spot.messages.SpotMessageType;
import spot.messages.SpotMessages;
import troubleshoot.event.handler.EventManager;
import troubleshoot.model.pojo.FileBrowseInfo;
import troubleshoot.socket.event.EventListener;
import troubleshoot.socket.event.SocketEvent;
import troubleshoot.socket.event.SocketEventService;
import troubleshoot.util.EdtConvert;

public class FileBrowse
{
	private final static Logger logger = Logger.getLogger(FileBrowse.class);
	private static final long SPOT_COMMAND_REPLY_TIMEOUT_MILLISECONDS = 20000L;
	private static final String NO_ERROR = "00";
	
	private static FileBrowse fileBrowse;
	private Object monitor;
	private boolean isErrorInResponse;
	public Vector<FileBrowseInfo> fileIdsForReqType;
	
	private FileBrowse()
	{
		fileIdsForReqType = new Vector<FileBrowseInfo>();
		monitor = new Object();
		subscribeToEvent();
	}
	
	private void subscribeToEvent()
	{
		SocketEventService.getInstance().subscribe(new SocketEventListener());
	}

	// ******************************************************************
	// PUBLIC METHODS (general, getter, setter, interface imp)
	// ******************************************************************
	
	public static FileBrowse getInstance()
	{
		if(fileBrowse == null)
		{
			fileBrowse = new FileBrowse();
		}
		return fileBrowse;
	}
	
	public boolean browseFileResource(byte fileType)
	{
		isErrorInResponse = false;
		fileIdsForReqType.clear();
		
		sendFileBrowseResponse(fileType);
		if(!waitForResponse(SPOT_COMMAND_REPLY_TIMEOUT_MILLISECONDS) && !isErrorInResponse)
		{
			return true;
		}
		return false;
	}
	
	private void sendFileBrowseResponse(byte fileType)
	{
		EventManager.getInstance().send(SpotMessages.getInstance().getFileBrowseMessage(SpotMessageType.FILE_BROWSE, fileType));
	}
	
	public void handleFileBrowseResponse(SpotApdu apdu)
	{
		if(apdu.getAck().equals(NO_ERROR)) // Received Ok and has more blocks to request
		{
			int index = 0;
			byte[] data = EdtConvert.hexStringToByteArray(apdu.getData());
			
			byte fileType = data[index];
			index++;
			
			int numberOfEntries = data[index];
			if(numberOfEntries < 0)
			{
				numberOfEntries+=256;
			}
			
			index++;
			
			for(int i = 0; i< numberOfEntries; i++)
			{
				FileBrowseInfo resource = new FileBrowseInfo();
				resource.setFileType(fileType);
				resource.setFileId(data[index]);
				index++;
				resource.setCrc( EdtConvert.bytesToHexString(Arrays.copyOfRange(data, index, (index+4))) );
				index += 4;
				resource.setFileAuthentication(data[index]);
				index++;
				byte[] desc = Arrays.copyOfRange(data, index, (index+16));
				resource.setFileName(new String(desc).trim());
				index += 16;
				
				fileIdsForReqType.add(resource);
			}
			isErrorInResponse = false;
			wakeUp();
		}
		else
		{
			logger.error("File Browse : error in response : error code "+apdu.getAck());
			isErrorInResponse = true;
			wakeUp(); // wakes up the wait()
		}
	}
	
	private boolean waitForResponse(long timeout)
	{
		boolean isTimeOutError = false;
		synchronized (monitor) 
		{
			try 
			{
				long t0 = System.currentTimeMillis();
				long t1 = 0;
				monitor.wait(timeout);
				t1 = System.currentTimeMillis();
				if((t1-t0) >= timeout)
				{
					isTimeOutError = true;
					logger.error("Response not received : timeout error");
				}
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		return isTimeOutError;
	}
	
	private void wakeUp()
	{
		synchronized (monitor) 
		{
			monitor.notify();
		}
	}
	
	private final class  SocketEventListener implements EventListener
	{

		@Override
		public void onMessage(SocketEvent eventType)
		{
			switch (eventType)
			{
				case Disconnect:
					logger.debug("SOCKET_DISCONNECT");
					wakeUp( );
					break;
				default:
					break;
			}
		}
		
	}
}
