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

public class CreateContentJSON {
	private long userId = 0;
	private String link     = "";
	private boolean malformedJsonRequest = false;
	private Log log = LogFactory.getLog(this.getClass().getName());
	
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public boolean isMalformedJsonRequest() {
		return malformedJsonRequest;
	}
	public void setMalformedJsonRequest(boolean malformedJsonRequest) {
		this.malformedJsonRequest = malformedJsonRequest;
	}
	
	
	public void LoadJSONRequest( long id ,InputStream incomingJson)
	{
		JsonFactory jsonFactory = new JsonFactory();
		
		setUserId(id);
		try
		{
			JsonParser jsonParser = jsonFactory.createJsonParser(incomingJson);
			
			while( jsonParser.nextToken() != JsonToken.END_OBJECT)
			{
				String token = jsonParser.getCurrentName();
				
				if("link".equals(token))
				{
					jsonParser.nextToken();
					setLink(jsonParser.getText());
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
			if(getLink().isEmpty())
			{
				sb.append("Missing Name");
			}
		}
		else
		{
			sb.append("malformed JSOn request");
		}
		
		JsonFactory jsonFactory = new MappingJsonFactory();
		StringWriter out = new StringWriter();
		
		try{
			JsonGenerator jsongenerator = jsonFactory.createJsonGenerator(out);
			jsongenerator.writeStartArray();
			jsongenerator.writeStartObject();
			
			if(sb.length() == 0)
			{
				List<String> userDetails = new ArrayList<String>();
				userDetails = userService.createContent(userId, link);
				
				jsongenerator.writeStringField("id", userDetails.get(0));
				jsongenerator.writeStringField("link", userDetails.get(2));
				jsongenerator.writeStringField("user_id", userDetails.get(1));
				
				
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