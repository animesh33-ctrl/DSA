package queueImp;

import java.util.Arrays;

public class CustomQueue {

    private int size;
    private int[] queue;
    private int front;
    private int rear;

    public CustomQueue() {
        this.queue = new int[10];
        this.front = -1;
        this.rear = -1;
    }
    public CustomQueue(int size) {
        this.size = size;
        this.queue = new int[size];
        this.front = -1;
        this.rear = -1;
    }

    public boolean isFull(){
        return rear==size-1;
    }

    public boolean isEmpty(){
        return front>rear;
    }

    public int enque(int element){
        if(isFull()){
            throw new RuntimeException("Queue is Full");
        }
        queue[++rear] = element;
        front=0;
        return element;
    }

    public int deque(){
        if(isEmpty()){
            throw new RuntimeException("Queue is Empty");
        }
        return queue[front++];
    }

    public String toString(){
        return Arrays.toString(queue);
    }

    public static void main(String[] args) {
        CustomQueue queue = new CustomQueue(10);
        queue.enque(1);
        queue.enque(2);
        queue.enque(3);
        queue.enque(4);
        queue.enque(5);
        queue.enque(6);
        queue.enque(7);
        queue.enque(8);

        System.out.println(queue);

    }
}
