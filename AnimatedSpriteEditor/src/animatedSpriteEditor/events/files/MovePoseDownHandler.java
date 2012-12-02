package animatedSpriteEditor.events.files;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * This class will handler events interact with the export
 * pose button.
 * @author sixin
 *
 */
public class MovePoseDownHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null,  
				"The Move Pose Down Button is clicked.", 
				"To the User: ",
				JOptionPane.OK_OPTION);
	}

}
