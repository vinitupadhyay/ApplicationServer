package troubleshoot.models;

import java.util.Arrays;

import org.apache.log4j.Logger;

import spot.message.models.SpotApdu;
import spot.messages.SpotMessageType;
import spot.messages.SpotMessages;
import troubleshoot.config.TroubleshootConfigurations;
import troubleshoot.event.handler.EventManager;
import troubleshoot.file.managment.api.FileResourceReader;
import troubleshoot.socket.event.EventListener;
import troubleshoot.socket.event.SocketEvent;
import troubleshoot.socket.event.SocketEventService;
import troubleshoot.util.EdtConvert;

public class TestPrinter
{
	private final static Logger logger = Logger.getLogger(TestPrinter.class);
	private static final int DEFAULT_TIME_OUT = 20000;
	private final static int PRINT_MAX_LEN = 2048;
	
	private static TestPrinter testPrinter = null;
	
	private boolean isSuccess;
	private boolean isCheck;
	private boolean isErrorInResponse;
	private Object monitor;
	private byte[] jobId;
	
	private TestPrinter() 
	{
		monitor = new Object();
		subscribeToEvent();
	}
	
	public static TestPrinter getInstance()
	{
		if(testPrinter == null)
		{
			testPrinter = new TestPrinter();
		}
		return testPrinter;
	}
	
	private void subscribeToEvent()
	{
		SocketEventService.getInstance().subscribe(new SocketEventListener());
	}
	
	public boolean startTest()
	{
		isSuccess = false;
		isCheck = true;
		isErrorInResponse = false;
	
		if(sendCreateJobRequest())
		{
			if(sendEnqueueDataRequest())
			{
				if(sendExecuteJobRequest())
				{
					while(isCheck)
					{
						waitForJobStatus();
					}
				}
			}
		}
		return isSuccess;
	}
	
	private boolean waitForJobStatus()
	{
		if(!waitForResponse(DEFAULT_TIME_OUT) && !isErrorInResponse)
		{
			logger.info("Job status response received");
			return true;
		}
		else
		{
			logger.error("error or timeout while getting job status.");
			return false;
		}
	}
	
	public boolean sendCreateJobRequest()
	{
		EventManager.getInstance().send(SpotMessages.getInstance().getPrinterCreateJobMessage(SpotMessageType.PRINTER_CREATE_JOB, (byte)0x01));
		if(!waitForResponse(DEFAULT_TIME_OUT) && !isErrorInResponse)
		{
			logger.info("Job created successfully");
			return true;
		}
		else
		{
			logger.error("error or timeout while creating job.");
			return false;
		}
	}
	
