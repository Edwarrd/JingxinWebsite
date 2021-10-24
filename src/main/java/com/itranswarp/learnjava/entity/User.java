package com.itranswarp.learnjava.entity;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class User {

	private int id;
	private String gender;
	private String email;
	private String password;
	private String name;
	private Date birth;
	private String phone;
	private int checkedDate;
	private long createdAt;


    public int getId() {
		return id;
	}

	public void setId(int account_id) {
		this.id = account_id;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedDateTime() {
		return Instant.ofEpochMilli(this.createdAt).atZone(ZoneId.systemDefault())
				.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

	//what is this used for?
	public String getImageUrl() {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			//here used to be this.email
			byte[] hash = md.digest(this.phone.trim().toLowerCase().getBytes(StandardCharsets.UTF_8));
			return "https://www.gravatar.com/avatar/" + String.format("%032x", new BigInteger(1, hash));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public Date getBirth() { return birth; }

	public void setBirth(Date birth) {this.birth = birth;}

	public String getPhone() {return phone; }

	public void setPhone(String phone) {this.phone = phone;}

	public String getGender() {return gender; }

	public void setGender(String gender) {this.gender = gender;}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("User[id=%s, email=%s, name=%s, password=%s, createdAt=%s, createdDateTime=%s]", getId(),
				getEmail(), getName(), getPassword(), getCreatedAt(), getCreatedDateTime());
	}

    public int getCheckedDate() {
        return checkedDate;
    }

    public void setCheckedDate(int checkedDate) {
        this.checkedDate = checkedDate;
    }
}
