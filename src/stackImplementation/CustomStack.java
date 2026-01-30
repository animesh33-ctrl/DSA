package stackImplementation;

import java.util.Arrays;

public class CustomStack {
    int[] stack;
    private int size;
    int top;

    public CustomStack() {
    }

    public CustomStack(int size){
        stack = new int[size];
        top = -1;
        this.size = size;
    }

    public boolean isEmpty(){
        return top==-1;
    }
    public boolean isFull(){
        return top==size-1;
    }

    public int push(int data){
        if(isFull()) {
            throw new RuntimeException("Stack Overflow");
        }
        stack[++top] = data;
        return data;
    }

    public int pop(){
        if(isEmpty()){
            throw new RuntimeException("Stack Underflow Exception");
        }
        return stack[top--];
    }

    public int size(){
        return top+1;
    }

    public int peek(){
        return stack[top];
    }

    public String toString(){
        return Arrays.toString(stack);
    }
}

class Controller{
    public static void main(String[] args) {
        CustomStack stack = new CustomStack(4);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);

        System.out.println(stack.peek());

        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());

//        System.out.println(stack.pop());

        System.out.println(stack);
    }
}