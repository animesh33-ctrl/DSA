package lambda;

public class LambdaSample {
    public LambdaSample() {
        System.out.println("LambdaSample Constructor 1");
    }

    static {
        System.out.println("LambdaSample Static()");
    }

//    {
//        System.out.println("LambdaSample non Static()");
//    }

    public static void main(String[] args) {
        System.out.println("Main");

        LambdaSample obj = new LambdaSample2();

    }
}

class LambdaSample2 extends LambdaSample{
    public LambdaSample2() {
        System.out.println("LambdaSample2 Constructor 1");
    }

    {
        System.out.println("LambdaSampleChild non Static()");
    }

    {
        System.out.println("Bokachoda");
    }

    static {
        System.out.println("LambdaSampleChild Static()");
    }
}