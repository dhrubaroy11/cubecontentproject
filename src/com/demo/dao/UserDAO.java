package com.demo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.demo.model.Content;
import com.demo.model.Cube;

public class UserDAO {

	PreparedStatement insertUser = null;
	PreparedStatement insertCube = null;
	PreparedStatement insertContentStmt = null;
	PreparedStatement addContentStmt = null;
	PreparedStatement deleteContentStmt = null;
	PreparedStatement deleteCubeStmt = null;
	PreparedStatement shareCubeStmt = null;
	PreparedStatement shareContentStmt = null;
	PreparedStatement dltSharedCubeStmt = null;

	private JdbcTemplate jdbcTemplate;

	String insertUserSql = "INSERT INTO USERTABLE(ID, USERNAME, CITY) VALUES (SEQ_USER_IID.NEXTVAL , ? , ?)";
	String insertCubeSql = "INSERT INTO CUBE (ID, USERID, NAME) VALUES (SEQ_CUBE_IID.NEXTVAL , ? , ?)";
	String insertContentSql = "INSERT INTO CONTENT (ID, USERID, LINK) VALUES (SEQ_CONTENT_IID.NEXTVAL , ? , ?)";
	String addContentSql = "INSERT INTO RELATION (ID, CUBEID, CONTENTID) VALUES (SEQ_RELATION_IID.NEXTVAL , ? , ?)";
	String deleteContentSql = "DELETE FROM RELATION WHERE CUBEID = ? AND CONTENTID = ?";
	String deleteCubeSql = "DELETE FROM CUBE WHERE ID = ? ";
	String deleteCubeShareSql = "DELETE FROM SHAREDCUBE WHERE CUBEID = ? ";
	String shareCubeSql = "INSERT INTO SHAREDCUBE (ID, CUBEID, USERID) VALUES (SEQ_SHAREDCUBE_IID.NEXTVAL , ? , ?)";
	String shareContentSql = "INSERT INTO SHAREDCONTENT (ID, CONTENTID, USERID) VALUES (SEQ_SHAREDCONTENT_IID.NEXTVAL , ? , ?)";

	private Log log = LogFactory.getLog(this.getClass().getName());

