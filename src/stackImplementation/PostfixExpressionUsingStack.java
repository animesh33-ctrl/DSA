package stackImplementation;

import java.util.Stack;

public class PostfixExpressionUsingStack {

    public static int postfixEvaluation(String expression){
        if(expression.isEmpty()) return 0;
        String[] tokens = expression.split(" ");
        Stack<Integer> stack = new Stack<>();

        for(String token : tokens){

            if(isOperator(token)){
                int op1 = stack.pop();
                int op2 = stack.pop();

                int res = evaluate(op1,op2,token);
                stack.push(res);
            }
            else{
                stack.push(Integer.parseInt(token));
            }
        }
        return stack.pop();
    }

    public static int evaluate(int op1,int op2,String tok){
        switch (tok){
            case "+":
                return op1+op2;
            case "-":
                return op1-op2;
            case "*":
                return op1*op2;
            case "/":
                if(op2!=0) return op1/op2;
                else throw new RuntimeException("Operand 2 is 0");
            case "%":
                if(op2!=0) return op1%op2;
                else throw new RuntimeException("Operand 2 is 0");
            case "^":
                return (int)Math.pow(op1,op2);
            default:
                throw new RuntimeException("Operator not available");
        }
    }

    public static boolean isOperator(String token){
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("%") || token.equals("^");
    }

    public static void main(String[] args) {
        String[] expressions = {
                "2 3 +",                    // 2 + 3 = 5
                "5 6 2 + *",                // 5 * (6 + 2) = 40
                "10 2 / 3 +",               // (10 / 2) + 3 = 8
                "5 1 2 + 4 * + 3 -",        // 5 + ((1 + 2) * 4) - 3 = 14
                "15 7 1 1 + - / 3 * 2 1 1 + + -"  // Complex expression = 5
        };

        System.out.println("Postfix Expression Evaluator\n");

        for (String expr : expressions) {
            try {
                int result = postfixEvaluation(expr);
                System.out.println("Expression: " + expr);
                System.out.println("Result: " + result);
                System.out.println();
            } catch (Exception e) {
                System.out.println("Error evaluating: " + expr);
                System.out.println("Error: " + e.getMessage());
                System.out.println();
            }
        }
    }
}
