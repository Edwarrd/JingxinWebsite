package com.itranswarp.learnjava.web;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.SimpleFormatter;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.itranswarp.learnjava.entity.User;
import com.itranswarp.learnjava.service.UserService;

@Controller
public class UserController {

	public static final String KEY_USER = "__user__";

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	UserService userService;

	@GetMapping("/")
	public ModelAndView index(HttpSession session) {
		User user = (User) session.getAttribute(KEY_USER);
		Map<String, Object> model = new HashMap<>();
		if (user != null) {
			model.put("user", user);
		}
		return new ModelAndView("FrontPage-1.html",model);
	}

	@GetMapping("/checkin")
    public ModelAndView check(HttpSession session) {
	    User user = (User) session.getAttribute(KEY_USER);
        if (user == null) {
            return new ModelAndView("redirect:/signin");
        }
        else if (user.getCheckedDate()!=0){
            return new ModelAndView("redirect:/", Map.of("user", user));
        }
        else {
            Date checkIn = new Date(System.currentTimeMillis());
            try {
                user = userService.checkIn(user, user.getId(), user.getName(), checkIn);
                session.setAttribute(KEY_USER, user);
            } catch (RuntimeException e) {
                return new ModelAndView("redirect:/");
            }
        }
        return new ModelAndView("redirect:/checkin", Map.of("user", user));
    }

	@GetMapping("/register")
	public ModelAndView register() {
		return new ModelAndView("Register.html");
	}

	@PostMapping("/register")
	public ModelAndView doRegister(@RequestParam("name") String name, @RequestParam("gender") String gender, @RequestParam("phone") String phone,
								   @RequestParam("birth") Date birth, @RequestParam("password") String password) {
		try {
			User user = userService.register(name, gender, phone, birth, password);
			logger.info("user registered: {}", user.getEmail());
		} catch (RuntimeException e) {
			return new ModelAndView("Register.html", Map.of("phone", phone, "error", "Register failed"));
		}
		return new ModelAndView("redirect:/signin");
	}

	@GetMapping("/signin")
	public ModelAndView signin(HttpSession session) {
		User user = (User) session.getAttribute(KEY_USER);
		if (user != null) {
			return new ModelAndView("redirect:/user/profile");
		}
		return new ModelAndView("Signin.html");
	}

	@PostMapping("/signin")
	public ModelAndView doSignin(@RequestParam("phone") String phone, @RequestParam("password") String password,
			HttpSession session) {
		try {
			User user = userService.signin(phone, password);
			session.setAttribute(KEY_USER, user);
		} catch (RuntimeException e) {
			return new ModelAndView("Signin.html", Map.of("phone", phone, "error", "Signin failed"));
		}
		return new ModelAndView("redirect:/ ");
	}

	@GetMapping("/profile")
	public ModelAndView profile(HttpSession session) {
		User user = (User) session.getAttribute(KEY_USER);
		if (user == null) {
			return new ModelAndView("redirect:/signin");
		}
		return new ModelAndView("Account.html", Map.of("user", user));
	}

    @GetMapping("/intro")
    public ModelAndView intro(HttpSession session) {
        User user = (User) session.getAttribute(KEY_USER);
        Map<String, Object> model = new HashMap<>();
        if (user != null) {
            model.put("user", model);
        }
        return new ModelAndView("Intro-1.html",Map.of("user", user));
    }

    @GetMapping("/activity")
    public ModelAndView activity(HttpSession session) {
        User user = (User) session.getAttribute(KEY_USER);
        Map<String, Object> model = new HashMap<>();
        if (user != null) {
            model.put("user", model);
        }
        return new ModelAndView("Activity-1.html",model);
    }

    @GetMapping("/exp")
    public ModelAndView exp(HttpSession session) {
        User user = (User) session.getAttribute(KEY_USER);
        Map<String, Object> model = new HashMap<>();
        if (user != null) {
            model.put("user", model);
        }
        return new ModelAndView("Exp-1.html",model);
    }

    @GetMapping("/discuss")
    public ModelAndView discuss(HttpSession session) {
        User user = (User) session.getAttribute(KEY_USER);
        Map<String, Object> model = new HashMap<>();
        if (user != null) {
            model.put("user", model);
        }
        return new ModelAndView("Discuss-1.html",model);
    }

    @GetMapping("/story")
    public ModelAndView story(HttpSession session) {
        User user = (User) session.getAttribute(KEY_USER);
        Map<String, Object> model = new HashMap<>();
        if (user != null) {
            model.put("user", model);
        }
        return new ModelAndView("Story-1.html",model);
    }

    @GetMapping("/msg")
    public ModelAndView msg(HttpSession session) {
        User user = (User) session.getAttribute(KEY_USER);
        Map<String, Object> model = new HashMap<>();
        if (user != null){
            model.put("user", model);
        }
        return new ModelAndView("Msg-1.html",model);
    }

	@GetMapping("/signout")
	public String signout(HttpSession session) {
		session.removeAttribute(KEY_USER);
		return "redirect:/ ";
	}
}
