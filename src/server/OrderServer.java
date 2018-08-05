package server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Car;
import model.Order;

public class OrderServer {

	List<Order> myorderlist;
	
	CarServer myCarServer;
	UserServer myUserServer;
	
	@Override
	public String toString() {
		String mes = "";
		mes += ("Orders num: "+myorderlist.size()+";");
		Iterator <Order> it = myorderlist.iterator();  
		while(it.hasNext())  
		{  
			mes += (it.next().toString()+",");
		} 
		return mes;
	}
	
	public OrderServer(CarServer myCarServer,UserServer myUserServer) {
		
		myorderlist = new ArrayList<Order>();
		
		this.myCarServer = myCarServer;
		this.myUserServer = myUserServer;
	}
	
	void changeOrder()
	{
	}
	
	/**创建订单*/
	void createOrder(String carid,String usercode,int startid ,int endid,int num)
	{
		Order myOrder = new Order();
		
		myOrder.carid = carid;
		myOrder.orderEndStationId = endid;
		myOrder.orderid = usercode;
		myOrder.orderseatsize = num;
		myOrder.orderStartStationId = startid;
		myOrder.status = 1;
		
		myorderlist.add(myOrder);
	}
	/**车下线*/
	void deleteCarAllOrder(String carid)
	{
		/**
		 * 删掉所有该车的代码
		 * */
		Iterator <Order> it = myorderlist.iterator();  
		while(it.hasNext())  
		{  
		    if(it.next().carid.equals(carid))  
		    {  
		        it.remove(); 
		    }  
		} 
	}
	/**得到该车的所有订单*/
	public List<Order> getCarOrderS(String carid)
	{
		List<Order> mytemorderlist =  new ArrayList<Order>();
		
		Iterator <Order> it = myorderlist.iterator();  
		while(it.hasNext())  
		{  
			Order myOrder = it.next();
		    if(myOrder.carid.equals(carid))  
		    {  
		    	mytemorderlist.add(myOrder);
		    }  
		} 
		return mytemorderlist;
	}
	
	public int getCarNextOrderStation(String carid) 
	{
		return 0;
	}
	public int getOrderStatus(String orderid)
	{
		int status = -1;
		
		Iterator <Order> it = myorderlist.iterator();  
		while(it.hasNext())  
		{  
			Order myOrder = it.next();
		    if(myOrder.orderid.equals(orderid) )  
		    {  
		    	return myOrder.status;
		    }  
		} 
		
		return status;
	}
	
	/**改变订单状态   车id ， 站id*/
	public void changeCarOrderStatus(String carid,int stationid) 
	{//对车
		System.out.println("1111111111111111111:::::::::::::::::::::::::::::::::::::");
		/**
		 * 车到达目的地 ，要么是让乘客上车，要么要乘客下车
		 * */
		Iterator <Order> it = myorderlist.iterator();  
		while(it.hasNext())  
		{  
			Order myOrder = it.next();
		    if(myOrder.carid.equals(carid))  
		    {  
		    	//-2,车异常，-1,人异常， 1等待，2，到达请上车，3上车完成（行驶中），4，到达目的请下车，5下车完成，订单结束 
		    	if (myOrder.status==1 && myOrder.orderStartStationId==stationid)
		    	{
		    		myOrder.status=2;
		    	}
		    	if (myOrder.status==3 && myOrder.orderEndStationId==stationid)
		    	{
		    		System.out.println(":::::::::::::::::::::::::::::::::::::");
		    		myOrder.status=4;
		    	}
		    }  
		} 
	}
	/**改变订单状态*/
	public void changeUserOrderStatus(String orderid) 
	{//对人
		/**
		 * 上车完成，下车完成，要改变其状态
		 * */
		Iterator <Order> it = myorderlist.iterator();  
		while(it.hasNext()) 
		{  
			Order myOrder = it.next();
		    if(myOrder.orderid.equals(orderid))  
		    {  
		    	//-2,车异常，-1,人异常， 1等待，2，到达请上车，3上车完成（行驶中），4，到达目的请下车，5下车完成，订单结束 
		    	if (myOrder.status==2 )
		    	{
		    		myOrder.status=3;
		    	}
		    	if (myOrder.status==4 )
		    	{
		    		myOrder.status=5;
		    	}
		    }  
		} 
		
		it = myorderlist.iterator();  
		while(it.hasNext())  
		{  
		    if(it.next().status == 5)  
		    {  
		        it.remove();  
		    }  
		} 
	}
}
