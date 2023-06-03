package win.idecm.towerdefence;

import com.badlogic.gdx.graphics.Texture;

import java.util.List;

public class RunningTower {
    TowerKind kind;
    Point location;
    Texture texture;

    RunningTower(TowerKind kind, Point location) {
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
        return location;
    }
}
