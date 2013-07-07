package animatedSpriteEditor.files;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import animatedSpriteEditor.AnimatedSpriteEditor;
import static animatedSpriteEditor.AnimatedSpriteEditorSettings.*;
import animatedSpriteEditor.gui.AnimatedSpriteEditorGUI;
import animatedSpriteEditor.gui.PoseCanvas;
import animatedSpriteEditor.shapes.PoseurEllipse;
import animatedSpriteEditor.shapes.PoseurLine;
import animatedSpriteEditor.shapes.PoseurRectangle;
import animatedSpriteEditor.shapes.PoseurShape;
import animatedSpriteEditor.shapes.PoseurShapeType;
import animatedSpriteEditor.state.PoseurPose;
import animatedSpriteEditor.state.PoseurState;
import animatedSpriteEditor.state.PoseurStateManager;

/**
 * This class performs Pose object saving to an XML formatted .pose file, loading
 * such files, and exporting Pose objects to image files.
 * 
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
public class PoseIO 
{
    /**
     * This method loads the contents of the poseFileName .pose file into
     * the Pose for editing. Note that this file must validate against
     * the poseur_pose.xsd schema.
     * 
     * @param poseFileName The Pose to load for editing.
     */
    public void loadPose(String poseFileName)
    {
        // THIS WILL HELP US LOAD STUFF
        XMLUtilities xmlUtil = new XMLUtilities();
        
        // WE'RE GOING TO LOAD IT INTO THE POSE
        try
        {
            // LOAD THE XML FILE
            Document doc = xmlUtil.loadXMLDocument(poseFileName, POSE_SCHEMA);
            
            // AND THEN EXTRACT ALL THE DATA
            
            // LET'S START WITH THE POSE DIMENSIONS
            int poseWidth = xmlUtil.getIntData(doc, POSE_WIDTH_NODE);
            int poseHeight = xmlUtil.getIntData(doc, POSE_HEIGHT_NODE);
                        
            // WE'RE USING A TEMP POSE IN CASE SOMETHING GOES WRONG
            PoseurPose tempPose = new PoseurPose(poseWidth, poseHeight);
            LinkedList<PoseurShape> shapesList = tempPose.getShapesList();
            
            // LET'S GET THE SHAPE LIST
            NodeList shapeNodes = doc.getElementsByTagName(POSEUR_SHAPE_NODE);
            for (int i = 0; i < shapeNodes.getLength(); i++)
            {
                // GET THE NODE, THEN WE'LL EXTRACT DATA FROM IT
                // TO FILL IN THE shapeToAdd SHAPE
                Node node = shapeNodes.item(i);
                PoseurShape shapeToAdd;
                               
                // WHAT TYPE IS IT?
                Node geometryNode = xmlUtil.getChildNodeWithName(node, GEOMETRY_NODE);
                NamedNodeMap attributes = geometryNode.getAttributes();
                String shapeTypeText = attributes.getNamedItem(SHAPE_TYPE_ATTRIBUTE).getTextContent();
                PoseurShapeType shapeType = PoseurShapeType.valueOf(shapeTypeText);
                
                // A RECTANGLE?
                if (shapeType == PoseurShapeType.RECTANGLE)
                {
                    double x = Double.parseDouble(attributes.getNamedItem(X_ATTRIBUTE).getTextContent());
                    double y = Double.parseDouble(attributes.getNamedItem(Y_ATTRIBUTE).getTextContent());
                    double width = Double.parseDouble(attributes.getNamedItem(WIDTH_ATTRIBUTE).getTextContent());
                    double height = Double.parseDouble(attributes.getNamedItem(HEIGHT_ATTRIBUTE).getTextContent());
                    Rectangle2D.Double geometry = new Rectangle2D.Double(x, y, width, height);
                    shapeToAdd = new PoseurRectangle(geometry);
                }
                // AN ELLIPSE?
                else if (shapeType == PoseurShapeType.ELLIPSE)
                {
                    double x = Double.parseDouble(attributes.getNamedItem(X_ATTRIBUTE).getTextContent());
                    double y = Double.parseDouble(attributes.getNamedItem(Y_ATTRIBUTE).getTextContent());
                    double width = Double.parseDouble(attributes.getNamedItem(WIDTH_ATTRIBUTE).getTextContent());
                    double height = Double.parseDouble(attributes.getNamedItem(HEIGHT_ATTRIBUTE).getTextContent());
                    Ellipse2D.Double geometry = new Ellipse2D.Double(x, y, width, height);
                    shapeToAdd = new PoseurEllipse(geometry);                        
                }
                // IT MUST BE A LINE
                else
                {
                    double x1 = Double.parseDouble(attributes.getNamedItem(X1_ATTRIBUTE).getTextContent());
                    double y1 = Double.parseDouble(attributes.getNamedItem(Y1_ATTRIBUTE).getTextContent());
                    double x2 = Double.parseDouble(attributes.getNamedItem(X2_ATTRIBUTE).getTextContent());
                    double y2 = Double.parseDouble(attributes.getNamedItem(Y2_ATTRIBUTE).getTextContent());
                    Line2D.Double geometry = new Line2D.Double(x1, y1, x2, y2);
                    shapeToAdd = new PoseurLine(geometry);                   
                }
                
                // FIRST GET THE OUTLINE THICKNESS
                Node outlineNode = xmlUtil.getChildNodeWithName(node, OUTLINE_THICKNESS_NODE);
                int outlineThickness = Integer.parseInt(outlineNode.getTextContent());
                BasicStroke outlineStroke = new BasicStroke(outlineThickness);
                
                // THEN THE OUTLINE COLOR
                Color outlineColor = extractColor(xmlUtil, node, OUTLINE_COLOR_NODE);
                
                // THEN THE FILL COLOR
                Color fillColor = extractColor(xmlUtil, node, FILL_COLOR_NODE);

                // AND THE TRANSPARENCY
                Node alphaNode = xmlUtil.getChildNodeWithName(node, ALPHA_NODE);
                int alpha = Integer.parseInt(alphaNode.getTextContent());

                // AND FILL IN THE REST OF THE SHAPE DATA
                shapeToAdd.setAlpha(alpha);
                shapeToAdd.setFillColor(fillColor);
                shapeToAdd.setOutlineColor(outlineColor);
                shapeToAdd.setOutlineThickness(outlineStroke);
                
                // WE'VE LOADED THE SHAPE, NOW GIVE IT TO THE POSE
                shapesList.add(shapeToAdd);
            }
           
            // EVERYTHING HAS LOADED WITHOUT FAILING, SO LET'S
            // FIRST LOAD THE DATA INTO THE REAL POSE
            AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
            PoseurStateManager stateManager = singleton.getStateManager().getPoseurStateManager();
            PoseurPose actualPose = stateManager.getPose();
            actualPose.loadPoseData(tempPose);            
            
            // TELL THE USER ABOUT OUR SUCCESS
            AnimatedSpriteEditorGUI gui = singleton.getGUI();
            JOptionPane.showMessageDialog(
                gui,
                POSE_LOADED_TEXT,
                POSE_LOADED_TITLE_TEXT,
                JOptionPane.INFORMATION_MESSAGE);

            // AND ASK THE GUI TO UPDATE
            singleton.getStateManager().getPoseurStateManager().setState(PoseurState.SELECT_SHAPE_STATE);
        }
        catch(InvalidXMLFileFormatException | DOMException | HeadlessException ex)
        {
            // SOMETHING WENT WRONG LOADING THE .pose XML FILE
        	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        	AnimatedSpriteEditorGUI gui = singleton.getGUI();
            JOptionPane.showMessageDialog(
                gui,
                POSE_LOADING_ERROR_TEXT,
                POSE_LOADING_ERROR_TITLE_TEXT,
                JOptionPane.ERROR_MESSAGE);            
        }    
    }
    
    /**
     * This method saves the pose currently being edited to the poseFile. Note
     * that it will be saved as a .pose file, which is an XML-format that will
     * conform to the poseur_pose.xsd schema.
     * 
     * @param poseFile The file to write the pose to.
     * 
     * @return true if the file is successfully saved, false otherwise. It's
     * possible that another program could lock out ours from writing to it,
     * so we need to let the caller know when this happens.
     */
    public boolean savePose(File poseFile, boolean newPose)
    {
        // GET THE POSE AND ITS DATA THAT WE HAVE TO SAVE
    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        PoseurStateManager poseurStateManager = singleton.getStateManager().getPoseurStateManager();
        PoseurPose poseToSave = poseurStateManager.getPose();
        if(newPose)
        {
        	poseToSave = new PoseurPose(DEFAULT_POSE_WIDTH, DEFAULT_POSE_HEIGHT);
            PoseurStateManager stateManager = singleton.getStateManager().getPoseurStateManager();
            PoseurPose actualPose = stateManager.getPose();
            actualPose.loadPoseData(poseToSave);  
        }
        LinkedList<PoseurShape> shapesList = poseToSave.getShapesList();
        
        try 
        {
            // THESE WILL US BUILD A DOC
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            
            // FIRST MAKE THE DOCUMENT
            Document doc = docBuilder.newDocument();
            
            // THEN THE ROOT ELEMENT
            Element rootElement = doc.createElement(POSEUR_POSE_NODE);
            doc.appendChild(rootElement);
 
            // THEN MAKE AND ADD THE WIDTH, HEIGHT, AND NUM SHAPES
            Element poseWidthElement = makeElement(doc, rootElement, 
                    POSE_WIDTH_NODE, "" + poseToSave.getPoseWidth());
            Element poseHeightElement = makeElement(doc, rootElement, 
                    POSE_HEIGHT_NODE, "" + poseToSave.getPoseHeight());
            Element numShapesElement = makeElement(doc, rootElement,
                    NUM_SHAPES_NODE, "" + shapesList.size());

            // NOW LET'S MAKE THE SHAPES LIST AND ADD THAT TO THE ROOT AS WELL
            Element shapesListElement = makeElement(doc, rootElement,
                    SHAPES_LIST_NODE, "");
            
            // AND LET'S ADD ALL THE SHAPES TO THE SHAPES LIST
            for (PoseurShape shape : shapesList)
            {
                // MAKE THE SHAPE NODE AND ADD IT TO THE LIST
                Element shapeNodeElement = makeElement(doc, shapesListElement,
                        POSEUR_SHAPE_NODE, "");
                
                // NOW LET'S FILL IN THE SHAPE'S DATA
                
                // FIRST THE OUTLINE THICKNESS
                Element outlineThicknessNode = makeElement(doc, shapeNodeElement,
                        OUTLINE_THICKNESS_NODE, "" + (int)(shape.getOutlineThickness().getLineWidth()));
                
                // THEN THE OUTLINE COLOR
                Element outlineColorNode = makeColorNode(doc, shapeNodeElement,
                        OUTLINE_COLOR_NODE, shape.getOutlineColor());
                
                // AND THE FILL COLOR
                Element fillColorNode = makeColorNode(doc, shapeNodeElement,
                        FILL_COLOR_NODE, shape.getFillColor());
                
                // AND THE THE ALPHA VALUE
                Element alphaNode = makeElement(doc, shapeNodeElement,
                        ALPHA_NODE, "" + shape.getAlpha());
                
                // SHAPES HAVE BEEN GIVEN THE GIFT OF KNOWING HOW TO
                // ADD THEMESELVES AND THEIR DATA TO THE SHAPE ELEMENT
                Element geometryNode = doc.createElement(GEOMETRY_NODE);
                shape.addNodeData(geometryNode);         
                shapeNodeElement.appendChild(geometryNode);        
            }
            
            // THE TRANSFORMER KNOWS HOW TO WRITE A DOC TO
            // An XML FORMATTED FILE, SO LET'S MAKE ONE
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(poseFile);
            
            // SAVE THE POSE TO AN XML FILE
            transformer.transform(source, result);    
            
            
            if(newPose)
            {
            File currentSpriteTypeFile = new File(SPRITE_TYPE_PATH + singleton.getSpriteTypeName() + 
            					File.separatorChar + singleton.getSpriteTypeName() + XML_FILE_EXTENSION);
            
            try {
				doc = docBuilder.parse(currentSpriteTypeFile);
				Node imageListNode = doc.getElementsByTagName(IMAGES_LIST_NODE).item(0);
				NodeList imageFileNodes = imageListNode.getChildNodes();
				int len = imageFileNodes.getLength();
				int imageCount = 0;
				for(int i=1; i<len; i+=2)
				{
					int imageID = Integer.parseInt(imageFileNodes.item(i).getAttributes().item(1).getTextContent());
					if(imageID>imageCount)
					{
						imageCount = imageID;
					}
				}
				
	            singleton.getFileManager().getPoseurFileManager().setPoseID(imageCount+1);
				
	            Element imageFileNode = doc.createElement(IMAGE_FILE_NODE);
				imageFileNode.setAttribute(ID_ATTRIBUTE, ""+(imageCount+1));
				String imgFileName = singleton.getFileManager().getPoseurFileManager().getCurrentPoseName() + PNG_FILE_EXTENSION;
				imageFileNode.setAttribute(FILE_NAME_ATTRIBUTE, imgFileName);
				imageListNode.appendChild(imageFileNode);
				
				NodeList stateNodeList = doc.getElementsByTagName(STATE_NODE);
				for( int i = 0; i< stateNodeList.getLength(); i++)
				{
					Node stateElement = stateNodeList.item(i);
					if (stateElement.getTextContent().equals(singleton.getAnimationStateName()))
					{
						Node animationSequenceElement = stateElement.getNextSibling().getNextSibling();
						Element poseNode = doc.createElement(POSE_NODE);
						poseNode.setAttribute(IMAGE_ID_ATTRIBUTE, ""+(imageCount+1));
						poseNode.setAttribute(DURATION_ATTRIBUTE, singleton.getFileManager().getPoseurFileManager().getPoseDuration()+"");
						animationSequenceElement.appendChild(poseNode);
						break;
					}
				}
				
	           
	            source = new DOMSource(doc);
	            result = new StreamResult(currentSpriteTypeFile);
	            
	            // SAVE THE POSE TO AN XML FILE
	            transformer.transform(source, result); 
				
				
				
			} catch (SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            } 
            
            // WE MADE IT THIS FAR WITH NO ERRORS
            AnimatedSpriteEditorGUI gui = singleton.getGUI();
            JOptionPane.showMessageDialog(
                gui,
                POSE_SAVED_TEXT,
                POSE_SAVED_TITLE_TEXT,
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        catch(TransformerException | ParserConfigurationException | DOMException | HeadlessException ex)
        {
            // SOMETHING WENT WRONG WRITING THE XML FILE
        	AnimatedSpriteEditorGUI gui = singleton.getGUI();
            JOptionPane.showMessageDialog(
                gui,
                POSE_SAVING_ERROR_TEXT,
                POSE_SAVING_ERROR_TITLE_TEXT,
                JOptionPane.ERROR_MESSAGE);  
            return false;
        }    
    }
    
    /**
     * This method copies the current pose with an input duration and place
     * it to the end of the animation state.
     * @param currentPoseName the name of the pose to copy
     * @param poseID	the pose ID of the new pose
     * @return true if the pose is copied
     */
    public boolean copyPose(String currentPoseName, int poseID)
    {
    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
    	String spriteTypeName = singleton.getSpriteTypeName();
    	String stateName = singleton.getAnimationStateName();
    	
    	
    	String spriteTypeXMLFile = SPRITE_TYPE_PATH + spriteTypeName + "/" 
				+ spriteTypeName + XML_FILE_EXTENSION;
    	// FIRST RETRIEVE AND LOAD THE FILE INTO A TREE

        File currentSpriteTypeFile = new File(spriteTypeXMLFile);
        
        try {
        	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(currentSpriteTypeFile);
			
			NodeList stateNodes = doc.getElementsByTagName(STATE_NODE);

			Node imageListNode = doc.getElementsByTagName(IMAGES_LIST_NODE).item(0);
			NodeList imageFileNodes = imageListNode.getChildNodes();
			int len = imageFileNodes.getLength();
			int imageCount = 0;
			for(int i=1; i<len; i+=2)
			{
				int imageID = Integer.parseInt(imageFileNodes.item(i).getAttributes().item(1).getTextContent());
				if(imageID>imageCount)
				{
					imageCount = imageID;
				}
			}
			
			
			len = stateNodes.getLength();
			
			Node currentStateNode = stateNodes.item(0);	
			for(int i=0; i<len; i++)
			{
				if(stateNodes.item(i).getTextContent().equals(stateName))
				{
					currentStateNode = stateNodes.item(i);
					break;
				}
			}
			
	    	String currentStateName = singleton.getAnimationStateName();
	    	File fileToGetIndex;
	    	int index = -1;
	    	int i=0;
	    	while (index == -1)
	    	{
	    		fileToGetIndex = new File(SPRITE_TYPE_PATH + spriteTypeName + "/" +POSE_FOLDER_PATH + stateName + "_" + i + POSE_FILE_EXTENSION);
	    		if(!fileToGetIndex.exists())
	    		{
	    			index = i;
	    		}
	    		i++;	
	    	}

			
	    	Node animationSequenceNode = currentStateNode.getNextSibling().getNextSibling();
	    	NodeList poseNodes = animationSequenceNode.getChildNodes();
	    	len = poseNodes.getLength();
	    	int poseDuration = DEFAULT_POSE_DURATION;
	    	for(int j=1; j<len; j+=2)
	    	{
	    		NamedNodeMap currentPoseNodeAttr = poseNodes.item(j).getAttributes();
	    		String IDString = currentPoseNodeAttr.item(1).getTextContent();
	    		if(IDString.equals(""+poseID))
	    		{
	    			String duration = currentPoseNodeAttr.item(0).getTextContent();
	    			poseDuration = Integer.parseInt(duration);
	    			break;
	    		}
	    		
	    	}
	    	
			String newPoseName = currentStateName + "_" + index;		
			Element poseNode = doc.createElement(POSE_NODE);
			poseNode.setAttribute(DURATION_ATTRIBUTE, "" + poseDuration);
			poseNode.setAttribute(IMAGE_ID_ATTRIBUTE, "" + (imageCount+1));
			animationSequenceNode.appendChild(poseNode);
			
			Node imageNode = doc.getElementsByTagName(IMAGES_LIST_NODE).item(0);
			Element poseImage = doc.createElement(IMAGE_FILE_NODE);
			poseImage.setAttribute(FILE_NAME_ATTRIBUTE, newPoseName + PNG_FILE_EXTENSION);
			poseImage.setAttribute(ID_ATTRIBUTE, ""+ (imageCount+1));
			imageNode.appendChild(poseImage);
			
			File fileSource = new File(SPRITE_TYPE_PATH + spriteTypeName + File.separatorChar + IMAGE_FOLDER_PATH + currentPoseName + PNG_FILE_EXTENSION);
			File fileDest = new File(SPRITE_TYPE_PATH + spriteTypeName + File.separatorChar + IMAGE_FOLDER_PATH + newPoseName + PNG_FILE_EXTENSION);
			singleton.getFileManager().getEditorIO().copyFile(fileSource, fileDest);
			
			
			fileSource = new File(SPRITE_TYPE_PATH + spriteTypeName + File.separatorChar + POSE_FOLDER_PATH + currentPoseName + POSE_FILE_EXTENSION);
			fileDest = new File(SPRITE_TYPE_PATH + spriteTypeName + File.separatorChar + POSE_FOLDER_PATH + newPoseName + POSE_FILE_EXTENSION);
			singleton.getFileManager().getEditorIO().copyFile(fileSource, fileDest);
			
			
			
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(currentSpriteTypeFile);
            
            transformer.transform(source, result);  
            

            
            return true;
        	}
            catch(TransformerException | ParserConfigurationException | DOMException | HeadlessException | SAXException | IOException ex)
            {
                // SOMETHING WENT WRONG WRITING THE XML FILE
            	AnimatedSpriteEditorGUI gui = singleton.getGUI();
                JOptionPane.showMessageDialog(
                    gui,
                    SPRITE_TYPE_SAVING_ERROR_TEXT,
                    SPRITE_TYPE_SAVING_ERROR_TITLE_TEXT,
                    JOptionPane.ERROR_MESSAGE);   
                return false;
            } 
    }
    
    /**
     * This method deletes the current pose
     * @param currentPoseName the name of the current pose
     * @return true is the pose is deleted
     */
    public boolean deletePose(String currentPoseName)
    {
       	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
    	String spriteTypeName = singleton.getSpriteTypeName();
    	String stateName = singleton.getAnimationStateName();
    	
    	

    	String spriteTypeXMLFile = SPRITE_TYPE_PATH + spriteTypeName + "/" 
				+ spriteTypeName + XML_FILE_EXTENSION;
    	// FIRST RETRIEVE AND LOAD THE FILE INTO A TREE

        File currentSpriteTypeFile = new File(spriteTypeXMLFile);
        
        try {
        	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(currentSpriteTypeFile);
			
			NodeList stateNodes = doc.getElementsByTagName(STATE_NODE);
			NodeList imageNodes = doc.getElementsByTagName(IMAGE_FILE_NODE);
			int imageCount = imageNodes.getLength();			
			int len = stateNodes.getLength();
			
			Node currentStateNode = stateNodes.item(0);	
			for(int i=0; i<len; i++)
			{
				if(stateNodes.item(i).getTextContent().equals(stateName))
				{
					currentStateNode = stateNodes.item(i);
					break;
				}
			}
			
			String imageIDString = "";
			for(int i=0; i<imageCount; i++)
			{
				Node imageFileNode = imageNodes.item(i);
				String imageFileName = imageFileNode.getAttributes().item(0).getTextContent();
				if(imageFileName.equals(currentPoseName+PNG_FILE_EXTENSION))
				{
					imageIDString = imageFileNode.getAttributes().item(1).getTextContent();
					Node imageListNode = imageFileNode.getParentNode();
					imageListNode.removeChild(imageFileNode);
					break;
				}
			}
			
			Node animationSequenceNode = currentStateNode.getNextSibling().getNextSibling();
			NodeList poseNodes =  animationSequenceNode.getChildNodes();
			len = poseNodes.getLength();
			for(int i=1; i<len; i+=2)
			{
				Node poseNode = poseNodes.item(i);
				String poseID = poseNode.getAttributes().item(1).getNodeValue();
				if(poseID.equals(imageIDString))
				{
					animationSequenceNode.removeChild(poseNode);
					break;
				}
			}
			
			File fileSource = new File(SPRITE_TYPE_PATH + spriteTypeName + File.separatorChar + IMAGE_FOLDER_PATH + currentPoseName + PNG_FILE_EXTENSION);
			fileSource.setWritable(true);
			fileSource.delete();
			
			fileSource = new File(SPRITE_TYPE_PATH + spriteTypeName + File.separatorChar + POSE_FOLDER_PATH + currentPoseName + POSE_FILE_EXTENSION);
			fileSource.setWritable(true);
			fileSource.delete();
			
			
			
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(currentSpriteTypeFile);
            
            transformer.transform(source, result);             

            
            return true;
        	}
            catch(TransformerException | ParserConfigurationException | DOMException | HeadlessException | SAXException | IOException ex)
            {
                // SOMETHING WENT WRONG WRITING THE XML FILE
            	AnimatedSpriteEditorGUI gui = singleton.getGUI();
                JOptionPane.showMessageDialog(
                    gui,
                    DELETE_POSE_ERROR_TEXT,
                    DELETE_POSE_ERROR_TITLE_TEXT,
                    JOptionPane.ERROR_MESSAGE);   
                return false;
            }    	
    }
    
    /**
     * This method moves the current pose up.
     * @param poseID the poseID of the pose to move
     * @param backOrForth 0 as move down, 1 as move up
     * @return true if the pose is moved
     */
    public boolean movePose(int poseID, int backOrForth)
    {
    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
    	String spriteTypeName = singleton.getSpriteTypeName();
    	String stateName = singleton.getAnimationStateName();
    	boolean moved = false;
    	

    	String spriteTypeXMLFile = SPRITE_TYPE_PATH + spriteTypeName + "/" 
				+ spriteTypeName + XML_FILE_EXTENSION;
    	// FIRST RETRIEVE AND LOAD THE FILE INTO A TREE

        File currentSpriteTypeFile = new File(spriteTypeXMLFile);
        
        try {
        	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(currentSpriteTypeFile);
			
			NodeList stateNodes = doc.getElementsByTagName(STATE_NODE);

			int len = stateNodes.getLength();
			
			Node currentStateNode = stateNodes.item(0);	
			for(int i=0; i<len; i++)
			{
				if(stateNodes.item(i).getTextContent().equals(stateName))
				{
					currentStateNode = stateNodes.item(i);
					break;
				}
			}
			
			
			Node animationSequenceNode = currentStateNode.getNextSibling().getNextSibling();
			NodeList poseNodes =  animationSequenceNode.getChildNodes();
			len = poseNodes.getLength();
			
			if(backOrForth == 0)
			{
				if(Integer.parseInt(poseNodes.item(1).getAttributes().item(1).getNodeValue()) == poseID )
				{
					moved = true;
				}
			
				if(!moved)
				{
					for(int i=3; i<len; i+=2)
					{
						Node poseNode = poseNodes.item(i);
						String poseIDtoCheck = poseNode.getAttributes().item(1).getNodeValue();
								
						if(poseID == Integer.parseInt(poseIDtoCheck))
						{
							String tempDuration = poseNode.getAttributes().item(0).getNodeValue();
							NamedNodeMap poseAttrToSwap = poseNodes.item(i-2).getAttributes();
							poseNode.getAttributes().item(0).setNodeValue(poseAttrToSwap.item(0).getNodeValue());
							poseAttrToSwap.item(0).setNodeValue(tempDuration);
							poseNode.getAttributes().item(1).setNodeValue(poseAttrToSwap.item(1).getNodeValue());
							poseAttrToSwap.item(1).setNodeValue(poseIDtoCheck);
						
							moved = true;
							break;
						}	
					}
				}
			}
			
			else if(backOrForth == 1)
			{
				if(Integer.parseInt(poseNodes.item(len-2).getAttributes().item(1).getNodeValue()) == poseID )
				{
					moved = true;
				}
			
				if(!moved)
				{
					for(int i=1; i<len-2; i+=2)
					{
						Node poseNode = poseNodes.item(i);
						String poseIDtoCheck = poseNode.getAttributes().item(1).getNodeValue();
								
						if(poseID == Integer.parseInt(poseIDtoCheck))
						{
							String tempDuration = poseNode.getAttributes().item(0).getNodeValue();
							NamedNodeMap poseAttrToSwap = poseNodes.item(i+2).getAttributes();
							poseNode.getAttributes().item(0).setNodeValue(poseAttrToSwap.item(0).getNodeValue());
							poseAttrToSwap.item(0).setNodeValue(tempDuration);
							poseNode.getAttributes().item(1).setNodeValue(poseAttrToSwap.item(1).getNodeValue());
							poseAttrToSwap.item(1).setNodeValue(poseIDtoCheck);
						
							moved = true;
							break;
						}	
					}
				}
			}
			
			if(moved)
			{
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
				transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(currentSpriteTypeFile);
            
				transformer.transform(source, result);             
			}
            
            return moved;
        	}
            catch(TransformerException | ParserConfigurationException | DOMException | HeadlessException | SAXException | IOException | NumberFormatException ex)
            {
                // SOMETHING WENT WRONG WRITING THE XML FILE
            	AnimatedSpriteEditorGUI gui = singleton.getGUI();
                JOptionPane.showMessageDialog(
                    gui,
                    MOVE_POSE_ERROR_TEXT,
                    MOVE_POSE_ERROR_TITLE_TEXT,
                    JOptionPane.ERROR_MESSAGE);   
                return false;
            }    	
    }
    
    public boolean changePoseDuration(int poseID, int newDuration)
    {
    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
    	String spriteTypeName = singleton.getSpriteTypeName();
    	String stateName = singleton.getAnimationStateName();
    	boolean changed = false;
    	

    	String spriteTypeXMLFile = SPRITE_TYPE_PATH + spriteTypeName + "/" 
				+ spriteTypeName + XML_FILE_EXTENSION;
    	// FIRST RETRIEVE AND LOAD THE FILE INTO A TREE

        File currentSpriteTypeFile = new File(spriteTypeXMLFile);
        
        try {
        	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(currentSpriteTypeFile);
			
			NodeList stateNodes = doc.getElementsByTagName(STATE_NODE);
			int len = stateNodes.getLength();
			
			Node currentStateNode = stateNodes.item(0);	
			for(int i=0; i<len; i++)
			{
				if(stateNodes.item(i).getTextContent().equals(stateName))
				{
					currentStateNode = stateNodes.item(i);
					break;
				}
			}
			
			
			Node animationSequenceNode = currentStateNode.getNextSibling().getNextSibling();
			NodeList poseNodes =  animationSequenceNode.getChildNodes();
			len = poseNodes.getLength();
			
			for(int i=1; i<len; i+=2)
			{
				Node poseNode = poseNodes.item(i);
				String poseIDtoCheck = poseNode.getAttributes().item(1).getNodeValue();
								
				if(poseID == Integer.parseInt(poseIDtoCheck))
				{
					poseNode.getAttributes().item(0).setNodeValue("" + newDuration);
					changed = true;
				}	
			}
			
			if(changed)
			{
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
				transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(currentSpriteTypeFile);
            
				transformer.transform(source, result);             
			}
            
            return changed;
        	}
            catch(TransformerException | ParserConfigurationException | DOMException | HeadlessException | SAXException | IOException | NumberFormatException ex)
            {
                // SOMETHING WENT WRONG WRITING THE XML FILE
            	AnimatedSpriteEditorGUI gui = singleton.getGUI();
                JOptionPane.showMessageDialog(
                    gui,
                    MOVE_POSE_ERROR_TEXT,
                    MOVE_POSE_ERROR_TITLE_TEXT,
                    JOptionPane.ERROR_MESSAGE);   
                return false;
            }    	
    }
    
    /**
	     * Exports the current pose to an image file
	     * of the same name in the ./data/exported_images 
	     * directory.
	     * 
	     * @param currentPoseName Name of the pose to export.
	     */
	    public void savePoseImage(String currentPoseName, int id)
	    {
	        // WE DON'T HAVE TO ASK THE USER, WE'LL JUST EXPORT IT
	        // FIRST GET THE STUFF WE'LL NEED
	    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
	    	AnimatedSpriteEditorGUI gui = singleton.getGUI();
	        PoseurStateManager state = singleton.getStateManager().getPoseurStateManager();
	        PoseCanvas trueCanvas = gui.getTruePoseCanvas();
	        
	        PoseurPose pose = state.getPose();
	        
	        // THEN MAKE OUR IMAGE THE SAME DIMENSIONS AS THE POSE
	        BufferedImage imageToExport = new BufferedImage(    pose.getPoseWidth(), 
	                                                            pose.getPoseHeight(),
	                                                            BufferedImage.TYPE_INT_ARGB);
	        
	        // AND ASK THE CANVAS TO FILL IN THE IMAGE,
	        // SINCE IT ALREADY KNOWS HOW TO DRAW THE POSE
	        trueCanvas.paintToImage(imageToExport);
	
	        // AND NOW SAVE THE IMAGE TO A .png FILE
	        File imageFile = new File(SPRITE_TYPE_PATH + singleton.getSpriteTypeName() 
	        							+ File.separatorChar
	        							+ IMAGE_FOLDER_PATH 
	                                    + currentPoseName 
	                                    + PNG_FILE_EXTENSION);
	        
	        
	        // LET'S SAVE THE FILE
	        try
	        {
	            ImageIO.write(imageToExport, PNG_FORMAT_NAME, imageFile);
	        }
	        catch(IOException ioe)
	        {
	        	ioe.printStackTrace();
	        }       
	        
	        HashMap<Integer,Image> imageHM = singleton.getSpriteType().getSpriteImages();

	        imageHM.remove(id);
	        imageHM.put(id, imageToExport);
	        singleton.getGUI().updatePoseList();
	    }

	/**
	 * Exports the current pose to an image file
	 * of the same name in the ./data/exported_images 
	 * directory.
	 * 
	 * @param currentPoseName Name of the pose to export.
	 */
	public void exportPose(String currentPoseName)
	{
	    // WE DON'T HAVE TO ASK THE USER, WE'LL JUST EXPORT IT
	    // FIRST GET THE STUFF WE'LL NEED
		AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
		AnimatedSpriteEditorGUI gui = singleton.getGUI();
	    PoseurStateManager state = singleton.getStateManager().getPoseurStateManager();
	    PoseCanvas trueCanvas = gui.getTruePoseCanvas();
	    
	    PoseurPose pose = state.getPose();
	    
	    // THEN MAKE OUR IMAGE THE SAME DIMENSIONS AS THE POSE
	    BufferedImage imageToExport = new BufferedImage(    pose.getPoseWidth(), 
	                                                        pose.getPoseHeight(),
	                                                        BufferedImage.TYPE_INT_ARGB);
	    
	    // AND ASK THE CANVAS TO FILL IN THE IMAGE,
	    // SINCE IT ALREADY KNOWS HOW TO DRAW THE POSE
	    trueCanvas.paintToImage(imageToExport);
	
	    // AND NOW SAVE THE IMAGE TO A .png FILE
	    File imageFile = new File(EXPORTED_IMAGES_PATH 
	                                + currentPoseName 
	                                + PNG_FILE_EXTENSION);
	    // LET'S SAVE THE FILE
	    try
	    {
	        ImageIO.write(imageToExport, PNG_FORMAT_NAME, imageFile);
	        JOptionPane.showMessageDialog(
	            gui,
	            IMAGE_EXPORTED_TEXT + currentPoseName + POSE_FILE_EXTENSION,
	            IMAGE_EXPORTED_TITLE_TEXT,
	            JOptionPane.INFORMATION_MESSAGE);
	    }
	    catch(IOException ioe)
	    {
	        JOptionPane.showMessageDialog(
	            gui,
	            IMAGE_EXPORTING_ERROR_TEXT,
	            IMAGE_EXPORTING_ERROR_TITLE_TEXT,
	            JOptionPane.ERROR_MESSAGE);            
	    }            
	}

	/**
	 * This helper method will extract color information from the first found
	 * child node of parentNode with color data and use this to build and
	 * return a Color object.
	 * 
	 * @param xmlUtil This helps perform some XML extraction.
	 * 
	 * @param parentNode This node has a color child of some sort that
	 * will have the color data.
	 * 
	 * @param colorChildNodeName This is the name of the child node that
	 * has the color data.
	 * 
	 * @return A Color with the color data found in the color node.
	 */
	private Color extractColor(XMLUtilities xmlUtil, Node parentNode, String colorChildNodeName)
	{
	    // GET THE NODES WE'LL NEED 
	    Node colorNode = xmlUtil.getChildNodeWithName(parentNode, colorChildNodeName);
	    Node redNode = xmlUtil.getChildNodeWithName(colorNode, RED_NODE);
	    Node greenNode = xmlUtil.getChildNodeWithName(colorNode, GREEN_NODE);
	    Node blueNode = xmlUtil.getChildNodeWithName(colorNode, BLUE_NODE);
	
	    // CONVERT THE COLOR DATA TO INTEGERS
	    int red = Integer.parseInt(redNode.getTextContent());
	    int green = Integer.parseInt(greenNode.getTextContent());
	    int blue = Integer.parseInt(blueNode.getTextContent());
	
	    // BUILD AND RETURN THE COLOR OBJECT
	    Color extractedColor = new Color(red, green, blue);
	    return extractedColor;
	}

	/**
     * This helper method can be used to build a node to store color data. We'll
     * use this for building a Doc for the purpose of saving it to a file.
     * 
     * @param doc The document where we'll put the node.
     * 
     * @param parent The node where we'll add the color node as a child.
     * 
     * @param elementName The name of the color node to make. Colors may be
     * used for different purposes, so may have different names.
     * 
     * @param color The color data we'll use to build the node is in here.
     * 
     * @return A Element (i.e. Node) with the color information inside.
     */
    private Element makeColorNode(Document doc, Element parent, String elementName, Color color)
    {
        // MAKE THE COLOR NODE
        Element colorNode = makeElement(doc, parent, elementName, "");
        
        // AND THE COLOR COMPONENTS
        Element redNode = makeElement(doc, colorNode, RED_NODE, "" + color.getRed());
        Element greenNode = makeElement(doc, colorNode, GREEN_NODE, "" + color.getGreen());
        Element blueNode = makeElement(doc, colorNode, BLUE_NODE, "" + color.getBlue());
        
        // AND RETURN OUR NEW ELEMENT (NODE)
        return colorNode;
    }

    /**
     * This helper method builds elements (nodes) for us to help with building
     * a Doc which we would then save to a file.
     * 
     * @param doc The document we're building.
     * 
     * @param parent The node we'll add our new node to.
     * 
     * @param elementName The name of the node we're making.
     * 
     * @param textContent The data associated with the node we're making.
     * 
     * @return A node of name elementName, with textComponent as data, in the doc
     * document, with parent as its parent node.
     */
    private Element makeElement(Document doc, Element parent, String elementName, String textContent)
    {
        Element element = doc.createElement(elementName);
        element.setTextContent(textContent);
        parent.appendChild(element);
        return element;
    }
}