package win.idecm.towerdefence.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import win.idecm.towerdefence.*;
import win.idecm.towerdefence.enemies.TestEnemy;

import java.util.HashMap;
import java.util.Optional;

import static com.badlogic.gdx.Gdx.input;

public class StageView implements GameView, InputProcessor {
    // we want the total to be 16:9 aspect ratio
    static final int MAP_WIDTH = 1400;
    static final int TOTAL_HEIGHT = 900;
    static final int UI_WIDTH = 200;

    private float mouse_x = 0;
    private float mouse_y = 0;

    SpriteBatch batch;
    RunningStage runningStage;
    Camera camera;
    Viewport viewport;
    ShapeRenderer shapeRenderer;
    BitmapFont font;

    UIMenu uiMenu;

    public StageView(RunningStage stage) {
        runningStage = stage;
    }

    @Override
    public void initialize() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(MAP_WIDTH+UI_WIDTH, TOTAL_HEIGHT, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        input.setInputProcessor(this);
        shapeRenderer.setAutoShapeType(true);
        uiMenu = new UIMenu(MAP_WIDTH, UI_WIDTH, TOTAL_HEIGHT, runningStage.getResources(), runningStage.getBannedGridPoints());
    }

    public Point fixMapPoint(Point input) {
        var mapAlignScale = ((float) runningStage.getBackground().getHeight()) / TOTAL_HEIGHT;
        return Point.of(input.getX() / mapAlignScale, TOTAL_HEIGHT - input.getY() / mapAlignScale);
    }

    public Point unfixMapPoint(Point input) {
        var mapAlignScale = ((float) runningStage.getBackground().getHeight()) / TOTAL_HEIGHT;
        return Point.of(input.getX() * mapAlignScale, TOTAL_HEIGHT - input.getY() * mapAlignScale);
    }

    @Override
    public Optional<GameView> render() {
        runningStage.update(Gdx.graphics.getDeltaTime());

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();
        batch.draw(runningStage.getBackground(), 0, 0, MAP_WIDTH, TOTAL_HEIGHT);
        batch.end();

        drawGrid();
        renderEnemies();
        renderTowers();
        renderProjectiles();

        uiMenu.drawUi(runningStage, batch, shapeRenderer, hoveredGrid(), gridToRenderable(Point.of(hoveredGrid())), runningStage.getGridSize());

        return Optional.empty();
    }

    private Point screenToWorld (Point screen) {
        return Point.of(viewport.unproject(screen.toVector2()));
    }

    private Point worldToScreen (Point world) {
        return Point.of(viewport.project(world.toVector2()));
    }

    private Point gridToRenderable (Point grid) {
        return fixMapPoint(grid.multipliedBy(runningStage.getGridSize()));
    }

    private Point getRenderableGridSize () {
        return Point.of(runningStage.getGridSize(), runningStage.getGridSize());
    }

    private GridPoint hoveredGrid (float x, float y) {
        var gridSize = runningStage.getGridSize();
        var gx = Math.floor(x / gridSize);
        var gy = Math.ceil((TOTAL_HEIGHT - y) / gridSize);
        return GridPoint.of((int)gx, (int)gy);
    }
    private GridPoint hoveredGrid () {
        return hoveredGrid(mouse_x, mouse_y);
    }

    private void drawGrid() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));

        var gridSize = runningStage.getGridSize();
        var limitX = runningStage.getBackgroundWidth();
        var limitY = runningStage.getBackgroundHeight();

        for(int x = 0; x <= limitX; x += gridSize) {
            var a = fixMapPoint(Point.of(x, 0));
            var b = fixMapPoint(Point.of(x, limitY));
            shapeRenderer.line((float) a.getX(), (float) a.getY(), (float) b.getX(), (float) b.getY());
        }
        for(int y = 0; y <= limitY; y += gridSize) {
            var a = fixMapPoint(Point.of(0, y));
            var b = fixMapPoint(Point.of(limitX, y));
            shapeRenderer.line((float) a.getX(), (float) a.getY(), (float) b.getX(), (float) b.getY());
        }

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }


    private void renderEnemies() {
        var gridSize = getRenderableGridSize();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        runningStage.getEnemies().forEach(renderInfo -> {
            var renderPos = gridToRenderable(renderInfo.position.addVector(Point.of(-renderInfo.size/2, renderInfo.size/2)));
            batch.draw(renderInfo.textureRegion,
                    (float) (renderPos.getX()),
                    (float) (renderPos.getY()),
                    (float) (gridSize.getX() * renderInfo.size),
                    (float) (gridSize.getY() * renderInfo.size)
            );
        });
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        runningStage.getEnemies().forEach(renderInfo -> {
            var currentHealthFrac = renderInfo.enemy.getHealth() / renderInfo.enemy.getMaxHealth();
            var renderPos = gridToRenderable(renderInfo.position.addVector(Point.of(-renderInfo.size/2, renderInfo.size)));
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect((float) renderPos.getX(), (float) renderPos.getY(), (float) (gridSize.getX() * renderInfo.size), 10);
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect((float) renderPos.getX(), (float) renderPos.getY(), (float) (gridSize.getX() * renderInfo.size * currentHealthFrac), 10);
        });
        shapeRenderer.end();
    }

    private void renderTowers() {
        batch.begin();
        camera.combined.getScaleX();

        var renderedLocations = new HashMap<Tower, Point>();
        var gridSize = runningStage.getGridSize();

        runningStage.getRunningTowers().forEach(tower -> {
            var location = tower.getLocation();
            var renderable = gridToRenderable(Point.of(location));
            batch.draw(
                    tower.getTexture(),
                    (float) renderable.getX(), (float) renderable.getY(),
                    gridSize, gridSize
            );
            renderedLocations.put(tower, renderable);
        });

        runningStage.getRunningTowers().forEach(tower -> {
            tower.drawBatchEffects(renderedLocations.get(tower), gridSize, batch);
        });
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        runningStage.getRunningTowers().forEach(tower -> {
            tower.drawShapeEffects(renderedLocations.get(tower), gridSize, shapeRenderer);
        });
        shapeRenderer.end();
    }

    private void renderProjectiles() {
        var gridSize = runningStage.getGridSize();

        batch.begin();

        int i = 0;
        for(var proj : runningStage.getProjectiles()) {
            i++;
            var texture = proj.getTexture();
            var pos = gridToRenderable(proj.getPosition());
            var size = proj.getVisualSize();
            batch.draw(texture,
                (float) (pos.getX() - texture.getRegionWidth()/2),
                (float) (pos.getY() - texture.getRegionHeight()/2),
                (float) (texture.getRegionWidth()/2),
                (float) (texture.getRegionHeight()/2),
                (float) (gridSize * size),
                (float) (gridSize * size),
                1.0f,
                1.0f,
                (float) (-proj.getRadians() / Math.PI * 180.0f)
            );
        }
        System.out.println("Rendered " + i + " projectiles");

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        runningStage.dispose();
        font.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            runningStage.spawnEnemy(new TestEnemy());
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        var unprojected = viewport.unproject(new Vector2(screenX, screenY));
        var action = uiMenu.onClick(unprojected.x, unprojected.y);
        if (action.isPresent()) {
            action.get().accept(runningStage);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        var unprojected = viewport.unproject(new Vector2(screenX, screenY));
        mouse_x = unprojected.x;
        mouse_y = unprojected.y;
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (mouse_x > MAP_WIDTH) {
            uiMenu.scroll(amountY);
            return true;
        }
        return false;
    }
}
