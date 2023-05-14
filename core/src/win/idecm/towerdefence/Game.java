package win.idecm.towerdefence;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import win.idecm.towerdefence.stages.TestStage;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	RunningStage runningStage;
	Camera camera;
	Viewport viewport;
	Stage stage;

	@Override
	public void create () {
		stage = new Stage();
		batch = new SpriteBatch();
		runningStage = new RunningStage(new TestStage(), new Resources());
		camera = new OrthographicCamera();
		viewport = new FitViewport(1920, 1080, camera);
		viewport.apply();
		camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
	}

	@Override
	public void render () {
		camera.update();
		ScreenUtils.clear(0, 0, 0, 1);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(runningStage.background, 0, 0, 1920, 1080);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		runningStage.dispose();
	}

	@Override
	public void resize(int width, int height){
		viewport.update(width,height);
		camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
	}
}
