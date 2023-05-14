package win.idecm.towerdefence.views;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import win.idecm.towerdefence.GameView;
import win.idecm.towerdefence.Resources;
import win.idecm.towerdefence.RunningStage;
import win.idecm.towerdefence.stages.TestStage;

public class StageView implements GameView {
    SpriteBatch batch;
    RunningStage runningStage;
    Camera camera;
    Viewport viewport;

    public StageView(RunningStage stage) {
        runningStage = stage;
    }

    @Override
    public void initialize() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
    }

    @Override
    public void render() {
        camera.update();
        ScreenUtils.clear(0, 0, 0, 1);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(runningStage.getBackground(), 0, 0, 1920, 1080);
        batch.end();
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
}
