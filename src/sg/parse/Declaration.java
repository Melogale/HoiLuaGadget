package sg.parse;

public class Declaration extends Element {

    private Item variable;
    private Item value;

    public Declaration(Item variable, Item value) {
        super(variable.toString());
        this.variable = variable;
        this.value = value;
    }

    public Item getVariable() {
        return variable;
    }

    public Item getValue() {
        return value;
    }

    @Override
    public String toString() {
        return variable.toString() + " = " + value.toString();
    }

}
