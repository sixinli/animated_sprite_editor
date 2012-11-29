package animatedSpriteEditor.files;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import animatedSpriteEditor.AnimatedSpriteEditor;
import static animatedSpriteEditor.AnimatedSpriteEditorSettings.*;
import animatedSpriteEditor.gui.EditorCanvas;
import animatedSpriteEditor.gui.AnimatedSpriteEditorGUI;
import animatedSpriteEditor.shapes.PoseurRectangle;
import animatedSpriteEditor.shapes.PoseurEllipse;
import animatedSpriteEditor.shapes.PoseurLine;
import animatedSpriteEditor.shapes.PoseurShape;
import animatedSpriteEditor.shapes.PoseurShapeType;
import animatedSpriteEditor.state.PoseurPose;
import animatedSpriteEditor.state.PoseurState;
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
	
    public void loadSpriteType(String spriteTypeFileName)
    {
    	
    }
    
    public boolean saveSpriteTye(File spriteTypeFile)
    {
    	return false;
    }
    
    public void loadAnimationState(String animationStateFileName)
    {
    	
    }
    
    public boolean saveAnimationState(File animationStateFile)
    {
    	return false;
    }
}