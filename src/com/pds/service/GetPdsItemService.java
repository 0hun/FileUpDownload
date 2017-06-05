package com.pds.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.jdbc.JdbcUtil;
import com.jdbc.connection.ConnectionProvider;
import com.pds.dao.PdsItemDao;
import com.pds.model.PdsItem;

public class GetPdsItemService {

	private static GetPdsItemService instance = new GetPdsItemService();

	public static GetPdsItemService getInstance() {
		return instance;
	}

	private GetPdsItemService() {
	}

	public PdsItem getPdsItem(int id) throws PdsItemNotFoundException {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			PdsItem pdsItem = PdsItemDao.getInstance().selectById(conn, id);
			if (pdsItem == null) {
				throw new PdsItemNotFoundException("해당 item이 없습니다.:" + id);
			}
			return pdsItem;
		} catch (SQLException e) {
			throw new RuntimeException("DB 연결실패: " + e.getMessage(), e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
}
