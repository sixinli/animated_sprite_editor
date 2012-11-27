package animatedSpriteEditor.files;

import java.awt.Color;
import java.awt.HeadlessException;

import static animatedSpriteEditor.AnimatedSpriteEditorSettings.*;

import javax.swing.JOptionPane;
import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;

import animatedSpriteEditor.AnimatedSpriteEditor;
import animatedSpriteEditor.gui.AnimatedSpriteEditorGUI;
import animatedSpriteEditor.state.ColorPalletState;

/**
 * This class can be used to load color pallet data from an XML 
 * file into a constructed ColorPalletState. Note that the XML
 * file must validate against the color_pallet_settings.xsd
 * schema. This class demonstrates how application settings can
 * be loaded dynamically from a file.
 * 
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
public class ColorPalletLoader 
{    
    /**
     * This method will extract the data found in the provided
     * XML file argument and use it to load the color pallet
     * argument.
     * 
     * @param colorPalletXMLFile Path and file name to an XML
     * file containing information about a custom color pallet. Note
     * this XML file must validate against the aforementioned schema.
     * 
     * @param colorPalletState The state manager for the color
     * pallet, we'll load all the data found in the XML file
     * inside here.
     */
    public void initColorPallet( String colorPalletXMLFile,
                                 ColorPalletState colorPalletState)
    {
        Color[] colorPallet = new Color[20];
        loadColors(colorPalletXMLFile, colorPallet);

        colorPalletState.loadColorPalletState(  colorPallet,
                                                2,
                                                12,
                                                Color.GRAY);
    }
    
    /**
     * Load the colors to the color pallet from a xml file
     * 
     * @param colorPalletXMLFile the address of the xml file
     * @param colors	the array of colors to be put at
     */
    public void loadColors(String colorPalletXMLFile, Color[] colors){
    	XMLUtilities xmlUtil = new XMLUtilities();
    	try{
    		Document doc = xmlUtil.loadXMLDocument(colorPalletXMLFile, COLOR_PALLET_SETTINGS_SCHEMA);
    		NodeList palletColors = doc.getElementsByTagName(PALLET_COLOR_NODE);
    		for(int i=0; i<palletColors.getLength(); i++){
    			int red = Integer.parseInt(palletColors.item(i).getFirstChild().getTextContent());
    			int green = Integer.parseInt(palletColors.item(i).getFirstChild().getNextSibling().getTextContent());
    			int blue = Integer.parseInt(palletColors.item(i).getLastChild().getTextContent());
    			Color c = new Color(red, green, blue);
    			colors[i] = c;
    		}
    	
    	}
        catch(InvalidXMLFileFormatException | DOMException | HeadlessException ex)
        {
            // SOMETHING WENT WRONG LOADING THE .pose XML FILE
        	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getPoseur();
        	AnimatedSpriteEditorGUI gui = singleton.getGUI();
            JOptionPane.showMessageDialog(
                gui,
                COLOR_PALLET_LOADING_ERROR_TEXT,
                COLOR_PALLET_LOADING_ERROR_TITLE_TEXT,
                JOptionPane.ERROR_MESSAGE);            
        }    
    }
}