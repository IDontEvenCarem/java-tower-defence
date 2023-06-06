package win.idecm.towerdefence;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Button {
    static public final NinePatch defaultNormalPatch = new NinePatch(new Texture("9patch-lil-normal.png"), 16, 16, 16, 16);
    static public final NinePatch defaultHoveredPatch = new NinePatch(new Texture("9patch-lil-hovered.png"), 16, 16, 16, 16);
    static public final NinePatch defaultPressedPatch = new NinePatch(new Texture("9patch-lil-pressed.png"), 16, 16, 16, 16);
    static public final NinePatch defaultDisabledPatch = new NinePatch(new Texture("9patch-lil-disabled.png"), 16, 16, 16, 16);

    static {
        defaultNormalPatch.scale(2.0f, 2.0f);
        defaultHoveredPatch.scale(2.0f, 2.0f);
        defaultPressedPatch.scale(2.0f, 2.0f);
        defaultDisabledPatch.scale(2.0f, 2.0f);
    }

    private NinePatch normalPatch, activePatch, hoveredPatch, disabledPatch;
    private int x, y, w, h;

    public Button(NinePatch normalPatch, NinePatch hoveredPatch, NinePatch activePatch, NinePatch disabledPatch, int x, int y, int w, int h) {
        this.normalPatch = normalPatch;
        this.hoveredPatch = hoveredPatch;
        this.activePatch = activePatch;
        this.disabledPatch = disabledPatch;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public Button(int x, int y, int w, int h) {
        this(defaultNormalPatch, defaultHoveredPatch, defaultPressedPatch, defaultDisabledPatch, x, y, w, h);
    }


    public void draw(SpriteBatch batch, Point mouseLoc, boolean isActive, boolean isDisabled) {
        var usedPatch = normalPatch;
        if (isDisabled) {
            usedPatch = disabledPatch;
        }
        else {
            var isHovered = isInside(mouseLoc);
            if (isHovered) {
                usedPatch = hoveredPatch;
            }
            if (isActive) {
                usedPatch = activePatch;
            }
        }

        usedPatch.draw(batch, x, y, w, h);
    }

    public void draw(SpriteBatch batch, Point mouseLoc, boolean isActive) {
        draw(batch, mouseLoc, isActive, false);
    }

    public void draw(SpriteBatch batch, Point mouseLoc) {
        draw(batch, mouseLoc, false, false);
    }


    public boolean isInside(Point point) {
        var tx = point.getX();
        var ty = point.getY();
        return x <= tx && tx <= x+w && y <= ty && ty <= y+h;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }
}
