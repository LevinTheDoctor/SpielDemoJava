package Spiel;
import javax.swing.*;
public class Spiel {
	
	public static void main(String[] args) {
		// Fenster einstellungen
		JFrame Fenster = new JFrame();
		Fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Fenster.setResizable(false);
		Fenster.setTitle("Spiel Demo");
		
		SpielePanel spielPanel = new SpielePanel();
		Fenster.add(spielPanel);
		
		Fenster.pack();
		
		Fenster.setLocationRelativeTo(null);
		Fenster.setVisible(true);
		
		spielPanel.startSpielThread();
		
	}

}
