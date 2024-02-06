package com.oracle.oBootBoard.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.oracle.oBootBoard.command.BExecuteCommand;

import jakarta.servlet.http.HttpServletRequest;

@Controller
// DispatcherServlet이 @컨트롤러로 선언되있는 것만 controller로 인식함
public class BController {
	
	private static final Logger logger = LoggerFactory.getLogger(BController.class);

	// service 호출
	private final BExecuteCommand bExecuteService;
	
	// service를 constructor로 사용 (인스턴스화 시켜야 사용가능)
	@Autowired
	public BController(BExecuteCommand bExecuteService) {
		this.bExecuteService = bExecuteService;
	}
	
	@RequestMapping("list")
	public String list(Model model) {
		logger.info("list start...");
		bExecuteService.bListCmd(model);
		
		return "list";
	}
	
	@RequestMapping("/write_view")
	public String write_view(Model model) {
		logger.info("write_view start...");
		
		return "write_view";
	}
	
	@PostMapping(value = "/write")
	// request를 받은 이유 write의 파라미터를 그대로 controller에 보내기위해서
	public String write(HttpServletRequest request, Model model) {
		logger.info("write start...");
		
		model.addAttribute("request", request); // string-request object-request (map 방식)
		bExecuteService.bWriteCmd(model); // call by reference

		return "redirect:list";
	}
	
	@RequestMapping("/content_view")
	public String content_view(HttpServletRequest request, Model model) {
		System.out.println("content_view()");
		
		model.addAttribute("request", request);
		// content를 누르는 순간 조회수 올라감
		bExecuteService.bContentCmd(model);
		return "content_view";
	}
	
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modify(HttpServletRequest request, Model model) {
		logger.info("modify start...");
		
		model.addAttribute("request", request);
		bExecuteService.bModifyCmd(model);
		
		return "redirect:list";
	}
	
	@RequestMapping("/reply_view")
	public String reply_view(HttpServletRequest request, Model model) {
		System.out.println("reply_view start...");
		
		model.addAttribute("request", request);
		bExecuteService.bReplyViewCmd(model);
		
		return "reply_view";
	}

	@RequestMapping(value="/reply", method=RequestMethod.POST)
	public String reply(HttpServletRequest request, Model model) {
		System.out.println("reply()");
		
		model.addAttribute("request", request);
		bExecuteService.bReplyCmd(model);
		
		return "redirect:list";
	}
	
	@RequestMapping("/delete")
	public String delete(HttpServletRequest request, Model model) {
		System.out.println("delete()");
		
		model.addAttribute("request", request);
		bExecuteService.bDeleteCmd(model);
		
		return "redirect:list";
	}
	
	
	
}
