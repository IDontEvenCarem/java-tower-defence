package win.idecm.towerdefence;

public class RunningEnemy {
    private final EnemyKind kind;
    private final int pathIndex;
    private double position;

    RunningEnemy(EnemyKind kind, int pathIndex) {
        this.kind = kind;
        this.pathIndex = pathIndex;
        this.position = 0.0;
    }

    public int getPathIndex () {
        return pathIndex;
    }

    public double getSpeed() {
        return kind.getSpeed();
    }

    public double getPosition() {
        return position;
    }

    public void move(double timeElapsed) {
        position += getSpeed() * timeElapsed;
    }
}
