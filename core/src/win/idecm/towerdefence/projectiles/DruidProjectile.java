package win.idecm.towerdefence.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import win.idecm.towerdefence.Point;
import win.idecm.towerdefence.Projectile;

public class DruidProjectile extends Projectile {
    static final private Texture texture = new Texture("projectiles/druid.png");
    static final private TextureRegion textureRegion = new TextureRegion(texture);


    public DruidProjectile(Point position, double radians) {
        super(position, radians);
    }

    @Override
    protected int getBasePierce() {
        return 2;
    }

    @Override
    public double getColiderSize() {
        return 0.5;
    }

    @Override
    protected double getBaseDamage() {
        return 35.0;
    }

    @Override
    protected double getBaseVelocity() {
        return 15.0;
    }

    @Override
    protected double getBaseLifetime() {
        return 0.20;
    }

    @Override
    public TextureRegion getTexture() {
        return textureRegion;
    }
}
