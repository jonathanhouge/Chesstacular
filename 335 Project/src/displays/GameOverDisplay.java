/* The Display for game over.
 * Prompts the client to decide whether or not they want to play another game.
 * 
 * AUTHOR: Jonathan Houge
 */

package displays;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;

public class GameOverDisplay {

	// start returns the player made by the client
	public boolean start(Display display, int winner) {
		
		//-- create and set up the display & create variables for the display's widgets 
		Shell shell = new Shell (display);
		shell.setSize(100, 100);
		shell.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		shell.setLayout(gridLayout);
		
		// title text, announcing the winner
		Text title = new Text(shell, SWT.READ_ONLY);
		if (winner == 1) { title.setText("Game Over! White won."); }
		else { title.setText("Game Over! Black won. "); }
		title.setFont(new Font(display, "Courier", 18, SWT.NONE));
		title.setForeground(display.getSystemColor(SWT.COLOR_YELLOW));
		
		//-- the widgets
		
		// play again button - starts up the ui again, a new game occurs
		ArrayList<String> playGame = new ArrayList<String>();
		Button again = new Button(shell, SWT.PUSH); again.setText("Play again!");
		PlayerCreateDisplay.selectListenCreation(again, playGame);
		
		// cancel button - ends the client
		ArrayList<String> cancelGame = new ArrayList<String>();
		Button cancel = new Button(shell, SWT.PUSH); cancel.setText("Cancel");
		PlayerCreateDisplay.selectListenCreation(cancel, cancelGame);
		
		shell.pack(); shell.open();
		while (playGame.size() == 0 && cancelGame.size() == 0) { // while an option hasn't been picked
			if (!display.readAndDispatch ())
				display.sleep (); }
		
		boolean playAgain = false;
		if (playGame.size() != 0) {
			playAgain = true;
		}
		
		shell.dispose();
		
		return playAgain; } // returning decision (play again or not

}