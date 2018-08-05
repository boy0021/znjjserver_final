package utl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

public class WriteLog {
	/**��־�ļ�������  �����ļ�����*/
	String rawfilename;
	/**�����¼��־*/
	HttpServlet myHttpServlet;
	public WriteLog(HttpServlet myHttpServlet,String rawfilename) 
	{
		this.myHttpServlet = myHttpServlet;
		this.rawfilename = rawfilename;
	}
	/**д��־*/
	public void log(String mes)
	{
		myHttpServlet.getServletContext().log(rawfilename+":"+mes);
	}
	/**д��־*/
	public void print(String mes)
	{
		System.out.println(rawfilename+":"+mes);
	}
}
