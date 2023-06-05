package win.idecm.towerdefence.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import win.idecm.towerdefence.Point;
import win.idecm.towerdefence.Projectile;

public class PiercerProjectile extends Projectile {
    static final private Texture texture = new Texture("projectiles/basic_piercer.png");
    static final private TextureRegion textureRegion = new TextureRegion(texture);


    public PiercerProjectile(Point position, double radians) {
        super(position, radians);
    }

    @Override
    protected int getBasePierce() {
        return 5;
    }

    @Override
    public double getColiderSize() {
        return 0.5;
    }

    @Override
    protected double getBaseDamage() {
        return 50.0;
    }

    @Override
    protected double getBaseVelocity() {
        return 10.0;
    }

    @Override
    public TextureRegion getTexture() {
        return textureRegion;
    }
}
