package win.idecm.towerdefence;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Projectile {
    static public final Texture missingTexture = new Texture("badlogic.jpg");
    static public final TextureRegion textureRegion = new TextureRegion(missingTexture);

    protected Point position;
    protected double radians;
    protected int piercesLeft = getBasePierce();
    protected double lifetimeLeft = getBaseLifetime();

    protected Set<RunningEnemy> alreadyHitEnemies = new HashSet<>();

    public Projectile(Point position, double radians) {
        this.position = position;
        this.radians = radians;
    }

    protected int getBasePierce() {
        return 0;
    }
    protected double getBaseLifetime() {
        return 1.0f;
    }

    protected double getBaseDamage() {
        return 1.0f;
    };

    protected double getBaseVelocity() {
        return 1.0f;
    };

    public double getColiderSize() {
        return 0.5f;
    };

    public double getVisualSize() {
        return getColiderSize();
    }

    public double getSquaredColiderSize() {
        var coliderSize = getColiderSize();
        return coliderSize*coliderSize;
    }

    public Point getPosition () {
        return position;
    }

    public TextureRegion getTexture () {
        return textureRegion;
    }

    /**
     * Tick the projectile, giving it a chance to update itself, and to damage enemies
     * @param frametime How much time passed in the frame
     * @return Whether this projectile has expired, and should be removed. true means this projectile want to be removed
     */
    public boolean update(double frametime, List<Tower.EnemyWithPositioning> enemyList) {
        lifetimeLeft -= frametime;
        for(var enemy : enemyList) {
            if (alreadyHitEnemies.contains(enemy.enemy)) {
                continue;
            }
            alreadyHitEnemies.add(enemy.enemy);
            enemy.enemy.dealDamage(getBaseDamage());
            piercesLeft -= 1;
            if (piercesLeft < 0) {
                return true;
            }
        }
        move(frametime);
        return lifetimeLeft < 0.0f;
    }

    protected void move(double frametime) {
        position = position.addVector(Point.of(Math.cos(radians), Math.sin(radians)).multipliedBy(getBaseVelocity() * frametime));
    }

    public double getRadians() {
        return radians;
    }
}
