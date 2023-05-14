package win.idecm.towerdefence;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import win.idecm.towerdefence.stages.TestStage;
import win.idecm.towerdefence.views.MainMenuView;
import win.idecm.towerdefence.views.StageView;

public class Game extends ApplicationAdapter {

	private GameView currentView;

	@Override
	public void create () {
		currentView = new MainMenuView(new RunningStage(new TestStage(), new Resources()));
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