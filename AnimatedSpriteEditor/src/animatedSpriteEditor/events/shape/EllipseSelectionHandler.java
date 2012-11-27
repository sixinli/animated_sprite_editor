package animatedSpriteEditor.events.shape;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * This class will handler events interact with the 
 * ellipse button.
 * @author sixin
 *
 */
public class EllipseSelectionHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null,  
				"The Ellipse Button is clicked.", 
				"To the User: ",
				JOptionPane.OK_OPTION);
	}

}
