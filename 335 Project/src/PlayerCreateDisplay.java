/* The Display for Player Creation.
 * Prompts the client to enter their name, pick their tank, and choose their color.
 * This is accomplished using SWT widgets and Arraylists. After submitting the information, 
 * a new Player object is created and the server is told of the successful player creation.
 * 
 * AUTHOR: Jonathan Houge
 */

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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionAdapter;

public class PlayerCreateDisplay {

	// start returns the player made by the client
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
		Group colors = new Group(shell, SWT.NONE);
		colors.setLayout(widgetLayout);
		Label label = new Label(colors, SWT.NONE);
		label.setText("Let's Get Your Name"); label.setFont(labelFont); label.setForeground(color);
		Text name = new Text(colors, SWT.BORDER); name.setLayoutData(widgetData);
		name.setText("Enter name here!"); name.setTextLimit(16);
		
		// pick preferred color; default: White
		ArrayList<String> decision2 = new ArrayList<String>(); decision2.add("White");
		colorButtons(shell, decision2, widgetLayout, labelFont, buttonFont, color);

		// pick opponent to face; default: Human
		ArrayList<String> decision3 = new ArrayList<String>(); decision3.add("Human");
		typeButtons(shell, decision3, widgetLayout, labelFont, buttonFont, color);
		
		// play button - will use the current selections to create a new Player
		ArrayList<String> decision = new ArrayList<String>();
		Button start = new Button(shell, SWT.PUSH); start.setText("Let's play!");
		selectListenCreation(start, decision);
		
		shell.pack(); shell.open();
		while (decision.size() == 0) { // while the play button hasn't been clicked
			if (!display.readAndDispatch ())
				display.sleep (); }
		
		// gathers the data the client picked to create their player
		String playerName = name.getText(); display.dispose();
		String preferredColor = decision2.get(decision2.size() - 1);
		String opponent = decision3.get(decision3.size() - 1);

		Player player = new Player(playerName, preferredColor, opponent);
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
		selectListenCreation(white, decision);
				
		Button black = new Button(colors, SWT.RADIO); black.setText("Black");
		black.setFont(button); black.setForeground(color);
		selectListenCreation(black, decision); }
		
	// radio button for picking the type of game
	private static void typeButtons(Shell shell, ArrayList<String> decision, GridLayout layout, Font title, Font button, Color color) {
		//-- set up general button layout
				Group colors = new Group(shell, SWT.NONE);
				colors.setLayout(layout);
				Label label = new Label(colors, SWT.NONE);
				label.setText("Who are you facing?"); label.setFont(title); label.setForeground(color);

				//-- buttons themselves - default select the first option
				Button human = new Button(colors, SWT.RADIO); human.setText("Human");
				human.setSelection(true); human.setFont(button); human.setForeground(color);
				selectListenCreation(human, decision);
						
				Button random = new Button(colors, SWT.RADIO); random.setText("Robot (Easy)");
				random.setFont(button); random.setForeground(color);
				selectListenCreation(random, decision);
				
				Button algorithm = new Button(colors, SWT.RADIO); algorithm.setText("Robot (Hard)");
				algorithm.setFont(button); algorithm.setForeground(color);
				selectListenCreation(algorithm, decision); }
	
	// selection listener - for the radio buttons & submit button
	protected static void selectListenCreation(Button button, ArrayList<String> decision) {
		button.addSelectionListener(new SelectionAdapter()  {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button source =  (Button) e.getSource();
				decision.add(source.getText()); } }); }
}