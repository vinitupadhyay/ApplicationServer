package troubleshoot.models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.Timer;

import org.apache.log4j.Logger;

import spot.message.models.SpotApdu;
import spot.messages.SpotMessageType;
import spot.messages.SpotMessages;
import troubleshoot.controller.TroubleshootController;
import troubleshoot.event.handler.EventManager;
import troubleshoot.model.pojo.SocketAttributes;
import troubleshoot.util.EdtConvert;

public class Connection 
{
	private final static Logger logger = Logger.getLogger(Connection.class);
	
	private static final int DEFAULT_TIME_OUT = 10000;
	/** @brief Default value for maintenance login mode to generate nonce data*/
	private static final byte	MAINTENANCE_GENERATE_NONCE_MODE	= (byte) 0x00;
	/** @brief Default value for maintenance login on non secure mode*/
	private static final byte	MAINTENANCE_NON_SECURE_MODE = (byte) 0x02;
	/** @brief Default value for maintenance login on non secure mode*/
	private static final byte	SERVICE_MENU_ENABLE = (byte) 0x01;
	
	public static final String	VGD_MAC_PREFIX = "005083";
	
	public static final byte HARD_RESET = 0x00;
	
	public static final byte SOFT_RESET = 0x01;
	
	private static Connection connection;
	
	private Object monitor;
	private boolean isLoggedIn;
	private byte[] nonce;
	private String challengeNumber;
	private boolean isErrorInResponse;
	public boolean isLoggedInMaintenaceMode;
	
	public Timer pingTimer;
	
