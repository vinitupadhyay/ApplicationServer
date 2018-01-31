/**
 * © 2013, 2014 Gilbarco Inc.
 * Confidential and Proprietary
 *
 * @file EdtCompressUtil.java
 * @author mgrieco
 * @date 17 Sept 2013
 * @copyright © 2012, 2013, 2014 Gilbarco Inc. Confidential and Propietary
 *
 */
package troubleshoot.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

/**
 * The Class EdtCompressUtil.
 */
public class EdtCompressUtil
{
	// ******************************************************************
	// STATIC FIELDS
	// ******************************************************************
	
	/**
	 * @brief 4Kb buffer for partial one-shot decompression
	 */
	private final static int DECOMPRESS_BUFFER_LENGTH = 4096;
	// ******************************************************************
	// PUBLIC STATIC METHODS
	// ******************************************************************

	
	/**
	 * Untar an input file into an output file.
	 * The output file is created in the output folder, having the same name
	 * as the input file, minus the '.tar' extension.
	 *
	 * @param inputFile     the input .tar file
	 * @param outputDir     the output directory file.
	 * @return  The {@link List} of {@link File}s with the untared content.
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ArchiveException the archive exception
	 */
	public static List<File> unTar(File inputFile, File outputDir) throws FileNotFoundException, IOException, ArchiveException
	{
		if (!outputDir.exists( ))
		{
			outputDir.mkdirs( );
		}

		List<File> untaredFiles = new LinkedList<File>( );
		InputStream is = new FileInputStream( inputFile );
		TarArchiveInputStream debInputStream = (TarArchiveInputStream) new ArchiveStreamFactory( ).createArchiveInputStream( "tar", is );
		TarArchiveEntry entry = null;
		while ((entry = (TarArchiveEntry) debInputStream.getNextEntry( )) != null)
		{
			final File outputFile = new File( outputDir, entry.getName( ) );
			if (entry.isDirectory( ))
			{
				if (!outputFile.exists( ))
				{
					if (!outputFile.mkdirs( ))
					{
						throw new IllegalStateException( String.format( "Couldn't create directory %s.", outputFile.getCanonicalPath( ) ) );
					}
				}
			}
			else
			{
				createParentDirectories( outputDir.getCanonicalPath( ) + "//" + entry.getName( )  );
				OutputStream outputFileStream = new FileOutputStream( outputFile );
				IOUtils.copy( debInputStream, outputFileStream );
				outputFileStream.close( );
				outputFileStream = null;
			}
			untaredFiles.add( outputFile );
		}
		
		is.close( );
		is = null;
		debInputStream.close( );
		debInputStream = null;
		System.gc( );

		return untaredFiles;
	}

	/**
	 * Creates the parent directories.
	 *
	 * @param filename the name of the file 
	 * @return <b>true</b> if and only if the directory was created, along with all necessary parent directories.<p>
	           <b>false</b> otherwise
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static boolean createParentDirectories(String filename) throws IOException
	{
		File file = new File(filename);
		File parentFile = file.getParentFile();
		boolean blOK = parentFile.mkdirs();
		FileWriter writer = new FileWriter(file);
		writer.close( );
		return blOK;
	}

	/**
	 * Ungzip an input file into an output file.
	 * <p>
	 * The output file is created in the output folder, having the same name
	 * as the input file, minus the '.gz' extension.
	 *
	 * @param inputFile     the input .gz file
	 * @param outputDir     the output directory file.
	 * @return  The {@File} with the ungzipped content.
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static File unGzip(final File inputFile, final File outputDir) throws FileNotFoundException, IOException
	{
		File outputFile;
		GZIPInputStream in = null;
		FileOutputStream out = null;
		FileInputStream inputStream = null;
		try
		{
			outputFile = new File( outputDir, inputFile.getName( ).substring( 0, inputFile.getName( ).length( ) - 3 ) );
			inputStream = new FileInputStream( inputFile );
			in = new GZIPInputStream( inputStream );
			out = new FileOutputStream( outputFile );
			IOUtils.copy( in, out );
			inputStream.close();
			in.close( );
			out.close( );
			out = null;
			in = null;
			inputStream = null;
			System.gc( );
			return outputFile;
		}
		catch (Exception e)
		{
			if(e instanceof FileNotFoundException)
			{
				throw (FileNotFoundException)e;
			}
			else if(e instanceof IOException)
			{
				throw (IOException)e;
			}
		}
		finally
		{
			if(inputStream != null)
			{
				inputStream.close();
			}
			
			if(in != null)
			{
				in.close( );
			}
			if(out != null)
			{
				out.close( );
			}
			out = null;
			in = null;
			inputStream = null;
		}
		return null;
	}
	
	
	/**
	 * Checks if is gzip file.
	 *
	 * @param inputFile the input file
	 * @return true, if is gzip file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean isGzipFile(File inputFile)   
	{
		ZipInputStream in;
		FileInputStream fis;
		try
		{
			fis = new FileInputStream( inputFile );
			in = new ZipInputStream( fis );
			ZipEntry pp = in.getNextEntry( );
			fis.close( );
			in.close( );
			return pp!=null;
		}
		catch (ZipException e)
		{
			return false;
		}
		catch (FileNotFoundException e)
		{
			return false;
		}
		catch (IOException e)
		{
			return false;
		}
		finally
		{
			fis = null;
			in = null;
			System.gc( );
		}
	}
	

	/**
	 * Creates the tar file.
	 *
	 * @param filePaths the file to tar
	 * @param saveAs the path where to save the tar file
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ArchiveException the archive exception
	 */
	public static File createTarFile(String[] filePaths, String saveAs) throws IOException, ArchiveException 
	{
		File tarFile = new File( saveAs );
		createParentDirectories( saveAs );
		OutputStream out = new FileOutputStream( tarFile );
		TarArchiveOutputStream aos = new TarArchiveOutputStream( out );
		aos.setLongFileMode( TarArchiveOutputStream.LONGFILE_GNU );

		for (String filePath : filePaths)
		{
			File file = new File( filePath );
			TarArchiveEntry entry = new TarArchiveEntry( file );
			entry.setSize( file.length( ) );
			aos.putArchiveEntry( entry );
			FileInputStream fis = new FileInputStream( file );
			IOUtils.copy( fis, aos );
			fis.close( );
			aos.closeArchiveEntry( );
			aos.close( );
		}
		out.flush( );
		out.close( );
		aos.flush( );
		aos.close( );

		out = null;
		aos = null;
		System.gc( );
		return tarFile;
	}

