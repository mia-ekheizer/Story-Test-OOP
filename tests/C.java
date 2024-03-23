package tests;

public class C {
    int x;
    int y;

    C(Integer _x) {
        x = _x;
        y = 2;
    }

    C(C that) {
        this.x = that.x;
        this.y = that.y;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
}
