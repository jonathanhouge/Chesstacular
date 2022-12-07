package displays;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.GridLayout;

/** 
 * The display for game over.
 * When either white or black has been put in checkmate, the game event loop stops
 * and this display is run. It announces which of the two colors won the game and 
 * prompts the client to decide whether or not they want to play another game.
 * This is called from UI's event loop. located within its start method.
 * 
 * Since every display utilizes selection listeners, a separate class with the method 
 * 'selectListenCreation()' is used to add a selection listener.
 * 
 * @author Jonathan Houge
 */
public class GameOverDisplay {

	/** 
	 * The start method creates and runs the display (shell) itself. It utilizes 
	 * functions to create the different kinds of radio buttons and creates
	 * textual widgets right then and there. The display stays open until
	 * the user decides to play a new game or not. In then collects that decision
	 * and returns the boolean result.
	 * 
	 * @param display: Display object, the Chessboard's Display
	 * @param winner: int representing the winner. 1 is white, 2 is black.
	 * @return boolean: true if the user wants to play another game, false if otherwise
	 */
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
		title.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		
		//-- the widgets
		
		// play again button - if selected, true is returned
		ArrayList<String> playGame = new ArrayList<String>();
		Button again = new Button(shell, SWT.PUSH); again.setText("Play again!");
		selectListener.selectListenCreation(again, playGame);
		
		// cancel button - if selected, false is returned
		ArrayList<String> cancelGame = new ArrayList<String>();
		Button cancel = new Button(shell, SWT.PUSH); cancel.setText("Cancel");
		selectListener.selectListenCreation(cancel, cancelGame);
		
		shell.pack(); shell.open();
		while (playGame.size() == 0 && cancelGame.size() == 0) { // while an option hasn't been picked
			if (!display.readAndDispatch ())
				display.sleep (); }
		
		boolean playAgain = false; // we assume the client won't want to play again
		if (playGame.size() != 0) { // but if playGame's size isn't zero, they selected play again!
			playAgain = true;
		}
		
		shell.dispose();
		
		return playAgain; } // returning decision (play again or not)

}