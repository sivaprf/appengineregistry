/**
 * 
 */
package com.gclouddemo.ecommerce.catalog.common;

import static com.google.common.net.MediaType.JSON_UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public class CatalogServletHelper {
	
	private static final Logger LOG = Logger.getLogger(CatalogServletHelper.class.getName());

    private static final String MIME_TYPE_JSON = JSON_UTF_8.toString();
	
	public void sendJson(HttpServletResponse response, String jsonStr) throws Exception {		
		sendBody(response, jsonStr, MIME_TYPE_JSON);
	}
	
	public void sendBody(HttpServletResponse response, String bodyStr, String mimeType) throws Exception {
		response.setStatus(HttpURLConnection.HTTP_OK);
		response.setContentType(mimeType);
		PrintWriter responseWriter = response.getWriter();
		if (bodyStr != null) {
			responseWriter.println(bodyStr);
		}
		
		responseWriter.close();
	}
	
	public String getJsonPayload(HttpServletRequest request) throws IOException {
		StringBuilder strBuilder = new StringBuilder();
	    BufferedReader bufferedReader = request.getReader();
		try {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
			    strBuilder.append(line);
			}
		    return strBuilder.toString();
		} catch (Throwable thr) {
			LOG.severe(thr.getLocalizedMessage());
		} finally {
	        if (bufferedReader != null) {
	            try {
	            	bufferedReader.close();
	            } catch (IOException ex) {
	                // Don't need to report -- HR.
	            }
	        }
		}
		
		return null;
	}
}
