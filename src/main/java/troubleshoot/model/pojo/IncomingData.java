package troubleshoot.model.pojo;

public class IncomingData 
{
	private byte[] data;
	
	public IncomingData(byte[] data) 
	{
		this.data = data;
	}
	
	public byte[] getData(){
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
}
