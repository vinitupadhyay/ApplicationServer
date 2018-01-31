package troubleshoot.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import troubleshoot.config.FileResourceCheckUPMConfig;
import troubleshoot.config.TroubleshootConfigurations;
import troubleshoot.controller.TroubleshootController;
import troubleshoot.file.managment.api.FileCompression;
import troubleshoot.model.pojo.LogError;
import troubleshoot.model.pojo.Resource;
import troubleshoot.util.EdtConvert;
import troubleshoot.views.ProcessDialog;

public class CheckUPMLogs implements Comparator<LogError>
{
	private final static Logger logger = Logger.getLogger(CheckUPMLogs.class);
	private final static String ROOT_HIST_LOGS_PATH = FileDownload.DEFAULT_LOG_FOLDER +"//gilbarco//logs";	
	private final static String ERRORS_XML_FILE_PATH = TroubleshootConfigurations.getResourcesDirectory() + "CheckUPMLogs//Errors.xml";
	private static CheckUPMLogs checkLogs = null;
	
	public LogResult Status;

	private ProcessDialog processDialog;
	private TroubleshootController controller;
	private HashMap<String, Vector<LogError>> logFileErrorMap;
	private Vector<LogError> vectorErrors;
	public Vector<LogError> foundErrors;
	private DocumentBuilder dBuilder;
	
	public enum LogResult
	{
		NONE,
		NOT_REQUIRED,
		ERROR,
		SUCCESS;
	}
	
