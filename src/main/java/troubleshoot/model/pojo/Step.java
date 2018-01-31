package troubleshoot.model.pojo;

import java.util.Vector;

public class Step
{
	private Vector<String> actionMessages = null;
	private String action = "";
	
	public Step()
	{
		actionMessages = new Vector<String>();
	}

	public Vector<String> getActionMessages() {
		return actionMessages;
	}

	public void setActionMessages(Vector<String> actionMessages) {
		this.actionMessages = actionMessages;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}
