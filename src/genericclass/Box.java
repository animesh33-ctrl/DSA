package genericclass;

public class Box<T> {
    T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Box{" +
                "value=" + value +
                '}';
    }
}

class Main{
    public static void main(String[] args) {
        Box<Integer> box = new Box<>();
        box.setValue(10);
        box.setValue(20);
        System.out.println(box);
    }
}



