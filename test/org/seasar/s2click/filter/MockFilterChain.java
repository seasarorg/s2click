package org.seasar.s2click.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MockFilterChain implements FilterChain {

	private boolean invoked = false;

	public boolean isInvoked(){
		return invoked;
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1)
			throws IOException, ServletException {
		this.invoked = true;
	}

}
