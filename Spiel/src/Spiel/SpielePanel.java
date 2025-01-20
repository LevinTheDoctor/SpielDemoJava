package Spiel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import entity.Projectile;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import entity.EnemyManager;
import entity.Entity;
import entity.Spieler;

public class SpielePanel extends JPanel implements Runnable {
    final int OrginaleTileSize = 16; // 16 x 16 Pixel Grafiken
    final int vergroessern = 3; // 16 x 16 zu 48 x 48 Grafiken
    final int FPS = 60;
    public final int TileSize = OrginaleTileSize * vergroessern;
    final int BildschirmSpalten = 16;
    final int BildschirmZeilen = 12;
    public final int BildschirmWeite = TileSize * BildschirmSpalten;
    public final int BildschirmHoehe = TileSize * BildschirmZeilen;

    KeyHandler keyH = new KeyHandler();
    Thread SpielThread;
    Spieler spieler = new Spieler(this, keyH);

    private int hearts = 6; // 3 full hearts (each heart = 2 hits)
    private int score = 0;

    private EnemyManager enemyManager;
    private BufferedImage background;
    private BufferedImage herz;
    private BufferedImage halbherz;
    private BufferedImage herzLeer;

    public SpielePanel() {
        this.setPreferredSize(new Dimension(BildschirmWeite, BildschirmHoehe));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        enemyManager = new EnemyManager(this);
        loadImages();


    }
    
    private void loadImages() {
        try {
        	herzLeer = ImageIO.read(getClass().getResourceAsStream("/assets/HerzLeer.png"));
            if (halbherz == null) {
                System.out.println("Halbherz.png konnte nicht geladen werden!");
            }
            background = ImageIO.read(getClass().getResourceAsStream("/assets/Background_.png"));
            if (background == null) {
                System.out.println("Background_.png konnte nicht geladen werden!");
            }
            herz = ImageIO.read(getClass().getResourceAsStream("/assets/Herz.png"));
            if (herz == null) {
                System.out.println("Herz.png konnte nicht geladen werden!");
            }
            halbherz = ImageIO.read(getClass().getResourceAsStream("/assets/Herzhalb.png"));
            if (halbherz == null) {
                System.out.println("Halbherz.png konnte nicht geladen werden!");
            }
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void startSpielThread() {
        SpielThread = new Thread(this);
        SpielThread.start();
    }

    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (SpielThread != null) {
            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        spieler.update();
        enemyManager.update();
        checkCollision();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Hintergrund zeichnen
        for (int x = 0; x < BildschirmSpalten; x++) {
            for (int y = 0; y < BildschirmZeilen; y++) {
                g2.drawImage(background, x * TileSize, y * TileSize, TileSize, TileSize, null);
            }
        }

        // Spieler zeichnen
        spieler.draw(g2);
     // Draw enemies
        enemyManager.draw(g2);

        // Draw hearts
        drawHearts(g2);

        // Draw score
        g2.setColor(Color.WHITE);
        g2.drawString("Score: " + score, 10, 20);

        g2.dispose();


    }
    
    private void drawHearts(Graphics2D g2) {
        int heartX = BildschirmWeite - 150;
        int heartY = 10;
        int heartSize = 20;

        // Anzahl der vollen Herzen (jedes Herz repräsentiert 2 Treffer)
        int fullHearts = hearts / 2;
        
        // Zeichne die vollen Herzen
        for (int i = 0; i < fullHearts; i++) {
            g2.drawImage(herz, heartX + i * (heartSize + 10), heartY, heartSize, heartSize, null);
        }

        // Wenn es ein halbes Herz gibt, zeichne es
        if (hearts % 2 == 1) {
            g2.drawImage(halbherz, heartX + fullHearts * (heartSize + 10), heartY, heartSize, heartSize, null);
        }
        
        // Zeichne leere Herzen für die restlichen (zerstörten) Herzen
        for (int i = fullHearts + (hearts % 2); i < 3; i++) {
            g2.drawImage(herzLeer, heartX + i * (heartSize + 10), heartY, heartSize, heartSize, null);
        }
    }


    public void loseHeart() {
        hearts--;
        if (hearts <= 0) {
            gameOver();
        }
    }


    private void gameOver() {
        // Stop the game
        SpielThread = null;
        System.out.println("Game Over!");
    }

    public void increaseScore() {
        score += 100;
    }
    
    private void checkCollision() {
        ArrayList<Projectile> playerProjectiles = spieler.getProjectiles();
        ArrayList<Entity> enemies = enemyManager.getEnemies();
        ArrayList<Projectile> enemyProjectiles = enemyManager.getProjectiles(); 

        ArrayList<Projectile> toRemoveProjectiles = new ArrayList<>();
        ArrayList<Entity> toRemoveEnemies = new ArrayList<>();
        ArrayList<Projectile> toRemoveEnemyProjectiles = new ArrayList<>();

        for (Projectile projectile : playerProjectiles) {
            for (Entity enemy : enemies) {
                if (projectile.x < enemy.X + TileSize &&
                    projectile.x > enemy.X &&
                    projectile.y < enemy.Y + TileSize &&
                    projectile.y > enemy.Y) {
                    
                    toRemoveProjectiles.add(projectile);
                    toRemoveEnemies.add(enemy);
                    increaseScore();
                }
            }
        }
        
        playerProjectiles.removeAll(toRemoveProjectiles);
        enemies.removeAll(toRemoveEnemies);
        
        for (Projectile enemyProjectile : enemyProjectiles) {
            // Überprüfe, ob das Projektil den Spieler trifft
            if (enemyProjectile.x < spieler.X + TileSize &&
                enemyProjectile.x > spieler.X &&
                enemyProjectile.y < spieler.Y + TileSize &&
                enemyProjectile.y > spieler.Y) {

                toRemoveEnemyProjectiles.add(enemyProjectile);
                loseHeart(); // Spieler verliert ein Herz
            }
        }
        playerProjectiles.removeAll(toRemoveProjectiles);
        enemies.removeAll(toRemoveEnemies);
        enemyProjectiles.removeAll(toRemoveEnemyProjectiles);
    }


}

    

    
    



