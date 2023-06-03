package win.idecm.towerdefence;

import java.util.List;

public interface TowerKind {
    String getName ();
    int getBasePrice ();
    double getBaseRange ();

    String getTexturePath();

    void onGameTickWithEnemies(List<RunningEnemy> enemies);
}
