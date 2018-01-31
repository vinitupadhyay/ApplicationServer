package troubleshoot.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import troubleshoot.model.pojo.Resource;
import troubleshoot.util.EdtConvert;

public class FileResourceLogStandardConfig 
{
	private final static Logger logger = Logger.getLogger(TroubleshootConfigurations.class);
	/** Default type id position on configuration file */
	private static final int						TYPE_ID_POSSITION		= 0;

	/** Default file id position on configuration file */
	private static final int						FILE_ID_POSSITION		= 1;
	
	// ******************************************************************
	// PRIVATED FIELDS
	// ******************************************************************
	
	/** The FileResourceLogStandardConfig instance */
	private static FileResourceLogStandardConfig	standardLogsConfig	= null;

	/** The loaded file properties */
	private Properties								upmLogsProperties;

	// ******************************************************************
	// CONSTRUCTOR.
	// ******************************************************************
	/**
	 * FileResourceLogStandardConfig constructor
	 */
	private FileResourceLogStandardConfig()
	{
		// Parse properties.
		upmLogsProperties = new Properties( );
		try
		{
			String filename = TroubleshootConfigurations.getDefaultUPMStandardLogsConfigFile( );
			upmLogsProperties.load( new FileInputStream( filename ) );
		}
		catch (IOException e)
		{
			logger.error( "Exception Occurred" + e.getMessage( ) );
		}
	}

	/**
	 * Instantiates a FileResourceLogStandardConfig.
	 */
	public static FileResourceLogStandardConfig getInstance()
	{
		if(standardLogsConfig != null)
		{
			return standardLogsConfig;
		}
		else
		{
			return standardLogsConfig = new FileResourceLogStandardConfig();
		}
	}
	
	
	/**
	 * Gets all the configured current logs types for vgd upm
	 * @return a vector with all configured current logs types
	 * */
	public Resource[] upmCurrentValues(String sessionDirectory)
	{
		return currentValues( upmLogsProperties ,sessionDirectory);
	}

	
	
	/**
	 * Gets all the configured current logs types for the given properties
	 * @return a vector with the configured current logs types
	 * */

	private Resource[] currentValues(Properties logsProp , String sessionDirectory)
	{
		int NumOfCurrentLogs = getCurrentLogsCount(logsProp);
		Resource[] types = new Resource[NumOfCurrentLogs];
		int i =0;
		for (String keyId : logsProp.stringPropertyNames( ))
		{
			try
			{			
				if(!keyId.contains("HIST"))
				{
					String propertyData = logsProp.getProperty( keyId );
					String[] data = propertyData.split( "\\." );
					types[i] = new Resource(keyId, (byte)EdtConvert.stringToInt(data[TYPE_ID_POSSITION]), (byte) EdtConvert.stringToInt(data[FILE_ID_POSSITION]),sessionDirectory,".log");
					++i;
				}
			}
			catch (NumberFormatException e)
			{
				logger.error( "upmValues - Invalid values on configuration file - " + keyId + " - " + e.getMessage( ) );
			}
		}		
		return types;
	}
	
	/**
	 * Gets all the configured logs types for the given properties
	 * @return an integer with the number of current logs present in the configuration file 
	 * */

	private int getCurrentLogsCount(Properties logsProp)
	{
		int count = 0;
		for (String keyId : logsProp.stringPropertyNames( ))
		{	
			if(!keyId.contains("HIST"))
			{
				count++;
			}
		}
		return count;
	}
}
