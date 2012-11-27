package animatedSpriteEditor.events.zoom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * This class will handler events interact with the zoom
 * in button.
 * @author sixin
 *
 */
public class ZoomInHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null,  
				"The Zoom In Button is clicked.", 
				"To the User: ",
				JOptionPane.OK_OPTION);
	}

}
