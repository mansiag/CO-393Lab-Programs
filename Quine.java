import java.util.*;
import java.awt.*;
import javax.swing.*;

// to store (row,col) pair which contains X in table initially
class Pair{
     public int key;
     public int value;
     Pair(int k,int v){
     	key = k;
     	value = v;
     }
     public int getKey(){
     	return key;
     }
     public int getValue(){
     	return value;
     }
}

class Tabu{
    
    int n,m,row,col;
    int kmap[];
    ArrayList<String> terms;  // stores terms in binary form
    ArrayList<String> vect;   // temporary vector 1 to find prime implicants
    ArrayList<String> temp1;  // temporary vector 2 to find prime implicants
    ArrayList<String> prime;  // holds prime implicants
    ArrayList<String> ep;     // holds all the minimized terms
    ArrayList<Integer> temp2; // holds already selected terms
    int a[];

	Tabu(int m){
        
        this.m = m;
	    int i,j,k=0,l,count;
		Scanner sc = new Scanner(System.in);
		System.out.println("enter the number of terms");
		n = sc.nextInt();
		a = new int[n];
		System.out.println("Enter the terms");
		for(i=0;i<n;i++){
			a[i] = sc.nextInt();
		}
		terms = new ArrayList<String>(n);
		vect = new ArrayList<String>(n);
		temp1 = new ArrayList<String>();
		prime = new ArrayList<String>();
		temp2 = new ArrayList<Integer>();
		ep = new ArrayList<String>();
		for(i=0;i<n;i++){
			terms.add(to_binary(a[i],m));
			vect.add(to_binary(a[i],m));
		}

        // Line 54 to 95 finds prime implicants
		while(vect.size()!=0){
			for(i=0;i<vect.size()-1;i++){
				int c = 0;
				for(j=i+1;j<vect.size();j++){
					l = compare(vect.get(i),vect.get(j));
					if(l!=-1 && l!=-2){
						temp1.add(change(vect.get(i),l));
						temp2.add(new Integer(j));
					}
					else c++;
				}
				if(c==j-i-1){
					int d = 0;
					for(k=0; k<temp2.size(); k++){
						if(temp2.get(k).intValue()==i){
							d = 1;
							break;
						}
					}
					if(d == 0){
						check_like(vect.get(i));
					}
				
				}

			}
			int d=0;
			for(k=0;k<temp2.size();k++){
				if(i==(temp2.get(k).intValue())){
					d=1;
					break;
				}
			}
			if(d==0)check_like(vect.get(i));
			vect.clear();
			for(i=0;i<temp1.size();i++){
				vect.add(temp1.get(i));
	   		}
			temp1.clear();
			temp2.clear();
		}

        // create prime implicant table
		row = prime.size();
		col = n;
		int[][] table = new int [row+1][col];
		for(i=0;i<row+1;i++){
			for(j=0;j<col;j++)
				table[i][j]=0;
		}

        // crossing in the table
		for(i=0;i<row;i++){
			int c;
			int n1=terms.size();
			int m1=prime.get(i).length();
			for( j=0;j<n1;j++){
				c=1;
				for( k=0;k<m1;k++){
					if(prime.get(i).charAt(k)!='_'){
						if(prime.get(i).charAt(k)!=terms.get(j).charAt(k)){c=0;break;}
					}
				}
   				if(c==1)table[i][j]=1;
			}
 		}

 		ArrayList<Pair> temp3 = new ArrayList<Pair>();
 		for(j=0;j<col;j++){
 			count=0;
			for(i=0;i<row;i++){
				if(table[i][j]==1){
					count++;k=i;
				}
			}
   			if(count==1){
   				temp3.add(new Pair(j,k));
   			}
 		}

 		// Line 135 to 151 finds the essential prime implicants
 		for(i=0;i<temp3.size();i++){
 			if(table[row][temp3.get(i).getKey()]!=1){
 				ep.add(prime.get(temp3.get(i).getValue()));
 				table[temp3.get(i).getValue()][temp3.get(i).getKey()]=-1;
    			table[row][temp3.get(i).getKey()]=1;
    			for(j=0;j<col;j++){
    				if(table[temp3.get(i).getValue()][j]==1){
    					for(k=0;k<row;k++){
    						if(table[k][j]==1)
								table[k][j]=-1;
            			}
           				table[row][j]=1;
          			}
      			}
			}
		}
		temp3.clear();

		// Now temp3 holds the count of minterms for a row (count,rowth number)
		for(i=0;i<row;i++){
			count=0;
			for(j=0;j<col;j++){
				if(table[i][j]==1)
    			count++;
  			}
  			if(count>0){
  				temp3.add(new Pair(count,i));
  			}
		}
        
        // sorting on the basis of number of unselected terms each row(prime implicant) can cover
		temp3.sort(new Comparator<Pair>(){
			@Override
			public int compare(Pair o1, Pair o2){
				if (o1.getKey() >= o2.getKey()) {
                	return 1;
            	}
            	else{
                	return -1;
           		 }
       		 }
		});

		// if there is  term left unselected
        if(temp3.size()!=0)
		{	
			// selecting the prime implicant which covers maximum remaining terms
			i=temp3.size()-1;
			{ ep.add(prime.get(temp3.get(i).getValue()));
  				for(j=0;j<col;j++){
  					if(table[temp3.get(i).getValue()][j]==1)
      				{ for(k=0;k<row;k++)
        				{ if(table[k][j]==1)
           					{table[k][j]=-1;}
       					}
      				}
   				 }
			}
			// removing that term from vector
			temp3.remove(temp3.size()-1);

            // selecting the prime implicant again and again which covers maximum remaining terms .
			while(temp3.size()!=0){

 				for(i=0;(i<temp3.size() && i>=0);i++){
 					count=0;
					for(j=0;j<col;j++){
						if(table[temp3.get(i).getValue()][j]==1)
    					count++;
  					}	
					if(count>0){
						temp3.get(i).key=count;
					}
					else
						temp3.get(i).key=0;
 				 }

				temp3.sort(new Comparator<Pair>(){
				@Override
				public int compare(Pair o1, Pair o2){
					if (o1.getKey() >= o2.getKey()) {
                		return 1;
            		}
            		else{
                		return -1;
           		 	}
       		 	}
				});

 				i=temp3.size()-1;
 				if(temp3.get(i).getKey()>0){
 					ep.add(prime.get(temp3.get(i).getValue()));
					for(j=0;j<col;j++){
						if(table[temp3.get(i).getValue()][j]==1){
							for(k=0;k<row;k++){
								if(table[k][j]==1){
           							table[k][j]=-1;
           						}
       						}
     					}
    				}				
				}
				else 
					break;
			}
//Printing minimized terms.
			System.out.println("Minimized terms are");
			for(i=0;i<ep.size();i++){
				for(j=0;j<m;j++){
					if(ep.get(i).charAt(j)!='_')
					{System.out.print((char)(j+65));
					if(ep.get(i).charAt(j) == '0')System.out.print('`');}
					
				} 
			 	System.out.print(" + ");             
			}
		}
		else{
			System.out.println("Minimized terms are");
			for(i=0;i<ep.size();i++){
				for(j=0;j<m;j++){
					if(ep.get(i).charAt(j)!='_'){
					System.out.print((char)(j+65));
					if(ep.get(i).charAt(j)=='0')System.out.print('`');
					}
				} 
			System.out.print(" + ");  
			}
		}
	}

