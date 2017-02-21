package com.dgut.lor.controller;

import java.io.File;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonFactoryBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dgut.lor.entity.User;
import com.dgut.lor.service.IRecordsService;
import com.dgut.lor.service.IUserService;
import com.dgut.lor.util.HttpRequest;
import com.dgut.lor.util.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/user")
public class UserAPIController {

	@Autowired
	IUserService userService;

	@Autowired
	IRecordsService recordsService;

	@RequestMapping(value = "/register/{account}+{passwordHash}", method = RequestMethod.GET)
	public JSONObject registerGet(@PathVariable String account,
			@PathVariable String passwordHash) {

		if (userService.findByAccount(account) == null) {

			User user = new User();
			user.setAccount(account);
			user.setPasswordHash(passwordHash);
			user = userService.save(user);

			return JsonUtils.toJson(1, "注册成功", user);

		}
		System.out.println("注册失败");
		return JsonUtils.toJson(2, "注册失败,用户已经存在", "");
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public JSONObject register(@RequestParam String account,
			@RequestParam String passwordHash) {

		if (userService.findByAccount(account) == null) {

			User user = new User();
			user.setAccount(account);
			user.setPasswordHash(passwordHash);
			System.out.println("注册成功");
			return JsonUtils.toJson(1, "注册成功", userService.save(user));

		}
		System.out.println("注册失败");
		return JsonUtils.toJson(2, "注册失败,用户已经存在", "");
	}

	
//	@RequestMapping(value = "/register", method = RequestMethod.POST)
//	public JSONObject registerBD(@RequestParam String account,
//			@RequestParam String passwordHash) {
//
//		if (userService.findByAccount(account) == null) {
//
//			User user = new User();
//			user.setAccount(account);
//			user.setPasswordHash(passwordHash);
//			System.out.println("注册成功");
//			return JsonUtils.toJson(1, "注册成功", userService.save(user));
//
//		}
//		System.out.println("注册失败");
//		return JsonUtils.toJson(2, "注册失败,用户已经存在", "");
//	}
//	
	
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public JSONObject login(@RequestParam String account,
			@RequestParam String passwordHash, HttpServletRequest request) {
		User user = userService.findByAccount(account);
		if (user == null) {
			System.out.println("login失败,用户不存在");
			return JsonUtils.toJson(2, "login失败,用户不存在", "");
		} else if (user.getPasswordHash().equals(passwordHash)) {
			HttpSession session = request.getSession(true);
			session.setAttribute("uid", user.getId());
			Integer uid = (Integer) session.getAttribute("uid");
			System.out.println("login成功 uid:"+uid);
			return JsonUtils.toJson(1, "login成功", userService.save(user));
		} else {
			System.out.println("login失败");
			return JsonUtils.toJson(3, "login失败,密码不正确", "");
		}
	}

	@RequestMapping(value = "/me", method = RequestMethod.GET)
	public User getCurrentUser(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		Integer uid = (Integer) session.getAttribute("uid");
		return userService.findById(uid);
	}
	@RequestMapping(value = "/me", method = RequestMethod.POST)
	public JSONObject getCurrentUserPost(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		Integer uid = (Integer) session.getAttribute("uid");
		User user=userService.findById(uid);
		if (user!=null) {
			return JsonUtils.toJson(1, "查找成功", user);
		}
		return JsonUtils.toJson(2, "查找失败", "");
	}
	
	@RequestMapping(value = "/update/avatar", method = RequestMethod.POST)
	public User UpdateAvatar(MultipartFile avatar, HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (avatar != null) {
			try {
				String realPath = request.getSession().getServletContext()
						.getRealPath("/WEB-INF/upload/user");
				FileUtils.copyInputStreamToFile(avatar.getInputStream(),
						new File(realPath, getCurrentUser(request).getAccount()
								+ ".png"));
				user.setAvatar("upload/user/" + user.getAccount() + ".png");
				user=userService.save(user);
			
				return user;
				
				
			} catch (Exception e) {
				e.printStackTrace();
			
			}
		}
			return null;
		
	}

	@RequestMapping(value = "/update/password", method = RequestMethod.POST)
	public JSONObject UpdatePassword(@RequestParam String newPassword,
			HttpServletRequest request) {
		User user = getCurrentUser(request);
		user.setPasswordHash(newPassword);
		user=userService.save(user);
		if (user!=null) {
			return JsonUtils.toJson(1, "密码修改成功", user);
		}
		return JsonUtils.toJson(2, "密码修改失败", "");
	}

	@RequestMapping(value = "/forget/password", method = RequestMethod.POST)
	public JSONObject ForgetPassword(@RequestParam String account,
			@RequestParam String newPassword) {

		User user = userService.findByAccount(account);
		if (user == null) {
			return null;
		}
		user.setPasswordHash(newPassword);
		user=userService.save(user);
		if (user!=null) {
			return JsonUtils.toJson(1, "密码修改成功", user);
		}
		return JsonUtils.toJson(2, "密码修改失败", "");
		
	}

	@RequestMapping(value = "/update/userName", method = RequestMethod.POST)
	public JSONObject UpdateUserName(@RequestParam String userName,
			HttpServletRequest request) {
		User user = getCurrentUser(request);
		user.setName(userName);
		user=userService.save(user);
		
		
		if (user!=null) {
			return JsonUtils.toJson(1, "昵称修改成功", user);
		}
		return JsonUtils.toJson(2, "昵称修改失败", "");
	}

	@RequestMapping(value = "/update/email", method = RequestMethod.POST)
	public User UpdateUserEmail(@RequestParam String email,
			HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user == null) {
			return null;
		}
		user.setEmail(email);
		return userService.save(user);
	}

	@RequestMapping(value = "/finduser", method = RequestMethod.POST)
	public JSONObject FindUserByAccount(@RequestParam String account) {
		User user= userService.findByAccount(account);
		if (user!=null) {
			return JsonUtils.toJson(1, "成功", user);
		}
		return JsonUtils.toJson(2, "失败", "");
	}
}