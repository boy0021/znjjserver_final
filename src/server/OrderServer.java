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
	
	/**��������*/
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
	/**������*/
	void deleteCarAllOrder(String carid)
	{
		/**
		 * ɾ�����иó��Ĵ���
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
	/**�õ��ó������ж���*/
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
	
	/**�ı䶩��״̬   ��id �� վid*/
	public void changeCarOrderStatus(String carid,int stationid) 
	{//�Գ�
		System.out.println("1111111111111111111:::::::::::::::::::::::::::::::::::::");
		/**
		 * ������Ŀ�ĵ� ��Ҫô���ó˿��ϳ���ҪôҪ�˿��³�
		 * */
		Iterator <Order> it = myorderlist.iterator();  
		while(it.hasNext())  
		{  
			Order myOrder = it.next();
		    if(myOrder.carid.equals(carid))  
		    {  
		    	//-2,���쳣��-1,���쳣�� 1�ȴ���2���������ϳ���3�ϳ���ɣ���ʻ�У���4������Ŀ�����³���5�³���ɣ��������� 
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
	/**�ı䶩��״̬*/
	public void changeUserOrderStatus(String orderid) 
	{//����
		/**
		 * �ϳ���ɣ��³���ɣ�Ҫ�ı���״̬
		 * */
		Iterator <Order> it = myorderlist.iterator();  
		while(it.hasNext()) 
		{  
			Order myOrder = it.next();
		    if(myOrder.orderid.equals(orderid))  
		    {  
		    	//-2,���쳣��-1,���쳣�� 1�ȴ���2���������ϳ���3�ϳ���ɣ���ʻ�У���4������Ŀ�����³���5�³���ɣ��������� 
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
