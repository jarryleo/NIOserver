package cn.leo.nio.db;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Mydao {
	// 修改密码
	public static void update(String name, String password) {
		MysqlHelper mh = new MysqlHelper();
		try {
			mh.setPreparedStatement("update person set password = ? where name = ?");
			mh.setString(1, password);
			mh.setString(2, name);
			mh.update();// 执行修改
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mh.close();
		}
	}

	// 删除数据库记录
	public static void delete(String name) { // 删除
		MysqlHelper mh = new MysqlHelper();
		try {
			mh.setPreparedStatement("delete from person where name = ?");
			mh.setString(1, name);
			mh.delete();// 执行删除
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mh.close();
		}
	}

	// 添加记录
	public static int insert(String username, String password, String name,
			int age, String sex) { // 插入
		int result = -1;
		MysqlHelper mh = new MysqlHelper();
		try {
			mh.setPreparedStatement("insert into person values(null,?,?,?,?,?)");
			mh.setString(1, username);
			mh.setString(2, password);
			mh.setString(3, name);
			mh.setint(4, age);
			mh.setString(5, sex);
			result = mh.insert();// 执行插入,返回结果
		} catch (SQLException e) {
			System.out.println("注册失败");
		} finally {
			mh.close();
		}
		return result;
	}

	// 获取登录结果
	public static String login(String user, String password) {
		MysqlHelper mh = new MysqlHelper();// 创建数据库工具类
		try {
			// 获得预编译对象
			mh.setPreparedStatement("select * from person where username = ? and password = ?");
			mh.setString(1, user); // 设置参数
			mh.setString(2, password);// 设置参数
			if (mh.checkLogin()) {
				return mh.getName();
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mh.close(); // 释放资源
		}
		return null;
	}

	// 查询数据库
	public static void select() {
		MysqlHelper mh = new MysqlHelper();// 创建数据库工具类
		try {
			mh.setPreparedStatement("select * from person"); // 获得预编译对象
			ResultSet rs = mh.select(); // 执行查询代码
			MysqlHelper.show(rs); // 打印结果集
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mh.close(); // 释放资源
		}
	}
}
