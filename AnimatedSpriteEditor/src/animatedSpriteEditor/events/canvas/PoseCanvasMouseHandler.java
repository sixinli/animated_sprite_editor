package animatedSpriteEditor.events.canvas;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JOptionPane;

import animatedSpriteEditor.AnimatedSpriteEditor;
import animatedSpriteEditor.state.PoseurStateManager;

/** This class will handler mouse events inside the pose canvas.
 * @author sixin
 *  
 */
public class PoseCanvasMouseHandler implements MouseListener, MouseMotionListener
{
	/**
     * This method responds to when the user presses the mouse
     * on the pose editing canvas. Note that it forwards the
     * response to the poseur state manager, since that object
     * manages the data it intends to change.
     * 
     * @param e The event object.
     */
    @Override
    public void mousePressed(MouseEvent e) 
    {
        AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        PoseurStateManager state = singleton.getStateManager().getPoseurStateManager();
        state.processMousePress(e.getX(), e.getY());
    }

    /**
     * This method responds to when the user releases the mouse
     * on the pose editing canvas. Note that it forwards the
     * response to the poseur state manager, since that object
     * manages the data it intends to change.
     * 
     * @param e The event object.
     */
    @Override
    public void mouseReleased(MouseEvent e) 
    {
        AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        PoseurStateManager state = singleton.getStateManager().getPoseurStateManager();
        state.processMouseReleased(e.getX(), e.getY());
    }
    
    /**
     * This method responds to when the user drags the mouse
     * on the pose editing canvas. Note that it forwards the
     * response to the poseur state manager, since that object
     * manages the data it intends to change.
     * 
     * @param e The event object.
     */
    @Override
    public void mouseDragged(MouseEvent e) 
    {
        AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        PoseurStateManager poseurStateManager = singleton.getStateManager().getPoseurStateManager();
        poseurStateManager.processMouseDragged(e.getX(), e.getY());
    }

    // WE ARE NOT USING THE REST OF THESE INTERACTIONS
    
    @Override
    public void mouseMoved(MouseEvent e)    {}    
    @Override
    public void mouseClicked(MouseEvent e)  {}
    @Override
    public void mouseEntered(MouseEvent e)  {}
    @Override
    public void mouseExited(MouseEvent e)   {}
}
