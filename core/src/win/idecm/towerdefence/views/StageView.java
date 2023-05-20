package win.idecm.towerdefence.views;

import com.badlogic.gdx.Gdx;
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
import win.idecm.towerdefence.GameView;
import win.idecm.towerdefence.Resources;
import win.idecm.towerdefence.RunningStage;
import win.idecm.towerdefence.stages.TestStage;

import java.util.Optional;

public class StageView implements GameView, InputProcessor {
    static final int T_WIDTH = 16000;
    static final int T_HEIGHT = 9000;

    private int mouse_x = 0;
    private int mouse_y = 0;

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
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public Optional<GameView> render() {
        camera.update();
        ScreenUtils.clear(0, 0, 0, 1);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(runningStage.getBackground(), 0, 0, T_WIDTH, T_HEIGHT);
        batch.end();

        var mapAlignScale = ((float) runningStage.getBackground().getHeight()) / T_HEIGHT;

        runningStage.getPaths().forEach(enemyPath -> {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            for(int i = 0; i+1 < enemyPath.points.size(); i++) {
                var a = enemyPath.points.get(i);
                var b = enemyPath.points.get(i+1);
                // Inside-image and outside-image coordinates are flipped (lib draws images upside down),
                // so we need to flip coorinates
                shapeRenderer.line(
                    a.getX()/mapAlignScale,
                    T_HEIGHT - a.getY()/mapAlignScale,
                    b.getX()/mapAlignScale,
                    T_HEIGHT - b.getY()/mapAlignScale
                );
            }
            shapeRenderer.end();
        });

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        var hovered = viewport.unproject(new Vector2(mouse_x, mouse_y));
        shapeRenderer.circle(hovered.x, hovered.y, 1000.0f);
        shapeRenderer.end();

        return Optional.empty();
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