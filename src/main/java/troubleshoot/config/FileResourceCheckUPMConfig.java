package troubleshoot.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import troubleshoot.model.pojo.Resource;
import troubleshoot.util.EdtConvert;

public class FileResourceCheckUPMConfig
{
	private final static Logger logger = Logger.getLogger(TroubleshootConfigurations.class);
	/** Default type id position on configuration file */
	private static final int TYPE_ID_POSSITION = 0;

	/** Default file id position on configuration file */
	private static final int FILE_ID_POSSITION = 1;
	
	// ******************************************************************
	// PRIVATED FIELDS
	// ******************************************************************
	
	/** The FileResourceLogStandardConfig instance */
	private static FileResourceCheckUPMConfig chechUPMConfig	= null;

	/** The loaded file properties */
	private Properties checkUpmLogsProperties;

	// ******************************************************************
	// CONSTRUCTOR.
	// ******************************************************************
	/**
	 * FileResourceLogStandardConfig constructor
	 */
	private FileResourceCheckUPMConfig()
	{
		// Parse properties.
		checkUpmLogsProperties = new Properties( );
		try
		{
			String filename = TroubleshootConfigurations.getCheckUPMLogsConfigFile();
			checkUpmLogsProperties.load( new FileInputStream( filename ) );
		}
		catch (IOException e)
		{
			logger.error( "Exception Occurred" + e.getMessage( ) );
		}
	}

	/**
	 * Instantiates a FileResourceLogStandardConfig.
	 */
	public static FileResourceCheckUPMConfig getInstance()
	{
		if(chechUPMConfig != null)
		{
			return chechUPMConfig;
		}
		else
		{
			return chechUPMConfig = new FileResourceCheckUPMConfig();
		}
	}
	
	
	/**
	 * Gets all the configured current logs types for vgd upm
	 * @return a vector with all configured current logs types
	 * */
	public Resource[] allUpmValues(String sessionDirectory)
	{
		return allValues( checkUpmLogsProperties ,sessionDirectory);
	}

	
	
	/**
	 * Gets all the configured current logs types for the given properties
	 * @return a vector with the configured current logs types
	 * */

	private Resource[] allValues(Properties logsProp , String sessionDirectory)
	{
		int NumOfCurrentLogs = logsProp.size();
		Resource[] types = new Resource[NumOfCurrentLogs];
		int i =0;
		for (String keyId : logsProp.stringPropertyNames( ))
		{
			try
			{
				String propertyData = logsProp.getProperty( keyId );
				String[] data = propertyData.split( "\\." );
				
				if(keyId.contains("HIST"))
				{
					types[i] = new Resource(keyId, (byte)EdtConvert.stringToInt(data[TYPE_ID_POSSITION]), (byte) EdtConvert.stringToInt(data[FILE_ID_POSSITION]),sessionDirectory,"");
				}
				else
				{
					types[i] = new Resource(keyId, (byte)EdtConvert.stringToInt(data[TYPE_ID_POSSITION]), (byte) EdtConvert.stringToInt(data[FILE_ID_POSSITION]),sessionDirectory,".log");
				}
				++i;
			}
			catch (NumberFormatException e)
			{
				logger.error( "upmValues - Invalid values on configuration file - " + keyId + " - " + e.getMessage( ) );
			}
		}		
		return types;
	}
}
