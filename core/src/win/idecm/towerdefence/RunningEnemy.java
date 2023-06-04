package win.idecm.towerdefence;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class RunningEnemy {
    private final EnemyKind kind;
    private final int pathIndex;
    private double position;
    private double speed;
    private double health;
    private double maxHealth;

    public RunningEnemy(EnemyKind kind, int pathIndex) {
        this.kind = kind;
        this.pathIndex = pathIndex;
        this.position = -kind.getSize();
        this.maxHealth = kind.getMaxHealth();
        this.health = kind.getMaxHealth();
        this.speed = kind.getSpeed();
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

    public double getSize() { return kind.getSize(); }

    public void applyLifeMultiplier(double mult) {
        maxHealth *= mult;
        health *= mult;
    }

    public void move(double timeElapsed) {
        position += getSpeed() * timeElapsed;
    }

    public void dealDotDamage(double amount) {
        health -= amount;
        doDeathChecksAndChanges();
    }

    public void decreaseHealth(int amount) {
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
