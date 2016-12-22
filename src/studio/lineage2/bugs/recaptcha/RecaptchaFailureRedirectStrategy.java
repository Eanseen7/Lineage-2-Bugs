package studio.lineage2.bugs.recaptcha;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

public class RecaptchaFailureRedirectStrategy extends DefaultRedirectStrategy
{
	@Override
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException
	{
		UriComponentsBuilder urlBuilder = fromUriString(url);
		AuthenticationException exception = readAuthenticationException(request);
		if(exception instanceof RecaptchaAuthenticationException)
		{
			urlBuilder.queryParam(DefaultLoginPageGeneratingFilter.ERROR_PARAMETER_NAME, "missing-input-response");
		}
		else
		{
			urlBuilder.queryParam(DefaultLoginPageGeneratingFilter.ERROR_PARAMETER_NAME, "code.badLoginOrPassword");
		}
		super.sendRedirect(request, response, urlBuilder.build(true).toUriString());
	}

	protected AuthenticationException readAuthenticationException(HttpServletRequest request)
	{
		Object exception = request.getSession(false).getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		if(exception == null)
		{
			exception = request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
		if(exception == null)
		{
			throw new IllegalStateException("Missing " + WebAttributes.AUTHENTICATION_EXCEPTION + " session or request attribute");
		}
		return (AuthenticationException) exception;
	}
}