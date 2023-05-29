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
        enemyTexture = new Texture("EnemyArcher.png");
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

        int initialHealth = 1000;
        int damagePerSecond = 1;
        enemies.add(new RunningEnemy(kind, spawnIndex, initialHealth, damagePerSecond));
    }

    public void updateEnemies(double timeDelta) {
        for (var enemy : enemies) {
            enemy.move(timeDelta);

            enemy.decreaseHealth(1);
        }
    }

    public class EnemyRenderInfo {
        public Point position;
        public TextureRegion textureRegion;
        public RunningEnemy enemy;

        public EnemyRenderInfo(Point p, TextureRegion tr, RunningEnemy enemy) {
            position = p;
            textureRegion = tr;
            this.enemy = enemy;
        }
    }

    public Stream<EnemyRenderInfo> getEnemies() {
        return enemies.stream().map(runningEnemy -> new EnemyRenderInfo(
                savedPaths.get(runningEnemy.getPathIndex()).getPointAtLength(runningEnemy.getPosition()),
                new TextureRegion(enemyTexture),
                runningEnemy
        ));
    }
}
