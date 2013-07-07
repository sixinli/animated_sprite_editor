package sprite_renderer;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Timer;
import javax.swing.JPanel;

/**
 * This class performs all the rendering of the scene, doing so
 * in the order the sprites are found inside the provided list. Note
 * that this class also drives the Timer, providing methods for
 * starting and stopping all scene animation.
 * 
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
public class SceneRenderer extends JPanel
{
    // THIS IS A LIST OF THE SPRITES POPULATING THIS SCENE
    private ArrayList<Sprite> spritesToRender;

    // THESE ARE USED TO CONTROL ANIMATION
    private Timer animationTimer;               // Java's Timer FOR FIXED-RATE SCHEUDLING
    private float timeScaler;                   // OUR SCALER FOR SLOWING/SPEEDING ANIMATION   
    private AnimateAndRenderTask animator;      // THIS IS THE TASK WE WANT SCHEDULED
    public static final long FRAME_RATE = 30;   // THIS IS OUR FRAME RATE, WE'LL NEVER CHANGE IT

    /**
     * Constructor for initializing the renderer, it will init the
     * sprite list and will set everything up. Note that it will not
     * start the animation schedule, however. That may be started on
     * demand via the startAnimation method.
     * 
     * @param initSpriteToAnimate The sprites in the scene to be
     * rendered each frame. Note that this is a mutable list of sprites
     * that could be changed on the fly at any time.
     */
    public SceneRenderer(ArrayList<Sprite> initSpriteToAnimate)
    {
        animationTimer = new Timer();
        spritesToRender = initSpriteToAnimate;
        timeScaler = 1.0f;
    }

    /**
     * Accessor method for the time scaler
     * 
     * @return The time scaler used for animation.
     */
    public float getTimeScaler()
    {
        return timeScaler;
    }

    /**
     * Mutator method for the time scaler. A value of 1.0f is the 
     * default. Values greater than 1 will slow down animation, lower
     * than 1 will speed it up.
     * 
     * @param initTimeScaler The value by which the animation rate
     * will be scaled.
     */
    public void setTimeScaler(float initTimeScaler)
    {
        timeScaler = initTimeScaler;
    }

    /**
     * This method starts the scheduling of animation and rendering.
     */
    public void startScene()
    {
        // NOTE THAT WE'LL START IN A PAUSED STATE
        animator = new AnimateAndRenderTask(spritesToRender, this);
        animator.pauseScene();
        animationTimer.scheduleAtFixedRate(animator, 0L, FRAME_RATE);
    }
    
    /**
     * When called, this method will relay the request to the animator,
     * resulting in a pause to all scene updates and rendering.
     */
    public void pauseScene()
    {
        animator.pauseScene();;
    }

    /**
     * This method will start up sprite updates and rendering.
     */
    public void unpauseScene()
    {
        animator.unpauseScene();
    }

    /**
     * This is where all scene rendering is done. Note that the method
     * iterates through all the sprites in order, rendering each sprite
     * if finds. Note that sprite order is important, with sprites towards
     * the back of the list being rendered on top of the ones towards
     * the front.
     * 
     * @param g The Graphics context for rendering onto this panel. All
     * rendering calls on g will result in rendering happening on this panel.
     */
    public void paintComponent(Graphics g)
    {
        // CLEAR USING THIS PANEL'S SET BACKGROUND COLOR
        super.paintComponent(g);
        
        // THE PANEL WILL ONLY BE RENDERABLE AFTER IT HAS
        // BEEN LAID OUT, MEANING IT HAS BEEN SIZED AND POSITIONED
        int panelWidth = getWidth();
        if (panelWidth > 0)
        {
            // THIS IS A SHORTHAND FOR USING AN ITERATOR FOR
            // SEQUENTIALLY GOING THRUOGH ALL THE ELEMENTS
            // IN A JAVA COLLECTION VIA AN ITERATOR
            for (Sprite spriteToRender : spritesToRender)
            {
                // GET THE STUFF TO RENDER
                Image img = spriteToRender.getImageToRender();
                int x = (int)spriteToRender.getPositionX();
                int y = (int)spriteToRender.getPositionY();
                
                // AND RENDER IT
                g.drawImage(img, x, y, null);
            }         
         }
    }
}