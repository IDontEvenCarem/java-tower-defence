package win.idecm.towerdefence;

public class Point {
    private double x;
    private double y;

    public Point (double x, double y) {
        this.x = x;
        this.y = y;
    }

    static public Point of(double x, double y) {
        return new Point(x, y);
    }

    public Point dividedBy(double v) {
        return new Point(x / v, y / v);
    }

    public Point multipliedBy(double v) {
        return new Point(x*v, y*v);
    }
    
    public double distanceTo(Point other) {
        var dx = getX() - other.getX();
        var dy = getY() - other.getY();
        return Math.sqrt(dx*dx + dy*dy);
    }

    public Point addVector(Point vector) {
        return new Point(getX() + vector.getX(), getY() + vector.getY());
    }

    public Point vectorTo(Point other) {
        return new Point(other.getX() - getX(), other.getY() - getY());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
