package animatedSpriteEditor.events.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import animatedSpriteEditor.AnimatedSpriteEditor;
import animatedSpriteEditor.state.EditorState;
import animatedSpriteEditor.state.PoseurStateManager;


/**
 * This class will handler events interact with the copy
 * button.
 * @author sixin
 *
 */
public class CopyHandler implements ActionListener{

	@Override
    public void actionPerformed(ActionEvent ae) 
    {
		AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
		if(singleton.getStateManager().getMode() == EditorState.SELECT_ANIMATION_STATE)
		{
			
		}
		
		else if(singleton.getStateManager().getMode() == EditorState.SELECT_POSE_STATE)
		{
			
		}
		
		else if(singleton.getStateManager().getMode() == EditorState.POSEUR_STATE)
		{	
			PoseurStateManager poseurStateManager = singleton.getStateManager().getPoseurStateManager();
			poseurStateManager.copySelectedItem();
		}
    }   

}
