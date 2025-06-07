package Task2;


class Data{
	int A1,A2,A3,B1,B2,B3,C;
	boolean gotoB2=false;
	boolean gotoA2=false;
	boolean gotoB3=false;
	boolean gotoA3=false;
	boolean gotoC=false;
}
//sum method in utility class 
class Utility{
	public static int calculate(int n) {
		return(n*(n+1)/2);
	}
}

//Thread A
class ThreadA extends Thread{
	private Data data;

	public ThreadA(Data data) {
		super();
		this.data = data;
	}
	
	public void run() {
		
		//Function A1 block
		synchronized(data) {
		data.A1=Utility.calculate(500);
		System.out.println("A1 completed "+data.A1);
		data.gotoB2=true; 
		data.notify();//notifying B2
		}
		
		
		//Function A2 block
		synchronized(data) {
			try {
			while (data.gotoA2==false) {
				data.wait();
				System.out.println("I am in A2 waiting for B2 to send notification");
				}
			data.A2=data.B2+Utility.calculate(300);
			System.out.println("A2 completed "+data.A2);
			data.gotoB3=true; 
			data.notifyAll();//notifying B3
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			
		}
		
		//Function A3 block
		synchronized(data) {
			try {
				while (data.gotoA3==false) {
					data.wait();
					System.out.println("I am in A3 waiting for B3 to send notification");
				}
				data.A3=data.B3+Utility.calculate(400);
				System.out.println("A3 completed "+data.A3);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}
	}
	
//Thread B
class ThreadB extends Thread{
	private Data data;

	public ThreadB(Data data) {
		super();
		this.data = data;
	}
public void run() {
		
		//Function B1 block
		synchronized(data) {
		data.B1=Utility.calculate(250);
		System.out.println("B1 completed "+data.B1);
		}
		
		//Function B2 block
		synchronized(data) {
			try {
				while (data.gotoB2==false) {
					data.wait();
					System.out.println("I am in B2 waiting for A1 to send notification");
					}
				data.B2=data.A1+Utility.calculate(200);
				System.out.println("B2 completed "+data.B2);
				data.gotoA2=true;
				data.notifyAll();// notify A2
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		//Function B3 block
		synchronized(data) {
			try {
				while (data.gotoB3==false) {
					data.wait();
					System.out.println("I am in B3 waiting for A2 to send notification");
				}
				data.B3=data.A2+Utility.calculate(400);
				System.out.println("B3 completed "+data.B3);
				data.gotoC=true;
				data.gotoA3=true;
				data.notifyAll(); //notify A3 and C
				} catch (InterruptedException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}
}

class ThreadC extends Thread{
	private Data data;

	public ThreadC(Data data) {
		super();
		this.data = data;
	}
	public void run() {
		synchronized(data) {
			try {
				while (data.gotoC==false) {
					data.wait();
					System.out.println("I am in Thread C waiting for B3 to send notification");
				}
				data.C=data.A2+data.B3;
				System.out.println("C completed "+data.C);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}
	}

public class Question3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//running the threads multiple times 
		for(int i=0; i<5;i++) {
		Data data=new Data();
		ThreadA threadA=new ThreadA(data); //creating ThreadA
		ThreadB threadB=new ThreadB(data);//creating ThreadB
		ThreadC threadC=new ThreadC(data);//creating ThreadC
		
		threadA.start();
		threadB.start();
		threadC.start();
		// Wait for all threads to finish
					try {
						threadA.join();
						threadB.join();
						threadC.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	}
	}

}
