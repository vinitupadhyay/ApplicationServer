package troubleshoot.models;

import java.io.File;
import java.util.Formatter;
import java.util.Vector;

import org.apache.log4j.Logger;

import spot.message.models.SpotApdu;
import spot.messages.SpotMessageType;
import spot.messages.SpotMessages;
import troubleshoot.event.handler.EventManager;
import troubleshoot.model.pojo.FileResource;
import troubleshoot.resources.SignedResource;
import troubleshoot.socket.event.EventListener;
import troubleshoot.socket.event.SocketEvent;
import troubleshoot.socket.event.SocketEventService;
import troubleshoot.util.EdtConvert;

public class FileUpload
{
	// ******************************************************************
	// INNER TYPES
	// ******************************************************************
	private enum ResourceState
	{
		IDLE, DELETING, UPLOADING, ERROR;
	}

	// ******************************************************************
	// STATIC FIELDS
	// ******************************************************************
	private final static Logger logger = Logger.getLogger(FileUpload.class);

	/** The max block size to sent to the unit */
	private static final int MAX_FILEBLOCK_LENGTH = 8192;

	/** Default target unit connection timeout measured in milliseconds */
	private static final long DEFAULT_TIMEOUT_MILLISECONDS = 6000L;

	/** Default none authentication type */
	private static final byte DEFAULT_NONE_AUTHENTICATION_TYPE = 0x00;

	/** The timeout for a download command answer is set to 60000 milliseconds for UPM */
	private static final long SPOT_COMMAND_REPLY_TIMEOUT_MILLISECONDS = 60000L;

	/** The value to indicate that resources engine is processing */
	private boolean processingResources;

	/** Default value for resource name possition on local file name */
	private static final int RESOURCE_NAME_POSITION = 6;

	/** Default value for resource name max len */
	private static final int RESOURCE_NAME_MAX_LEN = 16;
	
	private static final String NO_ERROR = "00";
	
	private static final String DOWNLOAD_COMPLETE = "02";
	
	private static FileUpload fileUpload = null;
	
	public boolean isErrorInResponse;

	// ******************************************************************
	// PRIVATE FIELDS.
	// ******************************************************************
	private SignedResource					currentResource;
	private byte							currentFileType;
	private byte							currentAuthentType;
	private byte							currentFileId;
	private volatile boolean				blOperationError						= false;
	private volatile ResourceState			currentState							= ResourceState.IDLE;
	private Object monitor;

	// ******************************************************************
	// CONSTRUCTOR.
	// ******************************************************************
	private FileUpload()
	{
		monitor = new Object();
		currentState = ResourceState.IDLE;
		subscribeToEvent();
		processingResources = false;
	}
	
	private void subscribeToEvent()
	{
		SocketEventService.getInstance().subscribe(new SocketEventListener());
	}

	// ******************************************************************
	// PUBLIC METHODS (general, getter, setter, interface imp)
	// ******************************************************************
	
	public static FileUpload getInstance()
	{
		if(fileUpload == null)
		{
			fileUpload = new FileUpload();
		}
		return fileUpload;
	}
	
	public void setCurrentResource(SignedResource currentResource)
	{
		this.currentResource = currentResource;
	}

	public File getCurrentFile()
	{
		return currentResource.getFile();
	}

	public void clearErrorCondition()
	{
		blOperationError = false;
		isErrorInResponse = false;
	}

	public synchronized boolean hasErrors()
	{
		return blOperationError;
	}

	public void upgradeResource(File file, byte fileType, byte fileId, byte authenticationType) throws InterruptedException
	{
		processingResources = true;
		clearErrorCondition();
		
		currentFileType = fileType;
		currentFileId = fileId;
		currentAuthentType = authenticationType;
		
		SignedResource resource = null;
		resource = new SignedResource( file );
		if(resource.hasError())
		{
			blOperationError = true;
		}
		else
		{
			setCurrentResource( resource );
			fileUpload( );
		}
		
		processingResources = false;
	}
	
	public void upgradeResource(String resourceRelativePath, byte fileType, byte fileId, byte authenticationType) throws InterruptedException
	{
		processingResources = true;
		clearErrorCondition();
		
		currentFileType = fileType;
		currentFileId = fileId;
		currentAuthentType = authenticationType;
		
		SignedResource resource = null;
		resource = new SignedResource(resourceRelativePath, true);
		if(resource.hasError())
		{
			blOperationError = true;
		}
		else
		{
			setCurrentResource( resource );
			fileUpload( );
		}
		
		processingResources = false;
	}
	

