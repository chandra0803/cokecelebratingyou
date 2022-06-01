
package com.biperf.core.utils.fedresources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.biperf.core.domain.enums.ModuleType;
import com.biperf.core.service.goalquest.GoalQuestService;
import com.biperf.core.service.home.FilterAppSetupService;
import com.biperf.core.service.home.UserDIYParams;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;

/*
 * NOTE: if we start to get a bunch more of these special cases like report and DIY communications,
 * this should be changed and each type converted to a strategy.
 */
public class FEDResourceLocatorFactoryImpl implements FEDResourceLocatorFactory
{
  private static final Log log = LogFactory.getLog( FEDResourceLocatorFactoryImpl.class );
  public static final String SESSION_VARIABLE_ELIGIBLE_PROMOTIONS = "eligiblePromotions";

  private MainContentService mainContentService;
  private AuthorizationService authorizationService;
  private FilterAppSetupService filterAppSetupService;
  private GoalQuestService goalQuestService;
  private ParticipantService participantService;

  @SuppressWarnings( "unchecked" )
  @Override
  public UserFEDResources getUserFEDResources( HttpServletRequest httpRequest )
  {
    Object attribute = httpRequest.getSession().getAttribute( SESSION_VARIABLE_ELIGIBLE_PROMOTIONS );

    if ( Objects.isNull( attribute ) )
    {
      return getUserFEDResourcesByAuthenticatedUser();
    }
    else
    {
      List<PromotionMenuBean> eligiblePromotions = (List<PromotionMenuBean>)attribute;
      return buildRequiredUserFEDResources( eligiblePromotions );
    }
  }

  @Override
  public UserFEDResources getUserFEDResourcesByAuthenticatedUser()
  {
    List<PromotionMenuBean> promotionMenuBeanList = mainContentService.buildEligiblePromoList( UserManager.getUser() );// NOTE
                                                                                                                       // -
                                                                                                                       // do
                                                                                                                       // we
                                                                                                                       // need
                                                                                                                       // to
                                                                                                                       // figure
                                                                                                                       // this
                                                                                                                       // out
                                                                                                                       // if
                                                                                                                       // a
                                                                                                                       // proxy
                                                                                                                       // or
                                                                                                                       // admin
                                                                                                                       // user?
    return buildRequiredUserFEDResources( promotionMenuBeanList );
  }

  private UserFEDResources buildRequiredUserFEDResources( List<PromotionMenuBean> eligiblePromotions )
  {
    UserFEDResources userResources = new UserFEDResources();

    if ( !CollectionUtils.isEmpty( eligiblePromotions ) )
    {
      List<ModuleType> userEligibleModuleTypes = new ArrayList<ModuleType>();

      eligiblePromotions.forEach( menuBean ->
      {
        if ( isValidFEDResourceForUser( menuBean ) )
        {
          ModuleType moduleType = ModuleType.getModuleTypeByPromotionTypeCode( menuBean.getPromotion().getPromotionType().getCode() );
          if ( !userEligibleModuleTypes.contains( moduleType ) )
          {
            userEligibleModuleTypes.add( moduleType );
          }
        }
      } );
      if ( !userEligibleModuleTypes.contains( ModuleType.GOALQUEST )
          && Objects.nonNull( getGoalQuestService().getHoneycombProgramDetails( getParticipantService().getParticipantById( UserManager.getUser().getUserId() ) ) ) )
      {
        ModuleType moduleType = ModuleType.GOALQUEST;
        userEligibleModuleTypes.add( moduleType );

      }
      buildEnabledResources( userResources, userEligibleModuleTypes );
    }
    // check for reports
    if ( isReportsIncluded() )
    {
      userResources.setReportsEnabled( true );
    }
    // check for DIY Communications
    if ( isDIYCommunicationIncluded() )
    {
      userResources.setDiyCommunicationsEnabled( true );
    }

    return userResources;
  }

  private void buildEnabledResources( UserFEDResources userResources, List<ModuleType> userEligibleModuleTypes )
  {
    for ( ModuleType type : userEligibleModuleTypes )
    {
      switch ( type )
      {
        case PRODUCTCLAIMS:
          userResources.setProductClaimsEnabled( true );
          break;
        case ENGAGEMENT:
          userResources.setEngagementEnabled( true );
          break;
        case GOALQUEST:
          userResources.setGoalquestEnabled( true );
          break;
        case DIY_QUIZ:
          userResources.setDiyQuizesEnabled( true );
          break;
        case INSTANT_POLL:
          userResources.setInstantPollsEnabled( true );
          break;
        case NOMINATIONS:
          userResources.setNominationsEnabled( true );
          break;
        case QUIZES:
          userResources.setQuizesEnabled( true );
          break;
        case RECOGNITION:
          userResources.setRecognitionsEnabled( true );
          break;
        case SSI:
          userResources.setSsiEnabled( true );
          break;
        case SURVEY:
          userResources.setSurveysEnabled( true );
          break;
        case THROWDOWN:
          userResources.setThrowdownEnabled( true );
          break;
        default:
          log.debug( "Undefined Module passed" );
          break;
      }
    }
  }

  private boolean isValidFEDResourceForUser( PromotionMenuBean menuBean )
  {
    if ( menuBean.isCanReceive() || menuBean.isCanSubmit() || menuBean.isViewTile() )
    {
      return true;
    }
    return true;
  }

  private boolean isReportsIncluded()
  {
    Set<String> includeAny = StringUtils.commaDelimitedListToSet( "MANAGER,PROCESS_TEAM,BI_ADMIN,PROJ_MGR,VIEW_REPORTS" );
    return authorizationService.isUserInRole( Collections.EMPTY_SET, includeAny, Collections.EMPTY_SET );
  }

  private boolean isDIYCommunicationIncluded()
  {
    UserDIYParams parms = new UserDIYParams( UserManager.getUserId(), UserManager.getUser().isManager(), UserManager.getUser().isOwner(), true );
    return filterAppSetupService.isUserInDIYAudience( parms );
  }

  public void setMainContentService( MainContentService mainContentService )
  {
    this.mainContentService = mainContentService;
  }

  public void setAuthorizationService( AuthorizationService authorizationService )
  {
    this.authorizationService = authorizationService;
  }

  public void setFilterAppSetupService( FilterAppSetupService filterAppSetupService )
  {
    this.filterAppSetupService = filterAppSetupService;
  }

  public GoalQuestService getGoalQuestService()
  {
    return goalQuestService;
  }

  public void setGoalQuestService( GoalQuestService goalQuestService )
  {
    this.goalQuestService = goalQuestService;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

}
