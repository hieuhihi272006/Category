package com.javaweb.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

public class CachedBodyServletInputStream extends ServletInputStream{

//	private InputStream cachedBodyInputStream;
	private final ByteArrayInputStream cachedBodyInputStream;


	public CachedBodyServletInputStream(byte[] cachedBody) {
	   this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
	}
	
	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
        return cachedBodyInputStream.available() == 0;
	}
	
	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setReadListener(ReadListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		return cachedBodyInputStream.read();
	}

}
