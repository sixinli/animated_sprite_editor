package sprite_renderer;

import java.awt.Image;
import java.util.Iterator;

/**
 * This class represents an animated sprite in a rendered scene. Note
 * that each sprite has a SpriteType, and that multiple sprites may
 * be of the same type, meaning they share artwork, but have their own
 * animation state, position, velocity, etc.
 * 
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
public class Sprite 
{
    // EACH SPRITE IS TIED TO A SET OF ART AND POSES VIA ITS SpriteType
    private SpriteType spriteType;
    
    // THESE VARIABLES COLLECTIVELY MAKE UP THE SPRITE'S STATE
    
    // THE LOCATION IN THE SCENE THAT THIS SPRITE IS LOCATED
    private float positionX;
    private float positionY;
    
    // THE CURRENT VELOCITY OF THIS SPRITE
    private float velocityX;
    private float velocityY;

    // THE CURRENT ANIMATION STATE, WHICH DICTATES WHICH POSE LIST TO USE.
    // NOTE THAT WHENEVER THE ANIMATION STATE CHANGES, WE SHOULD IMMEDIATELY
    // RETRIEVE AND STORE THE CORRESOPONDING ITERATOR FOR THAT LIST
    private AnimationState animationState;
    private Iterator<Pose> poseIterator;
    
    // FOR THE CURRENT ANIMATION STATE, DURING ANIMATION WE ONLY ANIMATE ONE
    // POSE AT A TIME, SO THESE VARIABLES KEEP TRACK OF WHAT THE CURRENT POSE
    // IS AND WHEN IT SHOULD BE FLIPPED
    private Pose currentPose;
    private int animationCounter;
    
    /**
     * This is the only constructor available. Note that sprites must always
     * be tied to a constructed SpriteType, and must always have an animation
     * state, even when first created. From those two initial values all other
     * necessary data can be setup.
     * 
     * @param initSpriteType The type of sprite (i.e. art and poses to use) that
     * this sprite will be used to represent in the scene.
     * 
     * @param initAnimationState The initial state of the sprite, which will dictate
     * which pose list to use.
     */
    public Sprite(  SpriteType initSpriteType,
                    AnimationState initAnimationState)
    {
        // SETUP THE SPRITE
        spriteType = initSpriteType;
        animationState = initAnimationState;
        
        // AND NOW LET'S GET IT READY TO ANIMATE
        setAnimationState(initAnimationState);     
    }    
    
    // ACCESSOR METHODS
    
    /**
     * Accessor method for getting this Sprite's animation state.
     * 
     * @return The current animation state for this Sprite.
     */
    public AnimationState   getAnimationState() { return animationState; }
    
    /**
     * Accessor method for getting this Sprite's x-axis position.
     * 
     * @return The current x-axis position of this Sprite in the scene.
     */
    public float    getPositionX() { return positionX; }

    /**
     * Accessor method for getting this Sprite's y-axis position.
     * 
     * @return The current y-axis position of this Sprite in the scene.
     */
    public float    getPositionY() { return positionY; }

    /**
     * Accessor method for getting this Sprite's x-axis velocity.
     * 
     * @return The current x-axis velocity of this Sprite in the scene.
     */
    public float    getVelocityX() { return velocityX; }
    
    /**
     * Accessor method for getting this Sprite's y-axis velocity.
     * 
     * @return The current y-axis velocity of this Sprite in the scene.
     */
    public float    getVelocityY() { return velocityY; }

    /**
     * Accessor method for getting the image id of the current pose.
     * 
     * @return The image id of the pose currently being rendered.
     */
    public int getCurrentPoseID() { return currentPose.getImageID(); }
    
    /**
     * Accessor method for the animation counter.
     * 
     * @return The animation counter for this sprite.
     */
    public int getAnimationCounter() { return animationCounter; }

    /**
     * This method retrieves and returns the Image that should
     * currently be used to render this Sprite.
     * 
     * @return A loaded Image representing the current state
     * of this Sprite.
     */
    public Image getImageToRender()
    {
        if (currentPose == null)
            return null;
        else
        {
            int poseID = currentPose.getImageID();
            return spriteType.getImage(poseID);
        }
    }
    
    /**
     * Thie method gets the pose list to the caller.
     * @return the pose iterator
     */
    public Iterator<Pose> getPoseIterator(){ return poseIterator;}
    
    // MUTATOR METHODS
    
    /**
     * Mutator method for setting this Sprite's x-axis position.
     * 
     * @param initPositionX The new x-axis position of this Sprite in the scene.
     */
    public void setPositionX(float initPositionX)
    {
        positionX = initPositionX;
    }

    /**
     * Mutator method for setting this Sprite's y-axis position.
     * 
     * @param initPositionY The new y-axis position of this Sprite in the scene.
     */
    public void setPositionY(float initPositionY)
    {
        positionY = initPositionY;
    }

    /**
     * Mutator method for setting this Sprite's x-axis velocity.
     * 
     * @param initVelocityX The new x-axis velocity of this Sprite.
     */
    public void setVelocityX(float initVelocityX)
    {
        velocityX = initVelocityX;
    }

    /**
     * Mutator method for setting this Sprite's y-axis velocity.
     * 
     * @param initVelocityY The new y-axis velocity of this Sprite.
     */
    public void setVelocityY(float initVelocityY)
    {
        velocityY = initVelocityY;
    }
    
    /**
     * Method for changing the animation state of this sprite, which 
     * will result in a change to the pose list employed for fetching
     * images.
     * 
     * @param initAnimationState The sprite animation state to switch to.
     */    
    public void setAnimationState(AnimationState initAnimationState)
    {
        // GET THE PoseList THAT CORRESPONDS TO THIS NEW ANIMATION STATE
        PoseList poseList = spriteType.getPoseList(initAnimationState);
        
        // MAKE SURE WE HAVE A VALID POSE LIST FOR THAT STATE
        if (poseList != null)
        {
            // AND INIT EVERYTHING
            animationState = initAnimationState;
            poseIterator = poseList.getPoseIterator();
            currentPose = poseIterator.next();
            animationCounter = 0;
        }
    }
    
    /**
     * Called each frame, this method would update the physical properties
     * of the Sprite and then forces animation.
     */
    public void update(float scaler)
    {
        float scaledVelocityX = velocityX / scaler;
        float scaledVelocityY = velocityY / scaler;
        positionX += scaledVelocityX;
        positionY += scaledVelocityY;
        animate(scaler);
    }

    /**
     * This method updates the current pose to be used
     * for rendering each frame. Note that the speed of
     * animation can be scaled via the scaler argument.
     * 
     * @param scaler Scales the speed of animation, with 1.0f
     * leaving it as is, numbers greater than 1.0f decreasing
     * the speed, and numbers lower than 1.0f increasing the speed.
     */
    public void animate(float scaler)
    {
        // FIRST SCALE THE DURATION
        float duration = currentPose.getDurationInFrames();
        duration *= scaler;
        int roundedDuration = (int)(Math.round(duration));

        // AND WHEN TIME IS UP, FLIP THE FRAME
        if (animationCounter >= roundedDuration)
        {
            currentPose = poseIterator.next();
            animationCounter = 0;
        }
        else
        {
            animationCounter++;
        }
    }
}