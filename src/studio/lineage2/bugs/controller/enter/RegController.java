package studio.lineage2.bugs.controller.enter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import studio.lineage2.bugs.controller.SiteController;
import studio.lineage2.bugs.model.MAccount;
import studio.lineage2.bugs.recaptcha.RecaptchaFormValidator;
import studio.lineage2.bugs.service.MAccountService;

import javax.validation.Valid;

/**
 Eanseen
 30.05.2016
 */
@Controller @RequestMapping("/enter/reg") public class RegController
{
	@Autowired private MAccountService mAccountService;
	@Autowired private RecaptchaFormValidator recaptchaFormValidator;

	@InitBinder
	public void initBinder(WebDataBinder binder)
	{
		binder.addValidators(recaptchaFormValidator);
	}

	@RequestMapping(method = {RequestMethod.GET})
	public String index(ModelMap model)
	{
		model.addAttribute("mAccount", new MAccount());
		SiteController.addAttributes(model, "enter/reg.vm");
		return "main";
	}

	@RequestMapping(method = {RequestMethod.POST})
	public String reg(ModelMap model, @ModelAttribute(value = "mAccount") @Valid MAccount mAccount, BindingResult result)
	{
		if(result.hasErrors())
		{
			SiteController.addAttributes(model, "enter/reg.vm");
			return "main";
		}

		if(mAccountService.containsByUsername(mAccount.getUsername().toLowerCase()))
		{
			result.rejectValue("username", "mAccount.username", "Пользователь существует");
			SiteController.addAttributes(model, "enter/reg.vm");
			return "main";
		}

		if(!mAccount.getPassword().equals(mAccount.getRepeatPassword()))
		{
			result.rejectValue("repeatPassword", "mAccount.repeatPassword", "Пароли не совпадают");
			SiteController.addAttributes(model, "enter/reg.vm");
			return "main";
		}

		mAccount.setUsername(mAccount.getUsername().toLowerCase());
		mAccount.setPassword(new BCryptPasswordEncoder().encode(mAccount.getPassword()));
		mAccount.setAdmin(false);
		mAccountService.save(mAccount);

		SiteController.addAttributes(model, "enter/reg.vm");
		return "main";
	}
}