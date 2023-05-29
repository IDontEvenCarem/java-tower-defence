package win.idecm.towerdefence;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class RunningEnemy {
    private final EnemyKind kind;
    private final int pathIndex;
    private double position;
    private int health;
    private int damagePerSecond;
    private double lastDamageTime;

    public RunningEnemy(EnemyKind kind, int pathIndex, int health, int damagePerSecond) {
        this.kind = kind;
        this.pathIndex = pathIndex;
        this.position = 0.0;
        this.health = health;
        this.damagePerSecond = damagePerSecond;
        this.lastDamageTime = 0.0;
    }

    public int getPathIndex() {
        return pathIndex;
    }

    public double getSpeed() {
        return kind.getSpeed();
    }

    public double getPosition() {
        return position;
    }

    public int getHealth() {
        return health;
    }

    public void move(double timeElapsed) {
        position += getSpeed() * timeElapsed;
    }

    public void decreaseHealth(int amount) {
        health -= amount;
        if (health <= 0) {
            position = Double.POSITIVE_INFINITY;
            health = 0;
        }
    }

}
