package tests;

public class B {
    private Integer x;
    private String y;

    public B(B that) {
        this.x = that.x;
        this.y = that.y;
    }

    public B(Integer _x) {
        x = _x;
        y = "Hello";
    }

    public void setX(Integer _x) {
        x = _x;
    }

    public void setY(String _y) {
        y = _y;
    }

    public Integer getX() {
        return x;
    }

    public String getY() {
        return y;
    }

}
