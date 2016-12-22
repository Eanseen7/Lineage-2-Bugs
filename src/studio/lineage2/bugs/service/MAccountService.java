package studio.lineage2.bugs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import studio.lineage2.bugs.model.MAccount;
import studio.lineage2.bugs.repository.MAccountRepository;

@Service public class MAccountService
{
	@Autowired private MAccountRepository mAccountRepository;

	public void save(MAccount user)
	{
		mAccountRepository.save(user);
	}

	public MAccount findByUsername(String username) throws UsernameNotFoundException
	{
		for(MAccount user : mAccountRepository.findAll())
		{
			if(user.getUsername().equals(username))
			{
				return user;
			}
		}
		throw new UsernameNotFoundException(username);
	}

	public boolean containsByUsername(String username)
	{
		for(MAccount user : mAccountRepository.findAll())
		{
			if(user.getUsername().equals(username))
			{
				return true;
			}
		}
		return false;
	}
}