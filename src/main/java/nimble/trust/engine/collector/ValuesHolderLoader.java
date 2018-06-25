package nimble.trust.engine.collector;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nimble.trust.common.Const;
import nimble.trust.common.ValuesHolder;

public class ValuesHolderLoader {

	private static final Logger log = LoggerFactory.getLogger(ValuesHolderLoader.class);
	
	private  String jdbc_url = "jdbc:mysql://localhost/composetrust?user=root&password=";
	
	private Connection conn = null;
	
	private java.sql.PreparedStatement ps = null;
	
	private  String sqlString = "select max(value), t.name from attribute a, attributetype t"
			+ " where t.id = a.typeid"
			+ " group by t.name";
	
	public ValuesHolderLoader() {
		try {
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("trust service aims to connect to: " + jdbc_url);
	}
	
	public  ValuesHolder loadValues() {
		ValuesHolder v = new ValuesHolder();
		ResultSet rs = null;
		try {
			rs = executeSelect();
			while (rs.next()) {
				Integer maxVal = rs.getInt(1);
				String attributeName = rs.getString(2);
				v.setValue(Const.MAX+attributeName, maxVal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				rs.close();
				ps.close();
//				getConnection().close();
			} catch (Exception e) {
				//
			}
		}
	
		return v;
	}
	
	private Connection  getConnection() throws Exception{
		if (conn == null || conn.isClosed() == true || conn.isValid(0) == false){
			 Class.forName("com.mysql.jdbc.Driver").newInstance();
			 conn = DriverManager.getConnection(jdbc_url);
		}
		return conn;
	}
	
	private ResultSet executeSelect() throws Exception{
		if (ps == null || ps.isClosed()){ 
			ps = getConnection().prepareStatement(sqlString);
		}
		return ps.executeQuery();
	}

}
