package studio.lineage2.bugs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 Eanseen
 29.05.2016
 */
@Controller @RequestMapping("/") public class SiteController
{
	public static void addAttributes(ModelMap model, String page)
	{
		model.addAttribute("header", "header.vm");
		model.addAttribute("body", "body.vm");
		model.addAttribute("footer", "footer.vm");

		model.addAttribute("menu", "menu.vm");

		model.addAttribute("body", page);
	}

	@RequestMapping(method = {
			RequestMethod.GET,
			RequestMethod.HEAD
	})
	public String index(ModelMap model)
	{
		addAttributes(model, "pages/index.vm");
		return "main";
	}
}