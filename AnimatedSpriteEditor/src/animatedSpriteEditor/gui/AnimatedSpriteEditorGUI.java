package animatedSpriteEditor.gui;

import static animatedSpriteEditor.AnimatedSpriteEditorSettings.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.border.Border;

import animatedSpriteEditor.events.canvas.PoseCanvasComponentHandler;
import animatedSpriteEditor.events.canvas.PoseCanvasMouseHandler;
import animatedSpriteEditor.events.colors.ColorPalletHandler;
import animatedSpriteEditor.events.colors.CustomColorHandler;
import animatedSpriteEditor.events.colors.FillColorHandler;
import animatedSpriteEditor.events.colors.LineThicknessHandler;
import animatedSpriteEditor.events.colors.OutlineColorHandler;
import animatedSpriteEditor.events.colors.TransparencyHandler;
import animatedSpriteEditor.events.display.SlowDownHandler;
import animatedSpriteEditor.events.display.SpeedUpHandler;
import animatedSpriteEditor.events.display.StartHandler;
import animatedSpriteEditor.events.display.StopHandler;
import animatedSpriteEditor.events.edit.CopyHandler;
import animatedSpriteEditor.events.edit.CutHandler;
import animatedSpriteEditor.events.edit.MoveToBackHandler;
import animatedSpriteEditor.events.edit.MoveToFrontHandler;
import animatedSpriteEditor.events.edit.PasteHandler;
import animatedSpriteEditor.events.edit.StartSelectionHandler;
import animatedSpriteEditor.events.files.DeleteHandler;
import animatedSpriteEditor.events.files.ExitHandler;
import animatedSpriteEditor.events.files.ExportPoseHandler;
import animatedSpriteEditor.events.files.NewHandler;
import animatedSpriteEditor.events.files.NewPoseHandler;
import animatedSpriteEditor.events.files.NewStateHandler;
import animatedSpriteEditor.events.files.OpenHandler;
import animatedSpriteEditor.events.files.PoseSelectionHandler;
import animatedSpriteEditor.events.files.SaveAsHandler;
import animatedSpriteEditor.events.files.SaveHandler;
import animatedSpriteEditor.events.files.StateSelectionHandler;
import animatedSpriteEditor.events.shape.EllipseSelectionHandler;
import animatedSpriteEditor.events.shape.LineSelectionHandler;
import animatedSpriteEditor.events.shape.RectangleSelectionHandler;
import animatedSpriteEditor.events.window.PoseurWindowHandler;
import animatedSpriteEditor.events.zoom.ChangePoseDimensionsHandler;
import animatedSpriteEditor.events.zoom.ZoomInHandler;
import animatedSpriteEditor.events.zoom.ZoomOutHandler;



import animatedSpriteEditor.AnimatedSpriteEditor;
import animatedSpriteEditor.files.ColorPalletLoader;
import animatedSpriteEditor.files.PoseurFileManager;
import animatedSpriteEditor.gui.ColorPallet;
import animatedSpriteEditor.gui.ColorToggleButton;
import animatedSpriteEditor.gui.EditorCanvas;
import animatedSpriteEditor.gui.AnimatedSpriteEditorGUI;
import animatedSpriteEditor.shapes.PoseurShape;
import animatedSpriteEditor.state.ColorPalletState;
import animatedSpriteEditor.state.EditorState;
import animatedSpriteEditor.state.EditorStateManager;
import animatedSpriteEditor.state.PoseCanvasState;
import animatedSpriteEditor.state.PoseurPose;
import animatedSpriteEditor.state.PoseurState;
import animatedSpriteEditor.state.PoseurStateManager;
import animatedSpriteEditor.gui.PoseDimensionsDialog;

/**
 * This class sets up the main GUI of the application with all the 
 * components and their listener. 
 * @author sixin
 *
 */
public class AnimatedSpriteEditorGUI  extends JFrame{
    // THE NAME OF THE APPLICATION, WE'LL PUT THIS IN THE 
    // WINDOW'S TITLE BAR, BUT MIGHT ALSO INCLUDE THE 
    // FILE NAME THERE
    protected String appName;
        
    // WE'LL HAVE TWO CANVASES IN THE CENTER, THE
    // ONE ON THE LEFT IS THE TRUE ONE, AND WILL NEVER
    // ZOOM THE VIEW. THE ONE ON THE RIGHT WILL ALLOW 
    // FOR ZOOMED IN AND OUT VIEWS. NOTE THAT WE'LL PUT
    // THEM INTO A SPLIT PANE
    private JSplitPane canvasSplitPane;
    private EditorCanvas trueCanvas;
    private EditorCanvas zoomableCanvas;
    private JPanel displayArea;
    private JComboBox stateComboBox;
    private JTextField stateComboBoxMessage;
    private JPanel northOfDisplayArea;
    private JPanel displayToolBar;
    private JPanel statePanel;
    
    // NORTH PANEL - EVERYTHING ELSE GOES IN HERE
    private JPanel northPanel;
    private JPanel northOfNorthPanel;
    private JPanel southOfNorthPanel;
    
    // FILE CONTROLS
    private JToolBar fileToolbar;
    private JButton newButton;
    private JButton newStateButton;
    private JButton newPoseButton;
    private JButton deleteButton;
    private JButton openButton;
    private JButton saveButton;
    private JButton saveAsButton;
    private JButton exportButton;
    private JButton exitButton;
    
    // EDIT CONTROLS
    private JToolBar editToolbar;
    private JButton selectionButton;
    private JButton cutButton;
    private JButton copyButton;
    private JButton pasteButton;
    private JButton moveToFrontButton;
    private JButton moveToBackButton;
    
