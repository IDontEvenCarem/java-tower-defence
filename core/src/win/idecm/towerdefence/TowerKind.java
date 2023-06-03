package win.idecm.towerdefence;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.List;

public interface TowerKind {
    String getName ();
    int getBasePrice ();
    double getBaseRange ();

    String getTexturePath();

    void onGameTickWithEnemies(double timeDelta, List<RunningEnemy> enemies);

    default void drawTextureEffects(RunningTower running, Point renderLocation, int gridSize, SpriteBatch b) {};
    default void drawShapeEffects(RunningTower running, Point renderLocation, int gridSize, ShapeRenderer sr) {};
}
