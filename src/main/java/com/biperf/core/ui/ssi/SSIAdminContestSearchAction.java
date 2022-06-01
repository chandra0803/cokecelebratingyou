
package com.biperf.core.ui.ssi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.ssi.SSIAdminContestActions;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.PresentationUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.UserValueBean;
import com.objectpartners.cms.domain.Asset;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.ContentReaderManager;

public class SSIAdminContestSearchAction extends BaseDispatchAction
{

  public static final String CONTEST_SECTION_CODE = "ssi_contest_data";
  public static final String CONTEST_CMASSET_CONTEST = "SSI Contest Data";

  /**
   * cancelled
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward searchContest( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    SSIContestSearchForm ssiContestSearchForm = (SSIContestSearchForm)actionForm;

    String contestName = ssiContestSearchForm.getContestName();
    String creatorLastName = ssiContestSearchForm.getCreatorName();
    String ssiContestStatus = ssiContestSearchForm.getSsiContestStatus();
    Date endDate = ssiContestSearchForm.getEndDate();
    Date startDate = ssiContestSearchForm.getStartDate();
    List<String> assetCodes = new ArrayList<String>();
    List<SSIContestAdminView> ssiContestAdminViewList = new ArrayList<SSIContestAdminView>();
    ssiContestSearchForm.setSearchCreatorLastName( null );
    if ( !StringUtils.isEmpty( contestName ) )
    {
      // RegExp to search Contest names
      contestName = contestName.replaceAll( "\\.", "" ); // escape dots
      contestName = contestName.replaceAll( "\\*", "" ); // escape *
      contestName = "(?i).*" + contestName + ".*";

      // Get Contest Asset codes
      List assets = getCMAssetService().getAssetsForSection( CONTEST_SECTION_CODE );
      for ( Iterator assetsIter = assets.iterator(); assetsIter.hasNext(); )
      {
        Object contentObject = null;
        Content content = null;

        Asset asset = (Asset)assetsIter.next();
        if ( asset.getName().equals( CONTEST_CMASSET_CONTEST ) )
        {
          contentObject = ContentReaderManager.getContentReader().getContent( asset.getCode() );
          content = (Content)contentObject;
          if ( ( (String)content.getContentDataMap().get( "CONTEST_NAME" ) ).matches( contestName ) )
          {
            assetCodes.add( asset.getCode() );
          }
        }
      }

      if ( ! ( assetCodes != null && assetCodes.size() > 0 ) )
      {
        request.setAttribute( "ssiContestList", ssiContestAdminViewList );
        request.setAttribute( "pageSize", new Integer( PresentationUtils.getDisplayTablePageSize( ssiContestAdminViewList.size() ) ) );
        return actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
      }
    }

    // Get the USER_ID of matching creator names search keyword
    List<Long> creatorIDs = null;
    if ( !StringUtils.isEmpty( creatorLastName ) )
    {
      creatorIDs = getParticipantService().getAllParticipantIDsByLastName( creatorLastName );
      if ( ! ( creatorIDs != null && creatorIDs.size() > 0 ) )
      {
        request.setAttribute( "ssiContestList", ssiContestAdminViewList );
        request.setAttribute( "pageSize", new Integer( PresentationUtils.getDisplayTablePageSize( ssiContestAdminViewList.size() ) ) );
        return actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
      }
    }

    List<SSIContest> ssiContestList = getSSIContestService().getContestSearchByAdmin( assetCodes, creatorIDs, ssiContestStatus, startDate, endDate );

    for ( SSIContest ssiContest : ssiContestList )
    {
      SSIContestAdminView ssiContestAdminView = new SSIContestAdminView();
      ssiContestAdminView.setId( ssiContest.getId() );
      ssiContestAdminView.setContestName( ssiContest.getContestNameFromCM() );
      ssiContestAdminView.setCreatorName( getParticipantService().getLNameFNameByPaxIdWithComma( ssiContest.getContestOwnerId() ) );
      ssiContestAdminView.setContestStatus( ssiContest.getStatus() );
      ssiContestAdminView.setContestStartDate( ssiContest.getStartDate() );
      ssiContestAdminView.setContestEndDate( ssiContest.getEndDate() );
      ssiContestAdminView.setContestType( ssiContest.getContestType() );
      int lastDayOfProcessExecutionDate = 1;// archived contest which is older than today.

      if ( ssiContest.getPromotion().getDaysToArchive() != null && ssiContest.getPromotion().getDaysToArchive() > 0 )
      {
        if ( ssiContest.getContestType().getCode().equals( SSIContestType.AWARD_THEM_NOW ) )
        {
          Promotion ssiPromotion = getPromotionService().getPromotionByIdWithAssociations( ssiContest.getPromotion().getId(), null );
          if ( ( (SSIPromotion)ssiPromotion ).getContestApprovalLevels() != 0 )
          {
            if ( ssiContest.getEndDate() != null && ssiContest.getEndDate().equals( getArchivalDate( lastDayOfProcessExecutionDate ) ) )
            {
              int issuanceWaitingforApprovalCount = getSSIContestService().getWaitingforApprovalAwardThemNowIssuancesCount( ssiContest.getId() );
              if ( issuanceWaitingforApprovalCount == 0 )
              {
                ssiContestAdminView.setCanClose( true );
              }
            }
          }
          else
          {
            if ( ssiContest.getEndDate() != null && ssiContest.getEndDate().equals( getArchivalDate( lastDayOfProcessExecutionDate ) ) )
            {
              ssiContestAdminView.setCanClose( true );
            }
          }
        }
        else
        {
          if ( ssiContest.getPayoutIssuedDate() != null && ssiContest.getPayoutIssuedDate().before( new Date() ) )
          {
            if ( ssiContest.getStatus().getCode().equals( SSIContestStatus.FINALIZE_RESULTS ) )
            {
              ssiContestAdminView.setCanClose( true );
            }
          }
        }
      }
      // yet to set modified date
      // Lunch As Button - logic
      if ( ssiContest.getStatus().getCode().equals( SSIContestStatus.DRAFT ) || ssiContest.getStatus().getCode().equals( SSIContestStatus.LIVE )
          || ssiContest.getStatus().getCode().equals( SSIContestStatus.PENDING ) || ssiContest.getStatus().getCode().equals( SSIContestStatus.WAITING_FOR_APPROVAL )
          || ssiContestAdminView.isCanClose() )
      {
        ssiContestAdminView.setCanShowLaunchAsButton( true );
      }
      SSIAdminContestActions adminContestActions = getSSIContestService().getAdminActionByEditCreator( ssiContest.getId() );
      if ( adminContestActions != null )
      {
        ssiContestAdminView.setCanShowEditedAdminIndicator( true );
        ssiContestAdminView.setSsiAdminContestActions( adminContestActions );
        ssiContestAdminView.setSsiAdminNameWithLastUpdated( getParticipantService().getLNameFNameByPaxIdWithComma( adminContestActions.getUserID() ) );
      }

      ssiContestAdminViewList.add( ssiContestAdminView );
    }

    Collections.sort( ssiContestAdminViewList, new SSIContestNameComparator() );

    request.setAttribute( "ssiContestList", ssiContestAdminViewList );
    request.setAttribute( "pageSize", new Integer( PresentationUtils.getDisplayTablePageSize( ssiContestAdminViewList.size() ) ) );
    return actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward closeContest( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    SSIContest ssiContest = getSSIContestService().getContestById( getContestId( request ) );
    int lastDayOfProcessExecutionDate = 1;// archived contest which is older than today.
    if ( ssiContest.getPromotion().getDaysToArchive() != null && ssiContest.getPromotion().getDaysToArchive() > 0 )
    {
      if ( ssiContest.getPayoutIssuedDate() != null && ssiContest.getPayoutIssuedDate().before( new Date() ) )
      {
        if ( ssiContest.getStatus().getCode().equals( SSIContestStatus.FINALIZE_RESULTS ) )
        {
          ssiContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.CLOSED ) );
          getSSIContestService().saveContest( ssiContest );
        }
      }
    }
    getSSIContestService().saveAdminAction( ssiContest.getId(),
                                            UserManager.getUserId(),
                                            SSIAdminContestActions.CLOSE_CONTEST,
                                            "Close Contest with ID: " + ssiContest.getId() + " by Admin ID: " + UserManager.getUser().getOriginalAuthenticatedUser().getUserId() );

    return searchContest( actionMapping, actionForm, request, response );
  }

  /**
   * Returns the import file ID.
   * 
   * @param request the HTTP request from which the import file ID is retrieved.
   * @return the import file ID.
   */
  private Long getContestId( HttpServletRequest request )
  {
    Long ssiContestId = null;

    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      try
      {
        String ssiContestIdString = (String)clientStateMap.get( "ssiContestId" );
        ssiContestId = new Long( ssiContestIdString );
      }
      catch( ClassCastException cce )
      {
        ssiContestId = (Long)clientStateMap.get( "ssiContestId" );
      }
    }
    catch( InvalidClientStateException e )
    {

      throw new IllegalArgumentException( "request parameter clientState was missing" );

    }

