package _pr;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

public class PrimeNumberCheck  {
    public static void check(int array[]) throws IOException {
        IntPredicate isPrime = (a) -> {
            for(int i=2;i<Math.sqrt(a);i++){
                if(a%i==0) return false;
            }
            return true;
        };

        try(FileWriter writer = new FileWriter(new File("./out.txt"))){
            Arrays.stream(array).filter(isPrime).forEach(e-> {
                try {
                    writer.append(String.valueOf(e)).append("\n");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        int arrays[] = {2,3,4,5,6,7,8,98,9,0,8,17};
        check(arrays);
    }
}
