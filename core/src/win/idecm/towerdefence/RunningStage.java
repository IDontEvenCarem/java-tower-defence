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

    List<RunningTower> runningTowers;

    Resources resources;
    List<EnemyPath> savedPaths;
    double maxPathLength;

    Texture enemyTexture;

    int gridWidth;
    int gridHeight;

    double fdPassiveHit = 0;

    private int currPathIdx = 0;

    public int getGridHeight() {
        return gridHeight;
    }
    public int getGridWidth() {
        return gridWidth;
    }

    public RunningStage(StageKind kind, Resources initialResources) {
        this.kind = kind;
        background = new Texture(kind.getBackgroundTexturePath());
        enemies = new ArrayList<>();
        runningTowers = new ArrayList<>();
        resources = initialResources;
        savedPaths = kind.getPaths();
        maxPathLength = savedPaths.stream().mapToDouble(EnemyPath::getTotalLength).max().orElse(0) + 128; // 128 for enemies to leave the stage
        enemyTexture = new Texture("EnemyArcher.png");
        gridWidth = background.getWidth() / kind.getGridSize();
        gridHeight = background.getHeight() / kind.getGridSize();
    }

    public void update(double timeDelta) {
        processTowers(timeDelta);
        updateEnemies(timeDelta);
    }

    private void processTowers(double timeDelta) {
        runningTowers.forEach(tower -> {
            var enemiesInRange = new ArrayList<RunningEnemy>();
            enemies.forEach(enemy -> {
                var enemyLoc = getEnemyLocation(enemy);
                if (enemyLoc.distanceTo(tower.location) <= tower.getRange()) {
                    enemiesInRange.add(enemy);
                }
            });
            tower.onGameTickWithEnemies(enemiesInRange);
        });
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

    public boolean tryPurchasingTower(TowerKind kind, Point location) {
        if (getResources().hasMoneyToBuy(kind.getBasePrice())) {
            getResources().spendMoney(kind.getBasePrice());
            runningTowers.add(new RunningTower(kind, location));
            return true;
        } else {
            return false;
        }
    }

    public void spawnEnemy(EnemyKind kind) {
        var spawnIndex = currPathIdx;
        currPathIdx += 1;
        if (currPathIdx == getPaths().size()) {
            currPathIdx = 0;
        }

        int damagePerSecond = 1;
        enemies.add(new RunningEnemy(kind, spawnIndex, damagePerSecond));
    }

    public void updateEnemies(double timeDelta) {
        var toBeRemoved = new ArrayList<RunningEnemy>();
        fdPassiveHit += timeDelta;
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
        getResources().loseLife(1);
    }

    private void onEnemyKilled() {
        getResources().gainMoney(10);
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

    public List<RunningTower> getRunningTowers() {
        return runningTowers;
    }

    public static class EnemyRenderInfo {
        public Point position;
        public TextureRegion textureRegion;
        public RunningEnemy enemy;
        public double size;

        public EnemyRenderInfo(Point p, TextureRegion tr, RunningEnemy enemy) {
            position = p;
            textureRegion = tr;
            this.enemy = enemy;
            this.size = enemy.getSize();
        }
    }

    public Stream<EnemyRenderInfo> getEnemies() {
        return enemies.stream().map(runningEnemy -> new EnemyRenderInfo(
                getEnemyLocation(runningEnemy),
                new TextureRegion(enemyTexture),
                runningEnemy
        ));
    }

    public Point getEnemyLocation (RunningEnemy enemy) {
        return savedPaths.get(enemy.getPathIndex()).getPointAtLength(enemy.getPosition());
    }
}
