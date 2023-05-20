package win.idecm.towerdefence;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class EnemyPath {
    public EnemyPath() {
        points = new ArrayList<>();
        cummulativeDistances = new ArrayList<>();
    }

    public List<Point> points;
    private List<Double> cummulativeDistances;

    public void add(Point p) {
        points.add(p);
    }

    public double getTotalLength() {
        return cummulativeDistances.get(cummulativeDistances.size()-1);
    }

    public void prepareForUsage() {
        if (points.size() < 2) return;
        cummulativeDistances.clear();

        double distance = 0.0;
        var prevPoint = points.get(0);
        for(var point : points) {
            var localDistance = prevPoint.distanceTo(point);
            cummulativeDistances.add(distance += localDistance);
            prevPoint = point;
        }
    }

    public Point getPointAtLength(double length) {
        // for complex path - binary search here?
        int idx = 0;
        for(;idx < cummulativeDistances.size() - 1; idx++) {
            if (length < cummulativeDistances.get(idx)) {
                break;
            }
        }
        if (idx == 0) {
            // special logic for underunning
            var offset = points.get(0).vectorTo(points.get(1));
            var scaledOffset = offset.multipliedBy(length / cummulativeDistances.get(1));
            return points.get(0).addVector(scaledOffset);
        }
        if (idx >= cummulativeDistances.get(cummulativeDistances.size()-1)) return points.get(cummulativeDistances.size()-1);
        var prevPoint = points.get(idx - 1);
        var currPoint = points.get(idx);
        var distanceBetweenPoints = cummulativeDistances.get(idx) - cummulativeDistances.get(idx-1);
        var distanceToHere = length - cummulativeDistances.get(idx-1);
        var offset = prevPoint.vectorTo(currPoint).multipliedBy(distanceToHere/distanceBetweenPoints);
        return prevPoint.addVector(offset);
    }
}
