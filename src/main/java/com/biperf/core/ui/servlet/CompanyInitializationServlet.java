
package com.biperf.core.ui.servlet;

import static com.biperf.core.utils.Environment.ENV_DEV;
import static com.biperf.core.utils.Environment.getEnvironment;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.company.Company;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.company.CompanyService;
import com.biperf.core.service.rosterproxy.RosterProxyRepository;
import com.biperf.core.service.rosterproxy.V1.RosterProxyView;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UrlReader;
import com.biperf.core.value.companysetup.v1.company.CompanyView;

@SuppressWarnings( "serial" )
public class CompanyInitializationServlet extends CMSAwareBaseServlet
{
  private static Log log = LogFactory.getLog( CompanyInitializationServlet.class );

  @Override
  public void init() throws ServletException
  {
    try
    {
      super.init();
      // DM Datatransporter need day maker instance context name for constructing roleARN for
      // consuming messages from Kinesis
      PropertySetItem dmContext = getSystemVariableService().getPropertyByName( SystemVariableService.DM_CONTEXT_NAME );
      if ( !isDevEnv() && Objects.nonNull( dmContext ) && dmContext.getStringVal().equalsIgnoreCase( "CHANGE ME!" ) )
      {
        updateContextName( dmContext );
      }
      else
      {
        log.info( "############ DM Context Name system variable already updated. ############" );
      }

    }
    catch( Exception e )
    {
      log.error( e.getMessage(), e );
    }

    try
    {
      String meshSecretKey = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.NACKLE_MESH_SERVICES_SECRET_KEY ).getStringVal();
      String meshClientId = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.NACKLE_MESH_SERVICES_CLIENT_ID ).getStringVal();

      if ( !isDevEnv() && Objects.nonNull( meshSecretKey ) && !meshSecretKey.trim().equalsIgnoreCase( "CHANGE ME!" ) && Objects.nonNull( meshClientId )
          && !meshClientId.trim().equalsIgnoreCase( "CHANGE ME!" ) )
      {
        initCompany();
        doRosterProxySetUp();
      }
      else
      {
        log.info( "############ Please Check Nackle Mesh Services Credentials. ############" );
      }

    }
    catch( Exception e )
    {
      e.printStackTrace();
      log.error( e.getMessage(), e );
    }

  }

  /**
   * Get Company Information from Company Setup and insert Company Table
   */
  private void initCompany()
  {

    try
    {
      SystemVariableService systemVariableService = getSystemVariableService();
      CompanyService companyService = getCompanyService();

      String companyIdentifier = systemVariableService.getContextName();
      Company company = companyService.getCompanyDetail();

      if ( company == null )
      {
        CompanyView companyView = companyService.getCompanyFromCompanySetup( companyIdentifier );
        if ( companyView == null )
        {
          log.info( "Could not get company information from Company Setup, Please ensure company is setup in Company Setup Admin UI and identifier same context name" );
        }
        else
        {
          company = new Company();

          company.setCompanyId( UUID.fromString( companyView.getId() ) );
          company.setCompanyEmail( companyView.getContactEmailAddress() );
          company.setCompanyName( companyView.getName() );
          company.setCompanyIdentifier( companyIdentifier );
          Company inCompany = companyService.saveCompany( company );

          if ( inCompany != null )
          {
            log.info( "Company Information setup in Company Table" );
          }
        }
      }
      else
      {
        log.info( "Company was alraedy configured.........." );
      }

    }
    catch( Exception exception )
    {
      log.error( "Exception in Company Setup", exception );
      exception.printStackTrace();
    }
  }

  private void doRosterProxySetUp()
  {
    try
    {
      PropertySetItem clinetIdProp = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.JWT_ISSUER_ROSTER );
      PropertySetItem keyProp = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.JWT_SECRET_ROSTER );

      if ( StringUtils.isEmpty( clinetIdProp.getStringVal() ) || clinetIdProp.getStringVal().trim().equalsIgnoreCase( "CHANGE ME!" ) || StringUtils.isEmpty( keyProp.getStringVal() )
          || keyProp.getStringVal().trim().equalsIgnoreCase( "CHANGE ME!" ) || clinetIdProp.getStringVal().trim().equalsIgnoreCase( "CHANGE ME" )
          || keyProp.getStringVal().trim().equalsIgnoreCase( "CHANGE ME" ) )
      {
        RosterProxyView view = getRosterProxyRepository().resideRosterProxyInfo();

        if ( Objects.nonNull( view ) )
        {
          if ( view.getResponseCode() == 200 )
          {

            clinetIdProp.setStringVal( getSystemVariableService().getContextName() );
            clinetIdProp.getAuditUpdateInfo().setDateModified( new Timestamp( System.currentTimeMillis() ) );

            keyProp.setStringVal( view.getDeveloperMessage() );
            keyProp.getAuditUpdateInfo().setDateModified( new Timestamp( System.currentTimeMillis() ) );

            getSystemVariableService().saveProperty( clinetIdProp );
            getSystemVariableService().saveProperty( keyProp );

            log.info( "************** Roster Proxy Updated Successfully !!!!!!!!!!!. **************" );

          }
          else
          {
            log.info( "########## Roster Proxy Setup Failed, Check Service Exception ##########" );
          }
        }
        else
        {
          log.info( "########## Roster Proxy Setup Failed, Check Service Exception ##########" );
        }

      }

    }
    catch( Exception exception )
    {
      log.error( "Exception in Roster Proxy Setup", exception );
      exception.printStackTrace();
    }
  }

  private void updateContextName( PropertySetItem dmContext ) throws ServiceErrorException
  {
    if ( Objects.isNull( dmContext.getStringVal() ) || dmContext.getStringVal().equalsIgnoreCase( "CHANGE ME!" ) )
    {
      dmContext.setStringVal( getSystemVariableService().getContextName() );
      dmContext.getAuditUpdateInfo().setDateModified( new Timestamp( System.currentTimeMillis() ) );
      getSystemVariableService().saveProperty( dmContext );
    }
  }

  private boolean isDevEnv()
  {
    try
    {
      String envUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

      if ( ENV_DEV.equalsIgnoreCase( getEnvironment() ) || !new UrlReader().useProxy( envUrl ) )
      {
        return true;
      }
    }
    catch( Exception e )
    {
      return false;
    }

    return false;
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  private CompanyService getCompanyService()
  {
    return (CompanyService)BeanLocator.getBean( CompanyService.BEAN_NAME );
  }

  private RosterProxyRepository getRosterProxyRepository()
  {
    return (RosterProxyRepository)BeanLocator.getBean( RosterProxyRepository.BEAN_NAME );
  }
}
