package win.idecm.towerdefence;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

public class RunningStage {
    StageKind kind;
    Texture background;
    List<EnemyKind> enemies;
    List<Tower> runningTowers;
    Resources resources;
    List<EnemyPath> savedPaths;

    public RunningStage(StageKind kind, Resources initialResources) {
        this.kind = kind;
        background = new Texture(kind.getBackgroundTexturePath());
        enemies = new ArrayList<>();
        runningTowers = new ArrayList<>();
        resources = initialResources;
        savedPaths = kind.getPaths();
    }

    public void dispose() {
        background.dispose();
    }


    public Texture getBackground() {
        return background;
    }

    public List<EnemyPath> getPaths() {
        return savedPaths;
    }
}
