package win.idecm.towerdefence.towers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import win.idecm.towerdefence.GridPoint;
import win.idecm.towerdefence.Point;
import win.idecm.towerdefence.Tower;
import win.idecm.towerdefence.TowerKind;

import java.util.List;

public class TestAoeTower extends Tower {
    static Texture towerTexture = new Texture("TowerStage6.png");
    static Texture ringOfFire = new Texture("ring_of_fire_50.png");
    static TextureRegion ringOfFireRegion = new TextureRegion(ringOfFire);

    public TestAoeTower(GridPoint location) {
        super(location);
    }

    @Override
    public String getName() {
        return "Test AOE";
    }

    @Override
    public Texture getTexture() {
        return towerTexture;
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
    public void update(double timeDelta, List<Tower.EnemyWithPositioning> enemies) {
        enemies.forEach(enemy -> {
            enemy.enemy.dealDotDamage(25 * timeDelta);
        });
    }

    @Override
    public void drawBatchEffects(Point renderLocation, int gridSize, SpriteBatch b) {
        b.draw(ringOfFireRegion,
            (float) (renderLocation.getX() - ringOfFire.getWidth()/2 + gridSize/2), (float) (renderLocation.getY() - ringOfFire.getHeight()/2 + gridSize/2),
            175.0f, 175.0f, 350.0f, 350.0f, 1.0f, 1.0f, getTime() * 60.0f
        );
    }
}
