package troubleshoot.models;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import spot.message.models.SpotApdu;
import spot.messages.SpotMessageType;
import spot.messages.SpotMessages;
import troubleshoot.config.TroubleshootConfigurations;
import troubleshoot.event.handler.EventManager;
import troubleshoot.file.managment.api.FileResourceWriter;
import troubleshoot.model.pojo.RollCallInfo;
import troubleshoot.util.EdtConvert;

public class DiagnosticInformation
{
	private final static Logger logger = Logger.getLogger(DiagnosticInformation.class);
	private static final int DEFAULT_TIME_OUT = 10000;
	
	public static RollCallInfo rollCallInfo;
	
	private static DiagnosticInformation diagInformation = null;
	
	private boolean isErrorInResponse;
	private Object monitor;
	
	public DiagnosticInformation() 
	{
		monitor = new Object();
		rollCallInfo = new RollCallInfo();
	}
	
	public static DiagnosticInformation getInstance()
	{
		if(diagInformation == null)
		{
			diagInformation = new DiagnosticInformation();
		}
		return diagInformation;
	}
	
	public boolean collectDiagnosticInfo()
	{
		logger.info("collect diagnostic info - START" );
		isErrorInResponse = false;
		
		sendExtDiagnosticRequest();
		if(!waitForResponse() && !isErrorInResponse)
		{
			parseRollCallFile();
			logger.info("collect diagnostic info - END - ");
			return true;
		}
		else
		{
			logger.info("Timeout or Error in response");
			logger.info("collect diagnostic info - END - ");
			return false;
		}
	}
	
	private void parseRollCallFile()
	{
		BufferedReader br = null;
		try
		{
			String rollCallFileName = TroubleshootConfigurations.getRollCallFilePath();
			br = new BufferedReader(new FileReader(rollCallFileName));
			String line = null;
			while ((line = br.readLine()) != null)
			{
				if(!line.isEmpty())
				{
					if(line.contains("Side="))
					{
						String values[] = line.split("=");
						rollCallInfo.setSide(values[1]);
						break;
					}
				}
			}
		}
		catch (FileNotFoundException e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		catch (IOException e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		catch (NumberFormatException e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if(br != null)
			{
				try
				{
					br.close();
				}
				catch (IOException e)
				{
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
	
	private void sendExtDiagnosticRequest()
	{
		EventManager.getInstance().send(SpotMessages.getInstance().getExtDignosticRequestMessage(SpotMessageType.VANGUARD_EXTENDED_DIAGNOSTIC, (byte)0x02));
	}
	
	public void processExtDiagnosticAnswer(SpotApdu apdu)
	{
		if(apdu.getAck().equals("00"))
		{
			isErrorInResponse = false;
			String data = apdu.getData().substring(6);
			if(FileResourceWriter.getInstance().open(TroubleshootConfigurations.getRollCallFilePath()))
			{
				if (FileResourceWriter.getInstance().write(EdtConvert.hexStringToByteArray(data)))
				{
					if (FileResourceWriter.getInstance().close( ))
					{
						logger.info("Roll call info stored successfully");
					}
					else
					{
						logger.error("unable to close file");			
					}
				}
				else
				{
					logger.error("unable to append data into file");
				}
			}
			else
			{
				logger.error("Error : error while opening file");
			}
		}
		else
		{
			isErrorInResponse = true;
			logger.error("Extended Diagnostic : error in response. error code = "+apdu.getAck() );
		}
		wakeUp();
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
	
	private void wakeUp()
	{
		synchronized (monitor) 
		{
			monitor.notify();
		}
	}
}