    // SHAPE SELECTION CONTROLS
    private JToolBar shapeToolbar;
    private JToggleButton rectToggleButton;
    private JToggleButton ellipseToggleButton;
    private JToggleButton lineToggleButton;
    private ButtonGroup shapeButtonGroup;
    private JComboBox lineStrokeSelectionComboBox;
    
    // ZOOM CONTROLS
    private JToolBar zoomToolbar;
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private JButton dimensionsButton;
    private JLabel zoomLabel;
        
    // COLOR SELECTION CONTROLS
    private JToolBar colorSelectionToolbar;
    private ColorToggleButton outlineColorSelectionButton;
    private ColorToggleButton fillColorSelectionButton;
    private ButtonGroup colorButtonGroup;
    private ColorPallet colorPallet;    
    private JButton customColorSelectorButton;
    private JLabel alphaLabel;
    private JSlider transparencySlider;

    // THIS TOOLBAR WILL ALLOW THE USER TO CONTROL ANIMATION
    private JPanel animationToolbar;
    private JButton startButton;
    private JButton stopButton;
    private JButton speedUpButton;
    private JButton slowDownButton;
    
    // THIS IS FOR THE POSES DISPLAYING
    private JPanel poseList;
    private JScrollPane scrollPane;
    private JButton randomButton;
    private JButton randomButton2;
    private JButton randomButton3;
    private JButton randomButton4;
    private JButton randomButton5;
    
