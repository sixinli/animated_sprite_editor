package animatedSpriteEditor.files;


import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import animatedSpriteEditor.AnimatedSpriteEditor;
import static animatedSpriteEditor.AnimatedSpriteEditorSettings.*;
import animatedSpriteEditor.gui.AnimatedSpriteEditorGUI;
import animatedSpriteEditor.state.PoseurState;
import animatedSpriteEditor.state.EditorStateManager;

/**
 * This class provides all the file servicing for the AnimatedSpriteEditor
 * application.This means it directs all operations regarding loading, 
 * exporting, creating, and saving files, Note that it employs use of 
 * AnimatedSpriteEditorIO for the actual file work, this class manages 
 * when to actually read and write from/to files, prompting
 * the user when necessary for file names and validation on actions.
 * 
 * @author  Sixin 
 * @version 1.0
 */
public class EditorFileManager 
{
    // WE'LL STORE THE FILE CURRENTLY BEING WORKED ON
    // AND THE NAME OF THE FILE
    private File currentFile;
    private String currentPoseName;
    private String currentFileName;
    
    // WE WANT TO KEEP TRACK OF WHEN SOMETHING HAS NOT BEEN SAVED
    private boolean saved;
    
    // THIS GUY KNOWS HOW TO READ, WRITE, AND EXPORT POESS
    private AnimatedSpriteEditorIO editorIO;
    
    // THIS GUY KNOWS HOW TO MANAGE POSE FILES
    private PoseurFileManager poseurFileManager;
    
    /**
     * This default constructor starts the program without a
     * pose file being edited.
     */
    public EditorFileManager()
    {
        // NOTHING YET
        currentFile = null;
        currentFileName = null;
        saved = true;
        editorIO = new AnimatedSpriteEditorIO();
        poseurFileManager = new PoseurFileManager();
        
    }
    
    /**
     * Accessor method for the poseur file manager.
     */
    public PoseurFileManager getPoseurFileManager(){ return poseurFileManager;}
    
    /**
     * This method starts the process of editing a new pose. If
     * a pose is already being edited, it will prompt the user
     * to save it first.
     */
    public void requestNewPose()
    {
  
    }
    
    /**
     * This method starts the process of editing a new state. If
     * a pose is already being edited, it will prompt the user
     * to save it first.
     */
    public void requestNewState()
    {
  
    }
    
    /**
     * This method starts the process of editing a new sprite type. 
     * If a pose is already being edited, it will prompt the user
     * to save it first.
     */
    public void requestNew()
    {
  
    }
    
    /**
     * This method lets the user open a spriteType saved
     * to a file. It will also make sure data for the
     * current pose is not lost.
     */
    public void requestOpen()
    {
   
    }
    
    /**
     * This method lets the user open a pose saved
     * to a file. It will also make sure data for the
     * current pose is not lost.
     */
    public void requestOpenPose()
    {
   
    }
    
    /**
     * This method lets the user open a state saved
     * to a file. It will also make sure data for the
     * current pose is not lost.
     */
    public void requestOpenState()
    {
   
    }
    
    /**
     * This method will save the current pose to a file. Note that 
     * we already know the name of the file, so we won't need to
     * prompt the user.
     */
    public void requestSavePose()
    {

    }
    
    /**
     * This method will save the current pose as a named file provided
     * by the user.
     */
    public void requestSaveAsPose()
    {
        // ASK THE USER FOR A FILE NAME
        promptForNew();
    }
    
    /**
     * This method will export the current pose to an image file.
     */
    public void requestExportPose()
    {
        
    }
    
    /**
     * This method will exit the application, making sure the user
     * doesn't lose any data first.
     */
    public void requestExit()
    {
        // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToExit = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE
            continueToExit = promptToSave();
        }
        
        // IF THE USER REALLY WANTS TO EXIT THE APP
        if (continueToExit)
        {
            // EXIT THE APPLICATION
            System.exit(0);
        }
    }

    /**
     * This helper method asks the user for a name for the pose about
     * to be created. Note that when the pose is created, a corresponding
     * .pose file is also created.
     * 
     * @return true if the user goes ahead and provides a good name
     * false if they cancel.
     */
    private boolean promptForNew()
    {
    	return false;
    }

    /**
     * This helper method verifies that the user really wants to save their
     * unsaved work, which they might not want to do. Note that it could be
     * used in multiple contexts before doing other actions, like creating a
     * new pose, or opening another pose, or exiting. Note that the user will 
     * be presented with 3 options: YES, NO, and CANCEL. YES means the user 
     * wants to save their work and continue the other action (we return true
     * to denote this), NO means don't save the work but continue with the
     * other action (true is returned), CANCEL means don't save the work and
     * don't continue with the other action (false is retuned).
     * 
     * @return true if the user presses the YES option to save, true if the user
     * presses the NO option to not save, false if the user presses the CANCEL
     * option to not continue.
     */
    private boolean promptToSave()
    {
    	return false;
    }
    
    /**
     * This helper method asks the user for a file to open. The
     * user-selected file is then loaded and the GUI updated. Note
     * that if the user cancels the open process, nothing is done.
     * If an error occurs loading the file, a message is displayed,
     * but nothing changes.
     */
    private void promptToOpen()
    {
       
    }
    
    /**
     * This mutator method marks the file as not saved, which means that
     * when the user wants to do a file-type operation, we should prompt
     * the user to save current work first. Note that this method should
     * be called any time the pose is changed in some way.
     */
    public void markFileAsNotSaved()
    {
        saved = false;
    }

    /**
     * Accessor method for checking to see if the current pose has been
     * saved since it was last editing. If the current file matches
     * the pose data, we'll return true, otherwise false.
     * 
     * @return true if the current pose is saved to the file, false otherwise.
     */
    public boolean isSaved()
    {
        return saved;
    }
}