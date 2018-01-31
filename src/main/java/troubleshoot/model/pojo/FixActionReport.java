package troubleshoot.model.pojo;

import java.util.Vector;

import troubleshoot.states.Device;

public class FixActionReport
{
	private boolean isLogErrorFix;
	private Device deviceType;
	private String errorMessage = "";
	private String actionFileName = "";
	private Vector<Step> steps = null;
	
	public FixActionReport(boolean isLogErrorFix, Device deviceType, String errorMessage, String actionFileName)
	{
		this.isLogErrorFix = isLogErrorFix;
		this.deviceType = deviceType;
		this.errorMessage = errorMessage;
		this.actionFileName = actionFileName;
		steps = new Vector<Step>();
	}
	
	public FixActionReport(boolean isLogErrorFix, Device deviceType, String errorMessage)
	{
		this.isLogErrorFix = isLogErrorFix;
		this.deviceType = deviceType;
		this.errorMessage = errorMessage;
		steps = new Vector<Step>();
	}

	public boolean isLogErrorFix() {
		return isLogErrorFix;
	}

	public void setLogErrorFix(boolean isLogErrorFix) {
		this.isLogErrorFix = isLogErrorFix;
	}

	public Device getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Device deviceType) {
		this.deviceType = deviceType;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getActionFileName() {
		return actionFileName;
	}

	public void setActionFileName(String actionFileName) {
		this.actionFileName = actionFileName;
	}

	public Vector<Step> getSteps() {
		return steps;
	}

	public void setSteps(Vector<Step> steps) {
		this.steps = steps;
	}
}
