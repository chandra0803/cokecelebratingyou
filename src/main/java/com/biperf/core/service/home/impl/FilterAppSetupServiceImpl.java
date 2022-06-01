
package com.biperf.core.service.home.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.cache.Cache;
import com.biperf.cache.CacheFactory;
import com.biperf.core.dao.homepage.FilterAppSetupDAO;
import com.biperf.core.dao.homepage.ModuleAppDAO;
import com.biperf.core.domain.enums.FilterSetupType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.ProxyCoreAccessType;
import com.biperf.core.domain.enums.TileMappingType;
import com.biperf.core.domain.homepage.CrossPromotionalModuleApp;
import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.homepage.ModuleApp;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.proxy.Proxy;
import com.biperf.core.domain.proxy.ProxyModule;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.home.FilterAppSetupService;
import com.biperf.core.service.home.HomePageFilterHolder;
import com.biperf.core.service.home.ModuleAppFilterMap;
import com.biperf.core.service.home.ModuleDisplayMapping;
import com.biperf.core.service.home.UserDIYParams;
import com.biperf.core.service.home.strategy.ModuleAppAudienceStrategy;
import com.biperf.core.service.home.strategy.ModuleAppAudienceStrategyFactory;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.proxy.ProxyAssociationRequest;
import com.biperf.core.service.proxy.ProxyService;
import com.biperf.core.service.ssi.SSIPromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.threads.CallableFactory;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.util.StringUtils;

public class FilterAppSetupServiceImpl implements FilterAppSetupService
{
  private static final Log logger = LogFactory.getLog( FilterAppSetupServiceImpl.class );

  private static final String CACHE_FILTER_APP_SETUP_TEMPLATE = "filterAppSetupTemplate";
  private static final String HIDDEN = "hidden";

  private SSIPromotionService ssiPromotionService;
  private Cache filterAppSetupTemplate;
  private ModuleAppDAO moduleAppDAO;
  private FilterAppSetupDAO filterAppSetupDAO;
  private AudienceService audienceService;
  private SystemVariableService systemVariableService;
  private ProxyService proxyService;
  private ModuleAppAudienceStrategyFactory moduleAppAudienceStrategyFactory;
  private ExecutorService executorService;
  private UserService userService;
  private PromotionService promotionService;

