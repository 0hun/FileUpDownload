package com.pds.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pds.model.PdsItemListModel;
import com.pds.service.ListPdsItemService;

@WebServlet("/list")
public class ListPdsItemController extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest request, 
						 HttpServletResponse response)
								 	throws ServletException, IOException {
		reqRes(request,response);		
	}

	@Override
	protected void doPost(HttpServletRequest request, 
						  HttpServletResponse response)
								  throws ServletException, IOException {
		reqRes(request,response);
	}
	
	private void reqRes(HttpServletRequest request, 
						HttpServletResponse response)
						throws ServletException, IOException{
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		
		String view="/pds/list_view.jsp";
		
		String pageNumberString = request.getParameter("p");
		
		int pageNumber = 1;
		
		if (pageNumberString != null && pageNumberString.length() > 0) {
			pageNumber = Integer.parseInt(pageNumberString);
		}
		
		ListPdsItemService listSerivce = ListPdsItemService.getInstance();
		
		PdsItemListModel itemListModel = 
				listSerivce.getPdsItemList(pageNumber);
		request.setAttribute("listModel", itemListModel);
		
		if (itemListModel.getTotalPageCount() > 0) {
			int beginPageNumber = (itemListModel.getRequestPage() - 1) 
									/ 10 * 10 + 1;
			int endPageNumber = beginPageNumber + 9;
			if (endPageNumber > itemListModel.getTotalPageCount()) {
				endPageNumber = itemListModel.getTotalPageCount();
			}
			request.setAttribute("beginPage", beginPageNumber);
			request.setAttribute("endPage", endPageNumber);
		}
		
		request.getRequestDispatcher(view).forward(request,response);
		
	}

	
}
