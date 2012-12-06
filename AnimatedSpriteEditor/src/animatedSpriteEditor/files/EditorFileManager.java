package animatedSpriteEditor.files;


import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import sprite_renderer.AnimationState;
import sprite_renderer.PoseList;
import sprite_renderer.SpriteType;

import animatedSpriteEditor.AnimatedSpriteEditor;
import static animatedSpriteEditor.AnimatedSpriteEditorSettings.*;
import animatedSpriteEditor.gui.AnimatedSpriteEditorGUI;
import animatedSpriteEditor.state.EditorState;
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
    private String currentSpriteTypeName;
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
     * @return the poseur file manager
     * 
     */
    public PoseurFileManager getPoseurFileManager(){ return poseurFileManager;}
    
    /**
     * Accessor method for the current sprite type name.
     * @return the current sprite type name.
     */
    public String getCurrentSpriteTypeName(){ return currentSpriteTypeName;}
    
    /**
     * Accessor method for the editor IO.
     * @return the editor IO
     */
    public AnimatedSpriteEditorIO getEditorIO(){ return editorIO;}
    
    
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
            continueToMakeNew = poseurFileManager.promptToSave();
        }
        
        if (!poseurFileManager.isSaved())
        {
        	continueToMakeNew = poseurFileManager.promptToSave();
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
                stateManager.setState(EditorState.POSEUR_STATE);
                stateManager.getPoseurStateManager().setState(PoseurState.SELECT_SHAPE_STATE);
            }
        }
    }
    
    /**
     * This method lets the user open a spriteType saved
     * to a file. It will also make sure data for the
     * current pose is not lost.
     */
    public void requestOpen()
    {
        // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToOpen = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToOpen = poseurFileManager.promptToSave();
        }
        
        if(!poseurFileManager.isSaved())
        {
        	continueToOpen = poseurFileManager.promptToSave();
        }
        
        // IF THE USER REALLY WANTS TO OPEN A POSE
        if (continueToOpen)
        {
            // GO AHEAD AND PROCEED MAKING A NEW POSE
            promptToOpen();
        }
    }
    
    /**
	 * This method starts the process of editing a new state. If
	 * a pose is already being edited, it will prompt the user
	 * to save it first.
	 */
	public void requestNewState()
	{
		// WE MAY HAVE TO SAVE CURRENT WORK
	    boolean continueToMakeNew = true;
	    if (!saved)
	    {
	        // THE USER CAN OPT OUT HERE WITH A CANCEL
	        continueToMakeNew = poseurFileManager.promptToSave();
	    }
	    
	    if (!poseurFileManager.isSaved())
	    {
	    	continueToMakeNew = poseurFileManager.promptToSave();
	    }
	    
	    // IF THE USER REALLY WANTS TO MAKE A NEW POSE
	    if (continueToMakeNew)
	    {
	        // GO AHEAD AND PROCEED MAKING A NEW POSE
	        continueToMakeNew = promptForNewState();
	        
	        if(continueToMakeNew)
	        {
	        	// NOW THAT WE'VE SAVED, LET'S MAKE SURE WE'RE IN THE RIGHT MODE
	        	EditorStateManager stateManager = AnimatedSpriteEditor.getEditor().getStateManager();
	        	stateManager.setState(EditorState.POSEUR_STATE);
	        	stateManager.getPoseurStateManager().setState(PoseurState.SELECT_SHAPE_STATE);
	        }
	    }   
	    
	}
    
    /**
     * This method lets the user to copy an
     * animation state.
     */
    public void requestCopyState()
    {
    	// WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToMakeCopy = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToMakeCopy = poseurFileManager.promptToSave();
        }
        
        if (!poseurFileManager.isSaved())
        {
        	continueToMakeCopy = poseurFileManager.promptToSave();
        }
        
        // IF THE USER REALLY WANTS TO MAKE A NEW POSE
        if (continueToMakeCopy)
        {
            // GO AHEAD AND PROCEED MAKING A NEW POSE
            continueToMakeCopy = promptToCopyState();

            if (continueToMakeCopy)
            {
                // NOW THAT WE'VE SAVED, LET'S MAKE SURE WE'RE IN THE RIGHT MODE
            	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
            	
            	singleton.getFileManager().getPoseurFileManager().setCurrentFile(null);
                EditorStateManager stateManager = singleton.getStateManager();
                stateManager.setState(EditorState.SELECT_POSE_STATE);
            }
        }
    }
    
    /**
	 * This method lets the user to delete an
	 * animation state. 
	 */
	public void requestDeleteState(){
		AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
		AnimatedSpriteEditorGUI gui = singleton.getGUI();
		String currentAnimationStateName = singleton.getAnimationStateName();
		boolean deleted = editorIO.deletedAnimationState(currentAnimationStateName);
		if(deleted)
		{
			// NOW THAT WE'VE SAVED, LET'S MAKE SURE WE'RE IN THE RIGHT MODE
			singleton.getFileManager().getPoseurFileManager().setCurrentFile(null);
			singleton.setAnimationState(null);
			singleton.getStateManager().setState(EditorState.SELECT_ANIMATION_STATE);
			reloadSpriteType();
		}
		else
		{
    		JOptionPane.showMessageDialog(
	                gui,
	                ANIMATION_STATE_NAME_EXISTED_TEXT,
	                ANIMATION_STATE_NAME_EXISTED_TITLE_TEXT,
	                JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
     * This method lets the user to rename an
     * animation state 
     */
    public boolean requestRenameState(){
    	// SO NOW ASK THE USER FOR AN ANIMATION STATE NAME
    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        AnimatedSpriteEditorGUI gui = singleton.getGUI();

        String stateName = JOptionPane.showInputDialog(
                gui,
                ANIMATION_STATE_NAME_REQUEST_TEXT,
                ANIMATION_STATE_NAME_REQUEST_TITLE_TEXT,
                JOptionPane.QUESTION_MESSAGE);
        if( (stateName!=null) && (stateName.length()>0))
        {
        	boolean validInput = false;
        	AnimationState[] states = AnimationState.values();
        	for(int i=0; i<states.length; i++)
        	{
        		if(states[i].name().equals(stateName))
        			validInput = true;
        	}
        	if(validInput)
        	{
        		// UPDATE THE FILE NAMES AND FILE
        		boolean existed = singleton.getSpriteType().getAnimations().get(AnimationState.valueOf(stateName)) != null;
        		if(!existed)
        		{
        			boolean renamed = editorIO.renameAnimationState(currentSpriteTypeName, stateName);
        			if(renamed)
        			{
        				singleton.setAnimationState((AnimationState.valueOf(stateName)));
        			
        				// NOW THAT WE'VE SAVED, LET'S MAKE SURE WE'RE IN THE RIGHT MODE
        				reloadSpriteType();
        			
        				singleton.getStateManager().setState(EditorState.POSEUR_STATE);
        				return true;
        			}
        		} 
        		JOptionPane.showMessageDialog(
    	                gui,
    	                ANIMATION_STATE_NAME_EXISTED_TEXT,
    	                ANIMATION_STATE_NAME_EXISTED_TITLE_TEXT,
    	                JOptionPane.ERROR_MESSAGE);
    			return false;
        	}
            
        	JOptionPane.showMessageDialog(
        				gui,
                        ANIMATION_STATE_NAME_ERROR_TEXT,
                        ANIMATION_STATE_NAME_ERROR_TITLE_TEXT,
                        JOptionPane.ERROR_MESSAGE);
        }
         return false;
    }
    
    /**
     * This method will exit the application, making sure the user
     * doesn't lose any data first.
     */
    public void requestExit()
    {
        // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToExit = true;
        if (!poseurFileManager.isSaved())
        {
            // THE USER CAN OPT OUT HERE
            continueToExit = poseurFileManager.promptToSave();
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
        if ( (fileName != null)&& (fileName.length() > 0))
        {
        	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
            String animationStateName = JOptionPane.showInputDialog(
                    gui,
                    ANIMATION_STATE_NAME_REQUEST_TEXT,
                    ANIMATION_STATE_NAME_REQUEST_TITLE_TEXT,
                    JOptionPane.QUESTION_MESSAGE);
            
            if((animationStateName != null) && (animationStateName.length()>0))
            {
            	boolean validInput = false;
            	AnimationState[] states = AnimationState.values();
            	for(int i=0; i<states.length; i++)
            	{
            		if(states[i].name().equals(animationStateName))
            			validInput = true;
            	}
                if(validInput)
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
            			  	success = (
            			  			new File(SPRITE_TYPE_PATH + fileName + "/poses")).mkdir();
            			  	if (success) {
            			  		System.out.println("Directory: " 
            			  				+ fileName + "/poses" + " created");
            			  	}  
            			  	success = (
            			  			new File(SPRITE_TYPE_PATH + fileName + "/images")).mkdir();
            			  	if (success) {
            			  		System.out.println("Directory: " 
            			  				+ fileName  + "/poses" + " created");
                    		}       			  
                	}catch (Exception e){//Catch exception if any
                		System.err.println("Error: " + e.getMessage());
                	}
	        
                	SpriteType newSpriteType = new SpriteType();
	        
                	newSpriteType.setWidth(Integer.parseInt(SPRITE_TYPE_WIDTH));
                	newSpriteType.setHeight(Integer.parseInt(SPRITE_TYPE_HEIGHT));
                	singleton.setSpriteType(newSpriteType);
                	singleton.setSpriteTypeName(currentSpriteTypeName);
                	singleton.setAnimationState(AnimationState.valueOf(animationStateName));
                	
	        
                	// SAVE OUR NEW FILE
                	editorIO.saveSpriteType(currentFile, newSpriteType, animationStateName);
                	saved = true;

                	
                	boolean poseMade = poseurFileManager.promptForNew();
                	while(poseMade!=true)
                	{
                		poseMade = poseurFileManager.promptForNew();
                	}
                	
                	// AND PUT THE FILE NAME IN THE TITLE BAR
//                	String appName = gui.getAppName();
//                	gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile); 
            
                	reloadSpriteType();
                	
                	// WE DID IT!
                	return true;
            }
                JOptionPane.showMessageDialog(
                        gui,
                        ANIMATION_STATE_NAME_ERROR_TEXT,
                        ANIMATION_STATE_NAME_ERROR_TITLE_TEXT,
                        JOptionPane.ERROR_MESSAGE);
            	return false;
            }
            return false;
        }
        // USER DECIDED AGAINST IT
        return false;
    }
    
    private boolean promptForNewState()
    {
    	// SO NOW ASK THE USER FOR AN ANIMATION STATE NAME
    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        AnimatedSpriteEditorGUI gui = singleton.getGUI();
        String stateName = JOptionPane.showInputDialog(
                gui,
                ANIMATION_STATE_NAME_REQUEST_TEXT,
                ANIMATION_STATE_NAME_REQUEST_TITLE_TEXT,
                JOptionPane.QUESTION_MESSAGE);
        
        // IF THE USER CANCELLED, THEN WE'LL GET A fileName
        // OF NULL, SO LET'S MAKE SURE THE USER REALLY
        // WANTS TO DO THIS ACTION BEFORE MOVING ON
        if ((stateName != null)
                &&
            (stateName.length() > 0))
        {
        	Iterator<AnimationState> currentStates = singleton.getSpriteType().getAnimationStates();
        	while(currentStates.hasNext())
        	{
        		AnimationState state = currentStates.next();
        		if(state.name().equals(stateName))
        		{
        			JOptionPane.showMessageDialog(
        	                gui,
        	                ANIMATION_STATE_NAME_EXISTED_TEXT,
        	                ANIMATION_STATE_NAME_EXISTED_TITLE_TEXT,
        	                JOptionPane.ERROR_MESSAGE);
        			return false;
        		}
        	}
        	
        	boolean validInput = false;
        	AnimationState[] states = AnimationState.values();
        	for(int i=0; i<states.length; i++)
        	{
        		if(states[i].name().equals(stateName))
        			validInput = true;
        	}
            if(validInput)
            {
            	// UPDATE THE FILE NAMES AND FILE
            	editorIO.saveAnimationState(currentSpriteTypeName, stateName);
            	AnimatedSpriteEditor.getEditor().setAnimationState((AnimationState.valueOf(stateName)));
            	boolean poseMade = poseurFileManager.promptForNew();
            	while(poseMade!=true)
            	{
            		poseMade = poseurFileManager.promptForNew();
            	}
            	reloadSpriteType();
            	
            	return true;
            }
            
            JOptionPane.showMessageDialog(
                        gui,
                        ANIMATION_STATE_NAME_ERROR_TEXT,
                        ANIMATION_STATE_NAME_ERROR_TITLE_TEXT,
                        JOptionPane.ERROR_MESSAGE);
            	return false;
            
        }
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
	 * This method lets the user to duplicate an 
	 * animation state 
	 */
	private boolean promptToCopyState()
	{
		// SO NOW ASK THE USER FOR AN ANIMATION STATE NAME
	    AnimatedSpriteEditorGUI gui = AnimatedSpriteEditor.getEditor().getGUI();
	    String stateName = JOptionPane.showInputDialog(
	            gui,
	            ANIMATION_STATE_NAME_REQUEST_TEXT,
	            ANIMATION_STATE_NAME_REQUEST_TITLE_TEXT,
	            JOptionPane.QUESTION_MESSAGE);
	    
	    // IF THE USER CANCELLED, THEN WE'LL GET A fileName
	    // OF NULL, SO LET'S MAKE SURE THE USER REALLY
	    // WANTS TO DO THIS ACTION BEFORE MOVING ON

	    if ((stateName != null)
	            &&
	        (stateName.length() > 0))
	    {
	    	boolean validInput = false;
	    	AnimationState[] states = AnimationState.values();
	    	for(int i=0; i<states.length; i++)
	    	{
	    		if(states[i].name().equals(stateName))
	    		{
	    			validInput = true;
	    			break;
	    		}
	    	}
	        if(validInput)
	        {
	        	// UPDATE THE FILE NAMES AND FILE
	        	boolean copied = editorIO.copyAnimationState(stateName);
	        	if(copied)
	        	{
	        		AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
	        		singleton.setAnimationState(AnimationState.valueOf(stateName));
	        		singleton.getFileManager().reloadSpriteType();
	            	return true;
	        	}
	        	
	        }
	        
	        JOptionPane.showMessageDialog(
	                    gui,
	                    ANIMATION_STATE_NAME_ERROR_TEXT,
	                    ANIMATION_STATE_NAME_ERROR_TITLE_TEXT,
	                    JOptionPane.ERROR_MESSAGE);
	        	return false;
	        
	    }
		return false;
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
    
    /**
     * This method will resize an image.
     * @param originalImage the original image
     * @param scaledWidth	the intended width
     * @param scaledHeight	the intended height
     * @param preserveAlpha	perserveAlpha or not
     * @return
     */
    public BufferedImage createResizedCopy(Image originalImage, 
    		int scaledWidth, int scaledHeight, 
    		boolean preserveAlpha)
    {
    	int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
    	BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
    	Graphics2D g = scaledBI.createGraphics();
    	if (preserveAlpha) {
    		g.setComposite(AlphaComposite.Src);
    	}
    	g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
    	g.dispose();
    	return scaledBI;
    }


	public void reloadSpriteType()
	{
		AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
		AnimationState state = singleton.getAnimationState();
		final AnimatedSpriteEditorGUI gui = singleton.getGUI();

		editorIO.loadSpriteType(currentSpriteTypeName);
		if(state!=null)
		{
			singleton.setAnimationState(state);
			gui.updateAnimationStatesList();
			gui.getStateComboBoxModel().setSelectedItem(state);
		}
    	gui.revalidate();
    	gui.repaint();
	}
	
}