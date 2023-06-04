package win.idecm.towerdefence.towers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import win.idecm.towerdefence.Point;
import win.idecm.towerdefence.RunningEnemy;
import win.idecm.towerdefence.RunningTower;
import win.idecm.towerdefence.TowerKind;

import java.util.List;

public class TestAoeTower implements TowerKind {
    static Texture ringOfFire = new Texture("ring_of_fire_50.png");
    static TextureRegion ringOfFireRegion = new TextureRegion(ringOfFire);

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
    public void drawTextureEffects(RunningTower running, Point renderLocation, int gridSize, SpriteBatch b) {
        b.draw(ringOfFireRegion,
            (float) (renderLocation.getX() - ringOfFire.getWidth()/2 + gridSize/2), (float) (renderLocation.getY() - ringOfFire.getHeight()/2 + gridSize/2),
            175.0f, 175.0f, 350.0f, 350.0f, 1.0f, 1.0f, running.getTime() * 60.0f
        );
    }
}
