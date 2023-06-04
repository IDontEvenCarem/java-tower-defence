package win.idecm.towerdefence.towers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import win.idecm.towerdefence.Point;
import win.idecm.towerdefence.RunningEnemy;
import win.idecm.towerdefence.RunningTower;
import win.idecm.towerdefence.TowerKind;

import java.util.List;

public class TestAoeTower implements TowerKind {

    @Override
    public String getName() {
        return "Test AOE";
    }

    @Override
    public int getBasePrice() {
        return 50;
    }

    @Override
    public double getBaseRange() {
        return 3;
    }

    @Override
    public String getTexturePath() {
        return "TowerStage6.png";
    }

    @Override
    public void onGameTickWithEnemies(double timeDelta, List<RunningEnemy> enemies) {
        enemies.forEach(enemy -> {
            enemy.dealDotDamage(25 * timeDelta);
        });
    }

    @Override
    public void drawShapeEffects(RunningTower running, Point renderLocation, int gridSize, ShapeRenderer sr) {
        if (sr.getCurrentType() != ShapeRenderer.ShapeType.Line) {
            sr.set(ShapeRenderer.ShapeType.Line);
        }
        sr.setColor(Color.ORANGE);
        var range = running.getRange();
        sr.circle(
            (float) renderLocation.getX() + gridSize/2,
            (float) renderLocation.getY() + gridSize/2,
            (float) (range*gridSize)
        );
    }
}
