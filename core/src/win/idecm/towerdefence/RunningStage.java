package win.idecm.towerdefence;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RunningStage {
    public RunningStage(StageKind kind, Resources initialResources) {
        this.kind = kind;
        background = new Texture(kind.getBackgroundTexturePath());
        enemies = new ArrayList<>();
        runningTowers = new ArrayList<>();
        resources = initialResources;
    }

    public void dispose() {
        background.dispose();
    }

    StageKind kind;
    Texture background;
    List<Enemy> enemies;
    List<Tower> runningTowers;
    Resources resources;
}
