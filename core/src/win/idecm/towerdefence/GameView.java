package win.idecm.towerdefence;

public interface GameView {

    void initialize();
    void render();
    void dispose();

    default void resize(int width, int height) {};
}
