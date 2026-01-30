package stackImplementation;

import java.util.Stack;

public class ValidParanthesis {

    public boolean validParanthesis(String str){
        Stack<Character> stack = new Stack<>();
        for(int i=0;i<str.length();i++){
            char ch = str.charAt(i);
            if(ch == '(' || ch == '{' || ch == '['){
                stack.push(ch);
            } else if (ch == ')') {
                if (stack.isEmpty() || stack.pop() != '(')
                    return false;
            }
            else if (ch == '}') {
                if (stack.isEmpty() || stack.pop() != '{')
                    return false;
            }
            else if (ch == ']') {
                if (stack.isEmpty() || stack.pop() != '[')
                    return false;
            }
        }
        return stack.isEmpty();
    }

    public static void main(String[] args) {

        ValidParanthesis val = new ValidParanthesis();

        String str1 = "({[]})"; //true
        String str2 = "({[}])"; //false

        System.out.println(val.validParanthesis(str1));
        System.out.println(val.validParanthesis(str2));

    }
}
