package streamapi;

public class Ambiguity {
    public static void main(String[] args) {

    }
}

interface A{
    static void add(){
        System.out.println("A");
    }
}

interface B{
    static void add(){
        System.out.println("B");
    }
}

class C implements A,B{
    public static void main(String[] args) {
        C c = new C();

    }
}
