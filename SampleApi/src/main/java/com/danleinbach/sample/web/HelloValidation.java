package com.danleinbach.sample.web;

import com.danleinbach.sample.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created: 12/13/13
 *
 * @author Daniel
 */
@Controller
@RequestMapping("/home")
public class HelloValidation {

	@RequestMapping(value = "/{age:[0-9]+}", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public User validateRequest(@RequestBody User user) {
		return user;
	}
}
