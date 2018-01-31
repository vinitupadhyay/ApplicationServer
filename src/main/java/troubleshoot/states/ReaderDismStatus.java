package troubleshoot.states;

import java.awt.Color;

public enum ReaderDismStatus implements StatusState
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
			return "Dismounted";
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
	},
	
	CODE_81
	{
		@Override
		public String getCode()
		{
			return "81";
		}

		@Override
		public String getResultText()
		{
			return "Card Inside Reader";
		}

		@Override
		public Color getColor()
		{
			return colorYellow;
		}
		
		@Override
		public String getStatusText()
		{
			return "";
		}

		@Override
		public Color getForColor()
		{
			return Color.black;
		}
	},
	
	CODE_83
	{
		@Override
		public String getCode()
		{
			return "83";
		}

		@Override
		public String getResultText()
		{
			return "Chip Card Inside Reader";
		}

		@Override
		public Color getColor()
		{
			return colorYellow;
		}
		
		@Override
		public String getStatusText()
		{
			return "";
		}

		@Override
		public Color getForColor()
		{
			return Color.black;
		}
	};
	
	private static final Color colorGreen = Color.decode("#53c653");
	private static final Color colorRed = Color.decode("#ff3333");
	private static final Color colorYellow = Color.decode("#ffff80");
}
