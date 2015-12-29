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

public class AddContentJSON {
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
	
	
	public void LoadJSONRequest( long usrid , long cubid , InputStream incomingJson)
	{
		JsonFactory jsonFactory = new JsonFactory();
		
		setUserId(usrid);
		setCubeId(cubid);
		try
		{
			JsonParser jsonParser = jsonFactory.createJsonParser(incomingJson);
			
			while( jsonParser.nextToken() != JsonToken.END_OBJECT)
			{
				String token = jsonParser.getCurrentName();
				
				if("content_id".equals(token))
				{
					jsonParser.nextToken();
					setContentId(Long.parseLong(jsonParser.getText()));
				}
			}
		}
		catch(JsonParseException e)
		{
			setMalformedJsonRequest(true);
		}
		catch( IOException e)
		{
			log.error("IOException occured");
		}
		
	}
	
	public String BuildJSON()
	{
		StringBuilder sb = new StringBuilder();
		UserService userService = new UserServiceImpl();
		
		if(isMalformedJsonRequest() == false)
		{
			if(getContentId() == 0)
			{
				sb.append("Missing Content Id");
			}
		}
		else
		{
			sb.append("malformed JSON request");
		}
		
		JsonFactory jsonFactory = new MappingJsonFactory();
		StringWriter out = new StringWriter();
		
		try{
			JsonGenerator jsongenerator = jsonFactory.createJsonGenerator(out);
			jsongenerator.writeStartArray();
			jsongenerator.writeStartObject();
			
			if(sb.length() == 0)
			{
				List<String> addDetails = new ArrayList<String>();
				addDetails = userService.addContent(cubeId , contentId);
				
				jsongenerator.writeStringField("id", addDetails.get(0));
				jsongenerator.writeStringField("cube_id", addDetails.get(2));
				jsongenerator.writeStringField("content_id", addDetails.get(1));
				
				
			}
			else 
			{
				jsongenerator.writeStringField("Error : ", sb.toString());
			}
			
			jsongenerator.writeEndObject();
			jsongenerator.writeEndArray();
			jsongenerator.close();
			jsongenerator = null;
			
			return out.toString();
			
		}
		catch( IOException e)
		{
			return "Error Creating JSON file";
		}
	}

}
