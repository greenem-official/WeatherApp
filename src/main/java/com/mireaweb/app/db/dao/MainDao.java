package com.mireaweb.app.db.dao;

import com.mireaweb.app.db.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainDao extends AbstractDAO {
	private static volatile MainDao INSTANCE;
	
	public static MainDao getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new MainDao();
		}
		return INSTANCE;
	}
	
	private MainDao() {}
	
	public boolean ifRowExists(String db, Object keyName, Object key) throws SQLException {
		int result = 0;
		Connection connection = getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT EXISTS(SELECT 1 FROM " + db + " WHERE " + keyName + "=? LIMIT 1)");
        preparedStatement.setObject(1, key);

        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()) {
        	result = rs.getInt(1);
        }
        CloseConnections(rs, preparedStatement, connection);
		if (result == 0) {
			return false;
		}
		else {
			return true;
		}
	}

	public boolean ifRowExists(String db, Object... keysAndValues) throws SQLException {
		int result = 0;
		Connection connection = getConnection();

		String condition  = "";

		for(int i = 0; i < keysAndValues.length; i++) {
			if(i%2==0) {
				condition +=keysAndValues[i] + "=?"; // =\"
			}
			else {
				//condition +=keysAndValues[i] + "\"";
				if(i+1<keysAndValues.length) {
					condition += " AND ";
				}
			}
		}

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT EXISTS(SELECT 1 FROM " + db + " WHERE " + condition + " LIMIT 1)");

        int prstmtNum = 1;
        for(int i = 0; i < keysAndValues.length; i++) {
        	if(i%2==1) {
        		preparedStatement.setObject(prstmtNum, keysAndValues[i]);
        		prstmtNum++;
        	}
        }

        //System.out.println(preparedStatement.toString());

        //if(1==1) return false;

        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()) {
        	result = rs.getInt(1);
        }
        CloseConnections(rs, preparedStatement, connection);
		if (result == 0) {
			return false;
		}
		else {
			return true;
		}
	}

	public boolean ifValueExists(String db, Object... keysAndValues) throws SQLException {
		int result = 0;
		Connection connection = getConnection();

		String condition = "";

		for(int i = 0; i < keysAndValues.length; i++) {
			if(i%2==0) {
				condition +=keysAndValues[i] + "=?"; // =\"
			}
			else {
				if(i+1<keysAndValues.length) {
					condition += " AND ";
				}
			}
		}

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT EXISTS(SELECT 1 FROM " + db + " WHERE " + condition + " LIMIT 1)");

        int prstmtNum = 1;
        for(int i = 0; i < keysAndValues.length; i++) {
        	if(i%2==1) {
        		preparedStatement.setObject(prstmtNum, keysAndValues[i]);
        		prstmtNum++;
        	}
        }

        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()) {
        	result = rs.getInt(1);
        }
        CloseConnections(rs, preparedStatement, connection);
		if (result == 0) {
			return false;
		}
		else {
			return true;
		}
	}

	public void setAnything(String db, Object mainOName, Object mainO, Object... keysAndValues) throws SQLException {
		if(keysAndValues.length%2==1) {
			return;
		}
		Connection connection = getConnection();

		Object[] mainSs = new Object[2];
		mainSs[0] = mainOName;
		mainSs[1] = mainO;

		String vars = mainOName.toString() + ", ";
		String values = "?, ";
		String condition = "";

		for(int i = 0; i < keysAndValues.length; i++) {
			if(i%2==0) {
				vars += keysAndValues[i];
				if(2+(i+1)<keysAndValues.length) {
					vars += ", ";
				}
				condition +=keysAndValues[i] + "=?";
			}
			else {
				values += "?";
				if(i+1<keysAndValues.length) {
					values += ", ";
				}
				if(i+1<keysAndValues.length) {
					condition += ", ";
				}
			}
		}

		boolean exists = ifRowExists(db, mainSs);
		if(exists == false) {
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + db + "(" + vars + ") VALUES(" + values + ")");

			preparedStatement.setObject(1, mainO);
			int prstmtNum = 2;
	        for(int i = 0; i < keysAndValues.length; i++) {
	        	if(i%2==1) {
	        		preparedStatement.setObject(prstmtNum, keysAndValues[i]);
	        		prstmtNum++;
	        	}
	        }

			preparedStatement.executeUpdate();
			CloseConnections(null, preparedStatement, connection);
		}
		else {
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + db + " SET " + condition + " WHERE " + mainOName + "=?");

			int prstmtNum = 1;
	        for(int i = 0; i < keysAndValues.length; i++) {
	        	if(i%2==1) {
	        		preparedStatement.setObject(prstmtNum, keysAndValues[i]);
	        		prstmtNum++;
	        	}
	        }
	        preparedStatement.setObject(prstmtNum, mainO);

			preparedStatement.executeUpdate();
			CloseConnections(null, preparedStatement, connection);
		}
	}

	public String getAnything(String db, String mainSName, String mainS, String valueSName) throws SQLException {
		String result = null;
		Connection connection = getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT " + valueSName + " FROM " + db + " WHERE " + mainSName + "=?");
        preparedStatement.setString(1, mainS);

        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()) {
            //System.out.println(valueSName);
        	result = rs.getString(valueSName);
        }
        CloseConnections(rs, preparedStatement, connection);
		return result;
	}

	public HashMap<Object, Object> getAnythingToHashMap(String db, String mainSName, String mainS, String... valueSName) throws SQLException {
		HashMap<Object, Object> result = new HashMap<Object, Object>();
		Connection connection = getConnection();

		String values = "";

		for (int i=0; i<valueSName.length; i++) {
			values+=valueSName[i];
			if(i+1<valueSName.length) {
				values+=", ";
			}
		}

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT " + values + " FROM " + db + " WHERE " + mainSName + "=?");
        preparedStatement.setString(1, mainS);

        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()) {
        	for (int i=0; i<valueSName.length; i++) {
        		result.put(valueSName[i], rs.getString(valueSName[i]));
        	}
        }
        CloseConnections(rs, preparedStatement, connection);
		return result;
	}

	public ArrayList<Object> getAnything(String db, String mainSName, String mainS, String... valueSName) throws SQLException {
		ArrayList<Object> result = new ArrayList<Object>();
		Connection connection = getConnection();

		String values = "";

		for (int i=0; i<valueSName.length; i++) {
			values+=valueSName[i];
			if(i+1<valueSName.length) {
				values+=", ";
			}
		}

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT " + values + " FROM " + db + " WHERE " + mainSName + "=?");
        preparedStatement.setString(1, mainS);

        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()) {
        	for (int i=0; i<valueSName.length; i++) {
        		result.add(rs.getString(valueSName[i]));
        	}
        }
        CloseConnections(rs, preparedStatement, connection);
		return result;
	}

	public void deleteAnything(String db, String... keysAndValues) throws SQLException {
		if(keysAndValues.length%2==1) {
			return;
		}
		Connection connection = getConnection();
		
		String condition  = "";
		
		for(int i = 0; i < keysAndValues.length; i++) {
			if(i%2==0) {
				condition +=keysAndValues[i] + "=\"";
			}
			else {
				condition +=keysAndValues[i] + "\"";
				if(i+1<keysAndValues.length) {
					condition += " AND ";
				}
			}
		}
		
		//System.out.println(condition);
		
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + db + " WHERE " + condition);
		
		//System.out.println(preparedStatement.toString());
		
		preparedStatement.executeUpdate();
		CloseConnections(null, preparedStatement, connection);
	}
	
	public static void main(String[] args) {
		try {
			//MainDao.getInstance().setPassword("GREEN3M", "rfn");
			//System.out.println(MainDao.getInstance().getPassword("GREEN3M"));
			//MainDao.getInstance().deletePlayer("GREEN3M");
			//MainDao.getInstance().setAnything("players_authentication", "name", "GREEN3M", "lastLoginAdress", "12324233");
			//System.out.println(MainDao.getInstance().getAnything("players_authentication", "name", "GREEN3M", "lastLoginAdress"));
			
//			String[] s1= new String[2];
//			s1[0] = "password";
//			s1[1] = "lastLoginAdress";
//			System.out.println(MainDao.getInstance().getAnything("players_authentication", "name", "GREEN3M", s1));

			//MainDao.getInstance().deleteAnything("players_authentication", "name", "GREEN3M", "password", "rfn");
			//MainDao.getInstance().deleteAnything("players_authentication", "name", "GREEN3M", "password", "ahahah");
			//MainDao.getInstance().deleteAnything("players_authentication", "name", "GREEN3M");
			//MainDao.getInstance().setAnything("players_authentication", "name", "GREEN3M", "password", "ya", "lastLoginAdress", "1232.4233", "lastLoginPort", "1523");
			
			//MainDao.getInstance().setAnything("players_authentication", "name", "GREEN3M", "password", "yat", "lastLoginAdress", "1232.4233", "lastLoginPort", "1523");
			//MainDao.getInstance().ifRowExists("players_authentication", "name", "GREEN3M", "password", "yat", "lastLoginAdress", "1232.4233", "lastLoginPort", "1523");
			//MainDao.getInstance().setAnything("players_profiles", "name", "GREEN3M2", "lastCustomLoginTime", "10102");
			//String s = MainDao.getInstance().getAnything("players_profiles", "name", "GREEN3M", "lastCustomLoginTime");
			//System.out.println(s);
			//System.out.println(MainDao.getInstance().getAnythingToHashMap("players_authentication", "name", "GREEN3M", "password", "lastLoginAdress")); 
			//System.out.println(MainDao.getInstance().ifRowExists("players_authentication", "name", "GREEN3M", "password", "no"));
			//System.out.println(MainDao.getInstance().ifValueExists("players_authentication", "name", "GREEN3M", "password", "no", "lastLoginPort", ""));
//			long date = 0;
//			System.out.println(date);
			//System.out.println(MainDao.getInstance().getAnything("players_authentication", "name", "GREEN3M2", "lastLoginAdress") == null);
			//System.out.println(MainDao.getInstance().getAnything("players_authentication", "name", "GREEN3M", "password", "lastLoginPort").get(1));
			System.out.println(MainDao.getInstance().getAnything("players_authentication", "name", "GREEN3M", "password").equals("nO"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void CloseConnections(ResultSet rs, PreparedStatement preparedStatement, Connection connection) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException sqlEx) {
			} // ignore
		}

		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException sqlEx) {
			} // ignore
		}

		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException sqlEx) {
			} // ignore
		}
	}
}