package animatedSpriteEditor.events.display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import sprite_renderer.SceneRenderer;

/**
 * This class will handler events interact with the slow
 * down button.
 * @author sixin
 *
 */
public class SlowDownHandler implements ActionListener{
	// THE SCENE RENDERER KNOWS AND USES THE RENDERING SPEED
    private SceneRenderer renderer;

    /**
     * We'll need the renderer for when the event happens.
     * 
     * @param initRenderer The renderer from the SceneRenderer library.
     */
    public SlowDownHandler(SceneRenderer initRenderer)
    {
        // KEEP IT FOR LATER
        renderer = initRenderer;
    }    

    /**
     * Called when someone presses the slow down the scene button,
     * it tells the renderer to do so.
     * 
     * @param ae Has info about the event.
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // GET THE SCALER AND SCALE IT
    	if(renderer.getTimeScaler() < 30){
    		float scaler = renderer.getTimeScaler();
    		renderer.setTimeScaler(scaler*1.5f);
    	}
		else {
			JOptionPane.showMessageDialog(null,  
					"Hmmmm....I guess the sprite is already pretty slow.....", 
					"To the User: ",
					JOptionPane.WARNING_MESSAGE);
		}
    }
}
