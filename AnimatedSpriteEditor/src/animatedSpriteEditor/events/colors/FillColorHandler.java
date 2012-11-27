package animatedSpriteEditor.events.colors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * This class will handler events interact with the fill color button.
 * @author sixin
 *
 */
public class FillColorHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null,  
				"The Fill Color Button is clicked.", 
				"To the User: ",
				JOptionPane.OK_OPTION);
	}

}
