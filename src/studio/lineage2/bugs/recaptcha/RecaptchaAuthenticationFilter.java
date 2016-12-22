package studio.lineage2.bugs.recaptcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Collections.singletonList;

public class RecaptchaAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
	@Autowired private ReCaptchaService reCaptchaService;
	@Value("${recaptcha.formFieldName}") private String recaptchFormFieldName;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
	{
		if(getUsernameParameter() == null)
		{
			throw new RecaptchaAuthenticationException(singletonList(ErrorCode.MISSING_USERNAME_REQUEST_PARAMETER));
		}
		ValidationResult result = reCaptchaService.check(request.getParameter(recaptchFormFieldName), request.getRemoteAddr());
		if(!result.isSuccess())
		{
			throw new RecaptchaAuthenticationException(result.getErrorCodes());
		}
		return super.attemptAuthentication(request, response);
	}
}