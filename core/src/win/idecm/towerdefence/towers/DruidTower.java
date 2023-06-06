package win.idecm.towerdefence.towers;

import com.badlogic.gdx.graphics.Texture;
import win.idecm.towerdefence.GridPoint;
import win.idecm.towerdefence.Projectile;
import win.idecm.towerdefence.Tower;
import win.idecm.towerdefence.projectiles.PiercerProjectile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DruidTower extends Tower {
    static public final Texture towerTexture = new Texture("TowerStage3.png");
    static public final String name = "Druid Tower";
    static public final int basePrice = 300;

    public DruidTower(GridPoint location) {
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
        return 2.5;
    }

    @Override
    public double getBaseAttackDelay() {
        return 1.5;
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
                projList.add(new PiercerProjectile(this.getCenterPoint(), rad));
                projList.add(new PiercerProjectile(this.getCenterPoint(), rad + 0.1));
                projList.add(new PiercerProjectile(this.getCenterPoint(), rad - 0.1));
                return Optional.of(projList);
            }
        }

        return Optional.empty();
    }
}
