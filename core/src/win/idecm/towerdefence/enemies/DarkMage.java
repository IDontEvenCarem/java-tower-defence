package win.idecm.towerdefence.enemies;

import com.badlogic.gdx.graphics.Texture;
import win.idecm.towerdefence.EnemyKind;

public class DarkMage implements EnemyKind {
    private static final Texture texture = new Texture("enemies/darkmage.png");

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public double getSpeed() {
        return 0.55;
    }

    @Override
    public double getSize() {
        return 0.95;
    }

    @Override
    public double getMaxHealth() {
        return 1000;
    }
}
