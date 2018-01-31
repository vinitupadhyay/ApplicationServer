package troubleshoot.models;

import java.util.ListIterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import spot.message.models.SpotApdu;
import spot.messages.SpotMessageType;
import spot.messages.SpotMessages;
import troubleshoot.event.handler.EventManager;
import troubleshoot.file.managment.api.FileResourceWriter;
import troubleshoot.model.pojo.Resource;
import troubleshoot.socket.event.EventListener;
import troubleshoot.socket.event.SocketEvent;
import troubleshoot.socket.event.SocketEventService;
import troubleshoot.states.DownloadResult;
import troubleshoot.util.EdtConvert;

public class FileDownload 
{
	private final static Logger logger = Logger.getLogger(FileDownload.class);
	public final static String	DEFAULT_LOG_FOLDER = "upm_logs";
	public final static String	DEFAULT_TEMP_FOLDER = "temp";
	private static final long DEFAULT_TIME_OUT = 600000L;
	private static final String DOWNLOAD_COMPLETE = "02";
	private static final String NO_ERROR = "00";
	private static FileDownload fileDownload = null;
	
	public static DownloadResult downloadResult = DownloadResult.None;
	
	private boolean isDownloadSuccess;
	private boolean isErrorInResponse;
	private boolean isProcessing;
	private Object monitor;
	private Resource currentFileResource;
	private Vector<Resource> allResources;
	private ListIterator<Resource> allResourcesIterator;
	
	public FileDownload() 
	{
		this.isProcessing = false;
		this.allResources = null;
		this.allResourcesIterator = null;
		monitor = new Object();
		subscribeToEvent();
	}
	
	private void subscribeToEvent()
	{
		SocketEventService.getInstance().subscribe(new SocketEventListener());
	}
	
	public static FileDownload getInstance()
	{
		if(fileDownload == null)
		{
			fileDownload = new FileDownload();
		}
		return fileDownload;
	}
	
	private void addAll(Vector<Resource> fileResourceVector)
	{
		if (allResources == null)
		{
			allResources = new Vector<Resource>( );
			allResourcesIterator = allResources.listIterator( );
		}
		allResources.addAll( fileResourceVector );
		allResourcesIterator = allResources.listIterator( allResourcesIterator.nextIndex( ) );
	}

	/**
	 * @brief Clears the engine writers list.
	 * 
	 * @since 1.0.0
	 */
	private void clear()
	{
		if (allResources == null)
		{
			allResources = new Vector<Resource>( );
			allResourcesIterator = allResources.listIterator( );
		}
		else
		{
			// add each file and its history file option.
			allResources.clear( );
			allResourcesIterator = allResources.listIterator( );
		}
	}
	
	private void cleanUpProcess()
	{
		currentFileResource = null;
		clear( );
	}
	
	private void processSuccess()
	{
		cleanUpProcess( );
		wakeUp();
	}
	
	private boolean blocksToRequest(String fileSize)
	{
		String fileSzInHex = "0x"+fileSize;
		int size = Integer.decode( fileSzInHex );
		return size > 0;
	}
	
	
	private void checkMoreFilesToUpload()
	{
		if (allResourcesIterator.hasNext( ))
		{
			currentFileResource = allResourcesIterator.next( );
			
			if(currentFileResource.getName().equals("UX300_VIPA") || currentFileResource.getName().equals("UX300_OS"))
			{
				if(currentFileResource.getName().equals("UX300_VIPA"))
				{
					logger.info("Sending Vipa Log download request to SPOT");
					if(!sendUxLogFileUploadRequest(true))
					{
						isErrorInResponse = true;
						wakeUp();
					}
				}
				else
				{
					logger.info("Sending OS Log download request to SPOT");
					if(!sendUxLogFileUploadRequest(false))
					{
						isErrorInResponse = true;
						wakeUp();
					}
				}
			}
			else
			{
				if(!sendFileUploadRequest( ))
				{
					isErrorInResponse = true;
					wakeUp();
				}
			}
		}
		else
		{
			processSuccess( );
		}
	}
	
	public boolean startDownload(Vector<Resource> allResources, boolean isDisconnectNeeded)
	{
		downloadResult = DownloadResult.None;
		isDownloadSuccess = false;
		isErrorInResponse = false;
		isProcessing = true;
		clear();
		addAll(allResources);
		
		checkMoreFilesToUpload();
		
		waitForResponse();
		
		if(isDisconnectNeeded)
		{
			Connection.getInstance().doClose();
		}
		isProcessing = false;
		return isDownloadSuccess && !isErrorInResponse;
	}
	
	private boolean sendUxLogFileUploadRequest(boolean isVipaLog)
	{
		byte logType = (byte) (isVipaLog ? 0x01 : 0x02);
		return EventManager.getInstance().send(SpotMessages.getInstance().getUxLogFileRequestData(SpotMessageType.UX_LOG_UPLOAD, logType));
	}
	
