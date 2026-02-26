package genericclass;

class Student{
    int id;
    String name;
    int[] marks;

    public Student(int id, String name, int[] marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
    }
}

public class CompareUtil{
    static  boolean compareString(String s1,String s2){
        //compare full length of string for null,length
        if(s1 == null & s2 == null) return true;
        if(s1==null || s2==null) return false;
        if(s1.length() != s2.length()) return false;

        //
        for(int i=0;i<s1.length();i++){
            if(s1.charAt(i) != s2.charAt(i)) return false;
        }
        return true;
    }

    static boolean compareIntArray(int[] a,int[] b){
        if(a==null && b==null) return true;
        if(a==null || b==null) return false;
        if(a.length != b.length) return false;

        for(int i=0;i<a.length;i++){
            if(a[i] != b[i]){
                return false;
            }
        }
        return true;
    }

    static boolean compareStudent(Student s1,Student s2){
        if(s1==s2) return true;
        if (s1 == null || s2 == null) return false;
        if(s1.id != s2.id) return false;
        if(!compareString(s1.name,s2.name)) return false;
        return compareIntArray(s1.marks, s2.marks);
    }
}

interface Compare<T>{
    int compare(T a,T b);
}

interface KeyExtractor<T,K>{
    K extract(T obj);
}

class Comparator{
    static int compare(Object a,Object b){
        if(a == b) return 0;
        if(a==null) return -1;
        if(b== null) return -1;

        if(a instanceof Integer && b instanceof Integer){
            return (int)a-(int)b;
        }
        if(a instanceof String s1 && b instanceof String s2){

            int len = Math.min(s1.length(),s2.length());
            for(int i=0;i<len;i++){
                if(s1.charAt(i) != s2.charAt(i)){
                    return s1.charAt(i) - s2.charAt(i);
                }
            }
            return s1.length() - s2.length();
        }

        throw new RuntimeException("Unsupported Type");
    }
}

class Sorter<T>{
    T[] arr;
    KeyExtractor<T,?> extractor;
    boolean ascending = true;

    public Sorter(T[] arr, KeyExtractor<T, ?> extractor) {
        this.arr = arr;
        this.extractor = extractor;
    }

    Sorter<T> asc(){
        ascending = true;
        sort();
        return this;
    }

    Sorter<T> desc(){
        ascending = false;
        sort();
        return this;
    }

    void sort(){
        int n = arr.length;

        for(int i=0;i<n-1;i++){
            for(int j=0;j<n-i-1;j++){
                Object v1 = extractor.extract(arr[j]);
                Object v2 = extractor.extract(arr[j+1]);

                int cmp = Comparator.compare(v1,v2);
                if((ascending && cmp > 0) || (!ascending && cmp <0)){
                    T temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }
}

class ArrayWrapper<T>{
    T[] arr;
    public ArrayWrapper(T[] arr){
        this.arr = arr;
    }

    Sorter<T> sort(KeyExtractor<T,?> extractor){
        return new Sorter<>(arr,extractor);
    }
}

class Student2{
    int id;
    String name;
    int age;

    public Student2(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student2{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}'+"\n";
    }
}

class Main2{
    public static void main(String[] args) {
        Student2[] student2s = {
                new Student2(1, "Animesh", 23),
                new Student2(2, "Rohit", 21),
                new Student2(3, "Sneha", 22),
                new Student2(4, "Priya", 20),
                new Student2(5, "Amit", 24)
        };

        ArrayWrapper<Student2> arrayWrapper = new ArrayWrapper<>(student2s);
        arrayWrapper.sort(s->s.name).asc();

        for(Student2 s : student2s){
            System.out.print(s);
        }
    }
}