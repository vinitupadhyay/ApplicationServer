package troubleshoot.states;

import java.awt.Color;

public interface StatusState
{
	public String getCode();
	public String getResultText();
	public String getStatusText();
	public Color getColor();
	public Color getForColor();
}
