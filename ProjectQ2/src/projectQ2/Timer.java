package projectQ2;

class Timer{
private double savedTime;
public Timer(){
resetTimer();
}
public void resetTimer(){
savedTime=System.nanoTime();
}

public double elapsedTime(){
double eTime;
eTime=(System.nanoTime()-savedTime)/1000000;
return eTime;
}
}