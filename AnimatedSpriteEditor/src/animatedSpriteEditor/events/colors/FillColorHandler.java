package animatedSpriteEditor.events.colors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import animatedSpriteEditor.AnimatedSpriteEditor;
import animatedSpriteEditor.gui.AnimatedSpriteEditorGUI;


/**
 * This class responds to when the user requests to change
 * the fill color to be used for shape rendering. Note that
 * it doesn't change any data in the state manager, it just
 * activates/toggles some gui controls.
 * 
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
public class FillColorHandler implements ActionListener
{
    /**
     * This method responds to the selecting the fill color control,
     * which once activated will allow for the selection of colors from
     * the pallet to change that fill color.
     * 
     * @param ae The event object.
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // GET THE GUI TO UPDATE IT
        AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        AnimatedSpriteEditorGUI gui = singleton.getGUI();
        
        // TOGGLE THE FILL BUTTON, WHICH WILL UPDATE THE VIEW
        gui.toggleFillColorButton();
    }
}