package win.idecm.towerdefence;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.List;
import java.util.Optional;

abstract public class Tower {
    GridPoint location;
    float time = 0;

    public abstract String getName();
    public abstract Texture getTexture();
    public abstract int getBasePrice();
    public abstract double getBaseRange();
    abstract public void update(double timeDelta, List<EnemyWithPositioning> enemiesInRange);

    public Tower(GridPoint location) {
        this.location = location;
    }

    public double getRange () {
        return getBaseRange();
    }

    static public class EnemyWithPositioning {
        public RunningEnemy enemy;
        public Point position;

        public EnemyWithPositioning(RunningEnemy enemy, Point position) {
            this.enemy = enemy;
            this.position = position;
        }
    }

    public GridPoint getLocation() {
        return location;
    }

    public Point getCenterPoint() {
        return Point.of(getLocation()).addVector(Point.of(0.5, -0.5));
    }

    public void drawBatchEffects(Point renderLocation, int gridSize, SpriteBatch b) {}

    public void drawShapeEffects(Point renderLocation, int gridSize, ShapeRenderer sr) {}

    public float getTime() {
        return time;
    }

    Optional<RunningEnemy> pickClosest(List<EnemyWithPositioning> enemies) {
        return enemies.stream().min((e1, e2) -> {
            var d1 = getCenterPoint().distanceSquaredTo(e1.position);
            var d2 = getCenterPoint().distanceSquaredTo(e2.position);
            return Double.compare(d1, d2);
        }).map(enemyWithPositioning -> enemyWithPositioning.enemy);
    };
}
