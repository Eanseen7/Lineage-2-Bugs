package studio.lineage2.bugs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import studio.lineage2.bugs.CmsApplication;
import studio.lineage2.bugs.model.Bug;
import studio.lineage2.bugs.model.BugTag;
import studio.lineage2.bugs.service.BugService;
import studio.lineage2.bugs.xml.Response;
import studio.lineage2.bugs.xml.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 Eanseen
 21.06.2016
 */
@RestController @RequestMapping("/api") public class ApiController
{
	private static Map<String, Long> lastSends = new HashMap<>();
	@Autowired private BugService bugService;

	@RequestMapping(value = "/getNames/{type}/{query}", method = RequestMethod.GET)
	Response getSkillNames(@PathVariable String type, @PathVariable String query)
	{
		Response response = new Response();
		response.setSuccess(true);

		Set<Result> names;

		if(query == null || query.isEmpty() || query.length() < 3)
		{
			names = new HashSet<>();
			Result result = new Result();
			result.setName("");
			result.setValue("");
			names.add(result);
			response.setResults(names);
			return response;
		}

		switch(type)
		{
			case "Skill":
				names = CmsApplication.getSkillNames().stream().filter(name->name.getValue().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toSet());
				break;
			case "Quest":
				names = CmsApplication.getQuestNames().stream().filter(name->name.getValue().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toSet());
				break;
			case "Instance":
				names = CmsApplication.getInstanceNames().stream().filter(name->name.getValue().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toSet());
				break;
			case "Monster":
				names = CmsApplication.getMonsterNames().stream().filter(name->name.getValue().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toSet());
				break;
			case "Item":
				names = CmsApplication.getItemNames().stream().filter(name->name.getValue().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toSet());
				break;
			case "Other":
				names = new HashSet<>();
				Result result = new Result();
				result.setName(query);
				result.setValue(query);
				names.add(result);
				for(BugTag bugTag : bugService.findAll())
				{
					if(bugTag.getTypeId() == 100 && bugTag.getName().toLowerCase().contains(query.toLowerCase()))
					{
						result = new Result();
						result.setName(bugTag.getName());
						result.setValue(bugTag.getName());
						names.add(result);
					}
				}
				break;
			default:
				names = new HashSet<>();
		}

		if(names.isEmpty())
		{
			Result result = new Result();
			result.setName("");
			result.setValue("");
			names.add(result);
		}

		response.setResults(names);

		return response;
	}

	@RequestMapping(value = "/bugAdd", method = RequestMethod.POST)
	String addBug(int projectId, int typeId, String name, String oldWork, String newWork, String other, String rulles, HttpServletRequest request)
	{
		if(!CmsApplication.getProjects().containsKey(projectId) || !CmsApplication.getTypes().containsKey(typeId) || name.isEmpty() || oldWork.isEmpty() || newWork.isEmpty() || other.isEmpty() || rulles == null || !rulles.equals("on"))
		{
			return "error";
		}

		if(lastSends.containsKey(request.getRemoteAddr()) && lastSends.get(request.getRemoteAddr()) + 60000 > System.currentTimeMillis())
		{
			return "spam";
		}

		lastSends.put(request.getRemoteAddr(), System.currentTimeMillis());

		BugTag bugTag = bugService.findOne(projectId, typeId, name);

		Bug bug = new Bug();
		bug.setBugTagId(bugTag.getId());
		bug.setOldWork(oldWork);
		bug.setNewWork(newWork);
		bug.setOther(other);
		bug.setIp(request.getRemoteAddr());
		bugService.save(bug);

		return "success";
	}
}