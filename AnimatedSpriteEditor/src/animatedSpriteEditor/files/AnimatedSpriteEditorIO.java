package animatedSpriteEditor.files;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sprite_renderer.AnimationState;
import sprite_renderer.Pose;
import sprite_renderer.PoseList;
import sprite_renderer.SpriteType;
import animatedSpriteEditor.AnimatedSpriteEditor;
import static animatedSpriteEditor.AnimatedSpriteEditorSettings.*;
import animatedSpriteEditor.gui.AnimatedSpriteEditorGUI;
import animatedSpriteEditor.state.EditorState;
import animatedSpriteEditor.state.EditorStateManager;


/**
 * This class performs Pose object saving to an XML formatted .pose file, loading
 * such files, and exporting Pose objects to image files.
 * 
 * @author  sixin
 * @version 1.0
 */
public class AnimatedSpriteEditorIO 
{
	PoseIO poseIO;
	AnimatedSpriteEditorIO()
	{
		poseIO = new PoseIO();
	}
	
	/**
	 * Accessor method for the poseur IO.
	 * @return the poseur IO
	 */
	public PoseIO getPoseIO(){ return poseIO;}
 
	/**
     * This method extracts the pose of a state of a sprite from the provided
     * xml file argument and loads these pose into the poseList list.
     * 
     * @param currentTypeName the name of the selected sprite type 
     * 
     * @param currentState the state of the sprite
     * 
     * @param poseList the list we load all the pose to.
     * 
     */
    public void loadPoseList( 	String currentTypeName,
								String currentState,
								ArrayList<Pose> poseList) {
    	String xmlFile = SPRITE_TYPE_PATH + currentTypeName + "/" + currentTypeName+ XML_FILE_EXTENSION;
        
        String xsdFile = SPRITE_TYPE_PATH + SPRITE_TYPE_NODE + ".xsd";
        
        WhitespaceFreeXMLDoc cleanDoc;
		try {
			cleanDoc = loadXMLDocument(xmlFile, xsdFile);
		} catch (InvalidXMLFileFormatException e) {
			// TODO Auto-generated catch block
			return;
		}
                
        WhitespaceFreeXMLNode spriteTypeNode = cleanDoc.getRoot();
        ArrayList<WhitespaceFreeXMLNode> animationStateNodes = 
        	((spriteTypeNode.getChildOfType(ANIMATIONS_LIST_NODE)).getChildrenOfType(ANIMATION_STATE_NODE));
        for (int i=0; i<animationStateNodes.size(); i++){
        	if(animationStateNodes.get(i).getChildOfType(STATE_NODE).getData().equals(currentState)){
        		ArrayList<WhitespaceFreeXMLNode> animationSequenceNodes = 
        			animationStateNodes.get(i).getChildOfType(ANIMATION_SEQUENCE_NODE).getChildrenOfType(POSE_NODE);
        		for( int j=0; j<animationSequenceNodes.size(); j++){
        			Pose poseToAdd = new Pose(
        						Integer.parseInt(animationSequenceNodes.get(j).getAttributeValue(IMAGE_ID_ATTRIBUTE)),
            					Integer.parseInt(animationSequenceNodes.get(j).getAttributeValue(DURATION_ATTRIBUTE)));
        			poseList.add(poseToAdd);
        		}
        	}
        }
    }
    
    
    public void loadImageList( 	String currentTypeName) 
    {
    	
    		String xmlFile = SPRITE_TYPE_PATH + currentTypeName + "/" + currentTypeName+ XML_FILE_EXTENSION;

    		String xsdFile = SPRITE_TYPE_PATH + SPRITE_TYPE_NODE + ".xsd";

    		MediaTracker tracker = AnimatedSpriteEditor.getEditor().getGUI().getMediaTracker();
    		WhitespaceFreeXMLDoc cleanDoc;
    		try {
    				cleanDoc = loadXMLDocument(xmlFile, xsdFile);
    		} catch (InvalidXMLFileFormatException e) {
    			e.printStackTrace();
    		return;
    		}
            
            WhitespaceFreeXMLNode spriteTypeNode = cleanDoc.getRoot();
            ArrayList<WhitespaceFreeXMLNode> imageFileNodes = 
            	((spriteTypeNode.getChildOfType(IMAGES_LIST_NODE)).getChildrenOfType(IMAGE_FILE_NODE));
            for (int i=0; i<imageFileNodes.size();i++){
            	int id = Integer.parseInt(imageFileNodes.get(i).getAttributeValue(ID_ATTRIBUTE));
    			Image img = loadImageInBatch(	SPRITE_TYPE_PATH + currentTypeName+ "/" + IMAGE_FOLDER_PATH,
    						imageFileNodes.get(i).getAttributeValue(FILE_NAME_ATTRIBUTE),
    						tracker, id);
    			
    			AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
    			   HashMap<Integer,Image> imageHM = singleton.getSpriteType().getSpriteImages();

    		        imageHM.remove(id);
    		        imageHM.put(id, img);
    		        
    		        
    			AnimatedSpriteEditor.getEditor().getSpriteType().addImage(id, img);
    			String fileName = imageFileNodes.get(i).getAttributeValue(FILE_NAME_ATTRIBUTE);
    			fileName = fileName.substring(0, fileName.indexOf('.'));
    			fileName = fileName + ".pose";
    			AnimatedSpriteEditor.getEditor().getPoseFileNames().put(id, 
    					SPRITE_TYPE_PATH + currentTypeName+ "/" + POSE_FOLDER_PATH + fileName);
    			
            }
            
}
    
