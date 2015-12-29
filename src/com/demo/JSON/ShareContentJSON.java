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

public class ShareContentJSON {
	private long userId1     = 0;
	private long contentId     = 0;
	private long userId2    = 0;
	private boolean malformedJsonRequest = false;
	private Log log = LogFactory.getLog(this.getClass().getName());
	
	
	public long getContentId() {
		return contentId;
	}
	public void setContentId(long contentId) {
		this.contentId = contentId;
	}
	public long getUserId1() {
		return userId1;
	}
	public void setUserId1(long userId1) {
		this.userId1 = userId1;
	}
	public long getUserId2() {
		return userId2;
	}
	public void setUserId2(long userId2) {
		this.userId2 = userId2;
	}
	public boolean isMalformedJsonRequest() {
		return malformedJsonRequest;
	}
	public void setMalformedJsonRequest(boolean malformedJsonRequest) {
		this.malformedJsonRequest = malformedJsonRequest;
	}
	
	
	public void LoadJSONRequest( long usrid , long cntId , InputStream incomingJson)
	{
		JsonFactory jsonFactory = new JsonFactory();
		
		setUserId1(usrid);
		setContentId(cntId);
		try
		{
			JsonParser jsonParser = jsonFactory.createJsonParser(incomingJson);
			
			while( jsonParser.nextToken() != JsonToken.END_OBJECT)
			{
				String token = jsonParser.getCurrentName();
				
				if("user_id".equals(token))
				{
					jsonParser.nextToken();
					setUserId2(Long.parseLong(jsonParser.getText()));
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
			if(getUserId2() == 0)
			{
				sb.append("wrong Uder Id ");
			}
			if( !userService.isUserContentLink(userId1, contentId))
			{
			
				sb.append( " User is not authorized to share ");
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
				List<String> sharedDetails = new ArrayList<String>();
				sharedDetails = userService.ShareContent(contentId, userId1, userId2);
				
				jsongenerator.writeStringField("id", sharedDetails.get(0));
				jsongenerator.writeStringField("content_id", sharedDetails.get(1));
				jsongenerator.writeStringField("user_id", sharedDetails.get(2));
				
				
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

