package model;

public class Order {
	/**������ʼվ*/
	public int orderStartStationId;
	/**��������վ*/
	public int orderEndStationId;
	/**��������λ����   ���������������  */
	public int orderseatsize;
	/**����*/
	public String carid;
	/**������   Ŀǰͬ�ֻ���*/
	//public int orderid;
	public String orderid;
	/**�ֻ���*/
	public int phonecode;
	/**  -2,���쳣��-1,���쳣��  1�ȴ���2���������ϳ���3�ϳ���ɣ���ʻ�У���4������Ŀ�����³���5�³���ɣ��������� */
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
			mes += ("status:"+status+"���쳣|");
			break;
		case -1:
			mes += ("status:"+status+"���쳣|");
			break;
		case 1:
			mes += ("status:"+status+"�ȴ�����|");
			break;
		case 2:
			mes += ("status:"+status+"�������ϳ�|");
			break;
		case 3:
			mes += ("status:"+status+"�ϳ���ɣ���ʻ�У�|");
			break;
		case 4:
			mes += ("status:"+status+"����Ŀ�����³�|");
			break;
		case 5:
			mes += ("status:"+status+"�³���ɣ��������� |");
			break;
		default:
			break;
		}
		
		return mes;
	}
}