	/**
	 * @brief Zips the supplied data in the buffer to a new .zip file.
	 * @param sFolderName Folder where zip file is created.
	 * @param sZipFilename Zip filename.
	 * @param sInternalZipPath Internal path inside the zip file where the data is stored.
	 * @param abBuffer The data buffer to save inside a zip file.
	 * @return true if success, false if fails
	 */
	public static void zipBuffer( String sFolderName, String sZipFilename,
	                                 String sInternalZipPath, byte abBuffer[] )
	throws EdtUtilException
	{
		File outputZipFile = new File(sFolderName, sZipFilename);
		ZipOutputStream zOutStream;

		try
		{
			zOutStream = new ZipOutputStream( new FileOutputStream(outputZipFile.getCanonicalPath()) );
		}
		catch (FileNotFoundException e)
		{
			String s = new String( "Invalid pathname " + sFolderName +
									'/' + sZipFilename + " . " + e.getMessage() );
			throw new EdtUtilException(s);
		}
		catch (IOException e)
		{
			String s = new String( "Trouble creating file " + sZipFilename + " . " + e.getMessage() );
			throw new EdtUtilException(s);
		}

		try
		{
			zOutStream.putNextEntry( new ZipEntry( sInternalZipPath ) );
			zOutStream.write(abBuffer, 0, abBuffer.length);
			zOutStream.closeEntry();
			zOutStream.close();
		}
		catch (IOException e)
		{
			String s = new String( "Trouble creating internal entry " + sInternalZipPath + " . " + e.getMessage() );
			throw new EdtUtilException(s);
		}
	}

	/**
	 * @param String: sFolderName is the name of the folder the zip file is located.
	 * @param String: sZipFilename is the name of the zip file to decompress.
	 * @param String: sInternalZipPath is the pathname (folder and subfolders) inside the zip file to reach
	 *                 the compressed data file.
	 * @return byte[] The decompressed data byte array or null if errors have arisen.
	 *                The owner of this <b>new</b> buffer is the caller routine.
	 * @throws EdtUtilException
	 */
	public static byte[] unzipToBuffer( String sFolderName, String sZipFilename,
                                        String sInternalZipPath )
	throws EdtUtilException
	{
		File inputZipFile = new File(sFolderName, sZipFilename);
		ZipInputStream zinStream;

		try
		{
			zinStream = new ZipInputStream( new FileInputStream(inputZipFile.getCanonicalPath()) );
		}
		catch (FileNotFoundException e)
		{
			String s = new String( "Invalid pathname " + sFolderName +
									'/' + sZipFilename + " . " + e.getMessage() );
			throw new EdtUtilException(s);
		}
		catch (IOException e)
		{
			String s = new String( "Trouble opening or reading file " + sZipFilename + " . " + e.getMessage() );
			throw new EdtUtilException(s);
		}

		try
		{
			boolean blEntryFound = false;
			ZipEntry ze = null;
			while( zinStream.available() > 0 )
			{
				ze = zinStream.getNextEntry( );
				if( ze.getName().equals(sInternalZipPath) )
				{
					blEntryFound = true;
					break;
				}
			}
			
			if( ! blEntryFound )
			{
				zinStream.closeEntry();
				zinStream.close();
				throw new EdtUtilException("File not found: " + sInternalZipPath);
			}

			int  iRead = 0;
			int  iOffset = 0;
			long lEstimatedLength = ze.getSize();
			byte abBuffer[] = new byte[DECOMPRESS_BUFFER_LENGTH];

			if(lEstimatedLength <= 0)
			{
				lEstimatedLength = 2 * DECOMPRESS_BUFFER_LENGTH; // if no estimation available, then 2 initial reads
			                                                     // are planned without stream re-dimension.
			}

			ByteArrayOutputStream byteStream = new ByteArrayOutputStream((int) lEstimatedLength);

			while( (iRead = zinStream.read(abBuffer, 0, abBuffer.length) ) > 0 )
			{
				byteStream.write(abBuffer, iOffset, iRead);
				iOffset += iRead;
			}

			zinStream.closeEntry();
			zinStream.close();
			return byteStream.toByteArray();
		}
		catch (EdtUtilException e)
		{
			throw e;
		}
		catch (IOException e)
		{
			String s = new String( "Trouble reading internal entry " + sInternalZipPath + " . " + e.getMessage() );
			throw new EdtUtilException(s);
		}
	}
	
