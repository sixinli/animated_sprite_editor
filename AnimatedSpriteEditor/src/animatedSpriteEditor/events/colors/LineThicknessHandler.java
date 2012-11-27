package animatedSpriteEditor.events.colors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * This class will handler events interact with the line thickness 
 * combo box.
 * @author sixin
 *
 */
public class LineThicknessHandler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null,  
				"The Line Thickness Combo Box is clicked.", 
				"To the User: ",
				JOptionPane.OK_OPTION);
	}

}
