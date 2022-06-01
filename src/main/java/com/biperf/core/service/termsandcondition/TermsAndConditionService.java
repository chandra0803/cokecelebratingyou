package com.biperf.core.service.termsandcondition;

import com.biperf.core.service.SAO;

public interface TermsAndConditionService extends SAO
{
  public static final String BEAN_NAME = "termsAndConditionService";

  public String getTermsAndConditionText();
  
}
