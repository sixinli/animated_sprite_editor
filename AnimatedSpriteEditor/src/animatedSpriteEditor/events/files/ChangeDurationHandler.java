package animatedSpriteEditor.events.files;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import animatedSpriteEditor.AnimatedSpriteEditor;

public class ChangeDurationHandler implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
		try {
			singleton.getFileManager().getEditorIO().exportToGIF();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		singleton.getFileManager().getPoseurFileManager().requestChangeDuration();
	}

}
