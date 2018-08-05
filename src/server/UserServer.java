package server;

import java.util.ArrayList;
import java.util.List;

import model.CMDUser;
import model.Car;
import model.Order;
import model.User;

public class UserServer {
	List<User> myorderlist;
	
	public UserServer() {
		// TODO Auto-generated constructor stub
		
		myorderlist = new ArrayList<User>();
	}
	@Override
	public String toString() 
	{
		String str = "";
		str = "User_size:"+myorderlist.size()+";";
		for (int i = 0;i<myorderlist.size();i++)
		{
			str += ( myorderlist.get(i).toString()+",");
		}
		return str;
	}
	/***/
	
	/**是否已经上线*/
	public boolean isExists(String phonecode)
	{
		boolean flag = false;
		
		for (int i = 0;i<myorderlist.size();i++)
		{
			if(myorderlist.get(i).phonecode.equals(phonecode))
			{
				return true;
			}
		}
		return flag;
	}
	/***/
	public String findcode(String phonecode)
	{
		for (int i = 0;i<myorderlist.size();i++)
		{
			if(myorderlist.get(i).phonecode.equals(phonecode))
			{
				return myorderlist.get(i).keycode;
			}
		}
		return "";
	}
	/**验证这个验证码   相等返回true*/
	public boolean compareCode(String phonecode,String keycode)
	{
		boolean flag = false;
		
		for (int i = 0;i<myorderlist.size();i++)
		{
			if(myorderlist.get(i).phonecode.equals(phonecode))
			{
				if (keycode.equals(myorderlist.get(i).keycode))
				{
					return true;
				}
			}
		}
		return flag;
	}
	/**增加这个用户*/
	public String addUser(String phonecode,String keycode) {
		if (isExists( phonecode))
		{
			return findcode(phonecode);
		}
		User myUser = new User(phonecode,keycode);
		
		myorderlist.add(myUser);
		
		return keycode;
	}
}
