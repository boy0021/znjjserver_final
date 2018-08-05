package apply;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import config.Config;
import model.CMDCar;
import model.CMDUser;
import model.Station;
import net.sf.json.JSONObject;
import server.ManagerServer;
import sun.util.logging.resources.logging;
import utl.WriteLog;
/**所有   用户  请求入口*/
public class DoApplyUser {
	WriteLog myWriteLog;
	ManagerServer myManagerServer;
	/**方便记录日志*/
	HttpServlet myHttpServlet;
	public DoApplyUser(HttpServlet myHttpServlet, ManagerServer myManagerServer) {
		this.myManagerServer = myManagerServer;
		this.myHttpServlet = myHttpServlet;
		
		myWriteLog = new WriteLog(myHttpServlet,"DoApplyUser");
	}

	public void doUserApply(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException 
	{
		myWriteLog.log("doUserApply");
		
		String excmd_s = request.getParameter("excmd");
		int excmd_i = -1;
		try 
		{
			excmd_i = Integer.valueOf(excmd_s);
		}
		catch (Exception e) 
		{
		}
		
		switch (excmd_i) 
		{
		case 1://申请订单
			doApplyOrder(request,response);
			break;
		case 2://上车完成
			doAbroadCarOk(request,response);
			break;
		case 3://下车完成
			doDownCarOk(request,response);
			break;
		case 4://呼吸
			doHUXI(request,response);
			break;
		case 5://请求车站
			doApplyStation(request,response);
			break;
		default:
		}
	}
	/**写返回值*/
	private void writeResponse(CMDUser myCMDUser, HttpServletResponse response) 
	throws ServletException, IOException 
	{
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		JSONObject jsonobject =  new JSONObject();
		
		String mes = "";
		switch (myCMDUser.cmd) 
		{
		//-2,约车失败，-1,异常，  1等待，2，到达请上车，3上车完成（行驶中），4，到达目的请下车，5下车完成，订单结束   6获取站 ，7呼吸
		case 7:
			jsonobject.put("ret", 1);
			jsonobject.put("cmd", 7);
			jsonobject.put("cmd2", myCMDUser.cmd2);
			jsonobject.put("carspositionsSize",myCMDUser.carspositions.size());
			for(int i = 0;i<myCMDUser.carspositions.size();i++)
			{
				jsonobject.put("carspositionlat"+i,myCMDUser.carspositions.get(i).lat);
				jsonobject.put("carspositionlon"+i,myCMDUser.carspositions.get(i).lon);
			}
			//myWriteLog.log("----------6666666666666-------------"+myCMDUser.stationpositions.size());
			jsonobject.put("stationNUM",myCMDUser.stationpositions.size() );
			//myWriteLog.log("----------6666666666666-------------"+myCMDUser.stationpositions.size());
			for(int i = 0;i<myCMDUser.stationpositions.size();i++)
			{
				jsonobject.put("stationlat"+i,myCMDUser.stationpositions.get(i).lat);
				jsonobject.put("stationlon"+i,myCMDUser.stationpositions.get(i).lon);
			}
			//myWriteLog.log("----------7777777777777777-------------");
			mes = ("ret"+ 1+"   Cmd"+ 7+" carspositionsSize:"+myCMDUser.carspositions.size()
			+" stationNUM:"+ myCMDUser.stationpositions.size());
			myWriteLog.log(mes);
			break;
			case 6:
				//获取站
				jsonobject.put("ret", 1);
				jsonobject.put("cmd", 6);
				jsonobject.put("user_num", Config.MAXSIZE);
				jsonobject.put("stationname",myCMDUser.mystationlist.get(0).name);
				
				jsonobject.put("stationnameid",myCMDUser.mystationlist.get(0).stationid);
				
				jsonobject.put("go_stationnameNUM",myCMDUser.mystationlist.size()-1);
				
				for(int i = 1;i<myCMDUser.mystationlist.size();i++)
				{
					jsonobject.put("go_stationname"+(i-1),myCMDUser.mystationlist.get(i).name);
					jsonobject.put("go_stationnameid"+(i-1),myCMDUser.mystationlist.get(i).stationid);
				}
				break;
			case 1:
				//订单成功   1等待
				jsonobject.put("ret", 1);
				jsonobject.put("cmd", 1);
				break;
			case -2:
				//申请订单失败
				jsonobject.put("ret", 1);
				jsonobject.put("cmd", -2);
				break;
			case 3:
				//上车成功   上车完成（行驶中）
				jsonobject.put("ret", 1);
				jsonobject.put("cmd", 3);
				break;
			case 4:
				//4，到达目的请下车请下车
				jsonobject.put("ret", 1);
				jsonobject.put("cmd", 4);
				break;
			case 5:
				//5下车完成，订单结束 收到
				jsonobject.put("ret", 1);
				jsonobject.put("cmd", 5);
				break;
			default:
				jsonobject.put("ret", -1);
				jsonobject.put("cmd", myCMDUser.cmd);
				break;
		}
		response.getWriter().println(jsonobject.toString());
	}
	
	/**处理    申请订单*/
	public void doApplyOrder(HttpServletRequest request,HttpServletResponse response)
	throws ServletException, IOException 
	{
		String lat_s = request.getParameter("lat");
		String lon_s = request.getParameter("lon");
		
		String startstationid_s = request.getParameter("startstationid");
		String endstationid_s = request.getParameter("endstationid");
		String num_s = request.getParameter("num");
		String phonecode_s = request.getParameter("pc");
		
		String mes_for_log = "";
		mes_for_log = ("lat_s:"+lat_s+" lon_s:"+lon_s+" startstationid_s:"+startstationid_s);
		mes_for_log += ("endstationid_s:"+endstationid_s+" num_s:"+num_s+" phonecode_s:"+phonecode_s);
		
		myWriteLog.log(mes_for_log);
		
		double lat = -1;
		double lon =  -1;
		//以后要判断距离
		int startstationid = -1;
		int endstationid = -1;
		int num = -1;
		
		CMDUser myCMDUser = new CMDUser();
		try 
		{
			startstationid = Integer.valueOf(startstationid_s);
			endstationid = Integer.valueOf(endstationid_s);
			num = Integer.valueOf(num_s);
			if (myManagerServer!=null)
				myCMDUser = myManagerServer.dealUserCMDApplyCar(phonecode_s,startstationid,endstationid,num);
		} 
		catch (Exception e) 
		{
			System.out.println(":*********************"+e.toString());
		}
		writeResponse( myCMDUser,  response) ;
	}
	/**处理    上车完成*/
	public void doAbroadCarOk(HttpServletRequest request,HttpServletResponse response)
	throws ServletException, IOException 
	{
		CMDUser myCMDUser = new CMDUser();
		
		String orderid_s = request.getParameter("orderid");
		myWriteLog.log("on car  "+"orderid_s:"+orderid_s);
		try 
		{
			myCMDUser = myManagerServer.dealUserCMDOnCar(orderid_s);
		} 
		catch (Exception e) 
		{
			myCMDUser.cmd = -1;
		}
		writeResponse( myCMDUser,  response) ;	
	}
	/**处理    下车完成*/
	public void doDownCarOk(HttpServletRequest request,HttpServletResponse response)
	throws ServletException, IOException 
	{
		CMDUser myCMDUser = new CMDUser();
		String orderid_s = request.getParameter("orderid");
		myWriteLog.log("off car orderid_s:"+orderid_s);
		try 
		{
			myCMDUser = myManagerServer.dealUserCMDOffCar(orderid_s);
		} 
		catch (Exception e) 
		{
			myCMDUser.cmd = -1;
		}
		
		writeResponse( myCMDUser,  response) ;	
	}
	/**处理    呼吸*/
	public void doHUXI(HttpServletRequest request,HttpServletResponse response)
	throws ServletException, IOException 
	{
		CMDUser myCMDUser = new CMDUser();
		String orderid_s = request.getParameter("orderid");
		//myWriteLog.log("---------------------huxi orderid_s:"+orderid_s);
		try 
		{
			//myWriteLog.log("---------------------huxi orderid_s:"+orderid_s);
			myCMDUser = myManagerServer.dealUserCMDHuxi(orderid_s);
			//myWriteLog.log("---------------------huxi orderid_s:"+orderid_s);
		} 
		catch (Exception e) 
		{
			myWriteLog.log("huxi eeeeeee:"+orderid_s+"   |"+e.toString());
		}
		
		writeResponse( myCMDUser,  response) ;
	}
	/**处理    请求车站*/
	public void doApplyStation(HttpServletRequest request,HttpServletResponse response)
	throws ServletException, IOException 
	{
		CMDUser myCMDUser = new CMDUser();
		String codestring = request.getParameter("codestring");
		
		myWriteLog.log("codestring:"+codestring);
		
		if (codestring.length()>0)
		{ 
			myCMDUser = myManagerServer.dealUserCMDApplyStation(codestring);
		}
		else
		{
			myCMDUser.cmd = -1;
		}
		writeResponse( myCMDUser,  response) ;
	}
}