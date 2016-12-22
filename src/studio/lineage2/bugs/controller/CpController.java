package studio.lineage2.bugs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import studio.lineage2.bugs.CmsApplication;
import studio.lineage2.bugs.service.BugService;

/**
 Eanseen
 23.06.2016
 */
@Controller @RequestMapping("/admin") public class CpController
{
	@Autowired private BugService bugService;

	@RequestMapping(method = {RequestMethod.GET})
	public String index(ModelMap model)
	{
		model.addAttribute("projects", CmsApplication.getProjects());
		model.addAttribute("types", CmsApplication.getTypes());
		model.addAttribute("bugTags", bugService.findAll());
		model.addAttribute("bugService", bugService);
		SiteController.addAttributes(model, "cp/index.vm");
		return "main";
	}

	@RequestMapping(value = "/viewByBugTag/{bugTagId}", method = {RequestMethod.GET})
	public String viewByBugTag(ModelMap model, @PathVariable long bugTagId)
	{
		model.addAttribute("bugTag", bugService.findOne(bugTagId));
		model.addAttribute("projects", CmsApplication.getProjects());
		model.addAttribute("types", CmsApplication.getTypes());
		model.addAttribute("bugService", bugService);
		SiteController.addAttributes(model, "cp/viewBugTag.vm");
		return "main";
	}
}