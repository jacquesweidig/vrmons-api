package example.nosql;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.codec.binary.Base64;

import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Document;
import com.cloudant.client.api.model.Params;
import com.cloudant.client.org.lightcouch.Attachment;
import com.google.gson.JsonObject;
import java.util.logging.Logger;

@WebServlet("/attach")
@MultipartConfig()
public class AttachServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

        private static final Logger log = Logger.getLogger(AttachServlet.class.getName());
        
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
            log.severe("Youpi Tralala");
		String caseName = request.getParameter("caseName");
		int mark = Integer.valueOf(request.getParameter("mark"));
		
		Database db = null;
		try {
			db = CloudantClientMgr.getDB();
		} catch (Exception re) {
			re.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		ResourceServlet servlet = new ResourceServlet();

		JsonObject resultObject = servlet.create(db, caseName, mark);

		response.getWriter().println(resultObject.toString());
	}
}
