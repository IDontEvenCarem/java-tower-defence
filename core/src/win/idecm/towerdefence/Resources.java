package win.idecm.towerdefence;

public class Resources {
    private int life;
    private int money;

    public Resources(int life, int money) {
        this.life = life;
        this.money = money;
    }

    public Resources() {
        this(10, 1000);
    }

    public void loseLife (int lost) {
        life -= lost;
    }

    public void gainMoney (int gain) {
        money += gain;
    }
    public void spendMoney(int cost) {
        money -= cost;
    }

    public boolean hasMoneyToBuy(int amount) {
        return getMoney() >= amount;
    }

    public int getLife() {
        return life;
    }

    public int getMoney() {
        return money;
    }
}
