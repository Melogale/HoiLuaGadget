package sg.obj;

public class SimpleColor {

    public int r;
    public int g;
    public int b;

    public SimpleColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public String toString() {
        return r + " " + g + " " + b;
    }
}
