package win.idecm.towerdefence.enemies;

import com.badlogic.gdx.graphics.Texture;
import win.idecm.towerdefence.EnemyKind;

public class TestEnemy implements EnemyKind {
    private static final Texture texture = new Texture("enemies/EnemyArcher.png");

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public double getSpeed() {
        return 2;
    }

    @Override
    public double getSize() {
        return 0.75;
    }

    @Override
    public double getMaxHealth() {
        return 100.0;
    }
}
