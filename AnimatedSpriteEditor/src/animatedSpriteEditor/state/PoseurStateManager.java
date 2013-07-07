package animatedSpriteEditor.state;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;



import animatedSpriteEditor.shapes.PoseurShape;
import animatedSpriteEditor.AnimatedSpriteEditor;
import static animatedSpriteEditor.AnimatedSpriteEditorSettings.*;
import animatedSpriteEditor.files.EditorFileManager;
import animatedSpriteEditor.files.InvalidXMLFileFormatException;
import animatedSpriteEditor.files.PoseurFileManager;
import animatedSpriteEditor.gui.AnimatedSpriteEditorGUI;
import animatedSpriteEditor.gui.PoseCanvas;
import animatedSpriteEditor.shapes.*;
/**
 * This class stores all the state information about the application
 * regarding the poses and rendering settings. Note that whenever
 * the data in this class changes, the visual representations
 * will also change.
 * 
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
public class PoseurStateManager 
{
    // THIS KEEPS TRACK OF WHAT MODE OUR APPLICATION
    // IS IN. IN DIFFERENT MODES IT BEHAVES DIFFERENTLY
    // AND DIFFERENT CONTROLS ARE ENABLED OR DISABLED
    private PoseurState state;
    
    // THIS IS THE POSE WE ARE ACTUALLY EDITING
    private PoseurPose pose;

    // THESE STORE THE STATE INFORMATION
    // FOR THE TWO CANVASES
    private PoseCanvasState trueCanvasState;
    private PoseCanvasState zoomableCanvasState;
       
    // THIS WILL SERVE AS OUR CLIPBOARD. NOTE THAT WE DO NOT
    // HAVE AN UNDO FEATURE, SO OUR CLIPBOARD CAN ONLY STORE
    // A SINGLE SHAPE
    private PoseurShape clipboard;
    
    // THIS WILL SERVE AS OUR CURRENTLY SELECTED SHAPE,
    // WHICH CAN BE MOVED AND CHANGED
    private PoseurShape selectedShape;

    // THIS IS A SHAPE THAT IS IN THE PROGRESS OF BEING
    // CREATED BY THE USER
    private PoseurShape shapeInProgress;
    private PoseurShapeType shapeInProgressType;

    // WE'LL USE THESE VALUES TO CALCULATE HOW FAR THE
    // MOUSE HAS BEEN DRAGGED BETWEEN CALLS TO
    // OUR mouseDragged METHOD CALLS
    private int lastMouseDraggedX;
    private int lastMouseDraggedY;
    
    /**
     * This constructor sets up all the state information regarding
     * the shapes that are to be rendered for the pose. Note that 
     * at this point the user has not done anything, all our
     * pose data structures are essentially empty.
     */
    public PoseurStateManager()
    {
        // WE ALWAYS START IN SELECT_SHAPE_MODE
        state = PoseurState.STARTUP_STATE;
        
        // CONSTRUCT THE POSE, WHICH WILL BE USED
        // TO RENDER OUR STUFF
        pose = new PoseurPose(DEFAULT_POSE_WIDTH, DEFAULT_POSE_HEIGHT);
        
        // NOW INITIALIZE THE CANVAS STATES. NOTE, DON'T CONFUSE
        // THE WORD "true" WITH THE PARAMETER "true". THEY HAVE
        // TWO SEPARATE MEANINGS
        trueCanvasState = new PoseCanvasState(
                false, pose, INIT_ZOOM_LEVEL, INIT_ZOOM_LEVEL, INIT_ZOOM_LEVEL);
        zoomableCanvasState = new PoseCanvasState(
                true, pose, INIT_ZOOM_LEVEL, ZOOM_FACTOR, MIN_ZOOM_LEVEL);

        // NOTHING IS ON THE CLIPBOARD TO START
        clipboard = null;
        
        // THERE IS NO SELECTED SHAPE YET
        selectedShape = null;
    }
 
    // ACCESSOR METHODS
    
    /**
     * Gets the current mode of the application.
     * 
     * @return The mode currently being used by the application. 
     */
    public PoseurState getMode() { return state; }

    /**
     * Gets the canvas state for the canvas on the left,
     * which does not zoom.
     * 
     * @return The canvas state for the left canvas.
     */
    public PoseCanvasState getTrueCanvasState() { return trueCanvasState; }

    /**
     * Gets the canvas state for the canvas on the right,
     * which can be zoomed in and out.
     * 
     * @return The canvas state for the right canvas.
     */
    public PoseCanvasState getZoomableCanvasState() { return zoomableCanvasState; }
        
    /**
     * Accessor method for getting the shape currently
     * being created.
     * 
     * @return The shape being created by the user.
     */
    public PoseurShape getShapeInProgress()
    {
        return shapeInProgress;
    }    
    
    /**
     * Accessor method for getting the pose object that
     * is being edited.
     * 
     * @return The Pose object currently being edited by the app.
     */
    public PoseurPose getPose() { return pose; }
    
    /**
     * Accessor method for getting the shape the user has
     * selected. Note that if no shape is currently selected,
     * null is returned.
     * 
     * @return The shape the use has selected, null if nothing
     * is currently selected.
     */
    public PoseurShape getSelectedShape() { return selectedShape; }
     
    /**
     * Accessor method that tests to see if a shape is currently
     * being created by the user. If so, true is returned, else
     * false.
     * 
     * @return true if the user is in the process of creating a
     * shape, false otherwise.0
     */
    public boolean isShapeInProgress()
    {
        return shapeInProgress != null;
    }

    /**
     * Accessor method to test and see if a shape is currently
     * on the clipboard.
     * 
     * @return true if a shape is currently on the clipboard,
     * false otherwise.
     */
    public boolean isShapeOnClipboard()
    {
        return clipboard != null;
    }

    /**
     * Accessor method to test and see if a shape is currently
     * selected.
     * 
     * @return true if a shape is currently selected, false otherwise.
     */
    public boolean isShapeSelected()
    {
        return selectedShape != null;
    }
    
    /**
     * Accessor method that tests if the testSelectedShape
     * argument is the currently selected shape. If it is,
     * true is returned, else false.
     * 
     * @param testSelectedShape The shape to test to see if it's
     * the same object as the currently selected shape.
     * 
     * @return true if testSelectedShape is the currently 
     * selected shape. false otherwise.
     */
    public boolean isSelectedShape(PoseurShape testSelectedShape)
    {
        return testSelectedShape == selectedShape;
    }
    
    // MUTATOR METHODS
    
    /**
     * This mutator method changes the mode of the application,
     * which may result in a cursor change and the enabling and
     * disabling of various controls.
     * 
     * @param newMode The mode to set as the current mode.
     * @throws InvalidXMLFileFormatException 
     */
    public void setState(PoseurState newMode) 
    {
        // KEEP THE MODE
        state = newMode;
        
        // AND UPDATE THE GUI
        AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        AnimatedSpriteEditorGUI gui = singleton.getGUI();
       
		gui.updatePoseurMode();
		
    }
    
    // METHODS MADE AVAILBLE TO OTHER CLASSES

    /**
     * This method is called when the user selects a shape type
     * to render. 
     * 
     * @param shapeType The shape type the user wishes to render.
     */
    public void selectShapeToDraw(PoseurShapeType shapeType)
    {
        // CLEAR THE SELECTED SHAPE
        selectedShape = null;
        
        // CHANGE THE MODE
        setState(PoseurState.CREATE_SHAPE_STATE);
        
        // THIS WILL UNDO ANY SHAPE IN PROGRESS ALREADY
        shapeInProgress = null;    
        shapeInProgressType = shapeType;
        
        // REPAINT THE CANVASES
        repaintCanvases();
    }
    
    /**
     * Mutator method for the current pose.
     * 
     * @param poseToSet
     */
    public void setPose(PoseurPose poseToSet)
    {
    	pose = poseToSet;
    }
    
    /**
     * This method manages the response to mouse button dragging
     * on the right canvas. 
     * 
     * @param x X-coordinate of the mouse drag.
     * 
     * @param y Y-coordinate of the mouse drag.
     */
    public void processMouseDragged(int x, int y)
    {
        // WE MAY HAVE TO USE VALUES IN POSE SPACE
        Rectangle2D.Double poseArea = zoomableCanvasState.getPoseArea();
        int incX = x - lastMouseDraggedX;
        int incY = y - lastMouseDraggedY;
        lastMouseDraggedX = x;
        lastMouseDraggedY = y;

        // IF WE ARE IN SHAPE_DRAG_MODE, THEN WHEREVER
        // THE MOUSE GOES IN THE POSE AREA THE SHAPE
        // MUST FOLLOW. NOTE THAT ONCE THE MOUSE
        // LEAVES THE POSE AREA WE NEED TO SWITCH
        // TO SHAPE_SELECTED_MODE
        if (state == PoseurState.DRAG_SHAPE_STATE)
        {
            // MAKE SURE WE'RE NOT WAY OFF THE POSE AREA
            if (poseArea.contains(x, y))
            {
                // DRAG THE SHAPE
                selectedShape.moveShape(incX, incY,  new Rectangle2D.Double(0, 0, 256, 256));
                repaintCanvases();
            }
            // WE WENT WAY OFF THE EDGE, LET'S STOP DRAGGING
            else
            {
                setState(PoseurState.SHAPE_SELECTED_STATE);
            }
        }
        // THIS IS AFTER WE'VE STARTED RENDERING A SHAPE
        // AND ARE DRAGGING THE MOUSE TO COMPLETE IT
        else if (state == PoseurState.COMPLETE_SHAPE_STATE)
        {
            // WE HAVE TO USE POSE SPACE COORDINATES
            float zoomLevel = zoomableCanvasState.getZoomLevel();
            int poseSpaceX = (int)((x - poseArea.getX()) / zoomLevel);
            int poseSpaceY = (int)((y - poseArea.getY()) / zoomLevel);
            
            // IF WE'RE NOT IN THE POSE AREA, WE'RE NOT MAKING A SHAPE
            if (    (poseSpaceX < 0) ||
                    (poseSpaceY < 0) ||
                    (poseSpaceX >= (poseArea.getWidth() / zoomLevel)) ||
                    (poseSpaceY >= (poseArea.getHeight() / zoomLevel)))
            {
                // IF WE GO OUTSIDE THE POSE AREA START OVER
                setState(PoseurState.CREATE_SHAPE_STATE);
                shapeInProgress = null;
                return;
            }

            // IF WE GET HERE THE SHAPE IS BEING DRAWN
            shapeInProgress.updateShapeInProgress(poseSpaceX, poseSpaceY);
                
            // REPAINT OF COURSE
            repaintCanvases();           
        }     
    }   
    
    /**
     * This method manages the response to a mouse button press
     * on the right canvas. 
     * 
     * @param x X-coordinate of the mouse press.
     * 
     * @param y Y-coordinate of the mouse press.
     */    
    public void processMousePress(int x, int y)
    {
        // WE MAY HAVE TO USE VALUES IN POSE SPACE
        Rectangle2D poseArea = zoomableCanvasState.getPoseArea();
        float zoomLevel = zoomableCanvasState.getZoomLevel();
        int poseSpaceX = (int)((x - poseArea.getX()) / zoomLevel);
        int poseSpaceY = (int)((y - poseArea.getY()) / zoomLevel);
        lastMouseDraggedX = x;
        lastMouseDraggedY = y;

        // IF WE'RE NOT IN THE POSE AREA WE WON'T DO ANYTHING
        if (    (poseSpaceX < 0) ||
                (poseSpaceY < 0) ||
                (poseSpaceX > poseArea.getWidth()) ||
                (poseSpaceY > poseArea.getHeight()))
        {
            return;
        }
                        
        // IF WE ARE IN SELECT_SHAPE_MODE, FIND THE FIRST SHAPE,
        // TOP TO BOTTOM, THAT CONTAINS THE POINT.
            // IF NO SHAPE IS FOUND, DO NOTHING
            // IF A SHAPE IS FOUND, SWITCH TO DRAG_SHAPE_MODE
            //   AND MAKE THE FOUND SHAPE THE SELECTED SHAPE
        if ((state == PoseurState.SELECT_SHAPE_STATE) ||
            (state == PoseurState.SHAPE_SELECTED_STATE))
        {
        	boolean found = false;
        	int i=pose.getShapesList().size()-1;
        	while(!found && i>=0){
        		PoseurShape testShape = pose.getShapesList().get(i);
        
        		if(testShape.containsPoint(new Point2D.Double(poseSpaceX, poseSpaceY))){
        			selectedShape = pose.getShapesList().get(i);
        			setState(PoseurState.DRAG_SHAPE_STATE);
        			found = true;		
        		}
        		i--;
        	}
            // IF WE DON'T FIND ONE, RESET IT SO THERE
            // IS NO SELECTED SHAPE
            if(!found){
            	selectedShape = null;
            	setState(PoseurState.SELECT_SHAPE_STATE);
            }
        }        
        // IF WE ARE IN CREATE_SHAPE_MODE
        else if (state == PoseurState.CREATE_SHAPE_STATE)
        {
            // LET'S MAKE A NEW SHAPE
            if (shapeInProgressType == PoseurShapeType.RECTANGLE)
            {
                shapeInProgress = PoseurRectangle.factoryBuildRectangle(poseSpaceX, poseSpaceY);
            }
            else if (shapeInProgressType == PoseurShapeType.ELLIPSE)
            {
                shapeInProgress = PoseurEllipse.factoryBuildCircle(poseSpaceX, poseSpaceY);
            }
            else if (shapeInProgressType == PoseurShapeType.LINE)
            {
                shapeInProgress = PoseurLine.factoryBuildLine(poseSpaceX, poseSpaceY);
            }

            // WE NEED TO SWITCH MODES
            setState(PoseurState.COMPLETE_SHAPE_STATE);
        }
    }

    /**
     * This method responds to a mouse button released on 
     * the right canvas.
     * 
     * @param x X-coordinate of where the mouse button release happened.
     * 
     * @param y Y-coordinate of where the mouse button release happened.
     */
    public void processMouseReleased(int x, int y)
    {
        // ARE WE DONE DRAGGING THE SHAPE AROUND?
        if (state == PoseurState.DRAG_SHAPE_STATE)
        {
            setState(PoseurState.SHAPE_SELECTED_STATE);
        }
        // OR DID WE COMPLETE MAKING OUR SHAPE?
        else if (state == PoseurState.COMPLETE_SHAPE_STATE)
        {
            // WE HAVE TO USE POSE SPACE COORDINATES
            Rectangle2D poseArea = zoomableCanvasState.getPoseArea();
            float zoomLevel = zoomableCanvasState.getZoomLevel();
            int poseSpaceX = (int)((x - poseArea.getX()) / zoomLevel);
            int poseSpaceY = (int)((y - poseArea.getY()) / zoomLevel);
            lastMouseDraggedX = x;
            lastMouseDraggedY = y;
        
            // IF WE'RE NOT IN THE POSE AREA, WE'RE NOT MAKING A SHAPE
            if (    (poseSpaceX < 0) ||
                    (poseSpaceY < 0) ||
                    (poseSpaceX > poseArea.getWidth()) ||
                    (poseSpaceY > poseArea.getHeight()))
            {
                setState(PoseurState.CREATE_SHAPE_STATE);
                shapeInProgress = null;
                return;
            }

            // ASK THE SHAPE TO UPDATE ITSELF BASE ON WHERE ON
            // THE POSE THE MOUSE BUTTON WAS RELEASED
            if (!shapeInProgress.completesValidShape(poseSpaceX, poseSpaceY))
            {
                shapeInProgress = null;
                setState(PoseurState.CREATE_SHAPE_STATE);
            }
            else
            {
                // OUR LITTLE SHAPE HAS GROWN UP
                pose.addShape(shapeInProgress);
                selectedShape = shapeInProgress;
                shapeInProgress = null;
                
                // WE CAN DRAW ANOTHER ONE NOW
                setState(PoseurState.CREATE_SHAPE_STATE);
                
                // REPAINT OF COURSE
                repaintCanvases();
            }
        }
    }
    
    /**
     * This method clears the shape in the clip board. It is
     * useful when we load a pose while we have something in 
     * the clip board because we don't want what's from the old
     * pose to be paste in the new pose.
     * 
     */
    public void clearClipboardShape() 
    { 
    	clipboard = null; 
    }
    
    /**
     * This method clears the select shape and set the 
     * isShapeSelected value to false;
     */
    public void clearSelectShape()
    {	
    	selectedShape = null;
    }
    
    /**
     * This method processes a request to cut the selected item, which 
     * will remove it from the rendering surface and place it on the
     * clipboard.
     */
    public void cutSelectedItem()
    {
        // REMOVE SELECTED ITEM FROM RENDER LIST
        pose.removeShape(selectedShape);
        
        // PLACE SELECTED ITEM ONTO CLIPBOARD
        clipboard = selectedShape;
        
        // THE POSE HAS CHANGED
        markPoseChanged();
        
        // FORCE A GUI UPDATE
        setState(state);
        
        // REPAINT THE CANVASES
        repaintCanvases();
    }    
    
    /**
     * This will process a request to copy the selected item, which
     * will copy the selected item and put it on the clipboard.
     */
    public void copySelectedItem()
    {
        // MAKE A COPY OF THE SELECTED ITEM AND        
        // PLACE THE SELECTED ITEM ON THE CLIPBOARD
        clipboard = selectedShape.clone();
        
        // THIS JUST REFRESHES CONTROLS
        setState(state);
    }

    /**
     * This method puts the item in the clipboard onto the 
     * rendering surface.
     */
    public void pasteSelectedItem()
    {
        // GET THE SHAPE NO THE CLIPBOARD AND ADD IT
        // TO THE RENDERING SURFACE
        PoseurShape shapeToPaste = clipboard.clone();
        shapeToPaste.move(0, 0);
        selectedShape = null;
        pose.addShape(shapeToPaste);

        // REPAINT THE CANVASES
        repaintCanvases();
    } 
    
    /**
     * This method moves the selected shape to the bottom
     * of the pose, meaning it will be drawn first and 
     * all other shapes will be drawn on top of it.
     */
    public void moveSelectedItemToBack()
    {
    	PoseurShape shapeToMove = selectedShape.clone();
        pose.getShapesList().remove(selectedShape);
        pose.getShapesList().add(0, shapeToMove);
        selectedShape = pose.getShapesList().getFirst();
        markPoseChanged();
        repaintCanvases();
    }    
    
    /**
     * This method moves the selected shape to the top
     * of the pose, meaning it will be drawn last and
     * on top of all other shapes.
     */
    public void moveSelectedItemToFront()
    {
        PoseurShape shapeToMove = selectedShape.clone();
        pose.getShapesList().remove(selectedShape);
        pose.getShapesList().add(shapeToMove);
        selectedShape = pose.getShapesList().getLast();
        markPoseChanged();
        repaintCanvases();
    }        

    /**
     * This method will assign the selected color to the selected
     * shape, if there is one, based on which toggle button is
     * selected. Note that it will also change the color in the
     * toggle button.
     * 
     * @param selectedColor The color used to change the selected
     * shape and/or the outline/fill toggle button background.
     */
    public void selectPalletColor(Color selectedColor)
    {
        AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        AnimatedSpriteEditorGUI gui = singleton.getGUI();
        
        // IF A SHAPE IS SELECTED, THEN WE'RE GOING TO CHANGE IT
        if (isShapeSelected())
        {
            // MAKR POSE CHANGED
            markPoseChanged();
            
            if (gui.isOutlineColorSelectionButtonToggled())
            {
                selectedShape.setOutlineColor(selectedColor);
            }
            else
            {
                selectedShape.setFillColor(selectedColor);
            }
            // REPAINT THE SHAPES
            repaintCanvases();
        }
        
        // EITHER WAY WE CHANGE THE BACKGROUND OF THE TOGGLE BUTTON
        if (gui.isOutlineColorSelectionButtonToggled())
        {
            gui.setOutlineToggleButtonColor(selectedColor);
        }
        else
        {
            gui.setFillToggleButtonColor(selectedColor);
        }
    }   
    
    /**
     * Called when the user selects a rendering stroke from the combo box,
     * this method makes sure that stroke is the currently used stroke for
     * future rendering and also applies that stroke to any currently
     * selected shape.
     * 
     * @param strokeThickness The stroke thickness, in pixels. A stroke of
     * 1 would mean lines would be drawn 1 pixel wide.
     */
    public void selectStroke(int strokeThickness)
    {
    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
    	AnimatedSpriteEditorGUI gui = singleton.getGUI();
        
        // IF A SHAPE IS SELECTED, THEN WE'RE GOING TO CHANGE IT
        if (isShapeSelected())
        {
            BasicStroke stroke = new BasicStroke(strokeThickness);
            selectedShape.setOutlineThickness(stroke);
            
            markPoseChanged();
            // REPAINT THE SHAPES
            repaintCanvases();
        }
    }    
    
    /**
     * Resets the state of the system such that the pose has
     * no shapes and there are no shapes in progress or
     * selected.
     */
    public void resetState()
    {
        pose.reset();
        selectedShape = null;
        shapeInProgress = null;
        setState(PoseurState.SHAPE_SELECTED_STATE);
    }
    
    /**
     * Changes the state (i.e. mode) of the application
     * such that the user may select shapes.
     */
    public void startShapeSelection()
    {
        setState(PoseurState.SELECT_SHAPE_STATE);
    }
        
    /**
     * Called any time the contents of this state change such that it
     * affects the view, this method forces both canvases to repaint.
     */
    private void repaintCanvases()
    {
        // THE POSE HAS CHANGED
        markPoseChanged();        
       
        PoseCanvas trueCanvas = trueCanvasState.getCanvas();
        trueCanvas.repaint();
        PoseCanvas zoomableCanvas = zoomableCanvasState.getCanvas();
        zoomableCanvas.repaint();
    }
    
    /**
     * Changes the alpha value of the selected shape
     * to the alpha argument.
     * 
     * @param alpha The transparency to use for changing the 
     * selected shape. Note that 0 is fully transparent and
     * 255 is fully opaque and values between results in
     * scaled transparency accordingly.
     */
    public void changeSelectedShapeAlpha(int alpha)
    {
        if (selectedShape != null)
        {
            selectedShape.setAlpha(alpha);
            markPoseChanged();
            repaintCanvases();
        }
    }

    /**
     * Changes the dimensions of the pose we're editing.
     * 
     * @param initWidth The new pose width to use.
     * 
     * @param initHeight The new pose height to use.
     */
    public void setPoseDimensions(int initWidth, int initHeight)
    {
        // CHANGE THE DATA0
        pose.setPoseWidth(initWidth);
        pose.setPoseHeight(initHeight);
        trueCanvasState.updatePoseArea();
        zoomableCanvasState.updatePoseArea();
        
        markPoseChanged();
        // AND RENDER EVERYTHING WITH THE NEW DATA
        repaintCanvases();
    }

    /**
     * This method sets the state to its current state, which
     * cascades a reloading of gui control enabling and disabling.
     */
    public void refreshState()
    {
        setState(state);
        repaintCanvases();
    }
    
    /**
     * We call this method every time something in the pose has changed
     * such that we are aware that the most recent Pose does not match
     * the file.
     */
    public void markPoseChanged()
    {
    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        PoseurFileManager fileManager = singleton.getFileManager().getPoseurFileManager();
        fileManager.markFileAsNotSaved();
        singleton.getGUI().updatePoseurMode();
    }    
}