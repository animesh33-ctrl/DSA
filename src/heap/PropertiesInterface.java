package heap;

public interface PropertiesInterface {

    int perform(int num);
}

class Main implements PropertiesInterface{

    @Override
    public int perform(int num) {
        return 0;
    }
}
class Anonymous{
    public static void main(String[] args) {

        PropertiesInterface pr2 = new PropertiesInterface() {
            @Override
            public int perform(int num) {
                return 0;
            }
        };

        PropertiesInterface pr = (int num) -> num*2;

        System.out.println(pr.perform(2));
    }
}