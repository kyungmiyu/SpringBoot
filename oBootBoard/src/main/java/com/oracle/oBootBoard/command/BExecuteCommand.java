package com.oracle.oBootBoard.command;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.oracle.oBootBoard.dao.BDao;
import com.oracle.oBootBoard.dto.BDto;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class BExecuteCommand {
	// BCommand와 BListCommand를 합쳐서 만든 boot스러운 코드
	private final BDao jdbcDao;
	
	public BExecuteCommand(BDao jdbcDao) {
		this.jdbcDao = jdbcDao;
	}
	
	public void bListCmd(Model model) {
		// Dao 연결
		ArrayList<BDto> boardDtoList = jdbcDao.boardList();
		System.out.println("BListCommand boardDtoList.size()-->" +boardDtoList.size());
		model.addAttribute("boardList", boardDtoList);
	}
	
	public void bWriteCmd(Model model) {
		// 1. model이용 map선언
		// 2. request 이용 -> bName bTitle, bConent
		// 3. dao instance 선언
		// 4. write method 이용하여 저장 (bName, bTitle, bContent)
		Map<String, Object> map = model.asMap();
		HttpServletRequest request = (HttpServletRequest)map.get("request");
		String bName = request.getParameter("bName");
		String bTitle = request.getParameter("bTitle");
		String bContent = request.getParameter("bContent");
		
		jdbcDao.write(bName, bTitle, bContent);

	}
	
	// HW2
	public void bContentCmd(Model model) {
		// 1. model map으로 전환
		// 2. request -> bId Get
		Map<String, Object> map = model.asMap();
		
		HttpServletRequest request = (HttpServletRequest)map.get("request");
		int bId = Integer.parseInt(request.getParameter("bId"));
		
		System.out.println("bContentCmd bId->"+bId);
		
		// HW3
		// db에서 조회수 같이 작성
		BDto board = jdbcDao.contentView(bId);
		System.out.println("bContentCmd board.getbName->"+board.getbName());
		model.addAttribute("mvc_board", board);
		
	}

	public void bModifyCmd(Model model) {
		// 1. model Map선언
		Map<String, Object> map = model.asMap();
		HttpServletRequest request = (HttpServletRequest)map.get("request");
		
		int bId = Integer.parseInt(request.getParameter("bId"));
		String bName = request.getParameter("bName");
		String bTitle = request.getParameter("bTitle");
		String bContent = request.getParameter("bContent");

		// 2. parameter ->  bId, bName , bTitle , bContent
		jdbcDao.modify(bId, bName, bTitle, bContent);
	}

	public void bReplyViewCmd(Model model) {
		// 1. model이용 map선언
		// 2. request 이용 -> bid 추출
		// 3. reply_view method 이용해서 (bid) - BDto dto = dao.reply_view(bid);
		Map<String, Object> map = model.asMap();
		HttpServletRequest request = (HttpServletRequest)map.get("request");
		
		int bId = Integer.parseInt(request.getParameter("bId"));		
		
		BDto dto = jdbcDao.reply_view(bId);
		model.addAttribute("reply_view", dto);
	}
	
	public void bReplyCmd(Model model) {
//		  1)  model이용 , map 선언
//		  2) request 이용 -> bid, bName, bTitle, bContent, bGroup, bStep, bIndent 추출
//		  3) reply method 이용하여 댓글저장 
//		    - dao.reply(bId, bName, bTitle, bContent, bGroup, bStep, bIndent);
//		    [1] bId SEQUENCE = bGroup 
//		    [2] bName, bTitle, bContent -> request Value
//		    [3] 홍해 기적
//		    [4] bStep / bIndent + 1
		Map<String, Object> map = model.asMap();
		HttpServletRequest request = (HttpServletRequest)map.get("request");
		
		int bId = Integer.parseInt(request.getParameter("bId"));
		String bName = request.getParameter("bName");
		String bTitle = request.getParameter("bTitle");
		String bContent = request.getParameter("bContent");
		int bGroup = Integer.parseInt(request.getParameter("bGroup"));
		int bStep = Integer.parseInt(request.getParameter("bStep"));
		int bIndent = Integer.parseInt(request.getParameter("bIndent"));
		
		jdbcDao.reply(bId, bName, bTitle, bContent, bGroup, bStep, bIndent);
	}

	public void bDeleteCmd(Model model) {
		Map<String, Object> map = model.asMap();
		HttpServletRequest request = (HttpServletRequest)map.get("request");
		
		int bId = Integer.parseInt(request.getParameter("bId"));
		jdbcDao.delete(bId);
	}
	
	
	
}
	

