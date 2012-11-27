package animatedSpriteEditor.events.colors;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JOptionPane;

/**
 * This class will handler events interact with the transparency 
 * slide bar.
 * @author sixin
 *
 */
public class TransparencyHandler implements MouseMotionListener{
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null,  
				"The Transparency Slide Bar is dragged.", 
				"To the User: ",
				JOptionPane.OK_OPTION);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

}
