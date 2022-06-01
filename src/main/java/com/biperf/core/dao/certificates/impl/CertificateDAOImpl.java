
package com.biperf.core.dao.certificates.impl;

import java.util.Map;

import javax.sql.DataSource;

import com.biperf.core.dao.certificates.CertificateDAO;
import com.biperf.core.dao.claim.hibernate.CallPrcNominationCertificateDtl;

public class CertificateDAOImpl implements CertificateDAO
{
  private DataSource dataSource;

  @Override
  public Map<String, Object> getCertificateDetails( Long claimId )
  {
    CallPrcNominationCertificateDtl prc = new CallPrcNominationCertificateDtl( dataSource );
    return prc.executeProcedure( claimId );
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

}
