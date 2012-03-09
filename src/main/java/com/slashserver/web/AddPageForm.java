
package com.slashserver.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.slashserver.model.Page;
import com.slashserver.persistence.PageStore;

/**
 * 
 */
@Controller
@RequestMapping("/page/new")
@SessionAttributes(types = Page.class)
public class AddPageForm {

	private final PageStore pageStore;


	@Autowired
	public AddPageForm(PageStore pageStore) {
		this.pageStore = pageStore;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		//AMDO: what does this do?
		dataBinder.setDisallowedFields("id");
	}

	@RequestMapping(method = RequestMethod.GET)
	public String setupForm(Model model) {
		Page page = new Page();
		model.addAttribute(page);
		return "pages/form";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String processSubmit(@ModelAttribute Page page, BindingResult result, SessionStatus status) {
		new PageValidator().validate(page, result);
		if (result.hasErrors()) {
			return "pages/form";
		}
		else {
			this.pageStore.store(page);
			status.setComplete();
			return "redirect:/pages/" + page.getId();
		}
	}

}
