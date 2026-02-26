package lambda;

public abstract class Parent {
    abstract void function(Number number);
    abstract void function(Integer integer);
    public abstract void function(Double doubled);
    public abstract void function(Float floatf);


    public static void main(String[] args) {
        Parent p =new Child();
        p.function(8);
        p.function(19.9d);
        p.function(19.9);
        p.function(19.9f);

        Parent p2 = new Parent() {
            @Override
            public void function(Number number) {
                System.out.println("Number : "+number);
            }

            @Override
            public void function(Integer integer) {
                System.out.println("Integer : "+integer);
            }
            @Override
            public void function(Double doubled) {
                System.out.println("Double : "+doubled);
            }
            @Override
            public void function(Float floatf) {
                System.out.println("Float : "+floatf);
            }
        };
        p2.function(8);
        p2.function(19.9d);
        p2.function(19.9);
        p2.function(19.9f);
    }
}

class Child extends Parent{

    @Override
    public void function(Number number) {
        System.out.println("Number : "+number);
    }

    @Override
    public void function(Integer integer) {
        System.out.println("Integer : "+integer);
    }
    @Override
    public void function(Double doubled) {
        System.out.println("Double : "+doubled);
    }
    @Override
    public void function(Float floatf) {
        System.out.println("Float : "+floatf);
    }
}
