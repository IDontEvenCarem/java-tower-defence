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
import win.idecm.towerdefence.views.StageView;

public class Game extends ApplicationAdapter {
	GameView currentView;

	@Override
	public void create () {
		currentView = new StageView(new RunningStage(new TestStage(), new Resources()));
		currentView.initialize();
	}

	@Override
	public void render () {
		currentView.render();
	}
	
	@Override
	public void dispose () {
		currentView.dispose();
	}

	@Override
	public void resize(int width, int height){
		currentView.resize(width, height);
	}
}
