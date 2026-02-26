package _pr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MergeIntervals {
    public static void mergeIntervals(int[][] intervals){
        if(intervals == null) return;
        Arrays.sort(intervals,(a,b)->a[0]-b[0]);
        List<int[]> res = new ArrayList<>();

        int[] curr = intervals[0];

        for(int i=1;i<intervals.length;i++){
            if(intervals[i][0] <= curr[1]){
                curr[1] = Math.max(curr[1],intervals[i][1]);
            }
            else{
                res.add(curr);
                curr = intervals[i];
            }
        }
        res.add(curr);
        for(int[] i : res)
            System.out.println(Arrays.toString(i));
    }

    public static void main(String[] args) {
        int[][] intervals = {{1,3},{2,6},{8,10},{12,15}};
        mergeIntervals(intervals);
    }
}
