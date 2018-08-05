package server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Car;
import model.MyPosition;

/**存储所有车辆的所有信息*/
public class CarServer {
	List<Car> mycarlist;
	
	public Car getCarInfor(String carid)
	{
		Car temp = null;
		
		for (int i = 0;i<mycarlist.size();i++)
		{
			if (mycarlist.get(i).carid.equals(carid))
			{
				temp = mycarlist.get(i);
				break;
			}
		}
		
		return temp;
	}
	
	/**toString */
	@Override
	public String toString() 
	{
		String str = "";
		
		str = "mycarlist_size:"+mycarlist.size()+";";
		for (int i = 0;i<mycarlist.size();i++)
		{
			str += (mycarlist.get(i).toString()+",");
		}
		return str;
	}
	
	public CarServer() {
		mycarlist = new ArrayList<Car>();
	}
	
	/**上线*/
	public void carOnline(Car car)
	{
		mycarlist.add(car);
	}
	/**下线*/
	public void carOffline(String carid) 
	{
		Iterator <Car> it = mycarlist.iterator();  
		while(it.hasNext())  
		{  
		    if(it.next().carid.equals(carid))  
		    {  
		        it.remove();  
		    }  
		} 
	}
	/**通过carid取 其对象*/
	public Car getCarFromid(String carid) 
	{
		Iterator <Car> it = mycarlist.iterator();  
		while(it.hasNext())  
		{  
			Car temp = it.next(); 
			if(temp.carid.equals(carid))  
		    {  
		        return temp;
		    }  
		} 
		return null;
	}

	public List<MyPosition> getcarPosition() {
		List<MyPosition> tempMyPosition = new ArrayList<MyPosition>();
		for (int i = 0;i<mycarlist.size() ;i++)
		{
			MyPosition temp  = new MyPosition();
			temp.lat = mycarlist.get(i).nowLat;
			temp.lon = mycarlist.get(i).nowLon;
			if (temp.lat>0)
			{
				//System.out.println("--------------------------------"+temp.lat+"  "+temp.lon);
				tempMyPosition.add(temp);
			}
		}
		return tempMyPosition;
	}
}
