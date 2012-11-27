package animatedSpriteEditor.events.files;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * This class will handler events interact with the new
 * button.
 * @author sixin
 *
 */
public class NewHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null,  
				"The New Button is clicked.", 
				"To the User: ",
				JOptionPane.OK_OPTION);
	}

}
