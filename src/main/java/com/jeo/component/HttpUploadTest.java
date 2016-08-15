package com.jeo.component;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.jivesoftware.whack.ExternalComponentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.ComponentException;

public class HttpUploadTest {
	protected static final Logger log = LoggerFactory
			.getLogger("HttpUploadTest");
	private static final String HOST = "192.168.211.166";
	private static final String URL_PUT = "http://192.168.254.67:8080/fileserver/file/";
	private static final String URL_GET = "http://192.168.254.67:8080/fileserver/file/";
	private static final long FILE_MAX = 5242880;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExternalComponentManager mgr = new ExternalComponentManager(HOST
				, 5275);
		mgr.setSecretKey("upload", "uploadKey");
		try {
			mgr.addComponent("upload", new HttpUploadComponent(URL_PUT,URL_GET,FILE_MAX));
		} catch (ComponentException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		// daemon
		while (true)
			try {
				Thread.sleep(10000);
			} catch (Exception e) {
			}
	}	

}
