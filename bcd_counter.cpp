// This program will run only in Linux 

#include<iostream>
#include<graphics.h> 
#include<unistd.h>

using namespace std;

int Not(int x){
	if(x) return 0;
	return 1;
}

class SRflipflop{
   
    private:
   		int R;
   		int S;
   		int Q;

    public:
     
    	SRflipflop(){
        	Q = 0;
        } 

    	void initQ(int q){
     		Q = q;
     	}

   	 	void set(unsigned int r, unsigned int s){
   	 		R = r;
   	 		S = s;
        	calculate();
   	 	}

   	 	unsigned int getQ(){
   	 		return Q;
   		}

   	 	void calculate(){
   	 		Q = S | (Not(R) & Q);
   	 	}

};

void drawff(){
	rectangle(20, 50, 120, 150);
	outtextxy(35, 65, "Ra");
	outtextxy(85, 65, "Sa");
	outtextxy(35, 120, "Qa");
	outtextxy(85, 120, "Qa'");
	rectangle(145, 50, 245, 150);
	outtextxy(160, 65, "Rb");
	outtextxy(210, 65, "Sb");
	outtextxy(160, 120, "Qb");
	outtextxy(210, 120, "Qb'");
	rectangle(270, 50, 370, 150);
	outtextxy(285, 65, "Rc");
	outtextxy(335, 65, "Sc");
	outtextxy(285, 120, "Qc");
	outtextxy(335, 120, "Qc'");
	rectangle(395, 50, 495, 150);
	outtextxy(410, 65, "Rd");
	outtextxy(460, 65, "Sd");
	outtextxy(410, 120, "Qd");
	outtextxy(460, 120, "Qd'");
}

int main(){
   
	SRflipflop A, B, C, D;
   	int gd = DETECT,gm,left=100,top=100,right=200,bottom=200;
   	initgraph(&gd,&gm,NULL);
   	int x = 10, y = 300, count = 0, maxx = getmaxx(), ans;
   	cout<<"Which type of counter do you want?\n1)Upcounter\n2)Downcounter";
   	cin>>ans;
  	if(ans==2){
   		A.initQ(1);
   	 	D.initQ(1);
   	}

   	drawff();
   	while(1){
    	for(int i = 0; i < 30; i++){
    		circle(x, y - i, 0);
       		delay(10);
     	}
    	unsigned int a = A.getQ();
     	unsigned int b = B.getQ();
     	unsigned int c = C.getQ();
     	unsigned int d = D.getQ();
     	cout<<a<<b<<c<<d<<endl;

     	if(ans == 1){
     		A.set(Not(c) & d, b & c & d);
     		B.set(b & c & d, Not(b) & c & d);
     		C.set(c & d, Not(a) & Not(c) & d);
     		D.set(d, Not(d));
    	}

    	else{
 	   		A.set(a & Not(d), Not(a) & Not(b) & Not(c) & Not(d));
       		B.set(Not(a) & Not(c) & Not(d),a & Not(d));
       		C.set(c & Not(d), (b & Not(c) & Not(d)) | (a & Not(c) & Not(d)));
       		D.set(d,Not(d));
     	}

     	y -= 29; 
     	for(int i = 0;i < 30; i++){
     		circle(x + i, y,0);
       		delay(10);
     	}

     	x += 29;
     	for(int i = 0; i < 30; i++){
     		circle(x, y+i, 0);
       		delay(10); 
     	}

     	y += 29;
     	for(int i = 0; i < 30; i++){
     		circle(x + i, y, 0);
       		delay(10);
     	}

     	x+=29;
     	if(x+59>maxx){
     		x = 10;
     		y+=100;
     	}
    }
 	return 0;
}