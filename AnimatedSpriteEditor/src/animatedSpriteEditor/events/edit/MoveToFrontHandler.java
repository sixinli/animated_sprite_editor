package animatedSpriteEditor.events.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * This class will handler events interact with the move 
 * to front button.
 * @author sixin
 *
 */
public class MoveToFrontHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null,  
				"The Move To Front Button is clicked.", 
				"To the User: ",
				JOptionPane.OK_OPTION);
	}

}
