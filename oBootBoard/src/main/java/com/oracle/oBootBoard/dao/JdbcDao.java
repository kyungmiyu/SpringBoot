package com.oracle.oBootBoard.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

import com.oracle.oBootBoard.dto.BDto;

// JdbcDao MysqlDao 등 사용할 dao를 인터페이스 상속받아서 사용 
public class JdbcDao implements BDao {
	
	// JDBC 사용
	// final: 한 번 설정하고 변경 x
	private final DataSource dataSource;
	
	public JdbcDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	private Connection getConnection() {
		return DataSourceUtils.getConnection(dataSource);
	}
	
	@Override
	public ArrayList<BDto> boardList() {
		ArrayList<BDto> bList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		System.out.println("BDao boardList start...");
		
		try {
			// *로 해도 상관없음 밑에 dto 꺼내오는데 문제만 없다면
			String sql = "select * from mvc_board order by bGroup desc, bStep asc";
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println("BDao sql-->" + sql);
			
			// ** while: where order by 로 조건이 있는 경우나 primary 키가 아닌 경우 ** 
			while (rs.next()) {
				int bId = rs.getInt("bId");
				String bName = rs.getString("bName");
				String bTitle = rs.getString("bTitle");
				String bContent = rs.getString("bContent");
				Timestamp bDate = rs.getTimestamp("bDate");
				int bHit = rs.getInt("bHit");
				int bGroup = rs.getInt("bGroup");
				int bStep = rs.getInt("bStep");
				int bIndent = rs.getInt("bIndent");
				BDto dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
				bList.add(dto);
			}
		} catch (SQLException e) {
			System.out.println("list dataSource->"+e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch(Exception e2) {
				e2.printStackTrace();
			} 
		}
		return bList;
	}
	
	// HW1
	@Override
	public void write(String bName, String bTitle, String bContent) {
		// 1. INSERT into mvc_board
		// 2. PreparedStatement
		// 3. mvc_board sequence 사용
		// 4. bId , bGroup 같게
		// 5. bStep, bIndent, bDate --> 0, 0 , sysdate
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			// sql 작성할 때 db date 순서 맞춰주기
			String sql = "insert into mvc_board (bId, bName, bTitle, bContent, bHit, bGroup, bStep, bIndent, bDate)"
					+ "values(mvc_board_seq.nextval, ?, ?, ?, 0, mvc_board_seq.currval, 0, 0, sysdate)";
			System.out.println("BDao write sql->" + sql);

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bName);
			pstmt.setString(2, bTitle);
			pstmt.setString(3, bContent);
			int rs = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("write dataSource->"+ e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	@Override
	public BDto contentView(int bId) {
		// contentview를 클릭하면 조회수 올라가게 db같이 작성
		
		upHit(bId);
		
		BDto dto = null; 
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		try {
			conn = getConnection();
			String sql = "select * from mvc_board where bId=?";
			System.out.println("Dao contentView sql->"+sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bId);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				String bName = rs.getString("bName");
				String bTitle = rs.getString("bTitle");
				String bContent = rs.getString("bContent");
				Timestamp bDate = rs.getTimestamp("bDate");
				int bHit = rs.getInt("bHit");
				int bGroup = rs.getInt("bGroup");
				int bStep = rs.getInt("bStep");
				int bIndent = rs.getInt("bIndent");
				dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return dto;
	}
	
	@Override
	public void modify(int bId, String bName, String bTitle, String bContent) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			String sql = "update mvc_board set bname=?, btitle=?, bcontent=? where bid=?";
			System.out.println("BDto modify sql->"+sql);
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bName);
			pstmt.setString(2, bTitle);
			pstmt.setString(3, bContent);
			pstmt.setInt(4, bId);
			int rn = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	// 외부에서 접근못하게 private로 작성
	private void upHit(int bId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			String sql = "update mvc_board set bHit = bHit+1 where bId=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bId);
			int rn = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	@Override
	public BDto reply_view(int bId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BDto dto = null;
		
		try {
			conn = getConnection();
			String sql = "select * from mvc_board where bid=?";
			// String sql2 = "update mvc_board set bstep = bstep + 1 where bgroup=? and bindent ?";
			System.out.println("reply_view sql->"+sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bId);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				String bName = rs.getString("bName");
				String bTitle = rs.getString("bTitle");
				String bContent = rs.getString("bContent");
				Timestamp bDate = rs.getTimestamp("bDate");
				int bHit = rs.getInt("bHit");
				int bGroup = rs.getInt("bGroup");
				int bStep = rs.getInt("bStep");
				int bIndent = rs.getInt("bIndent");
				dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return dto;
	}
	
	@Override
	public void reply(int bId, String bName, String bTitle, String bContent, int bGroup, int bStep, int bIndent) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		// 대댓글
		replyShape(bGroup, bStep);
		
		try {
			conn = getConnection();
			String sql = "insert into mvc_board(bId, bName, bTitle, bContent, bGroup, bStep, bIndent) "
							+ "values(mvc_board_seq.nextval,?,?,?,?,?,?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bName);
			pstmt.setString(2, bTitle);
			pstmt.setString(3, bContent);
			pstmt.setInt(4, bGroup);
			pstmt.setInt(5, bStep + 1);
			pstmt.setInt(6, bIndent + 1);
		
			int rn = pstmt.executeUpdate();
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
	}
	
	private void replyShape(int bGroup, int bStep) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			String sql = "update mvc_board set bstep = bstep + 1 where bgroup =? and bstep > ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bGroup);
			pstmt.setInt(2, bStep);
			
			int rn = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	@Override
	public void delete(int bId) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = getConnection();
			String sql = "delete from mvc_board where bid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bId);
			int rn = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
	}

}
