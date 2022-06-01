
package com.biperf.core.service.certificates;

import java.util.Map;

import com.biperf.core.service.SAO;

public interface CertificateService extends SAO
{
  /** BEAN_NAME for referencing in tests and spring config files. */
  public final String BEAN_NAME = "certificateService";

  Map<String, Object> getCertificateDetails( Long claimId );
}
