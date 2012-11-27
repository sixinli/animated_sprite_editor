package animatedSpriteEditor.events.canvas;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JOptionPane;

/**
 * This class will handler events interact with the pose canvas component.
 * @author sixin
 *
 */
public class PoseCanvasComponentHandler implements ComponentListener{

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub
        // GET THE CANVAS' STATE AND UPDATE IT
		JOptionPane.showMessageDialog(null,  
				"The zoomable canvas component is resized.", 
				"To the User: ",
				JOptionPane.OK_OPTION);
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
