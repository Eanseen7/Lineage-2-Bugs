package studio.lineage2.bugs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import studio.lineage2.bugs.service.BugService;

/**
 Eanseen
 21.06.2016
 */
@RestController @RequestMapping("/admin/api") public class AdminApiController
{
	@Autowired private BugService bugService;

	@RequestMapping(value = "/deleteBug/{bugId}", method = {RequestMethod.GET})
	public void deleteBug(@PathVariable long bugId)
	{
		bugService.deleteBug(bugId);
	}
}