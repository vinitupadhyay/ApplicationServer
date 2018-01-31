package troubleshoot.states;

import java.awt.Color;

public enum PrinterDismStatus implements StatusState
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
			return "No Info";
		}

		@Override
		public Color getColor()
		{
			return colorGreen;
		}
		
		@Override
		public String getStatusText()
		{
			return "No Information";
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
			return "Ticket present/ready";
		}

		@Override
		public Color getColor()
		{
			return colorYellow;
		}
		
		@Override
		public String getStatusText()
		{
			return "Ticket present/ready";
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
			return "Paper near to end";
		}

		@Override
		public Color getColor()
		{
			return colorYellow;
		}
		
		@Override
		public String getStatusText()
		{
			return "Paper near to end";
		}

		@Override
		public Color getForColor()
		{
			return Color.black;
		}
	};
	
	private static final Color colorGreen = Color.decode("#53c653");
	private static final Color colorYellow = Color.decode("#ffff80");
}