	String to_binary(int num, int m){
		char[] b = new char[m];
		for(int i=m-1;i>=0;i--){
			if(num%2==0) b[i] = '0';
			else b[i]= '1';
			num = num/2;
		}
		return new String(b);
	}

	String change(String a,int l){
		char[] b = a.toCharArray();
   		b[l] = '_';
   		return new String(b);
	}

	int compare(String a,String b){
		int n=a.length(), c=0,d=0,k=0;
		for(int i=0;i<n;i++){
			if(a.charAt(i)!=b.charAt(i)){c++;k=i;}
			else if(a.charAt(i)==b.charAt(i)){d++;}
		}
		if(c==1) return k;
		else if (d==n) return -2;
		else return -1;
	}

	void check_like(String a){
		int flag=0;
		int n = prime.size();
		for(int i=0;i<n;i++){
			if(compare(a,prime.get(i))==-2)return;
		else flag++;
		}
		if(flag==n){
			prime.add(a);
		}
		return;
	}
	public void createkMap(){
	    kmap = new int [(1 << m)];
        ArrayList<String> v = new ArrayList<>();
        ArrayList<String> h = new ArrayList<>();

        for (int i = 0; i < (1 << m/2); i++) {
            v.add(String.format("%0$"+ m/2 + "s",
                            Integer.toBinaryString(i ^ (i >> 1)))
                            .replace(" ", "0"));
        }

        for (int i = 0; i < (1 << (m - m/2)); i++) {
            h.add(String.format("%0$"+ (m - m/2) + "s",
                            Integer.toBinaryString(i ^ (i >> 1)))
                            .replace(" ", "0"));
        }

        int count = 0;
        for (int i = 0; i < v.size(); i++) {
            for (int j = 0; j < h.size(); j++) {
                kmap[count] = Integer.parseInt(
                            (v.get(i) + h.get(j)), 2);
            count++;
            }
        }
    }

    public String getKMapValue(Integer i) {
            return String.valueOf(kmap[i]);
    }
    public Integer getNo_ofMinterm() {
    	return n;
    }
    public Integer getMinterms(Integer i) {
    	return a[i];
    }

    public Integer GreyCode(Integer i){
    	int inv = 0;
    	for(;i!=0;i=i>>1){
    		inv ^= i;
    	}
    	return inv;
    }

}

public class Quine{

	static int m;
	public static void main(String args[]){

		JFrame f = new JFrame();
		// JPanel kmap = new JPanel();
		int n,j,l,count;
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter the number of variables");
		m = sc.nextInt();
		int row = m/2;
	    int col = (m+1)/2;
	    int kmap_row = (int)Math.pow(2,row);
	    int kmap_col = (int)Math.pow(2,col);
	    
	    Tabu t = new Tabu(m);
        t.createkMap();

        GridLayout grid =  new GridLayout(kmap_row,kmap_col);
		JButton block[] = new JButton[1<<m];
        
        for(int i=0; i<(1<<m); i++){
        	
        		block[i] = new JButton(t.getKMapValue(i));
        		block[i].setBackground(Color.CYAN);
				block[i].setOpaque(true);
				f.add(block[i]);        	
        }
        for (int i = 0; i < t.getNo_ofMinterm(); i++) {
        	int k = t.getMinterms(i);
        	block[Integer.parseInt(t.getKMapValue(k))].setBackground(Color.RED);
        }

		f.setLayout(grid);
		f.setSize(500,500);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	
}