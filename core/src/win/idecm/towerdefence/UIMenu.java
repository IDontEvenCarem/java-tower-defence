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
import com.badlogic.gdx.utils.Align;
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
    private Texture levelUpArrowTexture;
    private String[] towerDescriptions = {
            "Piercer Tower, Damage 50,   Level 1",
            "Archer Tower, Damage 60,   Level 1",
            "Druid Tower, Damage 70,   Level 1",
            "Wizard Tower, Damage 85,   Level 1",
            "Inferno Tower, Damage 110,   Level 1",
            "Royal Tower, Damage 150,   Level 1"
    };
    private int[] towerLevels;

    private boolean showTipBox;
    private String currentTowerDescription = "";

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

        levelUpArrowTexture = new Texture("LvlUpArrow.png");

        towerLevels = new int[6];
        for (int i = 0; i < 6; i++) {
            towerLevels[i] = 1;
        }
    }

    public void drawUi(RunningStage stage, SpriteBatch batch, ShapeRenderer shapeRenderer, GridPoint hovered, Point hoveredRenderable, int gridSize) {
        lastHovered = hovered;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        batch.begin();

        drawStatusPart(batch);
        drawPurchasePart(batch);

        if (hoveredRenderable.getX() >= leftOffset && !bannedGridPoints.contains(hovered)) {
            showTipBox = true;
            updateCurrentTowerDescription(hoveredRenderable);
            drawTipBox(batch);
        } else {
            showTipBox = false;
        }

        if (hoveredRenderable.getX() < leftOffset) {
            drawHoveredGrid(batch, hovered, hoveredRenderable, gridSize);
        }

        drawTowerIcons(batch);

        batch.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void updateCurrentTowerDescription(Point hoveredRenderable) {
        int towerIndex = getTowerIndex(hoveredRenderable);
        if (towerIndex >= 0) {
            currentTowerDescription = towerDescriptions[towerIndex];
        } else {
            currentTowerDescription = "";
        }
    }

    private int getTowerIndex(Point hoveredRenderable) {
        float iconSize = 48;
        float iconSpacing = 8;
        float startY = totalHeight - topPartFract * totalHeight - iconSize - 8;

        int towerIndex = (int) ((startY - hoveredRenderable.getY()) / (iconSize + iconSpacing));
        if (towerIndex >= 0 && towerIndex < towerTextures.length) {
            return towerIndex;
        } else {
            return -1;
        }
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

        font.getData().setScale(1.5f);
        font.setColor(Color.WHITE);
        float textX = leftOffset - totalWidth + 10;
        float textY = (float) (totalHeight * 0.2) - 20;
        font.draw(b, currentTowerDescription, textX, textY, totalWidth - 20, Align.topLeft, true);
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
        float startY = totalHeight - topPartFract * totalHeight - iconSize - 8; // Dostosowana pozycja do ikon wież

        int basePrice = 100; // Bazowa cena dla pierwszej wieży
        for (int i = 0; i < towerTextures.length; i++) {
            float y = startY - (iconSize + iconSpacing) * i;
            b.draw(towerTextures[i], leftOffset + 8, y, iconSize, iconSize);

            // Rysuj cenę wieży
            font.getData().setScale(1.5f);
            String priceText = "$ " + (basePrice + (i * 100));
            float priceX = leftOffset + 8 + iconSize + 8;
            float priceY = y + iconSize / 2 + font.getLineHeight() / 2;
            font.setColor(Color.WHITE);
            font.draw(b, priceText, priceX, priceY);

            // Rysuj poziom wieży
            Texture levelArrow = new Texture("LvlUpArrow.png");
            float arrowX = priceX + font.getCapHeight() + 50;
            float arrowY = priceY - font.getLineHeight() / 2 - 5;
            b.draw(levelArrow, arrowX, arrowY, 24, 24);
            String towerLevel = "1";
            float levelX = arrowX + 20;
            float levelY = arrowY + 14;
            font.getData().setScale(1.5f);
            font.draw(b, towerLevel, levelX, levelY);
        }
    }



    public void scroll(float amount) {

    }

    public Optional<Consumer<RunningStage>> onClick(float x, float y) {
        if (x < leftOffset) {
            return Optional.of(runningStage -> {
                if (Math.random() > 0.5) {
                    runningStage.tryPurchasingTower(new TestAoeTower(lastHovered));
                } else {
                    runningStage.tryPurchasingTower(new PiercerTower(lastHovered));
                }
            });
        }
        return Optional.empty();
    }
}
