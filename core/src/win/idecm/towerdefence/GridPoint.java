package win.idecm.towerdefence;

import java.util.Objects;

public class GridPoint {
    int x;
    int y;

    public GridPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static GridPoint of(int x, int y) {
        return new GridPoint(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "GridPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GridPoint gridPoint = (GridPoint) o;
        return getX() == gridPoint.getX() && getY() == gridPoint.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}
