package animatedSpriteEditor.state;

import animatedSpriteEditor.AnimatedSpriteEditor;
import animatedSpriteEditor.gui.AnimatedSpriteEditorGUI;


/**
 * This class stores all the state information about the application
 * regarding the poses and rendering settings. Note that whenever
 * the data in this class changes, the visual representations
 * will also change.
 * 
 * @author  sixin
 * @version 1.0
 */
public class EditorStateManager 
{
    // THIS KEEPS TRACK OF WHAT MODE OUR APPLICATION
    // IS IN. IN DIFFERENT MODES IT BEHAVES DIFFERENTLY
    // AND DIFFERENT CONTROLS ARE ENABLED OR DISABLED
    private EditorState editorState;
    private PoseurStateManager poseurStateManager;
    
    /**
     * This constructor sets up all the state information regarding
     * the shapes that are to be rendered for the pose. Note that 
     * at this point the user has not done anything, all our
     * pose data structures are essentially empty.
     */
    public EditorStateManager()
    {
        // WE ALWAYS START IN SELECT_SHAPE_MODE
        editorState = EditorState.SPRITE_TYPE_STATE;
        
        // CONSTRUCT THE POSE, WHICH WILL BE USED
        // TO RENDER OUR STUFF
        poseurStateManager = new PoseurStateManager();
    }
    
    /**
     * Gets the current mode of the application.
     * 
     * @return The mode currently being used by the application. 
     */
     public EditorState getMode() { return editorState; }
     
     /**
      * Gets the poseur state manager
      * 
      * @return The poseur state manager
      */
      public PoseurStateManager getPoseurStateManager() {return poseurStateManager;} 
 
      /**
       * This mutator method changes the mode of the application,
       * which may result in a cursor change and the enabling and
       * disabling of various controls.
       * 
       * @param newMode The mode to set as the current mode.
       */
      public void setState(EditorState newMode)
      {
          // KEEP THE MODE
          editorState = newMode;
          
          // AND UPDATE THE GUI
          AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
          AnimatedSpriteEditorGUI gui = singleton.getGUI();
          gui.updateMode();
      }
}
