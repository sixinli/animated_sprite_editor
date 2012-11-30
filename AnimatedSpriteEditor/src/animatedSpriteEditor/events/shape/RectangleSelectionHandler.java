package animatedSpriteEditor.events.shape;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import animatedSpriteEditor.AnimatedSpriteEditor;
import animatedSpriteEditor.shapes.PoseurShapeType;
import animatedSpriteEditor.state.PoseurStateManager;

/**
 * This handler responds to when the user requests to
 * start drawing a rectangle.
 * 
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
public class RectangleSelectionHandler implements ActionListener
{
    /**
     * When the user requests to draw a rectangle, we'll need
     * to notify the data manager, since it managers the 
     * shape in progress. It will update the gui as needed
     * as well.
     * 
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // RELAY THE REQUEST TO THE DATA MANAGER
    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        PoseurStateManager poseurStateManager = singleton.getStateManager().getPoseurStateManager();
        poseurStateManager.selectShapeToDraw(PoseurShapeType.RECTANGLE);
    }
}