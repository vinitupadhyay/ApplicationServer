package spot.message.models;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;

public class InputEnableRequest
{
	private Map<String, Object> map;
	private int messageLen;
	private byte[] message;
	
	public InputEnableRequest(Map<String, Object> param) 
	{
		map = param;
		getMessageLen();
	}
	
	public int getMessageLen() 
	{
		messageLen = 2;
		for (Object value : map.values()) 
		{
			messageLen += ((byte[])value).length;
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
		linkMap.put("minLen", map.get("minLen"));
		linkMap.put("maxLen", map.get("maxLen"));
		linkMap.put("autoEnter", map.get("autoEnter"));
		linkMap.put("delEnable", map.get("delEnable"));
		linkMap.put("timeout", map.get("timeout"));
		linkMap.put("beepEcho", map.get("beepEcho"));
		linkMap.put("echoMode", map.get("echoMode"));
		linkMap.put("winId", map.get("winId"));
		linkMap.put("tagId", map.get("tagId"));
		linkMap.put("ipMode", map.get("ipMode"));
		linkMap.put("alphaEnable", map.get("alphaEnable"));
		linkMap.put("format", map.get("format"));
		linkMap.put("spec", map.get("spec"));
		
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
