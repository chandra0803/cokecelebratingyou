
package com.biperf.core.dao.certificates;

import java.util.Map;

public interface CertificateDAO
{
  Map<String, Object> getCertificateDetails( Long claimId );
}
