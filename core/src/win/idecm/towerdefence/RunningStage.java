package win.idecm.towerdefence;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RunningStage {
    StageKind kind;
    Texture background;
    List<RunningEnemy> enemies;
    List<Tower> runningTowers;
    Resources resources;
    List<EnemyPath> savedPaths;

    Texture enemyTexture;

    private int currPathIdx = 0;

    public RunningStage(StageKind kind, Resources initialResources) {
        this.kind = kind;
        background = new Texture(kind.getBackgroundTexturePath());
        enemies = new ArrayList<>();
        runningTowers = new ArrayList<>();
        resources = initialResources;
        savedPaths = kind.getPaths();
//        enemyTexture = new Texture("towerDefense_tilesheet@2.png");
        enemyTexture = new Texture("badlogic.jpg");
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

    public void spawnEnemy(EnemyKind kind) {
        var spawnIndex = currPathIdx;
        currPathIdx += 1;
        if (currPathIdx == getPaths().size()) {
            currPathIdx = 0;
        }

        enemies.add(new RunningEnemy(kind, spawnIndex));
    }

    public void updateEnemies(double timeDelta) {
        // TODO: Collisions with projectiles, as well as projectiles
        for(var enemy : enemies) {
            enemy.move(timeDelta);
        }
    }

    public class EnemyRenderInfo {
        public Point position;
        public TextureRegion textureRegion;
        EnemyRenderInfo(Point p, TextureRegion tr) {
            position = p;
            textureRegion = tr;
        }
    };

    public Stream<EnemyRenderInfo> getEnemies () {
        return enemies.stream().map(runningEnemy -> new EnemyRenderInfo(
            savedPaths.get(runningEnemy.getPathIndex()).getPointAtLength(runningEnemy.getPosition()),
//                new TextureRegion(enemyTexture, 2944, 1664, 128, 128)
            new TextureRegion(enemyTexture)
        ));
    }
}
