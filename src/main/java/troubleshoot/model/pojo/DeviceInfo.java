package troubleshoot.model.pojo;

public class DeviceInfo
{
	private String type = "";
	private String model = "";
	private String serialNumber = "";
	private String fmwVersion = "";
	private String dispResolution = "";
	private DeviceStatus status;
	
	public DeviceInfo()
	{
		status = new DeviceStatus();
	}
	
	public DeviceInfo(DeviceInfo other)
	{
		this.type = other.type;
		this.model = other.model;
		this.serialNumber = other.serialNumber;
		this.fmwVersion = other.fmwVersion;
		this.dispResolution = other.dispResolution;
		this.status = new DeviceStatus(other.status);
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getFmwVersion() {
		return fmwVersion;
	}

	public void setFmwVersion(String fmwVersion) {
		this.fmwVersion = fmwVersion;
	}

	public DeviceStatus getStatus() {
		return status;
	}

	public void setStatus(DeviceStatus status) {
		this.status = status;
	}

	public String getDispResolution() {
		return dispResolution;
	}

	public void setDispResolution(String dispResolution) {
		this.dispResolution = dispResolution;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
}
