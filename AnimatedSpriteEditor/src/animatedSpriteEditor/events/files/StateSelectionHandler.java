package animatedSpriteEditor.events.files;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * This class will handler events interact with the 
 * state combo box.
 * @author sixin
 *
 */
public class StateSelectionHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null, 
									"The Animation State ComboBox is clicked",
									"To the user: ",
									JOptionPane.OK_OPTION);
	}

}
