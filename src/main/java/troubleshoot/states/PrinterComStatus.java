package troubleshoot.states;

import java.awt.Color;

public enum PrinterComStatus implements StatusState
{
	CODE_01
	{
		@Override
		public String getCode()
		{
			return "01";
		}

		@Override
		public String getResultText()
		{
			return "Error";
		}

		@Override
		public Color getColor()
		{
			return colorRed;
		}
		
		@Override
		public String getStatusText()
		{
			return "Printer in error";
		}

		@Override
		public Color getForColor()
		{
			return Color.white;
		}
	},
	
	CODE_02
	{
		@Override
		public String getCode()
		{
			return "02";
		}

		@Override
		public String getResultText()
		{
			return "Ready to operate";
		}

		@Override
		public Color getColor()
		{
			return colorGreen;
		}
		
		@Override
		public String getStatusText()
		{
			return "Printer is ready to operate";
		}

		@Override
		public Color getForColor()
		{
			return Color.black;
		}
	},
	
	CODE_03
	{
		@Override
		public String getCode()
		{
			return "03";
		}

		@Override
		public String getResultText()
		{
			return "Busy";
		}

		@Override
		public Color getColor()
		{
			return colorGreen;
		}
		
		@Override
		public String getStatusText()
		{
			return "Printer is busy";
		}

		@Override
		public Color getForColor()
		{
			return Color.black;
		}
	},
	
	CODE_04
	{
		@Override
		public String getCode()
		{
			return "04";
		}

		@Override
		public String getResultText()
		{
			return "Starting up";
		}

		@Override
		public Color getColor()
		{
			return colorGreen;
		}
		
		@Override
		public String getStatusText()
		{
			return "Printer is starting up";
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
