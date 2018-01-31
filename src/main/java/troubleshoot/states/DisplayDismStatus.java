package troubleshoot.states;

import java.awt.Color;

public enum DisplayDismStatus implements StatusState
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
			return "Display Dismounted";
		}

		@Override
		public Color getColor()
		{
			return Color.red;
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
			return Color.green;
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
}
