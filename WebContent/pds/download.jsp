<%@page import="com.pds.service.PdsItemNotFoundException"%>
<%@page import="com.pds.service.IncreaseDownloadCountService"%>
<%@page import="com.pds.file.FileDownloadHelper"%>
<%@page import="com.pds.service.GetPdsItemService"%>
<%@page import="com.pds.model.PdsItem"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ page pageEncoding="euc-kr" %>
<%@ page trimDirectiveWhitespaces="true" %>

<%@page import="java.net.URLEncoder"%>


<%
	int id = Integer.parseInt(request.getParameter("id"));
	try {
		PdsItem item = GetPdsItemService.getInstance().getPdsItem(id);

		// 응답 헤더 다운로드로 설정
		response.reset();
		
		String fileName = new String(item.getFileName().getBytes("euc-kr"), 
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
	}
%>