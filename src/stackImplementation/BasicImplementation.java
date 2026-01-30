package stackImplementation;

import java.util.Stack;

public class BasicImplementation {
    public static void main(String[] args) {
        Stack<Integer> st = new Stack<>();
        st.push(1);
        st.push(2);
        st.push(3);
        st.push(4);
        st.push(5);

        System.out.println(st);
        System.out.println(st.pop());

        for(int i=0;i<st.size();i++){
            System.out.print(st.get(i)+" ");
        }

        for (Integer integer : st) {
            System.out.print(integer + " ");
        }



    }
}
