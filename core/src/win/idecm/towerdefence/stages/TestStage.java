package win.idecm.towerdefence.stages;

import win.idecm.towerdefence.EnemyPath;
import win.idecm.towerdefence.StageKind;

import java.util.ArrayList;
import java.util.List;

public class TestStage implements StageKind {
    @Override
    public String getName() {
        return "Test Stage";
    }

    @Override
    public List<EnemyPath> getPaths() {
        return new ArrayList<>();
    }

    @Override
    public String getBackgroundTexturePath() {
        return "TD_Map_Test_1.png";
    }
}
