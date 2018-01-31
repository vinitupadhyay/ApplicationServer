package troubleshoot.xml;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import troubleshoot.config.TroubleshootConfigurations;
import troubleshoot.controller.TroubleshootController;
import troubleshoot.model.pojo.FixActionReport;

public class ErrorFileParser 
{
	private final static Logger logger = Logger.getLogger(ErrorFileParser.class);
	private DocumentBuilder dBuilder;
	private String nodeName = null;
	private String errorFileName = null;


	public ErrorFileParser()
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try 
		{
			dBuilder = dbFactory.newDocumentBuilder();
		} 
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
	}

	public void setNodeName(String node)
	{
		nodeName=node;
	}

	public String getNodeName()
	{
		return nodeName;
	}

	public void setErrorStepsFileName(String fileName)
	{
		errorFileName=fileName;
	}

	public String getErrorStepsFileName()
	{
		return errorFileName;
	}

	public int searchError(String errorFileToParse , String status , String statusExt , String error, FixActionReport fixActionReport)
	{
		int ErrorCode=1;
		try 
		{	
			File inputFile = new File(errorFileToParse);
			if(inputFile.exists())
			{
				Document doc = dBuilder.parse(inputFile);
				doc.getDocumentElement().normalize();
				logger.info("Root element :"  + doc.getDocumentElement().getNodeName());
				NodeList nList = doc.getElementsByTagName("errorType");
				for (int temp = 0; temp < nList.getLength(); temp++)
				{
					Node nNode = nList.item(temp);
					System.out.println("\nCurrent Element :" + nNode.getNodeName());
					if (nNode.getNodeType() == Node.ELEMENT_NODE) 
					{
						Element eElement = (Element) nNode;
						logger.info(eElement .getElementsByTagName("status").item(0).getTextContent());
						logger.info(eElement .getElementsByTagName("statusExt").item(0).getTextContent());
						logger.info(eElement .getElementsByTagName("error").item(0).getTextContent());
						if(status.equalsIgnoreCase(eElement.getElementsByTagName("status").item(0).getTextContent()) &&
								statusExt.equalsIgnoreCase(eElement .getElementsByTagName("statusExt").item(0).getTextContent()) &&
								error.equalsIgnoreCase(eElement .getElementsByTagName("error").item(0).getTextContent()))
						{
							ErrorCode=0;
							String fileName = eElement.getElementsByTagName("fileName").item(0).getTextContent();
							fileName = TroubleshootConfigurations.getResourcesDirectory() + fileName;
							if(new File(fileName).exists())
							{
								fixActionReport.setActionFileName(fileName);
								setErrorStepsFileName(fileName);
								setNodeName("step");
								searchSteps(fixActionReport);
								break;
							}
							else
							{
								ErrorCode=2;
								break;
							}
							
						}
						else
						{
							//no error found in the file
							ErrorCode=1;
						}
					}

				}
			}
			else
			{
				ErrorCode=2;
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ErrorCode;
	}

	public void searchSteps(FixActionReport fixActionReport)
	{
		File stepsFile = new File(getErrorStepsFileName());
		Document stepsDoc;
		try 
		{
			stepsDoc = dBuilder.parse(stepsFile);
			stepsDoc.getDocumentElement().normalize();
			System.out.println("Root element :"  + stepsDoc.getDocumentElement().getNodeName());
			NodeList stepsList = stepsDoc.getDocumentElement().getChildNodes();
			logger.info("Number of childs :"+stepsList.getLength());
			if(stepsList.getLength()!=0)
			{
				// Find the elements to show on dialog
				for (int temp2 = 0; temp2 < stepsList.getLength(); temp2++)
				{
					Node stepNode = stepsList.item(temp2);
					if (stepNode.getNodeType() == Node.ELEMENT_NODE)
					{
						Element eElementSteps = (Element) stepNode; 
						System.out.println("Name : "  + eElementSteps .getNodeName());
						System.out.println("get node name :"+getNodeName());
						if(eElementSteps .getNodeName().equals(getNodeName()))
						{

							if(eElementSteps .getNodeName().equals("step"))
							{
								showStepsOnDialog(this, stepNode, fixActionReport);
							}
							else
							{
								System.out.println("Name : "  + eElementSteps .getNodeName());
								searchChildSteps(stepNode, fixActionReport);
							}
						}
					}
				}  
			}
		} 
		catch (SAXException | IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void showStepsOnDialog(ErrorFileParser fileParser, Node stepNode, FixActionReport fixActionReport)
	{
		NodeList uIElementsList=stepNode.getChildNodes();
		logger.info(uIElementsList.getLength());
		Vector<Tag> vecTags = new Vector<Tag>();
		
		for(int temp3 = 0; temp3 < uIElementsList.getLength();temp3++)
		{
			Node uIElementsNode = uIElementsList.item(temp3);
			if (uIElementsNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Tag tag = new Tag();
				Element uIElements = (Element) uIElementsNode; 
				System.out.println("Name : "  + uIElements .getNodeName() + "  Value = " + uIElements.getTextContent());
				
				tag.setName(uIElements.getNodeName());
				tag.setValue(uIElements.getTextContent());
				tag.setxPos(uIElements.getAttribute("x"));
				tag.setyPos(uIElements.getAttribute("y"));
				tag.setWidth(uIElements.getAttribute("width"));
				tag.setHeight(uIElements.getAttribute("height"));
				tag.setAction(uIElements.getAttribute("action"));
				tag.setAlignment(uIElements.getAttribute("align"));
				
				vecTags.addElement(tag);
			}
		}
		//show dialog
		TroubleshootController.getInstance().showErrorParsingStepsDialog(fileParser, vecTags, fixActionReport);
	}

	public void searchChildSteps(Node stepNode, FixActionReport fixActionReport)
	{
		NodeList uIElementsList=stepNode.getChildNodes();
		System.out.println("Size :"  + uIElementsList.getLength() );
		for(int temp4=0 ; temp4<uIElementsList.getLength() ; temp4++)
		{
			Node uIElementsNode = uIElementsList.item(temp4);
			if (uIElementsNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element uIElements = (Element) uIElementsNode; 
				System.out.println("Name : " + uIElements .getNodeName());
				if(uIElements .getNodeName().equals("step"))
				{
					showStepsOnDialog(this, uIElementsNode, fixActionReport);
				}
				else
				{
					System.out.println("get node name :"+getNodeName());
					if(uIElementsNode.getNodeName().equals(getNodeName()))
					{
						searchChildSteps(uIElementsNode, fixActionReport);
					}

				}
			}
		}
		setNodeName("stop");
	}
}
