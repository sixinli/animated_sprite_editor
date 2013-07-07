package sprite_renderer;

import java.util.ArrayList;
import java.util.TimerTask;

/**
 * The AnimateAndRenderTask class specifies the work to be done each
 * frame by some animation timer. Each frame, it will update all
 * sprites and then render them to the provided renderer. Note that
 * this class works on a frame-to-frame basis and has no knowledge
 * of frame rate or even the timer.
 * 
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
public class AnimateAndRenderTask extends TimerTask
{
    // THE LIST OF SPRITES TO BE UPDATED AND RENDERED
    private ArrayList<Sprite> spritesToAnimate;

    // THE RENDERING SURFACE
    private SceneRenderer renderer;
    
    // LET'S US PAUSE THE SCENE, FREEZING ALL UPDATES AND RENDERING
    private boolean scenePaused;

    /**
     * Constructor for setting up this task. Note that this
     * class must have a constructed render list and renderer
     * from the beginning since we have not provided a default
     * constructor or mutator methods.
     * 
     * @param initSpritesToAnimate This list contains all the images
     * to be rendered each frame.
     * 
     * @param initRenderer This contains the instructions on how to
     * actually render the images (i.e. sprites).
     */
    public AnimateAndRenderTask(    ArrayList<Sprite> initSpritesToAnimate,
                                    SceneRenderer initRenderer)
    {
        // INIT OUR INSTANCE VARIABLES, WHICH WE'LL NEED LATER
        spritesToAnimate = initSpritesToAnimate;
        renderer = initRenderer;
    }
    
    /**
     * Accessor method for testing to see if the scene is paused
     * or not. When as scene is paused, all updates cease.
     * 
     * @return true if the scene is currently paused, false otherwise. 
     */
    public boolean isScenePaused()  { return scenePaused; }
    
    /**
     * Pauses the scene, which means sprites are not updated, which means
     * animation stops.
     */
    public void pauseScene()
    {
        scenePaused = true;
    }

    /**
     * Unpauses the scene, which means sprites are updated, which means
     * animation starts again.
     */
    public void unpauseScene()
    {
        scenePaused = false;
    }
    
    /**
     * Called each frame by the animation timer, this method simply
     * goes through all the sprites, updating each of them. All sprites
     * are then sent to the renderer to render the scene.
     */
    public void run()
    {
        if (!scenePaused)
        {
            // THIS ITERATOR WILL SIMPLY GO THROUGH
            // ALL THE SPRITES IN THE SCENE
            for (Sprite spriteToAnimate : spritesToAnimate)
            {
                float timeScaler = renderer.getTimeScaler();
            
                // UPDATE THE SPRITE
                spriteToAnimate.update(timeScaler);
            }
            // AND NOW RENDER ALL THE NEWLY UPDATED SPRITES
            renderer.repaint();        
       }
     }    
}