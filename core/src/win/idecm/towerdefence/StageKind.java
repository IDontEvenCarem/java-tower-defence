package win.idecm.towerdefence;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public interface StageKind {
    String getName();
    List<EnemyPath> getPaths();
    String getBackgroundTexturePath();

    int getGridSize();

    default Set<GridPoint> getBannedGridPoints() {
        return new HashSet<>();
    };
}
