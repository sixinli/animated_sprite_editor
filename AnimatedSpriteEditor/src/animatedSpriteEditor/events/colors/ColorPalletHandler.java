package animatedSpriteEditor.events.colors;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import animatedSpriteEditor.AnimatedSpriteEditor;
import animatedSpriteEditor.state.PoseurStateManager;

/**
 * This class will handler events interact with the color pallet.
 * @author sixin
 *
 */
public class ColorPalletHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
        // WE CAN GET THE COLOR FROM THE PRESSED BUTTON'S BACKGROUND
        JButton source = (JButton)ae.getSource();
        Color selectedColor = source.getBackground();
        
        // AND SEND IT OFF TO THE STATE MANAGER TO UPDATE THE APP
        AnimatedSpriteEditor singleton = AnimatedSpriteEditor.getEditor();
        PoseurStateManager poseurStateManager = singleton.getStateManager().getPoseurStateManager();
        poseurStateManager.selectPalletColor(selectedColor);
	}
}