	public boolean sendEnqueueDataRequest()
	{
		boolean result = false;
		try
		{
			FileResourceReader fileReader = new FileResourceReader(TroubleshootConfigurations.getPrinterResourceFilePath());
			byte[] text = fileReader.getFullData();
			
			if(text.length > PRINT_MAX_LEN)
			{
				int count = text.length/PRINT_MAX_LEN;
				int startIndex = 0;
				
				for(int i=0; i<=count; i++)
				{
					byte[] seqNumber = new byte[4];
					byte[] dataLen = new byte[4];
					
					EdtConvert.intToBytes(true, seqNumber, 0, i);
					if((text.length - startIndex) == 0)
					{
						continue;
					}
					else
					{
						if((text.length - startIndex) > PRINT_MAX_LEN)
						{
							byte[] data = Arrays.copyOfRange(text, startIndex, (startIndex + PRINT_MAX_LEN));
							startIndex += PRINT_MAX_LEN;
							EdtConvert.intToBytes(true, dataLen, 0, data.length);
							
							result = sendEnqueueRequest(new byte[]{seqNumber[2], seqNumber[3]}, new byte[]{dataLen[2],dataLen[3]}, data);
							if(result == false)
							{
								break;
							}
						}
						else
						{
							byte[] data = Arrays.copyOfRange(text, startIndex, text.length);
							startIndex += (text.length - startIndex);
							EdtConvert.intToBytes(true, dataLen, 0, data.length);
							
							result = sendEnqueueRequest(new byte[]{seqNumber[2], seqNumber[3]}, new byte[]{dataLen[2],dataLen[3]}, data);
						}
					}
				}
			}
			else
			{
				byte[] len = new byte[4];
				EdtConvert.intToBytes(true, len, 0, text.length);
				result = sendEnqueueRequest(new byte[]{0x00, 0x00}, new byte[]{len[2],len[3]}, text);
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
			logger.error("Exception : "+ e.getMessage());
		}
		return result;
	}
	
	private boolean sendEnqueueRequest(byte[] seqNumber, byte[] dataLen, byte[] data)
	{
		EventManager.getInstance().send(SpotMessages.getInstance().getPrinterEnqueueMessage(SpotMessageType.PRINTER_ENQUEUE_DATA_XML, jobId, seqNumber, dataLen, data));
		if(!waitForResponse(DEFAULT_TIME_OUT) && !isErrorInResponse)
		{
			logger.info("data send successfully");
			return true;
		}
		else
		{
			logger.error("error or timeout while enqueue data.");
			return false;
		}
	}
	
	public boolean sendExecuteJobRequest()
	{
		EventManager.getInstance().send(SpotMessages.getInstance().getPrinterExecuteJobMessage(SpotMessageType.PRINTER_EXECUTE_JOB, jobId));
		if(!waitForResponse(DEFAULT_TIME_OUT) && !isErrorInResponse)
		{
			logger.info("Job executed successfully");
			return true;
		}
		else
		{
			logger.error("error or timeout while executing job.");
			return false;
		}
	}
	
	public void processCreateJobAnswer(SpotApdu apdu)
	{	
		if(apdu.getAck().equals("00"))
		{
			jobId = EdtConvert.hexStringToByteArray(apdu.getData());
		} 
		else
		{
			isErrorInResponse = true;
			logger.error("error while creating job");
		}
		wakeUp();
	}
	
	public void processEnqueueDataAnswer(SpotApdu apdu)
	{	
		if(apdu.getAck().equals("00"))
		{
			logger.info("enqueue data command success");
		} 
		else
		{
			isErrorInResponse = true;
			logger.error("error while enqueue data");
		}
		wakeUp();
	}
	
	public void processExecuteJobAnswer(SpotApdu apdu)
	{	
		if(apdu.getAck().equals("00"))
		{
			logger.info("execute job command success");
		} 
		else
		{
			isErrorInResponse = true;
			logger.error("error while executing job");
		}
		wakeUp();
	}
	
	public void processJobStatusAnswer(SpotApdu apdu)
	{	
		if(isCheck)
		{
			if(apdu.getAck().equals("00"))
			{
				int index = 0;
				byte[] data = EdtConvert.hexStringToByteArray(apdu.getData());
				byte[] jobId = getParamValue(index, 4, data);
				index += 6;
				byte[] status = getParamValue(index, 1, data);
				
				if(Arrays.equals(jobId, this.jobId))
				{
					if(status[0] == 0x04)
					{
						isSuccess = true;
						isCheck = false;
						logger.info("job was printed successfully");
					}
					else if(status[0] == 0x05)
					{
						isSuccess = false;
						isCheck = false;
						logger.error("job print failed");
					}
					else if(status[0] == 0x06)
					{
						isSuccess = false;
						isCheck = false;
						logger.info("job cancel command received");
					}
				}
			} 
			else
			{
				isErrorInResponse = true;
				logger.error("error while executing job");
			}
			wakeUp();
		}
	}
	
	private byte[] getParamValue(int index, int paramLen ,byte[] rawData)
	{
		byte[] value = new byte[paramLen];
		System.arraycopy(rawData, index, value, 0, paramLen);
		return value;
	}
	
	private void wakeUp()
	{
		synchronized (monitor) 
		{
			monitor.notify();
		}
	}
	
	private boolean waitForResponse(final int timeout)
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
	
	private final class  SocketEventListener implements EventListener
	{

		@Override
		public void onMessage(SocketEvent eventType)
		{
			switch (eventType)
			{
				case Disconnect:
					
					break;
				default:
					break;
			}
		}
		
	}
}
