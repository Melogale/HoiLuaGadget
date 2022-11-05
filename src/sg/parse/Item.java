package sg.parse;

public class Item extends Element {

    String content;

    public Item(String content) {
        super(content);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return content;
    }

}
