package animatedSpriteEditor.events.files;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import animatedSpriteEditor.AnimatedSpriteEditor;

public class SaveAsHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
		singleton.getFileManager().requestSaveAs();
	}

}
