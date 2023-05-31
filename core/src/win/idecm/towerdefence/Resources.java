package win.idecm.towerdefence;

public class Resources {
    private int life;
    private int money;

    public Resources(int life, int money) {
        this.life = life;
        this.money = money;
    }

    public Resources() {

    }

    public void loseLife (int lost) {
        life -= lost;
    }

    public void gainMoney (int gain) {
        money += gain;
    }
}
