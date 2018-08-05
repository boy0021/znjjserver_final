package utl;

public class RandomCode {
	public static String getRandNumCode() 
	{
		int min = 1;
		int max = 999999;
	    int randNum = min + (int)(Math.random() * ((max - min) + 1));
	    String randNum_s = ""+randNum;
	    while(randNum_s.length()<6)
	    {
	    	randNum_s = "0"+randNum_s;
	    }
	    return randNum_s;
	}
}
