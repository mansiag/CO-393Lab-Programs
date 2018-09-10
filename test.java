import java.util.*;

public class test{
    
    int n,m;
    ArrayList<String> terms;
    ArrayList<String> vect;
    ArrayList<String> temp1;

	public static void main(String args[]){
		test t = new test();
		t.terms = new ArrayList<String>();
		t.temp1 = new ArrayList<String>();
		t.terms.add(new String("hello"));
		t.change();
        System.out.println(t.terms.get(0));
        System.out.println(t.temp1.get(0));
	}
   
   void change(){
   	 temp1.add("tolu");
   }
}