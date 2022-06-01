
package com.biperf.core.service.company.impl;

import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biperf.cache.Cache;
import com.biperf.cache.CacheFactory;
import com.biperf.core.dao.company.CompanyDAO;
import com.biperf.core.domain.company.Company;
import com.biperf.core.service.company.CompanyService;
import com.biperf.core.service.company.CompanyServiceFactory;
import com.biperf.core.value.companysetup.v1.company.CompanyView;

@Service( "companyService" )
public class CompanyServiceImpl implements CompanyService
{

  private static final Log log = LogFactory.getLog( CompanyServiceImpl.class );
  private Cache companyDetailCache;

  @Autowired
  private CompanyDAO companyDAO;

  @Autowired
  private CompanyServiceFactory companyServiceFactory;

  @Override
  public Company saveCompany( Company company ) throws Exception
  {
    company = companyDAO.saveCompany( company );
    return company;
  }

  @Override
  public Company getCompanyDetail()
  {
    Company company = (Company)companyDetailCache.get( "company-info" );
    if ( Objects.isNull( company ) )
    {
      try
      {
        company = companyDAO.getCompanyDetail();
        companyDetailCache.put( "company-info", company );
      }
      catch( Exception exception )
      {
        log.error( "Company detail not initialized properly", exception );
      }
    }
    
    if ( Objects.nonNull( company ) )
    {
      return company;
    }

    return null;
  }

  public void setCacheFactory( CacheFactory cacheFactory )
  {
    companyDetailCache = cacheFactory.getCache( "companyDetail" );
  }

  public void clearPropertyFromCache()
  {
    if ( companyDetailCache != null )
    {
      companyDetailCache.clear();
    }
  }

  @Override
  public CompanyView getCompanyFromCompanySetup( String companyIdentifier ) throws Exception
  {
    return companyServiceFactory.getRepo().getCompany( companyIdentifier );
  }

}
