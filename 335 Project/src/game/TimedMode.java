/**
 * Description: Implements a timed mode for the chess game.
 * 			it can be enabled/disabled by the players.
 * 
 * @author Khojiakbar Yokubjonov
 */
package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class TimedMode {

	int seconds;
	int minutes;
	Timer timer;
	Label timeLabel;
	Label nameLabel;
	Composite composite;
	Shell shell;
	DecimalFormat decimalFormat;
	boolean isOutOfTime;
	int counter = 0;
	String player;
	
	/**
	 * Constructs a countdown timer for the player
	 * Accepts a shell and a composite to draw the timer
	 * @param shell UI's shell
	 * @param comp composite to hold the timer on the shell
	 */
	public TimedMode(Shell shell, Composite comp) {
		this.shell = shell;
		this.composite = comp;
		decimalFormat = new DecimalFormat("00");
		timeLabel = new Label(composite, SWT.None);
		timeLabel.setSize(270, 40);
		timeLabel.setFont(new Font(Display.getDefault(), "Custom Font", 15, SWT.BOLD));
		timeLabel.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_RED));
		timeLabel.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		timeLabel.setText("00:00");
		
		nameLabel = new Label(composite, SWT.None);
		nameLabel.setFont(new Font(Display.getDefault(), "Custom Font", 15, SWT.BOLD));
		nameLabel.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_BLUE));
		nameLabel.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_GRAY));
		nameLabel.setSize(450, 40);
		nameLabel.setText("Player");
		nameLabel.moveAbove(timeLabel);
		isOutOfTime = false;

	}
	
	/**
	 * Updates the countdown timer
	 */
	public void update() {
		if(isOutOfTime) {return;}
		String min, sec;
		seconds--;
		min = decimalFormat.format(minutes);
		sec = decimalFormat.format(seconds);
		timeLabel.setText(min + ":" + sec);
		timeLabel.requestLayout();
		
		
		if (seconds == -1) {
			seconds = 59;
			minutes--;
			min = decimalFormat.format(minutes);
			sec = decimalFormat.format(seconds);
			timeLabel.setText(min + ":" + sec);
			timeLabel.requestLayout();
		
		}
		if (minutes == 0 && seconds == 0) {
			isOutOfTime = true;
			// STOP the timer
		}				
		
		counter++;
	}
	
	/**
	 * Sets the player's name next to the timer
	 * @param player the player's name
	 */
	public void setPlayer(String player) {
		nameLabel.setText(player);
		nameLabel.requestLayout();		
		}

	
	/**
	 * Sets the time limit for a player.
	 * Accepts the time limit as String in the format- MM:SS. 
	 * e.g. 10:30
	 * @param limit String time limit
	 */
	public void setTimeLimit(String limit) {

		int[] playerTimeLimit = manageUserTimeInput(limit);
		minutes = playerTimeLimit[0];
		seconds = playerTimeLimit[1];
		
		String min = decimalFormat.format(minutes);
		String sec = decimalFormat.format(seconds);
		timeLabel.setText(min + ":" + sec);

	}

	/**
	 * Accepts a string time input in the format "MM:SS"
	 * Returns an array containing the minutes and seconds
	 * e.g for the input "10:30", it'd return {10, 30}
	 * @param time String time
	 * @return array of ints containing the minutes and seconds
	 */
	private int[] manageUserTimeInput(String time) {
		int[] timeLimit = new int[2]; // {minutes, seconds}
		String[] st = time.split(":");
		timeLimit[0] = Integer.parseInt(st[0]); // taking the minute(s)
		timeLimit[1] = Integer.parseInt(st[1]); // taking the second(s)

		return timeLimit;

	}

	
	/**
	 * Returns true if the player is out of time, otherwise false.
	 * @return boolean value
	 */
	public boolean isTimerOver() {
		return this.isOutOfTime;
	}

}