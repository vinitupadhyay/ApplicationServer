package troubleshoot.file.managment.api;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.log4j.Logger;

public class FileResourceWriter 
{
	private final static Logger logger = Logger.getLogger(FileResourceWriter.class);
	private static FileResourceWriter fileResourceWriter = null;
	
	private OutputStream outputStream;
	
	public FileResourceWriter()
	{
	
	}
	
	public static FileResourceWriter getInstance()
	{
		if(fileResourceWriter == null)
		{
			fileResourceWriter = new FileResourceWriter();
		}
		return fileResourceWriter;
	}

	public boolean open(String filePath)
	{
		boolean bResult = false;
		if (outputStream != null)
		{
			try
			{
				outputStream.close();
			}
			catch (IOException e)
			{
				logger.error("Error while closing unexpected old outputStream. " + e.getMessage( ));
			}
			outputStream = null;
		}
		try
		{
			outputStream = new BufferedOutputStream(Files.newOutputStream(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING), 524288); // Buffer size to hold before flushing is 512kb.
			bResult = true;
		}
		catch (IOException e)
		{
			logger.error("File " + filePath + " could not be created. " + e.getMessage( ));
		}
		return bResult;
	}

	public boolean write(byte[] data)
	{
		boolean bResult = false;
		try
		{
			outputStream.write(data);
			bResult = true;
		}
		catch (IOException e)
		{
			outputStream = null;
			logger.error("Buffer could not be wrote. " + e.getMessage( ));
		}
		return bResult;
	}

	public boolean close()
	{
		boolean bResult = false;
		if (outputStream != null)
		{
			try
			{
				outputStream.close();
				outputStream = null;
			}
			catch (IOException e)
			{
				logger.error("File could not be properly closed. " + e.getMessage( ));
				outputStream = null;
			}

			Path closedFileToCheck = Paths.get("");
			try
			{
				if (Files.size(closedFileToCheck) > 0)
				{
					
					logger.info("File has been saved.");								
					bResult = true;
				}
				else
				{
				    Files.delete(closedFileToCheck);
					logger.info("File has been reported but no content is stored. File removed.");								
				}
			}
			catch (NoSuchFileException e)
			{
				logger.error("File could not be properly checked. File not exists. " + e.getMessage( ));
			}
			catch (DirectoryNotEmptyException e)
			{
				logger.error("File could not be properly checked. Directory conflict. " + e.getMessage( ));
			}
			catch (IOException e)
			{
				logger.error("File could not be properly checked. " + e.getMessage( ));
			}
			catch (SecurityException e)
			{
				logger.error("File could not be properly accessed for check. " + e.getMessage( ));
			}
		}
		return bResult;
	}
}
