package animatedSpriteEditor.events.display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * This class will handler events interact with the start
 * button.
 * @author sixin
 *
 */
public class StartHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null, 
									"The Start Animation Button is clicked",
									"To the user: ",
									JOptionPane.OK_OPTION);
	}

}
