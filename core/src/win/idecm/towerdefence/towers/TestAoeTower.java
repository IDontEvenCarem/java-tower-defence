package win.idecm.towerdefence.towers;

import win.idecm.towerdefence.RunningEnemy;
import win.idecm.towerdefence.TowerKind;

import java.util.List;

public class TestAoeTower implements TowerKind {

    @Override
    public String getName() {
        return "Test AOE";
    }

    @Override
    public int getBasePrice() {
        return 0;
    }

    @Override
    public double getBaseRange() {
        return 5;
    }

    @Override
    public String getTexturePath() {
        return "TowerStage6.png";
    }

    @Override
    public void onGameTickWithEnemies(List<RunningEnemy> enemies) {
        enemies.forEach(enemy -> {
            enemy.dealDotDamage(3);
        });
    }
}
