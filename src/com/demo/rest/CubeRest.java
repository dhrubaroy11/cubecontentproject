package com.demo.rest;

import java.io.InputStream;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.demo.JSON.AddContentJSON;
import com.demo.JSON.CreateContentJSON;
import com.demo.JSON.CreateCubeJSON;
import com.demo.JSON.CreateUserJSON;
import com.demo.JSON.DeleteContentJSON;
import com.demo.JSON.GetContentJSON;
import com.demo.JSON.GetCubeJSON;
import com.demo.JSON.ShareContentJSON;
import com.demo.JSON.ShareCubeJSON;
import com.demo.service.UserService;
import com.demo.service.UserServiceImpl;

@Stateless
@Local
@Path("/user")
public class CubeRest {
	
	UserService userService = new UserServiceImpl();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userCreateRequest(InputStream incomingdata) {
		CreateUserJSON cr = new CreateUserJSON();
		cr.LoadJSONRequest(incomingdata);
		return Response.status(200).entity(cr.BuildJSON()).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{user_id}/cube")
	public Response cubeCreateRequest(@PathParam("user_id") long id,
			InputStream incomingdata) {
		CreateCubeJSON cr = new CreateCubeJSON();
		cr.LoadJSONRequest(id, incomingdata);
		return Response.status(200).entity(cr.BuildJSON()).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{user_id}/content")
	public Response contentCreateRequest(@PathParam("user_id") long id,
			InputStream incomingdata) {
		CreateContentJSON cr = new CreateContentJSON();
		cr.LoadJSONRequest(id, incomingdata);
		return Response.status(200).entity(cr.BuildJSON()).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{user_id}/cube/{cube_id}/content")
	public Response contentAddRequest(@PathParam("user_id") long userId,
			@PathParam("cube_id") long cubeId, InputStream incomingdata) {
		AddContentJSON cr = new AddContentJSON();
		cr.LoadJSONRequest(userId, cubeId, incomingdata);
		return Response.status(200).entity(cr.BuildJSON()).build();
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{user_id}/cube/{cube_id}/content/{content_id}")
	public void deleteContentFromCube(@PathParam("user_id") long userId,
			@PathParam("cube_id") long cubeId , @PathParam("content_id") long contentId) {
		DeleteContentJSON cr = new DeleteContentJSON();
		cr.LoadJSONRequest(userId, cubeId, contentId);
		cr.deleteContentFromCube();
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{user_id}/cube/{cube_id}")
	public void deleteCube(@PathParam("user_id") long userId,
			@PathParam("cube_id") long cubeId) {
		userService.deleteCube(cubeId);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{user_id}/cube/{cube_id}/share")
	public Response shareCubeRequest(@PathParam("user_id") long userId,
			@PathParam("cube_id") long cubeId, InputStream incomingdata) {
		ShareCubeJSON cr = new ShareCubeJSON();
		cr.LoadJSONRequest(userId, cubeId, incomingdata);
		return Response.status(200).entity(cr.BuildJSON()).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{user_id}/content/{content_id}/share")
	public Response shareContentRequest(@PathParam("user_id") long userId,
			@PathParam("content_id") long contentId, InputStream incomingdata) {
		ShareContentJSON cr = new ShareContentJSON();
		cr.LoadJSONRequest(userId, contentId, incomingdata);
		return Response.status(200).entity(cr.BuildJSON()).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{user_id}/cube")
	public Response getAllCube(@PathParam("user_id") long userId) {
		
		GetCubeJSON cr = new GetCubeJSON();
		return Response.status(200).entity(cr.BuildJSON( userId)).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{user_id}/content")
	public Response getAllContent(@PathParam("user_id") long userId) {
		
		GetContentJSON cr = new GetContentJSON();
		return Response.status(200).entity(cr.BuildJSON( userId)).build();
	}

	
}
