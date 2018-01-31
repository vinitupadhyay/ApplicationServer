package troubleshoot.file.managment.api;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.log4j.Logger;

import troubleshoot.util.EdtCompressUtil;
import troubleshoot.util.EdtFileUtil;

public class FileCompression
{
	private final static Logger logger = Logger.getLogger(FileCompression.class);
	
	/** Default message to identify uncompressing text file exception */
	private static final String			ERROR_DETECTED_PARSING_THE_HEADER	= "Error detected parsing the header";
	private static FileCompression fileCompression = null;
	
	public FileCompression()
	{
	
	}
	
	public static FileCompression getInstance()
	{
		if(fileCompression == null)
		{
			fileCompression = new FileCompression();
		}
		return fileCompression;
	}
	
	public void uncompress(File currFile, File DestDirFolder)
	{
		List<File> untaredFiles = new LinkedList<File>( );
		
		try
		{
			System.out.println("Uncompressing File :"+ currFile.getName());
			File tarfile = EdtCompressUtil.unGzip( currFile, DestDirFolder);
			untaredFiles.addAll(EdtCompressUtil.unTar( tarfile, DestDirFolder ) );
			EdtFileUtil.delete( currFile, true );
			EdtFileUtil.delete( tarfile, true );
		}
		catch (IOException | ArchiveException e)
		{
			try
			{
				untaredFiles.addAll( EdtCompressUtil.unTar( currFile, DestDirFolder ) );
				EdtFileUtil.delete( currFile, true );
			}
			catch (NullPointerException f) 
			{
				logger.error( currFile.getName( ) + " - " + e.getMessage( ) );
				EdtFileUtil.delete( currFile, true );
			}
			catch (ArchiveException f)
			{
				try
				{
					EdtFileUtil.copyFile( new File( DestDirFolder.getCanonicalPath( ) + "\\" + currFile.getName( ) ), currFile ); 
				}
				catch (IOException g)
				{
					logger.error( currFile.getName( ) + " - " + e.getMessage( ) );
					EdtFileUtil.delete( currFile, true );
				}				
			}
			catch (IOException f)
			{
				try
				{
					if(f.getMessage( ).equals( ERROR_DETECTED_PARSING_THE_HEADER ))//exception when the header is not as expected.
					{
						EdtFileUtil.copyFile( new File( DestDirFolder.getCanonicalPath( ) + "\\" + currFile.getName( ) + ".log"), currFile );
					}
				}
				catch (IOException g)
				{
					logger.error( currFile.getName( ) + " - " + e.getMessage( ) );
				}	
			}
		}
	}
	
	public void unGZipFiles(File destinationFolder)
	{
		try
		{
			if(destinationFolder.isDirectory())
			{
				File [] logfiles = destinationFolder.listFiles();
				for (File file : logfiles) 
				{
					if(file.isDirectory())
					{
						unGZipFiles(file);
					}
					else
					{
						try
						{
							EdtCompressUtil.unGzip( file, destinationFolder);
							EdtFileUtil.delete( file, true );
						}
						catch(IOException e)
						{
							e.printStackTrace();
							logger.error(e.getMessage());
						}
					}
				}
			}
			else if(destinationFolder.isFile())
			{
				try
				{
					EdtCompressUtil.unGzip( destinationFolder, destinationFolder.getParentFile());
					EdtFileUtil.delete( destinationFolder, true );
				}
				catch(IOException e)
				{
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
}
