
package com.biperf.core.ui.pastwinner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.mtc.MTCVideo;
import com.biperf.core.domain.participant.NameIdBean;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.claim.NominationClaimService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.mtc.MTCVideoService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.nomination.EligibleNominationPromotionListViewObject;
import com.biperf.core.ui.nomination.EligibleNominationPromotionViewObject;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.CertUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.nomination.EligibleNominationPromotionValueObject;
import com.biperf.core.value.nomination.MyWinnersTabularDataColumnsViewBean;
import com.biperf.core.value.nomination.MyWinnersTabularDataMetaViewBean;
import com.biperf.core.value.nomination.MyWinnersTabularDataResultsViewBean;
import com.biperf.core.value.nomination.MyWinnersTabularDataViewBean;
import com.biperf.core.value.nomination.NominationApprovalsViewBean;
import com.biperf.core.value.nomination.NominationDetailInfoBadgesViewBean;
import com.biperf.core.value.nomination.NominationDetailInfoFieldsViewBean;
import com.biperf.core.value.nomination.NominationPastWinnersDetailViewBean;
import com.biperf.core.value.nomination.NominationPastWinnersViewBean;
import com.biperf.core.value.nomination.NominationWinnerModalDataViewBean;
import com.biperf.core.value.nomination.NominationWinnerModalDetailsViewBean;
import com.biperf.core.value.nomination.NominationWinnerModalDisplayDetaillsViewBean;
import com.biperf.core.value.nomination.NominationWinnerModalViewBean;
import com.biperf.core.value.nomination.NominationWinnersDetailViewBean;
import com.biperf.core.value.nomination.NominationsMyWinnersViewBean;
import com.biperf.core.value.nomination.NominatorDetailInfoViewBean;
import com.biperf.core.value.nomination.NominatorInfoViewBean;
import com.biperf.core.value.nomination.TeamListViewBean;
import com.biperf.core.value.nomination.WinnersInfoViewBean;
import com.biperf.core.value.pastwinners.NominationModalNomineeDetails;
import com.biperf.core.value.pastwinners.NominationMyWinners;
import com.biperf.core.value.pastwinners.PastWinnersActivityList;
import com.biperf.core.value.pastwinners.PastWinnersBehaviorDetails;
import com.biperf.core.value.pastwinners.PastWinnersCustomDetails;
import com.biperf.core.value.pastwinners.PastWinnersNominatorDetails;
import com.biperf.core.value.pastwinners.PastWinnersNominatorSummary;
import com.biperf.core.value.pastwinners.PastWinnersNomineeDetails;
import com.biperf.core.value.pastwinners.PastWinnersNomineeSummary;
import com.biperf.core.value.pastwinners.PastWinnersTeamMembersDetail;
import com.biperf.core.value.pastwinners.PastWinnersTeamMembersSummary;
import com.objectpartners.cms.util.CmsResourceBundle;

public class NominationPastWinnerAction extends BaseDispatchAction
{
  private static final int RESULTS_PER_PAGE = 30;
  private static String FIRST_NAME = "firstName";
  private static String LAST_NAME = "lastName";
  private static String COUNTRY = "countryId";
  private static String DEPARTMENT = "department";
  private static String TEAM_NAME = "teamName";

  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return display( mapping, actionForm, request, response );
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  public ActionForward nominationsWinnersList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    NominationPastWinnersForm pastWinnersForm = (NominationPastWinnersForm)form;

    Map<String, Object> parameters = new HashMap<>();
    String promotionId = null;
    if ( !StringUtils.isEmpty( pastWinnersForm.getNominationId() ) )
    {
      promotionId = pastWinnersForm.getNominationId();
    }

    parameters.put( "promotionId", promotionId );
    parameters.put( "participantId", UserManager.getUserId() );
    if ( !StringUtils.isEmpty( pastWinnersForm.getLastName() ) )
    {
      parameters.put( LAST_NAME, pastWinnersForm.getLastName() );
    }
    if ( !StringUtils.isEmpty( pastWinnersForm.getFirstName() ) )
    {
      parameters.put( FIRST_NAME, pastWinnersForm.getFirstName() );
    }
    if ( !StringUtils.isEmpty( pastWinnersForm.getCountry() ) )
    {
      parameters.put( COUNTRY, pastWinnersForm.getCountry() );
    }
    if ( !StringUtils.isEmpty( pastWinnersForm.getDepartment() ) )
    {
      parameters.put( DEPARTMENT, pastWinnersForm.getDepartment() );
    }
    if ( !StringUtils.isEmpty( pastWinnersForm.getTeamName() ) )
    {
      parameters.put( TEAM_NAME, pastWinnersForm.getTeamName() );
    }

    parameters.put( "startDate", DateUtils.toDate( pastWinnersForm.getStartDate() ) );
    parameters.put( "endDate", DateUtils.toDate( pastWinnersForm.getEndDate() ) );
    parameters.put( "rowNumStart", 0 );
    parameters.put( "rowNumEnd", RESULTS_PER_PAGE + 1 );
    parameters.put( "sortedBy", "ASC" );

