package sprite_renderer;

/**
 * A Pose represents a single still frame (image) for a sprite and how
 * long it is to remain on the screen during animation.
 * 
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
public class Pose
{
    // THE IMAGE RENDERED WHILE THE SPRITE IS IN THIS POSE
    private int imageID;
    
    // THE AMOUNT OF FRAMES TO KEEP THIS POSE ON
    // THE SCREEN DURING ANIMATION. NOTE THAT THIS VALUE WOULD
    // REPRESENT THE DEFAULT POSE DURATION, BUT THAT AN ANIMATION
    // PROGRAM COULD THEN OPTIONALLY SPEED UP OR SLOW DOWN
    // ANIMATION BY SCALING THIS VALUE
    private int durationInFrames;

    /**
     * This constructor initializes all necessary data. Note that this
     * class does not provide a default constructor.
     * 
     * @param initImageID Image to be used for this pose.
     * 
     * @param initDurationInFrames The number of frames to keep this image on 
     * the screen.
     */
    public Pose(int initImageID, int initDurationInFrames)
    {
        imageID = initImageID;
        durationInFrames = initDurationInFrames;
    }

    // ACCESSOR METHODS
    
    /**
     * Accessor method for getting this Pose's image id.
     * 
     * @return The image id number for this Pose.
     */
    public int getImageID() { return imageID; }
    
    /**
     * Accessor method for getting this Pose's duration, meaning
     * the number of frames it should be kept on screen.
     * 
     * @return The duration to be used for rendering this pose.
     */
    public int getDurationInFrames(){ return durationInFrames;}   
}