package shyn.zyot.mytravels.base;

public class MyKeyValue {
    public final int id;
    public final String key;
    public final String value;

    public MyKeyValue(int id, String key, String value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "MyKeyValue{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
