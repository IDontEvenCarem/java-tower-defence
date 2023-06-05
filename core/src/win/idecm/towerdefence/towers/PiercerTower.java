package win.idecm.towerdefence.towers;

import com.badlogic.gdx.graphics.Texture;
import win.idecm.towerdefence.GridPoint;
import win.idecm.towerdefence.Tower;

import java.util.List;

public class PiercerTower extends Tower {
    static private final Texture towerTexture = new Texture("TowerStage1.png");

    public PiercerTower(GridPoint location) {
        super(location);
    }

    @Override
    public String getName() {
        return "Piercer Tower";
    }

    @Override
    public Texture getTexture() {
        return towerTexture;
    }

    @Override
    public int getBasePrice() {
        return 50;
    }

    @Override
    public double getBaseRange() {
        return 5;
    }

    @Override
    public void update(double timeDelta, List<EnemyWithPositioning> enemiesInRange) {

    }
}
