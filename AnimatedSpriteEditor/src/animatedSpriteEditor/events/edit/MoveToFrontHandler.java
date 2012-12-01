package animatedSpriteEditor.events.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import animatedSpriteEditor.AnimatedSpriteEditor;
import animatedSpriteEditor.state.EditorState;
import animatedSpriteEditor.state.PoseurStateManager;

/**
 * This class will handler events interact with the move 
 * to front button.
 * @author sixin
 *
 */
public class MoveToFrontHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) 
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
	        poseurStateManager.moveSelectedItemToFront();
	    }  
	}
}