    if ( pastWinnersForm.getLastName() != null && !pastWinnersForm.getLastName().isEmpty() )
    {
      parameters.put( "sortedOn", "LAST_NAME" );
    }
    else if ( pastWinnersForm.getFirstName() != null && !pastWinnersForm.getFirstName().isEmpty() )
    {
      parameters.put( "sortedOn", "FIRST_NAME" );
    }
    else
    {
      parameters.put( "sortedOn", "DATE_APPROVED" );
    }

    boolean fieldSearch = false;

    if ( pastWinnersForm.getLastName() != null || pastWinnersForm.getFirstName() != null || pastWinnersForm.getTeamName() != null || pastWinnersForm.getCountry() != null
        || pastWinnersForm.getDepartment() != null )
    {
      fieldSearch = true;
    }
    else if ( pastWinnersForm.getStartDate() != null && pastWinnersForm.getEndDate() != null )
    {
      fieldSearch = false;
    }

    Map<String, Object> output = getNominationClaimService().getNominationPastWinnersList( parameters );
    List<PastWinnersNomineeSummary> nomineeDetails = (List<PastWinnersNomineeSummary>)output.get( "p_out_nominee_dtl_res" );
    List<PastWinnersNominatorSummary> nominatorDetails = (List<PastWinnersNominatorSummary>)output.get( "p_out_nominator_res" );
    List<PastWinnersTeamMembersSummary> teamMembers = (List<PastWinnersTeamMembersSummary>)output.get( "p_out_team_memb_res" );
    List<PastWinnersActivityList> activityList = (List<PastWinnersActivityList>)output.get( "p_out_activity_list" );

