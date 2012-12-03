package animatedSpriteEditor.events.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import animatedSpriteEditor.AnimatedSpriteEditor;
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
		PoseurStateManager poseurStateManager = singleton.getStateManager().getPoseurStateManager();
		poseurStateManager.copySelectedItem();
    }

}
