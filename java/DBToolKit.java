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
	 * 数据库驱动类名称
	 */
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";

	/**
	 * 连接字符串
	 */
	private static final String URLSTR = "jdbc:oracle:thin:@localhost:1521:orcl";

	/**
	 * 用户名
	 */
	private static final String USERNAME = "Scott";

	/**
	 * 密码
	 */
	private static final String USERPASSWORD = "123456";

	/**
	 * 创建数据库连接类
	 */
	private static  Connection connnection = null;

	private static  PreparedStatement preparedStatement = null;

	/**
	 * 创建结果集对象
	 */
	private static  ResultSet resultSet = null;

	static {
		try {
			// 加载数据库驱动程序
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println("加载驱动错误");
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 建立数据库连接
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		try {
			// 获取连接
			connnection = DriverManager.getConnection(URLSTR, USERNAME,
					USERPASSWORD);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return connnection;
	}
    //带参数的增删改
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
	
	//不带参数的查询
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

	//带参数的查询
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
	 * 获取结果集，并将结果放在List中
	 * 
	 * @param sql
	 *            SQL语句
	 * @return List结果集
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
	* 调用查询类存储过程，此存储过程第一个参数必须是out游标类型。
	* 
	* @param proc_name存储过程
	* @param params所需参数
	* @return List，返回的是游标类型的参数，用ResultSet进行接收。然后再转变为List类型
	*/
	
	public static List<Object> ExecuteProcReturnResult(String proc_name, Object[] params) {

		ResultSet rs = null;
		List<Object> list = new ArrayList<Object>();
		try {
			connnection = getConnection();

			// 设置存储过程的调用形式：{call DepositbyATM(?,?,?)}
			CallableStatement cs = connnection.prepareCall(proc_name);

			// 先注册第一个参数为输出参数，并且必须是游标类型，用于存放返回值。
			cs.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
			// 添加其余参数
			for (int i = 0; i < params.length; i++) {
				cs.setObject(i + 2, params[i]);
			}
			// 执行存储过程
			cs.execute();
			// 获取第一个参数值，并强制转换为ResultSet类型
			rs = (ResultSet) cs.getObject(1);
			// 将Result类型的转换为LIST类型
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
	* 调用更新类的存储过程，第一个参数必须为输出参数，用于标识存储过程是否执行成功。
	* 
	* @param proc_name存储过程
	* @param params所需参数
	* @return boolean，返回的是布尔类型，用于标识存储过程是否执行成功。
	*/
	public static boolean ExecuteUpdateProc(String proc_name, Object[] params) {
		
		boolean Success=false;
		try {
			connnection = getConnection();
			
			//设置存储过程的调用形式：{call DepositbyATM(?,?,?)}
			CallableStatement   cs =connnection.prepareCall(proc_name);
			
			//先注册第一个参数为输出参数，用于存放返回值，标识是否修改成功
			cs.registerOutParameter(1, java.sql.Types.INTEGER); 
			//添加其余参数
			for (int i = 0; i < params.length; i++) {
				//cs.setObject(i+1, params[i]);
				cs.setObject(i+2, params[i]);
			}
			//执行存储过程
			cs.executeUpdate();
			//根据存储过程的返回值，即第一个参数判断是否业务操作成功。
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
	* 调用更新类存储过程
	* 
	* @param proc_name存储过程
	* @param params所需参数
	* @return void，不用返回任何值，如果有问题只能通过错误进行捕捉。
	*/
	public static void ExecuteProcVoid(String proc_name, Object[] params) {
		
		try {
			connnection = getConnection();
			
			//设置存储过程的调用形式：{call DepositbyATM(?,?)}
			CallableStatement   cs =connnection.prepareCall(proc_name);
			
			//添加参数
			for (int i = 0; i < params.length; i++) {
				cs.setObject(i+1, params[i]);
			}
			//执行存储过程
			cs.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeAll();
		}
	}
	//调用没有参数的存储过程，成功返回true，否则返回false,待修改
	public static boolean ExecuteProc(String proc_name) {
		
		boolean Success=true;
		try {
			connnection = getConnection();
			
			//设置存储过程的调用形式：{?=call DepositbyATM(?,?)}
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