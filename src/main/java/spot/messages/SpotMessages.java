package spot.messages;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import spot.message.models.ChallengeNumberRequest;
import spot.message.models.ChipResetRequest;
import spot.message.models.CreateWindowResourceRequest;
import spot.message.models.DestroyWindowsRequest;
import spot.message.models.DisplayStatusRequest;
import spot.message.models.ExtDiagnosticRequest;
import spot.message.models.FileBlockDownloadRequest;
import spot.message.models.FileBlockUploadRequest;
import spot.message.models.FileBrowseRequest;
import spot.message.models.FileDeleteRequest;
import spot.message.models.FileDownloadRequest;
import spot.message.models.FileUploadRequest;
import spot.message.models.HardwareInfoRequest;
import spot.message.models.HideWindowRequest;
import spot.message.models.InputAbortRequest;
import spot.message.models.InputEnableRequest;
import spot.message.models.LoginRequest;
import spot.message.models.LogoutRequest;
import spot.message.models.MaintainanceModeRequest;
import spot.message.models.PackageInfoRequest;
import spot.message.models.PingRequest;
import spot.message.models.PrinterCreateJobRequest;
import spot.message.models.PrinterEnqueueDataRequest;
import spot.message.models.PrinterExecuteJobRequest;
import spot.message.models.ReaderDisableRequest;
import spot.message.models.ReaderEnableRequest;
import spot.message.models.SMCRequest;
import spot.message.models.ServiceMenuRequest;
import spot.message.models.ShowWindowRequest;
import spot.message.models.SoftwareInfoRequest;
import spot.message.models.SystemStatusRequest;
import spot.message.models.TempActivationRequest;
import spot.message.models.UxLogRequest;
import spot.message.models.WarmReboot;
import troubleshoot.crypto.CRC32;
import troubleshoot.util.EdtConvert;
public class SpotMessages 
{
	private static SpotMessages spotMessages = null;
	
	public SpotMessages() 
	{
	}
	
	public static SpotMessages getInstance()
	{
		if(spotMessages == null)
		{
			spotMessages = new SpotMessages();
		}
		
		return spotMessages;
	}
	
	public byte[] getLoginRequestMessage(SpotMessageType command, byte clientId, byte loginMode)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", new byte[]{0x00});
		map.put("ssk", new byte[]{0x00});
		map.put("cmd", command.getCommand());
		map.put("clientId", new byte[]{clientId});
		map.put("moduloLen", new byte[]{0x00, 0x00});
		map.put("pingTimeOut", new byte[]{0x1E});
		map.put("loginMode", new byte[]{loginMode});
		
