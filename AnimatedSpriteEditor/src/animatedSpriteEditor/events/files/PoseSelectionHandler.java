package animatedSpriteEditor.events.files;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import animatedSpriteEditor.AnimatedSpriteEditor;
import animatedSpriteEditor.state.EditorState;

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
		AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
		boolean continueToChoosePose = true;
		if(!singleton.getFileManager().getPoseurFileManager().isSaved())
		{
			continueToChoosePose = singleton.getFileManager().getPoseurFileManager().promptToSave();
		}
		
		if(continueToChoosePose)
		{
			String poseFileName = singleton.getPoseFileNames().get(Integer.parseInt(e.getActionCommand()));	
			singleton.getFileManager().getPoseurFileManager().setPoseID(Integer.parseInt(e.getActionCommand()));
			singleton.getStateManager().setState(EditorState.POSEUR_STATE);
			singleton.getFileManager().getEditorIO().getPoseIO().loadPose(poseFileName);
			singleton.getFileManager().getPoseurFileManager().setCurrentFile(poseFileName);
		}
	}
}
