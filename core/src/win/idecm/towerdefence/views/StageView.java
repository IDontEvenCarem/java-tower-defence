package win.idecm.towerdefence.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import win.idecm.towerdefence.*;
import win.idecm.towerdefence.enemies.TestEnemy;
import win.idecm.towerdefence.stages.TestStage;

import java.awt.event.KeyEvent;
import java.util.Optional;

import static com.badlogic.gdx.Gdx.input;

public class StageView implements GameView, InputProcessor {
    static final int T_WIDTH = 16000;
    static final int T_HEIGHT = 9000;

    private int mouse_x = 0;
    private int mouse_y = 0;

    private double previewPointOffset = 0.0;

    SpriteBatch batch;
    RunningStage runningStage;
    Camera camera;
    Viewport viewport;
    ShapeRenderer shapeRenderer;

    public StageView(RunningStage stage) {
        runningStage = stage;
    }

    @Override
    public void initialize() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(T_WIDTH, T_HEIGHT, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
        shapeRenderer = new ShapeRenderer();
        input.setInputProcessor(this);
    }

    public Point fixMapPoint (Point input) {
        var mapAlignScale = ((float) runningStage.getBackground().getHeight()) / T_HEIGHT;
        return Point.of(input.getX()/mapAlignScale, T_HEIGHT - input.getY()/mapAlignScale);
    }

    @Override
    public Optional<GameView> render() {
        runningStage.updateEnemies(Gdx.graphics.getDeltaTime());

        camera.update();
        ScreenUtils.clear(0, 0, 0, 1);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(runningStage.getBackground(), 0, 0, T_WIDTH, T_HEIGHT);
        var mapAlignScale = ((float) runningStage.getBackground().getHeight()) / T_HEIGHT;
        renderEnemies();
        batch.end();


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        var hovered = viewport.unproject(new Vector2(mouse_x, mouse_y));
        shapeRenderer.circle(hovered.x, hovered.y, 1000.0f);
        shapeRenderer.end();

        return Optional.empty();
    }

    private void renderEnemies() {
        runningStage.getEnemies().forEach(renderInfo -> {
            System.out.println(renderInfo.position.getX() + " " + renderInfo.position.getY());
            var fixedPos = fixMapPoint(renderInfo.position);
            batch.draw(renderInfo.textureRegion,
                (float) (fixedPos.getX() - 128),
                (float) (fixedPos.getY() - 128),
                256, 256
            );
        });
    }

    private void renderTowers() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        runningStage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
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