    public void loadSpriteType(String spriteTypeName) 
    {
            // BUILD THE PATH WHERE ITS XML FILE AND IMAGES SHOUDL BE
            String spriteTypeXMLFile = SPRITE_TYPE_PATH + spriteTypeName + "/" 
            							+ spriteTypeName + XML_FILE_EXTENSION;
            String spriteTypeXSDFile = SPRITE_TYPE_PATH + SPRITE_TYPE_NODE + ".xsd";
            
            // FIRST RETRIEVE AND LOAD THE FILE INTO A TREE
            WhitespaceFreeXMLDoc cleanDoc;
            
            try
            {
                cleanDoc = loadXMLDocument(spriteTypeXMLFile, spriteTypeXSDFile);
            }
            catch(InvalidXMLFileFormatException ixffe)
            {
                // COULD NOT LOAD THE SPRITE TYPE BECAUSE OF A FAULTY
                // XML DOC, SO WE'LL JUST SKIP IT
                return;
            }
     
            // GET THE ROOT NODE
            WhitespaceFreeXMLNode spriteTypeNode = cleanDoc.getRoot();

            // GET THE WIDTH
            WhitespaceFreeXMLNode widthNode = spriteTypeNode.getChildOfType(SPRITE_TYPE_WIDTH_NODE);
            String widthAsText = widthNode.getData();
            int width = Integer.parseInt(widthAsText);
            
            // GET THE HEIGHT
            WhitespaceFreeXMLNode heightNode = spriteTypeNode.getChildOfType(SPRITE_TYPE_HEIGHT_NODE);
            String heightAsText = heightNode.getData();
            int height = Integer.parseInt(heightAsText);
            
            // NOW LET'S USE THE LOADED DATA TO BUILD OUR SPRITE TPYE
            SpriteType spriteTypeToLoad = new SpriteType(width, height);
            
            // NOW LET'S GET THE IMAGES
            // FIRST LET'S GET THE IMAGES LIST
            WhitespaceFreeXMLNode imageListNode = spriteTypeNode.getChildOfType(IMAGES_LIST_NODE);
            
            // NEXT THE INDIVIDUAL IMAGES
            ArrayList<WhitespaceFreeXMLNode> imageFileNodes = imageListNode.getChildrenOfType(IMAGE_FILE_NODE);
            MediaTracker tracker = AnimatedSpriteEditor.getEditor().getGUI().getMediaTracker();
            Toolkit tk = Toolkit.getDefaultToolkit();
            for (WhitespaceFreeXMLNode imageFileNode : imageFileNodes)
            {
                String idAsText = imageFileNode.getAttributeValue(ID_ATTRIBUTE);
                int id = Integer.parseInt(idAsText);
                String fileName = imageFileNode.getAttributeValue(FILE_NAME_ATTRIBUTE);
                String imageFileNameAndPath = SPRITE_TYPE_PATH
                                            + spriteTypeName
                                            + "/"
                                            + fileName;
                Image loadedImage = tk.createImage(imageFileNameAndPath);
                tracker.addImage(loadedImage, id);
                spriteTypeToLoad.addImage(id, loadedImage);
            }
            
            // MAKER SURE ALL THE IMAGES ARE FULLY LOADED BEFORE GOING ON
            try {   tracker.waitForAll();   }
            catch(InterruptedException ie)
            {       ie.printStackTrace();   }
            
            // AND NOW LOAD ALL THE ANIMATIONS
            WhitespaceFreeXMLNode animationListNode = spriteTypeNode.getChildOfType(ANIMATIONS_LIST_NODE);
            ArrayList<WhitespaceFreeXMLNode> animationStateNodes = animationListNode.getChildrenOfType(ANIMATION_STATE_NODE);
            for (WhitespaceFreeXMLNode animationState : animationStateNodes)
            {
                // FIRST GET THE STATE NAME
                WhitespaceFreeXMLNode stateNode = animationState.getChildOfType(STATE_NODE);
                String state = stateNode.getData();
                
                // AND NOW ALL THE POSES
                AnimationState animState = AnimationState.valueOf(state);
                PoseList poseList = spriteTypeToLoad.addPoseList(animState);
                WhitespaceFreeXMLNode animationSequenceNode = animationState.getChildOfType(ANIMATION_SEQUENCE_NODE);
                ArrayList<WhitespaceFreeXMLNode> poseNodes = animationSequenceNode.getChildrenOfType(POSE_NODE);
                for (WhitespaceFreeXMLNode poseNode : poseNodes)
                {
                    String imageIDText = poseNode.getAttributeValue(IMAGE_ID_ATTRIBUTE);
                    int imageID = Integer.parseInt(imageIDText);
                    String durationText = poseNode.getAttributeValue(DURATION_ATTRIBUTE);
                    int duration = Integer.parseInt(durationText);
                    poseList.addPose(imageID, duration);
                }
            }         
          AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
          singleton.setSpriteType(spriteTypeToLoad);
          singleton.setSpriteTypeName(spriteTypeName);
          singleton.getFileManager().getEditorIO().loadImageList(spriteTypeName);
          singleton.getStateManager().setState(EditorState.SELECT_ANIMATION_STATE);
    }
    
