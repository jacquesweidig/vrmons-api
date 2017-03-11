package example.nosql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Part;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cloudant.client.api.Database;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

@Path("/favorites")
/**
 * CRUD service of todo list table. It uses REST style.
 */
public class ResourceServlet {

	public ResourceServlet() {
	}

	@POST
	public Response create(@FormParam("caseName") String caseName, @FormParam("mark") int mark)
			throws Exception {

		Database db = null;
		try {
			db = getDB();
		} catch (Exception re) {
			re.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		JsonObject resultObject = create(db, caseName, mark);

		System.out.println("Create Successful.");

		return Response.ok(resultObject.toString()).build();
	}

	protected JsonObject create(Database db, String caseName, int mark)
			throws IOException {

		// check if document exist
		HashMap<String, Object> obj = null ; //(id == null) ? null : db.find(HashMap.class, id);

		if (obj == null) {
			// if new document

			//id = String.valueOf(System.currentTimeMillis());

			// create a new document
			//System.out.println("Creating new document with id : " + id);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("name", caseName);
			//data.put("_id", id);
			data.put("mark", mark);
			data.put("creation_date", new Date().toString());
			db.save(data);

			// attach the attachment object
//			obj = db.find(HashMap.class, id);
//			saveAttachment(db, id, part, fileName, obj);
		} else {
			// if existing document
			// attach the attachment object
//			saveAttachment(db, id, part, fileName, obj);

			// update other fields in the document
//			obj = db.find(HashMap.class, id);
//			obj.put("name", name);
//			obj.put("value", mark);
//			db.update(obj);
		}

//
		JsonObject resultObject = toJsonObject(obj);

		return resultObject;
	}

	private JsonObject toJsonObject(HashMap<String, Object> obj) {
		JsonObject jsonObject = new JsonObject();
		LinkedTreeMap<String, Object> attachments = (LinkedTreeMap<String, Object>) obj.get("_attachments");
		//		jsonObject.addProperty("id", obj.get("_id") + "");
		jsonObject.addProperty("caseName", obj.get("caseName") + "");
		jsonObject.addProperty("mark", obj.get("mark") + "");
		return jsonObject;
	}

	/*
	 * Create a document and Initialize with sample data/attachments
	 */
	private List<HashMap> initializeSampleData(Database db) throws Exception {

		long id = System.currentTimeMillis();
		String name = "Sample category";
		String value = "List of sample files";

		// create a new document
		System.out.println("Creating new document with id : " + id);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("name", name);
		data.put("_id", id + "");
		data.put("value", value);
		data.put("creation_date", new Date().toString());
		db.save(data);

		// attach the object
		HashMap<String, Object> obj = db.find(HashMap.class, id + "");

		// attachment#1
		File file = new File("Sample.txt");
		file.createNewFile();
		PrintWriter writer = new PrintWriter(file);
		writer.write("This is a sample file...");
		writer.flush();
		writer.close();
		FileInputStream fileInputStream = new FileInputStream(file);
		db.saveAttachment(fileInputStream, file.getName(), "text/plain", id + "", (String) obj.get("_rev"));
		fileInputStream.close();

		return db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse().getDocsAs(HashMap.class);

	}

	private Database getDB() {
		return CloudantClientMgr.getDB();
	}

}
