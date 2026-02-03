package queueImp;

import java.util.LinkedList;
import java.util.Queue;

public class ToDoList {

    public static void main(String[] args) {
        Queue<String> queue = new LinkedList<>();

        queue.offer("A");
        queue.offer("B");
        queue.offer("C");
        queue.offer("D");
        queue.offer("E");
        queue.offer("F");

        System.out.println(queue);
        int n = queue.size();
        for(int i=0;i<n;i++){
            System.out.println(queue.poll());
        }


    }
}