  public PromotionService getPromotionService()
  {
    return promotionService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public ModuleAppAudienceStrategyFactory getModuleAppAudienceStrategyFactory()
  {
    return moduleAppAudienceStrategyFactory;
  }

  public void setModuleAppAudienceStrategyFactory( ModuleAppAudienceStrategyFactory moduleAppAudienceStrategyFactory )
  {
    this.moduleAppAudienceStrategyFactory = moduleAppAudienceStrategyFactory;
  }

  public AudienceService getAudienceService()
  {
    return audienceService;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public SSIPromotionService getSsiPromotionService()
  {
    return ssiPromotionService;
  }

  public void setSsiPromotionService( SSIPromotionService ssiPromotionService )
  {
    this.ssiPromotionService = ssiPromotionService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public UserService getUserService()
  {
    return userService;
  }

  @Override
  public FilterAppSetup getFilterAppSetupById( Long filterAppSetupId )
  {
    return filterAppSetupDAO.getFilterAppSetupById( filterAppSetupId );
  }

  @Override
  public List<FilterAppSetup> getAllFilterAppSetup()
  {
    return filterAppSetupDAO.getAll();
  }

  @Override
  public FilterAppSetup save( FilterAppSetup filterAppSetup )
  {
    filterAppSetupDAO.save( filterAppSetup );
    clearFilterAppSetupCache();
    return filterAppSetup;
  }

  @Override
  public void clearFilterAppSetupCache()
  {
    filterAppSetupTemplate.remove( CACHE_FILTER_APP_SETUP_TEMPLATE );
  }

  @Override
  public void delete( FilterAppSetup filterAppSetup )
  {
    filterAppSetupDAO.delete( filterAppSetup );
    clearFilterAppSetupCache();
  }

  @Override
  public ModuleApp getModuleAppById( Long moduleAppId )
  {
    return moduleAppDAO.getModuleAppById( moduleAppId );
  }

  @Override
  public List<ModuleApp> getAllModuleApps()
  {
    return moduleAppDAO.getAllForAdminSetup();
  }

  @Override
  public List<ModuleApp> getAllAvailableModuleApps()
  {
    return moduleAppDAO.getAllAvailableModuleApps();
  }

  @Override
  public List<ModuleApp> getAllAudienceSpecificTiles()
  {
    return moduleAppDAO.getAllAudienceSpecificTiles();
  }

  @Override
  public ModuleApp save( ModuleApp moduleApp ) throws ServiceErrorException
  {
    moduleAppDAO.save( moduleApp );
    clearFilterAppSetupCache();
    return moduleApp;
  }

  @Override
  public void delete( ModuleApp moduleApp )
  {
    moduleAppDAO.delete( moduleApp );
    clearFilterAppSetupCache();
  }

  public ModuleAppDAO getModuleAppDAO()
  {
    return moduleAppDAO;
  }

  public void setModuleAppDAO( ModuleAppDAO moduleAppDAO )
  {
    this.moduleAppDAO = moduleAppDAO;
  }

  @SuppressWarnings( "unchecked" )
  public List<FilterAppSetup> getFilterAppSetupTemplate()
  {
    List<FilterAppSetup> filters = (List<FilterAppSetup>)filterAppSetupTemplate.get( CACHE_FILTER_APP_SETUP_TEMPLATE );
    if ( filters == null )
    {
      List<FilterAppSetup> allActiveFilters = new ArrayList<FilterAppSetup>();
      // remove inactive filters and ones with no audience(can't figure out a way to use a hibernate
      // criteria
      for ( FilterAppSetup filter : filterAppSetupDAO.getAllForHomePage() )
      {
        FilterSetupType type = filter.getFilterSetupType();
        ModuleApp module = filter.getModuleApp();
        // note, for whatever reason, the isActive flag is never getting set (default to false)
        if ( type.isActive() && !module.getAudienceType().isNoneAudienceType() )
        {
          allActiveFilters.add( filter );
        }
      }
      filterAppSetupTemplate.put( CACHE_FILTER_APP_SETUP_TEMPLATE, allActiveFilters );
      return allActiveFilters;
    }
    return filters;
  }

  @Override
  public HomePageFilterHolder getHomePageModulesForUser( Participant participant, Map<String, Object> parameterMap )
  {
    List<ModuleApp> modules = moduleAppDAO.getAllForHomePage();
    List<FilterAppSetup> filters = getAllFilterAppSetupForUser( participant, parameterMap );
    List<ModuleAppFilterMap> mappings = new ArrayList<ModuleAppFilterMap>();

    boolean isGiftCardOnly = false;
    if ( isGiftCodeOnlyPax() )
    {
      isGiftCardOnly = true;
    }

    for ( ModuleApp module : modules )
    {
      ModuleAppFilterMap moduleMap = new ModuleAppFilterMap();
      moduleMap.setModule( module );
      // gather the user assigned filters
      moduleMap.setUserFilterAppSetupList( filters );

      // assign FilterAppSetup mapping to the module
      for ( Object filterSetupType : FilterSetupType.getList() )
      {
        FilterSetupType type = (FilterSetupType)filterSetupType;
        FilterAppSetup assignedFilter = null;
        for ( FilterAppSetup filter : filters )
        {
          if ( filter.getModuleApp().getId().equals( module.getId() ) && filter.getFilterSetupType().getCode().equals( type.getCode() ) )
          {
            assignedFilter = filter;
            break;
          }
        }
        moduleMap.getFilterAppSetups().put( type, assignedFilter );
        assignedFilter = null;
      }
      mappings.add( moduleMap );
    }

    // ok, now fully populate, we should be able to assign the preferred sizes
    HomePageFilterHolder filterHolder = new HomePageFilterHolder();
    filterHolder.setModuleAppFilterMap( mappings );
    // get all the filters for the user per filter and apply the rules
    filterHolder.buildAllDimensions();

    // filter out the hidden stuff..
    List<ModuleAppFilterMap> filteredModuleList = new ArrayList<ModuleAppFilterMap>();

    for ( ModuleAppFilterMap moduleApp : filterHolder.getModuleAppFilterMap() )
    {
      boolean displayModuleApp = false;
      for ( ModuleDisplayMapping displayMapping : moduleApp.getModuleDisplay().getModuleDisplayMappings() )
      {
        if ( !HIDDEN.equals( displayMapping.getOrder() ) )
        {
          displayModuleApp = true;
          break;
        }
      }
      if ( displayModuleApp )
      {
        if ( moduleApp.getModule().getTileMappingType() != null )
        {
          filteredModuleList.add( moduleApp );
        }
      }
    }
    filterHolder.setModuleAppFilterMap( filteredModuleList );

    return filterHolder;
  }

  private boolean isGiftCodeOnlyPax()
  {
    return UserManager.getUser().isParticipant() && UserManager.getUser().isGiftCodeOnlyPax();
  }

  public List<ModuleApp> getHomePageModulesForDelegate( Participant participant, Map<String, Object> parameterMap )
  {
    List<ModuleApp> modules = moduleAppDAO.getAllForDelegateHomePage();
    List<ModuleApp> userFilters = null;
    List<ModuleAppFilterMap> mappings = new ArrayList<ModuleAppFilterMap>();
    userFilters = new ArrayList<ModuleApp>();
    Proxy proxy = null;
    AssociationRequestCollection proxyAssociationRequestCollection = new AssociationRequestCollection();
    proxyAssociationRequestCollection.add( new ProxyAssociationRequest( ProxyAssociationRequest.PROXY_MODULE ) );
    proxy = getProxyService().getProxyByUserAndProxyUserWithAssociations( participant.getId(), UserManager.getUser().getOriginalAuthenticatedUser().getUserId(), proxyAssociationRequestCollection );
    for ( ModuleApp module : modules )
    {
      ModuleAppFilterMap moduleMap = new ModuleAppFilterMap();
      moduleMap.setModule( module );
      if ( isUserInAudienceDelegate( participant, module, parameterMap ) )
      {
        if ( isModuleAllowedForDelegate( participant, module, parameterMap, proxy ) )
        {
          userFilters.add( module );
        }
      }
      mappings.add( moduleMap );
    }

    return userFilters;
  }

  /**
   * This method will check to see if we need to display the tool kit tile or not and what links in it.
   * isCompleteCheck - This flag will check whether to check all the conditions or not.
   * If the flag is false we will check the resource center and see if there is any active content. If so we 
   * don't need to check any other conditions and can skip the other calls and display the tile.
   * For ManagerToolkitModuleAppAudienceStrategy all we care is whether to display the toolkit or not. to avoid
   * the performance overhead we added the isCompleteCheck flag.
   * For ManagerToolKitController we need to check if tool kit is available and if so what links need to be displayed
   * and hence the flag is true.
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> getToolkitOptions( Participant participant, boolean isCompleteCheck )
  {
    Map<String, Object> options = new HashMap<>();
    AuthenticatedUser user = UserManager.getUser();

    boolean showToolkit = false;

    boolean displayLeaderBoard = getSystemVariableService().getPropertyByName( SystemVariableService.LEADERBOARD_SHOW_HIDE ).getBooleanVal();
    boolean displayManageContests = false;

    if ( systemVariableService.getPropertyByName( SystemVariableService.INSTALL_SSI ).getBooleanVal() )
    {
      displayManageContests = getSsiPromotionService().isPaxInContestCreatorAudience( user.getUserId() );
    }

    boolean budgetTransferEnabled = systemVariableService.getPropertyByName( SystemVariableService.BUDGET_TRANSFER_SHOW ).getBooleanVal();
    boolean hasEligibleBudgets = false;
    if ( budgetTransferEnabled )
    {
      hasEligibleBudgets = !getPromotionService().getEligibleBudgetTransfer( UserManager.getUser() ).isEmpty();
    }
    boolean displayBudgetTransfer = budgetTransferEnabled && hasEligibleBudgets;

    boolean displayRosterMgmt = systemVariableService.getPropertyByName( SystemVariableService.ROSTER_MGMT_AVAILABLE ).getBooleanVal();
    boolean displayManagerSendAlert = systemVariableService.getPropertyByName( SystemVariableService.MANAGER_SEND_ALERT ).getBooleanVal();

    boolean displayManageQuizzes = false;
    if ( ! ( systemVariableService.getPropertyByName( SystemVariableService.SALES_MAKER ).getBooleanVal() ) )
    {
      displayManageQuizzes = getPromotionService().isParticipantInDIYPromotionAudience( participant );
    }

    if ( user.isDelegate() )
    {
      Proxy proxy = null;
      AssociationRequestCollection proxyAssociationRequestCollection = new AssociationRequestCollection();
      proxyAssociationRequestCollection.add( new ProxyAssociationRequest( ProxyAssociationRequest.PROXY_MODULE ) );
      proxy = getProxyService().getProxyByUserAndProxyUserWithAssociations( participant.getId(), UserManager.getUser().getOriginalAuthenticatedUser().getUserId(), proxyAssociationRequestCollection );

      if ( proxy == null )
      {
        proxy = new Proxy();
      }
      if ( budgetTransferEnabled )
      {
        if ( proxy.getCoreAccess() != null && proxy.getCoreAccess().contains( ProxyCoreAccessType.BUDGET_TRANSFER ) )
        {
          displayBudgetTransfer = true;
        }
        else
        {
          displayBudgetTransfer = false;
        }
      }
      if ( displayRosterMgmt )
      {
        if ( proxy.getCoreAccess() != null && proxy.getCoreAccess().contains( ProxyCoreAccessType.ROSTER_MGMT ) )
        {
          displayRosterMgmt = true;
        }
        else
        {
          displayRosterMgmt = false;
        }
      }
      if ( displayManagerSendAlert )
      {
        if ( proxy.getCoreAccess() != null && proxy.getCoreAccess().contains( ProxyCoreAccessType.SEND_ALERT ) )
        {
          displayManagerSendAlert = true;
        }
        else
        {
          displayManagerSendAlert = false;
        }
      }

      if ( proxy.isAllowLeaderboard() && ( participant.isManager() || participant.isOwner() ) )
      {
        displayLeaderBoard = true;
      }
      else
      {
        displayLeaderBoard = false;
      }

      displayManageQuizzes = false;
      displayManageContests = false;
    }

    boolean isUserInDIYAudience = false;
    if ( ! ( systemVariableService.getPropertyByName( SystemVariableService.SALES_MAKER ).getBooleanVal() ) )
    {
      isUserInDIYAudience = isUserInDIYAudience( participant, displayManagerSendAlert );
    }

    displayLeaderBoard = participant.isManager() || participant.isOwner() ? displayLeaderBoard : false;
    if ( !user.isDelegate() )
    {
      displayBudgetTransfer = participant.isManager() || participant.isOwner() || participant.isParticipant() ? displayBudgetTransfer : false;
    }
    displayRosterMgmt = participant.isManager() || participant.isOwner() ? displayRosterMgmt : false;

    showToolkit = displayLeaderBoard || displayManageContests || displayBudgetTransfer || displayRosterMgmt || displayManageQuizzes || isUserInDIYAudience ? Boolean.TRUE : false;

    options.put( "displayManageContests", displayManageContests );
    options.put( "displayLeaderBoard", displayLeaderBoard );
    options.put( "displayBudgetTransfer", displayBudgetTransfer );
    options.put( "displayRosterMgmt", displayRosterMgmt );
    options.put( "displayManageQuizzes", displayManageQuizzes );
    options.put( "showDIYCommunication", isUserInDIYAudience );
    options.put( "showToolkit", showToolkit );
    return options;
  }

  private boolean isUserInDIYAudience( Participant pax, boolean displayManagerSendAlert )
  {
    UserDIYParams parms = new UserDIYParams( pax.getId(), pax.isManager(), pax.isOwner(), displayManagerSendAlert );
    return isUserInDIYAudience( parms );
  }

  public boolean isUserInDIYAudience( UserDIYParams parms )
  {
    boolean isUserInDIYAudience = false;
    String[] audienceRoleCode = { "DIY_BANNER_ADMIN", "DIY_NEWS_ADMIN", "DIY_RESOURCE_ADMIN", "DIY_TIPS_ADMIN" };
    for ( int i = 0; i < audienceRoleCode.length; i++ )
    {
      String roleCode = null;
      roleCode = audienceRoleCode[i];
      // check user exist in any type of diy communication audience
      isUserInDIYAudience = getAudienceService().isUserInDIYCommAudience( parms.getUserId(), roleCode );
      if ( isUserInDIYAudience )
      {
        break;
      }
    }

    if ( !isUserInDIYAudience && ( parms.isManager() || parms.isOwner() ) && parms.isDisplayManagerSendAlert() )
    {
      isUserInDIYAudience = true;
    }
    return isUserInDIYAudience;
  }

  @Override
  public List<FilterAppSetup> getAllFilterAppSetupForUser( Participant participant, Map<String, Object> parameterMap )
  {
    boolean threadedEnabled = systemVariableService.getPropertyByName( "homepage.filters.strategy.threaded" ).getBooleanVal();
    List<FilterAppSetup> userFilters = null;
    // get Template
    List<FilterAppSetup> filters = getFilterAppSetupTemplate();
    // filters the user can see
    if ( threadedEnabled ) // multi-threaded
    {
      userFilters = buildCallableArrayAndExecute( participant, parameterMap, filters );
    }
    else // single-threaded
    {
      userFilters = new ArrayList<FilterAppSetup>();
      Map<Long, FilterAppSetup> validModuleAudiences = buildDistinctFiltersModules( filters );
      for ( FilterAppSetup filter : validModuleAudiences.values() )
      {
        if ( isUserInAudience( participant, filter, parameterMap ) )
        {
          userFilters.add( filter );
        }
      }
      List<FilterAppSetup> otherFilters = new ArrayList<FilterAppSetup>();
      // now assign the rest based on the mappings
      for ( final FilterAppSetup filter : filters )
      {
        // ok - we don't need to re-eval the same strategy - just add those
        // filters that have the same one if it exists
        for ( FilterAppSetup userFilter : userFilters )
        {
          if ( filter.getModuleApp().getId().equals( userFilter.getModuleApp().getId() ) )
          {
            otherFilters.add( filter );
            break;
          }
        }
      }
      userFilters.addAll( otherFilters );
    }
    return userFilters;
  }

  // OK, lets make sure that each strategy is ONLY run ONCE. We can then
  // re-use that result with other Filters that have that strategy, otherwise we'll
  // be re-running the same operation multiple times.
  private Map<Long, FilterAppSetup> buildDistinctFiltersModules( List<FilterAppSetup> filters )
  {
    Map<Long, FilterAppSetup> distinctModules = new HashMap<Long, FilterAppSetup>();
    for ( final FilterAppSetup filter : filters )
    {
      if ( !distinctModules.containsKey( filter.getModuleApp().getId() ) )
      {
        // ok, a new Module, add it to the list
        distinctModules.put( filter.getModuleApp().getId(), filter );
      }
    }
    return distinctModules;
  }

  private List<FilterAppSetup> buildCallableArrayAndExecute( final Participant participant, final Map<String, Object> parameterMap, final List<FilterAppSetup> filters )
  {
    // User's selection
    List<FilterAppSetup> userFilters = new ArrayList<FilterAppSetup>();
    // divide and conquer
    List<Callable<FilterAppSetup>> callables = new ArrayList<Callable<FilterAppSetup>>();

    Map<Long, FilterAppSetup> distinctModules = buildDistinctFiltersModules( filters );

    for ( final FilterAppSetup filter : distinctModules.values() )
    {
      callables.add( CallableFactory.createCallable( new Callable<FilterAppSetup>()
      {
        public FilterAppSetup call() throws Exception
        {
          if ( isUserInAudience( participant, filter, parameterMap ) )
          {
            return filter;
          }
          return null;
        }
      } ) );
    }
    try
    {
      List<Future<FilterAppSetup>> threadResults = executorService.invokeAll( callables );
      List<Long> validModuleAudiences = new ArrayList<Long>();
      for ( Future<FilterAppSetup> future : threadResults )
      {
        FilterAppSetup filterAppSetup = future.get();
        if ( null != filterAppSetup )
        {
          validModuleAudiences.add( filterAppSetup.getModuleApp().getId() );
        }
      }
      // ok - reassign the other filters that have the distinct ModuleAudienceStrategy assigend
      for ( final FilterAppSetup filter : filters )
      {
        if ( validModuleAudiences.contains( filter.getModuleApp().getId() ) )
        {
          userFilters.add( filter );
        }
      }
    }
    catch( Throwable t )
    {
      logger.error( t.getMessage(), t );
    }
    return userFilters;
  }

  private boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    ModuleAppAudienceStrategy strategy = moduleAppAudienceStrategyFactory.getModuleAppAudienceStrategy( filter );
    return strategy.isUserInAudience( participant, filter, parameterMap );
  }

  private boolean isUserInAudienceDelegate( Participant participant, ModuleApp module, Map<String, Object> parameterMap )
  {
    ModuleAppAudienceStrategy strategy = moduleAppAudienceStrategyFactory.getModuleAppAudienceStrategyDelegate( module );
    return strategy.isUserInAudience( participant, null, parameterMap );
  }

  private boolean isModuleAllowedForDelegate( Participant participant, ModuleApp module, Map<String, Object> parameterMap, Proxy proxy )
  {
    if ( proxy == null ) // NPE
    {
      proxy = new Proxy();
    }

    if ( proxy.isAllowLeaderboard() && module.getTileMappingType().getCode().equalsIgnoreCase( TileMappingType.LEADER_BOARD ) )
    {
      return true;
    }

    if ( module.getTileMappingType().getCode().equalsIgnoreCase( TileMappingType.MANAGER_TOOL_KIT ) )
    {
      Map<String, Object> options = getToolkitOptions( participant, false );
      if ( !StringUtils.isEmpty( proxy.getCoreAccess() ) || proxy.isAllowLeaderboard() || (boolean)options.get( "showToolkit" ) )
      {
        return true;
      }
    }

    if ( proxy.isAllModules() )
    {
      return true;
    }
    else
    {
      boolean atleastOneRecPromotion = false;
      boolean atleastOneNomPromotion = false;
      boolean atleastOnePCPromotion = false;
      List<PromotionMenuBean> eligiblePromotions = (List<PromotionMenuBean>)parameterMap.get( "eligiblePromotions" );
      if ( eligiblePromotions != null && eligiblePromotions.size() > 0 )
      {
        for ( PromotionMenuBean promoMenu : eligiblePromotions )
        {
          if ( promoMenu.getPromotion().isRecognitionPromotion() )
          {
            atleastOneRecPromotion = true;
            break;
          }
        }
        for ( PromotionMenuBean promoMenu : eligiblePromotions )
        {
          if ( promoMenu.getPromotion().isNominationPromotion() )
          {
            atleastOneNomPromotion = true;
            break;
          }
        }
        for ( PromotionMenuBean promoMenu : eligiblePromotions )
        {
          if ( promoMenu.getPromotion().isProductClaimPromotion() )
          {
            atleastOnePCPromotion = true;
            break;
          }
        }
      }
      Set<ProxyModule> allowedModules = proxy.getProxyModules();
      for ( ProxyModule allowedModule : allowedModules )
      {
        if ( allowedModule.getPromotionType() != null )
        {
          if ( allowedModule.getPromotionType().getCode().equalsIgnoreCase( PromotionType.NOMINATION ) && module.getTileMappingType().getCode().equalsIgnoreCase( TileMappingType.SEARCH )
              && atleastOneNomPromotion )
          {
            return true;
          }
          else if ( allowedModule.getPromotionType().getCode().equalsIgnoreCase( PromotionType.RECOGNITION ) && module.getTileMappingType().getCode().equalsIgnoreCase( TileMappingType.SEARCH )
              && atleastOneRecPromotion )
          {
            return true;
          }
          else if ( allowedModule.getPromotionType().getCode().equalsIgnoreCase( PromotionType.PRODUCT_CLAIM )
              && module.getTileMappingType().getCode().equalsIgnoreCase( TileMappingType.PRODUCT_CLAIM ) && atleastOnePCPromotion )
          {
            return true;
          }
        }
      }
    }
    return false;
  }

  public void setCacheFactory( CacheFactory cacheFactory )
  {
    filterAppSetupTemplate = cacheFactory.getCache( "filterAppSetupTemplate" );
  }

  public FilterAppSetupDAO getFilterAppSetupDAO()
  {
    return filterAppSetupDAO;
  }

  public void setFilterAppSetupDAO( FilterAppSetupDAO filterAppSetupDAO )
  {
    this.filterAppSetupDAO = filterAppSetupDAO;
  }

  @Override
  public List getAudienceforModuleApp( Long moduleAppId )
  {
    return moduleAppDAO.getAudienceforModuleApp( moduleAppId );
  }

  @Override
  public List<ModuleApp> getAllModuleAppByPriorityTwo()
  {
    return moduleAppDAO.getModuleByPriorityTwo();
  }

  @Override
  public List<FilterAppSetup> getFilterAppSetupByFilterTypeCode( String code )
  {
    return filterAppSetupDAO.getFilterAppSetupByFilterTypeCode( code );
  }

  @Override
  public List<ModuleApp> getAllModuleAppByPriorityOne()
  {
    return moduleAppDAO.getModuleByPriorityOne();
  }

  @Override
  public ModuleApp getModuleByAppName( String appName )
  {
    return moduleAppDAO.getModuleByAppName( appName );
  }

  public ExecutorService getExecutorService()
  {
    return executorService;
  }

  public void setExecutorService( ExecutorService executorService )
  {
    this.executorService = executorService;
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public ProxyService getProxyService()
  {
    return proxyService;
  }

  public void setProxyService( ProxyService proxyService )
  {
    this.proxyService = proxyService;
  }

  @Override
  public List<CrossPromotionalModuleApp> getCrossPromotionalModuleApps()
  {
    return moduleAppDAO.getCrossPromotionalModuleApps();
  }

  @Override
  public List<ModuleApp> getModuleAppByTileMappingType( String tileMappingType )
  {
    return moduleAppDAO.getModuleAppByTileMappingType( tileMappingType );
  }

  @Override
  public void updateMEPlusFilterPageSetup( boolean svValue )
  {
    moduleAppDAO.updateMEPlusFilterPageSetup( svValue );
  }

  public void updateRecognitionOnlyFilterPageSetup( boolean svValue )
  {
    moduleAppDAO.updateRecognitionOnlyFilterPageSetup( svValue );
  }

  public void updateSalesMakerFilterPageSetup( boolean svValue )
  {
    moduleAppDAO.updateSalesMakerFilterPageSetup( svValue );
  }

  @SuppressWarnings( "unchecked" )
  public boolean isFilterEnabled( HttpServletRequest httpRequest, String tileMappingType )
  {
    List<ModuleAppFilterMap> moduleAppFilters = (List<ModuleAppFilterMap>)httpRequest.getSession().getAttribute( "filteredModuleList" );
    if ( CollectionUtils.isNotEmpty( moduleAppFilters ) )
    {
      for ( ModuleAppFilterMap moduleApp : moduleAppFilters )
      {
        if ( moduleApp.getModule() != null && moduleApp.getModule().getTileMappingType() != null && moduleApp.getModule().getTileMappingType().getCode().equals( tileMappingType )
            && moduleApp.getModule().isActive() )
        {
          return true;
        }
      }
    }
    return false;
  }
}
