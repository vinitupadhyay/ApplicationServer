package troubleshoot.states;

import java.awt.Color;

public enum PrinterErrorStatus implements StatusState
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
			return "No Error";
		}

		@Override
		public Color getColor()
		{
			return colorGreen;
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
			return "Communication Error";
		}

		@Override
		public Color getColor()
		{
			return colorRed;
		}
		
		@Override
		public String getStatusText()
		{
			return "Device not responding";
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
			return "End of paper";
		}

		@Override
		public Color getColor()
		{
			return colorRed;
		}
		
		@Override
		public String getStatusText()
		{
			return "End of paper";
		}

		@Override
		public Color getForColor()
		{
			return Color.white;
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
			return "Head up";
		}

		@Override
		public Color getColor()
		{
			return colorRed;
		}
		
		@Override
		public String getStatusText()
		{
			return "Head up";
		}

		@Override
		public Color getForColor()
		{
			return Color.white;
		}
	},
	
	CODE_08
	{
		@Override
		public String getCode()
		{
			return "08";
		}

		@Override
		public String getResultText()
		{
			return "Head HW error";
		}

		@Override
		public Color getColor()
		{
			return colorRed;
		}
		
		@Override
		public String getStatusText()
		{
			return "Head HW error";
		}

		@Override
		public Color getForColor()
		{
			return Color.white;
		}
	},
	
	CODE_10
	{
		@Override
		public String getCode()
		{
			return "10";
		}

		@Override
		public String getResultText()
		{
			return "Paper jammed";
		}

		@Override
		public Color getColor()
		{
			return colorRed;
		}
		
		@Override
		public String getStatusText()
		{
			return "Printer is starting up";
		}

		@Override
		public Color getForColor()
		{
			return Color.white;
		}
	};
	
	private static final Color colorGreen = Color.decode("#53c653");
	private static final Color colorRed = Color.decode("#ff3333");
}
