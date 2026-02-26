package lambda;

public interface Parent2 {
    void function(Number number);

}

class Main{
    public static void main(String[] args) {
        Parent2 p2 = new Parent2() {
            @Override
            public void function(Number number) {
                System.out.println("Number : "+number);
            }
        };

        p2.function(76);

        Parent2 parent1 = (number)->
            System.out.println("Number : "+number);

        parent1.function(2);
    }
}