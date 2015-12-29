package com.demo.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;

import com.demo.service.UserService;
import com.demo.service.UserServiceImpl;

public class DeleteContentJSON {
	private long userId     = 0;
	private long cubeId     = 0;
	private long contentId = 0;
	private boolean malformedJsonRequest = false;
	private Log log = LogFactory.getLog(this.getClass().getName());
	
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getCubeId() {
		return cubeId;
	}
	public void setCubeId(long cubeId) {
		this.cubeId = cubeId;
	}
	
	public long getContentId() {
		return contentId;
	}
	public void setContentId(long contentId) {
		this.contentId = contentId;
	}
	public boolean isMalformedJsonRequest() {
		return malformedJsonRequest;
	}
	public void setMalformedJsonRequest(boolean malformedJsonRequest) {
		this.malformedJsonRequest = malformedJsonRequest;
	}
	
	
	public void LoadJSONRequest( long usrid , long cubid , long cntId)
	{
		
		setUserId(usrid);
		setCubeId(cubid);
		setContentId(cntId);
		
	}
	
	public void deleteContentFromCube()
	{
		UserService userService = new UserServiceImpl();
		userService.deleteContentFromCube(cubeId, contentId);
	}

}