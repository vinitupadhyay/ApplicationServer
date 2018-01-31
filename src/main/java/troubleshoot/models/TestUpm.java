package troubleshoot.models;

import org.apache.log4j.Logger;

public class TestUpm
{
	private final static Logger logger = Logger.getLogger(TestUpm.class);
	private static final int DEFAULT_TIME_OUT = 10000;
	
	private static TestUpm testUpm = null;
	
	private boolean isErrorInResponse;
	private Object monitor;
	
	public TestUpm() 
	{
		monitor = new Object();
	}
	
	public static TestUpm getInstance()
	{
		if(testUpm == null)
		{
			testUpm = new TestUpm();
		}
		return testUpm;
	}
	
	private void wakeUp()
	{
		synchronized (monitor) 
		{
			monitor.notify();
		}
	}
	
	private boolean waitForResponse()
	{
		boolean isTimeOutError = false;
		synchronized (monitor) 
		{
			try 
			{
				long t0 = System.currentTimeMillis();
				long t1 = 0;
				monitor.wait(DEFAULT_TIME_OUT);
				t1 = System.currentTimeMillis();
				if((t1-t0) >= DEFAULT_TIME_OUT)
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
}
