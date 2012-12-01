package animatedSpriteEditor.events.files;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import animatedSpriteEditor.AnimatedSpriteEditor;

/**
 * This class will handler events interact with the pose
 * buttons.
 * @author sixin
 *
 */
public class PoseSelectionHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String poseFileName = AnimatedSpriteEditor.getEditor().getPoseFileNames().get(Integer.parseInt(e.getActionCommand()));
		JOptionPane.showMessageDialog(null,
										"Pose Image" + e.getActionCommand() + " is clicked " + poseFileName, 
										"To the user: ",
										JOptionPane.OK_OPTION);
		AnimatedSpriteEditor.getEditor().getFileManager().getEditorIO().getPoseIO().loadPose(poseFileName);
		AnimatedSpriteEditor.getEditor().getFileManager().getPoseurFileManager().setCurrentFile(poseFileName);
	}
}
