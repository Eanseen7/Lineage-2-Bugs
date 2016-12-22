package studio.lineage2.bugs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import studio.lineage2.bugs.model.Bug;
import studio.lineage2.bugs.model.BugTag;
import studio.lineage2.bugs.repository.BugRepository;
import studio.lineage2.bugs.repository.BugTagRepository;

import java.util.ArrayList;
import java.util.List;

/**
 Eanseen
 23.06.2016
 */
@Service public class BugService
{
	@Autowired private BugRepository bugRepository;
	@Autowired private BugTagRepository bugTagRepository;

	public void save(Bug bug)
	{
		bugRepository.save(bug);
	}

	public void save(BugTag bugTag)
	{
		bugTagRepository.save(bugTag);
	}

	public List<BugTag> findAll()
	{
		return bugTagRepository.findAll();
	}

	public List<Bug> findByBugTagId(long bugTagId)
	{
		List<Bug> bugs = new ArrayList<>();
		for(Bug bug : bugRepository.findAll())
		{
			if(bug.getBugTagId() == bugTagId)
			{
				bugs.add(bug);
			}
		}
		return bugs;
	}

	public BugTag findOne(int projectId, int typeId, String name)
	{
		for(BugTag bugTag : bugTagRepository.findAll())
		{
			if(bugTag.getProjectId() == projectId && bugTag.getTypeId() == typeId && bugTag.getName().equals(name))
			{
				return bugTag;
			}
		}
		BugTag bugTag = new BugTag();
		bugTag.setProjectId(projectId);
		bugTag.setTypeId(typeId);
		bugTag.setName(name);
		save(bugTag);
		return bugTag;
	}

	public BugTag findOne(long bugTagId)
	{
		return bugTagRepository.findOne(bugTagId);
	}

	public int getCount(long bugTagId)
	{
		int count = 0;
		for(Bug bug : bugRepository.findAll())
		{
			if(bug.getBugTagId() == bugTagId)
			{
				count++;
			}
		}
		return count;
	}

	public void deleteBug(long bugId)
	{
		bugRepository.delete(bugId);
	}
}