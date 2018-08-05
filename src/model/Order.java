package model;

public class Order {
	/**订单开始站*/
	public int orderStartStationId;
	/**订单结束站*/
	public int orderEndStationId;
	/**订单的座位数量   本订单申请的数量  */
	public int orderseatsize;
	/**车号*/
	public String carid;
	/**订单号   目前同手机号*/
	//public int orderid;
	public String orderid;
	/**手机号*/
	public int phonecode;
	/**  -2,车异常，-1,人异常，  1等待，2，到达请上车，3上车完成（行驶中），4，到达目的请下车，5下车完成，订单结束 */
	public int status;
	
	@Override
	public String toString() {
		String mes = "";
		mes += ("orderStartStationId:"+orderStartStationId+"|");
		mes += ("orderEndStationId:"+orderEndStationId+"|");
		mes += ("orderseatsize:"+orderseatsize+"|");
		mes += ("carid:"+carid+"|");
		mes += ("orderid:"+orderid+"|");
		mes += ("phonecode:"+phonecode+"|");
		switch (status) {
		case -2:
			mes += ("status:"+status+"车异常|");
			break;
		case -1:
			mes += ("status:"+status+"人异常|");
			break;
		case 1:
			mes += ("status:"+status+"等待车辆|");
			break;
		case 2:
			mes += ("status:"+status+"到达请上车|");
			break;
		case 3:
			mes += ("status:"+status+"上车完成（行驶中）|");
			break;
		case 4:
			mes += ("status:"+status+"到达目的请下车|");
			break;
		case 5:
			mes += ("status:"+status+"下车完成，订单结束 |");
			break;
		default:
			break;
		}
		
		return mes;
	}
}
