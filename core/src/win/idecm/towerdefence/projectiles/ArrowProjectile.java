package win.idecm.towerdefence.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import win.idecm.towerdefence.Point;
import win.idecm.towerdefence.Projectile;

public class ArrowProjectile extends Projectile {
    static final private Texture texture = new Texture("projectiles/arrow.png");
    static final private TextureRegion textureRegion = new TextureRegion(texture);

    public ArrowProjectile(Point position, double radians) {
        super(position, radians);
    }

    @Override
    public double getColiderSize() {
        return 0.5;
    }

    @Override
    protected double getBaseDamage() {
        return 100.0;
    }

    @Override
    protected double getBaseVelocity() {
        return 30.0;
    }

    @Override
    protected double getBaseLifetime() {
        return 1;
    }

    @Override
    public TextureRegion getTexture() {
        return textureRegion;
    }
}