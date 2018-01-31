package troubleshoot.file.managment.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class FileResourceReader 
{
	// ******************************************************************
	// STATIC FIELDS 
	// ******************************************************************

	protected static final int	MAX_FILEBLOCK_LENGTH = 65529;    // maximum file block length.
	private final static Logger logger = Logger.getLogger(FileResourceReader.class);

	// ******************************************************************
	// PROTECTED FIELDS.
	// ******************************************************************
	protected File			file = null;
	protected boolean		blError = false;
	protected boolean		blEOF = false;
	protected byte[]		abFileData = null;
	
	// ******************************************************************
	// PRIVATE FIELDS.
	// ******************************************************************

	// ******************************************************************
	// CONSTRUCTORS.
	// ******************************************************************

	/**
	 * @param pathname
	 */
	public FileResourceReader(String pathname)
	{
		file = new File(pathname);
	}

	/**
	 * @param parent
	 * @param child
	 */
	public FileResourceReader(String parent, String child)
	{
		file = new File(parent, child);
	}

	/**
	 * @param parent
	 * @param child
	 */
	public FileResourceReader(File parent, String child)
	{
		file = new File(parent, child);
	}

	/**
	 * @param file : a pre-existent file object
	 */
	public FileResourceReader(File file)
	{
		this.file = file;
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
		if(file == null)
		{
			return -1;
		}

		return file.length();
	}

	/** Gets the file name
	 * @return the file name, empty string if the file is null*/
	public String getName()
	{
		if(file == null)
		{
			return "";
		}
		return file.getName();
	}

	/** Gets the file to download*/
	public File getFile()
	{
		return file;
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
		if( offset >= this.fullLength() )
		{
			blEOF = true;
			return null; // no more data
		}

		int nread = blockLength;
		if( this.fullLength() < offset + blockLength ) // This is last block to read
		{
			nread = (int) this.fullLength() - offset;   // so nread is adjusted.
		}
		
		if(abFileData == null)
		{
			if(getFullData() == null)
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
			FileInputStream fis = null;

			try
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
				fis.close();
			}
			catch (IOException e)
			{
				logger.error(e.getMessage(), e);
			}
			return (blError ? null : abFileData);
		}
	}

	/**
	 * Gets complete data from the file
	 * @return complete data from the file
	 * */
	public byte[] getFullData()
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
			FileInputStream fis = null;

			try
			{
				fis = new FileInputStream(file);
				abFileData = new byte[(int) this.fullLength()];
				nread = fis.read(abFileData);
				if(nread < this.fullLength())
				{
					String sMsg = String.format("Only %d bytes were read from resource having length %d.", nread, this.fullLength());
					Arrays.fill(abFileData, nread, (int) (this.fullLength()), (byte) 0);
					logger.error(sMsg);
					blError = true;
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
				fis.close();
			}
			catch (IOException e)
			{
				logger.error(e.getMessage(), e);
			}
			return (blError ? null : abFileData);
		}
	}

	/**
	 * Gets complete data length from the file
	 * @return complete data length from the file
	 * */
	public long fullLength() 
	{
		if(file == null)
			{
				return -1;
			}
		return file.length();
	}
}
