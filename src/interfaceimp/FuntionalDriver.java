package interfaceimp;

public class FuntionalDriver implements FunctionalInterfaceImp{

    public void add(){
        System.out.println("Add method in subclass");
    }

    public boolean equals(Object o){
        return o==null;
    }
}
