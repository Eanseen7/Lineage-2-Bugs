package studio.lineage2.bugs.controller.enter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import studio.lineage2.bugs.controller.SiteController;
import studio.lineage2.bugs.model.MAccount;

@Controller @RequestMapping("/enter") public class AuthController
{
	@RequestMapping(method = {RequestMethod.GET})
	public String index(ModelMap model)
	{
		SiteController.addAttributes(model, "enter/index.vm");
		return "main";
	}

	@RequestMapping(value = "/auth", method = {RequestMethod.GET})
	public String auth(ModelMap model, @RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout)
	{
		if(error == null)
		{
			error = "";
		}
		switch(error)
		{
			case "missing-input-response":
				error = "Вы не прошли проверку";
				break;
			case "code.badLoginOrPassword":
				error = "Доступ запрещен";
				break;
		}
		model.addAttribute("error", error);
		model.addAttribute("logout", logout == null ? "" : "Вы вышли");
		model.addAttribute("mAccount", new MAccount());
		SiteController.addAttributes(model, "enter/auth.vm");
		return "main";
	}
}