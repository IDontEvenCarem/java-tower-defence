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
import win.idecm.towerdefence.towers.PiercerTower;
import win.idecm.towerdefence.towers.TestAoeTower;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class UIMenu {
    private Resources resources;
    private BitmapFont font;

    private int leftOffset;
    private int totalWidth;
    private int totalHeight;

    static final private float topPartFract = 0.2f;

    private Set<GridPoint> bannedGridPoints;
    private Texture hoverNeutral;
    private Texture hoverDisallowed;
    private NinePatch nPatch;

    private GridPoint lastHovered;

    private Texture[] towerTextures;

    public UIMenu(int left, int width, int height, Resources resources, Set<GridPoint> bannedGridPoints) {
        this.resources = resources;
        leftOffset = left;
        totalHeight = height;
        totalWidth = width;
        font = new BitmapFont();
        hoverNeutral = new Texture("hovers/neutral.png");
        hoverDisallowed = new Texture("hovers/disallowed.png");
        nPatch = new NinePatch(new Texture("9patch-bg.png"), 32, 32, 32, 32);
        nPatch.scale(2, 2);

        this.bannedGridPoints = bannedGridPoints;

        towerTextures = new Texture[6];
        for (int i = 0; i < 6; i++) {
            towerTextures[i] = new Texture("TowerStage" + (i + 1) + ".png");
        }
    }

    public void drawUi(RunningStage stage, SpriteBatch batch, ShapeRenderer shapeRenderer, GridPoint hovered, Point hoveredRenderable, int gridSize) {
        lastHovered = hovered;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        batch.begin();

        drawStatusPart(batch);
        drawPurchasePart(batch);

        if (hoveredRenderable.getX() >= leftOffset) {
            drawTipBox(batch);
        }

        if (hoveredRenderable.getX() < leftOffset) {
            drawHoveredGrid(batch, hovered, hoveredRenderable, gridSize);
        }

        drawTowerIcons(batch);

        batch.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void drawStatusPart(Batch b) {
        var height = (int) (topPartFract * totalHeight);
        nPatch.draw(b, leftOffset, totalHeight - height, totalWidth, height);

        font.setColor(Color.WHITE);
        font.getData().setScale(2.0f);
        var moneydraw = font.draw(b, "$  " + resources.getMoney(), leftOffset + 20, totalHeight - 20);

        // Draw heart icon
        Texture heartIcon = new Texture("heart.png");
        float heartX = leftOffset + 20;
        float heartY = totalHeight - 25 - moneydraw.height - heartIcon.getHeight();
        b.draw(heartIcon, heartX, heartY);

        font.getData().setScale(1.5f);
        var lifeDraw = font.draw(b, " " + resources.getLife(), heartX + heartIcon.getWidth(), heartY + heartIcon.getHeight() / 2 + font.getLineHeight() / 2);
    }


    public void drawPurchasePart(Batch b) {
        var statusPartHeight = (int) (topPartFract * totalHeight);
        nPatch.draw(b, leftOffset, 0, totalWidth, totalHeight - statusPartHeight);
    }

    public void drawTipBox(Batch b) {
        nPatch.draw(b, leftOffset - totalWidth, 0, totalWidth, (float) (totalHeight * 0.2));
    }

    public void drawHoveredGrid(Batch b, GridPoint hovered, Point hoveredRenderable, float gridSize) {
        if (bannedGridPoints.contains(hovered)) {
            b.draw(hoverDisallowed, (float) hoveredRenderable.getX(), (float) hoveredRenderable.getY(), gridSize, gridSize);
        } else {
            b.draw(hoverNeutral, (float) hoveredRenderable.getX(), (float) hoveredRenderable.getY(), gridSize, gridSize);
        }
    }

    public void drawTowerIcons(Batch b) {
        float iconSize = 48;
        float iconSpacing = 8;
        float startY = totalHeight - topPartFract * totalHeight - iconSize - 8; // Adjusted position to fit the tower icons

        int basePrice = 100; // Base price for the first tower
        for (int i = 0; i < towerTextures.length; i++) {
            float y = startY - (iconSize + iconSpacing) * i;
            b.draw(towerTextures[i], leftOffset + 8, y, iconSize, iconSize);

            // Draw tower price
            font.getData().setScale(1.5f);
            String priceText = "$ " + (basePrice + (i * 100));
            float priceX = leftOffset + 8 + iconSize + 8;
            float priceY = y + iconSize / 2 + font.getLineHeight() / 2;
            font.draw(b, priceText, priceX, priceY);
        }
    }


    public void scroll(float amount) {

    }

    public Optional<Consumer<RunningStage>> onClick(float x, float y) {
        if (x < leftOffset) {
            return Optional.of(runningStage -> {
                if(Math.random() > 0.5) {
                    runningStage.tryPurchasingTower(new TestAoeTower(lastHovered));
                } else {
                    runningStage.tryPurchasingTower(new PiercerTower(lastHovered));
                }
            });
        }
        return Optional.empty();
    }
}
