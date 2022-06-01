
package com.biperf.core.service.home;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.homepage.CrossPromotionalModuleApp;
import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.homepage.ModuleApp;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.service.home.strategy.ModuleAppAudienceStrategyFactory;

public interface FilterAppSetupService extends SAO
{
  /**
   * Static final for the BEAN_NAME for use in the applicationContext.xml
   */
  public static final String BEAN_NAME = "filterAppSetupService";

  public HomePageFilterHolder getHomePageModulesForUser( Participant participant, Map<String, Object> parameterMap );

  public List<FilterAppSetup> getFilterAppSetupTemplate();

  public FilterAppSetup getFilterAppSetupById( Long filterAppSetupId );

  public List<FilterAppSetup> getAllFilterAppSetup();

  public List<FilterAppSetup> getAllFilterAppSetupForUser( Participant participant, Map<String, Object> parameterMap );

  public FilterAppSetup save( FilterAppSetup filterAppSetup );

  public void delete( FilterAppSetup filterAppSetup );

  public ModuleApp getModuleAppById( Long moduleAppId );

  public List<ModuleApp> getAllAudienceSpecificTiles();

  public ModuleApp save( ModuleApp moduleApp ) throws ServiceErrorException;

  public void delete( ModuleApp moduleApp );

  public List getAudienceforModuleApp( Long moduleAppId );

  public List<ModuleApp> getAllModuleAppByPriorityOne();

  public List<ModuleApp> getAllModuleApps();

  public List<ModuleApp> getAllModuleAppByPriorityTwo();

  public List<FilterAppSetup> getFilterAppSetupByFilterTypeCode( String code );

  public void clearFilterAppSetupCache();

  public List<ModuleApp> getHomePageModulesForDelegate( Participant participant, Map<String, Object> parameterMap );

  public Map<String, Object> getToolkitOptions( Participant participant, boolean isCompleteCheck );

  public List<CrossPromotionalModuleApp> getCrossPromotionalModuleApps();

  public ModuleAppAudienceStrategyFactory getModuleAppAudienceStrategyFactory();

  public List<ModuleApp> getModuleAppByTileMappingType( String tileMappingType );

  public void updateMEPlusFilterPageSetup( boolean stringVal );

  public List<ModuleApp> getAllAvailableModuleApps();

  public void updateRecognitionOnlyFilterPageSetup( boolean svValue );

  public ModuleApp getModuleByAppName( String appName );

  public boolean isFilterEnabled( HttpServletRequest httpRequest, String tileMappingType );

  public boolean isUserInDIYAudience( UserDIYParams parms );

  public void updateSalesMakerFilterPageSetup( boolean stringVal );

}
