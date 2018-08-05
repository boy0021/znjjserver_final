package model;

import utl.Common;

public class User {
	/**用户名 手机号*/
	public String phonecode;
	/**验证码*/
	public String keycode;
	/**自身的位置  维度*/
	public double lat;
	/**自身的位置  */
	public double lon;
	/**上线时间  */
	public long  onlinetime;
	public User(String phonecode,String keycode) 
	{
		this.phonecode = phonecode;
		this.keycode = keycode;
		
		onlinetime = Common.getNowTime();
	}
	
	@Override
	public String toString() {
		String str = "";
		str += ("phonecode:"+phonecode+"|");
		str += ("keycode:"+keycode+"|");
		return str;
	}
}
