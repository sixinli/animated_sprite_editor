package animatedSpriteEditor.files;


import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import animatedSpriteEditor.AnimatedSpriteEditor;
import static animatedSpriteEditor.AnimatedSpriteEditorSettings.*;
import animatedSpriteEditor.gui.AnimatedSpriteEditorGUI;
import animatedSpriteEditor.state.EditorState;
import animatedSpriteEditor.state.PoseurState;
import animatedSpriteEditor.state.EditorStateManager;
import animatedSpriteEditor.state.PoseurStateManager;

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
    private String currentSpriteTypeName;
    private String currentAnimationStateName;
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
        editorIO = new AnimatedSpriteEditorIO();
        poseurFileManager = new PoseurFileManager();
        saved = poseurFileManager.isSaved();
        
    }
    
    /**
     * Accessor method for the poseur file manager.
     */
    public PoseurFileManager getPoseurFileManager(){ return poseurFileManager;}
    
    /**
     * This method starts the process of editing a new sprite type. 
     * If a pose is already being edited, it will prompt the user
     * to save it first.
     */
    public void requestNew()
    {
    	// WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToMakeNew = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToMakeNew = promptToSave();
        }
        
        // IF THE USER REALLY WANTS TO MAKE A NEW POSE
        if (continueToMakeNew)
        {
            // GO AHEAD AND PROCEED MAKING A NEW POSE
            continueToMakeNew = promptForNew();

            if (continueToMakeNew)
            {
                // NOW THAT WE'VE SAVED, LET'S MAKE SURE WE'RE IN THE RIGHT MODE
                EditorStateManager stateManager = AnimatedSpriteEditor.getEditor().getStateManager();
                stateManager.setState(EditorState.SELECT_ANIMATION_STATE);
            }
        }
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
     * This method lets the user open a spriteType saved
     * to a file. It will also make sure data for the
     * current pose is not lost.
     */
    public void requestOpen()
    {
    	EditorState state = AnimatedSpriteEditor.getEditor().getStateManager().getMode();
        // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToOpen = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToOpen = promptToSave();
        }
        
        // IF THE USER REALLY WANTS TO OPEN A POSE
        if (continueToOpen)
        {
            // GO AHEAD AND PROCEED MAKING A NEW POSE
            promptToOpen();
        }
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
     * This method lets the user to duplicate an 
     * animation state or a pose
     */
    public void requestDuplicate()
    {
    	
    	
    }
    
    /**
     * This method lets the user to delete an
     * animation state or a pose
     */
    public void requestDelete(){
    	
    	
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
        // SO NOW ASK THE USER FOR A SPRITE TYPE NAME
        AnimatedSpriteEditorGUI gui = AnimatedSpriteEditor.getEditor().getGUI();
        String fileName = JOptionPane.showInputDialog(
                gui,
                SPRITE_TYPE_NAME_REQUEST_TEXT,
                SPRITE_TYPE_NAME_REQUEST_TITLE_TEXT,
                JOptionPane.QUESTION_MESSAGE);
        
        // IF THE USER CANCELLED, THEN WE'LL GET A fileName
        // OF NULL, SO LET'S MAKE SURE THE USER REALLY
        // WANTS TO DO THIS ACTION BEFORE MOVING ON
        if ((fileName != null)
                &&
            (fileName.length() > 0))
        {
            // UPDATE THE FILE NAMES AND FILE
            currentSpriteTypeName = fileName;
            currentFileName = fileName + XML_FILE_EXTENSION;
	        currentFile = new File(SPRITE_TYPE_PATH + fileName + File.separator + currentFileName);

	        try 
            {
            	boolean success = (
            			  new File(SPRITE_TYPE_PATH + fileName)).mkdir();
            			  if (success) {
            			  System.out.println("Directory: " 
            			   + fileName + " created");
            			  }  
            }catch (Exception e){//Catch exception if any
            	  System.err.println("Error: " + e.getMessage());
            }
	        
            // SAVE OUR NEW FILE
            editorIO.saveSpriteTye(currentFile);
            saved = true;

            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile); 
            
            // WE DID IT!
            return true;
        }
        // USER DECIDED AGAINST IT
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
        // WE'LL NEED THE GUI
        AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        AnimatedSpriteEditorGUI gui = singleton.getGUI();
        EditorStateManager stateManager = singleton.getStateManager();
        
        // AND NOW ASK THE USER FOR THE POSE TO OPEN
        JFileChooser poseFileChooser = new JFileChooser(SPRITE_TYPE_PATH);
        int buttonPressed = poseFileChooser.showOpenDialog(gui);
        
        // ONLY OPEN A NEW FILE IF THE USER SAYS OK
        if (buttonPressed == JFileChooser.APPROVE_OPTION)
        {
            // GET THE FILE THE USER ENTERED
            currentFile = poseFileChooser.getSelectedFile();
            currentFileName = currentFile.getName();
            currentSpriteTypeName = currentFileName.substring(0, currentFileName.indexOf("."));
            saved = true;
 
            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile);             
            
            // AND LOAD THE .pose (XML FORMAT) FILE
            editorIO.loadSpriteType(currentSpriteTypeName);
        }
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