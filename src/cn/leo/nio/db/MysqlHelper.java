package cn.leo.nio.db;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

public class MysqlHelper {
	private static Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private static String path;
	private static String user;
	private static String psw;
	public static int port;
	static {
		Properties p = new Properties();
		try {
			p.load(new FileInputStream("db.properties")); // 读取配置文件
			String driver = p.getProperty("driver"); // 获得驱动全类名
			path = p.getProperty("path");// 获得数据库连接路径
			user = p.getProperty("user");// 获得数据库登陆名
			psw = p.getProperty("password");// 获得数据库登陆密码
			port = Integer.parseInt(p.getProperty("port"));// 服务器端口
			Class.forName(driver);// 加载驱动
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	{
		try {
			conn = DriverManager.getConnection(path, user, psw); // 获取连接
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setPreparedStatement(String sql) throws SQLException {
		ps = conn.prepareStatement(sql); // 设置预编译对象
	}

	public void setString(int index, String arg) throws SQLException {
		ps.setString(index, arg); // 设置参数 先重写两个常用的，需要什么就重写什么
	}

	public void setInt(int index, int arg) throws SQLException {
		ps.setInt(index, arg);// 设置参数 先重写两个常用的，需要什么就重写什么
	}

	public int insert() throws SQLException { // 添加记录
		return ps.executeUpdate();
	}

	public int delete() throws SQLException { // 删除记录
		return ps.executeUpdate();
	}

	public int update() throws SQLException { // 修改记录
		return ps.executeUpdate();
	}

	public ResultSet query() throws SQLException { // 查询记录
		rs = ps.executeQuery();
		return rs;
	}

	public String getName() throws SQLException {
		return rs.getString("name");
	}

	public boolean checkLogin() throws SQLException {
		query();
		return rs.next(); // 判断是否登陆成功
	}

	public static void show(ResultSet rs) throws SQLException { // 打印指定记录集
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		for (int i = 1; i <= count; i++) {
			System.out.print(rsmd.getColumnName(i) + "\t");
		}
		System.out.println();
		while (rs.next()) {
			for (int i = 1; i <= count; i++) {
				System.out.print(rs.getObject(i) + "\t");
			}
			System.out.println();
		}
	}

	public void close() { // 释放资源
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs = null;
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ps = null;
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}
}
