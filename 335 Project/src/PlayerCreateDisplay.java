/* The Display for Player Creation.
 * Prompts the client to enter their name, pick their tank, and choose their color.
 * This is accomplished using SWT widgets and Arraylists. After submitting the information, 
 * a new Player object is created and the server is told of the successful player creation.
 * 
 * Uses XTankHostDisplay's selectListenCreation to save code.
 * 
 * AUTHOR: Jonathan Houge
 */

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class PlayerCreateDisplay {

	// start - takes in id for player creation, returns the player made
	public Player start(int id) {
		
		//-- create and set up the display & create variables for the display's widgets 
		Display display = new Display();
		Shell shell = new Shell (display);
		shell.setSize(100, 100);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		shell.setLayout(gridLayout);
			
		GridData widgetData = new GridData();
		
		//-- the widgets
		
		// enter your name; default will pick a random name from the Player class
		Label label = new Label(shell, SWT.NONE);
		label.setText("Name:");
		Text name = new Text(shell, SWT.BORDER); name.setLayoutData(widgetData);
		name.setText("Default"); name.setTextLimit(10);
		
		// pick tank to use; default: Defaulty
		ArrayList<String> decision2 = new ArrayList<String>(); decision2.add("Defaulty");
		tankButtons(shell, decision2);

		// pick rules to use; default: standard rules
		ArrayList<String> decision3 = new ArrayList<String>(); decision3.add("Green");
		colorButtons(shell, decision3);
		
		// join button - will use the current selections to create a new Player
		ArrayList<String> decision = new ArrayList<String>();
		Button start = new Button(shell, SWT.PUSH); start.setText("Join Game!");
		selectListenCreation(start, decision);
		
		shell.pack(); shell.open();
		while (decision.size() == 0) { // while the join button hasn't been clicked
			if (!display.readAndDispatch ())
				display.sleep (); }
		
		// gathers the data the client picked to create their player
		String playerName = name.getText(); display.dispose();
		String tank = decision2.get(decision2.size() - 1);
		String color = decision3.get(decision3.size() - 1);

		Player player = new Player(playerName, id);
		return player; } // player successfully created!

	// radio button for tank picking
	private static void tankButtons(Shell shell, ArrayList<String> decision) {
		// set up general button layout
		Group tank = new Group(shell, SWT.NONE);
		tank.setLayout(new GridLayout());
		Label label = new Label(tank, SWT.NONE);
		label.setText("Tank: ");

		//-- buttons themselves - default select the first option
		Button tank1 = new Button(tank, SWT.RADIO); tank1.setText("Defaulty"); // in-between
		tank1.setSelection(true);
		selectListenCreation(tank1, decision);

		Button tank2 = new Button(tank, SWT.RADIO); tank2.setText("Quicky"); // fast, less health
		selectListenCreation(tank2, decision);

		Button tank3 = new Button(tank, SWT.RADIO); tank3.setText("Biggy"); // slower, more health
		selectListenCreation(tank3, decision); }
		
	// radio button for color picking
	private static void colorButtons(Shell shell, ArrayList<String> decision) {
		// set up general button layout
		Group color = new Group(shell, SWT.NONE);
		color.setLayout(new GridLayout());
		Label label = new Label(color, SWT.NONE);
		label.setText("Color: ");

		//-- buttons themselves - default select the first option
		Button green = new Button(color, SWT.RADIO); green.setText("Green");
		green.setSelection(true);
		selectListenCreation(green, decision);
			
		Button blue = new Button(color, SWT.RADIO); blue.setText("Blue");
		selectListenCreation(blue, decision);
			
		Button orange = new Button(color, SWT.RADIO); orange.setText("Gray");
		selectListenCreation(orange, decision);
			
		Button red = new Button(color, SWT.RADIO); red.setText("Red");
		selectListenCreation(red, decision);
		
		Button yellow = new Button(color, SWT.RADIO); yellow.setText("Yellow");
		selectListenCreation(yellow, decision); }
	
	// selection listener - for the radio buttons & submit button
	protected static void selectListenCreation(Button button, ArrayList<String> decision) {
		button.addSelectionListener(new SelectionAdapter()  {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button source =  (Button) e.getSource();
				decision.add(source.getText()); } }); }
}