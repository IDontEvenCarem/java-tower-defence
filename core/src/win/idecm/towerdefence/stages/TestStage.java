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
        path.add(Point.of(0, 500));
        path.add(Point.of(1275,  500));
        path.add(Point.of(1400, 800));
        path.add(Point.of(1400, 2800));
        path.add(Point.of(1500, 3150));
        path.add(Point.of(1600, 3200));
        path.add(Point.of(2600, 3200));
        path.add(Point.of(2700, 3100));
        path.add(Point.of(2700, 2100));
        path.add(Point.of(2550, 2000));
        path.add(Point.of(900, 2000));
        path.add(Point.of(700, 2200));
        path.add(Point.of(700, 3700));
        path.add(Point.of(1000, 3900));
        path.add(Point.of(3500, 3900));

        path.prepareForUsage();
        paths.add(path);
        return paths;
    }

    @Override
    public String getBackgroundTexturePath() {
        return "TD_Map_Test_1.png";
    }
}
