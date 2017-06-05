package com.pds.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.pds.file.FileSaveHelper;
import com.pds.model.AddRequest;
import com.pds.model.PdsItem;
import com.pds.service.AddPdsItemService;


public class FileUploadServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, 
						  HttpServletResponse response)
			throws ServletException, IOException {
		
		request.getRequestDispatcher("/pds/uploadForm.jsp")
		.forward(request,response);
	}
	
	
	@Override
	protected void doPost(HttpServletRequest request, 
						  HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");

		String contentType = request.getContentType();
		if (contentType != null
				&& contentType.toLowerCase().startsWith("multipart/")) {
			PdsItem uploadedItem = saveUploadFile(request);
			
			request.setAttribute("uploadedItem", uploadedItem);

			response.sendRedirect("list");
		} else {
			response.sendRedirect("/pds/invalid.jsp");
									
		}
	}

	private PdsItem saveUploadFile(HttpServletRequest request) 
			throws IOException,
			ServletException {
		
		Part descPart = request.getPart("description");
		String description = readParameterValue(descPart);
		
		Part filePart = request.getPart("file");
		String fileName = getFileName(filePart);
		String realPath = FileSaveHelper.save("c:\\pds",
				filePart.getInputStream());

		AddRequest addRequest = new AddRequest();
		addRequest.setFileName(fileName);
		addRequest.setFileSize(filePart.getSize());
		addRequest.setDescription(description);
		addRequest.setRealPath(realPath);

		PdsItem pdsItem = AddPdsItemService.getInstance().add(addRequest);
		return pdsItem;
	}

	private String getFileName(Part part) throws UnsupportedEncodingException {
		for (String cd : part.getHeader("Content-Disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				return cd.substring(cd.indexOf('=') + 1).trim()
						.replace("\"", "");
			}
		}
		return null;
	}

	private String readParameterValue(Part part) throws IOException {
		InputStreamReader reader 
		= new InputStreamReader(part.getInputStream(),
				"utf-8");
		char[] data = new char[512];
		int len = -1;
		StringBuilder builder = new StringBuilder();
		while ((len = reader.read(data)) != -1) {
			builder.append(data, 0, len);
		}
		return builder.toString();
	}

}
