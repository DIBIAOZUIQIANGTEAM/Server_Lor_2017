package com.dgut.lor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.dgut.lor.util.CreateDateRecord;
import com.fasterxml.jackson.annotation.JsonIgnore;

/*
 * 用户基础信息表
 */
@Entity
public class User extends CreateDateRecord {
	String account;
	String passwordHash;
	String name;//普通用户昵称，商家店名
	String avatar;//头像本地相对地址
	String email;//邮箱，暂时没用到
	double coin;

	@Column(unique = true)
	public String getAccount() {
		return account;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public String getName() {
		return name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getCoin() {
		return coin;
	}

	public void setCoin(double coin) {
		this.coin = coin;
	}

}
