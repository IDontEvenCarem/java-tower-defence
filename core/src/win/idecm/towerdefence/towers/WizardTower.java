package win.idecm.towerdefence.towers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import win.idecm.towerdefence.GridPoint;
import win.idecm.towerdefence.Point;
import win.idecm.towerdefence.Projectile;
import win.idecm.towerdefence.Tower;

import java.util.List;
import java.util.Optional;

public class WizardTower extends Tower {
    public static Texture towerTexture = new Texture("TowerStage4.png");
    public static String name = "Wizard Tower";
    public static int basePrice = 400;
    static Texture arcaneRing = new Texture("arcane-circle-transparenter.png");
    static TextureRegion arcaneRingRegion = new TextureRegion(arcaneRing);

    public WizardTower(GridPoint location) {
        super(location);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Texture getTexture() {
        return towerTexture;
    }

    @Override
    public int getBasePrice() {
        return 400;
    }

    @Override
    public double getBaseRange() {
        return 2.5;
    }

    @Override
    public Optional<List<Projectile>> update(double timeDelta, List<EnemyWithPositioning> enemies) {
        updateTiming(timeDelta);
        enemies.forEach(enemy -> {
            enemy.enemy.applySlow(0.85f);
            enemy.enemy.dealDotDamage(20 * timeDelta);
        });
        return Optional.empty();
    }

    @Override
    public void drawBatchEffects(Point renderLocation, int gridSize, SpriteBatch b) {
        b.draw(arcaneRingRegion,
            (float) (renderLocation.getX() - arcaneRingRegion.getRegionWidth()/2 + gridSize/2),
            (float) (renderLocation.getY() - arcaneRingRegion.getRegionWidth()/2 + gridSize/2),
            arcaneRingRegion.getRegionWidth()/2.0f,
            arcaneRingRegion.getRegionHeight()/2.0f,
            300, 300, 1, 1,
            getTime() * 60.0f
        );
    }
}
