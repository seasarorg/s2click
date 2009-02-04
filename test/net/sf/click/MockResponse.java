package net.sf.click;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class MockResponse implements HttpServletResponse {
	
	private String contentType;
	private ServletOutputStreamImpl out = new ServletOutputStreamImpl();
	
	public void addCookie(Cookie arg0) {
	}

	public void addDateHeader(String arg0, long arg1) {
	}

	public void addHeader(String arg0, String arg1) {
	}

	public void addIntHeader(String arg0, int arg1) {
	}

	public boolean containsHeader(String arg0) {
		return false;
	}

	public String encodeRedirectURL(String arg0) {
		return null;
	}

	public String encodeRedirectUrl(String arg0) {
		return null;
	}

	public String encodeURL(String arg0) {
		return arg0;
	}

	public String encodeUrl(String arg0) {
		return arg0;
	}

	public void sendError(int arg0) throws IOException {
	}

	public void sendError(int arg0, String arg1) throws IOException {
	}

	public void sendRedirect(String arg0) throws IOException {
	}

	public void setDateHeader(String arg0, long arg1) {
	}

	public void setHeader(String arg0, String arg1) {
	}

	public void setIntHeader(String arg0, int arg1) {
	}

	public void setStatus(int arg0) {
	}

	public void setStatus(int arg0, String arg1) {
	}

	public void flushBuffer() throws IOException {
	}

	public int getBufferSize() {
		return 0;
	}

	public String getCharacterEncoding() {
		return null;
	}
	
	public String getContentType() {
		return this.contentType;
	}

	public Locale getLocale() {
		return null;
	}

	public ServletOutputStream getOutputStream() throws IOException {
		return out;
	}

	public PrintWriter getWriter() throws IOException {
		return null;
	}

	public boolean isCommitted() {
		return false;
	}

	public void reset() {
	}

	public void resetBuffer() {
	}

	public void setBufferSize(int arg0) {
	}

	public void setCharacterEncoding(String arg0) {
	}

	public void setContentLength(int arg0) {
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setLocale(Locale arg0) {
	}

	public static class ServletOutputStreamImpl extends ServletOutputStream {
		
		private ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		@Override
		public void write(int b) throws IOException {
			out.write(b);
		}
		
		public byte[] getContents(){
			return out.toByteArray();
		}
	}
}
