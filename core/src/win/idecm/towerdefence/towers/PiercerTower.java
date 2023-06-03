package win.idecm.towerdefence.towers;

import win.idecm.towerdefence.RunningEnemy;
import win.idecm.towerdefence.TowerKind;

import java.util.List;

public class PiercerTower implements TowerKind {

    @Override
    public String getName() {
        return "Piercer";
    }

    @Override
    public int getBasePrice() {
        return 100;
    }

    @Override
    public double getBaseRange() {
        return 100;
    }

    @Override
    public String getTexturePath() {
        return null;
    }

    @Override
    public void onGameTickWithEnemies(List<RunningEnemy> enemies) {
    }
}
