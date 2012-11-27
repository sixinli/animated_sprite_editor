package animatedSpriteEditor.events.files;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

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
		JOptionPane.showMessageDialog(null,
										"A Pose Image is clicked", 
										"To the user: ",
										JOptionPane.OK_OPTION);
	}

}
