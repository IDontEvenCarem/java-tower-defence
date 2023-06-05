package win.idecm.towerdefence.projectiles;

import com.badlogic.gdx.graphics.Texture;
import win.idecm.towerdefence.Point;
import win.idecm.towerdefence.Projectile;

public class PiercerProjectile extends Projectile {
    static final private Texture texture = new Texture("projectiles/basic_piercer.png");

    public PiercerProjectile(Point position, double angle) {
        super(position, angle);
    }

    @Override
    protected int getBasePierce() {
        return 5;
    }

    @Override
    public double getColiderSize() {
        return 0.2;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }
}
