package com.kh.toy.board.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kh.toy.board.model.dto.Board;
import com.kh.toy.common.code.ErrorCode;
import com.kh.toy.common.db.JDBCTemplate;
import com.kh.toy.common.exception.DataAccessException;
import com.kh.toy.common.file.FileDTO;

public class BoardDao {
	
	JDBCTemplate template = JDBCTemplate.getInstance();

	public void insertBoard(Board board, Connection conn) {
		
		String sql = "insert into board "
				+ "(bd_idx,user_id,title,content) "
				+ "values (sc_board_idx.nextval,?,?,?)";

		PreparedStatement pstm = null;
		try {
			pstm = conn.prepareStatement(sql);
			
			pstm.setString(1, board.getUserId());
			pstm.setString(2, board.getTitle());
			pstm.setString(3, board.getContent());
			pstm.executeUpdate();
						
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}finally {
			template.close(pstm);
		}
		
		
		
		
		
	}

	// *** currval를 쓸 수 있는 이유??
	//	dao단에서는 connection을 close하지 않고,
	//	service단에서 transaction이 성공적으로 수행된 이후에 close(conn)처리하기 때문에 
	//	dao단에서는 아직 세션이 유효(살아있기)하기 때문
	
	public void insertFile(FileDTO fileDTO, Connection conn) {
		String sql = "insert into file_info "
				+ "(fl_idx,type_idx,origin_file_name,rename_file_name,save_path) "
				+ "values(sc_file_idx.nextval,sc_board_idx.currval,?,?,?)";
		
		PreparedStatement pstm = null;
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, fileDTO.getOriginFileName());
			pstm.setString(2, fileDTO.getRenameFileName());
			pstm.setString(3, fileDTO.getSavePath());
			pstm.executeUpdate();
			
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}finally {
			template.close(pstm);
		}
				
		
	}

	public Board selectBoardDetail(String bdIdx, Connection conn) {

		String sql = "select bd_idx,user_id,reg_date,title,content "
				+ "from board where bd_idx = ?";
		PreparedStatement pstm = null;
		ResultSet rset = null;
		Board board = null;
		
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, bdIdx);
			rset = pstm.executeQuery();
			
			if(rset.next()) {
				board = new Board();
				board.setBdIdx(bdIdx);
				board.setTitle(rset.getString("title"));
				board.setContent(rset.getString("content"));
				board.setUserId(rset.getString("user_id"));
				board.setRegDate(rset.getDate("reg_date"));
				
			}
		} catch (Exception e) {
			throw new DataAccessException(e);
		}finally {
			template.close(rset,pstm);
		}
		return board;
	}

	public List<FileDTO> selectFileDTOs(String bdIdx, Connection conn) {
		String sql = "select fl_idx, type_idx, origin_file_name, "
				+ "rename_file_name, save_path, reg_date "
				+ "from file_info where type_idx = ?";
		
		PreparedStatement pstm = null;
		ResultSet rset = null;
		List<FileDTO> files = new ArrayList<FileDTO>();	//null이 아니라 길이가 0인 빈 list를 만들어 반환하는 것이 예외를 줄일 수 있다.
		
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, bdIdx);
			rset = pstm.executeQuery();
						
			while(rset.next()) {
				FileDTO fileDTO = new FileDTO();
				fileDTO.setFlIdx(rset.getString("fl_idx"));
				fileDTO.setTypeIdx(rset.getString("type_idx"));
				fileDTO.setOriginFileName(rset.getString("origin_file_name"));
				fileDTO.setRenameFileName(rset.getString("rename_file_name"));
				fileDTO.setSavePath(rset.getString("save_path"));
				fileDTO.setRegDate(rset.getDate("reg_date"));
				files.add(fileDTO);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}finally {
			template.close(rset,pstm);
		}
		
		return files;
	}

}
