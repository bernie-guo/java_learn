package com.java.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBToolKit {

	/**
	 * ���ݿ�����������
	 */
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";

	/**
	 * �����ַ���
	 */
	private static final String URLSTR = "jdbc:oracle:thin:@localhost:1521:orcl";

	/**
	 * �û���
	 */
	private static final String USERNAME = "Scott";

	/**
	 * ����
	 */
	private static final String USERPASSWORD = "123456";

	/**
	 * �������ݿ�������
	 */
	private static  Connection connnection = null;

	private static  PreparedStatement preparedStatement = null;

	/**
	 * �������������
	 */
	private static  ResultSet resultSet = null;

	static {
		try {
			// �������ݿ���������
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println("������������");
			System.out.println(e.getMessage());
		}
	}

	/**
	 * �������ݿ�����
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		try {
			// ��ȡ����
			connnection = DriverManager.getConnection(URLSTR, USERNAME,
					USERPASSWORD);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return connnection;
	}
    //����������ɾ��
	public static  int executeUpdate(String sql, Object[] params) {
		int affectedLine = 0;
		try {
			connnection = getConnection();
			preparedStatement = connnection.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				preparedStatement.setObject(i + 1, params[i]);
			}

			affectedLine = preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeAll();
		}
		return affectedLine;
	}
	
	//���������Ĳ�ѯ
	public static ResultSet executeQueryRS(String sql) {
		try {
			connnection = getConnection();
			preparedStatement = connnection.prepareStatement(sql);
			
			resultSet = preparedStatement.executeQuery();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return resultSet;
	}

	//�������Ĳ�ѯ
	public static ResultSet executeQueryRS(String sql, Object[] params) {
		try {
			connnection = getConnection();
			preparedStatement = connnection.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				preparedStatement.setObject(i + 1, params[i]);
			}

			resultSet = preparedStatement.executeQuery();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return resultSet;
	}

	/**
	 * ��ȡ������������������List��
	 * 
	 * @param sql
	 *            SQL���
	 * @return List�����
	 */
	public static  List<Object> excuteQuery(String sql, Object[] params) {
		ResultSet rs = executeQueryRS(sql, params);
		ResultSetMetaData rsmd = null;
		int columnCount = 0;
		try {
			rsmd = rs.getMetaData();
			columnCount = rsmd.getColumnCount();
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}

		List<Object> list = new ArrayList<Object>();

		try {
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					map.put(rsmd.getColumnLabel(i), rs.getObject(i));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeAll();
		}

		return list;
	}
	
	/**
	* ���ò�ѯ��洢���̣��˴洢���̵�һ������������out�α����͡�
	* 
	* @param proc_name�洢����
	* @param params�������
	* @return List�����ص����α����͵Ĳ�������ResultSet���н��ա�Ȼ����ת��ΪList����
	*/
	
	public static List<Object> ExecuteProcReturnResult(String proc_name, Object[] params) {

		ResultSet rs = null;
		List<Object> list = new ArrayList<Object>();
		try {
			connnection = getConnection();

			// ���ô洢���̵ĵ�����ʽ��{call DepositbyATM(?,?,?)}
			CallableStatement cs = connnection.prepareCall(proc_name);

			// ��ע���һ������Ϊ������������ұ������α����ͣ����ڴ�ŷ���ֵ��
			cs.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
			// ����������
			for (int i = 0; i < params.length; i++) {
				cs.setObject(i + 2, params[i]);
			}
			// ִ�д洢����
			cs.execute();
			// ��ȡ��һ������ֵ����ǿ��ת��ΪResultSet����
			rs = (ResultSet) cs.getObject(1);
			// ��Result���͵�ת��ΪLIST����
			ResultSetMetaData rsmd = null;
			int columnCount = 0;
			rsmd = rs.getMetaData();
			columnCount = rsmd.getColumnCount();

			// try {
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					map.put(rsmd.getColumnLabel(i), rs.getObject(i));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			// System.out.println(e.getMessage());
		} finally {
			closeAll();
		}
		return list;
	}
	
	/**
	* ���ø�����Ĵ洢���̣���һ����������Ϊ������������ڱ�ʶ�洢�����Ƿ�ִ�гɹ���
	* 
	* @param proc_name�洢����
	* @param params�������
	* @return boolean�����ص��ǲ������ͣ����ڱ�ʶ�洢�����Ƿ�ִ�гɹ���
	*/
	public static boolean ExecuteUpdateProc(String proc_name, Object[] params) {
		
		boolean Success=false;
		try {
			connnection = getConnection();
			
			//���ô洢���̵ĵ�����ʽ��{call DepositbyATM(?,?,?)}
			CallableStatement   cs =connnection.prepareCall(proc_name);
			
			//��ע���һ������Ϊ������������ڴ�ŷ���ֵ����ʶ�Ƿ��޸ĳɹ�
			cs.registerOutParameter(1, java.sql.Types.INTEGER); 
			//����������
			for (int i = 0; i < params.length; i++) {
				//cs.setObject(i+1, params[i]);
				cs.setObject(i+2, params[i]);
			}
			//ִ�д洢����
			cs.executeUpdate();
			//���ݴ洢���̵ķ���ֵ������һ�������ж��Ƿ�ҵ������ɹ���
			if (cs.getInt(1)>0){
				Success =true;
			}
			else
				Success=false;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeAll();
		}
		return Success;
		
	}
	
	/**
	* ���ø�����洢����
	* 
	* @param proc_name�洢����
	* @param params�������
	* @return void�����÷����κ�ֵ�����������ֻ��ͨ��������в�׽��
	*/
	public static void ExecuteProcVoid(String proc_name, Object[] params) {
		
		try {
			connnection = getConnection();
			
			//���ô洢���̵ĵ�����ʽ��{call DepositbyATM(?,?)}
			CallableStatement   cs =connnection.prepareCall(proc_name);
			
			//��Ӳ���
			for (int i = 0; i < params.length; i++) {
				cs.setObject(i+1, params[i]);
			}
			//ִ�д洢����
			cs.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeAll();
		}
	}
	//����û�в����Ĵ洢���̣��ɹ�����true�����򷵻�false,���޸�
	public static boolean ExecuteProc(String proc_name) {
		
		boolean Success=true;
		try {
			connnection = getConnection();
			
			//���ô洢���̵ĵ�����ʽ��{?=call DepositbyATM(?,?)}
			CallableStatement   cs =connnection.prepareCall(proc_name);
			cs.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeAll();
		}
		return Success;
		
	}


	private static  void closeAll() {

		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

		if (connnection != null) {
			try {
				connnection.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}