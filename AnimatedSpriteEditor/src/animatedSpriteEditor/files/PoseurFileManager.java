package animatedSpriteEditor.files;

import java.awt.Image;
import java.awt.MediaTracker;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import sprite_renderer.AnimationState;
import sprite_renderer.PoseList;
import animatedSpriteEditor.AnimatedSpriteEditor;
import static animatedSpriteEditor.AnimatedSpriteEditorSettings.*;
import animatedSpriteEditor.gui.AnimatedSpriteEditorGUI;
import animatedSpriteEditor.state.EditorState;
import animatedSpriteEditor.state.EditorStateManager;
import animatedSpriteEditor.state.PoseurPose;
import animatedSpriteEditor.state.PoseurState;
import animatedSpriteEditor.state.PoseurStateManager;

/**
 * This class provides all the file servicing for the Poseur application. This
 * means it directs all operations regarding loading, exporting, creating, and
 * saving files, Note that it employs use of PoseIO for the actual file work, 
 * this class manages when to actually read and write from/to files, prompting
 * the user when necessary for file names and validation on actions.
 * 
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
public class PoseurFileManager 
{
    // WE'LL STORE THE FILE CURRENTLY BEING WORKED ON
    // AND THE NAME OF THE FILE
    private File currentFile;
    private String currentPoseName;
	

	private String currentFileName;
    private int poseDuration;
    private int poseID;
    
    // WE WANT TO KEEP TRACK OF WHEN SOMETHING HAS NOT BEEN SAVED
    private boolean saved;
    
    // THIS GUY KNOWS HOW TO READ, WRITE, AND EXPORT POESS
    private PoseIO poseIO;
    
    /**
     * This default constructor starts the program without a
     * pose file being edited.
     */
    public PoseurFileManager()
    {
        // NOTHING YET
        currentFile = null;
        currentFileName = null;
        saved = true;
        poseIO = new PoseIO();
    }
    
    /**
     * Mutator method for the current file. 
     * @param fileName file to set
     */
    public void setCurrentFile(String fileName)
    {
    	
    	currentFileName = fileName;
    	if(currentFileName != null)
    	{
    		currentFile = new File(fileName); 	
    		currentPoseName = currentFile.getName().substring(0, currentFile.getName().lastIndexOf('.'));
    	}
    	saved =true;
    }
    
    /**
     * Accessor method for the new pose duration.
     * @return the pose duration value
     */
    public int getPoseDuration(){ return poseDuration;}
    
    /**
     * Accessor method for the current pose ID.
     * @return the current pose id
     */
    public int getPoseID(){ return poseID;}
    
    /**
     * Accessor method for the current pose name.
     * @return the current pose name
     */
    public String getCurrentPoseName() {return currentPoseName;}

    /**
     * Mutator method for the current pose ID.
     * @param i the ID to set
     */
    public void setPoseID(int i) { poseID = i;} 

	/**
     * This method starts the process of editing a new pose. If
     * a pose is already being edited, it will prompt the user
     * to save it first.
     */
    public void requestNewPose()
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
            	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
    	
                PoseurStateManager poseurStateManager = singleton.getStateManager().getPoseurStateManager();
                
                PoseList pl = singleton.getSpriteType().getAnimations().get(singleton.getAnimationState());
                pl.addPose(poseID, poseDuration);
                poseurStateManager.setState(PoseurState.SELECT_SHAPE_STATE);
        		singleton.getStateManager().setState(EditorState.POSEUR_STATE);
            }
        }
    }
    
    /**
     * This method lets the user open a pose saved
     * to a file. It will also make sure data for the
     * current pose is not lost.
     */
    public void requestOpenPose()
    {
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
     * This method will save the current pose to a file. Note that 
     * we already know the name of the file, so we won't need to
     * prompt the user.
     */
    public void requestSavePose() 
    {
        
    	// DON'T ASK, JUST SAVE
        boolean savedSuccessfully = poseIO.savePose(currentFile, false);
        if (savedSuccessfully)
        {
        	poseIO.savePoseImage(currentPoseName, poseID);
            // MARK IT AS SAVED
            saved = true;
            
            // AND REFRESH THE GUI
            AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();  
            
            PoseurStateManager poseurStateManager = singleton.getStateManager().getPoseurStateManager();
            poseurStateManager.clearSelectShape();
            poseurStateManager.setState(PoseurState.SELECT_SHAPE_STATE);
         
        }
    }
    
    /**
     * This method will copy the current pose to a file. Note that 
     * we already know the name of the file, so we won't need to
     * prompt the user.
     */
    public void requestCopyPose()
    {
    	AnimatedSpriteEditorGUI gui = AnimatedSpriteEditor.getEditor().getGUI(); 
   
        boolean continueToCopy = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE
            continueToCopy = promptToSave();
        }
        
        // IF THE USER REALLY WANTS TO EXIT THE APP
        if (continueToCopy)
        {
        	continueToCopy = poseIO.copyPose(currentPoseName, poseID);
        }	      
        
    	if (continueToCopy)
    	{
    		JOptionPane.showMessageDialog(
	                gui,
	                POSE_COPIED_TEXT,
	                POSE_COPIED_TITLE_TEXT,
	                JOptionPane.INFORMATION_MESSAGE);
    		AnimatedSpriteEditor.getEditor().getFileManager().reloadSpriteType();
            EditorStateManager stateManager = AnimatedSpriteEditor.getEditor().getStateManager();
            stateManager.setState(EditorState.SELECT_POSE_STATE);
            stateManager.getPoseurStateManager().setState(PoseurState.SELECT_SHAPE_STATE);
    	}
    	else
    	{
    		JOptionPane.showMessageDialog(
	                gui,
	                POSE_COPIED_ERROR_TEXT,
	                POSE_COPIED_ERROR_TITLE_TEXT,
	                JOptionPane.ERROR_MESSAGE);
    	}
    	
    }
    
    /**
     * This method will delete the current pose and its image.
     */
    public void requestDeletePose()
    {
        // ASK THE USER TO MAKE SURE THEY WANT TO GO AHEAD WITH IT
       AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
       AnimatedSpriteEditorGUI gui = singleton.getGUI();
        int selection = JOptionPane.showOptionDialog(   gui, 
                DELETE_POSE_TEXT,
                DELETE_POSE_TITLE_TEXT, 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, null, null);
        
        // IF THE USER CLICKED OK, THEN EXPORT THE POSE
        if (selection == JOptionPane.YES_OPTION)
        {
            if (gui.getPosesCount() <= 1)
            {
            	 selection = JOptionPane.showOptionDialog(   
            			 gui, 
                         DELETE_ONLY_POSE_TEXT,
                         DELETE_ONLY_POSE_TITLE_TEXT, 
                         JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                         null, null, null);
            	 if (selection == JOptionPane.YES_OPTION)
            	 {
            		 singleton.getFileManager().requestDeleteState();
            	 }
            }
            else{
            	poseIO.deletePose(currentPoseName);
            	singleton.getFileManager().reloadSpriteType();
            }
        }
    }
    
    /**
     * This method will move the current pose up.
     */
    public void requestMovePoseUp()
    {
    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
    	AnimatedSpriteEditorGUI gui = singleton.getGUI(); 
    	   
        boolean continueToMove = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE
            continueToMove = promptToSave();
        }
        if(continueToMove)
        {
        	poseIO.movePose(poseID, 0);
        	singleton.getFileManager().reloadSpriteType();
        	singleton.getGUI().updatePoseList();
        	JOptionPane.showMessageDialog(
                    gui,
                    POSE_MOVED_TEXT,
                    POSE_MOVED_TITLE_TEXT,
                    JOptionPane.INFORMATION_MESSAGE);
        }  
    }
    
    /**
     * This method will move the current pose down.
     */
    public void requestMovePoseDown()
    {
    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
    	AnimatedSpriteEditorGUI gui = singleton.getGUI(); 
    	   
        boolean continueToMove = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE
            continueToMove = promptToSave();
        }
        if(continueToMove)
        {
        	poseIO.movePose(poseID, 1);
        	singleton.getFileManager().reloadSpriteType();
        	gui.updatePoseList();
        	JOptionPane.showMessageDialog(
                    gui,
                    POSE_MOVED_TEXT,
                    POSE_MOVED_TITLE_TEXT,
                    JOptionPane.INFORMATION_MESSAGE);
        }  
        else
        {
        	JOptionPane.showMessageDialog(
                    gui,
                    POSE_MOVE_ERROR_TEXT,
                    POSE_MOVE_ERROR_TITLE_TEXT,
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * This method changes the duration of the current pose.
     */
    public void requestChangeDuration()
    {
        AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        AnimatedSpriteEditorGUI gui = singleton.getGUI();
        String poseDurationI = JOptionPane.showInputDialog(
                gui,
                POSE_DURATION_REQUEST_TEXT,
                POSE_DURATION_REQUEST_TITLE_TEXT,
                JOptionPane.QUESTION_MESSAGE);
    	
    		if((poseDurationI != null) &&(poseDurationI.length()>0))
    		{
 	
    			try{
    				int newDuration = Integer.parseInt(poseDurationI);
    				boolean changedDuration = poseIO.changePoseDuration(poseID, newDuration);
    				
    				if(changedDuration)
    				{
    					poseDuration = newDuration;
    					singleton.getFileManager().reloadSpriteType();
    					JOptionPane.showMessageDialog(
    										gui,
    										POSE_DURATION_CHANGED_TEXT,
    										POSE_DURATION_CHANGED_TITLE_TEXT,
    										JOptionPane.INFORMATION_MESSAGE);
    				}
    				else
    				{
    					singleton.getFileManager().reloadSpriteType();
    					JOptionPane.showMessageDialog(
    							gui,
    							POSE_DURATION_CHANGE_ERROR_TEXT,
    							POSE_DURATION_CHANGE_ERROR_TITLE_TEXT,
    							JOptionPane.INFORMATION_MESSAGE);
    				}
    			}
    			catch (NumberFormatException ex)
    			{
    				JOptionPane.showMessageDialog(
                            gui,
                            POSE_DURATION_INPUT_ERROR_TEXT,
                            POSE_DURATION_INPUT_ERROR_TITLE_TEXT,
                            JOptionPane.ERROR_MESSAGE);   	
    			}
    		}
    }
    
    /**
     * This method will export the current pose to an image file.
     */
    public void requestExportPose()
    {
        // ASK THE USER TO MAKE SURE THEY WANT TO GO AHEAD WITH IT
        AnimatedSpriteEditorGUI gui = AnimatedSpriteEditor.getEditor().getGUI();
        int selection = JOptionPane.showOptionDialog(   gui, 
                EXPORT_POSE_TEXT + currentPoseName + POSE_FILE_EXTENSION,
                EXPORT_POSE_TITLE_TEXT, 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, null, null);
        
        // IF THE USER CLICKED OK, THEN EXPORT THE POSE
        if (selection == JOptionPane.OK_OPTION)
        {
            poseIO.exportPose(currentPoseName);
        }
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
    public boolean promptForNew()
    {
    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();	
    	AnimatedSpriteEditorGUI gui = singleton.getGUI();
    	String currentSpriteTypeName = singleton.getSpriteTypeName();
    	String currentStateName = singleton.getAnimationStateName();
    	File fileToGetIndex;
    	int index = -1;
    	int i=0;
    	while (index == -1)
    	{
    		fileToGetIndex = new File(SPRITE_TYPE_PATH + currentSpriteTypeName + "/" +POSE_FOLDER_PATH + currentStateName + "_" + i + POSE_FILE_EXTENSION);
    		if(!fileToGetIndex.exists())
    		{
    			index = i;
    		}
    		i++;	
    	}

    	currentPoseName = currentStateName + "_" + index;
        currentFileName = currentPoseName + POSE_FILE_EXTENSION;
        currentFile = new File(SPRITE_TYPE_PATH + currentSpriteTypeName + "/" +POSE_FOLDER_PATH + currentFileName);
        poseDuration = DEFAULT_POSE_DURATION;
 
      	 // SAVE OUR NEW FILE
        boolean created = poseIO.savePose(currentFile, true);
        if(created)
        {
        	poseIO.loadPose(currentFile.getAbsolutePath());
        	poseIO.savePoseImage(currentPoseName, poseID);
        				
        	saved = true;
             
        	// AND PUT THE FILE NAME IN THE TITLE BAR
        	String appName = gui.getAppName();
        	gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile); 
            
            singleton.setSpriteTypeName(currentSpriteTypeName);
            singleton.getPoseFileNames().put(poseID, currentFile.getAbsolutePath());            
                    	
        	// WE DID IT!
            return true;
        }
        	
        gui.updatePoseList();
    	JOptionPane.showMessageDialog(
                gui,
                POSE_SAVING_ERROR_TEXT,
                POSE_SAVING_ERROR_TITLE_TEXT,
                JOptionPane.ERROR_MESSAGE);
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
    public boolean promptToSave()
    {
       // PROMPT THE USER TO SAVE UNSAVED WORK
        AnimatedSpriteEditorGUI gui = AnimatedSpriteEditor.getEditor().getGUI();
        int selection =JOptionPane.showOptionDialog(gui, 
                    PROMPT_TO_SAVE_TEXT, PROMPT_TO_SAVE_TITLE_TEXT, 
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, 
                    null, null, null);
        
        // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
        if (selection == JOptionPane.YES_OPTION)
        {
            poseIO.savePose(currentFile, false);
            poseIO.savePoseImage(currentPoseName, poseID);
            saved = true;
        }
        
        // IF THE USER SAID CANCEL, THEN WE'LL TELL WHOEVER
        // CALLED THIS THAT THE USER IS NOT INTERESTED ANYMORE
        else if (selection == JOptionPane.CANCEL_OPTION)
        {
            return false;
        }

        // IF THE USER SAID NO, WE JUST GO ON WITHOUT SAVING
        // BUT FOR BOTH YES AND NO WE DO WHATEVER THE USER
        // HAD IN MIND IN THE FIRST PLACE
        return true;
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
        
        // AND NOW ASK THE USER FOR THE POSE TO OPEN
        JFileChooser poseFileChooser = new JFileChooser(POSES_PATH);
        int buttonPressed = poseFileChooser.showOpenDialog(gui);
        
        // ONLY OPEN A NEW FILE IF THE USER SAYS OK
        if (buttonPressed == JFileChooser.APPROVE_OPTION)
        {
            // GET THE FILE THE USER ENTERED
            currentFile = poseFileChooser.getSelectedFile();
            currentFileName = currentFile.getName();
            currentPoseName = currentFileName.substring(0, currentFileName.indexOf("."));
            saved = true;
 
            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile);             
            
            // AND LOAD THE .pose (XML FORMAT) FILE
            poseIO.loadPose(currentFile.getAbsolutePath());
            singleton.getStateManager().getPoseurStateManager().clearClipboardShape();
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