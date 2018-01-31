/**
 * © 2014 Gilbarco Inc.
 * Confidential and Proprietary
 *
 * @file EdtFileUtil.java
 * @copyright © 2010, 2011, 2012, 2013, 2014 Gilbarco Inc. Confidential and Proprietary
 *
 */

package troubleshoot.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class EdtFileUtil 
{
	// ******************************************************************
	// PUBLIC FIELDS.
	// ******************************************************************

	// ******************************************************************
	// PROTECTED FIELDS.
	// ******************************************************************

	// ******************************************************************
	// PRIVATE FIELDS.
	// ******************************************************************

	// ******************************************************************
	// CONSTRUCTOR.
	// ******************************************************************

	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	/**
	 * Create. <p>
	 */
	private EdtFileUtil()
	{
	}

	// ******************************************************************
	// FINALIZER.
	// ******************************************************************

	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	public void finalize() throws Throwable
	{
	}

	// ******************************************************************
	// PUBLIC METHODS	   (general, getter, setter, interface imp)
	// ******************************************************************

	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	/**
	 * Delete file/dir. <p>
	 *
	 * @param file File/dir to delete.
	 * @param recursiveDirDelete If true will do recursive dir delete.
	 * @return true if delete successful, otherwise false.
	 */
	public static boolean delete(
			File	file,
			boolean recursiveDirDelete)
	{
		boolean retval = false;
		boolean fileIsDir = false;

		// Check for dir.
		if (file.isDirectory())
		{
			fileIsDir = true;

			// Get dir conents.
			String[] dirContents = file.list();

			// Process dir contents.
			for (String item : dirContents)
			{
				File itemFile = new File(file,item);

				// Delete what?
				if (itemFile.isDirectory())
				{
					// Delete dir.
					if (recursiveDirDelete)
					{
						delete(itemFile,true);
					}
				}
				else
				{
					// Delete file.
					retval =  itemFile.delete();
					System.gc();
					if (!retval)
					{
						// Failure.
						return false;
					}
				}
			} // for
		}

		// Delete it.
		if (fileIsDir)
		{
			if (recursiveDirDelete)
			{
				retval = file.delete();
				System.gc();
			}
			else
			{
				retval = true;
			}
		}
		else
		{
			retval = file.delete();
			System.gc();
		}

		return retval;
	}

	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	/**
	 * Copy source file/dir to dest file/dir.
	 *
	 * @param destFile Destination file/dir.
	 * @param sourceFile Source file/dir.
	 * @param excludeDirs List of dirs to exclude from the copy.
	 *					If any source dir ends with any of
	 *					these dirs it is excluded from the copy.
	 * @throws IOException If copy failed.
	 */
	public static void copy(
			File destFile,
			File sourceFile,
			String[] excludeDirs)
	throws IOException
	{
		// Check for dir.
		if (sourceFile.isDirectory())
		{
			// Excludes.
			if (excludeDirs != null)
			{
				for (String ed : excludeDirs)
				{
					if (sourceFile.getCanonicalPath().replace("\\","/").endsWith(ed))
					{
						// Don't copy.
						return;
					}
				} // for
			}

			// Create dest dir if needed.
			if (!destFile.exists())
			{
				destFile.mkdir();
			}

			// Get source dir contents.
			String[] sourceDirContents = sourceFile.list();

			// Process source dir contents.
			for (String item : sourceDirContents)
			{
				// Copy item.
				copy(new File(destFile,item),
					 new File(sourceFile,item),
					 excludeDirs);
			} // for
		}
		else
		{
			// Copy file.
			copyFile(destFile,sourceFile);
		}
	}

	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	/**
	 * Copy a file. <p>
	 *
	 * @param destFile Destination file.
	 * @param sourceFile Source file.
	 * @throws IOException If copy failed.
	 */
	public static void copyFile(
			File destFile,
			File sourceFile)
	throws IOException
	{
		FileChannel sourceChan = null;
		FileChannel destChan = null;

		try
		{
			// Dest and source channels.
			destChan = new FileOutputStream(destFile.getCanonicalPath()).getChannel();
			sourceChan = new FileInputStream(sourceFile.getCanonicalPath()).getChannel();
	
			// Copy.
			destChan.transferFrom(sourceChan, 0, sourceChan.size());
		}
		catch (IOException e)
		{
			if(sourceChan != null)
			{
				sourceChan.close();
			}

			if(destChan != null)
			{
				destChan.close();
			}
			
			throw new IOException(e.getMessage());
		}

		// Close.
		sourceChan.close();
		destChan.close();
	}

	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	/**
	 * Save content to a file. <p>
	 * Dialog w/user (if needed) to get file name.
	 *
	 * @param dialogForFileName true will dialog to get file path.
	 * @param dialogForFileExists true will dialog if save file exists.
	 * @param fileDir File dir or null to use Edt's start dir.
	 * @param fileName File name or null to force dialog.
	 * @param fileContent File content.
	 * @return File used if all went well or null if not saved
	 *		 and no exception happened.
	 * @throws IOException If file access problem.
	 */
	public static File saveFile(
			boolean dialogForFileName,
			boolean dialogForFileExists,
			String  dialogTitle,
			String  fileDir,
			String  fileName,
			String  fileContent)
	throws IOException
	{
		boolean okToSave = false;

		// Sanity.
		if (fileContent != null)
		{
			// File dir.
			if (fileDir == null)
			{
				fileDir = System.getProperty("user.dir");
			}

			// Dialog if needed.
			if ((dialogForFileName) || (fileName == null))
			{
				JFileChooser fc = new JFileChooser(fileDir);
				fc.setDialogTitle(dialogTitle);

				// Start w/file.
				if (fileName != null)
				{
					fc.setSelectedFile(
							new File(fileDir + "/" + fileName));
				}

				// Dialog.
				int fcVal = fc.showSaveDialog(null);

				// What did user do ?
				if (fcVal == JFileChooser.APPROVE_OPTION)
				{
					okToSave = true;

					fileName = fc.getSelectedFile().getName();
				}
			}
			else
			{
				okToSave = true;
			}
		}

		// Save if ok.
		if (okToSave)
		{
			// File.
			File saveFile = new File(fileDir + "/" + fileName);

			// Exists.
			if ((dialogForFileExists) && (saveFile.exists()))
			{
				int option =
					JOptionPane.showConfirmDialog(
							null,
							"Overwrite existing file " + saveFile.getName() + " ?",
							dialogTitle,
							JOptionPane.YES_NO_OPTION);

				// Save?
				okToSave = (option == JOptionPane.YES_OPTION);
			}

			// Save if ok.
			if (okToSave)
			{
				// Setup.
				FileWriter	 fWriter = new FileWriter(saveFile);
				BufferedWriter bWriter = new BufferedWriter(fWriter);

				// Save.
				bWriter.write(fileContent);

				// Close.
				bWriter.close();
			}
			else
			{
				// Non exception failure.
				return null;
			}

			// Save path.
			return saveFile;
		}
		else
		{
			// Non exception failure.
			return null;
		}
	}
	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	public static File openFolder(String folderDir, String dialogTitle) {
		if (EdtStringUtil.isNullOrEmpty(folderDir))
			folderDir = System.getProperty("user.dir");

		JFileChooser fc = new JFileChooser(folderDir);
		fc.setDialogTitle(dialogTitle);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int fcVal = fc.showOpenDialog(null);

		if (fcVal == JFileChooser.APPROVE_OPTION)
			return fc.getSelectedFile();
		
		return null;
		
	}
	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	public static File openFile(String fileDir, String dialogTitle, String filterDescription, String... filterExt) {
		if (EdtStringUtil.isNullOrEmpty(fileDir))
			fileDir = System.getProperty("user.dir");

		JFileChooser fc = new JFileChooser(fileDir);
		fc.setDialogTitle(dialogTitle);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if(filterExt != null)
			fc.addChoosableFileFilter(new FileNameExtensionFilter(filterDescription, filterExt));
		
		int fcVal = fc.showOpenDialog(null);

		if (fcVal == JFileChooser.APPROVE_OPTION)
			return fc.getSelectedFile();
		
		return null;
		
	}


	/**
	 * @param sFilePath is the complete pathname to the file
	 * @param iFirstPosition is the first position into the file to search for the given mark.
	 * @param iLastPosition is one past the last position into the file to search for the given mark.
	 * @param sTheMark is the mark to be searched for.
	 * @return <b>true</b> if the given mark is found in the given interval, <b>false</b> otherwise.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean  hasFileTheGivenMark(String sFilePath, int iFirstPosition, int iLastPosition, String sTheMark)
	throws FileNotFoundException, IOException
	{
		if( iLastPosition <= iFirstPosition )
		{
			return false; // bad interval definition
		}

		FileInputStream fis = new FileInputStream(sFilePath);
		int iFileLength = (int) fis.getChannel().size();

		if( iFileLength <= iFirstPosition )
		{
			fis.close();
			return false; // bad beginning position parameter
		}
		else if( iLastPosition >= iFileLength )
		{
			iLastPosition = iFileLength; // interval too long. Adjusted according to file length.
		}

		if( iFirstPosition > 0 )
		{
			fis.skip( iFirstPosition );
		}

		byte abFileChunk[] = new byte[ iLastPosition - iFirstPosition ];
		fis.read( abFileChunk, 0, iLastPosition - iFirstPosition );
		fis.close();
		int iPosition = EdtByteUtil.indexOf(abFileChunk, sTheMark.getBytes("UTF-8"));

		return iPosition < 0 ? false : true;
	}

	/**
	 * @param sFilePath : String : Is the complete pathname to the file
	 * @param iFirstPosition : int : Is the first position into the file to search for the given mark.
	 * @param iLastPosition : int : Is one past the last position into the file to search for the given mark.
	 * @param sTheseMarks : String[] : Are the string marks to be searched for.
	 * @return <b>true</b> if all the given marks are found in the given interval, <b>false</b> otherwise.
	 * @throws FileNotFoundException
	 * @throws EdtUtilException if one of the string marks is not found. This mark is denoted in the error string.
	 * @throws IOException
	 */
	public static boolean  hasFileAllTheseMarks(String sFilePath, int iFirstPosition, int iLastPosition, String sTheseMarks[])
	throws FileNotFoundException, EdtUtilException, IOException
	{
		if( sTheseMarks == null || sTheseMarks.length == 0)
		{
			return true; // null strings are always found
		}

		if( iLastPosition <= iFirstPosition )
		{
			return false; // bad interval definition
		}

		FileInputStream fis = new FileInputStream(sFilePath);
		int iFileLength = (int) fis.getChannel().size();

		if( iFileLength <= iFirstPosition )
		{
			fis.close();
			return false; // bad beginning position parameter
		}
		else if( iLastPosition >= iFileLength )
		{
			iLastPosition = iFileLength; // interval too long. Adjusted according to file length.
		}

		BufferedInputStream bis = new BufferedInputStream(fis, iLastPosition - iFirstPosition);

		bis.skip( iFirstPosition );
		// the file chunk is going to be repeatedly read "array length" times.
		bis.mark(sTheseMarks.length * (iLastPosition - iFirstPosition));

		byte abFileChunk[] = new byte[ iLastPosition - iFirstPosition ];
		bis.read( abFileChunk, 0, iLastPosition - iFirstPosition );

		for(String sTheMark : sTheseMarks)
		{
			if( sTheMark == null || sTheMark.isEmpty() )
			{
				continue; // null strings are always found
			}

			int iPosition = EdtByteUtil.indexOf(abFileChunk, sTheMark.getBytes("UTF-8"));
	
			if( iPosition < 0 )
			{
				bis.close();
				throw new EdtUtilException(String.format("String Mark not found: %s", sTheMark));
			}

			bis.reset();
		}

		bis.close();
		return true; // all the strings are found
	}

	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	/**
	 * Load file into string. <p>
	 *
	 * @param filePath File pathname.
	 * @param addLF Add LF to each line read from file.
	 * @return String if file read ok.
	 * @throws IFileNotFoundException If problem.
	 * @throws IOException If problem.
	 */
	public static String loadFile(
			String  filePath,
			boolean addLF)
	throws FileNotFoundException,IOException
	{
		StringBuilder sb = new StringBuilder(1000);

		FileReader	 fReader = null;
		BufferedReader bReader = null;

		FileNotFoundException fnfe = null;
		IOException ioe = null;

		try
		{
			// Setup.
			fReader = new FileReader(filePath);
			bReader = new BufferedReader(fReader);
			String line = "";

			// Load.
			while (line != null)
			{
				line = bReader.readLine();
				if (line != null)
				{
					sb.append(line);
					if (addLF)
					{
						sb.append("\n");
					}
				}
			} // while
		}
		catch (FileNotFoundException ex)
		{
			// Save.
			fnfe = ex;
		}
		catch (IOException ex)
		{
			// Save.
			ioe = ex;
		}

		// Close anything open.

		try
		{
			if (bReader != null)
			{
				bReader.close();
			}
		}
		catch (IOException ex)
		{

		}

		try
		{
			if (fReader != null)
			{
				fReader.close();
			}
		}
		catch (IOException ex)
		{
			
		}

		if (fnfe != null)
		{
			throw fnfe;
		}

		if (ioe != null)
		{
			throw ioe;
		}

		return sb.toString();
	}

	/**
	 * @brief Loads a small text file into string. <p>
	 *
	 * @param sFilePath Pathname to the given file.
	 * @param sCharset The charset name the file characters are encoded.
	 * @return String if the file could be opened and read OK, null otherwise
	 * @throws UnsupportedEncodingException If the supplied charset name is not properly recognized.
	 * @throws IOException If problem arises reading the file.
	 * @since 1.7
	 */
	public static String loadSmallTextFile( String sFilePath, String  sCharset )
	throws UnsupportedEncodingException, IOException
	{
		File fileText = new File(sFilePath);

		return new String( Files.readAllBytes(fileText.toPath()), sCharset );
	}

	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	/**
	 * Uncompress compressed input using Gzip engine. <p>
	 *
	 * @param compressed the compressed data as a byte array.
	 * @return uncompressed byte array.
	 * @throws IOException.
	 */
	public static byte[] uncompressInflate(byte[] compressed)
	throws IOException
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(compressed);
		InflaterInputStream gis = new InflaterInputStream(bais);
		StringBuilder builder = new StringBuilder(512);
		byte[] uncompressed = new byte[compressed.length];

		try
		{
			int len = 0;
			while ((len = gis.read(uncompressed)) != -1)
			{
				byte[] actualBytesRead = new byte[len];
				System.arraycopy(uncompressed, 0, actualBytesRead, 0, len);
				builder.append(new String(actualBytesRead));
			}
		}
		finally
		{
			gis.close();
			bais.close();
		}
		return builder.toString().getBytes();
	}

	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	/**
	 * Compress the plain input using Gzip engine. <p>
	 *
	 * @param  uncompressed the uncompressed data as a byte array.
	 * @return compressed byte array.
	 * @throws IOException.
	 */
	public static byte[] compressDeflate(byte[] uncompressed)
	throws IOException
	{
		ByteArrayOutputStream baos = null;
		DeflaterOutputStream gos = null;

		try
		{
			baos = new ByteArrayOutputStream();
			gos = new DeflaterOutputStream(baos);
			gos.write(uncompressed);
		}
		finally
		{
			if (gos != null)
			{
				gos.close();
			}
		}
		return baos.toByteArray();
	}

	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	/**
	 * Get file extension. <p>
	 *
	 * @param f The file.
	 * @return The value or null if no extension.
	 */
	public static String getFileExtension(File f)
	{
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 &&  i < s.length() - 1)
		{
			ext = s.substring(i+1).toLowerCase();
		}
		return ext;
	}

	/**
	 * Get a File Filter to exclude hidden files and directories from a listFile
	 * 
	 * @return A FileFilter for hidden files & dirs
	 */
	public static FileFilter getHiddenAndDirFileFilter()
	{
	   return new FileFilter()
	   {
		  @Override
		  public boolean accept(File pathname)
		  {
			 // skip hidden files & directories
			 return !(pathname.isHidden() || pathname.isDirectory());
		  }
	   };
	}
	
	/**
	 * Get a File Filter to exclude hidden files and hidden directories from a listFile
	 * 
	 * @return A FileFilter for hidden files & hidden dirs
	 */
	public static FileFilter getHiddenFileFilter()
	{
	   return new FileFilter()
	   {
		  @Override
		  public boolean accept(File pathname)
		  {
			 // skip hidden files/directories
			 return !pathname.isHidden();
		  }
	   };
	}

	
	public static boolean checkOrCreateDirectory(Path path)
	{
		if (Files.exists( path )) return true;

		try
		{
			Files.createDirectories( path );
		}
		catch (IOException e)
		{
			return false;
		}

		return true;
	}
	
	/**
	 * @brief counts the amount of files of the given path
	 * @param path 
	 * @return the amount of files of the given path 
	 */
	public static int getFilesCount(String path)
	{
		File currentFolder = new File( path );
		if (!currentFolder.exists( ) || !currentFolder.isDirectory( ))
		{
			return 0;
		}

		int count = 0;
		File[] fileVector = currentFolder.listFiles( );
		for (File file : fileVector)
		{
			if (file.isDirectory( ))
			{
				try
				{
					count  += getFilesCount( file.getCanonicalPath( ) );				
				}
				catch (IOException e)
				{
				}
			}
			else
			{
				if (file.exists( ))
				{	
					count++;
				}
			}
		}
		return count;
	}

	// ******************************************************************
	// PROTECTED METHODS.
	// ******************************************************************

	// ******************************************************************
	// PRIVATE METHODS.
	// ******************************************************************

	// ******************************************************************
	// INNER CLASSES.
	// ******************************************************************

	// ******************************************************************
	// NATIVE METHODS.
	// ******************************************************************

	// ******************************************************************
	// MAIN.
	// ******************************************************************
	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}