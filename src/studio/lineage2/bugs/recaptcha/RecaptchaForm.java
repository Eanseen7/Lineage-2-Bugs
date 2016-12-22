package studio.lineage2.bugs.recaptcha;

import java.beans.Transient;

public interface RecaptchaForm
{
	@Transient
	String getRecaptchaResponse();

	@Transient
	void setRecaptchaResponse(String recaptchaResponse);
}