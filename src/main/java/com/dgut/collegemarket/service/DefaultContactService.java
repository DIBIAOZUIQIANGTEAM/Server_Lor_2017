package com.dgut.collegemarket.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dgut.collegemarket.entity.Contact;
import com.dgut.collegemarket.entity.User;
import com.dgut.collegemarket.repository.IContactRepository;
import com.dgut.collegemarket.repository.IUserRepository;

@Component
@Service
@Transactional
public class DefaultContactService implements IContactService {

	@Autowired
	IContactRepository contactRepo;

	@Override
	public Contact save(Contact contact) {
		return contactRepo.save(contact);
	}

}