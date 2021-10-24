package com.itranswarp.learnjava.service;

import java.sql.Date;
import java.sql.Statement;

import com.itranswarp.learnjava.entity.CheckIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.itranswarp.learnjava.entity.User;

@Component
public class UserService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	JdbcTemplate jdbcTemplate;

	RowMapper<User> userRowMapper = new BeanPropertyRowMapper<>(User.class);

	public User getUserById(long id) {
		return jdbcTemplate.queryForObject("SELECT * FROM accounts WHERE id = ?", new Object[] { id }, userRowMapper);
	}

	public User getUserByEmail(String email) {
		return jdbcTemplate.queryForObject("SELECT * FROM accounts WHERE email = ?", new Object[] { email },
				userRowMapper);
	}

	public User getUserByPhone(String phone) {
		return jdbcTemplate.queryForObject("SELECT * FROM accounts WHERE phone = ?", new Object[] { phone },
				userRowMapper);
	}

	public User signin(String phone, String password) {
		logger.info("try login by {}...", phone);
		User user = getUserByPhone(phone);
		if (user.getPassword().equals(password)) {
			return user;
		}
		throw new RuntimeException("login failed.");
	}

	public User register(String name, String gender, String phone, Date birth, String password) {
		logger.info("try register by {}...", phone);
		User user = new User();
		user.setGender(gender);
		user.setPhone(phone);
		user.setPassword(password);
		user.setName(name);
		user.setBirth(birth);
		KeyHolder holder = new GeneratedKeyHolder();
		if (1 != jdbcTemplate.update((conn) -> {
			var ps = conn.prepareStatement("INSERT INTO accounts(name, gender, phone, birth, password) VALUES(?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, user.getName());
            ps.setObject(2, user.getGender());
			ps.setObject(3, user.getPhone());
            ps.setObject(4, user.getBirth());
            ps.setObject(5, user.getPassword());
			return ps;
		}, holder)) {
			throw new RuntimeException("Insert failed.");
		}
		user.setId(holder.getKey().intValue());
		return user;
	}

	public void updateUser(User user) {
		if (1 != jdbcTemplate.update("UPDATE user SET name = ? WHERE id=?", user.getName(), user.getId())) {
			throw new RuntimeException("User not found by id");
		}
	}

	public User checkIn(User user, int account_id, String name, Date checkIn){
	    RowMapper<CheckIn> checkInRowMapper = new BeanPropertyRowMapper<>(CheckIn.class);
        String insertSql = "INSERT INTO dailycheck (id, name, checkdate) VALUES (?,?,?)";
        String sql = "SELECT * FROM dailycheck WHERE id = ? and checkdate = ?";
        if (jdbcTemplate.query(sql,new Object[] {account_id, checkIn}, checkInRowMapper).isEmpty()){
            if (1 != jdbcTemplate.update(insertSql, account_id, name, checkIn)) {
                throw new RuntimeException("Insert checkin Date error");
            }
        }
        else{
            int size = jdbcTemplate.query("SELECT * FROM dailycheck WHERE id = ?", new Object[]{account_id},checkInRowMapper).size();
            user.setCheckedDate(size);
        }

        return user;
    }


}
