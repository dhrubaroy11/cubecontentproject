package com.demo.service;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.demo.dao.UserDAO;
import com.demo.model.Content;
import com.demo.model.Cube;

public class UserServiceImpl  implements UserService{

	ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	UserDAO userDAO = (UserDAO) context.getBean("userDAO"); 
	public List<String> createUser(String username, String city) {
		List<String> userDetails = userDAO.insertUser(username, city);
		return userDetails;
	}
	public List<String> createCube(long id, String name) {
		List<String> cubeDetails = userDAO.insertCube(id, name);
		return cubeDetails;
	}
	public List<String> createContent(long id, String link) {
		List<String> contentDetails = userDAO.insertContent(id, link);
		return contentDetails;
	}
	public List<String> addContent(long cubeId, long contentId) {
		List<String> addDetails = userDAO.addContent(cubeId , contentId);
		return addDetails;
	}
	public void deleteContentFromCube(long cubeId, long contentId) {
		userDAO.deleteContentFromCube(cubeId, contentId);
		
	}
	public void deleteCube(long cubeId) {
		userDAO.deleteCube(cubeId);
		
	}
	public List<String> ShareCube(long cubeId, long userId1, long userId2) {
		List<String> sharedDetails = userDAO.shareCube( cubeId,  userId1,  userId2);
		return sharedDetails;
	}
	public List<String> ShareContent(long contentId, long userId1, long userId2) {
		List<String> sharedDetails = userDAO.shareContent(contentId, userId1, userId2);
		return sharedDetails;
	}
	public List<Cube> getAllCube(long userId) {
		List<Cube> cubeList = userDAO.getAllCube(userId);
		return cubeList;
	}
	
	public List<Content> getAllContent(long userId) {
		List<Content> contentList = userDAO.getAllContent(userId);
		return contentList;
	}
	public boolean isUserContentLink(long userId, long contentId) {
		return userDAO.isUserContentLink(userId, contentId);
	}
	public boolean isUserCubeLink(long userId, long cubeId) {
		return userDAO.isUserCubeLink(userId, cubeId);
	}
	

}
