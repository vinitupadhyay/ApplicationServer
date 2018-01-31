package troubleshoot.states;

import java.awt.Color;

public enum ReaderErrorStatus implements StatusState
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
			return "NA";
		}

		@Override
		public Color getColor()
		{
			return colorRed;
		}
		
		@Override
		public String getStatusText()
		{
			return "";
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
			return "Communication error but before the reader talk UPM";
		}

		@Override
		public Color getForColor()
		{
			return Color.white;
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
			return "Communication error after startup no TLS handshake";
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
			return "NA";
		}

		@Override
		public Color getColor()
		{
			return colorRed;
		}
		
		@Override
		public String getStatusText()
		{
			return "";
		}

		@Override
		public Color getForColor()
		{
			return Color.white;
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
			return "NA";
		}

		@Override
		public Color getColor()
		{
			return colorRed;
		}
		
		@Override
		public String getStatusText()
		{
			return "";
		}

		@Override
		public Color getForColor()
		{
			return Color.white;
		}
	},
	
	CODE_07
	{
		@Override
		public String getCode()
		{
			return "07";
		}

		@Override
		public String getResultText()
		{
			return "NA";
		}

		@Override
		public Color getColor()
		{
			return colorRed;
		}
		
		@Override
		public String getStatusText()
		{
			return "";
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
			return "Generic Fatal Error";
		}

		@Override
		public Color getColor()
		{
			return colorRed;
		}
		
		@Override
		public String getStatusText()
		{
			return "Generic Fatal Error";
		}

		@Override
		public Color getForColor()
		{
			return Color.white;
		}
	},
	
	CODE_09
	{
		@Override
		public String getCode()
		{
			return "09";
		}

		@Override
		public String getResultText()
		{
			return "NA";
		}

		@Override
		public Color getColor()
		{
			return colorRed;
		}
		
		@Override
		public String getStatusText()
		{
			return "";
		}

		@Override
		public Color getForColor()
		{
			return Color.white;
		}
	},
	
	CODE_0A
	{
		@Override
		public String getCode()
		{
			return "0A";
		}

		@Override
		public String getResultText()
		{
			return "NA";
		}

		@Override
		public Color getColor()
		{
			return colorRed;
		}
		
		@Override
		public String getStatusText()
		{
			return "";
		}

		@Override
		public Color getForColor()
		{
			return Color.white;
		}
	},
	
	CODE_0B
	{
		@Override
		public String getCode()
		{
			return "0B";
		}

		@Override
		public String getResultText()
		{
			return "NA";
		}

		@Override
		public Color getColor()
		{
			return colorRed;
		}
		
		@Override
		public String getStatusText()
		{
			return "";
		}

		@Override
		public Color getForColor()
		{
			return Color.white;
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
			return "Daily check";
		}

		@Override
		public Color getColor()
		{
			return colorYellow;
		}
		
		@Override
		public String getStatusText()
		{
			return "The reader is performing the Daily Check and is not available. Please wait the end of reader restart";
		}

		@Override
		public Color getForColor()
		{
			return Color.black;
		}
	},
	
	CODE_7F
	{
		@Override
		public String getCode()
		{
			return "7F";
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
			return "Generic error";
		}

		@Override
		public Color getForColor()
		{
			return Color.white;
		}
	};
	
	private static final Color colorGreen = Color.decode("#53c653");
	private static final Color colorRed = Color.decode("#ff3333");
	private static final Color colorYellow = Color.decode("#ffff80");
}