	/**
	 * Deletes the given set of resources
	 * @param the resources set to delete
	 **/
	public void deleteResources(Vector<FileResource> filesToProcess) throws InterruptedException
	{
		processingResources = true;		
		clearErrorCondition();

		for (FileResource file : filesToProcess)
		{
			currentFileId = file.getFileId( );
			currentAuthentType = DEFAULT_NONE_AUTHENTICATION_TYPE;
			currentFileType = file.getFileType( );

			fileDeletion( );
		}
		processingResources = false;
	}
	
	// ******************************************************************
	// PRIVATE METHODS.
	// ******************************************************************

	/** Initiates the process to delete the given resources file set*/
	private void fileDeletion() throws InterruptedException
	{
		logger.debug( "Deleting file #" + currentFileId + "...." );

		// Deletes the current file on list from OPT terminal, to ensure no upload errors.
		// After the proper OPT reply of this, the new resource on list will be updated.
		currentState = ResourceState.DELETING;
		sendDeleteFile();

		synchronized (this)
		{
			while( currentState == ResourceState.DELETING )
			{
				if(waitForResponse(DEFAULT_TIMEOUT_MILLISECONDS))
				{
					break;
				}
			}

			// Timeout or Error deleting resource file
			if( currentState == ResourceState.DELETING || currentState == ResourceState.ERROR)
			{
				String sMsg = (currentState == ResourceState.DELETING)	? "Resource #" + currentFileId + " file deletion skipped because an answer DISCONNECT or TIMEOUT."
																		: "Resource #" + currentFileId + " file deletion couldn't be done.";
				logger.error( sMsg );
				blOperationError = true;
				return;
			}
			else
			{
				logger.info( "Resource #" + currentFileId + " has been deleted successfully." );
			}
		}
	}
	
	/** Initiates the process to send the given resouces file set*/
	private void fileUpload() throws InterruptedException
	{
		currentState = ResourceState.UPLOADING;
		if( currentResource == null )
		{
			logger.error( "Resource upload skipped because no file has been set." );
			blOperationError = true;
			return;
		}

		// In Java ... bytes are signed ??!!
		int idRes = (currentFileId >= 0 ? currentFileId : currentFileId + 256);
		String sTempl, sMsg, sFilename = "", sFullFilePath = "";
		StringBuilder sMsgBuffer = new StringBuilder();
		Formatter f = new Formatter(sMsgBuffer);

		
		// current STATE is UPLOADING now
		sTempl = "Updating file [%d]: %s ....";
		sMsg = f.format( sTempl, idRes, sFullFilePath ).out().toString();
		logger.info( sMsg );			
		sMsgBuffer.delete(0, sMsgBuffer.length());
		sendFileRequest();

		if(!waitForResponse(SPOT_COMMAND_REPLY_TIMEOUT_MILLISECONDS) && currentState != ResourceState.ERROR)
		{
			sTempl = "Resource [%d]: %s has been sent successfully.";
			sMsg = f.format( sTempl, idRes, sFilename ).out().toString();
			logger.info( sMsg );

			sMsgBuffer.delete(0, sMsgBuffer.length());
		}
		else
		{
			String sErr = "ERROR";
			if(currentState == ResourceState.UPLOADING)
			{
				sErr = "DISCONNECT or TIMEOUT on reply";
			}

			sTempl = "Resource [%d]: %s uploading file %s . Skipped.";
			sMsg = f.format( sTempl, idRes, sErr, sFilename ).out().toString();
			logger.error( sMsg );
			sMsgBuffer.delete(0, sMsgBuffer.length());
			blOperationError = true;
		}
		
		f.close( );
		return;
	}
	
	
		
	// ******************************************************************
	// PROTECTED METHODS.
	// ******************************************************************

	protected void sendDeleteFile()
	{
		EventManager.getInstance().send(SpotMessages.getInstance().getFileDeleteMessage(SpotMessageType.FILE_DELETE, currentFileType, currentFileId));
	}

