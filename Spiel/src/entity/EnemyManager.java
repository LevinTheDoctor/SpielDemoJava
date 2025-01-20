package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import Spiel.SpielePanel;

public class EnemyManager {
    private final ArrayList<Entity> enemies;
    private final ArrayList<Projectile> projectiles = new ArrayList<>();
    private final int startX;
    private final int startY;
    private final int enemySpacing;
    private final int moveSpeed;
    private final SpielePanel gp;
    private BufferedImage projectileImage;

    private boolean movingRight;

    public EnemyManager(SpielePanel gp) {
        this.gp = gp;
        this.enemies = new ArrayList<>();
        this.startX = 50;
        this.startY = 50;
        this.enemySpacing = 60;
        this.moveSpeed = 2;
        this.movingRight = true;
        spawnEnemies();
        loadProjectileImage();
    }

    private void loadProjectileImage() {
        try {
            projectileImage = ImageIO.read(getClass().getResourceAsStream("/assets/ShotEvil.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void spawnEnemies() {
        enemies.clear();
        for (int i = 0; i < 4; i++) {
            Entity enemy = new Entity();
            enemy.X = startX + i * enemySpacing;
            enemy.Y = startY;
            try {
                enemy.Enemy = ImageIO.read(getClass().getResourceAsStream("/assets/Enemy.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            enemies.add(enemy);
        }
    }

    public void update() {
        if (enemies.isEmpty()) {
            spawnEnemies();
        }

        for (Entity enemy : enemies) {
            if (movingRight) {
                enemy.X += moveSpeed;
            } else {
                enemy.X -= moveSpeed;
            }
        }

        // Check bounds and reverse direction
        if (!enemies.isEmpty()) {
            Entity first = enemies.get(0);
            Entity last = enemies.get(enemies.size() - 1);
            if (last.X + gp.TileSize >= gp.BildschirmWeite || first.X <= 0) {
                movingRight = !movingRight;
                shoot(); // Gegner schießen, wenn die Richtung wechselt
            }
        }

        updateProjectiles();
    }

    private void shoot() {
        for (Entity enemy : enemies) {
        	projectiles.add(new Projectile(enemy.X + gp.TileSize / 2, enemy.Y + gp.TileSize, projectileImage, false));  // Projektil nach unten

        }
    }

    private void updateProjectiles() {
        ArrayList<Projectile> toRemove = new ArrayList<>();
        for (Projectile projectile : projectiles) {
            projectile.update();
            if (projectile.y > gp.BildschirmHoehe) { // Entferne Projektile, die den Bildschirm verlassen
                toRemove.add(projectile);
            }
        }
        projectiles.removeAll(toRemove);
    }

    public void draw(Graphics2D g2) {
        for (Entity enemy : enemies) {
            g2.drawImage(enemy.Enemy, enemy.X, enemy.Y, gp.TileSize, gp.TileSize, null);
        }
        drawProjectiles(g2);
    }

    private void drawProjectiles(Graphics2D g2) {
        for (Projectile projectile : projectiles) {
            projectile.draw(g2);
        }
    }

    public void removeEnemy(Entity enemy) {
        enemies.remove(enemy);
    }

    public ArrayList<Entity> getEnemies() {
        return enemies;
    }

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }
    
    
}


