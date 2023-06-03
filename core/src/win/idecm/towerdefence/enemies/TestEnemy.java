package win.idecm.towerdefence.enemies;

import win.idecm.towerdefence.EnemyKind;

public class TestEnemy implements EnemyKind {
    @Override
    public String getTextureName() {
        return null;
    }

    @Override
    public double getSpeed() {
        return 2;
    }

    @Override
    public double getSize() {
        return 0.75;
    }

    @Override
    public int getMaxHealth() {
        return 100;
    }
}
