package stackImplementation;

public class ReverseString {

    public static String revereString(String str){
        StringBuilder st = new StringBuilder();
        for(int i=0;i<str.length();i++){
            st.insert(0,str.charAt(i));
        }
        return st.toString();
    }

    public static void main(String[] args) {
        String str = "abcde";
        System.out.println(revereString(str));
    }
}