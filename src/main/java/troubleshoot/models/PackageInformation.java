package troubleshoot.models;

import java.util.Vector;

import org.apache.log4j.Logger;

import spot.message.models.SpotApdu;
import spot.messages.SpotMessageType;
import spot.messages.SpotMessages;
import troubleshoot.event.handler.EventManager;
import troubleshoot.model.pojo.ROMfsCertificate;
import troubleshoot.util.EdtConvert;

public class PackageInformation
{
	private final static Logger logger = Logger.getLogger(PackageInformation.class);
	private static final int DEFAULT_TIME_OUT = 10000;
	
	private static PackageInformation pkgInformation = null;
	
	private boolean isErrorInResponse;
	private Object monitor;
	private Vector<ApplicationVersion> appVersionsFromSpot	= null;
	
	public PackageInformation() 
	{
		monitor = new Object();
		appVersionsFromSpot = new Vector<ApplicationVersion>( );
	}
	
	public static PackageInformation getInstance()
	{
		if(pkgInformation == null)
		{
			pkgInformation = new PackageInformation();
		}
		return pkgInformation;
	}

	public Vector<ApplicationVersion> getAppVersionsFromSpot()
	{
		return appVersionsFromSpot;
	}

	public void setAppVersionsFromSpot(Vector<ApplicationVersion> appVersionsFromSpot)
	{
		this.appVersionsFromSpot = appVersionsFromSpot;
	}
	
	private void addUnitAppToVerify(String appName, String appVersion, String appBuildNumber)
	{
		ApplicationVersion application = new ApplicationVersion( appName, appVersion, appBuildNumber);
		appVersionsFromSpot.add( application );
	}
	
	public boolean collectPkgInfoFromUPM() throws InterruptedException
	{
		logger.info("collect pkg info - START" );
		isErrorInResponse = false;
		appVersionsFromSpot.clear();

		sendPackageInfoRequest();
		if(!waitForResponse() && !isErrorInResponse)
		{
			logger.info("collect pkg info - END - ");
			return true;
		}
		else
		{
			logger.info("Timeout or Error in response");
			logger.info("collect pkg info - END - ");
			return false;
		}
	}
	
	private void sendPackageInfoRequest()
	{
		EventManager.getInstance().send(SpotMessages.getInstance().getPackageInfoRequestMessage(SpotMessageType.VANGUARD_ROMfs_PACKAGE_INFO, (byte)0x00));
	}
	
	public void processPackageInfoAnswer(SpotApdu apdu)
	{
		if(apdu.getAck().equals("00"))
		{
			int index = 0;
			int numberOfPackages = Integer.valueOf(apdu.getData().substring(0, 2), 16);
			index = 2;
			for(int i=0; i<numberOfPackages; i++)
			{
				int length = Integer.valueOf(apdu.getData().substring(index, index+4), 16);
				index += 4;
				String data = apdu.getData().substring(index, (index + (length*2)) );
				index += (length*2);
				
				ROMfsCertificate certificate = new VanguardROMfsCertificate(EdtConvert.hexStringToByteArray(data));
				if (certificate.loadCertificate( true ))
				{
					addUnitAppToVerify( new String( certificate.getAppName( ) ), new String( certificate.getAppVersion( ) ), new String(certificate.getAppBuildNumber()) );
				}
				else
				{
					if(certificate.getAppName()!=null)
					{
						addUnitAppToVerify( new String( certificate.getAppName( ) ), new String( certificate.getAppVersion( ) ) , new String(certificate.getAppBuildNumber()) );
					}
				}
			}
			isErrorInResponse = false;
		}
		else
		{
			isErrorInResponse = true;
			logger.error("Package Info : error in response. error code = "+apdu.getAck() );
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
	
	/**
	 * The Class ApplicationVersion.
	 */
	public class ApplicationVersion
	{

		/** The app id. */
		private String appId;

		/** The version. */
		private String version;
		
		/** The build number. */
		private String buildNumber;

		/**
		 * Instantiates a new application version.
		 * 
		 * @param appId
		 *            the app id
		 * @param version
		 *            the version
		 * @param buildNumber
		 *            the buildNumber
		 */
		public ApplicationVersion(String appId, String version , String buildNumber)
		{
			this.appId = appId;
			this.version = version;
			this.buildNumber=buildNumber;
		}

		/**
		 * Gets the app id.
		 * 
		 * @return the app id
		 */
		public String getAppId()
		{
			return appId;
		}

		/**
		 * Gets the version.
		 * 
		 * @return the version
		 */
		public String getVersion()
		{
			return version;
		}
		
		/**
		 * Gets the buildNumber.
		 * 
		 * @return the buildNumber
		 */
		public String getBuildNumber()
		{
			return buildNumber;
		}
	}
}
