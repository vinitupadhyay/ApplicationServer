package troubleshoot.states;

import java.awt.Color;

public enum ReaderComStatus  implements StatusState
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
			return "Reader Disabled";
		}

		@Override
		public Color getColor()
		{
			return colorGreen;
		}

		@Override
		public String getStatusText()
		{
			return "DISABLED: reader disabled, cards are not accepted.";
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
			return "ENABLED: reader enabled, waiting for card insertion.";
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
			return "BUSY: reader in use, processing the inserted card.";
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
			return "ERROR: reader in error, waiting for error recovery.";
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
			return "INOPERATIVE: reader in fatal error, cannot operate.";
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
			return "STARTUP: reader initializing device.";
		}

		@Override
		public Color getForColor()
		{
			return Color.black;
		}
	},
	
	CODE_06
	{
		@Override
		public String getCode()
		{
			return "06";
		}

		@Override
		public String getResultText()
		{
			return "Downloading Firmware";
		}

		@Override
		public Color getColor()
		{
			return colorYellow;
		}
		
		@Override
		public String getStatusText()
		{
			return "DOWNLOAD: reader downloading new firmware";
		}

		@Override
		public Color getForColor()
		{
			return Color.black;
		}
	},
	
	CODE_0C
	{
		@Override
		public String getCode()
		{
			return "0C";
		}

		@Override
		public String getResultText()
		{
			return "Daily Check";
		}

		@Override
		public Color getColor()
		{
			return colorYellow;
		}
		
		@Override
		public String getStatusText()
		{
			return "The reader is performing the Daily Check and is not available. Please wait the end of reader restart.";
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
