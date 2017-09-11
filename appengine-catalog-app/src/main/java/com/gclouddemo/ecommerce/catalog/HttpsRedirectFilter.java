/**
 * 
 */
package com.gclouddemo.ecommerce.catalog;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A filter to force non-HTTPS requests to redirect to the HTTPS equivalent.
 * <p>
 * Not a substitute for doing it in (e.g.) external mappings, etc., but it's
 * a reasonable last-mile fallback, and shows a simple filter being used in App Engine.
 */
public class HttpsRedirectFilter implements Filter {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(HttpsRedirectFilter.class.getName());
	
    /** We want to redirect to HTTPS using the filter... */
    private static final String PREFERRED_REQUEST_SCHEME = "https";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (request instanceof HttpServletRequest
		         && response instanceof HttpServletResponse) {
	        HttpServletRequest httpReq = (HttpServletRequest) request;
	        String redirectTarget = getHttpsRedirect(httpReq);
	       
	        if (redirectTarget != null) {                   
	            ((HttpServletResponse) response).sendRedirect(redirectTarget); 
	        } else {  
	            chain.doFilter(request, response);  
	        }  
	    }
	}

	@Override
	public void destroy() {
		
	}
	
	private String getHttpsRedirect(HttpServletRequest request) {
		if (!PREFERRED_REQUEST_SCHEME.equalsIgnoreCase(request.getScheme())) {
			if ("localhost".equalsIgnoreCase(request.getServerName())) {
				return null;
			}
			String httpsUrl = PREFERRED_REQUEST_SCHEME + "://" + request.getServerName()
            				+ request.getContextPath() + request.getServletPath();
	        if (request.getPathInfo() != null) {
	        	httpsUrl += request.getPathInfo();
	        }
	        if (request.getQueryString() != null) {
	        	httpsUrl += ("?" + request.getQueryString());
	        }
	        return httpsUrl;
		}
		return null;	
	}

}