    return ssiContestId;
  }

  private java.sql.Date getArchivalDate( int archivalPeriod )
  {
    Calendar calendar = (Calendar)Calendar.getInstance().clone();
    calendar.setTime( new Date() );
    calendar.add( Calendar.DAY_OF_YEAR, -archivalPeriod );
    return new java.sql.Date( calendar.getTime().getTime() );
  }

  /**
   * cancelled
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward searchCreator( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    SSIContestSearchForm ssiContestSearchForm = (SSIContestSearchForm)actionForm;

    // Get search results based on last name only.
    ParticipantSearchCriteria searchCriteria = new ParticipantSearchCriteria();
    searchCriteria.setLastName( ssiContestSearchForm.getSearchCreatorLastName() );
    searchCriteria.setSortByLastNameFirstName( true );
    List<Participant> resultList = getParticipantService().searchParticipant( searchCriteria, true );
    // Remove the current Creator from the list
    SSIContest ssiContest = getSSIContestService().getContestById( ssiContestSearchForm.getSsiContestID() );
    List<Participant> filteredResultList = new ArrayList<Participant>();

    for ( Participant p : resultList )
    {
      if ( !p.getId().equals( ssiContest.getCreatorId() ) )
      {
        filteredResultList.add( p );
      }
    }
    List<Participant> filteredResultListByAudience = new ArrayList<Participant>();
    for ( Participant p : filteredResultList )
    {
      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );
      Promotion promotion = getPromotionService().getPromotionByIdWithAssociations( ssiContest.getPromotion().getId(), promoAssociationRequestCollection );

      // getPromotionService().getPromotionByIdWithAssociations( id, associationRequestCollection )
      if ( getPromotionService().isParticipantInAudience( getParticipantService().getParticipantById( p.getId() ), promotion ) )
      {
        filteredResultListByAudience.add( p );
      }
    }

    // I'm not sure but I get the feel request attributes are usually light-weight, so change from
    // participant to lighter value bean object?
    List<UserValueBean> beanResultList = new ArrayList<UserValueBean>( resultList.size() );
    for ( Participant p : filteredResultListByAudience )
    {
      UserValueBean bean = new UserValueBean();
      bean.setId( p.getId() );
      bean.setFirstName( p.getFirstName() );
      bean.setLastName( p.getLastName() );
      bean.setCountry( getUserService().getPrimaryUserAddress( p.getId() ).getAddress().getCountry() );
      bean.setNodeName( getUserService().getPrimaryUserNode( p.getId() ).getNode().getName() );
      beanResultList.add( bean );
    }
    ssiContestSearchForm.setSearchCreatorByLastNameList( beanResultList );
    request.setAttribute( "ssiContestSearchForm", ssiContestSearchForm );
    request.setAttribute( "ssiCreatorSearchResults", beanResultList );
    request.setAttribute( "ssiCreatorSearchResultsCount", beanResultList.size() );
    ActionMessages actionErrors = new ActionMessages();
    if ( beanResultList == null || beanResultList.size() == 0 )
    {
      request.setAttribute( "noResultsMsg", true );
    }
    return actionMapping.findForward( ActionConstants.SEARCH_FORWARD );
  }

  /**
   * cancelled
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward updateCreator( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    SSIContestSearchForm ssiContestSearchForm = (SSIContestSearchForm)actionForm;
    SSIContest ssiContest = getSSIContestService().getContestById( ssiContestSearchForm.getSsiContestID() );
    Long originalCreator = ssiContest.getContestOwnerId();
    ssiContest.setContestOwnerId( ssiContestSearchForm.getSelectedCreatorUserId() );
    getSSIContestService().saveContest( ssiContest );

    getSSIContestService().saveAdminAction( ssiContest.getId(),
                                            UserManager.getUserId(),
                                            SSIAdminContestActions.EDIT_CREATOR,
                                            "Admin Edited Creatror from " + originalCreator + " to " + ssiContestSearchForm.getSelectedCreatorUserId() + " for Contset ID: " + ssiContest.getId() );

    ssiContestSearchForm.setSearchCreatorLastName( null );
    ssiContestSearchForm.setSelectedCreatorUserId( null );

    return searchContest( actionMapping, actionForm, request, response );
  }

  private SSIContestService getSSIContestService()
  {
    return (SSIContestService)getService( SSIContestService.BEAN_NAME );
  }

  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

}

class SSIContestNameComparator implements Comparator
{
  public int compare( Object o1, Object o2 )
  {
    if ( ! ( o1 instanceof SSIContestAdminView ) || ! ( o2 instanceof SSIContestAdminView ) )
    {
      throw new ClassCastException( "Object is not a SSIContestAdminView object!" );
    }

    String fname1 = ( (SSIContestAdminView)o1 ).getContestName();
    String fname2 = ( (SSIContestAdminView)o2 ).getContestName();
    return fname1.compareTo( fname2 );
  }

}
