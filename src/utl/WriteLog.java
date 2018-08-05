package utl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

public class WriteLog {
	/**日志文件的名字  代码文件名字*/
	String rawfilename;
	/**方便记录日志*/
	HttpServlet myHttpServlet;
	public WriteLog(HttpServlet myHttpServlet,String rawfilename) 
	{
		this.myHttpServlet = myHttpServlet;
		this.rawfilename = rawfilename;
	}
	/**写日志*/
	public void log(String mes)
	{
		myHttpServlet.getServletContext().log(rawfilename+":"+mes);
	}
	/**写日志*/
	public void print(String mes)
	{
		System.out.println(rawfilename+":"+mes);
	}
}
