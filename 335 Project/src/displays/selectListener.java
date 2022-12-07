package displays;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionAdapter;

/** 
 * Since every display utilizes selection listeners, this separate class with the method 
 * 'selectListenCreation()' is used to add a selection listener instead of each display
 * doing it locally. 
 * 
 * @author Jonathan Houge
 */
public class selectListener {

	/** 
	 * Selection listener for radio buttons and submit buttons.
	 * Utilized by every display (other than the chessboard) in an effort to reduce code.
	 * 
	 * @param button: the button that the listener is being applied to
	 * @param decision: the arraylist that'll keep track of button results
	 */
	public static void selectListenCreation(Button button, ArrayList<String> decision) {
		button.addSelectionListener(new SelectionAdapter()  {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button source =  (Button) e.getSource();
				decision.add(source.getText()); } }); }
}