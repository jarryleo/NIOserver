package cn.leo.nio.db;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Mydao {
	// �޸�����
	public static void update(String name, String password) {
		MysqlHelper mh = new MysqlHelper();
		try {
			mh.setPreparedStatement("update person set password = ? where name = ?");
			mh.setString(1, password);
			mh.setString(2, name);
			mh.update();// ִ���޸�
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mh.close();
		}
	}

	// ɾ�����ݿ��¼
	public static void delete(String name) { // ɾ��
		MysqlHelper mh = new MysqlHelper();
		try {
			mh.setPreparedStatement("delete from person where name = ?");
			mh.setString(1, name);
			mh.delete();// ִ��ɾ��
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mh.close();
		}
	}

	// ��Ӽ�¼
	public static int insert(String username, String password, String name,
			int age, String sex) { // ����
		int result = -1;
		MysqlHelper mh = new MysqlHelper();
		try {
			mh.setPreparedStatement("insert into person values(null,?,?,?,?,?)");
			mh.setString(1, username);
			mh.setString(2, password);
			mh.setString(3, name);
			mh.setint(4, age);
			mh.setString(5, sex);
			result = mh.insert();// ִ�в���,���ؽ��
		} catch (SQLException e) {
			System.out.println("ע��ʧ��");
		} finally {
			mh.close();
		}
		return result;
	}

	// ��ȡ��¼���
	public static String login(String user, String password) {
		MysqlHelper mh = new MysqlHelper();// �������ݿ⹤����
		try {
			// ���Ԥ�������
			mh.setPreparedStatement("select * from person where username = ? and password = ?");
			mh.setString(1, user); // ���ò���
			mh.setString(2, password);// ���ò���
			if (mh.checkLogin()) {
				return mh.getName();
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mh.close(); // �ͷ���Դ
		}
		return null;
	}

	// ��ѯ���ݿ�
	public static void select() {
		MysqlHelper mh = new MysqlHelper();// �������ݿ⹤����
		try {
			mh.setPreparedStatement("select * from person"); // ���Ԥ�������
			ResultSet rs = mh.select(); // ִ�в�ѯ����
			MysqlHelper.show(rs); // ��ӡ�����
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mh.close(); // �ͷ���Դ
		}
	}
}
