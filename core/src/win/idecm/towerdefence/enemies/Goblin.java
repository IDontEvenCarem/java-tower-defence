package win.idecm.towerdefence.enemies;

import com.badlogic.gdx.graphics.Texture;
import win.idecm.towerdefence.EnemyKind;

public class Goblin implements EnemyKind {
    private static final Texture texture = new Texture("enemies/goblin.png");

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public double getSpeed() {
        return 1 + Math.random();
    }

    @Override
    public double getSize() {
        return 0.55 + Math.random() * 0.2;
    }

    @Override
    public double getMaxHealth() {
        return 50 + Math.random() * 100;
    }
}
