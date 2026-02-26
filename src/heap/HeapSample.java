package heap;

import java.util.*;

public class HeapSample {
    public static void main(String[] args) {

        /*

        parent = (i-1)/2;
        left child = 2*i + 1
        right child = 2*i + 2  //this is for array indexing of parent and childs

         */

        PriorityQueue<Integer> pq = new PriorityQueue<>();

        pq.offer(1);
        pq.offer(0);
        pq.offer(2);
        pq.offer(6);
        pq.offer(9);
        pq.offer(8);

        System.out.println(pq);

//        for(Integer q:pq){
//            System.out.println(q);
//        }
//
        List<Integer> list = new ArrayList<>();
        while(!pq.isEmpty()){   // nlogn
            list.add(pq.poll());
        }

        System.out.println(list);
//        Iterator<Integer> it = pq.iterator();
//        while (it.hasNext()){
//            System.out.println(it.next());
//        }

    }
}

//@interface MyAnnotation{  // My Custom Annotation
//
//}.