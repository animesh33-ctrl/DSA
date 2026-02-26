package interfaceimp;


@java.lang.FunctionalInterface
public interface FunctionalInterfaceImp {

    void add();

    static void add2(){
        System.out.println("ADD2");
    }
    static void add3(){
        System.out.println("ADD3");
    }
    static void add4(){
        System.out.println("ADD4");
    }
    private static void add5(){
        System.out.println("ADD5");
    }

    boolean equals(Object o);  // this is overriden method

    int hashCode();

    String toString();

    public static void main(String[] args) {
        add5();
    }
}
