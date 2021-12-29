package sg.obj;

public class VP implements Comparable {

    public int province;
    public int value;

    public VP(int province, int value) {
        this.province = province;
        this.value = value;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof VP) {
            VP compared = (VP) o;
            return this.province - compared.province;
        }
        return 0;
    }
}
