package animatedSpriteEditor.events.canvas;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import animatedSpriteEditor.gui.PoseCanvas;
import animatedSpriteEditor.state.PoseCanvasState;


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
	public void componentResized(ComponentEvent arg0) 
	{
	        // GET THE CANVAS' STATE AND UPDATE IT
	        PoseCanvas canvas = (PoseCanvas)arg0.getSource();       
	        PoseCanvasState canvasState = canvas.getState();
	        canvasState.updatePoseArea();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
