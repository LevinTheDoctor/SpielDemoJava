package Spiel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
	public boolean LeftPressed,RightPressed,ShootPressed;

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_A) {
			LeftPressed = true;
		}
		if(code == KeyEvent.VK_D) {
			RightPressed = true;
			
		}
		if(code == KeyEvent.VK_SPACE) {
			ShootPressed = true;
			
			
		}


		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_A) {
			LeftPressed = false;
		}
		if(code == KeyEvent.VK_D) {
			RightPressed = false;
			
		}
		if(code == KeyEvent.VK_SPACE) {
			ShootPressed = false;
			
		}
		
	}
	

}