	private Connection() 
	{
		monitor = new Object();
		isLoggedInMaintenaceMode = false;
		pingTimer = new Timer(25000, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ping();
			}
		});
	}
	
	public static Connection getInstance()
	{
		if(connection == null)
		{
			connection = new Connection();
		}
		return connection;
	}
	
	private void wakeUp()
	{
		synchronized (monitor) 
		{
			monitor.notify();
		}
	}
	
	
	/**
	 * Calculates the hash sha256 data for maintenance login on secure mode 
	 * @param vcode the vcode given by the call center
	 * @param nonceData to use in the hash
	 * @return the sha256 hash on parameters data 
	 * */
	private byte[] hashMaintenanceSecureData(byte[] nonceData, String vcode) 
	{
		try
		{
			byte[] bytesToHash = new byte[nonceData.length + vcode.length( )];
			System.arraycopy( nonceData, 0, bytesToHash, 0, nonceData.length );
			System.arraycopy( vcode.getBytes( ), 0, bytesToHash, nonceData.length, vcode.getBytes( ).length );
				
			MessageDigest digest = MessageDigest.getInstance( "SHA-256" );
			digest.update( bytesToHash );
			return digest.digest( );
		}
		catch (NoSuchAlgorithmException e)
		{	
			return new byte[]{};
		}		
	}
	
	/**
	 * Calculates the hash sha256 data for maintenance login mode 
	 * @param ppn the serial number to use in the hash calculus
	 * @param nonceData to use in the hash
	 * @return the sha256 hash on parameters data 
	 * */
	private byte[] hashMaintenanceNonSecureData(byte[] nonceData, String ppn) throws IOException, NoSuchAlgorithmException
	{
		try
		{
			String password = ppn.substring( 2 ); // the valid ppn on the maintenance login mode is the last 6 characters
			byte[] bytesToHash = new byte[nonceData.length + password.length( )];
			System.arraycopy( nonceData, 0, bytesToHash, 0, nonceData.length );
			System.arraycopy( password.getBytes( ), 0, bytesToHash, nonceData.length, password.getBytes( ).length );
				
			MessageDigest digest = MessageDigest.getInstance( "SHA-256" );
			digest.update( bytesToHash );
			return digest.digest( );
		}
		catch (NoSuchAlgorithmException e)
		{	
			throw e;
		}		
	}
	
	private void ping()
	{
		if(!EventManager.getInstance().send(SpotMessages.getInstance().getPingRequestMessage(SpotMessageType.PING)))
		{
			pingTimer.stop();
		}
	}
	
	public boolean doConnectAndLogin(String ipAddress, int port, String trustKeyFileNamePath, boolean isTlsMode)
	{
		isLoggedIn = false;
		logger.info("trying to connect and login with SPOT "+ipAddress);
		if(EventManager.getInstance().doConnect(new SocketAttributes(ipAddress, port, trustKeyFileNamePath, isTlsMode)))
		{
			isErrorInResponse = false;
			EventManager.getInstance().send(SpotMessages.getInstance().getLoginRequestMessage(SpotMessageType.LOGIN, (byte)0x64, (byte)0x01));
			waitForResponse();
		}
		else
		{
			logger.error("failed to connect and login with SPOT");
		}
		
		return isLoggedIn;
	}
	
	public void doLogout()
	{
		EventManager.getInstance().send(SpotMessages.getInstance().getLogoutRequestMessage(SpotMessageType.LOGOUT));
	}
	
	public void doClose()
	{
		TroubleshootController.getInstance().disconnect();
	}
	
	public boolean doDisconnect()
	{
		pingTimer.stop();
		EventManager.getInstance().doClose();
		EventManager.getInstance().send(SpotMessages.getInstance().getLogoutRequestMessage(SpotMessageType.LOGOUT));
		if(!waitForResponse() && !isErrorInResponse)
		{
			isErrorInResponse = false;
			return true;
		}
		else
		{
			logger.error("response time out - failed to logout");
		}
		return false;
	}
	
	public boolean goToMaintainanceMode(String ppn)
	{
		isErrorInResponse = false;
		nonce = null;
		
		if(isLoggedInMaintenaceMode)
			return true;
		
		logger.info("generating nonce");
		EventManager.getInstance().send(SpotMessages.getInstance().getMaintainanceModeLoginRequestMessage(SpotMessageType.VANGUARD_MAINTENANCE_MODE_LOGIN, MAINTENANCE_GENERATE_NONCE_MODE, null));
		if(!waitForResponse() && !isErrorInResponse)
		{
			try 
			{
				isErrorInResponse = false;
				logger.info("sending non secure maintenance login command");
				EventManager.getInstance().send(SpotMessages.getInstance().getMaintainanceModeLoginRequestMessage(SpotMessageType.VANGUARD_MAINTENANCE_MODE_LOGIN, MAINTENANCE_NON_SECURE_MODE, hashMaintenanceNonSecureData(nonce, ppn)));
				isLoggedInMaintenaceMode = (!waitForResponse() && !isErrorInResponse);
				return isLoggedInMaintenaceMode;
			} 
			catch (NoSuchAlgorithmException e) 
			{
				logger.error(e.getMessage());
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		else
		{
			logger.error("response time out - failed to generate nonce");
		}
		return false;
	}
	
	public boolean goToServiceMenu()
	{
		try
		{
			String ppn = TroubleshootController.spotInfo.getUPMppn();
			
			if(goToMaintainanceMode(ppn))
			{
				isErrorInResponse = false;
				EventManager.getInstance().send(SpotMessages.getInstance().getServiceMenuRequestMessage(SpotMessageType.SERVICE_MENU_SWITCH, SERVICE_MENU_ENABLE));
				if(!waitForResponse() && !isErrorInResponse)
				{
					doClose();
					return true;
				}
			}
			else
			{
				logger.error("failed to login into non secure maintenance");
			}
		}
		catch(Exception e)
		{
			if(e.getMessage() != null)
			{
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		doClose();
		return false;
	}
	
	public void processLoginAnswer(SpotApdu apdu)
	{
		if(apdu.getAck().equals("00"))
		{
			logger.info("connected and logged in to SPOT");
			isLoggedIn = true;
			isErrorInResponse = false;
			pingTimer.start();
			wakeUp();
		}
		else
		{
			isErrorInResponse = true;
			logger.error("error code = "+apdu.getAck());
			logger.error("failed to login");
			wakeUp();
		}
	}
	
	public void processLogoutAnswer(SpotApdu apdu)
	{
		if(apdu.getAck().equals("00"))
		{
			logger.info("logged out from SPOT");
			isLoggedIn = false;
			TroubleshootController.isConnected = false;
			isErrorInResponse = false;
			wakeUp();
		}
		else
		{
			isErrorInResponse = true;
			logger.error("error code = "+apdu.getAck());
			logger.error("failed to logout");
			wakeUp();
		}
	}
	
	public void processMaintainanceLoginModeAnswer(SpotApdu apdu)
	{
		if(apdu.getAck().equals("00"))
		{
			if( !apdu.getData().isEmpty() || !apdu.getData().equals(""))
			{
				nonce = new byte[128];
				EdtConvert.hexStringToBytes(nonce, apdu.getData());
				logger.info("nonce generated : "+apdu.getData());
			}
			isErrorInResponse = false;
			wakeUp();
		}
		else
		{
			logger.error("Maintenance Login Mode : error in response. error code = "+apdu.getAck());
			isErrorInResponse = true;
			wakeUp();
		}
	}
	
	public void processServiceMenuAnswer(SpotApdu apdu)
	{
		if(apdu.getAck().equals("00"))
		{
			isErrorInResponse = false;
			logger.info("switched to service menu successfully");
			wakeUp();
		}
		else
		{
			isErrorInResponse = true;
			logger.error("Service Menu Switch : error in response. error code = "+apdu.getAck());
			wakeUp();
		}
	}
	
	public void processChallengeNumberAnswer(SpotApdu apdu)
	{
		if(apdu.getAck().equals("00"))
		{
			isErrorInResponse = false;
			challengeNumber = apdu.getData().substring(0, 32);
			logger.info("challenge number retrieved : "+EdtConvert.hexToAscii(challengeNumber));
			wakeUp();
		}
		else
		{
			isErrorInResponse = true;
			logger.error("Challenge Number : error in response. error code = "+apdu.getAck());
			wakeUp();
		}
	}
	
	public void processTempActivationAnswer(SpotApdu apdu)
	{
		if(apdu.getAck().equals("00"))
		{
			isErrorInResponse = false;
			logger.info("temporary activation successful");
			wakeUp();
		}
		else
		{
			isErrorInResponse = true;
			logger.error("Temporary Activation : error in response. error code = "+apdu.getAck());
			wakeUp();
		}
	}
	
	public void warmReboot(byte resetType)
	{
		logger.debug( "Sending Reboot command to SPOT ..." );
		EventManager.getInstance().send( SpotMessages.getInstance().getResetRequest(SpotMessageType.RESET, resetType));
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
