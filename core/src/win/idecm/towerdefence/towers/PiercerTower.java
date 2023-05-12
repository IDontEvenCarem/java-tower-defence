package win.idecm.towerdefence.towers;

import win.idecm.towerdefence.TowerKind;

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
}