	protected void sendFileRequest()
	{
		logger.info("Download Request for resource #" + currentFileId + ", type " + currentFileType + ", auth " + currentAuthentType);
		
		String sName = null;
		
		if(currentAuthentType == 1 || currentAuthentType == 2)
		{
			currentAuthentType = (byte)0xff;
		}
		else
		{
			currentAuthentType = 0;
		}
		
		String sFileName = currentResource.getName().trim().substring( RESOURCE_NAME_POSITION );
		sName = (sFileName.length() > RESOURCE_NAME_MAX_LEN) ? sFileName.substring(0, RESOURCE_NAME_MAX_LEN-1) : sFileName;
		
		EventManager.getInstance().send(SpotMessages.getInstance().getFileUploadMessage(SpotMessageType.FILE_DOWNLOAD_REQUEST, currentResource.getData(), currentFileType, currentFileId, currentAuthentType, sName));
	}

	/**
	 * Sends the next block from the current file to download
	 * @param offset, the next block offset
	 * @return returns true if the 
	 */
	protected boolean sendNextBlock(String offset) throws Exception
	{
		try
		{
			offset = "0x"+offset;
			byte[] data = null;
			int iOffSet = EdtConvert.stringToInt( offset );

			if( currentResource.hasError() )
			{
				throw new Exception("Resource [" + currentFileId + "] has errors (1).");
			}

			data = currentResource.getNextBlock(iOffSet, MAX_FILEBLOCK_LENGTH);
			if( currentResource.hasError() )
			{
				throw new Exception("Resource [" + currentFileId + "] has errors (2).");
			}

			if (data != null)
			{
				logger.info("Next Block Request, fileId: " + currentFileId + ", offset " + offset + ", datasize " + data.length);
				sendFileBlockDownloadData( iOffSet, data.length, data );
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			currentState = ResourceState.ERROR;
			String sErr = "ResourceUploadEngine - Error sending data : " + e.getMessage();
			logger.error( sErr );
		}

		return false;
	}

	/**
	 * Sends the given data for a new block download request to the unit
	 * @param offSet, the block offset according to protocol message parameter
	 * @param length, the block length according to protocol message parameter
	 * @param data, the data to send 
	 */
	protected void sendFileBlockDownloadData(int offSet, int length, byte[] data)
	{
		EventManager.getInstance().send(SpotMessages.getInstance().getFileBlockUploadMessage(SpotMessageType.FILE_BLOCK_DOWNLOAD, offSet, length, data));
	}
	
	public void handleFileDeleteResponse(SpotApdu apdu)
	{
		if(apdu.getAck().equals(NO_ERROR)) // Received Ok and has more blocks to request
		{
			currentState = ResourceState.UPLOADING;
		}
		else
		{
			currentState = ResourceState.ERROR;
		}
		wakeUp(); // wakes up the wait()
	}
	
	public void handleFileUploadResponse(SpotApdu apdu)
	{
		if(apdu.getAck().equals(NO_ERROR)) // Received Ok and has more blocks to request
		{
			try
			{
				if ( !sendNextBlock( apdu.getData()) ) // look for another file in the list
				{
					if (currentState != ResourceState.ERROR)
					{
						currentState = ResourceState.IDLE;
					}
					wakeUp(); // wakes up the wait()
				}
			}
			catch (Exception e)
			{
				currentState = ResourceState.ERROR;
				wakeUp(); // wakes up the wait()
			}
		}
		else if(apdu.getAck().equals(DOWNLOAD_COMPLETE))
		{
			currentState = ResourceState.IDLE;
			wakeUp();
		}
		else
		{
			isErrorInResponse = true;
			logger.error("File Upload : error in response : code - "+apdu.getAck());
			currentState = ResourceState.ERROR;
			wakeUp(); // wakes up the wait()
		}
	}
	
	private boolean waitForResponse(long timeout)
	{
		boolean isTimeOutError = false;
		synchronized (monitor) 
		{
			try 
			{
				long t0 = System.currentTimeMillis();
				long t1 = 0;
				monitor.wait(timeout);
				t1 = System.currentTimeMillis();
				if((t1-t0) >= timeout)
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
			switch (eventType)
			{
				case Disconnect:
					logger.debug("SOCKET_DISCONNECT");
					currentState = ResourceState.ERROR;
					wakeUp( );
					break;
				default:
					break;
			}
		}
		
	}
}