    public boolean saveSpriteType(File spriteTypeFile, SpriteType spriteTypeToSave, String animationStateName) 
    {
        // GET THE POSE AND ITS DATA THAT WE HAVE TO SAVE
    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        
        try 
        {
            // THESE WILL US BUILD A DOC
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            
            // FIRST MAKE THE DOCUMENT
            Document doc = docBuilder.newDocument();
            
            // THEN THE ROOT ELEMENT
            Element rootElement = doc.createElement(SPRITE_TYPE_NODE);
            doc.appendChild(rootElement);
 
            // THEN MAKE AND ADD THE WIDTH, HEIGHT, AND NUM SHAPES
            Element spriteTypeWidthElement = makeElement(doc, rootElement, 
                    SPRITE_TYPE_WIDTH_NODE, SPRITE_TYPE_WIDTH);
            Element spriteTypeHeightElement = makeElement(doc, rootElement, 
                    SPRITE_TYPE_HEIGHT_NODE, SPRITE_TYPE_HEIGHT);
            Element imageListElement = makeElement(doc, rootElement, 
                    IMAGES_LIST_NODE, null);
            Element animationListElement = makeElement(doc, rootElement, 
                    ANIMATIONS_LIST_NODE, null);
            Element animationStateElement = makeElement(doc, animationListElement, 
                    ANIMATION_STATE_NODE, null);
            Element stateElement = makeElement(doc, animationStateElement, 
                    STATE_NODE, animationStateName);
            Element animationSequenceElement = makeElement(doc, animationStateElement, 
                    ANIMATION_SEQUENCE_NODE, null);
            
            
            
            
            // THE TRANSFORMER KNOWS HOW TO WRITE A DOC TO
            // An XML FORMATTED FILE, SO LET'S MAKE ONE
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(spriteTypeFile);
            
            // SAVE THE POSE TO AN XML FILE
            transformer.transform(source, result);    
            
            // WE MADE IT THIS FAR WITH NO ERRORS
            AnimatedSpriteEditorGUI gui = singleton.getGUI();
            JOptionPane.showMessageDialog(
                gui,
                SPRITE_TYPE_SAVED_TEXT,
                SPRITE_TYPE_SAVED_TITLE_TEXT,
                JOptionPane.INFORMATION_MESSAGE);
            
   //lalalalalal idk         singleton.getStateManager().setState(EditorState.SELECT_ANIMATION_STATE);
            return true;
        }
        catch(TransformerException | ParserConfigurationException | DOMException | HeadlessException ex)
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
    
    public boolean copyAnimationState(String newAnimationStateName)
    {
    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
    	String spriteTypeName = singleton.getSpriteTypeName();
    	
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
			
			String currentAnimationStateName = singleton.getAnimationStateName();
			int currentStateNameLength = currentAnimationStateName.length();
			
			Node currentStateNode = stateNodes.item(0);	
			NodeList currentPoseNodes = currentStateNode.getNextSibling().getNextSibling().getChildNodes();
			for(int i=0; i<len; i++)
			{
				if(stateNodes.item(i).getTextContent().equals(currentAnimationStateName))
				{
					currentStateNode = stateNodes.item(i);
					currentPoseNodes = currentStateNode.getNextSibling().getNextSibling().getChildNodes();
					break;
				}
			}
			
			Element animationStateNode = doc.createElement(ANIMATION_STATE_NODE);
			Element stateNode = doc.createElement(STATE_NODE);
			stateNode.setTextContent(newAnimationStateName);
			
			imageListNode = doc.getElementsByTagName(IMAGES_LIST_NODE).item(0);
			Node animationListNodes = doc.getElementsByTagName(ANIMATIONS_LIST_NODE).item(0);
			Element animationSequenceNode = doc.createElement(ANIMATION_SEQUENCE_NODE);
			animationStateNode.appendChild(stateNode);
			animationStateNode.appendChild(animationSequenceNode);
			animationListNodes.appendChild(animationStateNode);
			
			
			
			NodeList poseNodes = currentStateNode.getNextSibling().getNextSibling().getChildNodes();
			len = poseNodes.getLength();
			for(int i=1; i<len; i+=2)
			{
				String duration = currentPoseNodes.item(i).getAttributes().item(0).getTextContent();
				int id = Integer.parseInt(currentPoseNodes.item(i).getAttributes().item(1).getTextContent());
				String fileName = "";
				
				for(int j=0; j<imageNodes.getLength(); j++)
				{
					String imageID = imageNodes.item(j).getAttributes().item(1).getTextContent();
					if(imageID.equals(""+id))
					{
						fileName = imageNodes.item(j).getAttributes().item(0).getTextContent();
					}
				}
				
				String newFileName = newAnimationStateName + fileName.substring( currentStateNameLength, fileName.length());
				Element newPoseNode = doc.createElement(POSE_NODE);
				newPoseNode.setAttribute(DURATION_ATTRIBUTE, duration);
				newPoseNode.setAttribute(IMAGE_ID_ATTRIBUTE, ""+(imageCount+i/2+1));
				animationSequenceNode.appendChild(newPoseNode);
				Element newImageNode = doc.createElement(IMAGE_FILE_NODE);
				newImageNode.setAttribute(FILE_NAME_ATTRIBUTE, newFileName);
				newImageNode.setAttribute(ID_ATTRIBUTE, ""+(imageCount+i/2+1));
				
				File fileSource = new File(SPRITE_TYPE_PATH + spriteTypeName + File.separatorChar + IMAGE_FOLDER_PATH + fileName);
				File fileDest = new File(SPRITE_TYPE_PATH + spriteTypeName + File.separatorChar + IMAGE_FOLDER_PATH + newFileName);
				copyFile(fileSource, fileDest);
				
				
				
				fileName = fileName.substring(0, fileName.lastIndexOf('.')) + POSE_FILE_EXTENSION;
				newFileName = newFileName.substring(0, newFileName.lastIndexOf('.')) + POSE_FILE_EXTENSION;
				fileSource = new File(SPRITE_TYPE_PATH + spriteTypeName + File.separatorChar + POSE_FOLDER_PATH + fileName);
				fileDest = new File(SPRITE_TYPE_PATH + spriteTypeName + File.separatorChar + POSE_FOLDER_PATH + newFileName);
				copyFile(fileSource, fileDest);
				
				animationSequenceNode.appendChild(newPoseNode);
				imageListNode.appendChild(newImageNode);
				
			}
			
			
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
                    ANIMATION_STATE_SAVING_ERROR_TEXT,
                    ANIMATION_STATE_SAVING_ERROR_TITLE_TEXT,
                    JOptionPane.ERROR_MESSAGE);   
                return false;
            } 
    }
    
    public boolean saveAnimationState(String spriteTypeName, String animationStateName)
    {
    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();

    	String spriteTypeXMLFile = SPRITE_TYPE_PATH + spriteTypeName + "/" 
				+ spriteTypeName + XML_FILE_EXTENSION;
    	// FIRST RETRIEVE AND LOAD THE FILE INTO A TREE
    	
        File currentSpriteTypeFile = new File(spriteTypeXMLFile);
        
        try {
        	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(currentSpriteTypeFile);
			
			Node animationListNode = doc.getElementsByTagName(ANIMATIONS_LIST_NODE).item(0);
			Element animationStateNode = doc.createElement(ANIMATION_STATE_NODE);
			Element stateNode = doc.createElement(STATE_NODE);
			stateNode.setTextContent(animationStateName);
			Element animationSequenceNode = doc.createElement(ANIMATION_SEQUENCE_NODE);
			animationStateNode.appendChild(stateNode);
			animationStateNode.appendChild(animationSequenceNode);
			animationListNode.appendChild(animationStateNode);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(currentSpriteTypeFile);
            
            // SAVE THE POSE TO AN XML FILE
            transformer.transform(source, result); 
			
			
			} catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
				e.printStackTrace();
				AnimatedSpriteEditorGUI gui = singleton.getGUI();
	            JOptionPane.showMessageDialog(
	                gui,
	                ANIMATION_STATE_SAVING_ERROR_TEXT,
	                ANIMATION_STATE_SAVING_ERROR_TITLE_TEXT,
	                JOptionPane.ERROR_MESSAGE);  
	            return false;
			}      
        AnimatedSpriteEditorGUI gui = singleton.getGUI();
        JOptionPane.showMessageDialog(
                gui,
                ANIMATION_STATE_SAVED_TEXT,
                ANIMATION_STATE_SAVED_TITLE_TEXT,
                JOptionPane.INFORMATION_MESSAGE);
            
 //lalala i still dont know       singleton.getStateManager().setState(EditorState.SELECT_ANIMATION_STATE);
        return true; 
    }
    
    public boolean renameAnimationState(String spriteTypeName, String newStateName)
    {
    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
    	
    	String spriteTypeXMLFile = SPRITE_TYPE_PATH + spriteTypeName + "/" 
				+ spriteTypeName + XML_FILE_EXTENSION;
    	
    	String currentStateName = singleton.getAnimationStateName();
    	int currentStateNameLength = currentStateName.length();
        File currentSpriteTypeFile = new File(spriteTypeXMLFile);
        
        try {
        	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(currentSpriteTypeFile);
			
			NodeList stateNodes = doc.getElementsByTagName(STATE_NODE);
			int len = stateNodes.getLength();
			ArrayList<String> ids = new ArrayList<String>();
			for(int i=0; i<len; i++)
			{	
				Node stateNode = stateNodes.item(i);
			
				if (stateNode.getTextContent().equals(currentStateName))
				{
					stateNode.setTextContent(newStateName);
					
					NodeList posesList = stateNode.getNextSibling().getNextSibling().getChildNodes();
					len = posesList.getLength();
					for(int j = 1; j<len; j+=2)
					{
						ids.add(posesList.item(j).getAttributes().item(1).getTextContent());
					}
					
					NodeList imagesFileNodes = doc.getElementsByTagName(IMAGE_FILE_NODE);
					len = imagesFileNodes.getLength();
					for(int j = 0, k=0; k<ids.size(); j++)
					{
						NamedNodeMap imageFileNodesAttr = imagesFileNodes.item(j).getAttributes();
						String ID = imageFileNodesAttr.item(1).getTextContent();
						if(ID.equals(ids.get(k)))
						{
							String newFileName = imageFileNodesAttr.item(0).getTextContent();
							File oldImageFile = new File(SPRITE_TYPE_PATH + spriteTypeName + File.separatorChar + IMAGE_FOLDER_PATH + newFileName);
							File oldPoseFile = new File(SPRITE_TYPE_PATH + spriteTypeName + File.separatorChar + 
									POSE_FOLDER_PATH + newFileName.substring(0, newFileName.lastIndexOf('.')) + POSE_FILE_EXTENSION);
							
							newFileName = newFileName.substring(currentStateNameLength, newFileName.length());
							newFileName = newStateName + newFileName;
							imageFileNodesAttr.item(0).setNodeValue(newFileName);
							
							File newImageFile = new File(SPRITE_TYPE_PATH + spriteTypeName + File.separatorChar + IMAGE_FOLDER_PATH + newFileName);
							File newPoseFile = new File(SPRITE_TYPE_PATH + spriteTypeName + File.separatorChar + 
									POSE_FOLDER_PATH + newFileName.substring(0, newFileName.lastIndexOf('.')) + POSE_FILE_EXTENSION);
							
							oldImageFile.renameTo(newImageFile);
							oldPoseFile.renameTo(newPoseFile);
							k++;
						}
					}
					
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
		            Transformer transformer = transformerFactory.newTransformer();
		            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
		            transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
		            DOMSource source = new DOMSource(doc);
		            StreamResult result = new StreamResult(currentSpriteTypeFile);
		            
		            // SAVE THE POSE TO AN XML FILE
		            transformer.transform(source, result); 
		            
		            AnimatedSpriteEditorGUI gui = singleton.getGUI();
		            JOptionPane.showMessageDialog(
		                    gui,
		                    ANIMATION_STATE_SAVED_TEXT,
		                    ANIMATION_STATE_SAVED_TITLE_TEXT,
		                    JOptionPane.INFORMATION_MESSAGE);
		            
		            return true; 
				}
			}
		} catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
			AnimatedSpriteEditorGUI gui = singleton.getGUI();
	        JOptionPane.showMessageDialog(
	                gui,
	                ANIMATION_STATE_SAVING_ERROR_TEXT,
	                ANIMATION_STATE_SAVING_ERROR_TITLE_TEXT,
	                JOptionPane.ERROR_MESSAGE);  
	         return false;
		}      
    	return false;
    }
    

    public boolean deletedAnimationState(String stateName)
    {
    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
    	String spriteTypeName = singleton.getSpriteTypeName();
    	
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
			Node imageListNode = imageNodes.item(0).getParentNode();
			int len = stateNodes.getLength();
			String currentAnimationStateName = singleton.getAnimationStateName();
			
			Node currentStateNode = stateNodes.item(0);	
			NodeList currentPoseNodes = currentStateNode.getNextSibling().getNextSibling().getChildNodes();
			for(int i=0; i<len; i++)
			{
				if(stateNodes.item(i).getTextContent().equals(currentAnimationStateName))
				{
					currentStateNode = stateNodes.item(i);
					currentPoseNodes = currentStateNode.getNextSibling().getNextSibling().getChildNodes();
					break;
				}
			}
			
			NodeList poseNodes = currentStateNode.getNextSibling().getNextSibling().getChildNodes();
			len = poseNodes.getLength();
			for(int i=1; i<len; i+=2)
			{
				int id = Integer.parseInt(currentPoseNodes.item(i).getAttributes().item(1).getTextContent());
			 	String fileName = imageNodes.item(id-1).getAttributes().item(0).getTextContent();
	
				Node imageFileNodeToDelete = imageNodes.item(id-1);
				imageListNode.removeChild(imageFileNodeToDelete);
				File fileSource = new File(SPRITE_TYPE_PATH + spriteTypeName + File.separatorChar + IMAGE_FOLDER_PATH + fileName);
				fileSource.setWritable(true);
				fileSource.delete();
				
				
				
				fileName = fileName.substring(0, fileName.lastIndexOf('.')) + ".pose";
				fileSource = new File(SPRITE_TYPE_PATH + spriteTypeName + File.separatorChar + POSE_FOLDER_PATH + fileName);
				fileSource.setWritable(true);
				fileSource.delete();
				
			}
			
			Node animationStateNode = currentStateNode.getParentNode();
			Node animationListNode = animationStateNode.getParentNode();
			animationListNode.removeChild(animationStateNode);
			
			
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
                    DELETE_ANIMATION_STATE_ERROR_TEXT,
                    DELETE_ANIMATION_STATE_ERROR_TITLE_TEXT,
                    JOptionPane.ERROR_MESSAGE);   
                return false;
            }   
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
    
    /**
     * This method reads in the xmlFile, validates it against the
     * schemaFile, and if valid, loads it into a WhitespaceFreeXMLDoc
     * and returns it, which helps because that's a much easier
     * format for us to deal with.
     * 
     * @param xmlFile Path and name of xml file to load.
     * 
     * @param schemaFile Path and name of schema file to use for validation.
     * 
     * @return A WhitespaceFreeXMLDoc object fully loaded with the data found
     * in the xmlFile. We like these because they don't have all the clutter
     * that whitespace requires.
     * 
     * @throws InvalidXMLFileFormatException Thrown if the xml file validation fails.
     */
    public WhitespaceFreeXMLDoc loadXMLDocument(String xmlFile,
                                                String schemaFile)
            throws InvalidXMLFileFormatException
    {
        // FIRST LET'S VALIDATE IT
        boolean validDoc = validateXMLDoc(xmlFile, schemaFile);
        if (!validDoc)
        {
            // FAIL
            throw new InvalidXMLFileFormatException(xmlFile, schemaFile);
        }

        // THIS IS JAVA API STUFF
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try
        {            
            // FIRST RETRIEVE AND LOAD THE FILE INTO A TREE
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xmlDoc = db.parse(xmlFile);
            
            // THEN PUT IT INTO A FORMAT WE LIKE
            WhitespaceFreeXMLDoc cleanDoc = new WhitespaceFreeXMLDoc();
            cleanDoc.loadDoc(xmlDoc);
            return cleanDoc;
        }
        // THESE ARE XML-RELATED ERRORS THAT COULD HAPPEN DURING
        // LOADING AND PARSING IF THE XML FILE IS NOT WELL FORMED
        // OR IS NOW WHERE AND WHAT WE SAY IT IS
        catch(ParserConfigurationException pce)
        {
            pce.printStackTrace();
            return null;
        }
        catch(SAXException se)
        {
            se.printStackTrace();
            return null;
        }
        catch(IOException io)
        {
            io.printStackTrace();
            return null;
        }           
    }
    
    /**
     * This method is used to load an individual image among many
     * in a batch. The reason for batch loading is to use a single
     * MediaTracker to ensure that all images in the batch are fully
     * loaded before continuing.
     * 
     * @param path Relative path to the image location from the working directory.
     * 
     * @param fileName File name of the image to load
     * 
     * @param tracker The MediaTracker to ensure the image is fully loaded.
     * 
     * @param id A unique number for the image so the tracker can identify it.
     * 
     * @return A reference to the Image represented by the path + fileName.
     */
	public Image loadImageInBatch( String path, String fileName, 
									MediaTracker tracker, int id)
	{
		Toolkit tk = Toolkit.getDefaultToolkit();
		String fullFileNameWithPath = path + fileName;
		Image img = tk.createImage(fullFileNameWithPath);
		tracker.addImage(img, id);
		return img;
	}
	
    /**
     * This method validates the xmlDocNameAndPath doc against the 
     * xmlSchemaNameAndPath schema and returns true if valid, false
     * otherwise. Note that this is taken directly (with comments)
     * from and example on the IBM site with only slight modifications.
     *      
     * @param xmlDocNameAndPath XML Doc to validate
     * 
     * @param xmlSchemaNameAndPath XML Schema to use in validation
     * 
     * @return true if the xml doc is validate, false if it does not.
     */
    public boolean validateXMLDoc(  String xmlDocNameAndPath,
                                    String xmlSchemaNameAndPath)
    {
        try
        {
            // 1. Lookup a factory for the W3C XML Schema language
            SchemaFactory factory = 
                    SchemaFactory.newInstance(SCHEMA_STANDARD_SPEC_URL);
            
            // 2. Compile the schema. 
            // Here the schema is loaded from a java.io.File, but you could use 
            // a java.net.URL or a javax.xml.transform.Source instead.
            File schemaLocation = new File(xmlSchemaNameAndPath);
            Schema schema = factory.newSchema(schemaLocation);
            
            // 3. Get a validator from the schema.
            Validator validator = schema.newValidator();
            
            // 4. Parse the document you want to check.
            Source source = new StreamSource(xmlDocNameAndPath);
            
            // 5. Check the document
            validator.validate(source);
            return true;
        }
        catch (Exception e) 
        {
            return false;
        }          
    }
    
    /**
     * This method copies a source file to a destination file.
     * @param sourceFile 	the file to copy
     * @param destFile		the file copy to
     * @throws IOException 
     */
    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if(!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
    }
}