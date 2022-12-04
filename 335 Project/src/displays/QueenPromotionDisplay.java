package displays;

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
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionAdapter;

public class QueenPromotionDisplay {

	// start returns the player made by the client
	public String start(Display display) {
		
		//-- create and set up the display & create variables for the display's widgets 
		Shell shell = new Shell (display);
		shell.setSize(100, 100);
		shell.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		shell.setLayout(gridLayout);

		RowLayout widgetLayout = new RowLayout();
		Font labelFont = new Font(display, "Courier", 12, SWT.NONE);
		Font buttonFont = new Font(display, "Courier", 8, SWT.NONE);
		Color color = display.getSystemColor(SWT.COLOR_WHITE);
		
		// title text to let the client know they're the host - they need to set it up!
		Text title = new Text(shell, SWT.READ_ONLY);
		title.setText("Choose Pawn Promotion");
		title.setFont(new Font(display, "Courier", 18, SWT.NONE));
		title.setForeground(display.getSystemColor(SWT.COLOR_YELLOW));
		
		//-- the widgets
		
		// pick what piece pawn should be promoted to; default: Queen
		ArrayList<String> decision = new ArrayList<String>(); decision.add("Queen");
		colorButtons(shell, decision, widgetLayout, labelFont, buttonFont, color);
		
		// play button - will use the current selections to create a new Player
		ArrayList<String> submit = new ArrayList<String>();
		Button start = new Button(shell, SWT.PUSH); start.setText("Promote!");
		selectListenCreation(start, submit);
		
		shell.pack(); shell.open();
		while (submit.size() == 0) { // while the promotion button hasn't been clicked
			if (!display.readAndDispatch ())
				display.sleep (); }
		
		// gathers the piece the client picked
		String piece = decision.get(decision.size() - 1);
		shell.dispose();
		
		return piece; } // returning wanted piece

	// radio button for picking piece color
	private static void colorButtons(Shell shell, ArrayList<String> decision, RowLayout layout, Font title, Font button, Color color) {
		//-- set up general button layout
		Group colors = new Group(shell, SWT.NONE);
		colors.setLayout(layout);

		//-- buttons themselves - default select the first option
		Button rook = new Button(colors, SWT.RADIO); rook.setText("Rook");
		rook.setFont(button); rook.setForeground(color);
		selectListenCreation(rook, decision);
				
		Button knight = new Button(colors, SWT.RADIO); knight.setText("Knight");
		knight.setFont(button); knight.setForeground(color);
		selectListenCreation(knight, decision);
		
		Button bishop = new Button(colors, SWT.RADIO); bishop.setText("Bishop");
		bishop.setFont(button); bishop.setForeground(color);
		selectListenCreation(bishop, decision);
		
		Button queen = new Button(colors, SWT.RADIO); queen.setText("Queen");
		queen.setSelection(true); queen.setFont(button); queen.setForeground(color);
		selectListenCreation(queen, decision);
	}
	
	// selection listener - for the radio buttons & submit button
	protected static void selectListenCreation(Button button, ArrayList<String> decision) {
		button.addSelectionListener(new SelectionAdapter()  {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button source =  (Button) e.getSource();
				decision.add(source.getText()); } }); }
}
