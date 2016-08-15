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
		try {
//			startHttpServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// daemon
		while (true)
			try {
				Thread.sleep(10000);
			} catch (Exception e) {
			}
	}

	public static void startHttpServer() throws Exception {
		// Create a basic jetty server object that will listen on port 8080.
		// Note that if you set this to port 0 then a randomly available port
		// will be assigned that you can either look in the logs for the port,
		// or programmatically obtain it for use in test cases.
		Server server = new Server(8080);

		// The ServletHandler is a dead simple way to create a context handler
		// that is backed by an instance of a Servlet.
		// This handler then needs to be registered with the Server object.
		ServletHandler handler = new ServletHandler();
		server.setHandler(handler);

		// Passing in the class for the Servlet allows jetty to instantiate an
		// instance of that Servlet and mount it on a given context path.

		// IMPORTANT:
		// This is a raw Servlet, not a Servlet that has been configured
		// through a web.xml @WebServlet annotation, or anything similar.
		handler.addServletWithMapping(FileUploadHandler.class, "/*");

		// Start things up!
		server.start();

		// The use of server.join() the will make the current thread join and
		// wait until the server is done executing.
		// See
		// http://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#join()
		server.join();
	}

	@SuppressWarnings("serial")
	public static class FileUploadHandler extends HttpServlet {
		private final String UPLOAD_DIRECTORY = File.separator + "logs";

		@Override
		protected void doGet(HttpServletRequest request,
				HttpServletResponse response) throws ServletException,
				IOException {
			System.out.println("doGet");
		}
		@Override
		protected void doPut(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			doPost(req, resp);
		}
		
		
		protected void doPost1(HttpServletRequest request,
				HttpServletResponse response) throws ServletException,
				IOException {
			System.out.println("doPost");
			// process only if its multipart content
			if (ServletFileUpload.isMultipartContent(request)) {
				try {
					System.out.println("inTry");
					List<FileItem> multiparts = new ServletFileUpload(
							new DiskFileItemFactory()).parseRequest(request);

					if(multiparts==null||multiparts.size()==0){
						request.setAttribute("message",
								"No File uploading Found");
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					}else{
						for (FileItem item : multiparts) {
							if (!item.isFormField()) {
								String name = new File(item.getName()).getName();
								File file = new File(UPLOAD_DIRECTORY
										+ File.separator + name);
								item.write(file);
								log.info("file:"+file.getAbsolutePath());
							}
						}
	
						// File uploaded successfully
						response.getWriter().println("File Uploaded Successfully");
						response.setStatus(HttpServletResponse.SC_OK);
					}

				} catch (Exception ex) {
					ex.printStackTrace();
					response.getWriter().println(
							"File Upload Failed due to " + ex);
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}

			} else {
				response.getWriter().println(
						"Sorry this Servlet only handles file upload request");
				request.setAttribute("message",
						"Sorry this Servlet only handles file upload request");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}

			response.setContentType("text/html");
			response.getWriter().println("<h1>end</h1>");

		}
		
		protected void doPost2(HttpServletRequest req, HttpServletResponse response)
		        throws ServletException, IOException
		{
		    try {
		        ServletFileUpload upload = new ServletFileUpload();
		        // set max file size to 1 MB:
		        upload.setFileSizeMax(1024 * 1024);
		        FileItemIterator it = upload.getItemIterator(req);
		        // handle with each file:
		        while (it.hasNext()) {
		            FileItemStream item = it.next();
		            if (! item.isFormField()) {
		                // it is a file upload:
		                handleFileItem(item);
		            }
		        }
		        response.getWriter().println("File Uploaded Successfully");
				response.setStatus(HttpServletResponse.SC_OK);
		    }
		    catch(FileUploadException e) {
		        e.printStackTrace();
		        response.getWriter().println(
						"File Upload Failed due to " + e);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		    }
		}
		
		void handleFileItem(FileItemStream item) throws IOException {
		    System.out.println("upload file: " + item.getName());
//		    File newUploadFile = new File(uploadDir + "/" + UUID.randomUUID().toString());
		    String name = new File(item.getName()).getName();
			File newUploadFile = new File(UPLOAD_DIRECTORY
					+ File.separator + name);
		    byte[] buffer = new byte[4096];
		    InputStream input = null;
		    OutputStream output = null;
		    try {
		        input = item.openStream();
		        output = new BufferedOutputStream(new FileOutputStream(newUploadFile));
		        for (;;) {
		            int n = input.read(buffer);
		            if (n==(-1))
		                break;
		            output.write(buffer, 0, n);
		        }
		    }
		    finally {
		        if (input!=null) {
		            try {
		                input.close();
		            }
		            catch (IOException e) {}
		        }
		        if (output!=null) {
		            try {
		                output.close();
		            }
		            catch (IOException e) {}
		        }
		    }
		}
	}

}
