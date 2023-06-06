package win.idecm.towerdefence.towers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;
import win.idecm.towerdefence.GridPoint;
import win.idecm.towerdefence.Point;
import win.idecm.towerdefence.Projectile;
import win.idecm.towerdefence.Tower;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class RoyalTower extends Tower {
    private static final double FLOWER_DAMAGE_RANGE = 1.75;
    public static Texture towerTexture = new Texture("TowerStage6.png");
    public static final String name = "Royal Tower";
    public static final Texture holyFlower = new Texture("holy-flower.png");
    public static final TextureRegion holyFlowerRegion = new TextureRegion(holyFlower);
    static public final int basePrice = 800;


    private Point holyFlowerLocation = Point.of(Double.NEGATIVE_INFINITY);
    private Function<Point, Point> renderfixer = p -> p;

    public RoyalTower(GridPoint location) {
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
        return basePrice;
    }

    @Override
    public double getBaseRange() {
        return 7;
    }

    @Override
    public Optional<List<Projectile>> update(double timeDelta, List<EnemyWithPositioning> enemies) {
        updateTiming(timeDelta);
        var furthestEnemy = enemies.stream().max(Comparator.comparingDouble(o -> o.enemy.getPosition()));
        if (furthestEnemy.isPresent()) {
            var flowerCenter = furthestEnemy.get().position;
            enemies.forEach(enemy -> {
                if (enemy.position.distanceSquaredTo(flowerCenter) < FLOWER_DAMAGE_RANGE*FLOWER_DAMAGE_RANGE) {
                    enemy.enemy.dealDotDamage(50 * timeDelta);
                }
            });
            holyFlowerLocation = flowerCenter;
        } else {
            holyFlowerLocation = Point.of(Double.NEGATIVE_INFINITY);
        }
        return Optional.empty();
    }

    @Override
    public void drawBatchEffects(Point renderLocation, int gridSize, SpriteBatch b) {
        if (holyFlowerLocation.getX() != Double.NEGATIVE_INFINITY) {
            b.draw(
                holyFlowerRegion,
                (float) renderLocation.getX() - holyFlowerRegion.getRegionWidth()/2 + gridSize/2,
                (float) renderLocation.getY() - holyFlowerRegion.getRegionHeight()/2 + gridSize/2,
                holyFlowerRegion.getRegionWidth()/2,
                holyFlowerRegion.getRegionHeight()/2,
                125.0f,
                125.0f,
                0.75f,
                0.75f,
                getTime() * 90.0f
            );

            var renderto = renderfixer.apply(holyFlowerLocation);
            b.draw(
                holyFlowerRegion,
                (float) renderto.getX() - holyFlowerRegion.getRegionWidth()/2,
                (float) renderto.getY() - holyFlowerRegion.getRegionHeight()/2,
                holyFlowerRegion.getRegionWidth()/2,
                holyFlowerRegion.getRegionHeight()/2,
                125.0f,
                125.0f,
                (float) FLOWER_DAMAGE_RANGE,
                (float) FLOWER_DAMAGE_RANGE,
                getTime() * 90.0f
            );
        }
    }

    @Override
    public void injectRenderabler (Function<Point, Point> toRenderable) {
        renderfixer = toRenderable;
    }
}
