package com.pds.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.jdbc.JdbcUtil;
import com.jdbc.connection.ConnectionProvider;
import com.pds.dao.PdsItemDao;
import com.pds.model.AddRequest;
import com.pds.model.PdsItem;

public class AddPdsItemService {

	private static AddPdsItemService instance = 
			new AddPdsItemService();
	public static AddPdsItemService getInstance() {return instance;}
	private AddPdsItemService() {}
	
	public PdsItem add(AddRequest request) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			PdsItem pdsItem = request.toPdsItem();
			int id = PdsItemDao.getInstance().insert(conn, pdsItem);
			if (id == -1) {
				JdbcUtil.rollback(conn);
				throw new RuntimeException("DB 에러 발생");
			}
			pdsItem.setId(id);
			
			conn.commit();
			
			return pdsItem;
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
				} catch (SQLException e) {
				}
			}
			JdbcUtil.close(conn);
		}
	}
}
