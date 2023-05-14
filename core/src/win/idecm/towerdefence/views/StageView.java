package win.idecm.towerdefence.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
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
    ShapeRenderer shapeRenderer;

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
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render() {
        camera.update();
        ScreenUtils.clear(0, 0, 0, 1);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(runningStage.getBackground(), 0, 0, 1920, 1080);
        batch.end();

        var mapAlignScale = ((float) runningStage.getBackground().getHeight()) / 1080.0f;

        runningStage.getPaths().forEach(enemyPath -> {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            for(int i = 0; i+1 < enemyPath.points.size(); i++) {
                var a = enemyPath.points.get(i);
                var b = enemyPath.points.get(i+1);
                shapeRenderer.line(a.getX()/mapAlignScale, 1080 - a.getY()/mapAlignScale, b.getX()/mapAlignScale, 1080 - b.getY()/mapAlignScale);
            }
            shapeRenderer.end();
        });

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.circle(0, 0, 50);
//        shapeRenderer.end();

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
