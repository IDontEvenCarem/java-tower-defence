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
    double maxPathLength;

    Texture enemyTexture;

    double fdPassiveHit = 0;

    private int currPathIdx = 0;

    public RunningStage(StageKind kind, Resources initialResources) {
        this.kind = kind;
        background = new Texture(kind.getBackgroundTexturePath());
        enemies = new ArrayList<>();
        runningTowers = new ArrayList<>();
        resources = initialResources;
        savedPaths = kind.getPaths();
        maxPathLength = savedPaths.stream().mapToDouble(p -> p.getTotalLength()).max().orElse(0) + 128; // 128 for enemies to leave the stage
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

    public Resources getResources() {
        return resources;
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
        var toBeRemoved = new ArrayList<RunningEnemy>();
        fdPassiveHit += timeDelta;
        System.out.println(fdPassiveHit);
        for (RunningEnemy enemy : enemies) {
            enemy.move(timeDelta);
            if (fdPassiveHit >= 1.0) enemy.decreaseHealth(12);
            if (enemy.getHealth() <= 0) {
                toBeRemoved.add(enemy);
                onEnemyKilled();
            }
            if (enemy.getPosition() > maxPathLength) {
                toBeRemoved.add(enemy);
                onEnemyLeaked();
            }
        }
        if (fdPassiveHit >= 1.0) {
            fdPassiveHit -= 1.0;
        }
        enemies.removeAll(toBeRemoved);
    }

    private void onEnemyLeaked() {
    }

    private void onEnemyKilled() {
    }

    public int getGridSize() {
        return kind.getGridSize();
    }

    public int getBackgroundWidth() {
        return background.getWidth();
    }

    public int getBackgroundHeight() {
        return background.getHeight();
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
