package animatedSpriteEditor.events.files;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import animatedSpriteEditor.AnimatedSpriteEditor;
import animatedSpriteEditor.files.PoseurFileManager;

/**
 * This class will handler events interact with the save
 * button.
 * @author sixin
 *
 */
public class SaveHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
        AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        PoseurFileManager poseurFileManager = singleton.getFileManager().getPoseurFileManager();
        poseurFileManager.requestSavePose();
	}

}
