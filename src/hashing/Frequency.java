package hashing;

import java.util.HashMap;
import java.util.Map;

public class Frequency {

    public static HashMap<Integer,Integer> frequecy(int[] arr){
        if(arr.length == 0) return null;

        HashMap<Integer,Integer> map = new HashMap<>();

        for(int i=0;i<arr.length;i++){
            map.put(arr[i],map.getOrDefault(arr[i],0)+1); //return the value if present otherwise null
        }
        return map;
    }

    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5,6,7,3,4};

        HashMap<Integer,Integer> ans = frequecy(arr);

        assert ans != null;
        for(Map.Entry<Integer,Integer> entry:ans.entrySet()){
            System.out.println(entry.getKey()+" "+entry.getValue());
        }

    }
}