    NominationPastWinnersViewBean nominationPastWinnersViewBean = new NominationPastWinnersViewBean();
    if ( nominatorDetails != null && nominatorDetails.size() > 0 )
    {
      List<NominationApprovalsViewBean> nominationApprovals = new ArrayList<>();
      for ( PastWinnersActivityList pastWinnersActivityBean : activityList )
      {
        Map<String, Object> urlParams = new HashMap<String, Object>();
        Long id;
        if ( pastWinnersActivityBean.getClaimGroupId() != null && pastWinnersActivityBean.getClaimGroupId() != 0 )
        {
          id = pastWinnersActivityBean.getClaimGroupId();
        }
        else if ( pastWinnersActivityBean.getTeamName() != null )
        {
          id = pastWinnersActivityBean.getTeamId();
        }
        else
        {
          id = pastWinnersActivityBean.getActivityId();
        }
        NominationApprovalsViewBean nominationApprovalsViewBean = new NominationApprovalsViewBean();
        nominationApprovalsViewBean.setActivityId( id );

        List<NominatorInfoViewBean> nominatorInfo = new ArrayList<NominatorInfoViewBean>();
        for ( PastWinnersNominatorSummary nominator : nominatorDetails )
        {
          Long idNominator;
          if ( nominator.getClaimGroupId() != null && nominator.getClaimGroupId() != 0 )
          {
            idNominator = nominator.getClaimGroupId();
          }
          else if ( nominator.getTeamName() != null )
          {
            idNominator = nominator.getTeamId();
          }
          else
          {
            idNominator = nominator.getActivityId();
          }
          if ( id.longValue() == idNominator.longValue() )
          {
            NominatorInfoViewBean nominatorInfoViewBean = new NominatorInfoViewBean();
            nominatorInfoViewBean.setNominatorId( nominator.getUserId() );
            nominatorInfoViewBean.setNominatorName( nominator.getName() );
            nominatorInfoViewBean.setCountryCode( nominator.getCountryCode() );
            nominatorInfoViewBean.setCountryName( nominator.getCountryName() );
            nominatorInfoViewBean.setNominatorOrg( nominator.getOrganisationName() );
            nominatorInfoViewBean.setTitle( nominator.getPosition() );
            nominatorInfoViewBean.setDepartmentName( nominator.getDepartmentName() );
            nominatorInfoViewBean.setCommentText( nominator.getCommentText() );
            nominatorInfo.add( nominatorInfoViewBean );
          }
        }
        nominationApprovalsViewBean.setNominatorInfo( nominatorInfo );

        List<WinnersInfoViewBean> winnersInfo = new ArrayList<WinnersInfoViewBean>();
        Map<String, Object> winnersTeam = new HashMap<String, Object>();
        for ( PastWinnersNomineeSummary nominee : nomineeDetails )
        {
          Long idNominee;
          if ( nominee.getClaimGroupId() != null && nominee.getClaimGroupId() != 0 )
          {
            idNominee = nominee.getClaimGroupId();
          }
          else if ( nominee.getTeamName() != null )
          {
            idNominee = nominee.getTeamId();
          }
          else
          {
            idNominee = nominee.getActivityId();
          }
          if ( id.longValue() == idNominee.longValue() )
          {
            WinnersInfoViewBean winnersInfoViewBean = new WinnersInfoViewBean();

            if ( nominee.getTimePeriodId() != null )
            {
              if ( nominee.getTimePeriodId().longValue() == pastWinnersActivityBean.getTimePeriodId().longValue() )
              {
                winnersInfoViewBean.setTeamNomination( nominee.isTeamNomination() );
                winnersInfoViewBean.setTeamId( nominee.getTeamId() );
                urlParams.put( "teamId", nominee.getTeamId() );
                winnersInfoViewBean.setTeamNominationWinnersName( nominee.getTeamName() );
                winnersInfoViewBean.setWinnerName( nominee.getName() );
                winnersInfoViewBean.setWinnerId( nominee.getUserId() );
                urlParams.put( "winnerId", nominee.getUserId() );
                urlParams.put( "timePeriodId", nominee.getTimePeriodId() );
                winnersInfoViewBean.setPeriod( nominee.getTimePeriodName() );
                winnersInfoViewBean.setDetailName( nominee.getDetailedName( nominee.isTeamNomination() ) );
                winnersInfoViewBean.setWinnerOrgName( nominee.getOrgName() );
                winnersInfoViewBean.setDepartmentName( nominee.getDepartmentName() );
                winnersInfoViewBean.setWinnerPosition( nominee.getPosition() );
                winnersInfoViewBean.setCountryCode( nominee.getCountryCode() );
                winnersInfoViewBean.setCountryName( nominee.getCountryName() );
                winnersInfoViewBean.setAvatarUrl( nominee.getAvatar() );
              }
            }
            else
            {
              winnersInfoViewBean.setTeamNomination( nominee.isTeamNomination() );
              winnersInfoViewBean.setTeamId( nominee.getTeamId() );
              urlParams.put( "teamId", nominee.getTeamId() );
              winnersInfoViewBean.setTeamNominationWinnersName( nominee.getTeamName() );
              winnersInfoViewBean.setWinnerName( nominee.getName() );
              winnersInfoViewBean.setWinnerId( nominee.getUserId() );
              urlParams.put( "winnerId", nominee.getUserId() );
              urlParams.put( "timePeriodId", nominee.getTimePeriodId() );
              winnersInfoViewBean.setPeriod( nominee.getTimePeriodName() );
              winnersInfoViewBean.setDetailName( nominee.getDetailedName( nominee.isTeamNomination() ) );
              winnersInfoViewBean.setWinnerOrgName( nominee.getOrgName() );
              winnersInfoViewBean.setDepartmentName( nominee.getDepartmentName() );
              winnersInfoViewBean.setWinnerPosition( nominee.getPosition() );
              winnersInfoViewBean.setCountryCode( nominee.getCountryCode() );
              winnersInfoViewBean.setCountryName( nominee.getCountryName() );
              winnersInfoViewBean.setAvatarUrl( nominee.getAvatar() );
            }
            urlParams.put( "activityId", nominee.getActivityId() );

            if ( nominee.isTeamNomination() )
            {
              List<NameIdBean> teamList = getNominationClaimService().getTeamMembersByTeamName( nominee.getTeamId() );
              List<TeamListViewBean> teamListView = new ArrayList<>();

              for ( NameIdBean bean : teamList )
              {
                TeamListViewBean teamListViewBean = new TeamListViewBean();
                teamListViewBean.setName( bean.getName() );
                teamListViewBean.setId( bean.getId().toString() );
                teamListView.add( teamListViewBean );
              }
              nominationApprovalsViewBean.setTeamList( teamListView );

            }

            winnersInfoViewBean.setDetailUrl( buildNominationsPastWinnerDetailPageUrl( request, urlParams ) );//
            winnersInfoViewBean.setAwardName( buildWinnerPromotionName( nominee.getPromotionName(), nominee.getLevelId(), nominee.getLevelName() ) );

            winnersInfo.add( winnersInfoViewBean );
          }
        }
        nominationApprovalsViewBean.setWinnersInfo( winnersInfo );

        if ( pastWinnersActivityBean.getClaimGroupId() == null || pastWinnersActivityBean.getClaimGroupId() == 0 )
        {
          for ( PastWinnersTeamMembersSummary pastWinnersTeamMembers : teamMembers )
          {
            if ( id.longValue() == pastWinnersTeamMembers.getActivityId().longValue() )
            {
              // The detail page expects both a team ID and winner user ID.
              // Results are being split to one member each, so there will only be the one winnerId
              urlParams.put( "winnerId", pastWinnersTeamMembers.getMemberId() );
            }
            else if ( id.longValue() == pastWinnersTeamMembers.getTeamId() )
            {
              urlParams.put( "winnerId", null );
            }
          }
        }

        nominationApprovalsViewBean.setDetailURL( buildNominationsPastWinnerDetailPageUrl( request, urlParams ) );
        nominationApprovals.add( nominationApprovalsViewBean );
      }

      // in case if there is a team we need to show only one winner of that team
      // we could have used filter to remove unwanted items but not sure if the order is important
      // here
      Set<Long> teams = new HashSet<Long>();
      if ( CollectionUtils.isNotEmpty( nominationApprovals ) )
      {
        for ( Iterator<NominationApprovalsViewBean> iterator = nominationApprovals.iterator(); iterator.hasNext(); )
        {
          NominationApprovalsViewBean nominationApprovalsViewBean = (NominationApprovalsViewBean)iterator.next();
          List<WinnersInfoViewBean> winnersInfo = nominationApprovalsViewBean.getWinnersInfo();
          if ( CollectionUtils.isNotEmpty( winnersInfo ) )
          {
            for ( WinnersInfoViewBean winnersInfoViewBean : winnersInfo )
            {
              if ( winnersInfoViewBean.getTeamId() != null )
              {
                if ( teams.contains( winnersInfoViewBean.getTeamId() ) )
                {
                  iterator.remove();
                }
                else
                {
                  teams.add( winnersInfoViewBean.getTeamId() );
                }
              }
            }
          }
        }
      }

      nominationPastWinnersViewBean.setNominationApprovals( nominationApprovals );
      writeAsJsonToResponse( nominationPastWinnersViewBean, response, ContentType.JSON );
    }
    else
    {
      List<ServiceError> validationErrors = new ArrayList<ServiceError>();
      if ( fieldSearch )
      {
        validationErrors.add( new ServiceError( "Participant is not a   previous winner." ) );
      }
      else
      {
        validationErrors.add( new ServiceError( "No winner(s) found to display for the selected date range." ) );
      }
      writeAsJsonToResponse( getMessageFromServiceErrors( validationErrors ), response );
    }
    return null;
  }

  public ActionForward eligiblePastWinnersPromotions( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    EligibleNominationPromotionListViewObject jsonResponse = new EligibleNominationPromotionListViewObject();

    Map<String, Object> output = getNominationClaimService().getEligiblePastWinnersPromotions( UserManager.getUserId() );
    List<EligibleNominationPromotionValueObject> nomPromos = (List<EligibleNominationPromotionValueObject>)output.get( "p_out_promo_list" );

    for ( EligibleNominationPromotionValueObject vo : nomPromos )
    {
      jsonResponse.addPromotionView( new EligibleNominationPromotionViewObject( vo.getPromoId(), vo.getName(), vo.getMaxSubmissionAllowed(), vo.getUsedSubmission(), vo.getMessage() ) );
    }
    jsonResponse.setTotalPromotionCount( nomPromos.size() );
    writeAsJsonToResponse( jsonResponse, response, ContentType.JSON );
    return null;
  }

  public ActionForward nominationWinnerDetailsPage( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    Map<String, Object> parameters = new HashMap<String, Object>();
    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    if ( StringUtils.isNotBlank( clientState ) )
    {
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      parameters = ClientStateSerializer.deserialize( clientState, password );
    }

    request.setAttribute( "pastWinnersPageDataUrl", buildNominationsPastWinnerDetailsDataUrl( request, parameters ) );
    request.setAttribute( "pdfServiceUrl", getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.PDF_SERVICE_URL ).getStringVal() );
    return mapping.findForward( "nominationDetails" );
  }

  private String buildNominationsPastWinnerDetailsDataUrl( HttpServletRequest request, Map<String, Object> urlParams )
  {
    String url = ClientStateUtils.generateEncodedLink( request.getContextPath(), "/nomination/viewNominationPastWinnersList.do?method=nominationWinnersDetail", urlParams );
    return url;
  }

  public ActionForward nominationWinnersDetail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    NominationPastWinnersForm pastWinnersForm = (NominationPastWinnersForm)form;
    Map<String, Object> parameters = new HashMap<>();

    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();

    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    parameters = ClientStateSerializer.deserialize( clientState, password );
    parameters.put( "participantId", UserManager.getUserId() );

    // This is because from past winners list page, we are passing winnerId. But the page can be
    // accessed from activity history and my winners tile. We can default winnerId to logged in user
    // id in this case. Procedure needs this for validation
    if ( !parameters.containsKey( "winnerId" ) )
    {
      parameters.put( "winnerId", UserManager.getUserId() );
    }

    if ( parameters.get( "claimId" ) != null )
    {
      Claim claim = getClaimService().getClaimById( (Long)parameters.get( "claimId" ) );
      NominationClaim nominationClaim = (NominationClaim)claim;

      if ( StringUtils.isNotEmpty( nominationClaim.getTeamName() ) )
      {
        parameters.put( "teamId", nominationClaim.getTeamId() );
      }
      if ( Objects.nonNull( nominationClaim.getTimPeriod() ) )
      {
        parameters.put( "timePeriodId", nominationClaim.getTimPeriod().getId() );
      }

      if ( parameters.get( "promotionId" ) != null )
      {
        NominationPromotion nominationPromotion = (NominationPromotion)nominationClaim.getPromotion();
        Long id;
        boolean isCumulative = false;
        if ( nominationPromotion.isCumulative() )
        {
          id = nominationClaim.getClaimGroup().getId();
          isCumulative = true;
        }
        else
        {
          id = (Long)parameters.get( "claimId" );
        }
        Activity activity = getActivityService().getNominationActivityId( UserManager.getUserId(), (Long)parameters.get( "promotionId" ), id, isCumulative, (Long)parameters.get( "approvalRound" ) );
        parameters.put( "activityId", activity.getId() );
      }
    }

    Map<String, Object> output = getNominationClaimService().getNominationPastWinnersDetail( parameters );

    List<PastWinnersNomineeDetails> nomineeDetails = (List<PastWinnersNomineeDetails>)output.get( "p_out_nominee_dtl_res" );
    List<PastWinnersNominatorDetails> nominatorDetails = (List<PastWinnersNominatorDetails>)output.get( "p_out_nominator_dtl_res" );
    List<PastWinnersTeamMembersDetail> teamDetails = (List<PastWinnersTeamMembersDetail>)output.get( "p_out_team_memb_dtl_res" );
    List<PastWinnersBehaviorDetails> behaviorDetails = (List<PastWinnersBehaviorDetails>)output.get( "p_out_behavior_dtl_res" );
    List<PastWinnersCustomDetails> customDetails = (List<PastWinnersCustomDetails>)output.get( "p_out_custom_dtl_res" );

    NominationPastWinnersDetailViewBean nominationPastWinnersDetailViewBean = new NominationPastWinnersDetailViewBean();

    List<NominationWinnersDetailViewBean> winnersList = new ArrayList<NominationWinnersDetailViewBean>();
    List<NominatorDetailInfoViewBean> nominatorDetailInfo = new ArrayList<NominatorDetailInfoViewBean>();
    for ( PastWinnersNomineeDetails nominee : nomineeDetails )
    {
      NominationWinnersDetailViewBean nominationWinnersDetailViewBean = new NominationWinnersDetailViewBean();

      String siteUrlPrefix = "";
      if ( !Environment.isCtech() )
      {
        siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_PRE ).getStringVal();
      }
      else
      {
        siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
      }
      nominationWinnersDetailViewBean.setContextPath( siteUrlPrefix + "/assets/" );
      if ( nominee.getNomineeUserId() != null && UserManager.getUserId().equals( nominee.getNomineeUserId() ) )
      {
        nominationWinnersDetailViewBean.setMine( true );
      }
      nominationWinnersDetailViewBean.setWinner( true );
      String winnerName = nominee.getNomineeName();
      if ( StringUtils.isNotEmpty( winnerName ) )
      {
        nominationWinnersDetailViewBean.setWinnerName( nominee.getNomineeName() );
      }
      else
      {
        nominationWinnersDetailViewBean.setWinnerName( nominee.getTeamName() );
        nominationWinnersDetailViewBean.setTeamId( nominee.getTeamId() );
        nominationWinnersDetailViewBean.setTeam( true );

        List<NameIdBean> teamList = getNominationClaimService().getTeamMembersByTeamName( nominee.getTeamId() );
        List<TeamListViewBean> teamListView = new ArrayList<>();

        for ( NameIdBean bean : teamList )
        {
          TeamListViewBean teamListViewBean = new TeamListViewBean();
          teamListViewBean.setName( bean.getName() );
          teamListViewBean.setId( bean.getId().toString() );
          teamListView.add( teamListViewBean );
        }
        nominationWinnersDetailViewBean.setTeamList( teamListView );
      }
      nominationWinnersDetailViewBean.setAwardName( buildWinnerPromotionName( nominee.getPromotionName(), nominee.getLevelNumber(), nominee.getLevelName() ) );
      nominationWinnersDetailViewBean.setAwardedDate( DateUtils.toDisplayString( nominee.getDateApproved() ) );
      if ( StringUtils.isNotBlank( nominee.getOtherDescription() ) )
      {
        nominationWinnersDetailViewBean.setAward( nominee.getOtherDescription() );
      }
      else if ( nominee.getAwardAmount() != null && !nominee.getAwardAmount().equals( new BigDecimal( 0 ) ) )
      {
        nominationWinnersDetailViewBean.setAward( nominee.getAwardAmount().toString() );
      }
      nominationWinnersDetailViewBean.setCurrencyLabel( nominee.getCurrencyLabel() );
      nominationWinnersDetailViewBean.setAllowTranslate( !UserManager.getUserLocale().equals( nominee.getSubmitterLangId() ) );

      for ( PastWinnersNominatorDetails nominator : nominatorDetails )
      {

        NominatorDetailInfoViewBean nominatorDetailInfoViewBean = new NominatorDetailInfoViewBean();
        nominatorDetailInfoViewBean.setId( nominator.getNominatorUserId() );
        nominatorDetailInfoViewBean.setClaimId( nominator.getClaimId() );
        nominatorDetailInfoViewBean.setFirstName( nominator.getNominatorFirstName() );
        nominatorDetailInfoViewBean.setLastName( nominator.getNominatorLastName() );
        nominatorDetailInfoViewBean.setAvatarUrl( nominator.getNominatorAvatarUrl() );
        nominatorDetailInfoViewBean.setTitle( nominator.getNominatorPosition() );
        nominatorDetailInfoViewBean.setCountryCode( nominator.getNominatorCountryCode() );
        nominatorDetailInfoViewBean.setCountryName( nominator.getNominatorCountryName() );
        nominatorDetailInfoViewBean.setOrgName( nominator.getNominatorOrgName() );
        nominatorDetailInfoViewBean.setDepartmentName( nominator.getNominatorDprtName() );
        if ( StringUtils.isNotBlank( nominator.getSubmitterComments() ) )
        {
          nominatorDetailInfoViewBean.setCommentText( nominator.getSubmitterComments() );
        }
        if ( nominee.getOwnCardName() != null )
        {
          nominatorDetailInfoViewBean.setEcardImg( nominee.getOwnCardName() );
        }
        else if ( nominee.geteCardName() != null )
        {
          nominatorDetailInfoViewBean.setEcardImg( nominee.geteCardName() );
        }
        if ( StringUtils.isNotBlank( nominee.getCardVideoUrl() ) && Objects.nonNull( nominee.getCardVideoUrl() ) )
        {
          if ( nominee.getCardVideoUrl().contains( ActionConstants.REQUEST_ID ) )
          {
            MTCVideo mtcVideo = getMtcVideoService().getMTCVideoByRequestId( nominee.getRequestId( nominee.getCardVideoUrl() ) );
            nominatorDetailInfoViewBean.setVideoUrl( mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl() );
            nominatorDetailInfoViewBean.setVideoImg( mtcVideo.getThumbNailImageUrl() );
          }
          else
          {
            nominatorDetailInfoViewBean.setVideoImg( nominee.getCardVideoImageUrl() );
            nominatorDetailInfoViewBean.setVideoUrl( nominee.getCardVideoUrl() );
          }
        }
        else
        {
          nominatorDetailInfoViewBean.setVideoImg( nominee.getCardVideoImageUrl() );
          nominatorDetailInfoViewBean.setVideoUrl( nominee.getCardVideoUrl() );
        }

        List<NominationDetailInfoFieldsViewBean> fields = new ArrayList<NominationDetailInfoFieldsViewBean>();
        for ( PastWinnersCustomDetails custom : customDetails )
        {
          if ( nominator.getClaimId().longValue() == custom.getClaimId().longValue() )
          {
            NominationDetailInfoFieldsViewBean nominationDetailInfoFieldsViewBean = new NominationDetailInfoFieldsViewBean();
            if ( !custom.isWhy() )
            {
              nominationDetailInfoFieldsViewBean.setFieldId( custom.getClaimFormStepElementId() );
              nominationDetailInfoFieldsViewBean.setType( custom.getClaimFormStepElementName() );
              nominationDetailInfoFieldsViewBean.setLabel( custom.getClaimFormStepElementName() );
              nominationDetailInfoFieldsViewBean.setValue( custom.getClaimFormValue() );
              nominationDetailInfoFieldsViewBean.setSequenceNumber( custom.getSequenceNumber() );
            }
            else
            {
              if ( StringUtils.isNotBlank( custom.getClaimFormValue() ) )
              {
                nominatorDetailInfoViewBean.setCommentText( custom.getClaimFormValue() );
              }
            }
            fields.add( nominationDetailInfoFieldsViewBean );
          }
        }
        nominatorDetailInfoViewBean.setFields( fields );

        List<NominationDetailInfoBadgesViewBean> badges = new ArrayList<NominationDetailInfoBadgesViewBean>();
        for ( PastWinnersBehaviorDetails behavior : behaviorDetails )
        {
          if ( StringUtils.isNotBlank( behavior.getBehaviorName() ) )
          {
            NominationDetailInfoBadgesViewBean nominationDetailInfoBadgesViewBean = new NominationDetailInfoBadgesViewBean();
            nominationDetailInfoBadgesViewBean.setId( behavior.getBadgeId() );
            nominationDetailInfoBadgesViewBean.setBehavior( behavior.getBehaviorName() );
            if ( StringUtils.isNotBlank( behavior.getBadgeName() ) )
            {
              String badgeUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
              List earnedNotEarnedImageList = getGamificationService().getEarnedNotEarnedImageList( behavior.getBadgeName() );
              Iterator itr = earnedNotEarnedImageList.iterator();
              while ( itr.hasNext() )
              {
                BadgeLibrary badgeLib = (BadgeLibrary)itr.next();
                nominationDetailInfoBadgesViewBean.setBadgeUrl( badgeUrlPrefix + "/" + badgeLib.getEarnedImageSmall() );
              }
            }
            badges.add( nominationDetailInfoBadgesViewBean );
          }
        }
        nominatorDetailInfoViewBean.setBadges( badges );

        nominatorDetailInfoViewBean.setDateSubmitted( DateUtils.toDisplayString( nominee.getDateSubmitted() ) );
        if ( nominee.getCertificateId() != null )
        {
          nominatorDetailInfoViewBean.seteCertUrl( CertUtils.getFullCertificateUrl( nominee.getCertificateId(), request.getContextPath(), PromotionType.lookup( PromotionType.NOMINATION ) ) );
        }

        nominatorDetailInfo.add( nominatorDetailInfoViewBean );
      }

      nominationWinnersDetailViewBean.setNominatorInfo( nominatorDetailInfo );

      winnersList.add( nominationWinnersDetailViewBean );
    }
    nominationPastWinnersDetailViewBean.setNominationWinnersDetail( winnersList );
    writeAsJsonToResponse( nominationPastWinnersDetailViewBean, response, ContentType.JSON );
    return null;
  }

  public ActionForward getNominationWinnerModalDetails( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> output = getNominationClaimService().getNominationWinnerModalDetails();

    List<NominationModalNomineeDetails> modalDetails = (List<NominationModalNomineeDetails>)output.get( "p_out_nomniee_dtl" );

    if ( modalDetails != null && modalDetails.size() > 0 )
    {
      NominationWinnerModalViewBean nominationWinnerModalViewBean = new NominationWinnerModalViewBean();
      List<NominationWinnerModalDetailsViewBean> nominationWinnerModalDetailsViewBeans = new ArrayList<NominationWinnerModalDetailsViewBean>();
      NominationWinnerModalDetailsViewBean nominationWinnerModalDetailsViewBean = new NominationWinnerModalDetailsViewBean();
      NominationWinnerModalDataViewBean nominationWinnerModalDataViewBean = new NominationWinnerModalDataViewBean();
      NominationWinnerModalDisplayDetaillsViewBean nominationWinnerModalDisplayDetaillsViewBean = new NominationWinnerModalDisplayDetaillsViewBean();
      nominationWinnerModalDetailsViewBean.setType( "dataUpdate" );
      nominationWinnerModalDetailsViewBean.setName( "participantProfile" );
      nominationWinnerModalDisplayDetaillsViewBean.setDisplayNominationWinnerModal( true );
      if ( modalDetails.get( 0 ).getWinCout() > 1 )
      {
        nominationWinnerModalDisplayDetaillsViewBean.setMultipleNominationsWon( true );
      }
      nominationWinnerModalDisplayDetaillsViewBean.setNominationDetails( modalDetails.get( 0 ).getNominationDetails() );

      if ( modalDetails.size() == 1 )
      {
        NominationModalNomineeDetails nominationModalNomineeDetails = modalDetails.get( 0 );
        if ( nominationModalNomineeDetails != null )
        {
          Map<String, Object> urlParams = new HashMap<>();
          urlParams.put( "activityId", nominationModalNomineeDetails.getActivityId() );
          urlParams.put( "teamId", nominationModalNomineeDetails.getTeamId() );

          nominationWinnerModalDisplayDetaillsViewBean.setDetailURL( buildNominationsPastWinnerDetailPageUrl( request, urlParams ) );
          if ( nominationModalNomineeDetails.getCashWon() != null && nominationModalNomineeDetails.getCashWon().compareTo( new BigDecimal( 0 ) ) != 0 )
          {
            nominationWinnerModalDisplayDetaillsViewBean.setReceivedAward( nominationModalNomineeDetails.getCashWon() + " " + nominationModalNomineeDetails.getCurrencyCode() );
          }
          else if ( nominationModalNomineeDetails.getPointsWon() != null && nominationModalNomineeDetails.getPointsWon().longValue() != 0 )
          {
            nominationWinnerModalDisplayDetaillsViewBean
                .setReceivedAward( nominationModalNomineeDetails.getPointsWon() + " " + CmsResourceBundle.getCmsBundle().getString( "nomination.past.winners.POINTS" ) );
          }
          else if ( StringUtils.isNotBlank( nominationModalNomineeDetails.getPayoutDescription() ) )
          {
            nominationWinnerModalDisplayDetaillsViewBean.setReceivedAward( nominationModalNomineeDetails.getPayoutDescription() );
          }
          else
          {
            nominationWinnerModalDisplayDetaillsViewBean.setReceivedAward( "" );
          }
        }
      }
      nominationWinnerModalDataViewBean.setNominationWinnerModalDisplayDetaillsViewBean( nominationWinnerModalDisplayDetaillsViewBean );
      nominationWinnerModalDetailsViewBean.setNominationWinnerModalDataViewBean( nominationWinnerModalDataViewBean );
      nominationWinnerModalDetailsViewBeans.add( nominationWinnerModalDetailsViewBean );
      nominationWinnerModalViewBean.setMessages( nominationWinnerModalDetailsViewBeans );
      writeAsJsonToResponse( nominationWinnerModalViewBean, response, ContentType.JSON );
    }
    return null;
  }

  public ActionForward getNominationMyWinnersListPage( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( "nominationMyWinnersList" );
  }

  public ActionForward getNominationMyWinnersList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put( "participantId", UserManager.getUserId() );
    parameters.put( "locale", UserManager.getLocale().toString() );

    int pageNumber;
    if ( request.getParameter( "currentPage" ) != null )
    {
      pageNumber = Integer.parseInt( request.getParameter( "currentPage" ) );
    }
    else
    {
      pageNumber = 1;
    }
    int perPage = 10;
    int rowNumStart = ( pageNumber - 1 ) * perPage;
    int rowNumEnd = pageNumber * perPage;

    parameters.put( "rowNumberStart", rowNumStart );
    parameters.put( "rowNumberEnd", rowNumEnd + 1 );

    if ( request.getParameter( "sortedBy" ) != null )
    {
      String sortedBy = request.getParameter( "sortedBy" );
      parameters.put( "sortedBy", sortedBy );
    }
    else
    {
      parameters.put( "sortedBy", "asc" );
    }
    if ( request.getParameter( "sortedOn" ) != null )
    {
      String sortedOn = request.getParameter( "sortedOn" );
      parameters.put( "sortedOn", sortedOn );
    }
    else
    {
      parameters.put( "sortedOn", "date_won" );
    }

    Map<String, Object> output = getNominationClaimService().getNominationMyWinnersList( parameters );
    List<NominationMyWinners> myWinners = (List<NominationMyWinners>)output.get( "p_out_data" );
    int totalWinnersCount = (int)output.get( "p_out_win_count" );

    if ( myWinners != null && myWinners.size() > 0 )
    {
      NominationsMyWinnersViewBean nominationsMyWinners = new NominationsMyWinnersViewBean();
      MyWinnersTabularDataViewBean myWinnersTabularData = new MyWinnersTabularDataViewBean();

      MyWinnersTabularDataMetaViewBean myWinnersTabularDataMetaViewBean = new MyWinnersTabularDataMetaViewBean();
      List<MyWinnersTabularDataColumnsViewBean> columns = new ArrayList<MyWinnersTabularDataColumnsViewBean>();
      columns.add( new MyWinnersTabularDataColumnsViewBean( 1, "promotion_name", CmsResourceBundle.getCmsBundle().getString( "nomination.past.winners.PROMOTION_NAME" ), true ) );
      columns.add( new MyWinnersTabularDataColumnsViewBean( 2, "date_won", CmsResourceBundle.getCmsBundle().getString( "nomination.past.winners.DATE_WON" ), true ) );
      columns.add( new MyWinnersTabularDataColumnsViewBean( 3, "detailUrl", CmsResourceBundle.getCmsBundle().getString( "nomination.past.winners.DETAIL_PAGE" ), false ) );
      myWinnersTabularDataMetaViewBean.setColumns( columns );
      myWinnersTabularData.setMeta( myWinnersTabularDataMetaViewBean );

      List<MyWinnersTabularDataResultsViewBean> results = new ArrayList<>();

      for ( NominationMyWinners myWinner : myWinners )
      {
        Map<String, Object> urlParams = new HashMap<>();
        urlParams.put( "activityId", myWinner.getActivityId() );
        urlParams.put( "teamId", myWinner.getTeamId() );

        MyWinnersTabularDataResultsViewBean myWinnersTabularDataResults = new MyWinnersTabularDataResultsViewBean();
        myWinnersTabularDataResults.setPromotionName( buildPromotionName( myWinner.getPromotionName(), myWinner.getLevelNumber(), myWinner.getLevelName() ) );
        myWinnersTabularDataResults.setDateWon( DateUtils.toDisplayString( myWinner.getDateWon() ) );
        myWinnersTabularDataResults.setDetailURL( buildNominationsPastWinnerDetailPageUrl( request, urlParams ) );
        results.add( myWinnersTabularDataResults );
      }
      myWinnersTabularData.setResults( results );
      nominationsMyWinners.setTabularData( myWinnersTabularData );
      nominationsMyWinners.setTotal( totalWinnersCount );
      nominationsMyWinners.setPerPage( perPage );
      nominationsMyWinners.setCurrentPage( pageNumber );
      nominationsMyWinners.setSortedBy( (String)parameters.get( "sortedBy" ) );
      nominationsMyWinners.setSortedOn( (String)parameters.get( "sortedOn" ) );
      writeAsJsonToResponse( nominationsMyWinners, response, ContentType.JSON );
    }
    return null;
  }

  /** Compose PromotionName to include either level name or level number */
  private String buildPromotionName( String promotionName, Long levelNumber, String levelName )
  {
    String levelText = CmsResourceBundle.getCmsBundle().getString( "nomination.past.winners.LEVEL" );
    if ( StringUtils.isNotBlank( levelName ) )
    {
      return promotionName + " - " + levelText + " " + String.valueOf( levelNumber ) + " ( " + levelName + " ) ";
    }
    else
    {
      return promotionName + " - " + levelText + " " + String.valueOf( levelNumber );
    }
  }

  private String buildWinnerPromotionName( String promotionName, Long levelNumber, String levelName )
  {
    if ( StringUtils.isNotBlank( levelName ) )
    {
      return promotionName + " - " + levelName;
    }
    else
    {
      return promotionName;
    }
  }

  public WebErrorMessageList getMessageFromServiceErrors( List<ServiceError> serviceErrors )
  {

    WebErrorMessageList messageList = new WebErrorMessageList();
    WebErrorMessage msg = null;

    for ( Object obj : serviceErrors )
    {
      ServiceError error = (ServiceError)obj;
      String errorMessage = CmsResourceBundle.getCmsBundle().getString( error.getKey() );
      if ( StringUtils.isNotEmpty( error.getArg1() ) )
      {
        errorMessage = errorMessage.replace( "{0}", error.getArg1() );
      }
      if ( StringUtils.isNotEmpty( error.getArg2() ) )
      {
        errorMessage = errorMessage.replace( "{1}", error.getArg2() );
      }
      if ( StringUtils.isNotEmpty( error.getArg3() ) )
      {
        errorMessage = errorMessage.replace( "{2}", error.getArg3() );
      }
      errorMessage = errorMessage.replace( "???", "" );
      errorMessage = errorMessage.replace( "???", "" );
      error.setArg1( errorMessage );

      msg = new WebErrorMessage();
      msg.setText( errorMessage );
      WebErrorMessage.addErrorMessage( msg );
      messageList.getMessages().add( msg );

    }
    return messageList;
  }

  private String buildNominationsPastWinnerDetailPageUrl( HttpServletRequest request, Map<String, Object> urlParams )
  {
    String url = ClientStateUtils.generateEncodedLink( request.getContextPath(), "/nomination/viewNominationPastWinnersList.do?method=nominationWinnerDetailsPage", urlParams );
    return url;
  }

  protected static NominationClaimService getNominationClaimService()
  {
    return (NominationClaimService)getService( NominationClaimService.BEAN_NAME );
  }

  protected static ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  protected static ActivityService getActivityService()
  {
    return (ActivityService)getService( ActivityService.BEAN_NAME );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private GamificationService getGamificationService()
  {
    return (GamificationService)getService( GamificationService.BEAN_NAME );
  }

  private MTCVideoService getMtcVideoService()
  {
    return (MTCVideoService)getService( MTCVideoService.BEAN_NAME );
  }
}
