
package com.ktds;

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
@Path("/device")
public class MyResource {
    
    /** Method processing HTTP GET requests, producing "text/plain" MIME media
     * type.
     * @return String that will be send back as a response of type "text/plain".
     */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	public String postEnlist(InputStream in) {
		ObjectMapper om = new ObjectMapper();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		try {
			JsonNode request = om.readTree(in);
			System.out.println(request);
			
			String deviceId = request.get("deviceId").getValueAsText();
			String type = request.get("type").getValueAsText();
			String name = request.get("name").getValueAsText();
			
			conn = getDBConnection();
			conn.setAutoCommit(false);
			System.out.println(conn);
			
			stmt = conn.createStatement();
			try {
				stmt.executeUpdate("drop table agent");
			}
			catch(Throwable e) {
				e.printStackTrace();
			}
			stmt.executeUpdate("create table agent (deviceid varchar(25), type varchar(10), name varchar(20), primary key(deviceid))");
			
			pstmt = conn.prepareStatement("insert into agent values (?, ?, ?)");
			pstmt.setString(1, deviceId);
			pstmt.setString(2, type);
			pstmt.setString(3, name);
			pstmt.executeUpdate();
			
			conn.commit();
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return "success";
	}
	
	@GET
	@Path ("/{deviceId}")
	@Produces({MediaType.APPLICATION_JSON})
	public String get(@PathParam("deviceId") String deviceId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getDBConnection();
			conn.setAutoCommit(false);
			System.out.println(conn);
			
			pstmt = conn.prepareStatement("select * from agent where deviceid=?");
			pstmt.setString(1, deviceId);
			ResultSet rs = pstmt.executeQuery();
			
			rs.next();
			String type = rs.getString(2);
			String name = rs.getString(3);
			
			ObjectMapper om = new ObjectMapper();
			ObjectNode newDoc = (ObjectNode)om.createObjectNode();
			newDoc.put("deviceId", deviceId);
			newDoc.put("type", type);
			newDoc.put("name", name);
			
			return newDoc.toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}
	
	Connection getDBConnection() {
		try {
			Class cls = Class.forName("org.hsqldb.jdbcDriver");
			Driver drv = (Driver)cls.newInstance();
			Properties props = new Properties();
			props.setProperty("user", "sa");
			props.setProperty("password", "");
			return drv.connect("jdbc:hsqldb:file:mydb", props);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
