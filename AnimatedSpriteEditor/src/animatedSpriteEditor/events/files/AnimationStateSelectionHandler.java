package animatedSpriteEditor.events.files;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import sprite_renderer.AnimationState;

import animatedSpriteEditor.AnimatedSpriteEditor;
import animatedSpriteEditor.state.EditorState;

/**
 * The AnimationStateSelectionHandler responds to when the
 * user selects an animation state from the GUI. Note that
 * it simply forwards ownership of the request to the viewer.
 * 
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
public class AnimationStateSelectionHandler implements ItemListener
{
   
    /**
     * Called by Swing when something in the combobox changes, either
     * because the user selected something or because items were added
     * or removed from it.
     * 
     * @param ie Contains information about the event.
     */
    @Override
    public void itemStateChanged(ItemEvent ie) 
    {
        // THE viewer WILL HANDLE IT
    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
    	singleton.getGUI().getSpriteList().clear();
    	JComboBox listComboBox = (JComboBox)ie.getSource();

    	Object animationState = listComboBox.getSelectedItem();
    	if(animationState instanceof AnimationState)
    	{
    		singleton.setAnimationState((AnimationState)animationState);
    		singleton.getStateManager().setState(EditorState.SELECT_POSE_STATE);
    		
    	}
    }    
}
