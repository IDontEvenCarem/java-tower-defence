package win.idecm.towerdefence;

public class Point {
    private int x;
    private int y;

    public Point (int x, int y) {
        this.x = x;
        this.y = y;
    }

    static public Point of(int x, int y) {
        return new Point(x, y);
    }

    public Point dividedBy(int v) {
        return new Point(x / v, y / v);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
