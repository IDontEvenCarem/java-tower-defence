package win.idecm.towerdefence;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

public class Projectile {
    protected double lifetimeLeft;
    protected Point position;
    protected double angle;
    protected int piercesLeft;

    public Projectile(Point position, double angle) {
        this.position = position;
        this.angle = angle;
        piercesLeft = getBasePierce();
    }

    protected int getBasePierce() {
        return 0;
    }

    double getBaseDamage() {
        return 1.0f;
    };

    double getBaseVelocity() {
        return 1.0f;
    };

    public double getColiderSize() {
        return 0.5f;
    };

    public Point getPosition () {
        return position;
    }

    public Texture getTexture () {
        return new Texture("badlogic.jpg");
    }

    /**
     * Tick the projectile, giving it a chance to update itself, and to damage enemies
     * @param frametime How much time passed in the frame
     * @return Whether this projectile has expired, and should be removed. true means this projectile want to be removed
     */
    public boolean update(float frametime, List<RunningEnemy> enemyList) {
        lifetimeLeft -= frametime;
        for(var enemy : enemyList) {
            enemy.dealDamage(getBaseDamage());
            piercesLeft -= 1;
            if (piercesLeft < 0) {
                return true;
            }
        }
        move(frametime);
        return lifetimeLeft < 0.0f;
    }

    protected void move(float frametime) {
        var radian = angle / 180.0f * Math.PI;
        position = position.addVector(Point.of(Math.sin(radian), Math.cos(radian)).multipliedBy(getBaseVelocity() * frametime));
    }
}
