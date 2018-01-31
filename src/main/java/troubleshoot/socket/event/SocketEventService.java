package troubleshoot.socket.event;

import java.util.Vector;

public class SocketEventService
{
	private Vector<EventListener> listeners;
	private static SocketEventService service = null;
	private SocketEventService()
	{
		listeners = new Vector<EventListener>();
	}
	
	public static SocketEventService getInstance()
	{
		if(service == null)
		{
			service = new SocketEventService();
		}
		return service;
	}
	
	public void subscribe(EventListener eventListerner)
	{
		listeners.add(eventListerner);
	}
	
	public void publish(SocketEvent eventType)
	{
		for (EventListener eventListener : listeners)
		{
			new Thread(new Invoker(eventListener, eventType)).start();
		}
	}
}
