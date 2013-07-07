package animatedSpriteEditor.events.files;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import animatedSpriteEditor.AnimatedSpriteEditor;
import animatedSpriteEditor.files.PoseurFileManager;


/**
 * This class will handler events interact with the new
 * pose button.
 * @author sixin
 *
 */
public class DeletePoseHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		PoseurFileManager poseurFileManager = AnimatedSpriteEditor.getEditor().getFileManager().getPoseurFileManager();
		poseurFileManager.requestDeletePose();
	}

}
