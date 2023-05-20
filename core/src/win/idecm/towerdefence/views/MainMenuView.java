package win.idecm.towerdefence.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import win.idecm.towerdefence.GameView;
import win.idecm.towerdefence.Resources;
import win.idecm.towerdefence.RunningStage;
import win.idecm.towerdefence.stages.TestStage;

import java.util.Optional;

public class MainMenuView implements GameView {
    private static final float BUTTON_WIDTH = 569;
    private static final float BUTTON_HEIGHT = 183;
    private static final float EXIT_BUTTON_HEIGHT = 299;
    private static final float BUTTON_SPACING = 50;
    private static final float MARGIN_BOTTOM = 200;

    SpriteBatch batch;
    RunningStage runningStage;
    Camera camera;
    Viewport viewport;
    Texture background;
    Texture startButton;
    Texture exitButton;
    public Rectangle startButtonBounds;
    public Rectangle exitButtonBounds;

    public MainMenuView(RunningStage stage) {
        runningStage = stage;
    }

    @Override
    public void initialize() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);

        background = new Texture(Gdx.files.internal("MainMenuPanel.png"));
        startButton = new Texture(Gdx.files.internal("Start.png"));
        exitButton = new Texture(Gdx.files.internal("Exit.png"));
    }


    @Override
    public Optional<GameView> render() {
        camera.update();
        ScreenUtils.clear(0, 0, 0, 1);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, 1920, 1080);
        batch.draw(startButton, startButtonBounds.x, startButtonBounds.y, startButtonBounds.width, startButtonBounds.height);
        batch.draw(exitButton, exitButtonBounds.x, exitButtonBounds.y, exitButtonBounds.width, exitButtonBounds.height);
        batch.end();

        var clickedPoint = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        if (Gdx.input.justTouched()) {
            if (startButtonBounds.contains(clickedPoint)) {
                return Optional.of(new StageView(new RunningStage(new TestStage(), new Resources())));
            } else if (exitButtonBounds.contains(clickedPoint)) {
                Gdx.app.exit();
            }
        }

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

        // Center the buttons
        float startX = (camera.viewportWidth - BUTTON_WIDTH) / 2;
        float startY = (camera.viewportHeight - BUTTON_HEIGHT - EXIT_BUTTON_HEIGHT - BUTTON_SPACING - MARGIN_BOTTOM) / 2 + MARGIN_BOTTOM + EXIT_BUTTON_HEIGHT;

        startButtonBounds = new Rectangle(startX, startY, BUTTON_WIDTH, BUTTON_HEIGHT);

        // Adjust exit button position
        float exitY = startY - BUTTON_SPACING - EXIT_BUTTON_HEIGHT;
        exitButtonBounds = new Rectangle(startX, exitY, BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
    }
}
