package win.idecm.towerdefence.towers;

import com.badlogic.gdx.graphics.Texture;
import win.idecm.towerdefence.GridPoint;
import win.idecm.towerdefence.Projectile;
import win.idecm.towerdefence.Tower;
import win.idecm.towerdefence.projectiles.ArrowProjectile;
import win.idecm.towerdefence.projectiles.PiercerProjectile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArcherTower extends Tower {
    static public final Texture towerTexture = new Texture("TowerStage2.png");
    public static final String name = "Archer Tower";
    public static final int basePrice = 200;

    public ArcherTower(GridPoint location) {
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
        return 12;
    }

    @Override
    public double getBaseAttackDelay() {
        return 2;
    }

    @Override
    public Optional<List<Projectile>> update(double timeDelta, List<EnemyWithPositioning> enemiesInRange) {
        updateTiming(timeDelta);
        var enemy = pickClosest(enemiesInRange);
        if (enemy.isPresent()) {
            var from = this.getCenterPoint();
            var to = enemy.get().position;
            var rad = Math.atan2(to.getY() - from.getY(), to.getX() - from.getX());
            if(checkAttackTiming()) {
                var projList = new ArrayList<Projectile>();
                projList.add(new ArrowProjectile(this.getCenterPoint(), rad));
                return Optional.of(projList);
            }
        }

        return Optional.empty();
    }
}
