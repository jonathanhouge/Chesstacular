/**
 * This package is for all of the separate displays created, excluding the Chessboard itself.
 * All of these were created using a separate shell and SWT's widget system, utilizing 
 * ArrayLists to keep track of user decisions.
 * 
 * Player, the client's player which is created by PlayerCreateDisplay, is also included.
 * Whenever a display needs a listener, it utilizes selectListener's selectListenCreation
 * method to reduce redundancy.
 * 
 * @author Jonathan Houge
 */
package displays;