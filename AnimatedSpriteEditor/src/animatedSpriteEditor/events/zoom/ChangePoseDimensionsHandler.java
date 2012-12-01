package animatedSpriteEditor.events.zoom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import animatedSpriteEditor.gui.PoseDimensionsDialog;

/**
 * This class will handler events interact with the resize
 * button.
 * @author sixin
 *
 */
public class ChangePoseDimensionsHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
        PoseDimensionsDialog dialog = new PoseDimensionsDialog();
        dialog.initLocation();
        dialog.setVisible(true);
	}

}
