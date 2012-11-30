package animatedSpriteEditor.events.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import animatedSpriteEditor.AnimatedSpriteEditor;
import animatedSpriteEditor.state.PoseurStateManager;

/**
 * This class will handler events interact with the select
 * button.
 * @author sixin
 *
 */
public class StartSelectionHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
        AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        PoseurStateManager poseurStateManager = singleton.getStateManager().getPoseurStateManager();
        poseurStateManager.startShapeSelection();
	}

}
