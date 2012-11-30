package animatedSpriteEditor.events.colors;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import animatedSpriteEditor.AnimatedSpriteEditor;
import animatedSpriteEditor.state.PoseurStateManager;

/**
 * This class will handler events interact with the transparency 
 * slide bar.
 * @author sixin
 *
 */
public class TransparencyHandler implements ChangeListener
{
    /**
     * This event handler method is called when the user changes the
     * alpha (transparency) slider. Note that 0 means fully 
     * transparent and 255 means fully opaque. When the slider value
     * is changed we get that value and send it off to the state
     * manager to update the current state.
     * 
     * @param ce The Event Object.
     */
	@Override
	public void stateChanged(ChangeEvent ce) {
		// TODO Auto-generated method stub
		AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        PoseurStateManager stateManager = singleton.getStateManager().getPoseurStateManager();
        JSlider alphaSlider = (JSlider)ce.getSource();
        int alpha = alphaSlider.getValue();
        
        // AND SEND IT OFF TO UPDATE THE SELECTED SHAPE
        stateManager.changeSelectedShapeAlpha(alpha);
		
	}
}
