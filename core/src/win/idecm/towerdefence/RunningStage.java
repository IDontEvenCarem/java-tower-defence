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


    public Texture getBackground() {
        return background;
    }

    private GameView gameView;

    public void setGameView(GameView gameView) {
        if (this.gameView != null) {
            this.gameView.dispose();
        }
        this.gameView = gameView;
        if (this.gameView != null) {
            this.gameView.initialize();
        }
    }

}
