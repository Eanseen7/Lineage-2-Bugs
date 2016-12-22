package studio.lineage2.bugs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import studio.lineage2.bugs.CmsApplication;
import studio.lineage2.bugs.service.BugService;

/**
 Eanseen
 21.06.2016
 */
@Controller @RequestMapping("/bugs") public class BugsController
{
	@Autowired private BugService bugService;

	@RequestMapping(value = "/new", method = {RequestMethod.GET})
	public String newBug(ModelMap model)
	{
		model.addAttribute("projects", CmsApplication.getProjects());
		model.addAttribute("types", CmsApplication.getTypes());

		model.addAttribute("skillNames", CmsApplication.getSkillNames());
		model.addAttribute("questNames", CmsApplication.getQuestNames());
		model.addAttribute("instanceNames", CmsApplication.getInstanceNames());
		model.addAttribute("monsterNames", CmsApplication.getMonsterNames());

		SiteController.addAttributes(model, "bugs/new.vm");
		return "main";
	}
}