package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import DAO.UserDao;
import auth.AuthKey;
import beans.User;

@Path("/updateprofile")
public class UpdateProfile {
	
	final static Logger logger = Logger.getLogger(UpdateProfile.class);
	
	@Path("/edituser")
	@POST
	@Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
	public Response updateProfile(@Context HttpHeaders headers, String data) {
		
		if(headers.getRequestHeader("secret") == null || !headers.getRequestHeader("secret").get(0).equals(AuthKey.KEY+"")) {
			System.out.println(headers.getRequestHeader("secret"));
			logger.info("Login Bad request without AuthKey");
			return Response.status(302).entity("Unauthorized access").build();
		}
		
		boolean update = true;
		Gson gson = new Gson();
		User user = gson.fromJson(data, User.class);
		
		String username = user.getUsername();
		
		
		String address1 = user.getAddress1();
		String address2 = user.getAddress2();
		String city = user.getCity();
		String state = user.getState();
		String country = user.getCountry();
		
		UserDao dao = new UserDao();
		update = dao.updateUser(username, address1,address2,city,state,country);
		user = dao.getUser(username);
		
		System.out.println("isUpdateUserSuccessful: " + update);	
		
		if(update){
			logger.info("Update profile: "+username+" :SUCCESS");
		}
		else{
			logger.info("Update profile: "+username+" :FAIL");
		}
		String response = gson.toJson(user);
		return Response.ok().entity(String.valueOf(response)).build();
	}

}
