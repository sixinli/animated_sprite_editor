package sprite_renderer;

import java.awt.Image;
import java.util.HashMap;
import java.util.Iterator;

import animatedSpriteEditor.state.PoseurPose;

/**
 * This class represents a type of animated sprite. Note that a scene
 * many have many different sprites of similar type that would share
 * art and animation pose sequences.
 * 
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
public class SpriteType 
{
    // THIS KEEPS TRACK OF ALL OUR LOADED IMAGES FOR THIS SPRITE
    private HashMap<Integer, Image> spriteImages;
    
    // THIS STORES ALL THE ANIMATIONS
    private HashMap<AnimationState, PoseList> animations;
    
    // THE SPRITE TYPE'S DIMENSIONS SHOULD BE THE SAME
    // FOR ALL IMAGES
    private int width;
    private int height;

    /**
     * The default constructor, this method constructs the data
     * structures for storing this sprite type's art and animation data.
     */
    public SpriteType()
    {
        spriteImages = new HashMap<Integer, Image>();
        animations = new HashMap<AnimationState, PoseList>();
        
        // THIS MEAN IT HASN'T YET BEEN DETERMINED
        width = -1;
        height = -1;
    }

    /**
     * This constructor is for when the width and height are known
     * at the time of construction.
     * 
     * @param initWidth Width of all images in pixels for this sprite type.
     * 
     * @param initHeight Height of all images in pixels for this sprite type.
     */
    public SpriteType(int initWidth, int initHeight)
    {
        this();
        width = initWidth;
        height = initHeight;
    }

    // ACCESSOR METHODS
    
    /**
     * Accessor method for getting the sprite type width.
     */
    public int getWidth() { return width; }
    
    /**
     * Accessor method for getting the sprite type height.
     */
    public int getHeight() { return height; }
    
    /**
     * Accessor method for getting the sprite image that corresponds
     * to the provided imageId variable.
     * 
     * @param imageID The id number of the image to be retrieved.
     * 
     * @return The valid, loaded Image that corresponds to the one
     * represented by the imageID argument.
     */
    public Image getImage(int imageID)
    {
        return spriteImages.get(imageID);
    }
    
    /**
     * Accessor method for getting all the animation states
     * available for this sprite type.
     * 
     * @return An iterator for getting all the animation states.
     */
    public Iterator<AnimationState> getAnimationStates()
    {
        return animations.keySet().iterator();
    }

    /**
     * Accessor method for getting the sprite image that corresponds
     * to the provided imageId variable.
     * 
     * @param animationState the pose list's animation state
     * 
     * @return The valid, loaded Image that corresponds to the one
     * represented by the imageID argument.
     */
    public PoseList getPoseList(AnimationState animationState)
    {
        return animations.get(animationState);
    }
    
    /**
     * Accessor method for the sprite images
     * @return the hash map of the sprite type images
     */
    public HashMap<Integer, Image> getSpriteImages()
    {
    	return spriteImages;
    }

    public HashMap<AnimationState, PoseList> getAnimations() {
		return animations;
	}

	public void setAnimations(HashMap<AnimationState, PoseList> animations) {
		this.animations = animations;
	}

	/**
     * Adds the imageToAdd image argument for use with this sprite type 
     * and binds that image to the imageID value.
     * 
     * @param imageID A unique identifier bound to the Image being
     * added for use with this sprite type.
     * 
     * @param imageToAdd An image to be associated with this sprite type.
     */
    public void addImage(int imageID, Image imageToAdd)
    {
        spriteImages.put(imageID, imageToAdd);
    }
    
    /**
     * This method constructs and returns a new PoseList that will
     * be bound to the animation state as specified by the 
     * animationState argument.
     * 
     * @param animationState The animation state to bind 
     * @return the pose list
     */
    public PoseList addPoseList(AnimationState animationState)
    {
        PoseList animationPoses = animations.get(animationState);
        if (animationPoses == null)
        {
            animationPoses = new PoseList();
            animations.put(animationState, animationPoses);
        }
        return animationPoses;
    }
 
    /**
     * Mutator method for setting the width of this sprite type. Note
     * that all images in this sprite type must be the same dimensions.
     * 
     * @param initWidth The width of all the images for this sprite type.
     */
    public void setWidth(int initWidth)
    {
        width = initWidth;
    }
    
     /**
     * Mutator method for setting the height of this sprite type. Note
     * that all images in this sprite type must be the same dimensions.
     * 
     * @param initHeight The height of all the images for this sprite type.
     */
    public void setHeight(int initHeight)
    {
        height = initHeight;
    }
}