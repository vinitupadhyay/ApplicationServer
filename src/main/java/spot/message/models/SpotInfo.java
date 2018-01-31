package spot.message.models;

import troubleshoot.models.Connection;

public class SpotInfo 
{
	private static SpotInfo info = null;
	
	public static SpotInfo getInstance()
	{
		if(info == null)
		{
			info = new SpotInfo();
		}
		return info;
	}
	
	public void processIncommingMessage(SpotApdu apdu)
	{
		if(apdu.getCmd().equals("01"))
		{
			Connection.getInstance().processLoginAnswer(apdu);
		}
		else if(apdu.getCmd().equals("02"))
		{
			Connection.getInstance().processLogoutAnswer(apdu);
		}
		else if(apdu.getCmd().equals("0C"))
		{
			Connection.getInstance().processMaintainanceLoginModeAnswer(apdu);
		}
		else if(apdu.getCmd().equals("0D"))
		{
			Connection.getInstance().processServiceMenuAnswer(apdu);
		}
		else if(apdu.getCmd().equals("0E"))
		{
			Connection.getInstance().processChallengeNumberAnswer(apdu);
		}
	}
}
