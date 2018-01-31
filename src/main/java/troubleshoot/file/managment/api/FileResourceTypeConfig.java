/**
 * Copyright © 2014 Gilbarco Inc.
 * Confidential and Proprietary.
 *
 * @file FileResourceTypeConfig.java
 * @author mgrieco
 * @date Monday, June 02, 2014
 * @copyright Copyright © 2014 Gilbarco Inc. Confidential and Proprietary.
 */

package troubleshoot.file.managment.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import troubleshoot.exceptions.BadResourceFilenameException;

/**
 * @class FileResourceTypeConfig.java "com.gilbarco.globaltools.flexpay.maintenance.gui"
 * @brief This class implements the logic to configured the resources dynamically.
 * @see FlexPayMaintenanceController, FileResourceType
 * @since 1.0.0
 */
public class FileResourceTypeConfig
{
	/**
	 * @brief Default constructor
	 * @param the filename for resource type configuration
	 * @since 1.0.0
	 */
	// ******************************************************************
	// STATIC FIELDS
	// ******************************************************************
	/** The Constant logger. */
	private final static Logger logger = Logger.getLogger(FileResourceTypeConfig.class);

	/** Default value for hexadecimal radix */
	public static final int					HEX_RADIX						= 16;

	/** Default value for folder name separator */
	public static final String				DEFAULT_FOLDERNAME_SEPARATOR	= "_";
	/** Default value for folder path separator on config file */
	public static final String				DEFAULT_FOLDERPATH_SEPARATOR	= "\\";

	// ******************************************************************
	// PRIVATED FIELDS
	// ******************************************************************
	
	/** The loaded file properties*/
	private Properties	properties;

	/** The resources FileResourceTypeConfig instance */
	private static FileResourceTypeConfig	fileResourceTypeConfig	= null;

	// ******************************************************************
	// CONSTRUCTOR.
	// ******************************************************************
	/**
	 * FileResourceTypeConfig constructor
	 * @param filename the configuration file name to config the resources.
	 */
	private FileResourceTypeConfig(String filename)
	{
		// Parse properties.
		properties = new Properties( );
		try
		{
			properties.load( new FileInputStream( filename ) );
			for (String keyId : properties.stringPropertyNames( ))
			{
				logger.debug( "type id:" + keyId + " - type name:" + properties.getProperty( keyId ) );				
			}
		}
		catch (IOException e)
		{
			logger.error( "Exception Occurred" + e.getMessage( ) );
		}
	}

	/**
	 * Instantiates a FileResourceTypeConfig.
	 */
	public static FileResourceTypeConfig getInstance()
	{
		if(fileResourceTypeConfig != null)
		{
			return fileResourceTypeConfig;
		}
		else
		{
			return fileResourceTypeConfig = new FileResourceTypeConfig("");
		}
	}

	/**
	 * @brief Gets the file resource type (folder) name corresponding to a type id.
	 * @param the resource type id 
	 * @return the resource name for the type id
	 * @since 1.0.0
	 */
	public String getNameById(byte typeId)
	{
		String valueOf = String.format("%02X", typeId&0xff);
		return properties.getProperty( valueOf );		
	}

	/**
	 * @brief Gets the type id for a resource type name
	 * @param name, the file resource (folder) name
	 * @return the resource type id
	 * @throws BadResourceFilenameException 
	 * @since 1.0.0
	 */
	public byte getIdByName(String name) throws BadResourceFilenameException
	{
		for (String keyId : properties.stringPropertyNames( ))
		{
			try
			{
				String typeName = properties.getProperty( keyId ).replace( DEFAULT_FOLDERPATH_SEPARATOR, DEFAULT_FOLDERNAME_SEPARATOR);
				if (typeName.equalsIgnoreCase( name ))
				{
					return Byte.valueOf( keyId, HEX_RADIX ); 
				}
			}
			catch (NumberFormatException e)
			{
				logger.error( "getIdByName - Invalid type id on configurationfile - " + keyId + " - " + e.getMessage( ) );
			}
		}
		throw new BadResourceFilenameException("Invalid type id on configurationfile - " + name);
	}
	
	/**
	 * Gets the resources type from a given file path
	 * @param fullFilePath the file path to get the type 
	 * @return the resource type id
	 * @throws BadResourceFilenameException 
	 * */
	public byte getResourceTypeFromFilePath(String fullFilePath) throws BadResourceFilenameException
	{
		String path = fullFilePath.toLowerCase().replace( DEFAULT_FOLDERPATH_SEPARATOR, DEFAULT_FOLDERNAME_SEPARATOR);		
		for (String keyId : properties.stringPropertyNames( ))
		{
			try
			{
				String strkeyId = properties.getProperty( keyId ).toLowerCase( ).replace( DEFAULT_FOLDERPATH_SEPARATOR, DEFAULT_FOLDERNAME_SEPARATOR);
				if (path.indexOf( strkeyId ) >=0)
				{
					return Byte.valueOf( keyId, HEX_RADIX ); 
				}
			}
			catch (NumberFormatException e)
			{
				logger.error( "getIdByName - Invalid type id on configurationfile - " + keyId + " - " + e.getMessage( ) );
				throw new BadResourceFilenameException("Invalid type id on configurationfile - " + keyId + " - " + e.getMessage( ));
			}
		}
		throw new BadResourceFilenameException("Invalid type id on configurationfile - " + fullFilePath);
	}
	
	/**
	 * Gets all the configured resources types
	 * @return a vector with all configured resources types
	 * */
	public FileResourceType[] values()
	{
		FileResourceType[] types = new FileResourceType[properties.size( )];
		int i =0;
		for (String keyId : properties.stringPropertyNames( ))
		{
			try
			{			
				byte id= Byte.valueOf( keyId, HEX_RADIX );
				String name = properties.getProperty( keyId ).toLowerCase( ).replace( DEFAULT_FOLDERPATH_SEPARATOR, DEFAULT_FOLDERNAME_SEPARATOR );				
				types[i] = new FileResourceType(id, name);
				++i;
			}
			catch (NumberFormatException e)
			{
				logger.error( "getIdByName - Invalid type id on configurationfile - " + keyId + " - " + e.getMessage( ) );
			}
		}		
		return types;
	}
}
