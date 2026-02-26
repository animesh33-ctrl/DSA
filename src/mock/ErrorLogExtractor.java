package mock;

import java.util.*;
public class ErrorLogExtractor {
    public static void main(String[] args) {

        List<List<String>> logs = new ArrayList<>();

        logs.add(Arrays.asList("01-01-2023", "14:00", "ERROR", "failed"));
        logs.add(Arrays.asList("01-01-2023", "15:00", "INFO", "established"));
        logs.add(Arrays.asList("01-01-2023", "01:30", "ERROR", "failed"));
        logs.add(Arrays.asList("01-01-2023", "01:30", "ERROR", "disk"));

        List<List<String>> result = extractErrorLog(logs);

//        System.out.println(result);
        for(List<String> curr : result){
            System.out.println(curr);
        }
    }


    private static List<List<String>> extractErrorLog(List<List<String>> logs) {

        List<List<String>> ans = new ArrayList<>();
        for (List<String> curr : logs) {
            if (curr.get(2).equals("ERROR") || curr.get(2).equals("CRITICAL")) {
                ans.add(curr);
            }
        }

        ans.sort((a,b)->{
            String key1 = buildComparable(a.get(0),a.get(1));
            String key2 = buildComparable(b.get(0),b.get(1));

            return key1.compareTo(key2);
        });
        return ans;
    }

    private static String buildComparable(String dateStr, String timeStr) {
        String[] date = dateStr.split("-"); // d-m-yyyy
        String[] time = timeStr.split(":"); // hh:mm

        String day   = date[0].length() == 1 ? "0" + date[0] : date[0];
        String month = date[1].length() == 1 ? "0" + date[1] : date[1];
        String year  = date[2];

        String hour   = time[0].length() == 1 ? "0" + time[0] : time[0];
        String minute = time[1].length() == 1 ? "0" + time[1] : time[1];

        return year + month + day + hour + minute;
    }
}
