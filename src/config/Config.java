package config;

public class Config {
	/**最大人数   这一批车的最大人数，和每个具体的车没有关系*/
	public static int MAXSIZE = 8;
	public static int  MaxStationId = 1000000;
	
	/***/
	public static int  CARSTATUS_STOP = 1;
	public static int  CARSTATUS_MOVE = 2;
	
	/**数据库ip*/
	public static String DBIP = "121.42.199.164";
	/**数据库用户名*/
	public static String DBUser = "znjj";
	/**数据库密码*/
	public static String DBKey = "znjj123!_lijusheng";
	
	/**数据库  carinfer 的数据库*/
	public static String DBname = "ZNJJ";
	
	/**正常的最大连接数量(车辆)*/
	public static int maxConnNUM = 200;
	
	/**验证码的最大连接数量*/
	public static int CODEmaxConnNUM = 10;
	
	/**查询站的sql*/
	public static String sql_select_stations = "SELECT * FROM station WHERE onFlag='1'  ORDER BY station_id ";
	public static String sql_select_roadids = "SELECT DISTINCT roadid FROM station WHERE onFlag='1' ";
	
	public static String sql_select_stations1 = "SELECT * FROM station WHERE onFlag='1' AND roadid='";
	public static String sql_select_stations2 = "' ORDER BY station_id";
	
	
	
	/*******************错误代码**********************************/
	/** 上线失败，路号不存在等问题*/
	public static int error_code100000 = -100000;
	/** 在过而不停的过程中，出现问题*/
	public static int error_code100001 = -100000;
}
