
package com.biperf.core.service.company;

import com.biperf.core.value.companysetup.v1.company.CompanyView;

public interface CompanySetupRepository
{
  public CompanyView getCompany( String companyIdentifier ) throws Exception;
}
