package troubleshoot.socket.event;

public class Invoker implements Runnable
{
	EventListener listener;
	SocketEvent eventType;
	
	public Invoker(EventListener listener, SocketEvent eventType)
	{
		this.listener = listener;
		this.eventType = eventType;
	}

	@Override
	public void run()
	{
		listener.onMessage(eventType);
	}

}
