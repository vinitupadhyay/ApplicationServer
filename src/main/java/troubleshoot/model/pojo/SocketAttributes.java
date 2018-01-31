package troubleshoot.model.pojo;

public class SocketAttributes 
{
	private String ipAddress;
	private int port;
	private String tlsTrustKeyFileName;
	private boolean isTlsMode;
	
	public SocketAttributes(String ipAddress, int port, String trustKeyFileName, boolean isTlsMode) 
	{
		this.ipAddress = ipAddress;
		this.port = port;
		this.tlsTrustKeyFileName = trustKeyFileName;
		this.isTlsMode = isTlsMode;
	}
	
	public String getIpAddress() 
	{
		return ipAddress;
	}
	
	public void setIpAddress(String ipAddress) 
	{
		this.ipAddress = ipAddress;
	}
	
	public int getPort() 
	{
		return port;
	}
	
	public void setPort(int port) 
	{
		this.port = port;
	}
	
	public String getTlsTrustKeyFileName() 
	{
		return tlsTrustKeyFileName;
	}
	
	public void setTlsTrustKeyFileName(String tlsTrustKeyFileName) 
	{
		this.tlsTrustKeyFileName = tlsTrustKeyFileName;
	}
	
	public boolean isTlsMode() 
	{
		return isTlsMode;
	}
	
	public void setTlsMode(boolean isTlsMode) 
	{
		this.isTlsMode = isTlsMode;
	}
}
