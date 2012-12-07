package animatedSpriteEditor.events.files;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import animatedSpriteEditor.AnimatedSpriteEditor;


/**
 * This class will handler events interact with the export
 * pose button.
 * @author sixin
 *
 */
public class MovePoseDownHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
		singleton.getFileManager().getPoseurFileManager().requestMovePoseDown();
	}

}
