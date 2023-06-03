package win.idecm.towerdefence;

import java.util.List;

public interface StageKind {
    String getName();
    List<EnemyPath> getPaths();
    String getBackgroundTexturePath();

    int getGridSize();
}
