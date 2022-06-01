
package com.biperf.core.service.certificates.impl;

import java.util.Map;

import com.biperf.core.dao.certificates.CertificateDAO;
import com.biperf.core.service.certificates.CertificateService;

public class CertificateServiceImpl implements CertificateService
{
  private CertificateDAO certificateDAO;

  @Override
  public Map<String, Object> getCertificateDetails( Long claimId )
  {
    return certificateDAO.getCertificateDetails( claimId );
  }

  public CertificateDAO getCertificateDAO()
  {
    return certificateDAO;
  }

  public void setCertificateDAO( CertificateDAO certificateDAO )
  {
    this.certificateDAO = certificateDAO;
  }

}
