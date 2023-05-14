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
}
