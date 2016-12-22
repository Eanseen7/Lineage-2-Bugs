package studio.lineage2.bugs.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import studio.lineage2.bugs.model.MAccount;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 Eanseen
 18.03.2016
 */
@Component public class SiteInterceptor extends HandlerInterceptorAdapter
{
	@Value("${recaptcha.siteKey}") private String recaptchaSiteKey;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView model) throws Exception
	{
		//TODO Ссаный костыль, точно знаю что есть более изящное решение
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null && authentication.getPrincipal() instanceof MAccount && request.getRequestURI().contains("/enter"))
		{
			if(!request.getRequestURI().contains("logout"))
			{
				response.sendRedirect("/admin");
			}
		}

		if(model == null)
		{
			return;
		}

		model.addObject("recaptchaSiteKey", recaptchaSiteKey);

		authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null)
		{
			Object o = authentication.getPrincipal();
			if(o instanceof MAccount)
			{
				MAccount mAccount = (MAccount) o;
				model.addObject("mAccount", mAccount);
			}
		}
	}
}