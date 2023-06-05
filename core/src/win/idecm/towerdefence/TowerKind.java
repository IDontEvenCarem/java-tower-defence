package win.idecm.towerdefence;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.List;
import java.util.Optional;

public interface TowerKind {
    String getName ();
    int getBasePrice ();
    double getBaseRange ();

    String getTexturePath();

    void onGameTickWithEnemies(Tower tower, double timeDelta, List<Tower.EnemyWithPositioning> enemies);

    default void drawTextureEffects(Tower running, Point renderLocation, int gridSize, SpriteBatch b) {};
    default void drawShapeEffects(Tower running, Point renderLocation, int gridSize, ShapeRenderer sr) {};

    static Optional<RunningEnemy> pickClosest(Tower tower, List<Tower.EnemyWithPositioning> enemies) {
        return enemies.stream().min((e1, e2) -> {
            var d1 = tower.getCenterPoint().distanceSquaredTo(e1.position);
            var d2 = tower.getCenterPoint().distanceSquaredTo(e2.position);
            return Double.compare(d1, d2);
        }).map(enemyWithPositioning -> enemyWithPositioning.enemy);
    };
}
