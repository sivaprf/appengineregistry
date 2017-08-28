package com.gclouddemo.ecommerce.catalog;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Implements a simple servlet to dish up the raw contents of WEB-INF/letsencrypt/some-path-to-file
 * (where some-path-to-file is explained in the link below) for the LetsEncrypt challenge.
 * This has to be done like this because the App Engine doesn't allow paths to start with a period (full stop),
 * and the challenge URL must contain the path ".well-known".
 * <p>
 * See https://github.com/joseret/gclouddemowiki/blob/master/AppEngine/SSL-Certs.md
 * for an explanation and an example of how this is used.
 * <p>
 * This servlet was essentially copied from several places on the web, and is definitely not
 * intended for use except with the challenge; you should probably deny access to it under
 * normal circumstances.
 */
public class ChallengeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(ChallengeServlet.class.getName());

    private static final String PATH = "/WEB-INF/letsencrypt/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();

        if (!uri.startsWith("/.well-known/acme-challenge/")) {
            resp.sendError(404);
            return;
        }
        String challenge = uri.substring("/.well-known/acme-challenge/".length());
        challenge = sanitizePath(challenge);
        String res = PATH + challenge;

        try {
            String respString = readResource(res);
            resp.setContentType("text/plain");
            resp.getOutputStream().print(respString.trim());
        } catch (Throwable e) {
            log.severe("challenge not found: " + res);
            resp.sendError(404);
        }
    }

    private String sanitizePath(String s) {
        return s.replaceAll("[^a-zA-Z0-9-]+", "_");
    }

    private String readResource(String resName) throws IOException {
        log.info("reading " + resName);
        InputStream in = getServletContext().getResourceAsStream(resName);
        return inputStreamToString(in);
    }

    private static String inputStreamToString(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }

        return sb.toString();
    }
}
