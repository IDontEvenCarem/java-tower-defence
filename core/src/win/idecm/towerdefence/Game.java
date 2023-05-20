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
	private int lastResizeW = 0;
	private int lastResizeH = 0;

	@Override
	public void create () {
		currentView = new MainMenuView(new RunningStage(new TestStage(), new Resources()));
		currentView.initialize();
	}

	@Override
	public void render () {
		var newView = currentView.render();
		if (newView.isPresent()) {
			currentView.dispose();
			currentView = newView.get();
			currentView.initialize();
			// once a view changes, we still want it to get the sizing info through resize
			currentView.resize(lastResizeW, lastResizeH);
		}
	}

	@Override
	public void dispose () {
		currentView.dispose();
	}

	@Override
	public void resize(int width, int height){
		lastResizeW = width;
		lastResizeH = height;
		currentView.resize(width, height);
	}
}