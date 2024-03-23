package tests;

public class A implements Cloneable {
    private Integer x;
    protected boolean y;
    public String z;

    // Constructors

    public A(Integer _x) {
        x = _x;
        y = true;
        z = "Default";
    }

    public A(Integer _x, String _z) {
        x = _x;
        y = true;
        z = _z;
    }

    /**
     * This copy constructor should never be used during back-up, as this class implements cloneable!
     * For that reason, this copy constructor copies invalid values.
     */
    public A(A that) {
        this.x = that.x + 1;
        this.y = !(that.y);
        this.z = that.z + "more";
    }

    /**
     * This one should be invoked during back-up.
     */
    @Override
    public A clone() {
       A res = new A(this.x);
       res.y = this.y;
       res.z = this.z;
       return res;
    }

    // Setters
    public void setX(Integer _x) {x = _x;}
    public void setY(Boolean _y) {y = _y;}
    public void setZ(String _z) {z = _z;}

    // Getters
    public Integer getX() {return x;}
    public Boolean getY() {return y;}
    public String getZ() {return z;}
}
