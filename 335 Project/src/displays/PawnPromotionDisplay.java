/**
 * The Display for Pawn Promotion.
 * When a pawn reaches the final row (any tile on rows 1 & 8), this display is presented.
 * Prompts the client to pick which specialty piece the pawn is being promoted to. The
 * default is Queen, but Rook, Knight, and Bishop are options as well. It returns
 * the String of the piece the client wishes to promote their pawn to.
 * This is called from Chessboard's checkPromotion() method.
 * 
 * Since every display utilizes selection listeners, a separate class with the method 
 * 'selectListenCreation()' is used to add a selection listener.
 * 
 * @author Jonathan Houge
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
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.GridLayout;

public class PawnPromotionDisplay {

	// start returns the player made by the client

	/** 
	 * The start method creates and runs the display itself. It utilizes 
	 * functions to create the different kinds of radio buttons and creates
	 * textual widgets right then and there. The display stays open until
	 * the user clicks the "Promote!" button. In then collects the 
	 * entered information and returns it.
	 * 
	 * @param display: Display object, the Chessboard's Display
	 * @return String of what piece the client wants their pawn to be promoted to
	 */
	public String start(Display display) {
		
		//-- create and set up the shell & create variables for the shell's widgets 
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
		
		// title text to let the client know the purpose, that pawn promotion input is needed
		Text title = new Text(shell, SWT.READ_ONLY);
		title.setText("Choose Pawn Promotion");
		title.setFont(new Font(display, "Courier", 18, SWT.NONE));
		title.setForeground(color);
		
		//-- the widgets
		
		// pick what piece pawn should be promoted to; default: Queen
		ArrayList<String> decision = new ArrayList<String>(); decision.add("Queen");
		colorButtons(shell, decision, widgetLayout, labelFont, buttonFont, color);
		
		// submit button - will return currently selected radio button
		ArrayList<String> submit = new ArrayList<String>();
		Button start = new Button(shell, SWT.PUSH); start.setText("Promote!");
		selectListener.selectListenCreation(start, submit);
		
		shell.pack(); shell.open();
		while (submit.size() == 0) { // while the promotion button hasn't been clicked
			if (!display.readAndDispatch ())
				display.sleep (); }
		
		// gathers the piece the client picked
		String piece = decision.get(decision.size() - 1);
		shell.dispose();
		
		return piece; } // returning wanted piece

	/** 
	 * Radio button for picking pawn promotion. Makes the buttons 'Rook',
	 * 'Knight', 'Bishop', and 'Queen' in their own group, using the given layout,
	 * fonts, and color. Adds a selection listener to each button.
	 * 
	 * @param shell: the shell that we're adding the buttons to
	 * @param decision: the ArrayList that'll be added to whenever the user picks a new option
	 * @param layout: the layout that the widgets will conform to
	 * @param title: the font for the group's label / title
	 * @param button: the font for the button's themselves
	 * @param color: the color that the label and buttons will be
	 */
	private static void colorButtons(Shell shell, ArrayList<String> decision, RowLayout layout, Font title, Font button, Color color) {
		//-- set up general button layout
		Group colors = new Group(shell, SWT.NONE);
		colors.setLayout(layout);

		//-- buttons themselves - default select the fourth option ("Queen")
		Button rook = new Button(colors, SWT.RADIO); rook.setText("Rook");
		rook.setFont(button); rook.setForeground(color);
		selectListener.selectListenCreation(rook, decision);

		Button knight = new Button(colors, SWT.RADIO); knight.setText("Knight");
		knight.setFont(button); knight.setForeground(color);
		selectListener.selectListenCreation(knight, decision);

		Button bishop = new Button(colors, SWT.RADIO); bishop.setText("Bishop");
		bishop.setFont(button); bishop.setForeground(color);
		selectListener.selectListenCreation(bishop, decision);
		
		Button queen = new Button(colors, SWT.RADIO); queen.setText("Queen");
		queen.setSelection(true); queen.setFont(button); queen.setForeground(color);
		selectListener.selectListenCreation(queen, decision);
	}
}