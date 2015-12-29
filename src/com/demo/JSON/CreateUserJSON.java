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

public class CreateUserJSON {
	private String userName = "";
	private String city     = "";
	private boolean malformedJsonRequest = false;
	private Log log = LogFactory.getLog(this.getClass().getName());
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public boolean isMalformedJsonRequest() {
		return malformedJsonRequest;
	}
	public void setMalformedJsonRequest(boolean malformedJsonRequest) {
		this.malformedJsonRequest = malformedJsonRequest;
	}
	
	
	public void LoadJSONRequest(InputStream incomingJson)
	{
		JsonFactory jsonFactory = new JsonFactory();
		
		try
		{
			JsonParser jsonParser = jsonFactory.createJsonParser(incomingJson);
			
			while( jsonParser.nextToken() != JsonToken.END_OBJECT)
			{
				String token = jsonParser.getCurrentName();
				
				if("name".equals(token))
				{
					jsonParser.nextToken();
					setUserName(jsonParser.getText());
				}
				if("city".equals(token))
				{
					jsonParser.nextToken();
					setCity(jsonParser.getText());
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
			if(getUserName().isEmpty())
			{
				sb.append("Missing Username");
			}
			if(getCity().isEmpty())
			{
				sb.append("Missing City");
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
				userDetails = userService.createUser(userName, city);
				
				jsongenerator.writeStringField("id", userDetails.get(0));
				jsongenerator.writeStringField("name", userDetails.get(1));
				jsongenerator.writeStringField("city", userDetails.get(2));
				
				
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
