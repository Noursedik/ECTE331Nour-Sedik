package Task2;


class Data{
	int A1,A2,A3,B1,B2,B3;
	boolean gotoB2=false;
	boolean gotoA2=false;
	boolean gotoB3=false;
	boolean gotoA3=false;
}

class Utility{
	public static int calculate(int n) {
		return(n*(n+1)/2);
	}
}


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
		System.out.println("A1 completed"+data.A1);
		data.gotoB2=true;
		data.notify();
		}
		
		
		//Function B2 block
		synchronized(data) {
			try {
				while (data.gotoB2==false) {
					data.wait();
					System.out.println("I am in B2 waiting for A1 to send notification");
					}
				data.B2=data.A1+Utility.calculate(200);
				System.out.println("A2 completed"+data.B2);
				data.gotoA2=true;
				data.notify();
				} catch (InterruptedException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		
		//Function A2 block
		synchronized(data) {
			try {
			while (data.gotoA2==false) {
				data.wait();
				System.out.println("I am in A2 waiting for B2 to send notification");
				}
			data.A2=data.B2+Utility.calculate(300);
			System.out.println("A2 completed"+data.A2);
			data.gotoB3=true;
			data.notify();
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

	}

}
