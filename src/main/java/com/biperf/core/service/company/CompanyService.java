
package com.biperf.core.service.company;

import com.biperf.core.domain.company.Company;
import com.biperf.core.service.SAO;
import com.biperf.core.value.companysetup.v1.company.CompanyView;

public interface CompanyService extends SAO
{
  public static final String BEAN_NAME = "companyService";

  Company saveCompany( Company company ) throws Exception;

  Company getCompanyDetail();

  CompanyView getCompanyFromCompanySetup( String companyIdentifier ) throws Exception;
}
