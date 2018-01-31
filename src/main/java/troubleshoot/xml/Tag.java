package troubleshoot.xml;

public class Tag
{
	private String name;
	private String value;
	private int xPos = 10;
	private int yPos = 0;
	private int width = 470;
	private int height = 30;
	private String action = null;
	private int alignment = -1;
	
	
	public Tag()
	{
		
	}
	
	public Tag(String name, String value, int xPos, int yPos, int width, int height, String action, String alignment)
	{
		this.name = name;
		this.value = value;
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.action = action;
		setAlignment(alignment);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getxPos() {
		return xPos;
	}
	public void setxPos(String xPos) {
		this.xPos = Integer.parseInt(xPos);
	}
	public int getyPos() {
		return yPos;
	}
	public void setyPos(String yPos) {
		this.yPos = Integer.parseInt(yPos);
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = Integer.parseInt(width);
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = Integer.parseInt(height);
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public int getAlignment() {
		return alignment;
	}
	public void setAlignment(String alignment)
	{
		if(alignment.equalsIgnoreCase("center"))
		{
			this.alignment = 0;
		}
		else if(alignment.equalsIgnoreCase("left"))
		{
			this.alignment = 2;
		}
		else if(alignment.equalsIgnoreCase("right"))
		{
			this.alignment = 4;
		}
	}
}
