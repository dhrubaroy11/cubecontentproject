package com.demo.service;

import java.util.List;

import com.demo.model.Content;
import com.demo.model.Cube;

public interface UserService {
	
	public List<String> createUser(String username , String city);
	public List<String> createCube(long id , String name);
	public List<String> createContent(long id , String link);
	public List<String> addContent(long cubeId , long contentId);
	public void deleteContentFromCube(long cubeId , long contentId);
	public void deleteCube(long cubeId );
	public List<String> ShareCube( long cubeId , long userId1 , long userId2);
	public List<String> ShareContent( long contentId , long userId1 , long userId2);
	public List<Cube> getAllCube( long userId);
	public List<Content> getAllContent( long userId);
	public boolean isUserContentLink(long userId , long contentId);
	public boolean isUserCubeLink(long userId , long cubeId);
	

}
