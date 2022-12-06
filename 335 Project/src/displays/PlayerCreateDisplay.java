/** The Display for Player Creation.
 * Prompts the client to enter their name, pick their color, pick their opponent, and enter
 * the optional fields of savedGame.txt and/or the time constraint they wish to place upon
 * themselves (WARNING: this only works with 'Robot' and 'Remote' opponent modes).
 * This is accomplished using SWT widgets and Arraylists. After submitting the information, 
 * a new Player object is created and the client is given the Player object.
 * 
 * @author Jonathan Houge
 */

package displays;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class PlayerCreateDisplay {

	/** The start method creates and runs the display itself. It utilizes 
	 * functions to create the different kinds of radio buttons and creates
	 * textual widgets right then and there. The display stays open until
	 * the user clicks the "Let's Play!" button. In then collects the 
	 * entered information, creates the Player object, and returns it.
	 * 
	 * @return Player: a Player object created from the user's inputted settings
	 */
	public Player start() {
		
		//-- create and set up the display & create variables for the display's widgets 
		Display display = new Display();
		Shell shell = new Shell (display);
		shell.setSize(100, 100);
		shell.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		shell.setLayout(gridLayout);

		GridLayout widgetLayout = new GridLayout();
		GridData widgetData = new GridData();
		Font labelFont = new Font(display, "Courier", 12, SWT.NONE);
		Font buttonFont = new Font(display, "Courier", 8, SWT.NONE);
		Color color = display.getSystemColor(SWT.COLOR_WHITE);
		
		// title text to let the client know they're the host - they need to set it up!
		Text title = new Text(shell, SWT.READ_ONLY);
		title.setText("Welcome to Chess!");
		title.setFont(new Font(display, "Courier", 18, SWT.NONE));
		title.setForeground(display.getSystemColor(SWT.COLOR_YELLOW));
		
		//-- the widgets
		
		// enter your name; default will pick a random emoticon from the Player class
		Group naming = new Group(shell, SWT.NONE);
		naming.setLayout(widgetLayout);
		Label nameL = new Label(naming, SWT.NONE);
		nameL.setText("Let's Get Your Name"); nameL.setFont(labelFont); nameL.setForeground(color);
		Text name = new Text(naming, SWT.BORDER); name.setLayoutData(widgetData);
		name.setText("Enter name here!"); name.setTextLimit(16);
		
		// pick preferred color; default: White
		ArrayList<String> decision2 = new ArrayList<String>(); decision2.add("White");
		colorButtons(shell, decision2, widgetLayout, labelFont, buttonFont, color);

		// pick opponent to face; default: Remote
		ArrayList<String> decision3 = new ArrayList<String>(); decision3.add("Remote");
		typeButtons(shell, decision3, widgetLayout, labelFont, buttonFont, color);
		
		// if the user wishes to resume a game;
		Group load = new Group(shell, SWT.NONE);
		load.setLayout(widgetLayout);
		Label loadL = new Label(load, SWT.NONE);
		loadL.setText("Resume a Saved Game"); loadL.setFont(labelFont); loadL.setForeground(color);
		Text file = new Text(load, SWT.BORDER); file.setLayoutData(widgetData);
		file.setText("Enter .txt file"); file.setTextLimit(16);
		
		
		// if the user prefers a timed mode;
		Group timedMode = new Group(shell, SWT.NONE);
		timedMode.setLayout(widgetLayout);
		Label timeL = new Label(timedMode, SWT.NONE);
		timeL.setText("Play in timed mode?"); timeL.setFont(labelFont); timeL.setForeground(color);
		Text time = new Text(timedMode, SWT.BORDER); time.setLayoutData(widgetData);
		time.setText("MM:SS"); time.setTextLimit(6);
				
		// play button - will use the current selections to create a new Player
		ArrayList<String> decision = new ArrayList<String>();
		Button start = new Button(shell, SWT.PUSH); start.setText("Let's play!");
		selectListener.selectListenCreation(start, decision);
		
		shell.pack(); shell.open();
		while (decision.size() == 0) { // while the play button hasn't been clicked
			if (!display.readAndDispatch ())
				display.sleep (); }
		
		// gathers the data the client picked to create their player
		String playerName = name.getText();
		String preferredTime = time.getText();
		String fileName = file.getText(); display.dispose();
		String preferredColor = decision2.get(decision2.size() - 1);
		String opponent = decision3.get(decision3.size() - 1);

		Player player = new Player(playerName, preferredColor, opponent, fileName, preferredTime);
		return player; } // player successfully created!

	// radio button for picking piece color
	private static void colorButtons(Shell shell, ArrayList<String> decision, GridLayout layout, Font title, Font button, Color color) {
		//-- set up general button layout
		Group colors = new Group(shell, SWT.NONE);
		colors.setLayout(layout);
		Label label = new Label(colors, SWT.NONE);
		label.setText("Which Color Pieces?"); label.setFont(title); label.setForeground(color);

		//-- buttons themselves - default select the first option
		Button white = new Button(colors, SWT.RADIO); white.setText("White");
		white.setSelection(true); white.setFont(button); white.setForeground(color);
		selectListener.selectListenCreation(white, decision);
				
		Button black = new Button(colors, SWT.RADIO); black.setText("Black");
		black.setFont(button); black.setForeground(color);
		selectListener.selectListenCreation(black, decision); }
		
	// radio button for picking the type of game
	private static void typeButtons(Shell shell, ArrayList<String> decision, GridLayout layout, Font title, Font button, Color color) {
		//-- set up general button layout
		Group type = new Group(shell, SWT.NONE);
		type.setLayout(layout);
		Label label = new Label(type, SWT.NONE);
		label.setText("Who are you facing?"); label.setFont(title); label.setForeground(color);

		//-- buttons themselves - default select the second option
		Button locally = new Button(type, SWT.RADIO); locally.setText("Local");
		locally.setFont(button); locally.setForeground(color);
		selectListener.selectListenCreation(locally, decision);
						
		Button remotely = new Button(type, SWT.RADIO); remotely.setText("Remote");
		remotely.setSelection(true); remotely.setFont(button); remotely.setForeground(color);
		selectListener.selectListenCreation(remotely, decision);

		Button bot = new Button(type, SWT.RADIO); bot.setText("Robot");
		bot.setFont(button); bot.setForeground(color);
		selectListener.selectListenCreation(bot, decision); }
}