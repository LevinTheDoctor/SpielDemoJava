package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Spiel.KeyHandler;
import Spiel.SpielePanel;

public class Spieler extends Entity {
    private final SpielePanel gp;
    private final KeyHandler keyH;
    private final ArrayList<Projectile> projectiles = new ArrayList<>();
    private BufferedImage projectileImage;

    public Spieler(SpielePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues();
        spielerBildLaden();
        loadProjectileImage();
    }

    public void setDefaultValues() {
        speed = 200.0 / 60;
        X = 360;
        Y = 504;
    }

    public void spielerBildLaden() {
        try {
            Player = ImageIO.read(getClass().getResourceAsStream("/assets/Player.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProjectileImage() {
        try {
            projectileImage = ImageIO.read(getClass().getResourceAsStream("/assets/ShotGood.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (keyH.LeftPressed && X > 0) {
            X -= speed;
        }
        if (keyH.RightPressed && X < gp.BildschirmWeite - gp.TileSize) {
            X += speed;
        }
        if (keyH.ShootPressed) { // Spieler schießt
            shoot();
        }
        updateProjectiles();
    }

    private void shoot() {
        // Erstelle ein neues Projektil
        projectiles.add(new Projectile(X + gp.TileSize / 2, Y, projectileImage,true));
        keyH.ShootPressed = false;
        
    }

    private void updateProjectiles() {
        ArrayList<Projectile> toRemove = new ArrayList<>();
        for (Projectile projectile : projectiles) {
            projectile.update();
            if (projectile.y < 0) { // Entferne Projektile, die den Bildschirm verlassen
                toRemove.add(projectile);
            }
        }
        projectiles.removeAll(toRemove);
    }

    public void draw(Graphics2D g2) {
        if (Player != null) {
            g2.drawImage(Player, (int) X, Y, gp.TileSize, gp.TileSize, null);
        }
        drawProjectiles(g2);
    }

    private void drawProjectiles(Graphics2D g2) {
        for (Projectile projectile : projectiles) {
            projectile.draw(g2);
        }
    }

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }
    
    

}


