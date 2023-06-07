package win.idecm.towerdefence.stages;

import win.idecm.towerdefence.EnemyPath;
import win.idecm.towerdefence.GridPoint;
import win.idecm.towerdefence.Point;
import win.idecm.towerdefence.StageKind;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TestStage implements StageKind {
    @Override
    public String getName() {
        return "Test Stage";
    }

    @Override
    public String getDescription() {
        return "A simple stage, with some twists and turns, designed to test out features";
    }

    @Override
    public List<EnemyPath> getPaths() {
        var paths = new ArrayList<EnemyPath>();
        var path = new EnemyPath(getGridSize());
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
    public int getGridSize() {
        return 50;
    }

    @Override
    public Set<GridPoint> getBannedGridPoints() {
        var hs = StageKind.super.getBannedGridPoints();
        // lake
        for (int i = 5; i <= 18; i++) {
            hs.add(GridPoint.of(0, i));
            hs.add(GridPoint.of(1, i));
        }
        for (int i = 5; i <= 12; i++) {
            hs.add(GridPoint.of(2, i));
        }
        for (int i = 5; i <= 8; i++) {
            hs.add(GridPoint.of(3, i));
            hs.add(GridPoint.of(4, i));
            hs.add(GridPoint.of(5, i));
        }
        hs.add(GridPoint.of(6, 6));
        hs.add(GridPoint.of(6, 7));
        //rock
        for(var x = 0; x < 5; x++) {
            for(var y = 0; y < 5; y++) {
                hs.add(GridPoint.of(22+x, 7+y));
            }
        }
        hs.remove(GridPoint.of(22, 7+4));
        return hs;
    }
}
