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
        path.add(Point.of(1300,  500));
        path.add(Point.of(1400, 800));
        path.add(Point.of(1400, 2800));

        return paths;
    }

    @Override
    public String getBackgroundTexturePath() {
        return "TD_Map_Test_1.png";
    }
}
