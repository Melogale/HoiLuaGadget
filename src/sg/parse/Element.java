package sg.parse;

public abstract class Element {

    private String label;

    public Element(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public abstract String toString();

}
