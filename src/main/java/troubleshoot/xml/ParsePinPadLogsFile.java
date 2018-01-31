package troubleshoot.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.util.EdtFileUtil;

public class ParsePinPadLogsFile 
{
	private final static Logger logger = Logger.getLogger(ParsePinPadLogsFile.class);
	private static ParsePinPadLogsFile  pinpadLogsFileInstance= null;

	public static ParsePinPadLogsFile getInstance()
	{
		if(pinpadLogsFileInstance == null)
		{
			pinpadLogsFileInstance = new ParsePinPadLogsFile();
		}
		return pinpadLogsFileInstance;
	}

	public Date deviceLastStartDate(String fileName)
	{
		String line,last= "";
		Date firstDateOfFile = null;
		BufferedReader input = null;;
		try {
			input = new BufferedReader(new FileReader(fileName));
			while ((line = input.readLine()) != null) 
			{
				last = line;
			}
			System.out.println(last);
			String date1= last.substring(0, 8);
			firstDateOfFile = new SimpleDateFormat("yyyyMMdd").parse(date1);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if(input!=null)
			{
				try 
				{
					input.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			
			File fileToDelete = new File(fileName);
			EdtFileUtil.delete(fileToDelete,true);
		}

		return firstDateOfFile;
	}
	
	public File journalLogOfNearestDate(String RootFolderPath)
	{
		File RootFolder = new File(RootFolderPath);
		Date currentDate = TroubleshootController.getInstance().currentDateofSystem();
		long minDiff = -1;
		long currentTime = currentDate.getTime();
		String minDate = "";
		File currentJournalFile = null;
		if(RootFolder.isDirectory())
		{
			@SuppressWarnings("unchecked")
			Collection<File> listOfFiles = FileUtils.listFiles(RootFolder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
			for (File file : listOfFiles) 
			{
				String FileName = file.getName();					
				String[] Name= FileName.split("\\.");
				if(Name[0].equalsIgnoreCase("journal"))
				{
					String DateInStringFormat = Name[1];
					try 
					{
						Date dateOfFile = new SimpleDateFormat("yyyyMMdd").parse(DateInStringFormat);
						if(dateOfFile.before(currentDate) || dateOfFile.equals(currentDate))
						{
							long diff = Math.abs(currentTime - dateOfFile.getTime());
							if ((minDiff == -1) || (diff < minDiff)) 
							{
								minDiff = diff;
								minDate = DateInStringFormat;
							}
						}
					} 
					catch (ParseException e) 
					{
						e.printStackTrace();
					}
				}
			}
		}
		
		if(!minDate.equalsIgnoreCase(""))
		{
			currentJournalFile = new File(RootFolderPath+"//journal" + "." + minDate + ".log.gz");
			@SuppressWarnings("unchecked")
			Collection<File> listOfFiles = FileUtils.listFiles(RootFolder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
			for (File file : listOfFiles) 
			{
				if(!file.equals(currentJournalFile))
				{
					EdtFileUtil.delete(file,true);
				}
			}
		}
		return currentJournalFile;
	}
	
	public void logByDateAction(String RootFolderPath, Date firstDate, Date lastDate)
	{
		try
		{
			File RootFolder = new File(RootFolderPath);
			if(RootFolder.isDirectory())
			{
				@SuppressWarnings("unchecked")
				Collection<File> listOfFiles = FileUtils.listFiles(RootFolder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
				for (File file : listOfFiles) 
				{
					String FileName = file.getName();					
					String[] Name= FileName.split("\\.");
					
					if(Name[0].compareTo("LinuxKernel") == 0)
					{
						continue;
					}
					
					if(Name.length >= 3)
					{				
						try 
						{							
							int indexOfDate = Name.length - 3;
							String DateInStringFormat = Name[indexOfDate];
							
							if( (Name[0].compareTo("17") == 0) && (Name[1].compareTo("00") == 0) )
							{
								if(Name.length == 8)
								{
									//DateInStringFormat = getDateOfPCIFile(Name[5]);
								}
							}
							
							if(DateInStringFormat.length() == 8)
							{
								if(StringUtils.isNumeric(DateInStringFormat))
								{							
									Date DateOfFile = new SimpleDateFormat("yyyyMMdd").parse(DateInStringFormat);						
									if( (DateOfFile.after(firstDate)|| DateOfFile.equals(firstDate)) && (DateOfFile.before(lastDate) || DateOfFile.equals(lastDate)) )
									{
										continue;
									}
								}
							}
						}
						catch (ParseException e) 
						{
							logger.error(e.getMessage());
							e.printStackTrace();
						} 
					}
					
					file.delete();
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
	}
	
	public boolean unZipExtractedTarFiles(File destinationFolder, String extension)
	{
		try
		{
			boolean isError = false;
			if(destinationFolder.isDirectory())
			{
				File [] logfiles = destinationFolder.listFiles();
				for (File file : logfiles) 
				{
					if(file.isDirectory())
					{
						unZipExtractedTarFiles(file, extension);
					}
					else
					{
						String outputFile = destinationFolder.toString() + "\\" + FilenameUtils.getBaseName(file.toString()) + extension;
						gunzipIt(file.toString(), outputFile);
						EdtFileUtil.delete( file, true );
						isError = true;
					}
				}
				return isError;
			}
			else if(destinationFolder.isFile())
			{
				String outputFile = FilenameUtils.getFullPath(destinationFolder.toString()) + FilenameUtils.getBaseName(destinationFolder.toString()) + extension;
				gunzipIt(destinationFolder.toString() ,outputFile);
				EdtFileUtil.delete( destinationFolder, true );
				isError = true;
				return isError;
			}
			else
			{
				return isError;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public static void gunzipIt(String inputFile ,String outputFile)
	{
		byte[] buffer = new byte[1024];
		int len;
		try
		{
			GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(inputFile));
			FileOutputStream out = new FileOutputStream(outputFile);
			while ((len = gzis.read(buffer)) > 0) 
			{
				out.write(buffer, 0, len);
			}
			gzis.close();
			out.close();

		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public boolean searchKeyWord( File outputFolder,String keyWord)
	{
		boolean isKeyWordFound = false;
		File[] logFile = outputFolder.listFiles();
		for (File file : logFile) 
		{
			keyWord = keyWord.trim();
			BufferedReader br = null;
			try
			{
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				String line;
				try {
					while ((line = br.readLine()) != null)
					{
						if (line.contains(keyWord))
						{
							return isKeyWordFound =true;
						}
						else
						{
						}
					}
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			} 
			catch (FileNotFoundException e1) 
			{
				e1.printStackTrace();
			}
			finally
			{
				try
				{
					if (br != null)
						br.close();
				}
				catch (Exception e)
				{
					System.err.println("Exception while closing bufferedreader " + e.toString());
				}
			}

			return isKeyWordFound = false;
		}
		return isKeyWordFound;
	}

	

}
