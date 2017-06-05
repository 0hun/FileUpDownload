package com.pds.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pds.file.FileDownloadHelper;
import com.pds.model.PdsItem;
import com.pds.service.GetPdsItemService;
import com.pds.service.IncreaseDownloadCountService;
import com.pds.service.PdsItemNotFoundException;

@WebServlet("/download")
public class FileDownloadServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, 
			HttpServletResponse response) 
					throws ServletException, IOException {
		reqRes(request,response);
	}	
	protected void doPost(HttpServletRequest request, 
			HttpServletResponse response) 
					throws ServletException, IOException {
		reqRes(request,response);		
	}
	
	public void reqRes(HttpServletRequest request,HttpServletResponse response)
		throws ServletException, IOException{
		
		int id = Integer.parseInt(request.getParameter("id"));
		try {
			PdsItem item = GetPdsItemService.getInstance().getPdsItem(id);

			// 응답 헤더 다운로드로 설정
			response.reset();
			
			String fileName = new String(item.getFileName().getBytes("utf-8"), 
					"iso-8859-1");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", 
					"attachment; filename=\"" + fileName+"\"");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setContentLength((int)item.getFileSize());
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			FileDownloadHelper.copy(item.getRealPath(), 
					response.getOutputStream());
			
			response.getOutputStream().close();
			
			
			IncreaseDownloadCountService.getInstance().increaseCount(id);
			
			
			
		} catch (PdsItemNotFoundException ex) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}catch(Exception e){
			e.printStackTrace();
		}
	}		

}
