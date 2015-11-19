
package com.nice;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

/** Example resource class hosted at the URI path "/myresource"
 */
@Path("/sample")
public class MyResource {
	
	@GET
	@Path ("/{jobId}")
	@Produces({MediaType.APPLICATION_JSON})
	public String get(@PathParam("jobId") String jobId) {
		System.out.println("jobId=" + jobId);
		ObjectMapper om = new ObjectMapper();
		ObjectNode newDoc = (ObjectNode)om.createObjectNode();
		newDoc.put("jobId", jobId);

		System.out.println("*** Remote Job ***");
		System.out.println("*** Begin Long Running Job ***");
				for(int i = 0; i < 5; i++) {
					System.out.println("Long Running: " + i);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		System.out.println("*** End Long Running Job ***");

		System.out.println("Result: " + newDoc.toString());
		
		return newDoc.toString();
	}
}
