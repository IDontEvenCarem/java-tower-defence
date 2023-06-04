package win.idecm.towerdefence;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import win.idecm.towerdefence.enemies.TestEnemy;

import java.util.*;
import java.util.stream.Stream;

public class RunningStage {
    StageKind kind;
    Texture background;
    List<RunningEnemy> enemies;

    Map<GridPoint, RunningTower> runningTowers;

    Resources resources;
    List<EnemyPath> savedPaths;
    double maxPathLength;

    Texture enemyTexture;

    private Set<GridPoint> bannedGridPoints;

    int gridWidth;
    int gridHeight;

    private int currPathIdx = 0;

    private int enemiesSpawned = 0;
    private double timeToSpawn = 0;


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
        runningTowers = new HashMap<>();
        resources = initialResources;
        savedPaths = kind.getPaths();
        maxPathLength = savedPaths.stream().mapToDouble(EnemyPath::getTotalLength).max().orElse(0) + 1; // 1 for enemies to leave the stage
        enemyTexture = new Texture("EnemyArcher.png");
        gridWidth = background.getWidth() / kind.getGridSize();
        gridHeight = background.getHeight() / kind.getGridSize();
        bannedGridPoints = kind.getBannedGridPoints();
        
        banPathGridPoints();
    }

    public void update(double timeDelta) {
        timeToSpawn += timeDelta;
        if (timeToSpawn > 1.0) {
            timeToSpawn -= 1.0;
            spawnEnemy(new TestEnemy()).applyLifeMultiplier(1.0 + enemiesSpawned/100.0);
            enemiesSpawned += 1;
        }

        processTowers(timeDelta);
        updateEnemies(timeDelta);
    }

    private void processTowers(double timeDelta) {
        runningTowers.entrySet().forEach(gridPointRunningTowerEntry -> {
            var tower = gridPointRunningTowerEntry.getValue();
            var enemiesInRange = new ArrayList<RunningEnemy>();
            enemies.forEach(enemy -> {
                var enemyLoc = getEnemyLocation(enemy);
                if (enemyLoc.distanceTo(tower.getLocation()) <= tower.getRange()) {
                    enemiesInRange.add(enemy);
                }
            });
            tower.onGameTickWithEnemies(timeDelta, enemiesInRange);
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

    public boolean tryPurchasingTower(TowerKind kind, GridPoint location) {
        if (runningTowers.containsKey(location)) {
            return false;
        }

        if (getResources().hasMoneyToBuy(kind.getBasePrice())) {
            getResources().spendMoney(kind.getBasePrice());
            runningTowers.put(location, new RunningTower(kind, location));
            return true;
        } else {
            return false;
        }
    }

    public RunningEnemy spawnEnemy(EnemyKind kind) {
        var spawnIndex = currPathIdx;
        currPathIdx += 1;
        if (currPathIdx == getPaths().size()) {
            currPathIdx = 0;
        }

        var enemy = new RunningEnemy(kind, spawnIndex);
        enemies.add(enemy);
        return enemy;
    }

    public void updateEnemies(double timeDelta) {
        var toBeRemoved = new ArrayList<RunningEnemy>();
        for (RunningEnemy enemy : enemies) {
            enemy.move(timeDelta);
            if (enemy.getHealth() <= 0) {
                toBeRemoved.add(enemy);
                onEnemyKilled();
            }
            if (enemy.getPosition() > maxPathLength) {
                toBeRemoved.add(enemy);
                onEnemyLeaked();
            }
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

    public Collection<RunningTower> getRunningTowers() {
        return runningTowers.values();
    }

    public Set<GridPoint> getBannedGridPoints() {
        return bannedGridPoints;
    }

    private void banPathGridPoints() {
        getPaths().forEach(path -> {
            for(double i = 0; i < path.getTotalLength(); i += 0.5) {
                var point = GridPoint.of(path.getPointAtLength(i));
                bannedGridPoints.add(point);
            }
        });
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
