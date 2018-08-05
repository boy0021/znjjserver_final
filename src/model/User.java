package model;

import utl.Common;

public class User {
	/**�û��� �ֻ���*/
	public String phonecode;
	/**��֤��*/
	public String keycode;
	/**�����λ��  ά��*/
	public double lat;
	/**�����λ��  */
	public double lon;
	/**����ʱ��  */
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
