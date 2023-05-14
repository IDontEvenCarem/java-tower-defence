package win.idecm.towerdefence;

import java.util.ArrayList;
import java.util.List;

public class EnemyPath {
    public EnemyPath() {
        points = new ArrayList<>();
    }

    public List<Point> points;

    public void add(Point p) {
        points.add(p);
    }

    public void prepareForUsage() {

    }
}
