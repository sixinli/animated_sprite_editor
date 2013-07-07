package sprite_renderer;

/**
 * This enum stores the possible values one might use for representing
 * and animated state.
 *
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
public enum AnimationState 
{
    IDLE,
    RUNNING_LEFT,
    RUNNING_RIGHT,
    RUNNING_UP,
    RUNNING_DOWN,
    BOUNCING,
    WALKING_LEFT,
    WALKING_RIGHT,
    WALKING_UP,
    WALKING_DOWN,
    JUMPING,
    JUMPING_LEFT,
    JUMPING_RIGHT,
    JUMPING_UP,
    JUMPING_DOWN,
    ATTACKING_LEFT,
    ATTACKING_RIGHT,
    ATTACKING_UP,
    ATTACKING_DOWN,
    SHOOTING_LEFT,
    SHOOTING_DOWN,
    OTHER
}