	/**
	 * Unzips the zip file.
	 *
	 * 
	 * @param zipFile: Path of the zip file that is to be unzipped.
	 * @param  outputFolder: path where zip file gets extracted.
	 */

	public static void unZipIt(String zipFile, String outputFolder)
	{
		   byte[] buffer = new byte[1024];
		   try
		   	{
			   	//create output directory is not exists
		    	File folder = new File(outputFolder);
		    	if(!folder.exists())
		    	{
		    		folder.mkdir();
		    	}
		    	
		    	//get the zip file content
		    	ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
		    	//get the zipped file list entry
		    	ZipEntry ze = zis.getNextEntry();
		 
		    	while(ze!=null)
		    	{
		    		String fileName = ze.getName();
		    		File newFile = new File(outputFolder + File.separator + fileName);
		 
		    		System.out.println("file unzip : "+ newFile.getAbsoluteFile());
		 
		            //create all non exists folders
		            //else you will hit FileNotFoundException for compressed folder
		            new File(newFile.getParent()).mkdirs();
		 
		            FileOutputStream fos = new FileOutputStream(newFile);             
		 
		            int len;
		            while ((len = zis.read(buffer)) > 0) 
		            {
		            	fos.write(buffer, 0, len);
		            }
		 
		            fos.close();   
		            ze = zis.getNextEntry();
		    	}
		 
		        zis.closeEntry();
		    	zis.close();
		 
		    	System.out.println("Done");
		 
		    }
		   catch(IOException ex)
		   {
		       ex.printStackTrace(); 
		   }
	}  


	/**
	 * Creates the zip file.
	 *
	 * @param fileName the dir name
	 * @param nameZipFile the name zip file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void createZipFile(String fileName, String nameZipFile) throws IOException
	{
		createParentDirectories( nameZipFile );
		ZipOutputStream zip = null;
		FileOutputStream fW = null;
		fW = new FileOutputStream( nameZipFile );
		zip = new ZipOutputStream( fW );
		File folder = new File( fileName );
		if (folder.isDirectory( ))
		{
			addFolderToZip( "", fileName, zip );
		}
		else
		{
			addFileToZip( "", fileName, zip, true );
		}
		zip.close( );
		fW.close( );
		zip = null;
		fW = null;
		System.gc( );
	}

	/**
	 * Adds the folder to a zip file.
	 *
	 * @param path the path
	 * @param srcFolder the src folder
	 * @param zip the zip
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws IOException
	{
		File folder = new File( srcFolder );
		if (folder.list( ).length == 0)
		{
			addFileToZip( path, srcFolder, zip, true );
		}
		else
		{
			for (String fileName : folder.list( ))
			{
				if (path.equals( "" ))
				{
					addFileToZip( folder.getName( ), srcFolder + "/" + fileName, zip, false );
				}
				else
				{
					addFileToZip( path + "/" + folder.getName( ), srcFolder + "/" + fileName, zip, false );
				}
			}
		}
	}

	/**
	 * Adds the file to zip.
	 *
	 * @param path the path
	 * @param srcFile the src file
	 * @param zip the zip
	 * @param flag the flag
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void addFileToZip(String path, String srcFile, ZipOutputStream zip, boolean flag) throws IOException
	{
		File folder = new File( srcFile );
		if (flag)
		{
			zip.putNextEntry( new ZipEntry( path + "/" + folder.getName( ) + "/" ) );
		}
		else
		{
			if (folder.isDirectory( ))
			{
				addFolderToZip( path, srcFile, zip );
			}
			else
			{
				byte[] buf = new byte[1024];
				int len;
				FileInputStream in = new FileInputStream( srcFile );
				zip.putNextEntry( new ZipEntry( path + "/" + folder.getName( ) ) );
				while ((len = in.read( buf )) > 0)
				{
					zip.write( buf, 0, len );
				}
				in.close();
			}
		}
	}
}
