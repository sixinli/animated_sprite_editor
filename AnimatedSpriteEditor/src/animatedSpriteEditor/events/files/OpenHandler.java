package animatedSpriteEditor.events.files;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import animatedSpriteEditor.AnimatedSpriteEditor;
import animatedSpriteEditor.files.InvalidXMLFileFormatException;

/**
 * This class will handler events interact with the open
 * button.
 * @author sixin
 *
 */
public class OpenHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();

		singleton.getFileManager().requestOpen();
		
	}

}
