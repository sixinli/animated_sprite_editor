package animatedSpriteEditor.events.files;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import animatedSpriteEditor.AnimatedSpriteEditor;
import static animatedSpriteEditor.AnimatedSpriteEditorSettings.*;

public class ExportHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
		try {
			singleton.getFileManager().getEditorIO().exportToGIF();
			JOptionPane.showMessageDialog(singleton.getGUI(), 
					IMAGE_EXPORTED_TEXT + " " + SPRITE_TYPE_PATH + singleton.getSpriteTypeName() 
					+ "/" + singleton.getAnimationStateName() + ".gif",
					IMAGE_EXPORTED_TITLE_TEXT,
					JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(singleton.getGUI(), 
					IMAGE_EXPORTING_ERROR_TEXT,
					IMAGE_EXPORTING_ERROR_TITLE_TEXT,
					JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
}
