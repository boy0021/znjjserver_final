package config;

public class Config {
	/**�������   ��һ�����������������ÿ������ĳ�û�й�ϵ*/
	public static int MAXSIZE = 8;
	public static int  MaxStationId = 1000000;
	
	/***/
	public static int  CARSTATUS_STOP = 1;
	public static int  CARSTATUS_MOVE = 2;
	
	/**���ݿ�ip*/
	public static String DBIP = "121.42.199.164";
	/**���ݿ��û���*/
	public static String DBUser = "znjj";
	/**���ݿ�����*/
	public static String DBKey = "znjj123!_lijusheng";
	
	/**���ݿ�  carinfer �����ݿ�*/
	public static String DBname = "ZNJJ";
	
	/**�����������������(����)*/
	public static int maxConnNUM = 200;
	
	/**��֤��������������*/
	public static int CODEmaxConnNUM = 10;
	
	/**��ѯվ��sql*/
	public static String sql_select_stations = "SELECT * FROM station WHERE onFlag='1'  ORDER BY station_id ";
	public static String sql_select_roadids = "SELECT DISTINCT roadid FROM station WHERE onFlag='1' ";
	
	public static String sql_select_stations1 = "SELECT * FROM station WHERE onFlag='1' AND roadid='";
	public static String sql_select_stations2 = "' ORDER BY station_id";
	
	
	
	/*******************�������**********************************/
	/** ����ʧ�ܣ�·�Ų����ڵ�����*/
	public static int error_code100000 = -100000;
	/** �ڹ�����ͣ�Ĺ����У���������*/
	public static int error_code100001 = -100000;
}
