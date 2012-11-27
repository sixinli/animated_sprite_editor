package animatedSpriteEditor.events.shape;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * This class will handler events interact with the line
 * button.
 * @author sixin
 *
 */
public class LineSelectionHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null,  
				"The Line Button is clicked.", 
				"To the User: ",
				JOptionPane.OK_OPTION);
	}

}
