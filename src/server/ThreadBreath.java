package server;

public class ThreadBreath extends Thread{

	ManagerServer myManagerServer;
    public ThreadBreath(ManagerServer myManagerServer) {
    	this.myManagerServer = myManagerServer; 
    }
	public void run() {
		int i = 0;
		int sleepTime = 60000;
		while (true)
		{
			myManagerServer.doApply();
			try 
			{
				sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /*
            if (i<100000)
            {
            	i++;
            }
            else
            {
            	i =  0;
            }*/
		}
	}
	
}
