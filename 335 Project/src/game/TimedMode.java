/**
 * Implements a timed mode for the chess game.
 * it can be enabled/disabled by the players.
 * 
 * 
 * Khojiakbar Yokubjonov
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
	Composite composite;
	Shell shell;
	DecimalFormat decimalFormat;
	boolean isOutOfTime;
	int counter = 0;
	String player;

	public TimedMode(Shell shell, Composite comp) {
		this.shell = shell;
		this.composite = comp;
		decimalFormat = new DecimalFormat("00");
		timeLabel = new Label(comp, SWT.None);
		timeLabel.setSize(270, 30);
		timeLabel.setFont(new Font(Display.getDefault(), "Custom Font", 11, SWT.NORMAL));
		timeLabel.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		timeLabel.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_GRAY));
		isOutOfTime = false;

	}

	public void update() {
		if(isOutOfTime) {return;}
		String min, sec;
		seconds--;
		min = decimalFormat.format(minutes);
		sec = decimalFormat.format(seconds);
		timeLabel.setText(player + "'s time: " + min + ":" + sec);
		
		if (seconds == -1) {
			seconds = 59;
			minutes--;
			min = decimalFormat.format(minutes);
			sec = decimalFormat.format(seconds);
			timeLabel.setText(player + "'s time: " + min + ":" + sec);
		
		}
		if (minutes == 0 && seconds == 0) {
			isOutOfTime = true;
			// STOP the timer
		}
		
		//						composite.redraw();
		//					
		
		counter++;

}
	
	public void setPlayer(String player) {
		this.player = player;
		String min = decimalFormat.format(minutes);
		String sec = decimalFormat.format(seconds);
		timeLabel.setText(player + "'s time: " + min + ":" + sec);
		}


	/*
	 * Sets the time limit for a player Accepts the time limit as String in the
	 * format- MM:SS
	 */
	public void setTimeLimit(String limit) {

		int[] playerTimeLimit = manageUserTimeInput(limit);
		minutes = playerTimeLimit[0];
		seconds = playerTimeLimit[1];
		
		String min = decimalFormat.format(minutes);
		String sec = decimalFormat.format(seconds);
//		if(player == null) {player = "opponent";}
		timeLabel.setText(player + "'s time: " + min + ":" + sec);

	}

	private int[] manageUserTimeInput(String time) {
		int[] timeLimit = new int[2]; // {minutes, seconds}
		String[] st = time.split(":");
		timeLimit[0] = Integer.parseInt(st[0]); // taking the minute(s)
		timeLimit[1] = Integer.parseInt(st[1]); // taking the second(s)

		return timeLimit;

	}

	/*
	 * 
	 */
	public void continueCountDownTimer() {

	}

	/*
	 * 
	 */
	public void stopCountDownTimer() {

	}

	public boolean isTimerOver() {
		return this.isOutOfTime;
	}

}
