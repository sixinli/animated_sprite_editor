package animatedSpriteEditor.events.colors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * This class will handler events interact with the color pallet.
 * @author sixin
 *
 */
public class ColorPalletHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null,  
				"The color pallet is clicked.", 
				"To the User: ",
				JOptionPane.OK_OPTION);
	}

	
}
