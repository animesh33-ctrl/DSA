package queueImp;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class QueueImp {
    public static void main(String[] args) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(1);
        queue.add(2);
        queue.add(3);
        queue.add(4);
        queue.add(5);
        queue.add(6);

        System.out.println(queue);

        System.out.println(queue.poll());
        System.out.println(queue.remove());
        System.out.println(queue.poll());

        System.out.println(queue);
        System.out.println(queue.peek());

        for(Integer i:queue){
            System.out.println(i);
        }

        Iterator<Integer> iterator = queue.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }


    }
}
