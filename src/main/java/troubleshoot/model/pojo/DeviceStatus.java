package troubleshoot.model.pojo;

import troubleshoot.util.EdtConvert;

public class DeviceStatus
{
	private String communication = "";
	private String dismount = "";
	private String error = "";
	
	public DeviceStatus()
	{

	}
	
	public DeviceStatus(DeviceStatus other)
	{
		this.communication = other.communication;
		this.dismount = other.dismount;
		this.error = other.error;
	}

	public String getCommunication() {
		return communication;
	}

	public void setCommunication(String communication) {
		this.communication = communication;
	}

	public String getDismount() {
		return dismount;
	}

	public void setDismount(String dismount) {
		this.dismount = dismount;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	public void parse(byte[] data)
	{
		communication = EdtConvert.byteToHexString(data[0]);
		dismount = EdtConvert.byteToHexString(data[1]);
		error = EdtConvert.byteToHexString(data[2]);
	}
	
	@Override
	public String toString()
	{
		return (communication+ dismount + error);
	}
	
}
