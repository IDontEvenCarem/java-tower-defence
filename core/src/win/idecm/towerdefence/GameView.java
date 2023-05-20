package win.idecm.towerdefence;

import java.util.Optional;

public interface GameView {

    void initialize();
    Optional<GameView> render();
    void dispose();

    default void resize(int width, int height) {};
}