	public UserDAO() {

	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Connection getConnection() {
		Connection conn = null;
		try {
			DataSource ds = jdbcTemplate.getDataSource();
			conn = ds.getConnection();
		} catch (SQLException e) {
			log.error("SQLException occured");
		}

		return conn;
	}

	public List<String> insertUser(String userName, String city) {
		List<String> userDetails = new ArrayList();

		try {
			Connection conn = getConnection();

			insertUser = conn.prepareStatement(insertUserSql);

			insertUser.setString(1, userName);
			insertUser.setString(2, city);
			insertUser.execute();
		} catch (SQLException e) {
			log.error("SQLException occured");
		}

		String id = getUserId(userName, city).toString();
		userDetails.add(id);
		userDetails.add(userName);
		userDetails.add(city);

		return userDetails;

	}

	@SuppressWarnings("deprecation")
	public Long getUserId(String username, String city) {
		Long id = null;

		try {
			String getUserSql = "SELECT ID FROM USERTABLE WHERE USERNAME = ? AND CITY = ?";
			id = jdbcTemplate.queryForLong(getUserSql, new Object[] { username, city });

		} catch (DataAccessException e) {
			log.error("DataAccessException occured");
		}

		return id;
	}

	public List<String> insertCube(long id, String name) {
		List<String> cubeDetails = new ArrayList();

		try {
			Connection conn = getConnection();

			insertCube = conn.prepareStatement(insertCubeSql);

			insertCube.setLong(1, id);
			insertCube.setString(2, name);
			insertCube.execute();
			conn.close();
		} catch (SQLException e) {
			log.error("SQLException occured");
		}
		

		Long cubeId = getCubeId(id, name);
		String userId = Long.toString(id);
		
		shareCube(cubeId, 0, id);
		cubeDetails.add(cubeId.toString());
		cubeDetails.add(userId);
		cubeDetails.add(name);

		return cubeDetails;

	}

	@SuppressWarnings("deprecation")
	public Long getCubeId(long id, String name) {
		Long cubeId = null;

		try {
			String getUserSql = "SELECT ID FROM CUBE WHERE USERID = ? AND NAME = ?";
			cubeId = jdbcTemplate.queryForLong(getUserSql, new Object[] { id, name });

		} catch (DataAccessException e) {
			log.error("DataAccessException occured");
		}

		return cubeId;
	}

	public List<String> insertContent(long id, String link) {
		List<String> contentDetails = new ArrayList();

		try {
			Connection conn = getConnection();

			insertContentStmt = conn.prepareStatement(insertContentSql);

			insertContentStmt.setLong(1, id);
			insertContentStmt.setString(2, link);
			insertContentStmt.execute();
		} catch (SQLException e) {
			log.error("SQLException occured");
		}

		Long contentId = getContentId(id, link);
		String userId = Long.toString(id);
		shareContent(contentId, 0, id);
		contentDetails.add(contentId.toString());
		contentDetails.add(userId);
		contentDetails.add(link);

		return contentDetails;

	}

	@SuppressWarnings("deprecation")
	public Long getContentId(long id, String link) {
		Long contentId = null;

		try {
			String getUserSql = "SELECT ID FROM CONTENT WHERE USERID = ? AND LINK = ?";
			contentId = jdbcTemplate.queryForLong(getUserSql, new Object[] { id, link });

		} catch (DataAccessException e) {
			log.error("DataAccessException occured");
		}

		return contentId;
	}

	public List<String> addContent(long cubeId, long contentId) {
		List<String> contentDetails = new ArrayList();

		try {
			Connection conn = getConnection();

			addContentStmt = conn.prepareStatement(addContentSql);

			addContentStmt.setLong(1, cubeId);
			addContentStmt.setLong(2, contentId);
			addContentStmt.execute();
		} catch (SQLException e) {
			log.error("SQLException occured");
		}

		String relationId = getRelationId(cubeId, contentId).toString();
		String cId = Long.toString(cubeId);
		String cntId = Long.toString(contentId);
		contentDetails.add(relationId);
		contentDetails.add(cId);
		contentDetails.add(cntId);

		return contentDetails;

	}

	@SuppressWarnings("deprecation")
	public Long getRelationId(long cubeId, long contentId) {
		Long relationId = null;

		try {
			String getUserSql = "SELECT ID FROM RELATION WHERE CUBEID = ? AND CONTENTID = ?";
			relationId = jdbcTemplate.queryForLong(getUserSql, new Object[] { cubeId, contentId });

		} catch (DataAccessException e) {
			log.error("DataAccessException occured");
		}

		return relationId;
	}

	public void deleteContentFromCube(long cubeId, long contentId) {

		try {
			Connection conn = getConnection();

			deleteContentStmt = conn.prepareStatement(deleteContentSql);

			deleteContentStmt.setLong(1, cubeId);
			deleteContentStmt.setLong(2, contentId);
			deleteContentStmt.execute();
		} catch (SQLException e) {
			log.error("SQLException occured");
		}

	}

	public void deleteCube(long cubeId) {

		try {
			Connection conn = getConnection();

			deleteCubeStmt = conn.prepareStatement(deleteCubeSql);
			dltSharedCubeStmt = conn.prepareStatement(deleteCubeShareSql);

			deleteCubeStmt.setLong(1, cubeId);
			dltSharedCubeStmt.setLong(1, cubeId);
			deleteCubeStmt.execute();
			dltSharedCubeStmt.execute();
		} catch (SQLException e) {
			log.error("SQLException occured");
		}

	}

	public List<String> shareCube(long cubeId, long userId1, long userId2) {
		List<String> shareDetails = new ArrayList();

		try {
			Connection conn = getConnection();

			shareCubeStmt = conn.prepareStatement(shareCubeSql);

			shareCubeStmt.setLong(1, cubeId);
			shareCubeStmt.setLong(2, userId2);
			shareCubeStmt.execute();
		} catch (SQLException e) {
			log.error("SQLException occured");
		}

		String sharedId = getShareCubeId(userId2, cubeId).toString();
		String cId = Long.toString(cubeId);
		String usrId = Long.toString(userId2);
		shareDetails.add(sharedId);
		shareDetails.add(cId);
		shareDetails.add(usrId);

		return shareDetails;

	}

	@SuppressWarnings("deprecation")
	public Long getShareCubeId(long userId, long cubeId) {
		Long sharedId = null;

		try {
			String getUserSql = "SELECT ID FROM SHAREDCUBE WHERE USERID = ? AND CUBEID = ?";
			sharedId = jdbcTemplate.queryForLong(getUserSql, new Object[] { userId, cubeId });

		} catch (DataAccessException e) {
			log.error("DataAccessException occured");
		}

		return sharedId;
	}

	public List<String> shareContent(long contentId, long userId1, long userId2) {
		List<String> shareDetails = new ArrayList();

		try {
			Connection conn = getConnection();

			shareContentStmt = conn.prepareStatement(shareContentSql);

			shareContentStmt.setLong(1, contentId);
			shareContentStmt.setLong(2, userId2);
			shareContentStmt.execute();
		} catch (SQLException e) {
			log.error("SQLException occured");
		}

		String sharedId = getShareContentId(userId2, contentId).toString();
		String cId = Long.toString(contentId);
		String usrId = Long.toString(userId2);
		shareDetails.add(sharedId);
		shareDetails.add(cId);
		shareDetails.add(usrId);

		return shareDetails;

	}

	@SuppressWarnings("deprecation")
	public Long getShareContentId(long userId, long contentId) {
		Long sharedId = null;

		try {
			String getSharedSqlId = "SELECT ID FROM SHAREDCONTENT WHERE USERID = ? AND CONTENTID = ?";
			sharedId = jdbcTemplate.queryForLong(getSharedSqlId, new Object[] { userId, contentId });

		} catch (DataAccessException e) {
			log.error("DataAccessException occured");
		}

		return sharedId;
	}

	public List<Cube> getAllCube(long userId) {
		String name = "";
		BigDecimal id = null;
		List<Cube> list = new ArrayList<Cube>();

		try {
			String query = "SELECT SC.ID , C.NAME  FROM SHAREDCUBE SC , CUBE C WHERE  SC.CUBEID = C.ID AND SC.USERID = ? ";
			List<Map<String, Object>> cubeList = jdbcTemplate.queryForList(query, new Object[] { userId });

			for (Map<String, Object> row : cubeList) {
				Cube cube = new Cube();
				name = (String) row.get("Name");
				id = (BigDecimal) row.get("ID");
				cube.setId(id.longValue());
				cube.setName(name);
				list.add(cube);
			}

		} catch (DataAccessException e) {
			log.error("DataAccessException occured");
		}

		return list;

	}

	public List<Content> getAllContent(long userId) {
		String name = "";
		BigDecimal id = null;
		List<BigDecimal> idList = new ArrayList<BigDecimal>();
		List<Content> list = new ArrayList<Content>();

		try {
			String query = "SELECT SC.ID , C.LINK  FROM SHAREDCONTENT SC , CONTENT C WHERE  SC.CONTENTID = C.ID AND SC.USERID = ? ";
			List<Map<String, Object>> contentList = jdbcTemplate.queryForList(query, new Object[] { userId });
			String query1 = "SELECT SC.ID , C.LINK  FROM SHAREDCONTENT SC , CONTENT C , SHAREDCUBE SB , RELATION R WHERE  SC.CONTENTID = C.ID AND  SC.CONTENTID = R.CONTENTID AND R.CUBEID = SB.CUBEID AND  SB.USERID = ? ";
			List<Map<String, Object>> contentList1 = jdbcTemplate.queryForList(query1, new Object[] { userId });

			for (Map<String, Object> row : contentList) {
				Content content = new Content();
				name = (String) row.get("LINK");
				id = (BigDecimal) row.get("ID");
				content.setId(id.longValue());
				content.setUrl(name);
				idList.add(id);
				list.add(content);
			}

			for (Map<String, Object> row : contentList1) {
				Content content = new Content();
				name = (String) row.get("Link");
				id = (BigDecimal) row.get("ID");
				if (!idList.contains(id)) {
					content.setId(id.longValue());
					content.setUrl(name);
					list.add(content);
				}
			}

		} catch (DataAccessException e) {
			log.error("DataAccessException occured");
		}

		return list;

	}

	public boolean isUserCubeLink(long userId, long cubeId) {
		boolean link = false;
		try {
			String query = "SELECT COUNT(*) FROM SHAREDCUBE WHERE CUBEID = ? AND USERID = ?";
			long num = jdbcTemplate.queryForLong(query, new Object[] { cubeId, userId });
			if (num > 0)
				link = true;
		} catch (DataAccessException e) {
			log.error("DataAccessException occured");
		}

		return link;

	}

	public boolean isUserContentLink(long userId, long contentId) {
		boolean link = false;
		try {
			String query = "SELECT COUNT(*) FROM SHAREDCONTENT WHERE CONTENTID = ? AND USERID = ?";
			long num = jdbcTemplate.queryForLong(query, new Object[] { contentId, userId });
			if (num > 0)
				link = true;
		} catch (DataAccessException e) {
			log.error("DataAccessException occured");
		}

		return link;

	}

}