	private boolean sendFileUploadRequest()
	{
		return EventManager.getInstance().send(SpotMessages.getInstance().getFileDownloadRequestMessage(SpotMessageType.FILE_UPLOAD, currentFileResource.getFileType(),currentFileResource.getFileId()));
	}
	
	public void sendFileBlockDownloadRequest(byte[] blockOffset)
	{
		EventManager.getInstance().send(SpotMessages.getInstance().getFileBlockDownloadRequestMessage(SpotMessageType.FILE_BLOCK_UPLOAD, blockOffset));
	}
	
	public void uxLogFileUploadResponse(SpotApdu apdu)
	{			
		if ( apdu.getAck().equals("00") )
		{
			if(currentFileResource.getName( ).equals("UX300_VIPA"))
			{
				logger.info("Vipa Logs Successfully downloaded into SPOT");
			}
			else
			{
				logger.info("OS Logs Successfully downloaded into SPOT");
			}
			
			sendFileUploadRequest( );
		}
		else
		{
			if(currentFileResource.getName( ).equals("UX300_VIPA"))
			{
				logger.error("Vipa Logs failed to download into SPOT");
			}
			else
			{
				logger.error("OS Logs failed to download into SPOT");
			}
			checkMoreFilesToUpload( );
		}
	}
	
	public void processFileDownloadAnswer(SpotApdu apdu)
	{	
		if(apdu.getAck().equals("00"))
		{
			if(blocksToRequest(apdu.getData()))
			{
				if(FileResourceWriter.getInstance().open(currentFileResource.getLogFilePath()))
				{
					sendFileBlockDownloadRequest(new byte[]{0x00, 0x00, 0x00, 0x00});
				}
				else
				{
					logger.error("Error : error while opening file");
				}
			}
			
		} 
		else if(apdu.getAck().equals("19")) //File not found at target unit
		{
			isDownloadSuccess = true;
			downloadResult = DownloadResult.FileNotFound;
			logger.error(currentFileResource.getName()+": File not found at target unit");
			checkMoreFilesToUpload();
		}
		else
		{
			isErrorInResponse = true;
			logger.error("error while downloading file");
			checkMoreFilesToUpload();
		}
	}
	
	public void processFileBlockDownloadAnswer(SpotApdu apdu)
	{
		if(apdu.getAck().equals(DOWNLOAD_COMPLETE)) // File download complete
		{
			if(apdu.getData().length() > 16)
			{
				String blockData = apdu.getData().substring(16, apdu.getData().length());
				
				if (FileResourceWriter.getInstance().write(EdtConvert.hexStringToByteArray(blockData)))
				{
					if (FileResourceWriter.getInstance().close( ))
					{
						isDownloadSuccess = true;
						logger.info(currentFileResource.getName()+":File downloaded successfully");
					}
					else
					{
						logger.error(currentFileResource.getName()+" : unable to close file");			
					}
				}
				else
				{
					logger.error(currentFileResource.getName()+" : unable to append data into file");
				}
			}
			checkMoreFilesToUpload( );
		}
		else if(apdu.getAck().equals(NO_ERROR)) // Received Ok and has more blocks to request
		{
			if(apdu.getData().length() > 16)
			{
				String blockOffset = apdu.getData().substring(0, 8);
				String blockData = apdu.getData().substring(16, apdu.getData().length());
				if (FileResourceWriter.getInstance().write(EdtConvert.hexStringToByteArray(blockData)))
				{
					sendFileBlockDownloadRequest(EdtConvert.hexStringToByteArray(blockOffset));
				}
				else
				{
					logger.error(currentFileResource.getName()+" : unable to append data into file");
					checkMoreFilesToUpload( );
				}
			}
		}
		else
		{
			isErrorInResponse = true;
			logger.error(currentFileResource.getName()+" : Error occured while downloading");
			checkMoreFilesToUpload( );
		}
	}
	
	private boolean waitForResponse()
	{
		boolean isTimeOutError = false;
		synchronized (monitor) 
		{
			try 
			{
				long t0 = System.currentTimeMillis();
				long t1 = 0;
				monitor.wait(DEFAULT_TIME_OUT);
				t1 = System.currentTimeMillis();
				if((t1-t0) >= DEFAULT_TIME_OUT)
				{
					isTimeOutError = true;
					logger.error("Response not received : timeout error");
				}
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		return isTimeOutError;
	}
	
	private void wakeUp()
	{
		synchronized (monitor) 
		{
			monitor.notify();
		}
	}
	
	private final class  SocketEventListener implements EventListener
	{

		@Override
		public void onMessage(SocketEvent eventType)
		{
			if(isProcessing)
			{
				switch (eventType)
				{
					case Disconnect:
						isErrorInResponse = true;
						wakeUp();
						break;
					default:
						break;
				}
			}
		}
		
	}
}
