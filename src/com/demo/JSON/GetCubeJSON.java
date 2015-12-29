package com.demo.JSON;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.MappingJsonFactory;

import com.demo.model.Cube;
import com.demo.service.UserService;
import com.demo.service.UserServiceImpl;

public class GetCubeJSON {
	
	
	
	public String BuildJSON( long usrId)
	{
		StringBuilder sb = new StringBuilder();
		UserService userService = new UserServiceImpl();
		
		JsonFactory jsonFactory = new MappingJsonFactory();
		StringWriter out = new StringWriter();
		
		try{
			JsonGenerator jsongenerator = jsonFactory.createJsonGenerator(out);
			jsongenerator.writeStartArray();
			
			if(sb.length() == 0)
			{
				List<Cube> cubeList = new ArrayList<Cube>();
				cubeList = userService.getAllCube(usrId);
				
				for(Cube cube:cubeList)
				{
			    jsongenerator.writeStartObject();
				jsongenerator.writeStringField("id", (Long.toString(cube.getId())).toString());
				jsongenerator.writeStringField("name", cube.getName().toString());
				jsongenerator.writeStringField("user_id", (Long.toString(usrId)).toString());
				jsongenerator.writeEndObject();
				}
				
				
			}
			else 
			{
				jsongenerator.writeStringField("Error : ", sb.toString());
			}
			
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
