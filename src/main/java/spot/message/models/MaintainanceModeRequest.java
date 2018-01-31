package spot.message.models;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;

public class MaintainanceModeRequest 
{
	private Map<String, Object> map;
	private int messageLen;
	private byte[] message;
	
	public MaintainanceModeRequest(Map<String, Object> param) 
	{
		map = param;
		getMessageLen();
	}
	
	public int getMessageLen() 
	{
		messageLen = 2;
		for (Object value : map.values()) 
		{
			if(value != null)
			{
				messageLen += ((byte[])value).length;
			}
		}
		return messageLen;
	}
	
	private void addFullMessageLen()
	{
		ByteBuffer lenBuf = ByteBuffer.allocate(4).putInt(messageLen);
		byte[] len = lenBuf.array();
		map.put("len", new byte[]{len[2],len[3]});
	}
	
	private void buildCommand()
	{
		message = new byte[messageLen];
		LinkedHashMap<String, Object> linkMap = new LinkedHashMap<String, Object>();
		linkMap.put("len", map.get("len"));
		linkMap.put("appId", map.get("appId"));
		linkMap.put("ssk", map.get("ssk"));
		linkMap.put("cmd", map.get("cmd"));
		linkMap.put("mode", map.get("mode"));
		if(map.get("data") != null)
		{
			linkMap.put("data", map.get("data"));
		}
		
		int i=0;
		for (Object value : linkMap.values()) 
		{
			byte[] data = (byte[]) value;
			System.arraycopy(data, 0, message, i, data.length);
			i+= data.length;
		}
	}
	
	public byte[] getMessage() 
	{
		addFullMessageLen();
		buildCommand();
		return message;
	}
}
