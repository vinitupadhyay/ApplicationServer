package troubleshoot.models;

import troubleshoot.states.Device;
import troubleshoot.states.ReaderComStatus;
import troubleshoot.states.ReaderDismStatus;
import troubleshoot.states.ReaderErrorStatus;
import troubleshoot.states.StatusState;
import troubleshoot.states.UpmComStatus;
import troubleshoot.states.UpmDismStatus;
import troubleshoot.states.UpmErrorStatus;

public class SystemStatusInfo
{
	private static SystemStatusInfo info = null;
	
	private SystemStatusInfo()
	{
		
	}
	
	public static SystemStatusInfo getInstance()
	{
		if(info == null)
		{
			info = new SystemStatusInfo();
		}
		return info;
	}
	
	public StatusState getCommunicationInfo(Device deviceType, String state)
	{	
		if(Device.UPM == deviceType)
		{
			switch (state)
			{
				case "00":
					return UpmComStatus.CODE_00;
					
				case "01":
					return UpmComStatus.CODE_01;
					
				case "02":
					return UpmComStatus.CODE_02;
					
				case "03":
					return UpmComStatus.CODE_03;
					
				case "04":
					return UpmComStatus.CODE_04;
					
				case "05":
					return UpmComStatus.CODE_05;					
			}
		
		}
		else if(Device.CARD_READER == deviceType) 
		{
			switch (state)
			{
				case "00":
					return ReaderComStatus.CODE_00;
					
				case "01":
					return ReaderComStatus.CODE_01;
					
				case "02":
					return ReaderComStatus.CODE_02;
	
				case "03":
					return ReaderComStatus.CODE_03;
					
				case "04":
					return ReaderComStatus.CODE_04;
					
				case "05":
					return ReaderComStatus.CODE_05;
					
				case "06":
					return ReaderComStatus.CODE_06;
					
				case "0C":
					return ReaderComStatus.CODE_0C;
			}
		}
		return null;
	}
	
	public StatusState getDismountInfo(Device deviceType, String state)
	{
		if(Device.UPM == deviceType)
		{
			switch (state)
			{
				case "00":
					return UpmDismStatus.CODE_00;
					
				case "80":
					return UpmDismStatus.CODE_80;
			}
		}
		else if(Device.CARD_READER == deviceType) 
		{
			switch (state)
			{
				case "00":
					return ReaderDismStatus.CODE_00;
					
				case "80":
					return ReaderDismStatus.CODE_80;
					
				case "81":
					return ReaderDismStatus.CODE_81;
					
				case "83":
					return ReaderDismStatus.CODE_83;
			}
		}
		
		return null;
	}
	
	public StatusState getErrorInfo(Device deviceType, String state)
	{	
		if(Device.UPM == deviceType)
		{
			switch (state)
			{
				case "00":
					return UpmErrorStatus.CODE_00;
					
				case "01":
					return UpmErrorStatus.CODE_01;
					
				case "02":
					return UpmErrorStatus.CODE_02;
					
				case "03":
					return UpmErrorStatus.CODE_03;
					
				case "04":
					return UpmErrorStatus.CODE_04;
					
				case "06":
					return UpmErrorStatus.CODE_06;
					
				case "07":
					return UpmErrorStatus.CODE_07;
					
				case "09":
					return UpmErrorStatus.CODE_09;
					
				case "7F":
					return UpmErrorStatus.CODE_7F;	
			}
		}
		else if(Device.CARD_READER == deviceType) 
		{
			switch (state)
			{
				case "00":
					return ReaderErrorStatus.CODE_00;
					
				case "01":
					return ReaderErrorStatus.CODE_01;
					
				case "02":
					return ReaderErrorStatus.CODE_02;
					
				case "03":
					return ReaderErrorStatus.CODE_03;
					
				case "04":
					return ReaderErrorStatus.CODE_04;
					
				case "06":
					return ReaderErrorStatus.CODE_06;
					
				case "07":
					return ReaderErrorStatus.CODE_07;
					
				case "08":
					return ReaderErrorStatus.CODE_08;
					
				case "09":
					return ReaderErrorStatus.CODE_09;
					
				case "0A":
					return ReaderErrorStatus.CODE_0A;
					
				case "0B":
					return ReaderErrorStatus.CODE_0B;
					
				case "0C":
					return ReaderErrorStatus.CODE_0C;
					
				case "7F":
					return ReaderErrorStatus.CODE_7F;
					
				default:
					
			}
		}
		return null;
	}
}
