package assessment5;

import java.util.Stack;

public class ValidParentheses {

    public static boolean validParanthesis(String string){
        if(string == null) return false;
        Stack<Character> stack = new Stack<>();
        for(int i=0;i<string.length();i++){
            char ch = string.charAt(i);
            if(ch == '(' || ch == '{' || ch == '['){
                stack.push(ch);
            }
            else if(ch == ')' && stack.peek()=='(' && !stack.isEmpty()){
                stack.pop();
            }

            else if(ch == '}' && stack.peek()=='{' && !stack.isEmpty()){
                stack.pop();
            }

            else if(ch == ']' && stack.peek()=='[' && !stack.isEmpty()){
                stack.pop();
            }
        }
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        String string = "{([{}])}";
        System.out.println(validParanthesis(string));
    }
}
