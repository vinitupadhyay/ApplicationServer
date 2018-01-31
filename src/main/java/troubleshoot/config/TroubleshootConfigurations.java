package troubleshoot.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;

import troubleshoot.controller.TroubleshootController;

public class TroubleshootConfigurations
{
	private final static Logger logger = Logger.getLogger(TroubleshootConfigurations.class);
	
	/** @brief the default value for resources configuration file name */
	private static final String DEFAULT_UPM_STANDARD_LOGS_CONFIG_FILE_NAME = "spot_upm_logs.properties";
	private static final String	CHECK_UPM_LOGS_CONFIG_FILE_NAME	= "check_upm_logs.properties";
	private static final String	RESOURCES_DIRECTORY	= "Resources\\";
	private static final String	REPORTS_DIRECTORY	= "Reports\\";
	
	private static TroubleshootConfigurations troubleshootConfigurations = null;
	private static String installationDirectory = "";
	private static String certificatePath = "";
	
	private String upmIp = "";
	private String upmPort = "";
	private String gsomPort = "";
	private String actServerIp = "";
	private String actServerPort = "";
	private String upmLogsFolder = "";
	private boolean isBorderPainted = false;
	
	static
	{
		try
		{
			installationDirectory = TroubleshootConfigurations.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			System.out.println(installationDirectory);
			installationDirectory = installationDirectory.substring(0, installationDirectory.lastIndexOf("/") + 1);
			System.out.println(installationDirectory);
			certificatePath = "Certificates\\upmTrustStore.jks";
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
	}
	
	public TroubleshootConfigurations()
	{
		loadProperties();
	}
	
	public static TroubleshootConfigurations getInstance()
	{
		if(troubleshootConfigurations == null)
		{
			troubleshootConfigurations = new TroubleshootConfigurations();
		}
		return troubleshootConfigurations;
	}
	
	public void loadProperties()
	{
		try 
		{
			Ini ConfigFile = new Ini(new FileReader(installationDirectory + "app.properties"));
			Section connectionSection = ConfigFile.get("connection");
			upmIp = connectionSection.get("upmIpAddress");
			upmPort = connectionSection.get("upmPort");
			gsomPort = connectionSection.get("gsomPort");
			actServerIp = connectionSection.get("actServerIp");
			actServerPort = connectionSection.get("actServerPort");
			
			Section settingsSection = ConfigFile.get("settings");
			upmLogsFolder = settingsSection.get("upm_logs_folder");
			
			Section guiSection = ConfigFile.get("GUI");
			String isPainted = guiSection.get("isBorderPainted");
			if(isPainted != null)
			{
				isBorderPainted = Boolean.parseBoolean(isPainted);
			}
			
			setUPMIp();
			
		} 
		catch (InvalidFileFormatException e) 
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	
	public static String getInstallationDirectory() {
		return installationDirectory;
	}

	public static String getCertificatePath() {
		return installationDirectory + certificatePath;
	}
	
	public String getUpmLogsFolder()
	{
		return upmLogsFolder;
	}
	
	public void setUPMIp()
	{
		if(upmIp != null && !upmIp.isEmpty())
		{
			if(TroubleshootController.spotInfo.getUPMip().isEmpty())
			{
				TroubleshootController.spotInfo.setUPMip(upmIp);
			}
		}
	}
	
	public void updateLastUsedIp(String ip)
	{
		upmIp = ip;
		try
		{
			Ini props = new Ini();
			FileInputStream in = new FileInputStream(installationDirectory + "app.properties");
			props.load(in);
			in.close();
			
			FileOutputStream out = new FileOutputStream(installationDirectory + "app.properties");
			props.put("connection", "upmIpAddress", upmIp);
			props.store(out);
			out.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (InvalidFileFormatException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean getIsBorderPainted() {
		return isBorderPainted;
	}
	
	public String getUPMIp() {
		return upmIp;
	}

	public String getUPMport() {
		return upmPort;
	}

	public String getGSOMport() {
		return gsomPort;
	}

	public String getActServerIp() {
		return actServerIp;
	}

	public String getActServerPort() {
		return actServerPort;
	}
	
	/**
	 * @brief Gets the UPM standard logs configuration file property value
	 * @return the name for the UPM standard logs configuration file.
	 */
	public static String getDefaultUPMStandardLogsConfigFile()
	{
		return installationDirectory + DEFAULT_UPM_STANDARD_LOGS_CONFIG_FILE_NAME;
	}
	
	/**
	 * @brief Gets the resources configuration file property value
	 * @return the name for the resources configuration file.
	 */
	public static String getCheckUPMLogsConfigFile()
	{		
		return installationDirectory + CHECK_UPM_LOGS_CONFIG_FILE_NAME;
	}
	
	public static String getResourcesDirectory()
	{
		return installationDirectory + RESOURCES_DIRECTORY;
	}
	
	public static String getRollCallFilePath()
	{
		return ".\\RollCall_info.txt";
	}
	
	public static String getPrinterResourceFilePath()
	{
		return installationDirectory + RESOURCES_DIRECTORY + "\\PrinterRes\\Printer_test.xml";
	}
	
	public static String getReportsDirectoryPath()
	{
		return installationDirectory + REPORTS_DIRECTORY;
	}
}

