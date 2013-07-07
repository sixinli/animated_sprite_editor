package animatedSpriteEditor.state;

/**
 * This enum represents the states that are possible for our
 * Animated Sprite editor. States represent the following:
 * 
 * SPRITE_TYOE_STATE - This is the mode when the application first
 * opens, before a file has been loaded or a new sprite type has
 * been started. In this mode, most controls are inactive. Once
 * the user starts a new sprite type or loads an existing one we 
 * enter the select animation state mode.
 * 
 * SELECT_ANIMATION_STATE - This is the default mode when a sprite 
 * type is first started or loaded from a file. In this mode, the 
 * user needs to create a new animation state or load an existing
 * one, once this is done we enter the poseur state mode. 
 * 
 * POSEUR_STATE - This is the mode when the user can start editing a 
 * by loading or creating a new pose.
 * 
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
public enum EditorState 
{
    SPRITE_TYPE_STATE,
    SELECT_ANIMATION_STATE,
    SELECT_POSE_STATE,
    POSEUR_STATE,
}
