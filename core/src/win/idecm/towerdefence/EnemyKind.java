package win.idecm.towerdefence;

import com.badlogic.gdx.graphics.Texture;

public interface EnemyKind {
    Texture getTexture();
    double getSpeed();
    double getSize();
    double getMaxHealth();
}
