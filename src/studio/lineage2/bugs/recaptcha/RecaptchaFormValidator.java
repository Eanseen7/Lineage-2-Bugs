package studio.lineage2.bugs.recaptcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.servlet.http.HttpServletRequest;

@Component public class RecaptchaFormValidator implements Validator
{
	private final HttpServletRequest httpServletRequest;
	private final ReCaptchaService reCaptchaService;

	@Value("${recaptcha.secretKey}") private String secretKey;

	@Value("${recaptcha.verificationUrl}") private String verificationUrl;

	@Value("${recaptcha.formFieldNameReplacement}") private String recaptchaFormFieldNameReplacement;

	@Autowired
	public RecaptchaFormValidator(HttpServletRequest httpServletRequest, ReCaptchaService reCaptchaService)
	{
		this.httpServletRequest = httpServletRequest;
		this.reCaptchaService = reCaptchaService;
	}

	@Override
	public boolean supports(Class<?> clazz)
	{
		return RecaptchaForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors)
	{
		RecaptchaForm form = (RecaptchaForm) target;
		try
		{
			ValidationResult result = reCaptchaService.check(form.getRecaptchaResponse(), httpServletRequest.getRemoteAddr());
			if(!result.isSuccess())
			{
				for(ErrorCode errorCode : result.getErrorCodes())
				{
					errors.rejectValue(recaptchaFormFieldNameReplacement, errorCode.getText(), errorCode.getText());
				}
			}
		}
		catch(RecaptchaAuthenticationException exception)
		{
			ErrorCode errorCode = ErrorCode.VALIDATION_HTTP_ERROR;
			errors.rejectValue(recaptchaFormFieldNameReplacement, errorCode.getText(), errorCode.getText());
		}
	}
}