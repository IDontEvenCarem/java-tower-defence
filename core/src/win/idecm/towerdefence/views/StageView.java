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

import java.util.Optional;

import static com.badlogic.gdx.Gdx.input;

public class StageView implements GameView, InputProcessor {
    static final int MAP_WIDTH = 14000;
    static final int TOTAL_HEIGHT = 9000;
    static final int UI_WIDTH = 4000;

    private int mouse_x = 0;
    private int mouse_y = 0;

    private double previewPointOffset = 0.0;

    SpriteBatch batch;
    RunningStage runningStage;
    Camera camera;
    Viewport viewport;
    ShapeRenderer shapeRenderer;
    BitmapFont font;

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
    }

    public Point fixMapPoint(Point input) {
        var mapAlignScale = ((float) runningStage.getBackground().getHeight()) / TOTAL_HEIGHT;
        return Point.of(input.getX() / mapAlignScale, TOTAL_HEIGHT - input.getY() / mapAlignScale);
    }

    @Override
    public Optional<GameView> render() {
        runningStage.updateEnemies(Gdx.graphics.getDeltaTime());

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();
        batch.draw(runningStage.getBackground(), 0, 0, MAP_WIDTH, TOTAL_HEIGHT);
        batch.end();

        drawGrid();
        renderEnemies();
        drawUI();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        var hovered = viewport.unproject(new Vector2(mouse_x, mouse_y));
        shapeRenderer.circle(hovered.x, hovered.y, 1000.0f);
        shapeRenderer.end();

        return Optional.empty();
    }

    private void drawUI() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.OLIVE);
        shapeRenderer.rect(MAP_WIDTH, 0, UI_WIDTH, TOTAL_HEIGHT);
        shapeRenderer.end();
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
        batch.begin();
        runningStage.getEnemies().forEach(renderInfo -> {
            System.out.println(renderInfo.position.getX() + " " + renderInfo.position.getY());
            var fixedPos = fixMapPoint(renderInfo.position);
            batch.draw(renderInfo.textureRegion,
                    (float) (fixedPos.getX() - 128),
                    (float) (fixedPos.getY() - 128),
                    512, 512
            );

            String healthText = "HP: " + renderInfo.enemy.getHealth();
            float textX = (float) (fixedPos.getX() - 64);
            float textY = (float) (fixedPos.getY() - 256);
            font.getData().setScale(10.0f);
            font.draw(batch, healthText, textX, textY);
        });
        batch.end();
    }

    private void renderTowers() {

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
        return false;
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
        mouse_x = screenX;
        mouse_y = screenY;
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
