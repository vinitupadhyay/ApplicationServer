package troubleshoot.model.pojo;

public class LogError
{
	private String severity;
	private String errorString;
	private String actionFilePath;
	private String category;
	private boolean isFound;
	
	public LogError(String severity, String errorString, String actionFilePath, String category)
	{
		this.severity = severity;
		this.errorString = errorString;
		this.actionFilePath = actionFilePath;
		this.category = category;
		
		isFound = false;
	}
	
	public String getSeverity() {
		return severity;
	}
	public String getErrorString() {
		return errorString;
	}
	public String getActionFilePath() {
		return actionFilePath;
	}
	public String getCategory() {
		return category;
	}
	public boolean getIsFound() {
		return isFound;
	}
	public void setIsFound(boolean isFound) {
		this.isFound = isFound;
	}
}
