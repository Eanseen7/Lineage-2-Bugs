package studio.lineage2.bugs.recaptcha;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Component public class RecaptchaResponseFilter implements Filter
{
	private static final Logger log = getLogger(RecaptchaResponseFilter.class);
	@Value("${recaptcha.formFieldName}") private String recaptchFormFieldName;
	@Value("${recaptcha.formFieldNameReplacement}") private String recaptchaFormFieldNameReplacement;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
	{
		if(servletRequest.getParameter(recaptchFormFieldName) != null)
		{
			ModifiedHttpServerRequest request = new ModifiedHttpServerRequest((HttpServletRequest) servletRequest, recaptchFormFieldName, recaptchaFormFieldNameReplacement);
			log.info("Filtering: " + request.getParameterMap());
			filterChain.doFilter(request, servletResponse);
		}
		else
		{
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	@Override
	public void destroy()
	{
	}

	private static class ModifiedHttpServerRequest extends HttpServletRequestWrapper
	{
		final Map<String, String[]> parameters;

		public ModifiedHttpServerRequest(HttpServletRequest request, String origin, String replacement)
		{
			super(request);
			log.info("Replacing: " + origin + " by " + replacement);
			parameters = new HashMap<>(request.getParameterMap());
			parameters.put(replacement, request.getParameterValues(origin));
			parameters.remove(origin);
		}

		@Override
		public String getParameter(String name)
		{
			return parameters.containsKey(name) ? parameters.get(name)[0] : null;
		}

		@Override
		public Map<String, String[]> getParameterMap()
		{
			return parameters;
		}

		@Override
		public Enumeration<String> getParameterNames()
		{
			return Collections.enumeration(parameters.keySet());
		}

		@Override
		public String[] getParameterValues(String name)
		{
			return parameters.get(name);
		}
	}
}