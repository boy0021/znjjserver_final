package apply;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import server.ManagerServer;
import utl.WriteLog;
import net.sf.json.JSONObject;

/**所有请求入口*/
public class Apply extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
		if (first)
		{
			myManagerServer = new ManagerServer(this);
			myWriteLog = new WriteLog(this,"Apply");
			myDoApplyCar = new DoApplyCar(this,myManagerServer);
			myDoApplyUser = new DoApplyUser(this,myManagerServer);
			first = false;
		}
		
	response.setContentType("text/html");
	String mylogstr = "";
	JSONObject jsonobject =  new JSONObject();
	try 
	{
		String thisApplyid = request.getParameter("Applyid");
		mylogstr += ( "thisApplyid:"+thisApplyid+";");
		int thisApplyid_i = -1;
		try 
		{
			thisApplyid_i = Integer.parseInt(thisApplyid);
		}
		catch (Exception e) 
		{
		}
		//myWriteLog.log("Applyid : "+thisApplyid_i);
		switch (thisApplyid_i) 
		{
			case 1://车
				myWriteLog.log("Applyid : 1");
				myDoApplyCar.doCarApply( request,  response);
				break;
			case 2://人
				myWriteLog.log("Applyid : 2");
				myDoApplyUser.doUserApply(request, response);
				break;
			case 3://车载
				myWriteLog.log("Applyid : 3");
				myManagerServer.CarAndroid(request, response);
				break;
			case 4://测试
				myWriteLog.log("Applyid : 4");
				myManagerServer.doTestApply(request, response);
				break;
			case 5://申请 验证 验证码
				myWriteLog.log("Applyid : 5");
				myManagerServer.UserOnline(request, response);
				break;
			default:
				myWriteLog.log("inputs error ; ret:-1");
				jsonobject.put("ret", -1);
				response.getWriter().println(jsonobject.toString());
		} 
	} 
	catch (Exception e) 
	{
		myWriteLog.log("inputs error ;ret:-1");
		jsonobject.put("ret", -1);
		response.getWriter().println(jsonobject.toString());
	}
}
	
	ManagerServer myManagerServer;
	
	WriteLog myWriteLog;
	
	DoApplyCar myDoApplyCar;
	DoApplyUser myDoApplyUser;
	
	boolean first;
	/**********************************************************************************************************************/
	public Apply() {
		super();
		first = true;
	}
	public void destroy() {
		super.destroy(); 
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}
	public void init() throws ServletException {
	}
}