	public CheckUPMLogs()
	{
		controller = TroubleshootController.getInstance();
		logFileErrorMap = new HashMap<String, Vector<LogError>>();
		foundErrors = new Vector<LogError>();
		DocumentBuilderFactory dbFactory  = DocumentBuilderFactory.newInstance();
		try 
		{
			dBuilder = dbFactory.newDocumentBuilder();
		} 
		catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	public static CheckUPMLogs getInstance()
	{
		if(checkLogs == null)
		{
			checkLogs = new CheckUPMLogs();
		}
		return checkLogs;
	}
	
	public void checkLogs()
	{
		TroubleshootController.isLogCheckSkip = false;
		try
		{
			logFileErrorMap.clear();
			foundErrors.clear();
			Status = LogResult.ERROR;
			processDialog = ProcessDialog.getInstance();
			
			cleanDirectories();
			Vector<Resource> allLogResources = getLogInformation();
			if(!allLogResources.isEmpty())
			{
				logger.info("Logs Download - Start");
				if(FileDownload.getInstance().startDownload(allLogResources, false))
				{
					logger.info("Logs Download - End");
					
					logger.info("Logs Uncompress - Start");
					uncompressLogs();
					
					logger.info("Logs Uncompress - End");
					setProcessStatusOnGUI("Parsing logs...");
					
					logger.info("Delete unnecessary logs - Start");
					if(deleteLogsBeforeLastReboot())
					{
						logger.info("Delete unnecessary logs - End");
						parseLogs();
						
						if(!foundErrors.isEmpty())
						{
							Collections.sort(foundErrors, this);
							Status = LogResult.SUCCESS;
						}
						else
						{
							Status = LogResult.SUCCESS;
						}
					}
					else
					{
						logger.error("Error While deleting logs present before last reboot date");
					}
				}
				else
				{
					logger.error("Error in File Download");
				}
			}
			else
			{
				logger.error("There is nothing in check_upm_logs.properties");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error("Exception occured : "+e.getMessage());
		}
		finally
		{
			cleanDirectories();
		}
	}
	
	private void cleanDirectories()
	{
		try
		{
			File upmLogsFolder = new File(FileDownload.DEFAULT_LOG_FOLDER);
			File tempFolder = new File(FileDownload.DEFAULT_TEMP_FOLDER);
			
			if(upmLogsFolder.exists())
			{
				FileUtils.deleteDirectory(upmLogsFolder);
			}
			
			if(tempFolder.exists())
			{
				FileUtils.deleteDirectory(tempFolder);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	private Vector<Resource> getLogInformation()
	{
		Resource[] values = null;
		Vector<Resource> allResources = new Vector<Resource>();
		values = FileResourceCheckUPMConfig.getInstance().allUpmValues(FileDownload.DEFAULT_TEMP_FOLDER);
		if(values.length > 0)
		{
			for(int i = 0 ; i<values.length ; i++)
			{
				allResources.add(values[i]);
			}
		}
		return allResources;
	}
	
	private void setProcessStatusOnGUI(String status)
	{
		if(processDialog != null)
		{
			processDialog.setProcessStatus(status);
		}
	}
	
	private void uncompressLogs()
	{
		File tempFolder = new File(FileDownload.DEFAULT_TEMP_FOLDER);
		File destDirFolder = new File(FileDownload.DEFAULT_LOG_FOLDER);
		String[] files = tempFolder.list();
		for (String fileName : files)
		{
			String filePath = FileDownload.DEFAULT_TEMP_FOLDER + "\\" + fileName;
			
			if(fileName.contains("HIST"))
			{
				FileCompression.getInstance().uncompress(new File(filePath), destDirFolder);
			}
			else
			{
				try
				{
					File srcfile = new File(filePath);
					File destFile = new File(FileDownload.DEFAULT_LOG_FOLDER+"\\"+fileName);
					Files.move(srcfile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				}
				catch (IOException e)
				{
					logger.error(e.getMessage());
					logger.error(e.getCause());
				}
			}
		}
		
		File uncompressFilesDirectory = new File(ROOT_HIST_LOGS_PATH);
		FileCompression.getInstance().unGZipFiles(uncompressFilesDirectory);
	}
	
	private Date getLastRebootDate()
	{
		File journal = new File(FileDownload.DEFAULT_LOG_FOLDER + "//JOURNAL.log");
		Date lastRebootDate = null;
		
		if(journal.exists())
		{
			lastRebootDate = getRebootDate(journal);
		}
		else
		{
			lastRebootDate = getRebootDate(getLatestJournalLogFile(ROOT_HIST_LOGS_PATH));
		}
		
		return lastRebootDate;
	}
	
	private Date getRebootDate(File journal)
	{
		Date lastRebootDate = null;
		String line, last = "";
		BufferedReader input = null;
		
		try
		{
			input = new BufferedReader(new FileReader(journal));
			while ((line = input.readLine()) != null) 
			{
				last = line;
			}
			System.out.println(last);
			String date= last.substring(0, 8);
			lastRebootDate = new SimpleDateFormat("yyyyMMdd").parse(date);
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
		}
		
		return lastRebootDate;
	}
	
	private File getLatestJournalLogFile(String rootDirectoryPath)
	{
		File RootFolder = new File(rootDirectoryPath);
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
			currentJournalFile = new File(rootDirectoryPath + "//journal" + "." + minDate + ".log");
		}
		
		return currentJournalFile;
	}
	
	private boolean deleteLogsBeforeLastReboot()
	{
		Date lastDate =  controller.currentDateofSystem();
		Date firstDate = getLastRebootDate();
		if(lastDate != null && firstDate != null)
		{
			logByDateAction(ROOT_HIST_LOGS_PATH, firstDate, lastDate);
			return true;
		}
		return false;
	}
	
	private void logByDateAction(String RootFolderPath, Date firstDate, Date lastDate)
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
							int indexOfDate = Name.length - 2;
							String DateInStringFormat = Name[indexOfDate];
							
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
	
	private String getType(String fileName)
	{
		String fileType = "";
		Resource[] values = FileResourceCheckUPMConfig.getInstance().allUpmValues(FileDownload.DEFAULT_TEMP_FOLDER);
		for (Resource resource : values)
		{
			if(resource.getName().contains("HIST"))
			{
				if(resource.getName().contains(fileName.toUpperCase()))
				{
					fileType = EdtConvert.byteToHexString(resource.getFileType()) + "." +EdtConvert.byteToHexString(resource.getFileId());
					return fileType;
				}
			}
		}
		return fileType;
	}
	
	private void parseLogs()
	{
		collectErrorInformation();
		Collection<File> listOfFiles = FileUtils.listFiles(new File(FileDownload.DEFAULT_LOG_FOLDER), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		Set<String> fileNames = logFileErrorMap.keySet();
		
		for (String fileName : fileNames)
		{
			for (File logFile : listOfFiles)
			{
				String type = getType(fileName);
				String logFileName = logFile.getName();
				System.out.println("fileName : "+logFile.getName());
				if(logFileName.contains(fileName) || (!type.isEmpty() && logFileName.contains(type)) )
				{
					searchForErrors(logFileErrorMap.get(fileName), logFile);
				}
			}
		}
	}
	
	private void searchForErrors(Vector<LogError> errors, File logFile)
	{
		String line = "";
		BufferedReader input = null;
		try
		{
			input = new BufferedReader(new FileReader(logFile));
			while((line = input.readLine()) != null) 
			{
				if(isError(errors, line))
				{
					logger.info("Error found in file "+logFile.getName()+" Error : "+line);
				}
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		} 
		finally
		{
			if(input != null)
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
		}
	}
	
	private boolean isError(Vector<LogError> errors, String line)
	{
		if(!line.isEmpty())
		{
			for (LogError logError : errors)
			{
				if(!logError.getIsFound())
				{
					if(line.contains(logError.getErrorString()))
					{
						logError.setIsFound(true);
						foundErrors.add(logError);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean collectErrorInformation()
	{
		String fileName, severity, keyword, action, category; 
		try 
		{
			File fileErrors = new File(ERRORS_XML_FILE_PATH);
			if(fileErrors.exists())
			{
				Document doc = dBuilder.parse(fileErrors);
				doc.getDocumentElement().normalize();
				System.out.println("Root element :"  + doc.getDocumentElement().getNodeName());
				NodeList nodeFileList = doc.getElementsByTagName("File");
				for (int i = 0; i < nodeFileList.getLength(); i++)
				{
					fileName = "";
					Node nodeFile = nodeFileList.item(i);
					Element eFile = (Element)nodeFile;
					fileName = eFile.getAttribute("fileName");
					NodeList nodeErrorList = eFile.getElementsByTagName("Error");
					vectorErrors = new Vector<LogError>();
					for(int j=0; j<nodeErrorList.getLength(); j++)
					{
						severity = "";
						keyword = "";
						action = "";
						category = "";
						Node nodeError = nodeErrorList.item(j);
						Element eError = (Element) nodeError;
						severity = eError.getAttribute("Severity");
						keyword = eError.getElementsByTagName("Keyword").item(0).getTextContent();
						action = eError.getElementsByTagName("Action").item(0).getTextContent();
						category = eError.getElementsByTagName("Category").item(0).getTextContent();
						
						vectorErrors.add(new LogError(severity, keyword, action, category));
					}
					
					if(!vectorErrors.isEmpty())
					{
						logFileErrorMap.put(fileName, vectorErrors);
					}
				}
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		
		return false;
	}

	@Override
	public int compare(LogError error1, LogError error2)
	{
		int s1 = Integer.parseInt(error1.getSeverity());
		int s2 = Integer.parseInt(error2.getSeverity());
		
		if(s1 > s2)
		{
			return 1;
		}
		else if(s1 < s2)
		{
			return -1;
		}
		else
		{
			return 0;
		}
	}
}
