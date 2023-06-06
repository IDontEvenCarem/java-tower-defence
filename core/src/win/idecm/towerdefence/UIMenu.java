package win.idecm.towerdefence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import win.idecm.towerdefence.towers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class UIMenu {
    private static final int TOWER_BUY_BUTTONS_GAP = 20;
    private static final int TOWER_BUY_BUTTON_HEIGHT = 80;

    private Resources resources;
    private BitmapFont font;
    private int selectedTowerIndex = -1;
    private int leftOffset;
    private int totalWidth;
    private int totalHeight;

    static final private float topPartFract = 0.2f;

    private Set<GridPoint> bannedGridPoints;
    private Texture hoverNeutral;
    private Texture hoverDisallowed;
    private Texture heartIcon = new Texture("heart.png");
    private NinePatch nPatch;

    private Optional<Tower> selectedTower = Optional.empty();

    private GridPoint lastHovered;
    private Point lastHoveredRenderable;
    private Point mousePos = Point.of(0);

    private Texture[] towerTextures;
    private Texture levelUpArrowTexture;
    Texture levelArrow = new Texture("LvlUpArrow.png");

    private class MenuTowerEntry {
        public String name;
        public Texture texture;
        public Function<GridPoint, Tower> spawnerFn;
        public int price;
        public String description;

        public MenuTowerEntry(String name, Texture texture, Function<GridPoint, Tower> spawnerFn, int price, String description) {
            this.name = name;
            this.texture = texture;
            this.spawnerFn = spawnerFn;
            this.price = price;
            this.description = description;
        }
    };
    private List<MenuTowerEntry> purchasableTowers = new ArrayList<>();
    private List<Button> towerButtons = new ArrayList<>();

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

        purchasableTowers.add(new MenuTowerEntry(
            PiercerTower.name,
            PiercerTower.towerTexture,
            PiercerTower::new,
            PiercerTower.basePrice,
            "Quick shots that go through many enemies"
        ));

        purchasableTowers.add(new MenuTowerEntry(
            WizardTower.name,
            WizardTower.towerTexture,
            WizardTower::new,
            WizardTower.basePrice,
            "Consistent damage over a large area"
        ));

        layoutButtons();

        this.bannedGridPoints = bannedGridPoints;

        levelUpArrowTexture = new Texture("LvlUpArrow.png");

        towerLevels = new int[6];
        for (int i = 0; i < 6; i++) {
            towerLevels[i] = 1;
        }
    }

    public void drawUi(RunningStage stage, SpriteBatch batch, ShapeRenderer shapeRenderer, GridPoint hovered, Point hoveredRenderable, int gridSize) {
        lastHovered = hovered;
        lastHoveredRenderable = hoveredRenderable;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        batch.begin();

        drawStatusPart(batch);
        drawPurchasePart(batch);

        if (hoveredRenderable.getX() >= leftOffset && !bannedGridPoints.contains(hovered)) {
            showTipBox = true;
            updateCurrentTowerDescription(mousePos);
            drawTipBox(batch);
        } else {
            showTipBox = false;
        }

        if (hoveredRenderable.getX() < leftOffset) {
            drawHoveredGrid(batch, hovered, hoveredRenderable, gridSize);
        }

        if (selectedTower.isEmpty()) {
            drawTowerIcons(batch);
        } else {

        }

        batch.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void updateCurrentTowerDescription(Point mousePos) {
        int towerIndex = getTowerIndex(mousePos);
        if (towerIndex >= 0) {
            var pt = purchasableTowers.get(towerIndex);
            currentTowerDescription = pt.name + "\nPrice: $" + pt.price + '\n' + pt.description;
        } else {
            currentTowerDescription = "";
        }
    }

    private int getTowerIndex(Point mousePos) {
        for (int i = 0; i < towerButtons.size(); i++) {
            if (towerButtons.get(i).isInside(mousePos)) {
                return i;
            }
        }
        return -1;
    }

    private void layoutButtons() {
        var width = totalWidth - TOWER_BUY_BUTTONS_GAP*2;
        var height = TOWER_BUY_BUTTON_HEIGHT;

        var startY = totalHeight * (1.0 - topPartFract) - TOWER_BUY_BUTTONS_GAP - height;

        towerButtons.clear();
        var offset = 0;
        for(var tower : purchasableTowers) {
            towerButtons.add(new Button(
                leftOffset + TOWER_BUY_BUTTONS_GAP,
                (int) (startY - offset),
                width,
                height
            ));
            offset += height + TOWER_BUY_BUTTONS_GAP;
        }
    }

    public void drawStatusPart(Batch b) {
        var height = (int) (topPartFract * totalHeight);
        nPatch.draw(b, leftOffset, totalHeight - height, totalWidth, height);

        font.setColor(Color.WHITE);
        font.getData().setScale(2.0f);
        var moneydraw = font.draw(b, "$  " + resources.getMoney(), leftOffset + 20, totalHeight - 20);

        // Draw heart icon
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
        float textX = leftOffset - totalWidth + 20;
        float textY = (float) (totalHeight * 0.2) - 20;
        font.draw(b, currentTowerDescription, textX, textY, totalWidth - 40, Align.topLeft, true);
    }


    public void drawHoveredGrid(Batch b, GridPoint hovered, Point hoveredRenderable, float gridSize) {
        if (bannedGridPoints.contains(hovered)) {
            b.draw(hoverDisallowed, (float) hoveredRenderable.getX(), (float) hoveredRenderable.getY(), gridSize, gridSize);
        } else {
            b.draw(hoverNeutral, (float) hoveredRenderable.getX(), (float) hoveredRenderable.getY(), gridSize, gridSize);
        }
    }

    public void drawTowerIcons(SpriteBatch b) {
        float iconSize = TOWER_BUY_BUTTON_HEIGHT - 20;
        float iconSpacing = 8;
        float startY = totalHeight - topPartFract * totalHeight - iconSize - 8; // Dostosowana pozycja do ikon wież

        for (int i = 0; i < towerButtons.size(); i++) {
            towerButtons.get(i).draw(b, mousePos,
                selectedTowerIndex == i,
                !resources.hasMoneyToBuy(purchasableTowers.get(i).price)
            );
        }

        for (int i = 0; i < towerButtons.size(); i++) {
            var button = towerButtons.get(i);
            var pt = purchasableTowers.get(i);

            float y = button.getY() + 10;
            b.draw(pt.texture, leftOffset + TOWER_BUY_BUTTONS_GAP + 10, y, iconSize, iconSize);

            // Rysuj cenę wieży
            font.getData().setScale(2.0f);
            String priceText = "$ " + pt.price;
            float priceX = leftOffset + TOWER_BUY_BUTTONS_GAP + 12 + 8 + iconSize + 8;
            float priceY = y + iconSize / 2 + font.getLineHeight() / 2;
            font.setColor(Color.WHITE);
            font.draw(b, priceText, priceX, priceY);

            // Rysuj poziom wieży
            float arrowX = priceX + font.getCapHeight() + 80;
            float arrowY = priceY - font.getLineHeight() / 2 - 5;
            b.draw(levelArrow, arrowX, arrowY, 24, 24);
            String towerLevel = "1";
            float levelX = arrowX + 20;
            float levelY = arrowY + 14;
            font.getData().setScale(1.5f);
            font.draw(b, towerLevel, levelX, levelY);
        }
    }

    public void setMousePos (Point mousePos) {
        this.mousePos = mousePos;
    }

    public void scroll(float amount) {

    }
    public Optional<Consumer<RunningStage>> onClick(float x, float y) {
        if (x >= leftOffset) {
            int towerIndex = getTowerIndex(new Point(x, y));
            if (towerIndex >= 0) {
                if (towerIndex == selectedTowerIndex) {
                    selectedTowerIndex = -1;
                } else {
                    selectedTowerIndex = towerIndex;
                }
            }
        }
        if (x < leftOffset) {
            if (selectedTowerIndex != -1) {
                var towerSpawner = purchasableTowers.get(selectedTowerIndex).spawnerFn;
                return Optional.of(runningStage -> runningStage.tryPurchasingTower(towerSpawner.apply(lastHovered)));
            }
        }
        return Optional.empty();
    }
}
