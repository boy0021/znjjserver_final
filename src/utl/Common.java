package utl;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {

	public static long getNowTime()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//�������ڸ�ʽ
		
		String thistime = ""+df.format(new Date()) ;
		System.out.println(thistime);
		long thistime_l = Long.valueOf( thistime);
		//System.out.println(  thistime_l  );// new Date()Ϊ��ȡ��ǰϵͳʱ��
		return  thistime_l;
	}
}
