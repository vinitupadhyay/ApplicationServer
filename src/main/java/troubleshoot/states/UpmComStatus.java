package troubleshoot.states;

import java.awt.Color;

public enum UpmComStatus implements StatusState
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
			return "Keyboard Disabled";
		}

		@Override
		public Color getColor()
		{
			return colorGreen;
		}
		
		@Override
		public String getStatusText()
		{
			return "DISABLED: keyboard disabled, user input is not accepted.";
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
			return "Good";
		}

		@Override
		public Color getColor()
		{
			return colorGreen;
		}
		
		@Override
		public String getStatusText()
		{
			return "ENABLED: keyboard enabled, waiting for user input.";
		}

		@Override
		public Color getForColor()
		{
			return Color.black;
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
			return "Good";
		}

		@Override
		public Color getColor()
		{
			return colorGreen;
		}
		
		@Override
		public String getStatusText()
		{
			return "BUSY: keyboard in use, processing the user input.";
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
			return "ERROR: keyboard in error, waiting for error recovery";
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
			return "INOPERATIVE: keyboard in fatal error, cannot operate";
		}

		@Override
		public Color getForColor()
		{
			return Color.white;
		}
	},
	
	CODE_05
	{
		@Override
		public String getCode()
		{
			return "05";
		}

		@Override
		public String getResultText()
		{
			return "StartUp...";
		}

		@Override
		public Color getColor()
		{
			return colorYellow;
		}
		
		@Override
		public String getStatusText()
		{
			return "STARTUP: keyboard initializing device";
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
