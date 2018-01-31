package troubleshoot.states;

import java.awt.Color;

public enum UpmDismStatus implements StatusState
{
	CODE_00
	{
		@Override
		public String getCode()
		{
			return "00";
		}

		@Override
		public String getResultText()
		{
			return "Keyboard Dismounted";
		}

		@Override
		public Color getColor()
		{
			return colorRed;
		}
		
		@Override
		public String getStatusText()
		{
			return "Installation sensor deactivated";
		}

		@Override
		public Color getForColor()
		{
			return Color.white;
		}
	},
	
	CODE_80
	{
		@Override
		public String getCode()
		{
			return "80";
		}

		@Override
		public String getResultText()
		{
			return "Activated";
		}

		@Override
		public Color getColor()
		{
			return colorGreen;
		}
		
		@Override
		public String getStatusText()
		{
			return "Installation sensor activated";
		}

		@Override
		public Color getForColor()
		{
			return Color.black;
		}
	};
	
	private static final Color colorGreen = Color.decode("#53c653");
	private static final Color colorRed = Color.decode("#ff3333");
}
