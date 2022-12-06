/** Since every display utilizes selection listeners, this separate class with the method 
 * 'selectListenCreation()' is used to add a selection listener instead of each display
 * doing it locally. 
 * 
 * @author Jonathan Houge
 */

package displays;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionAdapter;

public class selectListener {

	// selection listener - for the radio buttons & submit button [utilized by other displays to save code]
	public static void selectListenCreation(Button button, ArrayList<String> decision) {
		button.addSelectionListener(new SelectionAdapter()  {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button source =  (Button) e.getSource();
				decision.add(source.getText()); } }); }
}