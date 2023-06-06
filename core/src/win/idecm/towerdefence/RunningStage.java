package win.idecm.towerdefence;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import win.idecm.towerdefence.enemies.DarkMage;
import win.idecm.towerdefence.enemies.Goblin;
import win.idecm.towerdefence.enemies.TestEnemy;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class RunningStage {
    private static final double GOBLIN_SPAWN_INTERVAL = 0.25;
    private static final double DARK_MAGE_SPAWN_INTERVAL = 60.0;
    private static final double GOBLIN_WAVE_INTERVAL = 25.0;
    StageKind kind;
    Texture background;
    List<RunningEnemy> enemies;

    Map<GridPoint, Tower> runningTowers;

    Resources resources;
    List<EnemyPath> savedPaths;
    double maxPathLength;

    private Set<GridPoint> bannedGridPoints;
    private List<Projectile> projectiles;

    int gridWidth;
    int gridHeight;

    private int currPathIdx = 0;

    private int enemiesSpawned = 0;
    private double timeToSpawn = 0;

    private int goblinWaveCount = 0;
    private double goblinWaveInnerSpawnCounter = 0.0;
    private double goblinWaveProgress = 10.0; // make the first wave appear earlier
    private int goblinWavesDone = 0;


    private int darkMagesSpawned = 0;
    private double darkMageProgress = 0.0;
    private Function<Point, Point> renderabler;

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
        gridWidth = background.getWidth() / kind.getGridSize();
        gridHeight = background.getHeight() / kind.getGridSize();
        bannedGridPoints = kind.getBannedGridPoints();
        projectiles = new ArrayList<>();
        banPathGridPoints();
    }

    public void update(double timeDelta) {
        doEnemySpawning(timeDelta);
        processTowers(timeDelta);
        updateProjectiles(timeDelta);
        updateEnemies(timeDelta);
    }

    private void doEnemySpawning(double timeDelta) {
        goblinWaveProgress += timeDelta;
        if (goblinWaveProgress > GOBLIN_WAVE_INTERVAL) {
            goblinWaveCount = 3 + (int)(2*Math.pow(goblinWavesDone, 1.1));
            goblinWaveProgress = 0.0;
        }

        if (goblinWaveCount > 0) {
            goblinWaveInnerSpawnCounter += timeDelta;
            if (goblinWaveInnerSpawnCounter > GOBLIN_SPAWN_INTERVAL) {
                spawnEnemy(new Goblin()).applyLifeMultiplier(1 + 0.2*goblinWavesDone);
                goblinWaveCount -= 1;
                if (goblinWaveCount == 0) {
                    goblinWavesDone += 1;
                }
                goblinWaveInnerSpawnCounter -= GOBLIN_SPAWN_INTERVAL;
            }
        } else {
            goblinWaveInnerSpawnCounter = 0;
        }

        timeToSpawn += timeDelta;
        if (timeToSpawn > 1.0) {
            timeToSpawn -= 1.0;
            spawnEnemy(new TestEnemy()).applyLifeMultiplier(1.0 + Math.max(0, (enemiesSpawned-20)/50.0));
            enemiesSpawned += 1;
        }

        darkMageProgress += timeDelta;
        if (darkMageProgress > DARK_MAGE_SPAWN_INTERVAL) {
            spawnEnemy(new DarkMage()).applyLifeMultiplier(1 + 0.5 * darkMagesSpawned);
            darkMagesSpawned += 1;
            darkMageProgress = 0.0;
        }
    }

    private void updateProjectiles(double timeDelta) {
        var removalList = new ArrayList<Projectile>();
        for(var proj : projectiles) {
            var enemiesInRange = new ArrayList<Tower.EnemyWithPositioning>();
            enemies.forEach(enemy -> {
                var enemyLoc = getEnemyLocation(enemy);
                var towerLoc = proj.getPosition();
                if (enemyLoc.distanceSquaredTo(towerLoc) <= proj.getSquaredColiderSize()) {
                    enemiesInRange.add(new Tower.EnemyWithPositioning(enemy, enemyLoc));
                }
            });

            var wantToDelete = proj.update(timeDelta, enemiesInRange);
            if (wantToDelete) {
                removalList.add(proj);
            }
        }
        projectiles.removeAll(removalList);
    }

    private void processTowers(double timeDelta) {
        runningTowers.entrySet().forEach(gridPointRunningTowerEntry -> {
            var tower = gridPointRunningTowerEntry.getValue();
            var enemiesInRange = new ArrayList<Tower.EnemyWithPositioning>();
            enemies.forEach(enemy -> {
                var enemyLoc = getEnemyLocation(enemy);
                var towerLoc = tower.getCenterPoint();
                if (enemyLoc.distanceTo(towerLoc) <= tower.getRange() + 0.05) {
                    enemiesInRange.add(new Tower.EnemyWithPositioning(enemy, enemyLoc));
                }
            });
            var proj = tower.update(timeDelta, enemiesInRange);
            if (proj.isPresent()) {
                projectiles.addAll(proj.get());
            }
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

    public boolean tryPurchasingTower(Tower tower) {
        var location = tower.getLocation();
        if (runningTowers.containsKey(location)) {
            return false;
        }

        if (getResources().hasMoneyToBuy(tower.getBasePrice())) {
            getResources().spendMoney(tower.getBasePrice());
            runningTowers.put(location, tower);
            tower.injectRenderabler(renderabler);
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

    public Collection<Tower> getRunningTowers() {
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

    public Collection<Projectile> getProjectiles() {
        return projectiles;
    }

    public static class EnemyRenderInfo {
        public Point position;
        public RunningEnemy enemy;
        public double size;

        public EnemyRenderInfo(Point p, RunningEnemy enemy) {
            position = p;
            this.enemy = enemy;
            this.size = enemy.getSize();
        }
    }

    public Stream<EnemyRenderInfo> getEnemies() {
        return enemies.stream().map(runningEnemy -> new EnemyRenderInfo(
                getEnemyLocation(runningEnemy),
                runningEnemy
        ));
    }

    public Point getEnemyLocation (RunningEnemy enemy) {
        return savedPaths.get(enemy.getPathIndex()).getPointAtLength(enemy.getPosition());
    }

    public void injectRenderabler (Function<Point, Point> toRenderable) {
        this.renderabler = toRenderable;
    }
}
