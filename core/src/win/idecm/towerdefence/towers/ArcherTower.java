package win.idecm.towerdefence.towers;

import com.badlogic.gdx.graphics.Texture;
import win.idecm.towerdefence.GridPoint;
import win.idecm.towerdefence.Projectile;
import win.idecm.towerdefence.Tower;
import win.idecm.towerdefence.projectiles.PiercerProjectile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArcherTower extends Tower {
    static private final Texture towerTexture = new Texture("TowerStage2.png");

    public ArcherTower(GridPoint location) {
        super(location);
    }

    @Override
    public String getName() {
        return "Archer Tower";
    }

    @Override
    public Texture getTexture() {
        return towerTexture;
    }

    @Override
    public int getBasePrice() {
        return 200;
    }

    @Override
    public double getBaseRange() {
        return 8;
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
                System.out.println("Trying to shoot");
                var projList = new ArrayList<Projectile>();
                projList.add(new PiercerProjectile(this.getCenterPoint(), rad));
                return Optional.of(projList);
            }
        }

        return Optional.empty();
    }
}