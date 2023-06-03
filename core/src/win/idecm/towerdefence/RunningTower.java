package win.idecm.towerdefence;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.List;

public class RunningTower {
    TowerKind kind;
    GridPoint location;
    Texture texture;

    RunningTower(TowerKind kind, GridPoint location) {
        this.kind = kind;
        this.location = location;
        texture = new Texture(kind.getTexturePath());
    }

    public double getRange () {
        return kind.getBaseRange();
    }

    public void onGameTickWithEnemies(double timeDelta, List<RunningEnemy> enemies) {
        kind.onGameTickWithEnemies(timeDelta, enemies);
    }

    public Texture getTexture() {
        return texture;
    }

    public Point getLocation() {
        return Point.of(location);
    }

    public void drawBatchEffects(Point renderLocation, int gridSize, SpriteBatch b) {
        kind.drawTextureEffects(this, renderLocation, gridSize, b);
    }

    public void drawShapeEffects(Point renderLocation, int gridSize, ShapeRenderer sr) {
        kind.drawShapeEffects(this, renderLocation, gridSize, sr);
    }
}
