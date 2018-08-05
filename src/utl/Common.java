package utl;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {

	public static long getNowTime()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
		
		String thistime = ""+df.format(new Date()) ;
		System.out.println(thistime);
		long thistime_l = Long.valueOf( thistime);
		//System.out.println(  thistime_l  );// new Date()为获取当前系统时间
		return  thistime_l;
	}
}
