package com.oracle.oBootBoard.dao;

import java.util.ArrayList;


import com.oracle.oBootBoard.dto.BDto;

// DB 프로그램은 변경할 수 있기 때문에 interface로 만드는 것이 좋음
// DB를 변경해도 사용 가능하게 모듈화
public interface BDao {

	public ArrayList<BDto> boardList();

	public void write(String bName, String bTitle, String bContent);

	public BDto contentView(int bId);

	public void modify(int bId, String bName, String bTitle, String bContent);

	public BDto reply_view(int bId);

	public void reply(int bId, String bName, String bTitle, String bContent, int bGroup, int bStep, int bIndent);

	public void delete(int bId);
	
}
