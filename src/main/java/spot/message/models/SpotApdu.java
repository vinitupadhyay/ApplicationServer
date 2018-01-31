package spot.message.models;

import troubleshoot.util.EdtConvert;

public class SpotApdu 
{
	private String appId;
	private String cmd;
	private String ack;
	private String data;
	
	public SpotApdu(byte[] message) 
	{
		parseMessage(message);
	}
	
	public String getAck() {
		return ack;
	}

	public void setAck(String ack) {
		this.ack = ack;
	}

	public String getAppId() 
	{
		return appId;
	}
	public void setAppId(String appId) 
	{
		this.appId = appId;
	}
	public String getCmd() 
	{
		return cmd;
	}
	public void setCmd(String cmd) 
	{
		this.cmd = cmd;
	}
	public String getData() 
	{
		return data;
	}
	public void setData(String data) 
	{
		this.data = data;
	}
	
	private void parseMessage(byte[] message)
	{
		appId = EdtConvert.byteToHexString(message[2]);
		
		if(appId.equals("80"))
		{
			cmd = EdtConvert.byteToHexString(message[4]);
			ack = EdtConvert.byteToHexString(message[5]);
			data = EdtConvert.bytesToHexString(message, 6, (message.length - 6));
		}
		else
		{
			cmd = EdtConvert.bytesToHexString(message, 4, 2);
			ack = EdtConvert.byteToHexString(message[6]);
			data = EdtConvert.bytesToHexString(message, 7, (message.length - 7));
		}
	}
}
