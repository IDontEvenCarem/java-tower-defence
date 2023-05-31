package win.idecm.towerdefence.stages;

import win.idecm.towerdefence.EnemyPath;
import win.idecm.towerdefence.Point;
import win.idecm.towerdefence.StageKind;

import java.util.ArrayList;
import java.util.List;

public class TestStage implements StageKind {
    @Override
    public String getName() {
        return "Test Stage";
    }

    @Override
    public List<EnemyPath> getPaths() {
        var paths = new ArrayList<EnemyPath>();
        var path = new EnemyPath();
        path.add(Point.of(0, 125));
        path.add(Point.of(475, 125));
        path.add(Point.of(475, 525));
        path.add(Point.of(225, 525));
        path.add(Point.of(225, 775));
        path.add(Point.of(975, 775));
        path.add(Point.of(975, 375));
        path.add(Point.of(675, 375));
        path.add(Point.of(675, 225));
        path.add(Point.of(1400, 225));
        path.prepareForUsage();
        paths.add(path);
        return paths;
    }

    @Override
    public String getBackgroundTexturePath() {
        return "GameBasePath.png";
    }

    @Override
    public Integer getGridSize() {
        return 50;
    }
}