    private MediaTracker tracker;
    /**
     * Default constructor for initializing the GUI. Note that the Poseur
     * application's state manager must have already been constructed and
     * setup when this method is called because we'll use some of the values
     * found there to initializer the GUI. The color pallet, for example, must
     * have already been loaded into the state manager before this constructor
     * is called.
     */
    public AnimatedSpriteEditorGUI()
    {
        // IN CASE THE PARENT DOES ANYTHING, I USUALLY LIKE TO EXPLICITY INCLUDE THIS
        super();
        
        // CONSTRUCT AND LAYOUT THE COMPONENTS
        initGUI();

        // AND SETUP THE HANDLERS
        initHandlers();
        
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    // ACCESSOR METHODS
    
    /**
     * Accessor method for getting the application's name.
     * 
     * @return The name of the application this window is 
     * being used for.
     */
    public String getAppName() { return appName; }
    
    /**
     * Accessor method for getting the color pallet.
     * 
     * @return The color pallet component.
     */
    public ColorPallet getColorPallet() { return colorPallet; }
    
    /**
     * Accessor method for getting the color currently set
     * for filling shapes.
     * 
     * @return The fill color currently in use for making shapes.
     */
    public Color getFillColor() { return fillColorSelectionButton.getBackground(); }

    /**
     * Accessor method for getting the color currently set
     * four outlining shapes.
     * 
     * @return The outline color currently in use for making shapes.
     */
    public Color getOutlineColor() { return outlineColorSelectionButton.getBackground(); }

    /**
     * Accessor method for getting the line thickness currently
     * set for drawing shape outlines and lines.
     * 
     * @return The line thickness currently in use for making shapes.
     */
    public int getLineThickness() { return lineStrokeSelectionComboBox.getSelectedIndex() + 1; }
    
    /**
     * Accessor method for getting the current transparency value of the slider.
     * 
     * @return The alpha value, between 0 (fully transparent) and 255 (fully opaque)
     * as currently set by the transparency slider.
     */
    public int getAlphaTransparency() { return transparencySlider.getValue(); }
    
    /**
     * Accessor method for getting the canvas that will
     * not zoom and will render the pose as is.
     * 
     * @return The true canvas, which is on the left.
     */
    public EditorCanvas getTruePoseCanvas() { return trueCanvas; }

    /**
     * Accessor method for getting the canvas that will
     * zoom in and out, rendering the pose accordingly.
     * 
     * @return The zoomable canvas, which is on the right.
     */
    public EditorCanvas getZoomablePoseCanvas() { return zoomableCanvas; }
    
    /**
     * Accessor method to the media tracker.
     */
    public MediaTracker getMediaTracker(){ return tracker;}
    
    /**
     * Accessor method to test if the outline color toggle button
     * is selected. Note that either the outline or fill button
     * must be selected at all times.
     * 
     * @return true if the outline toggle button is selected, false if
     * the fill button is selected.
     */
    public boolean isOutlineColorSelectionButtonToggled() { return outlineColorSelectionButton.isSelected(); }
    
    // MUTATOR METHODS
    
    /**
     * Mutator method for setting the app name.
     * 
     * @param initAppName The name of the application,
     * which will be put in the window title bar.
     */
    public void setAppName(String initAppName)
    {
        appName = initAppName;
    }    
    

	/**
     * This mutator method sets the background color for the
     * outline toggle button, which can then be used for 
     * the outline of new shapes.
     * 
     * @param initColor The color to use for shape outlines.
     */
    public void setOutlineToggleButtonColor(Color initColor)
    {
        outlineColorSelectionButton.setBackground(initColor);
    }

    /**
     * This mutator method sets the background color for the fill
     * toggle button, which can then be used for the fill
     * color of new shapes.
     * 
     * @param initColor The color to use for shape filling.
     */
    public void setFillToggleButtonColor(Color initColor)
    {
        fillColorSelectionButton.setBackground(initColor);
    }

    // PUBLIC METHODS OTHER CLASSES NEED TO CALL  
    
    /**
     * The fill and outline toggle buttons are connected,
     * only one can be toggled on a any time. This method
     * turns the fill toggle button on.
     */
    public void toggleFillColorButton()
    {
        fillColorSelectionButton.select();
        outlineColorSelectionButton.deselect();
    }
    
    /**
     * The fill and outline toggle buttons are connected,
     * only one can be toggled on a any time. This method
     * turns the outline toggle button on.
     */
    public void toggleOutlineColorButton()
    {
        fillColorSelectionButton.deselect();
        outlineColorSelectionButton.select();
    }
        
    /**
     * This method updates the appropriate toggle button,
     * either outline or fill, with the color argument, 
     * setting it to its background.
     * 
     * @param color Color to use to set the background
     * for the appropriate toggle button.
     */
    public void updateDrawingColor(Color color)
    {
        // IF THE OUTLINE TOGGLE IS THE ONE THAT'S
        // CURRENTLY SELECTED, THEN THAT'S THE ONE
        // THE USER WANTED TO CHANGE
        if (outlineColorSelectionButton.isSelected())
        {
            outlineColorSelectionButton.setBackground(color);
        }
        // OTHERWISE IT'S THE FILL TOGGLE BUTTON
        else if (fillColorSelectionButton.isSelected())
        {
            fillColorSelectionButton.setBackground(color);
        }
        
        
    }
    
    /**
     * Called each time the application's state changes, this method
     * is responsible for enabling, disabling, and generally updating 
     * all the GUI control based on what the current application
     * state (i.e. the PoseurMode) is in.
     */
    public final void updateMode()
    {
        // WE'LL NEED THESE GUYS
    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        EditorStateManager state = singleton.getStateManager();
        EditorState mode = state.getMode();

        
        if (mode == EditorState.SPRITE_TYPE_STATE)
        {


        	
        }
        
        else if (mode == EditorState.SELECT_ANIMATION_STATE)
        {
            
        	
        	
        	
        }
        
        else if (mode == EditorState.POSEUR_STATE)
        {
            updatePoseurMode();
        }     
    }

    /**
     * Called each time the application's state changes, this method
     * is responsible for enabling, disabling, and generally updating 
     * all the GUI control based on what the current application
     * state (i.e. the PoseurMode) is in.
     */
    public final void updatePoseurMode()
    {
        // WE'LL NEED THESE GUYS
        AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        animatedSpriteEditor.state.PoseurStateManager state = singleton.getStateManager().getPoseurStateManager();
        animatedSpriteEditor.state.PoseurState mode = state.getMode();
        PoseurFileManager fileManager = singleton.getFileManager().getPoseurFileManager();

        // IN THIS MODE THE USER IS DRAGGING THE MOUSE TO
        // COMPLETE THE DRAWING OF A SINGLE SHAPE
        if (mode == PoseurState.COMPLETE_SHAPE_STATE)
        {
            // THIS USES THE CROSSHAIR
            selectCursor(Cursor.CROSSHAIR_CURSOR);
        }
        // IN THIS MODE THE USER IS ABOUT TO START DRAGGING
        // THE MOUSE TO CREATE A SHAPE
        else if (mode == PoseurState.CREATE_SHAPE_STATE)
        {
            // THIS USES THE CROSSHAIR
            selectCursor(Cursor.CROSSHAIR_CURSOR);
            
            // TURN THE APPROPRIATE CONTROLS ON/OFF
            setEnabledEditControls(false);
            selectionButton.setEnabled(true);            
        }
        // IN THIS STATE THE USER HAS SELECTED A SHAPE
        // ON THE CANVAS AND IS DRAGGING IT
        else if (mode == PoseurState.DRAG_SHAPE_STATE)
        {
            // THIS USES THE MOVE 
            selectCursor(Cursor.MOVE_CURSOR);
        }
        // IN THIS STATE THE USER IS ABLE TO CLICK ON
        // A SHAPE TO SELECT IT. THIS IS THE MOST COMMON
        // STATE AND IS THE DEFAULT AT THE START OF THE APP
        else if (mode == PoseurState.SELECT_SHAPE_STATE)
        {
            // THIS USES THE ARROW CURSOR
            selectCursor(Cursor.DEFAULT_CURSOR);
            
            // THERE IS NO SHAPE SELECTED, SO WE CAN'T
            // USE THE EDIT CONTROLS
            enableSaveAsAndExport();
            setEnabledEditControls(false);
            selectionButton.setEnabled(false);
            setEnabledColorControls(true);
            setEnabledShapeControls(true);
            setEnabledZoomControls(true);
        }
        // IN THIS STATE A SHAPE HAS BEEN SELECTED AND SO WE
        // MAY EDIT IT, LIKE CHANGE IT'S COLORS OR TRANSPARENCY
        else if (mode == PoseurState.SHAPE_SELECTED_STATE)
        {
            // THIS USES THE ARROW CURSOR
            selectCursor(Cursor.DEFAULT_CURSOR);
            
            // THE EDIT CONTROLS CAN NOW BE USED
            setEnabledEditControls(true);
        }
        // THIS IS THE STATE WHEN THE Poseur APP FIRST
        // STARTS. THERE IS NO Pose YET, SO MOST CONTROLS
        // ARE DISABLED
        else if (mode == PoseurState.STARTUP_STATE)
        {
            // THIS USES THE ARROW CURSOR
            selectCursor(Cursor.DEFAULT_CURSOR);
            
            // NOTHING IS SELECTED SO WE CAN'T EDIT YET
            enableStartupFileControls();
            setEnabledEditControls(false);
            selectionButton.setEnabled(false);
            setEnabledColorControls(false);
            toggleOutlineColorButton();
            setEnabledZoomControls(false);
            setEnabledShapeControls(false);
        }
        saveButton.setEnabled(!fileManager.isSaved());
        
        // AND UPDATE THE SLIDER
        PoseurShape selectedShape = state.getSelectedShape();
        if (selectedShape != null)
        {
            // UPDATE THE SLIDER ACCORDING TO THE SELECTED
            // SHAPE'S ALPHA (TRANSPARENCY) VALUE, IF THERE
            // EVEN IS A SELECTED SHAPE
            transparencySlider.setValue(selectedShape.getAlpha());
        }    

        // REDRAW EVERYTHING
        zoomableCanvas.repaint();        
    }
    

    /**
     * This helper method constructs and lays out all GUI components, initializing
     * them to their default startup state.
     */
    private void initGUI()
    {    	
        // MAKE THE COMPONENTS
        constructGUIControls();
        
        // AND ARRANGE THEM
        layoutGUIControls();
    }
    
    /**
     * Helper method that constructs all the GUI controls and
     * loads them with their necessary art and data.
     */    
    private void constructGUIControls()
    {
        // SOME COMPONENTS MAY NEED THE STATE MANAGER
        // FOR INITIALIZATION, SO LET'S GET IT
    	AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        PoseurStateManager poseurStateManager = singleton.getStateManager().getPoseurStateManager();

        // LET'S START BY INITIALIZING THE CENTER AREA,
        // WHERE WE'LL RENDER EVERYTHING. WE'LL HAVE TWO
        // CANVASES AND PUT THEM INTO DIFFERENT SIDES
        // OF A JSplitPane
        canvasSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // LET'S MAKE THE CANVAS TO KEEP RECORD
        PoseCanvasState trueCanvasState = poseurStateManager.getTrueCanvasState();
        trueCanvas = new EditorCanvas(trueCanvasState);
        trueCanvasState.setPoseCanvas(trueCanvas);
        trueCanvas.setBackground(TRUE_CANVAS_COLOR);
        
        // AND NOW THE CANVAS ON THE RIGHT SIDE, WHICH
        // WILL BE ZOOMABLE
        PoseCanvasState zoomableCanvasState = poseurStateManager.getZoomableCanvasState();
        zoomableCanvas = new EditorCanvas(zoomableCanvasState);
        zoomableCanvasState.setPoseCanvas(zoomableCanvas);
        zoomableCanvas.setBackground(ZOOMABLE_CANVAS_COLOR);
        
        displayArea = new JPanel(new BorderLayout());
        displayArea.setBackground(Color.white);
        northOfDisplayArea = new JPanel();
        
        // ULTIMATELY EVERYTHING IN THE NORTH GOES IN HERE, INCLUDING
        // TWO PANELS FULL OF JToolBars
        northPanel = new JPanel();
        northOfNorthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southOfNorthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // WE'LL BATCH LOAD THE IMAGES
       tracker = new MediaTracker(this);
        int idCounter = 0;

        // FILE CONTROLS
        fileToolbar  = new JToolBar();
        newButton    = (JButton)initButton(NEW_IMAGE_FILE,      fileToolbar,  tracker, idCounter++, JButton.class, null, NEW_TOOLTIP);
        newStateButton    = (JButton)initButton(NEW_STATE_IMAGE_FILE,      fileToolbar,  tracker, idCounter++, JButton.class, null, NEW_STATE_TOOLTIP);
        newPoseButton    = (JButton)initButton(NEW_POSE_IMAGE_FILE,      fileToolbar,  tracker, idCounter++, JButton.class, null, NEW_POSE_TOOLTIP);
        deleteButton    = (JButton)initButton(DELETE_IMAGE_FILE,      fileToolbar,  tracker, idCounter++, JButton.class, null, DELETE_TOOLTIP);
        openButton   = (JButton)initButton(OPEN_IMAGE_FILE,     fileToolbar,  tracker, idCounter++, JButton.class, null, OPEN_TOOLTIP);
        saveButton   = (JButton)initButton(SAVE_IMAGE_FILE,     fileToolbar,  tracker, idCounter++, JButton.class, null, SAVE_TOOLTIP);
        saveAsButton = (JButton)initButton(SAVE_AS_IMAGE_FILE,  fileToolbar,  tracker, idCounter++, JButton.class, null, SAVE_AS_TOOLTIP);
        exportButton = (JButton)initButton(EXPORT_IMAGE_FILE,   fileToolbar,  tracker, idCounter++, JButton.class, null, EXPORT_TOOLTIP);
        exitButton   = (JButton)initButton(EXIT_IMAGE_FILE,     fileToolbar,  tracker, idCounter++, JButton.class, null, EXIT_TOOLTIP);
        
        // EDITING CONTROLS
        editToolbar = new JToolBar();
        selectionButton = (JButton)initButton(SELECTION_IMAGE_FILE, editToolbar, tracker, idCounter++, JButton.class, null, SELECT_TOOLTIP);
        cutButton   = (JButton)initButton(CUT_IMAGE_FILE,    editToolbar, tracker, idCounter++, JButton.class, null, CUT_TOOLTIP);
        copyButton  = (JButton)initButton(COPY_IMAGE_FILE,   editToolbar, tracker, idCounter++, JButton.class, null, COPY_TOOLTIP);
        pasteButton = (JButton)initButton(PASTE_IMAGE_FILE,  editToolbar, tracker, idCounter++, JButton.class, null, PASTE_TOOLTIP);
        moveToFrontButton = (JButton)initButton(MOVE_TO_FRONT_IMAGE_FILE, 
        								editToolbar, tracker, idCounter++, JButton.class, null, MOVE_TO_FRONT_TOOLTIP);
        moveToBackButton  = (JButton)initButton(MOVE_TO_BACK_IMAGE_FILE, 
        								editToolbar, tracker, idCounter++, JButton.class, null, MOVE_TO_BACK_TOOLTIP);
        
        // HERE ARE OUR SHAPE SELECTION CONTROLS
        shapeToolbar = new JToolBar();
        shapeButtonGroup = new ButtonGroup();
        rectToggleButton   = (JToggleButton)initButton( RECT_SELECTION_IMAGE_FILE,      shapeToolbar, tracker, idCounter++, JToggleButton.class, shapeButtonGroup, RECT_TOOLTIP);
        ellipseToggleButton = (JToggleButton)initButton( ELLIPSE_SELECTION_IMAGE_FILE,    shapeToolbar, tracker, idCounter++, JToggleButton.class, shapeButtonGroup, ELLIPSE_TOOLTIP);
        lineToggleButton   = (JToggleButton)initButton( LINE_SELECTION_IMAGE_FILE,      shapeToolbar, tracker, idCounter++, JToggleButton.class, shapeButtonGroup, LINE_TOOLTIP);
        
        // THE LINE THICKNESS SELECTION COMBO BOX WILL GO WITH THE SHAPE CONTROLS
        DefaultComboBoxModel lineThicknessModel = new DefaultComboBoxModel();
        for (int i = 0; i < NUM_STROKES_TO_CHOOSE_FROM; i++)
        {
            String imageFileName =  STROKE_SELECTION_FILE_PREFIX
                                  + (i+1)
                                  + PNG_FILE_EXTENSION;
            Image img = batchLoadImage(imageFileName, tracker, idCounter++);
            ImageIcon ii = new ImageIcon(img);
            lineThicknessModel.addElement(ii);
        }
        lineStrokeSelectionComboBox = new JComboBox(lineThicknessModel);

        // NOW THE ZOOM TOOLBAR
        zoomToolbar = new JToolBar();
        zoomInButton = (JButton)initButton(ZOOM_IN_IMAGE_FILE, zoomToolbar, tracker, idCounter++, JButton.class, null, ZOOM_IN_TOOLTIP);
        zoomOutButton = (JButton)initButton(ZOOM_OUT_IMAGE_FILE, zoomToolbar, tracker, idCounter++, JButton.class, null, ZOOM_OUT_TOOLTIP);
        zoomLabel = new JLabel();
        zoomLabel.setFont(ZOOM_LABEL_FONT);
        dimensionsButton = (JButton)initButton(POSE_DIMENSIONS_IMAGE_FILE, zoomToolbar, tracker, idCounter++, JButton.class, null, CHANGE_POSE_DIMENSIONS_TOOLTIP);
        
        // COLOR SELECTION CONTROLS
        colorSelectionToolbar = new JToolBar();
        colorButtonGroup = new ButtonGroup();
        outlineColorSelectionButton = (ColorToggleButton)initButton(OUTLINE_COLOR_IMAGE_FILE, colorSelectionToolbar, tracker, idCounter++, ColorToggleButton.class, colorButtonGroup, OUTLINE_TOOLTIP);
        outlineColorSelectionButton.setBackground(Color.BLACK);
        fillColorSelectionButton = (ColorToggleButton)initButton(FILL_COLOR_IMAGE_FILE, colorSelectionToolbar, tracker, idCounter++, ColorToggleButton.class, colorButtonGroup, FILL_TOOLTIP);
        fillColorSelectionButton.setBackground(Color.WHITE);
        outlineColorSelectionButton.setSelected(true);
        
        // AND LET'S LOAD THE COLOR PALLET FROM AN XML FILE
        ColorPalletLoader cpl = new ColorPalletLoader();
        ColorPalletState cps = new ColorPalletState();
        cpl.initColorPallet(COLOR_PALLET_SETTINGS_XML, cps);

        // NOW LET'S SETUP THE COLOR PALLET. USING THE
        // STATE WE JUST LOADED. NOW MAKE OUR COLOR PALLET
        // AND MAKE SURE THEY KNOW ABOUT ONE ANOTHER
        colorPallet = new ColorPallet(cps);
        cps.setView(colorPallet);

        // THIS CONTROL WILL LET US CHANGE THE COLORS IN THE COLOR PALLET
        customColorSelectorButton = (JButton)initButton(CUSTOM_COLOR_SELECTOR_IMAGE_FILE, colorSelectionToolbar, tracker, idCounter++, JButton.class, null, CUSTOM_COLOR_TOOLTIP);
        
        // AND THE TRANSPARENCY SLIDER AND LABEL
        alphaLabel = new JLabel(ALPHA_LABEL_TEXT);
        alphaLabel.setFont(ALPHA_LABEL_FONT);
        alphaLabel.setBackground(ALPHA_BACKGROUND_COLOR);
        transparencySlider = new JSlider(JSlider.HORIZONTAL, TRANSPARENT, OPAQUE, OPAQUE);
        transparencySlider.setBackground(ALPHA_BACKGROUND_COLOR);
        transparencySlider.setMajorTickSpacing(ALPHA_MAJOR_TICK_SPACING);
        transparencySlider.setMinorTickSpacing(ALPHA_MINOR_TICK_SPACING);
        transparencySlider.setPaintLabels(true);
        transparencySlider.setPaintTicks(true);
        transparencySlider.setPaintTrack(true);
        transparencySlider.setToolTipText(ALPHA_TOOLTIP);
        transparencySlider.setSnapToTicks(false);
        
        // NOW WE NEED TO WAIT FOR ALL THE IMAGES THE
        // MEDIA TRACKER HAS BEEN GIVEN TO FULLY LOAD
        try
        {
            tracker.waitForAll();
        }
        catch(InterruptedException ie)
        {
            // LOG THE ERROR
            Logger.getLogger(AnimatedSpriteEditorGUI.class.getName()).log(Level.SEVERE, null, ie);           
        }
        
      // ANIMATION BUTTON CONTROLS
        displayToolBar = new JPanel();        
        startButton   = (JButton)initButton(START_IMAGE_FILE, displayToolBar, tracker, idCounter++, JButton.class, null, START_TOOLTIP);
        stopButton   = (JButton)initButton(STOP_IMAGE_FILE, displayToolBar, tracker, idCounter++, JButton.class, null, STOP_TOOLTIP);
        speedUpButton   = (JButton)initButton(SPEED_UP_IMAGE_FILE, displayToolBar, tracker, idCounter++, JButton.class, null, SPEED_UP_TOOLTIP);
        slowDownButton   = (JButton)initButton(SLOW_DOWN_IMAGE_FILE, displayToolBar, tracker, idCounter++, JButton.class, null, SLOW_DOWN_TOOLTIP);
      
      // THE ANIMATION STATE SELECTION PANEL
        statePanel = new JPanel(new FlowLayout());
        DefaultComboBoxModel stateComboBoxModel = new DefaultComboBoxModel();
        stateComboBox = new JComboBox(stateComboBoxModel);
        stateComboBoxModel.addElement("The Animation States Displayed Here.");
        stateComboBox.setPrototypeDisplayValue("The Animation States Displayed Here.");
        stateComboBoxMessage = new JTextField("    Select Animation State:     ");
        stateComboBoxMessage.disable();
        stateComboBoxMessage.setDisabledTextColor(Color.black);
        stateComboBoxMessage.setBackground(Color.white);
        statePanel.add(stateComboBoxMessage);
        statePanel.add(stateComboBox);
        statePanel.setBackground(Color.white);
        statePanel.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
      // THE POSES LIST
        poseList = new JPanel();
       
        poseList.setLayout(new FlowLayout());
        poseList.setBackground(Color.white);
        randomButton   = (JButton)initButton(POSE_MESSAGE_IMAGE_FILE, poseList, tracker, idCounter++, JButton.class, null, null);
        randomButton2   = (JButton)initButton(POSE_MESSAGE_IMAGE_FILE, poseList, tracker, idCounter++, JButton.class, null, null);
        randomButton3   = (JButton)initButton(POSE_MESSAGE_IMAGE_FILE, poseList, tracker, idCounter++, JButton.class, null, null);
        randomButton4   = (JButton)initButton(POSE_MESSAGE_IMAGE_FILE, poseList, tracker, idCounter++, JButton.class, null, null);
        randomButton5   = (JButton)initButton(POSE_MESSAGE_IMAGE_FILE, poseList, tracker, idCounter++, JButton.class, null, null);
        scrollPane = new JScrollPane(poseList);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
    }
        
    /**
     * This helper method locates all the components inside the frame. Note
     * that it does not put most buttons into their proper toolbars because 
     * that was already done for most when they were initialized by initButton.
     */
    private void layoutGUIControls()
    {
        // LET'S PUT THE TWO CANVASES INSIDE 
        // THE SPLIT PANE. WE'LL PUT THE DIVIDER
        // RIGHT IN THE MIDDLE AND WON'T LET
        // THE USER MOVE IT - FOOLPROOF DESIGN!
    	displayArea.add(displayToolBar, BorderLayout.SOUTH);
        canvasSplitPane.setLeftComponent(zoomableCanvas);
        canvasSplitPane.setRightComponent(displayArea);
        canvasSplitPane.setResizeWeight(0.85);
        canvasSplitPane.setEnabled(false);
        
        // PUT THE COMBO BOX IN THE SHAPE TOOLBAR
        shapeToolbar.add(lineStrokeSelectionComboBox);
        
        // ARRANGE THE COLOR SELECTION TOOLBAR
        colorSelectionToolbar.add(colorPallet);        
        colorSelectionToolbar.add(customColorSelectorButton);
        colorSelectionToolbar.add(alphaLabel);
        colorSelectionToolbar.add(transparencySlider);
  
        // NOW ARRANGE THE TOOLBARS
        northOfNorthPanel.add(fileToolbar);
        northOfNorthPanel.add(editToolbar);
        northOfNorthPanel.add(statePanel);
        southOfNorthPanel.add(zoomToolbar);
        southOfNorthPanel.add(colorSelectionToolbar);
        southOfNorthPanel.add(shapeToolbar);
        
        // NOW PUT ALL THE CONTROLS IN THE NORTH
        northPanel.setLayout(new BorderLayout());
        northPanel.add(northOfNorthPanel, BorderLayout.NORTH);
        northPanel.add(southOfNorthPanel, BorderLayout.CENTER);        
        northPanel.add(scrollPane, BorderLayout.SOUTH);

        // NOW PUT ALL THE THINGS IN THE TOP PANEL
      
        // AND NOW PUT EVERYTHING INSIDE THE FRAME
        add(northPanel, BorderLayout.NORTH);
        add(canvasSplitPane, BorderLayout.CENTER);
  
    }
    
    /**
     * GUI setup method can be quite lengthy and repetitive so
     * it helps to create helper methods that can do a bunch of
     * things at once. This method creates a button with a bunch
     * of premade values. Note that we are using Java reflection
     * here, to make an object based on what class type it has.
     * 
     * @param imageFile The image to use for the button.
     * 
     * @param parent The container inside which to put the button.
     * 
     * @param tracker This makes sure our button fully loads.
     * 
     * @param id A unique id for the button so the tracker knows it's there.
     * 
     * @param buttonType The type of button, we'll use reflection for making it.
     * 
     * @param bg Some buttons will go into groups where only one may be selected
     * at a time.
     * 
     * @param tooltip The mouse-over text for the button.
     * 
     * @return A fully constructed and initialized button with all the data
     * provided to it as arguments.
     */
    
    private AbstractButton initButton(   String imageFile, 
                                        Container parent, 
                                        MediaTracker tracker, 
                                        int id, 
                                        Class buttonType,
                                        ButtonGroup bg,
                                        String tooltip)
    {
        try 
        {
            // LOAD THE IMAGE AND MAKE AN ICON
            Image img = batchLoadImage(imageFile, tracker, id);
            ImageIcon ii = new ImageIcon(img);
            
            // HERE'S REFLECTION MAKING OUR OBJECT USING IT'S CLASS
            // NOTE THAT DOING IT LIKE THIS CALLS THE buttonType
            // CLASS' DEFAULT CONSTRUCTOR, SO WE MUST MAKE SURE IT HAS ONE
            AbstractButton createdButton;
            createdButton = (AbstractButton)buttonType.newInstance();
            
            // NOW SETUP OUR BUTTON FOR USE
            createdButton.setIcon(ii);
            createdButton.setToolTipText(tooltip);
            parent.add(createdButton);
            
            // INSETS ARE SPACING INSIDE THE BUTTON,
            // TOP LEFT RIGHT BOTTOM
            Insets buttonMargin = new Insets(   
                    BUTTON_INSET, BUTTON_INSET, BUTTON_INSET, BUTTON_INSET);
            createdButton.setMargin(buttonMargin);
            
            // ADD IT TO ITS BUTTON GROUP IF IT'S IN ONE
            if (bg != null)
            {
                bg.add(createdButton);
            }
            
            // AND RETURN THE SETUP BUTTON
            return createdButton;
        } 
        catch (InstantiationException | IllegalAccessException ex) 
        {
            // WE SHOULD NEVER GET THIS ERROR, BUT WE HAVE TO PUT
            // A TRY CATCH BECAUSE WE'RE USING REFLECTION TO DYNAMICALLY
            // CONSTRUCT OUR BUTTONS BY CLASS NAME
            Logger.getLogger(AnimatedSpriteEditorGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        // THIS WOULD MEAN A FAILURE OF SOME SORT OCCURED
        return null;
    }

    /**
     * This method helps us load a bunch of images and ensure they are 
     * fully loaded when we want to use them.
     * 
     * @param imageFile The path and name of the image file to load.
     * 
     * @param tracker This will help ensure all the images are loaded.
     * 
     * @param id A unique identifier for each image in the tracker. It
     * will only wait for ids it knows about.
     * 
     * @return A constructed image that has been registered with the tracker.
     * Note that the image's data has not necessarily been fully loaded when 
     * this method ends.
     */
    private Image batchLoadImage(String imageFile, MediaTracker tracker, int id)
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image img = tk.getImage(imageFile);
        tracker.addImage(img, id);        
        return img;
    }

    /**
     * This method constructs and registers all the event handlers
     * for all the GUI controls.
     */
    private void initHandlers()
    {
        // THIS WILL HANDLE THE SCENARIO WHEN THE USER CLICKS ON
        // THE X BUTTON, WE'LL WANT A CUSTOM RESPONSE
        PoseurWindowHandler pwh = new PoseurWindowHandler();
        this.addWindowListener(pwh);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // FILE TOOLBAR HANDLER
        NewHandler nh = new NewHandler();
        newButton.addActionListener(nh);
        NewPoseHandler nph = new NewPoseHandler();
        newPoseButton.addActionListener(nph);
        NewStateHandler nsh = new NewStateHandler();
        newStateButton.addActionListener(nsh);
        DeleteHandler dh = new DeleteHandler();
        deleteButton.addActionListener(dh);
        OpenHandler oph = new OpenHandler();
        openButton.addActionListener(oph);
        SaveHandler sph = new SaveHandler();
        saveButton.addActionListener(sph);
        SaveAsHandler sah = new SaveAsHandler();
        saveAsButton.addActionListener(sah);
        ExportPoseHandler eph = new ExportPoseHandler();
        exportButton.addActionListener(eph);
        ExitHandler eh = new ExitHandler();
        exitButton.addActionListener(eh);
        
        // EDIT TOOLBAR HANDLER
        StartSelectionHandler startSH = new StartSelectionHandler();
        selectionButton.addActionListener(startSH);
        CutHandler cutEh = new CutHandler();
        cutButton.addActionListener(cutEh);
        CopyHandler copyEh = new CopyHandler();
        copyButton.addActionListener(copyEh);
        PasteHandler pasteEh = new PasteHandler();
        pasteButton.addActionListener(pasteEh);
        MoveToFrontHandler mtfEh = new MoveToFrontHandler();
        moveToFrontButton.addActionListener(mtfEh);
        MoveToBackHandler mtbEh = new MoveToBackHandler();
        moveToBackButton.addActionListener(mtbEh);
        
        // SHAPE SELECTION HANDLERS
        RectangleSelectionHandler rsh = new RectangleSelectionHandler();
        rectToggleButton.addActionListener(rsh);
        EllipseSelectionHandler ssh = new EllipseSelectionHandler();
        ellipseToggleButton.addActionListener(ssh);
        LineSelectionHandler lsh = new LineSelectionHandler();
        lineToggleButton.addActionListener(lsh);
                
        // ZOOM HANDLERS
        ZoomInHandler zih = new ZoomInHandler();
        zoomInButton.addActionListener(zih);
        ZoomOutHandler zoh = new ZoomOutHandler();
        zoomOutButton.addActionListener(zoh);
        ChangePoseDimensionsHandler cpdh = new ChangePoseDimensionsHandler();
        dimensionsButton.addActionListener(cpdh);
      
        
        // COLOR CONTROL HANDLERS
        OutlineColorHandler acal = new OutlineColorHandler();
        outlineColorSelectionButton.addActionListener(acal);
        FillColorHandler fcal = new FillColorHandler();
        fillColorSelectionButton.addActionListener(fcal);
        ColorPalletHandler cph = new ColorPalletHandler();
        colorPallet.registerColorPalletHandler(cph);
        TransparencyHandler tph = new TransparencyHandler();
        transparencySlider.addMouseMotionListener(tph);
        CustomColorHandler cch = new CustomColorHandler();
        customColorSelectorButton.addActionListener(cch);
        LineThicknessHandler lth = new LineThicknessHandler();
        lineStrokeSelectionComboBox.addActionListener(lth);
    
        // DISPLAY TOOLBAR HANDLERS
        StartHandler sh = new StartHandler();
        startButton.addActionListener(sh);
        StopHandler st = new StopHandler();
        stopButton.addActionListener(st);
        SpeedUpHandler suh = new SpeedUpHandler();
        speedUpButton.addActionListener(suh);
        SlowDownHandler sdh = new SlowDownHandler();
        slowDownButton.addActionListener(sdh);
        
        // ANIMATION STATE COMBOBOX HANDLER
        StateSelectionHandler statesh = new StateSelectionHandler();
        stateComboBox.addActionListener(statesh);
        
        // CANVAS MOUSE HANDLERS
        PoseCanvasMouseHandler rsmh = new PoseCanvasMouseHandler();
        zoomableCanvas.addMouseListener(rsmh);
        zoomableCanvas.addMouseMotionListener(rsmh);
        
        // THIS HANDLER IS CALLED WHEN THE COMPONENT IS 
        // FIRST SIZED TO BE DISPLAYED. WE WISH TO CALCULATE
        // THE POSE AREA AT THAT TIME, SO WE'LL DO IT FOR
        // BOTH CANVASES
        PoseCanvasComponentHandler pcch = new PoseCanvasComponentHandler();
        zoomableCanvas.addComponentListener(pcch);
        
        // POSE BUTTONS HANDLERS
        PoseSelectionHandler psh = new PoseSelectionHandler();
        randomButton.addActionListener(psh);
        randomButton2.addActionListener(psh);
        randomButton3.addActionListener(psh);
        randomButton4.addActionListener(psh);
        randomButton5.addActionListener(psh);
        
    }
       
    // METHODS FOR ENABLING AND DISABLING GROUPS OF CONTROLS.
    // THESE METHODS ALL SUPPOR THE updateMode METHOD. I'LL
    // SPARE YOU DESCRIPTIONS OF EACH ONE
    
    private void enableStartupFileControls()
    {
        // THESE BUTTONS ARE ALWAYS ENABLED
        newButton.setEnabled(true);
        newPoseButton.setEnabled(true);
        newStateButton.setEnabled(true);
        openButton.setEnabled(true);
        exitButton.setEnabled(true);
        
        // THESE BUTTONS START OFF AS DISABLED
        saveButton.setEnabled(false);
        exportButton.setEnabled(false);
    }
    
    /**
     * Enable the save as and export image buttons.
     */
    private void enableSaveAsAndExport()
    {
        // THESE ARE ENABLED AS SOON AS WE START EDITING
        exportButton.setEnabled(true);
    }    
    
    /**
     * Enable/Dis enable the edit tool bar.
     * @param isEnabled  whether if the tool bar is enabled
     */
    private void setEnabledEditControls(boolean isEnabled)
    {
        // THE SELECTION BUTTON NEEDS TO BE CHECKED SEPARATELY

        // THESE ARE EASY, JUST DO AS THEY'RE TOLD
    	moveToFrontButton.setEnabled(isEnabled);
    	moveToBackButton.setEnabled(isEnabled);
        pasteButton.setEnabled(isEnabled);

    }
    
    /**
     * Enable/Dis enable the color controls.
     * @param isEnabled  whether if the color controlls are enabled
     */
    private void setEnabledColorControls(boolean isEnabled)
    {
        outlineColorSelectionButton.setEnabled(isEnabled);
        fillColorSelectionButton.setEnabled(isEnabled);
        customColorSelectorButton.setEnabled(isEnabled);
        colorPallet.setEnabled(isEnabled);
        outlineColorSelectionButton.setSelected(isEnabled);
        alphaLabel.setEnabled(isEnabled);
        transparencySlider.setEnabled(isEnabled);
    }

    /**
     * Enable/Dis enable the zoom controls.
     * @param isEnabled  whether if the zoom controls are enabled
     */
    private void setEnabledZoomControls(boolean isEnabled)
    {
        zoomInButton.setEnabled(isEnabled);
        zoomOutButton.setEnabled(isEnabled);
        zoomLabel.setEnabled(isEnabled);
        dimensionsButton.setEnabled(isEnabled);
    }
    
    /**
     * Enable/Dis enable the shape controls
     * @param isEnabled  whether if the shape controls are enabled
     */
    private void setEnabledShapeControls(boolean isEnabled)
    {
        // INIT THEM AS USABLE OR NOT
        rectToggleButton.setEnabled(isEnabled);
        ellipseToggleButton.setEnabled(isEnabled);
        lineToggleButton.setEnabled(isEnabled);
        lineStrokeSelectionComboBox.setEnabled(isEnabled);
        
        // IF THEY'RE USABLE, MAKE THE TOGGLES UNSELECTED
        if (isEnabled)
        {
            shapeButtonGroup.clearSelection();
        }
    }
    
    /**
     * Select a cursor
     * @param cursorToUse	the cursor to use
     */
    private void selectCursor(int cursorToUse)
    {
        // AND NOW SWITCH TO A CROSSHAIRS CURSOR
        Cursor arrowCursor = Cursor.getPredefinedCursor(cursorToUse);
        setCursor(arrowCursor);    
    }
}
