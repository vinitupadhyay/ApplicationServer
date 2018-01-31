package troubleshoot.states;

import java.awt.Color;

public enum UpmErrorStatus implements StatusState 
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
			return "Error initializing driver";
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
			return "Error";
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
			return "Error";
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
			return "Error";
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
			return "Error";
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
			return "Error";
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
			return "Error";
		}

		@Override
		public Color getForColor()
		{
			return Color.white;
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
			return "Error";
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