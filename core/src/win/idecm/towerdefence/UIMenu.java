package win.idecm.towerdefence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Set;

public class UIMenu {
    private Resources resources;
    private BitmapFont font;

    private int leftOffset;
    private int totalWidth;
    private int totalHeight;

    private Set<GridPoint> bannedGridPoints;
    private Texture hoverNeutral;
    private Texture hoverDisallowed;
    private NinePatch nPatch;


    public UIMenu (int left, int width, int height, Resources resources, Set<GridPoint> bannedGridPoints) {
        this.resources = resources;
        leftOffset = left;
        totalHeight = height;
        totalWidth = width;
        font = new BitmapFont();
        hoverNeutral = new Texture("hovers/neutral.png");
        hoverDisallowed = new Texture("hovers/disallowed.png");
        nPatch = new NinePatch(new Texture("9patch-bg.png"), 32, 32, 32, 32);
        nPatch.scale(2,2);

        this.bannedGridPoints = bannedGridPoints;
    }

    public void drawUi(SpriteBatch batch, ShapeRenderer shapeRenderer, GridPoint hovered, Point hoveredRenderable, int gridSize) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


        batch.begin();

        nPatch.draw(batch, leftOffset, 0, totalWidth, totalHeight);

        font.setColor(Color.BLACK);
        font.getData().setScale(1.0f);
        var moneydraw = font.draw(batch, "$" + resources.getMoney(),  leftOffset + 10, totalHeight - 10);
        var heartsdraw = font.draw(batch, "♥" + resources.getLife(), leftOffset + 10, totalHeight - 15 - moneydraw.height);

        if (hoveredRenderable.getX() < leftOffset) {
            if (bannedGridPoints.contains(hovered)) {
                batch.draw(hoverDisallowed, (float) hoveredRenderable.getX(), (float) hoveredRenderable.getY(), gridSize, gridSize);
            }
            else {
                batch.draw(hoverNeutral, (float) hoveredRenderable.getX(), (float) hoveredRenderable.getY(), gridSize, gridSize);
            }
        }

        batch.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

    }

    public void drawHoveredGrid (Batch b) {

    }

    public void scroll(float amount) {

    }

    public void onClick(float x, float y) {

    }
}
