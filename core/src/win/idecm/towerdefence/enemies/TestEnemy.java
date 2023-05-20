package win.idecm.towerdefence.enemies;

import win.idecm.towerdefence.EnemyKind;

public class TestEnemy implements EnemyKind {
    @Override
    public String getTextureName() {
        return null;
    }

    @Override
    public double getSpeed() {
        return 150;
    }
}
