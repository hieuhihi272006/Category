package com.javaweb.filter;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;


public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {

	
	private byte[] cachedBody;
	
	
	public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException{
		 super(request);
		 InputStream requestInputStream = request.getInputStream();
//		 this.cachedBody = StreamUtils.copyToByteArray(requestInputStream);
		 this.cachedBody = requestInputStream.readAllBytes();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ServletInputStream getInputStream() throws IOException{
		return new CachedBodyServletInputStream(this.cachedBody);
	}
	
	@Override
	public BufferedReader getReader() throws IOException {
	    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
	    return new BufferedReader(new InputStreamReader(byteArrayInputStream));
	}
	
	

}
