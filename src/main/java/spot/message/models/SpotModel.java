package spot.message.models;

import troubleshoot.model.pojo.IncomingData;

public class SpotModel 
{
	public static final String INTERFACE_APP = "80";
	public static final String PINPAD_APP = "81";
	public static final String OPT_APP = "84";
	
	private static SpotModel spotModel = null;
	
	public SpotModel() 
	{
		spotModel = this;
	}
	
	public static SpotModel getInstance()
	{
		if(spotModel == null)
		{
			spotModel = new SpotModel();
		}
		return spotModel;
	}
	
	public void ProcessIncomingMessage(IncomingData data)
	{
		SpotApdu apdu = new SpotApdu(data.getData());
		
		if(apdu.getAppId().equals(INTERFACE_APP))
		{
			SpotInfo.getInstance().processIncommingMessage(apdu);
		}
		else if(apdu.getAppId().equals(PINPAD_APP))
		{
			PinpadApplication.getInstance().processIncommingMessage(apdu);
		}
		else if(apdu.getAppId().equals(OPT_APP))
		{
			OPTApplication.getInstance().processIncommingMessage(apdu);
		}
	}
}
