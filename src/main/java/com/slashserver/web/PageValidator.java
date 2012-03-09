package com.slashserver.web;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import com.slashserver.model.Page;

/**
 * 
 */
public class PageValidator {

	public void validate(Page page, Errors errors) {
		if (!StringUtils.hasLength(page.getName())) {
			errors.rejectValue("name", "required", "required");
		}
		
	}

}