		LoginRequest loginRequest = new LoginRequest(map);
		return loginRequest.getMessage();
	}
	
	public byte[] getLogoutRequestMessage(SpotMessageType command)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", new byte[]{0x00});
		map.put("ssk", new byte[]{0x00});
		map.put("cmd", command.getCommand());
		
		LogoutRequest logoutRequest = new LogoutRequest(map);
		return logoutRequest.getMessage();
	}
	
	public byte[] getMaintainanceModeLoginRequestMessage(SpotMessageType command, byte mode, byte[] data)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", new byte[]{0x00});
		map.put("ssk", new byte[]{0x00});
		map.put("cmd", command.getCommand());
		map.put("mode", new byte[]{mode});
		map.put("data", data);
		
		MaintainanceModeRequest mLoginRequest = new MaintainanceModeRequest(map);
		return mLoginRequest.getMessage();
	}
	
	public byte[] getServiceMenuRequestMessage(SpotMessageType command, byte switchType)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", new byte[]{0x00});
		map.put("ssk", new byte[]{0x00});
		map.put("cmd", command.getCommand());
		map.put("switchType", new byte[]{switchType});
		
		ServiceMenuRequest serviceMenuRequest = new ServiceMenuRequest(map);
		return serviceMenuRequest.getMessage();
	}
	
	public byte[] getChallengeNumberRequestMessage(SpotMessageType command)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", new byte[]{0x00});
		map.put("ssk", new byte[]{0x00});
		map.put("cmd", command.getCommand());
		
		ChallengeNumberRequest challengeNumberRequest = new ChallengeNumberRequest(map);
		return challengeNumberRequest.getMessage();
	}
	
	public byte[] getSystemStatusRequestMessage(SpotMessageType command, byte appId, byte device)
	{
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put("appId", new byte[] { appId } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put("cmd", command.getCommand());
		map.put( "device", new byte[] { device } );
		
		SystemStatusRequest systemStatusRequest = new SystemStatusRequest(map);
		return systemStatusRequest.getMessage();
	}
	
	public byte[] getHardwareConfigRequestMessage(SpotMessageType command, byte appId, byte numberOfItems, byte[] itemList)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", new byte[]{appId});
		map.put("ssk", new byte[]{0x00});
		map.put("cmd", command.getCommand());
		map.put("numOfItems", new byte[]{numberOfItems});
		map.put("itemList", itemList);
		
		HardwareInfoRequest hardwareInfoRequest = new HardwareInfoRequest(map);
		return hardwareInfoRequest.getMessage();
	}
	
	public byte[] getSoftwareConfigRequestMessage(SpotMessageType command, byte appId, byte numberOfItems, byte[] itemList)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", new byte[]{appId});
		map.put("ssk", new byte[]{0x00});
		map.put("cmd", command.getCommand());
		map.put("numOfItems", new byte[]{numberOfItems});
		map.put("itemList", itemList);
		
		SoftwareInfoRequest softwareInfoRequest = new SoftwareInfoRequest(map);
		return softwareInfoRequest.getMessage();
	}
	
	public byte[] getTempActivationRequestMessage(SpotMessageType command, byte mode)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", new byte[]{0x01});
		map.put("ssk", new byte[]{0x00});
		map.put("cmd", command.getCommand());
		map.put("mode", new byte[]{mode});
		
		TempActivationRequest activationRequest = new TempActivationRequest(map);
		return activationRequest.getMessage();
	}
	
	public byte[] getFileDownloadRequestMessage(SpotMessageType command, byte fileType, byte fileId)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", new byte[]{0x01});
		map.put("ssk", new byte[]{0x00});
		map.put("cmd", command.getCommand());
		map.put("fileType", new byte[]{fileType});
		map.put("fileId", new byte[]{fileId});
		
		FileDownloadRequest fileDownloadRequest = new FileDownloadRequest(map);
		return fileDownloadRequest.getMessage();
	}
	
	public byte[] getFileBlockDownloadRequestMessage(SpotMessageType command, byte[] blockOffset)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", new byte[]{0x01});
		map.put("ssk", new byte[]{0x00});
		map.put("cmd", command.getCommand());
		map.put("blockOffset", blockOffset);
		
		FileBlockDownloadRequest fileDownloadRequest = new FileBlockDownloadRequest(map);
		return fileDownloadRequest.getMessage();
	}
	
	public byte[] getPackageInfoRequestMessage(SpotMessageType command, byte set)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", new byte[]{0x01});
		map.put("ssk", new byte[]{0x00});
		map.put("cmd", command.getCommand());
		map.put("set", new byte[]{set});
		
		PackageInfoRequest packageInfoRequest = new PackageInfoRequest(map);
		return packageInfoRequest.getMessage();
	}
	
	public byte[] getExtDignosticRequestMessage(SpotMessageType command, byte infoRequested)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", new byte[]{0x01});
		map.put("ssk", new byte[]{0x00});
		map.put("cmd", command.getCommand());
		map.put("infoRequested", new byte[]{infoRequested});
		
		ExtDiagnosticRequest diagnosticRequest = new ExtDiagnosticRequest(map);
		return diagnosticRequest.getMessage();
	}
	
	public byte[] getUxLogFileRequestData(SpotMessageType command, byte logType)
	{
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put("appId", new byte[] { 0x01 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put("cmd", command.getCommand());
		map.put( "LogType", new byte[] { logType } );
		
		UxLogRequest uxLogRequest = new UxLogRequest(map);
		return uxLogRequest.getMessage();
	}
	
	public byte[] getPingRequestMessage(SpotMessageType command)
	{
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put("appId", new byte[] { 0x00 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put("cmd", command.getCommand());
		
		PingRequest pingRequest = new PingRequest(map);
		return pingRequest.getMessage();
	}
	
	public byte[] getReaderEnableMessage(SpotMessageType command)
	{
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put("appId", new byte[] { 0x01 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put("cmd", command.getCommand());
		
		ReaderEnableRequest enableRequest = new ReaderEnableRequest(map);
		return enableRequest.getMessage();
	}
	
	public byte[] getResetRequest(SpotMessageType command,byte reset)
	{
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put("appId", new byte[] { 0x00 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put("cmd", command.getCommand());
		map.put( "ResetType", new byte[] { reset } );

		WarmReboot warmReboot = new WarmReboot(map);
		return warmReboot.getMessage();
	}
	
	public byte[] getReaderDisableMessage(SpotMessageType command)
	{
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put("appId", new byte[] { 0x01 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put("cmd", command.getCommand());
		
		ReaderDisableRequest disableRequest = new ReaderDisableRequest(map);
		return disableRequest.getMessage();
	}
	
	public byte[] getChipResetMessage(SpotMessageType command)
	{
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put("appId", new byte[] { 0x01 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put("cmd", command.getCommand());
		
		ChipResetRequest chipResetRequest = new ChipResetRequest(map);
		return chipResetRequest.getMessage();
	}
	
	public byte[] getFileDeleteMessage(SpotMessageType command, byte fileType, byte fileId)
	{
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put( "appId", new byte[] { 0x01 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put("cmd", command.getCommand());
		map.put( "fileType", new byte[] { fileType } );
		map.put( "fileId", new byte[] { fileId } );
		
		FileDeleteRequest deleteRequest = new FileDeleteRequest(map);
		return deleteRequest.getMessage();
	}
	
	public byte[] getFileBrowseMessage(SpotMessageType command, byte fileType)
	{
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put( "appId", new byte[] { 0x01 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put("cmd", command.getCommand());
		map.put( "fileType", new byte[] { fileType } );
		
		FileBrowseRequest browseRequest = new FileBrowseRequest(map);
		return browseRequest.getMessage();
	}
	
	public byte[] getFileUploadMessage(SpotMessageType command, File file, byte fileType, byte fileId, byte authenType, String description)
	{
		
		byte[] size = new byte[4];
		EdtConvert.longToBytes( true, size, 0, size.length, file.length( ), 4 );
		
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put( "appId", new byte[] { 0x01 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put( "cmd", command.getCommand());
		map.put( "fileType", new byte[] { fileType } );
		map.put( "fileId", new byte[] { fileId } );
		map.put( "fileSize", size );
		map.put( "crc", CRC32.getValue( file, true ) );
		map.put( "authenType", new byte[] { authenType } );
		map.put( "authent", new byte[] { 0x00, 0x00, 0x00, 0x00 } );
		map.put( "desc", description.getBytes( ) );
		
		FileUploadRequest fileUploadRequest = new FileUploadRequest(map);
		return fileUploadRequest.getMessage();
	}
	
	public byte[] getFileUploadMessage(SpotMessageType command, byte[] abData, byte fileType, byte fileId, byte authenType, String description)
	{
		
		byte[] size = new byte[4];
		EdtConvert.longToBytes( true, size, 0, size.length, abData.length, 4 );
		
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put( "appId", new byte[] { 0x01 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put( "cmd", command.getCommand());
		map.put( "fileType", new byte[] { fileType } );
		map.put( "fileId", new byte[] { fileId } );
		map.put( "fileSize", size );
		map.put( "crc", CRC32.getValue( abData ) );
		map.put( "authenType", new byte[] { authenType } );
		map.put( "authent", new byte[] { 0x00, 0x00, 0x00, 0x00 } );
		map.put( "desc", description.getBytes( ) );
		
		FileUploadRequest fileUploadRequest = new FileUploadRequest(map);
		return fileUploadRequest.getMessage();
	}
	
	public byte[] getFileBlockUploadMessage(SpotMessageType command, int offset, int length, byte[] data)
	{
		
		byte[] offSet = new byte[4];
		EdtConvert.intToBytes( true, offSet, 0, offset );
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put( "appId", new byte[] { 0x01 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put( "cmd", command.getCommand());
		map.put( "blkOffset", offSet );
		byte abOnly2Bytes[] = { (byte) (length / 256), (byte) (length % 256) };
		map.put( "blkSize", abOnly2Bytes );
		map.put( "blkData", Arrays.copyOfRange( data, 0, length ) );
		
		FileBlockUploadRequest blockUploadRequest = new FileBlockUploadRequest(map);
		return blockUploadRequest.getMessage();
	}
	
	public byte[] getSMCMessage(SpotMessageType command, byte mode)
	{
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put( "appId", new byte[] { 0x01 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put("cmd", command.getCommand());
		map.put( "mode", new byte[] { mode } );
		
		SMCRequest smcRequest = new SMCRequest(map);
		return smcRequest.getMessage();
	}
	
	public byte[] getDisplayStatusMessage(SpotMessageType command)
	{
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put( "appId", new byte[] { 0x01 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put("cmd", command.getCommand());
		
		DisplayStatusRequest displayStatusRequest = new DisplayStatusRequest(map);
		return displayStatusRequest.getMessage();
	}
	
	public byte[] getInputEnableMessage(SpotMessageType command, byte winId, byte tagId)
	{
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put( "appId", new byte[] { 0x01 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put("cmd", command.getCommand());
		map.put("minLen", new byte[] { 0x01 });
		map.put("maxLen", new byte[] { 0x01 });
		map.put("autoEnter", new byte[] { 0x01 });
		map.put("delEnable", new byte[] { 0x01 });
		map.put("timeout", new byte[] { 0x01, (byte) 0x90 });
		map.put("beepEcho", new byte[] { 0x01 });
		map.put("echoMode", new byte[] { 0x00 });
		map.put("winId", new byte[] { winId });
		map.put("tagId", new byte[] { tagId });
		map.put("ipMode", new byte[] { 0x00 });
		map.put("alphaEnable", new byte[] { 0x21 });
		map.put("format", new byte[] { 0x00 });
		map.put("spec", new byte[] { 0x00 });
		
		InputEnableRequest request = new InputEnableRequest(map);
		return request.getMessage();
	}
	
	public byte[] getCreateWinFromResMessage(SpotMessageType command, byte winId, byte fileId)
	{
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put( "appId", new byte[] { 0x01 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put("cmd", command.getCommand());
		map.put("winId", new byte[] { winId });
		map.put("fileId", new byte[] { fileId });
		map.put("allign", new byte[] { (byte) 0xFF });
		map.put("posx", new byte[] { 0x00, 0x00 });
		map.put("posy", new byte[] { 0x00, 0x00 });
		
		CreateWindowResourceRequest request = new CreateWindowResourceRequest(map);
		return request.getMessage();
	}
	
	public byte[] getShowWindowMessage(SpotMessageType command, byte winId, byte tagId, byte zorder)
	{
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put( "appId", new byte[] { 0x01 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put("cmd", command.getCommand());
		map.put("winId", new byte[] { winId });
		map.put("numTags", new byte[] { 0x01 });
		map.put("tagId", new byte[] { tagId });
		map.put("zorder", new byte[] { zorder });
		
		ShowWindowRequest request = new ShowWindowRequest(map);
		return request.getMessage();
	}
	
	public byte[] getHideWindowMessage(SpotMessageType command, byte winId, byte tagId)
	{
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put( "appId", new byte[] { 0x01 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put("cmd", command.getCommand());
		map.put("winId", new byte[] { winId });
		map.put("numTags", new byte[] { 0x01 });
		map.put("tagId", new byte[] { tagId });
		
		HideWindowRequest request = new HideWindowRequest(map);
		return request.getMessage();
	}
	
	public byte[] getDestroyWindowMessage(SpotMessageType command, byte winId, byte tagId)
	{
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put( "appId", new byte[] { 0x01 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put("cmd", command.getCommand());
		map.put("winId", new byte[] { winId });
		map.put("numTags", new byte[] { 0x01 });
		map.put("tagId", new byte[] { tagId });
		
		DestroyWindowsRequest request = new DestroyWindowsRequest(map);
		return request.getMessage();
	}
	
	public byte[] getInputAbortMessage(SpotMessageType command)
	{
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put( "appId", new byte[] { 0x01 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put("cmd", command.getCommand());
		
		InputAbortRequest request = new InputAbortRequest(map);
		return request.getMessage();
	}
	
	public byte[] getPrinterCreateJobMessage(SpotMessageType command, byte jobId)
	{
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put( "appId", new byte[] { 0x04 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put("cmd", command.getCommand());
		map.put("jobId", new byte[] { jobId });
		
		PrinterCreateJobRequest createJobRequest = new PrinterCreateJobRequest(map);
		return createJobRequest.getMessage();
	}
	
	public byte[] getPrinterEnqueueMessage(SpotMessageType command, byte[] jobId, byte[] seqNumber, byte[] txtLen, byte[] text)
	{
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put( "appId", new byte[] { 0x04 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put("cmd", command.getCommand());
		map.put("jobId", jobId);
		map.put("seqNo", seqNumber);
		map.put("txtLen", txtLen);
		map.put("text", text);
		
		PrinterEnqueueDataRequest dataRequest = new PrinterEnqueueDataRequest(map);
		return dataRequest.getMessage();
	}
	
	public byte[] getPrinterExecuteJobMessage(SpotMessageType command, byte[] jobId)
	{
		Map<String, Object> map = new HashMap<String, Object>( );
		map.put( "appId", new byte[] { 0x04 } );
		map.put( "ssk", new byte[] { 0x00 } );
		map.put("cmd", command.getCommand());
		map.put("jobId", jobId);
		
		PrinterExecuteJobRequest executeJobRequest = new PrinterExecuteJobRequest(map);
		return executeJobRequest.getMessage();
	}
}
