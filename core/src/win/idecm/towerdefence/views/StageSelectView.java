package win.idecm.towerdefence.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import win.idecm.towerdefence.*;
import win.idecm.towerdefence.stages.TestStage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StageSelectView implements GameView, InputProcessor {
    static final int WIDTH = 1920;
    static final int HEIGHT = 1080;
    static final int OUTER_WINDOW_PADDING = 50;
    static final int RIGHT_PANELS_WIDTH = 450;
    static final int BOTTOM_PANELS_HEIGHT = 200;
    static final int PICKER_PADDING = 50;
    static final int PAGE_SWITCHER_HEIGHT = 0;

    SpriteBatch batch = new SpriteBatch();
    Camera camera = new OrthographicCamera();
    Viewport viewport = new FitViewport(WIDTH, HEIGHT, camera);
    NinePatch nPatch = new NinePatch(new Texture("9patch-bg.png"), 32, 32, 32, 32);
    NinePatch borderPath = new NinePatch(new Texture("9patch-lil-borderonly.png"), 16, 16, 16, 16);
    NinePatch borderActivePath = new NinePatch(new Texture("9patch-lil-borderonly-active.png"), 16, 16, 16, 16);

    Point mouse_hovered = Point.of(Double.NEGATIVE_INFINITY);

    int selectedMapIdx = -1;
    boolean clickedNextButton;

    Button startButton = new Button(
        WIDTH-OUTER_WINDOW_PADDING-RIGHT_PANELS_WIDTH+PICKER_PADDING,
        OUTER_WINDOW_PADDING+PICKER_PADDING,
        RIGHT_PANELS_WIDTH - PICKER_PADDING*2,
        100
    );

    List<StageKind> stages = new ArrayList<>();
    List<Rect> stageClickAreas = new ArrayList<>();
    List<Texture> textures = new ArrayList<>();

    public StageSelectView () {
        stages.add(new TestStage());
        stages.add(new TestStage());
        stages.add(new TestStage());
        stages.add(new TestStage());
        stages.add(new TestStage());
        stages.add(new TestStage());
        stages.add(new TestStage());
        stages.add(new TestStage());
        stages.add(new TestStage());
        for(var i = 0; i < stages.size(); i++) {
            stageClickAreas.add(this.getPositioningForStageImage(i));
        }
        nPatch.scale(2.0f, 2.0f);
        borderPath.scale(2.0f, 2.0f);
        borderActivePath.scale(2.0f, 2.0f);
        textures.addAll(stages.stream().map(stage -> new Texture(stage.getBackgroundTexturePath())).collect(Collectors.toList()));
    }

    @Override
    public void initialize() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public Optional<GameView> render() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();

        drawBackgrounds();
        drawPicks();
        startButton.draw(batch, mouse_hovered, false, selectedMapIdx == -1);

        batch.end();

        if (clickedNextButton) {
            System.out.println(selectedMapIdx);
            if (selectedMapIdx != -1) {
                return Optional.of(new StageView(new RunningStage(stages.get(selectedMapIdx), new Resources())));
            } else {
                clickedNextButton = false;
            }
        }

        return Optional.empty();
    }

    private void drawPicks() {
        for(int i = 0; i < 9 && i < stages.size(); i++) {
            var drawLoc = getPositioningForStageImage(i);
            var img = textures.get(i);

            batch.draw(img, drawLoc.x+3, drawLoc.y+3, drawLoc.w-6, drawLoc.h-6);
            if (selectedMapIdx == i) {
                borderActivePath.draw(batch, drawLoc.x, drawLoc.y, drawLoc.w, drawLoc.h);
            } else {
                borderPath.draw(batch, drawLoc.x, drawLoc.y, drawLoc.w, drawLoc.h);
            }
        }
    }

    private Rect getPositioningForStageImage (int imageNum) {
        var row = 2 - imageNum / 3;
        var col = imageNum % 3;

        var onewidth = (WIDTH - OUTER_WINDOW_PADDING*2 - RIGHT_PANELS_WIDTH - PICKER_PADDING*4) / 3;
        var oneheight = (HEIGHT - OUTER_WINDOW_PADDING*2 - PAGE_SWITCHER_HEIGHT - PICKER_PADDING*4) / 3;
        var x = OUTER_WINDOW_PADDING + PICKER_PADDING + (onewidth + PICKER_PADDING) * col;
        var y = OUTER_WINDOW_PADDING + PAGE_SWITCHER_HEIGHT + PICKER_PADDING + (oneheight + PICKER_PADDING) * row;

        return new Rect(x, y, onewidth, oneheight);
    }

    class Rect {
        public int x, y, w, h;
        public Rect(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        public boolean isInside (Point point) {
            var tx = point.getX();
            var ty = point.getY();
            return x <= tx && tx <= x+w && y <= ty && ty <= y+h;
        }
    }

    private void drawBackgrounds() {
        // left panel
        nPatch.draw(batch,
            OUTER_WINDOW_PADDING,
            OUTER_WINDOW_PADDING,
            WIDTH - OUTER_WINDOW_PADDING*2 - RIGHT_PANELS_WIDTH,
            HEIGHT - OUTER_WINDOW_PADDING*2
        );

        // top right panel
        nPatch.draw(batch,
            WIDTH - OUTER_WINDOW_PADDING - RIGHT_PANELS_WIDTH,
            OUTER_WINDOW_PADDING + BOTTOM_PANELS_HEIGHT,
            RIGHT_PANELS_WIDTH,
            HEIGHT - OUTER_WINDOW_PADDING*2 - BOTTOM_PANELS_HEIGHT
        );

        // bottom right panel
        nPatch.draw(batch,
            WIDTH - OUTER_WINDOW_PADDING - RIGHT_PANELS_WIDTH,
            OUTER_WINDOW_PADDING,
            RIGHT_PANELS_WIDTH,
            BOTTOM_PANELS_HEIGHT
        );
    }

    @Override
    public void dispose() {
        batch.dispose();
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
        for(var i = 0; i < stageClickAreas.size(); i++) {
            if (stageClickAreas.get(i).isInside(mouse_hovered)) {
                this.selectedMapIdx = i;
            }
        }

        if (startButton.isInside(mouse_hovered)) {
            this.clickedNextButton = true;
        }

        return true;
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
        mouse_hovered = Point.of(viewport.unproject(new Vector2(screenX, screenY)));
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }



    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
    }
}
