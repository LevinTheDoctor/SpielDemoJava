package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Projectile {
    public int x, y; // Position des Projektils
    public int speed = 5; // Geschwindigkeit des Projektils
    public BufferedImage image;
    public boolean isUpward; // Bestimmt die Richtung des Projektils (nach oben oder nach unten)

    // Konstruktor: Füge isUpward als Parameter hinzu, um die Richtung zu bestimmen
    public Projectile(int x, int y, BufferedImage image, boolean isUpward) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.isUpward = isUpward;
    }

    public void update() {
        if (isUpward) {
            y -= speed; // Projektil nach oben bewegen
        } else {
            y += speed; // Projektil nach unten bewegen
        }
    }

    public void draw(Graphics2D g2) {
        if (image != null) {
            int newWidth = image.getWidth() * 3; // Dreifache Breite
            int newHeight = image.getHeight() * 3; // Dreifache Höhe
            g2.drawImage(image, x, y, newWidth, newHeight, null);
        }
    }
}
