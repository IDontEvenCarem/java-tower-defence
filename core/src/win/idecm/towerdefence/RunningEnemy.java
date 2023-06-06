package win.idecm.towerdefence;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RunningEnemy {
    private final EnemyKind kind;
    private final int pathIndex;
    private double position;
    private double speed;
    private double health;
    private double maxHealth;
    private double size;
    private TextureRegion textureRegion;

    private double frameSlow = 1.0f;

    public RunningEnemy(EnemyKind kind, int pathIndex) {
        this.kind = kind;
        this.pathIndex = pathIndex;
        this.position = -kind.getSize();
        this.maxHealth = kind.getMaxHealth();
        this.health = kind.getMaxHealth();
        this.speed = kind.getSpeed();
        this.size = kind.getSize();
        this.textureRegion = new TextureRegion(kind.getTexture());
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public int getPathIndex() {
        return pathIndex;
    }

    public double getSpeed() {
        return speed;
    }

    public double getPosition() {
        return position;
    }

    public double getHealth() {
        return health;
    }
    public double getMaxHealth() { return maxHealth; }

    public double getSize() { return size; }

    public void applyLifeMultiplier(double mult) {
        maxHealth *= mult;
        health *= mult;
    }

    public void applySlow (double slow) {
        frameSlow *= slow;
        if (frameSlow < 0.5) {
            frameSlow = 0.5;
        }
    }

    public void move(double timeElapsed) {
        position += getSpeed() * timeElapsed * frameSlow;
        frameSlow = 1.0f;
    }

    public void dealDotDamage(double amount) {
        health -= amount;
        doDeathChecksAndChanges();
    }

    public void dealDamage(double amount) {
        health -= amount;
        doDeathChecksAndChanges();
    }

    private void doDeathChecksAndChanges () {
        if (health <= 0) {
            position = Double.NEGATIVE_INFINITY;
            health = 0;
        }
    }
}
