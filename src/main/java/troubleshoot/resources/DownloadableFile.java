/**
 * © 2012, 2014 Gilbarco Inc.
 * Confidential and Proprietary
 *
 * @file ResourcesEngine.java 
 * @author stomasini
 * @date 13 Feb 2012
 * @copyright © 2012, 2014 Gilbarco Inc. Confidential and Proprietary
 **/

package troubleshoot.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.log4j.Logger;

/**
 * It is the base class for files to be downloaded to SPOT
 * It contains a File reference inside (not by inheritance).
 * 
 * @author gparis
 *
 */
public class DownloadableFile
{
	// ******************************************************************
	// STATIC FIELDS 
	// ******************************************************************

	protected static final int	MAX_FILEBLOCK_LENGTH      = 	65529;    // maximum file block length.
	
	/** The Constant logger. */
	private final static Logger logger = Logger.getLogger(DownloadableFile.class);

	// ******************************************************************
	// PROTECTED FIELDS.
	// ******************************************************************
	protected File			file = null;
	protected boolean		blError = false;
	protected boolean		blEOF = false;
	protected byte[]		abFileData = null;
	
	protected String resourceRelativePath = null;
	protected boolean isInputStream = false;
	
	// ******************************************************************
	// PRIVATE FIELDS.
	// ******************************************************************

	// ******************************************************************
	// CONSTRUCTORS.
	// ******************************************************************

	/**
	 * @param pathname
	 */
	public DownloadableFile(String pathname)
	{
		file = new File(pathname);
	}

	/**
	 * @param parent
	 * @param child
	 */
	public DownloadableFile(String parent, String child)
	{
		file = new File(parent, child);
	}

	/**
	 * @param parent
	 * @param child
	 */
	public DownloadableFile(File parent, String child)
	{
		file = new File(parent, child);
	}

	/**
	 * @param file : a pre-existent file object
	 */
	public DownloadableFile(File file)
	{
		this.file = file;
	}
	
	/**
	 * @param resourceRelativePath : the relative file path
	 * @param isInputStream : boolean whether input stream
	 */
	public DownloadableFile(String resourceRelativePath, boolean isInputStream)
	{
		this.resourceRelativePath = resourceRelativePath;
		this.isInputStream = isInputStream;
	}

	// ******************************************************************
	// PUBLIC METHODS	  (general, getter, setter, interface imp)
	// ******************************************************************

	/** Gets the file error status
	 * @return true if the reading file has an error, false in another case.*/
	public boolean hasError()
	{
		return blError;
	}

	/** Gets the file end of file status
	 * @return true if the reading is on eof, false in another case.*/
	public boolean eof()
	{
		return blEOF;
	}

	/** Gets the file length
	 * @return the file length, -1 if the file is null*/
	public long length()
	{
		try
		{
			if(isInputStream)
			{
				InputStream in = getInputStream();
				if(in == null)
				{
					return -1;
				}
				return in.available();
			}
			else
			{
				if(file == null)
				{
					return -1;
				}
				return file.length();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		
		return -1;
	}

	/** Gets the file name
	 * @return the file name, empty string if the file is null*/
	public String getName()
	{
		if(isInputStream)
		{
			String[] names = resourceRelativePath.split("/");
			if(names.length > 1)
			{
				return names[1];
			}
		}
		else
		{
			if(file == null)
			{
				return "";
			}
			return file.getName();
		}
		
		return "";
	}

	/** Gets the file to download*/
	public File getFile()
	{
		return file;
	}
	
	/** Gets the inputStream to download*/
	public InputStream getInputStream()
	{
		ClassLoader classLoader = getClass().getClassLoader();
		return classLoader.getResourceAsStream(resourceRelativePath);
	}

	/**
	 * Gets the next data block to send to the unit 
	 * @param offset, the block offset. 
	 * @param blockLength, the maximum expected length for the block.
	 * @return the byte array for the data block*/
	public byte[] getNextBlock(int offset, int blockLength)
	{
		if( offset >= this.length() )
		{
			blEOF = true;
			return null; // no more data
		}

		int nread = blockLength;
		if( this.length() < offset + blockLength ) // This is last block to read
		{
			nread = (int) this.length() - offset;   // so nread is adjusted.
		}
		
		if(abFileData == null)
		{
			if(getData() == null)
			{
				return null;
			}
		}

		return Arrays.copyOfRange(abFileData, offset, offset + nread);
	}

	/**
	 * Gets the next data block to send to the unit 
	 * @param offset, the block offset. 
	 * @param blockLength, the maximum expected length for the block.
	 * @return the byte array for the data block
	*/
	public byte[] getNextBlocks(int offset, int blockLength)
	{
		if( offset >= this.length() )
		{
			blEOF = true;
			return null; // no more data
		}

		int nread = blockLength;
		if( this.length() < offset + blockLength ) // This is last block to read
		{
			nread = (int) this.length() - offset;   // so nread is adjusted.
		}
		
		if(abFileData == null)
		{
			if(getData() == null)
			{
				return null;
			}
		}

		return Arrays.copyOfRange(abFileData, offset, offset + nread);
	}
	/**
	 * Gets the complete data from the file
	 * @return the complete data from the file
	 * */
	public byte[] getData()
	{
		if(abFileData != null)
		{
			return abFileData;
		}
		
		synchronized(this)
		{
			if(abFileData != null)
			{
				return abFileData;
			}

			int nread = 0;
			InputStream fis = null;

			try
			{
				if(isInputStream)
				{
					fis = getInputStream();
					abFileData = new byte[(int) this.length()];
					nread = fis.read(abFileData);
					if(nread < this.length())
					{
						String sMsg = String.format("Only %d bytes were read from resource having length %d.", nread, this.length());
						Arrays.fill(abFileData, nread, (int) (this.length()), (byte) 0);
						logger.error(sMsg);
						blError = true;
					}
				}
				else
				{
					fis = new FileInputStream(file);
					abFileData = new byte[(int) this.length()];
					nread = fis.read(abFileData);
					if(nread < this.length())
					{
						String sMsg = String.format("Only %d bytes were read from resource having length %d.", nread, this.length());
						Arrays.fill(abFileData, nread, (int) (this.length()), (byte) 0);
						logger.error(sMsg);
						blError = true;
					}
				}
			}
			catch (FileNotFoundException e)
			{
				logger.error(e.getMessage(), e);
				abFileData = null;
				blError = true;
				return null;
			}
			catch (IOException e)
			{
				logger.error(e.getMessage(), e);
				abFileData = null;
				blError = true;
			}

			try
			{
				if(fis != null)
				{
					fis.close();
				}
			}
			catch (IOException e)
			{
				logger.error(e.getMessage(), e);
			}
			return (blError ? null : abFileData);
		}
	}

	// ******************************************************************
	// PROTECTED METHODS.
	// ******************************************************************

	// ******************************************************************
	// PRIVATE METHODS.
	// ******************************************************************

	// ******************************************************************
	// STATIC METHODS 
	// ******************************************************************

	// ******************************************************************
	// MAIN.
	// ******************************************************************

}
