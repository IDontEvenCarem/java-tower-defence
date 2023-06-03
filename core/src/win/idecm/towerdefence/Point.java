package win.idecm.towerdefence;

import com.badlogic.gdx.math.Vector2;

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
    static public Point of(double v) { return new Point(v,v); }
    static public Point of(Vector2 v) { return new Point(v.x, v.y); }

    public String toString() {
        return "Point{X: " + getX() + ", Y:" + getY() + "}";
    }

    public Point dividedBy(double v) {
        return new Point(x / v, y / v);
    }

    public Point multipliedBy(double v) {
        return new Point(x*v, y*v);
    }

    public Point hadamard(double x, double y) {
        return new Point(getX() * x, getY() * y);
    }

    public Point hadamard(Point other) {
        return new Point(getX() * other.getX(), getY() * other.getY());
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

    public Vector2 toVector2() {
        return new Vector2((float) getX(), (float) getY());
    }
}
