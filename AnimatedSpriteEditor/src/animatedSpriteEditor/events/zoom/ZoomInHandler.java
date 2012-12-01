package animatedSpriteEditor.events.zoom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import animatedSpriteEditor.AnimatedSpriteEditor;
import animatedSpriteEditor.gui.AnimatedSpriteEditorGUI;
import animatedSpriteEditor.state.PoseCanvasState;
import animatedSpriteEditor.state.PoseurStateManager;

/**
 * This class will handler events interact with the zoom
 * in button.
 * @author sixin
 *
 */
public class ZoomInHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
        // RELAY THE REQUEST TO THE STATE MANAGER
        AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        PoseurStateManager poseurStateManager = singleton.getStateManager().getPoseurStateManager();
        PoseCanvasState poseCanvasState = poseurStateManager.getZoomableCanvasState();
        poseCanvasState.zoomIn();
        
        // AND MAKE SURE THE ZOOM LABEL REFLECTS THE CHANGE
        AnimatedSpriteEditorGUI gui = singleton.getGUI();
        gui.updateZoomLabel();
	}

}
