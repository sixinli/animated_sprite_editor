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
import java.io.IOException;
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
import sprite_renderer.PoseList;
import sprite_renderer.SpriteType;
import animatedSpriteEditor.AnimatedSpriteEditor;
import static animatedSpriteEditor.AnimatedSpriteEditorSettings.*;
import animatedSpriteEditor.gui.EditorCanvas;
import animatedSpriteEditor.gui.AnimatedSpriteEditorGUI;
import animatedSpriteEditor.shapes.PoseurRectangle;
import animatedSpriteEditor.shapes.PoseurEllipse;
import animatedSpriteEditor.shapes.PoseurLine;
import animatedSpriteEditor.shapes.PoseurShape;
import animatedSpriteEditor.shapes.PoseurShapeType;
import animatedSpriteEditor.state.EditorState;
import animatedSpriteEditor.state.PoseurPose;
import animatedSpriteEditor.state.PoseurState;
import animatedSpriteEditor.state.EditorStateManager;
import animatedSpriteEditor.state.PoseurStateManager;


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
                Image loadedImage = tk.getImage(imageFileNameAndPath);
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
          singleton.getStateManager().setState(EditorState.SELECT_ANIMATION_STATE);
    }
    
    public boolean saveSpriteTye(File spriteTypeFile)
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
            Element spriteTypeImagesListElement = makeElement(doc, rootElement,
                    IMAGES_LIST_NODE, "" );
            Element spriteTypeAnimationsListElement = makeElement(doc, rootElement,
                    ANIMATIONS_LIST_NODE, "" );
       
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
            
            singleton.getStateManager().setState(EditorState.SELECT_ANIMATION_STATE);
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
    
    public void loadAnimationState(String animationStateFileName)
    {
    	
    }
    
    public boolean saveAnimationState(File animationStateFile)
    {
    	return false;
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
     * This method validates the xmlDocNameAndPath doc against the 
     * xmlSchemaNameAndPath schema and returns true if valid, false
     * otherwise. Note that this is taken directly (with comments)
     * from and example on the IBM site with only slight modifications.
     * 
     * @see http://www.ibm.com/developerworks/xml/library/x-javaxmlvalidapi/index.html
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
}