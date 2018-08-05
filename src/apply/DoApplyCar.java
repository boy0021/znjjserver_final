package apply;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import model.CMDCar;
import server.ManagerServer;
import utl.WriteLog;
/**所有   车辆  请求入口*/
public class DoApplyCar 
{
	WriteLog myWriteLog;
	ManagerServer myManagerServer;
	/**方便记录日志*/
	HttpServlet myHttpServlet;
	public DoApplyCar(HttpServlet myHttpServlet, ManagerServer myManagerServer) {
		this.myManagerServer = myManagerServer;
		this.myHttpServlet = myHttpServlet;
		
		myWriteLog = new WriteLog(myHttpServlet,"DoApplyCar");
	}

	public void doCarApply(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException 
	{
		myWriteLog.log("doCarApply");
		
		String carid_s = request.getParameter("carid");
		String lat_s = request.getParameter("Lat");
		String lon_s = request.getParameter("Lon");
		String excmd_s = request.getParameter("Excmd");
		String excmdstationid_s = request.getParameter("Excmd_station_id");
		String roadid_s = request.getParameter("roadid");
		String maxseatsize_s = request.getParameter("maxseatsize");
		
		String mes_for_log = "";
		mes_for_log += ("carid_s:"+carid_s+" lat_s:"+lat_s+" lon_s:"+lon_s+" excmd_s:"+excmd_s);
		mes_for_log += ("excmdstationid_s:"+excmdstationid_s+" roadid_s:"+roadid_s+" maxseatsize_s:"+maxseatsize_s);
		
		myWriteLog.log(mes_for_log);
		
		double lat_d =  0;
		double lon_d = 0;
		int excmd_i  = 0;
		int excmdstationid_id  = 0;
		int roadid_id = 0;
		int maxseatsize_i = 0;
		
		/**检测Car的输入是否正确*/
		try 
		{
			 lat_d = Double.valueOf(lat_s);
			 lon_d = Double.valueOf(lon_s);
			
			 excmd_i = Integer.valueOf(excmd_s);
			 excmdstationid_id = Integer.valueOf(excmdstationid_s);
			 roadid_id = Integer.valueOf(roadid_s);
			 maxseatsize_i = Integer.valueOf(maxseatsize_s);
		} 
		catch (Exception e) 
		{
			myWriteLog.log("input date has error");
			return;
		}
		
		try 
		{
			CMDCar myCMDCar = myManagerServer.dealCarCMD(carid_s,excmd_i,excmdstationid_id,roadid_id,maxseatsize_i,lat_d,lon_d);
			writeCar( myCMDCar,response);
		} 
		catch (Exception e) 
		{
			myWriteLog.print(e.toString());
			myWriteLog.log("do dealCarCMD occur error!");
		}
	}

	/**返回信息*/
	private void writeCar(CMDCar myCMDCar, HttpServletResponse response) 
	throws ServletException, IOException 
	{
		String mes = "";
		response.setContentType("text/html");
		JSONObject jsonobject =  new JSONObject();
		
		jsonobject.put("ret", myCMDCar.ret);
		jsonobject.put("Cmd", myCMDCar.cmd);
		jsonobject.put("Aim_lat1", myCMDCar.aim1lat);
		jsonobject.put("Aim_lon1", myCMDCar.aim1lon);
		jsonobject.put("Aim1_station_id", myCMDCar.aim1stationid);
		jsonobject.put("Aim2_station_id", myCMDCar.aim2stationid);
		jsonobject.put("Aim_lat2", myCMDCar.aim2lat);
		jsonobject.put("Aim_lon2", myCMDCar.aim2lon);
		mes = ("ret:"+ myCMDCar.ret+",Cmd:"+ myCMDCar.cmd+
			",Aim_lat1:"+ myCMDCar.aim1lat+",Aim_lon1:"+ myCMDCar.aim1lon);
		
		myWriteLog.log(mes);
		response.getWriter().println(jsonobject.toString());		
	}
}
