/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/leaderBoard/LeaderBoardAction.java,v $
 */

package com.biperf.core.ui.leaderBoard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.Fields;
import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.leaderboard.LeaderBoard;
import com.biperf.core.domain.leaderboard.LeaderBoardParticipant;
import com.biperf.core.domain.leaderboard.LeaderBoardView;
import com.biperf.core.domain.leaderboard.LeaderboardParticipantView;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.leaderboard.LeaderBoardAssociationRequest;
import com.biperf.core.service.leaderboard.LeaderBoardService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ArrayUtil;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.FormFieldConstants;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * LeaderBoardAction.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>sharafud</td>
 * <td>August 13, 2012</td>
 * <td>1.0</td> 
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class LeaderBoardAction extends BaseDispatchAction
{

  private static final String MANAGER = "manager";
  private static final String PARTICIPANT = "participant";
  public static final String LEADERBOARD_KEY = "leaderBoard";
  public static final String LEADERBOARD_TYPE_KEY = "leaderBoardNewlyCreated";
  private static final String SORT_DESCENDING = "desc";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Dispatcher.  Default to home page display.  Too much work to append 'method=display'
   * to all the paths that lead to the home page.  
   */
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    if ( mapping.getParameter() != null )
    {
      return super.execute( mapping, form, request, response );
    }
    else
    {
      return this.display( mapping, form, request, response );
    }
  }

  /**
   * Display home page.
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String forward = "leaderBoardsList";
    List activeLeaderBoards = new ArrayList();
    List underConstructionLeaderBoards = new ArrayList();
    List liveLeaderBoards = new ArrayList();

    activeLeaderBoards = getLeaderBoardService().getLeaderBoardByStatus( LeaderBoard.COMPLETED );
    underConstructionLeaderBoards = getLeaderBoardService().getLeaderBoardByStatus( LeaderBoard.LEADERBOARD_UNDER_CONSTR );
    liveLeaderBoards = getLeaderBoardService().getLeaderBoardByStatus( LeaderBoard.LIVE );
    request.setAttribute( "activeLeaderBoards", activeLeaderBoards );
    request.setAttribute( "liveLeaderBoards", liveLeaderBoards );
    request.setAttribute( "underConstructionLeaderBoards", underConstructionLeaderBoards );
    return mapping.findForward( forward );
  }

  public ActionForward saveDraft( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    LeaderBoardForm leaderBoardForm = (LeaderBoardForm)form;
    String leaderBoardId = leaderBoardForm.getLeaderBoardId();

    WebErrorMessageList messages = new WebErrorMessageList();
    WebErrorMessage message = new WebErrorMessage();
    LeaderBoard leaderBoard = null;

    String activityDate = leaderBoardForm.getActivityDate();
    message = validateDates( leaderBoardForm.getStartDate(), leaderBoardForm.getEndDate(), message, leaderBoardId, activityDate );
    jsonValidateLeaderBoardName( leaderBoardId, leaderBoardForm.getLeaderBoardName(), message );
    jsonValidatePromotionRules( leaderBoardForm, message );
    jsonValidateActivityTitle( leaderBoardForm.getActivityTitle(), message );
    if ( message.getFields().size() == 0 )
    {
      if ( !StringUtils.isEmpty( leaderBoardId ) )
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new LeaderBoardAssociationRequest( LeaderBoardAssociationRequest.ALL_PAX ) );
        leaderBoard = getLeaderBoardService().getLeaderBoardByIdWithAssociations( Long.parseLong( leaderBoardId ), associationRequestCollection );
      }
      else
      {
        User user = getUserService().getUserById( UserManager.getUserId() );
        leaderBoard = new LeaderBoard();
        leaderBoard.setUser( user );
      }
      Date currentStartDate = leaderBoard.getStartDate();
      leaderBoard = leaderBoardForm.toDomainObject( leaderBoard );
      if ( leaderBoard.getStartDate() == null )
      {
        leaderBoard.setStartDate( currentStartDate );
      }
      leaderBoard.setStatus( LeaderBoard.LEADERBOARD_UNDER_CONSTR );
      leaderBoard = getLeaderBoardService().saveRulesCmText( leaderBoard, leaderBoard.getRulescmAssetText() );
      if ( !StringUtils.isEmpty( leaderBoardId ) )
      {
        // copy pax in to reference pax list
        leaderBoardForm = copyParticipantsToReferenceParticipants( leaderBoardForm );

        // update score and add new lbPax object if not exists in old set
        leaderBoard = updateScoreAndAddNewPax( leaderBoard, leaderBoardForm );

        // making removed pax to inactive
        leaderBoard = markRemovedPaxToInactive( leaderBoard, leaderBoardForm );

        // copy assoation set of pax to transient list for sorting
        leaderBoard = sortLeaderBoardPaxOnSortOrder( leaderBoard );

        // calculating rank
        leaderBoard = calculateRankAndAddToPaxSet( leaderBoard, null );
      }
      else
      {
        // copy pax in to reference pax list
        leaderBoardForm = copyParticipantsToReferenceParticipants( leaderBoardForm );

        // sort pax based on leaderboard sort order
        leaderBoardForm = sortLeaderBoardFormPaxOnSortOrder( leaderBoardForm, leaderBoard );

        // create lb pax objects calculate rank and attach it to leaderboard
        leaderBoard = calculateRankAndAddToPaxSet( leaderBoard, leaderBoardForm );
      }
      LeaderBoard savedLb = getLeaderBoardService().save( leaderBoard );
      leaderBoardForm.setLeaderBoardId( savedLb.getId() + "" );
      leaderBoardForm.setStatus( LeaderBoard.LEADERBOARD_UNDER_CONSTR );
      message = WebErrorMessage.addServerCmd( message );
      if ( LeaderBoardService.PAGE_ADMIN.equals( leaderBoardForm.getSource() ) )
      {
        message.setUrl( RequestUtils.getBaseURI( request ) + PageConstants.LEADERBOARD_LIST_PAGE_REDIRECT_URL );
      }
      else
      {
        message = createURL( leaderBoardForm, message, request );
      }
    }
    else
    {
      if ( message.getFields().size() == 1 )
      {
        message.getFields().clear();
      }
      message = WebErrorMessage.addErrorMessage( message );
    }
    messages.getMessages().add( message );
    super.writeAsJsonToResponse( messages, response );
    return null;

  }

  private void jsonValidateActivityTitle( String activityTitle, WebErrorMessage message )
  {
    if ( StringUtils.isEmpty( activityTitle ) )
    {
      Fields activityTitleFields = new Fields();
      activityTitleFields.setName( FormFieldConstants.LEADERBOARD_FORM_LEADERBOARD_ACTIVITY_TITLE );
      activityTitleFields.setText( CmsResourceBundle.getCmsBundle().getString( "leaderboard.errors.ACTIVITY_TITLE" ) );
      message.setText( CmsResourceBundle.getCmsBundle().getString( "leaderboard.errors.ACTIVITY_TITLE" ) );
      message.getFields().add( activityTitleFields );
    }
  }

  private void jsonValidatePax( int paxCount, WebErrorMessage message )
  {
    if ( paxCount == 0 )
    {
      Fields pax = new Fields();
      pax.setName( FormFieldConstants.LEADERBOARD_FORM_LEADERBOARD_RULES_TEXT );
      pax.setText( CmsResourceBundle.getCmsBundle().getString( "leaderboard.errors.ADD_PARTICIPANTS" ) );
      message.setText( CmsResourceBundle.getCmsBundle().getString( "leaderboard.errors.ADD_PARTICIPANTS" ) );
      message.getFields().add( pax );
    }
  }

  private void jsonValidatePromotionRules( LeaderBoardForm leaderBoardForm, WebErrorMessage message )
  {
    if ( !StringUtil.hasContentInHtml( leaderBoardForm.getLeaderBoardRulesText() ) )
    {
      Fields rulesFields = new Fields();
      rulesFields.setName( FormFieldConstants.LEADERBOARD_FORM_LEADERBOARD_RULES_TEXT );
      rulesFields.setText( CmsResourceBundle.getCmsBundle().getString( "leaderboard.errors.PROMOTION_RULES" ) );
      message.setText( CmsResourceBundle.getCmsBundle().getString( "leaderboard.errors.PROMOTION_RULES" ) );
      message.getFields().add( rulesFields );
    }
  }

  private void jsonValidateLeaderBoardName( String leaderBoardId, String leaderBoardName, WebErrorMessage message )
  {
    boolean isLBNameUnique;
    if ( !StringUtils.isEmpty( leaderBoardId ) )
    {
      isLBNameUnique = getLeaderBoardService().isLeaderBoardNameUnique( leaderBoardName, Long.parseLong( leaderBoardId ) );
    }
    else
    {
      isLBNameUnique = getLeaderBoardService().isLeaderBoardNameUnique( leaderBoardName, null );
    }
    if ( !isLBNameUnique || StringUtils.isEmpty( leaderBoardName ) )
    {
      Fields leaderBoardNameFields = new Fields();
      leaderBoardNameFields.setName( FormFieldConstants.LEADERBOARD_FORM_LEADERBOARD_NAME );
      if ( StringUtils.isEmpty( leaderBoardName ) )
      {
        leaderBoardNameFields.setText( CmsResourceBundle.getCmsBundle().getString( "leaderboard.errors.LB_NAME_REQUIRED" ) );
        message.setText( CmsResourceBundle.getCmsBundle().getString( "leaderboard.errors.LB_NAME_REQUIRED" ) );
      }
      else
      {
        leaderBoardNameFields.setText( CmsResourceBundle.getCmsBundle().getString( "leaderboard.errors.UNIQUE_CONSTRAINT" ) );
        message.setText( CmsResourceBundle.getCmsBundle().getString( "leaderboard.errors.UNIQUE_CONSTRAINT" ) );
      }

      message.getFields().add( leaderBoardNameFields );
    }
  }

  public ActionForward previewLeaderBoard( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    LeaderBoardForm leaderBoardForm = (LeaderBoardForm)form;
    String leaderBoardId = leaderBoardForm.getLeaderBoardId();
    String leaderBoardName = leaderBoardForm.getLeaderBoardName();
    String startDate = leaderBoardForm.getStartDate();
    String endDate = leaderBoardForm.getEndDate();
    String activityTitle = leaderBoardForm.getActivityTitle();
    WebErrorMessageList messages = new WebErrorMessageList();
    WebErrorMessage message = new WebErrorMessage();

    String activityDate = leaderBoardForm.getActivityDate();
    LeaderBoard updatedLeaderBoard = null;
    if ( !StringUtils.isEmpty( leaderBoardForm.getLeaderBoardId() ) )
    {
      updatedLeaderBoard = getLeaderBoardService().getLeaderBoardById( Long.parseLong( leaderBoardForm.getLeaderBoardId() ) );
    }
    if ( StringUtils.isEmpty( startDate ) )
    {
      if ( updatedLeaderBoard != null )
      {
        startDate = DateUtils.toDisplayString( updatedLeaderBoard.getStartDate() );
      }
      else
      {
        startDate = DateUtils.toDisplayString( DateUtils.getCurrentDate() );
      }
    }

    message = validateDates( startDate, endDate, message, leaderBoardId, activityDate );
    jsonValidateLeaderBoardName( leaderBoardId, leaderBoardName, message );
    jsonValidatePromotionRules( leaderBoardForm, message );
    jsonValidateActivityTitle( activityTitle, message );
    jsonValidatePax( leaderBoardForm.getPaxCount(), message );

    if ( message.getFields().size() == 0 )
    {
      for ( LeaderboardParticipantView paxBean : leaderBoardForm.getParticipantsAsList() )
      {
        Long id = paxBean.getId();
        if ( null != id && id.intValue() != 0 )
        {
          leaderBoardForm.getRefPaxs().add( paxBean );
        }
      }
    }
    else
    {
      if ( message.getFields().size() == 1 )
      {
        message.getFields().clear();
      }
      message = WebErrorMessage.addErrorMessage( message );
      leaderBoardForm.setActivityDate( "" );
    }
    if ( leaderBoardForm.isNotifyParticipants() )
    {
      leaderBoardForm.setNotifyPaxChecked( "true" );
    }
    else
    {
      leaderBoardForm.setNotifyPaxChecked( "false" );
    }
    messages.getMessages().add( message );
    super.writeAsJsonToResponse( messages, response );
    return null;
  }

  /*
   * Display create page.
   */
  public ActionForward prepareCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();
    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
    String source = (String)clientStateMap.get( "source" );
    String type = (String)clientStateMap.get( "type" );

    LeaderBoardForm leaderBoardForm = (LeaderBoardForm)request.getAttribute( "leaderBoardForm" );

    // Set client state variables to form bean
    leaderBoardForm.setClientState( clientState );
    leaderBoardForm.setCryptoPassword( cryptoPass );
    leaderBoardForm.setSource( source );
    leaderBoardForm.setType( type );

    List<LeaderboardParticipantView> beans = new ArrayList<LeaderboardParticipantView>();
    String status = leaderBoardForm.getStatus();

    if ( UserManager.getUser().isParticipant() )
    {
      AssociationRequestCollection requestCollection = new AssociationRequestCollection();
      requestCollection.add( new UserAssociationRequest( UserAssociationRequest.NODE ) );
      User loggedInUser = getUserService().getUserByIdWithAssociations( UserManager.getUserId(), requestCollection );
      Collection<Node> nodes = new ArrayList<Node>();
      nodes.addAll( loggedInUser.getUserNodesAsNodes() );

      // Get list of participant in logged in user nodes
      Set<Participant> userNodesPaxList = getParticipantService().getPaxInNodes( nodes, false );

      for ( Participant pax : userNodesPaxList )
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );
        Participant partner = getParticipantService().getParticipantByIdWithAssociations( pax.getId(), associationRequestCollection );
        pax.setUserAddresses( partner.getUserAddresses() );

        LeaderboardParticipantView bean = new LeaderboardParticipantView( pax );
        leaderBoardForm.getBeans().add( bean );
        beans.add( bean );
      }
    }
    request.setAttribute( "leaderBoardForm", leaderBoardForm );
    request.setAttribute( "status", status );
    request.setAttribute( "beans", beans );
    request.setAttribute( "pageName", CmsResourceBundle.getCmsBundle().getString( "leaderboard.label.CREATE_TITLE" ) );
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  }

  public ActionForward prepareCancel( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    LeaderBoardForm leaderBoardForm = (LeaderBoardForm)form;
    String source = leaderBoardForm.getSource();
    WebErrorMessageList messages = new WebErrorMessageList();
    WebErrorMessage message = new WebErrorMessage();
    message = WebErrorMessage.addServerCmd( message );

    if ( LeaderBoardService.PAGE_ADMIN.equals( source ) )
    {
      message.setUrl( RequestUtils.getBaseURI( request ) + PageConstants.LEADERBOARD_LIST_PAGE_REDIRECT_URL );
    }
    else if ( LeaderBoardService.PAGE_DETAIL.equals( source ) )
    {
      message = createURL( leaderBoardForm, message, request );
    }
    else
    {
      message.setUrl( RequestUtils.getBaseURI( request ) + PageConstants.HOME_PAGE_G5_REDIRECT_URL );
    }
    messages.getMessages().add( message );
    super.writeAsJsonToResponse( messages, response );
    return null;

  }

  public ActionForward createOrUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    LeaderBoardForm leaderBoardForm = (LeaderBoardForm)form;
    String leaderBoardId = leaderBoardForm.getLeaderBoardId();
    LeaderBoard leaderBoard = new LeaderBoard();
    WebErrorMessageList messages = new WebErrorMessageList();
    WebErrorMessage message = new WebErrorMessage();

    // update leaderboard if leaderboardid is empty
    if ( !StringUtils.isEmpty( leaderBoardId ) )
    {
      request.getSession().setAttribute( LEADERBOARD_TYPE_KEY, false );

      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new LeaderBoardAssociationRequest( LeaderBoardAssociationRequest.ALL_PAX ) );
      leaderBoard = getLeaderBoardService().getLeaderBoardByIdWithAssociations( Long.parseLong( leaderBoardId ), associationRequestCollection );

      leaderBoard = leaderBoardForm.toDomainObject( leaderBoard );
      leaderBoard.setStatus( LeaderBoard.LEADERBOARD_COMPLETE );
      leaderBoard = getLeaderBoardService().saveRulesCmText( leaderBoard, leaderBoard.getRulescmAssetText() );

      // copy pax in to reference pax list
      leaderBoardForm = copyParticipantsToReferenceParticipants( leaderBoardForm );

      // updating score and attach new leaderboardparticipant object if not exists in old collection
      leaderBoard = updateScoreAndAddNewPax( leaderBoard, leaderBoardForm );

      // making removed pax to inactive
      leaderBoard = markRemovedPaxToInactive( leaderBoard, leaderBoardForm );

      // copy assoation set of pax to transient list for sorting
      leaderBoard = sortLeaderBoardPaxOnSortOrder( leaderBoard );

      // calculating rank
      leaderBoard = calculateRankAndAddToPaxSet( leaderBoard, null );

    }
    // Creating new leaderboard with paxs
    else
    {
      request.getSession().setAttribute( LEADERBOARD_TYPE_KEY, true );
      leaderBoard = leaderBoardForm.toDomainObject( leaderBoard );
      leaderBoard.setStatus( LeaderBoard.LEADERBOARD_COMPLETE );

      User user = getUserService().getUserById( UserManager.getUserId() );
      leaderBoard.setUser( user );

      leaderBoard = getLeaderBoardService().saveRulesCmText( leaderBoard, leaderBoard.getRulescmAssetText() );

      // copy pax in to reference pax list
      leaderBoardForm = copyParticipantsToReferenceParticipants( leaderBoardForm );

      // sort pax based on leaderboard sort order
      leaderBoardForm = sortLeaderBoardFormPaxOnSortOrder( leaderBoardForm, leaderBoard );

      // create lb pax objects calculate rank and attach it to leaderboard
      leaderBoard = calculateRankAndAddToPaxSet( leaderBoard, leaderBoardForm );
    }
    LeaderBoard savedLb = getLeaderBoardService().saveLeaderBoard( leaderBoard );
    leaderBoardForm.setLeaderBoardId( savedLb.getId() + "" );
    message = WebErrorMessage.addServerCmd( message );
    request.getSession().setAttribute( LEADERBOARD_KEY, savedLb );
    if ( LeaderBoardService.PAGE_ADMIN.equals( leaderBoardForm.getSource() ) )
    {
      message.setUrl( RequestUtils.getBaseURI( request ) + PageConstants.LEADERBOARD_LIST_PAGE_REDIRECT_URL );
    }
    else if ( LeaderBoardService.PAGE_DETAIL.equals( leaderBoardForm.getSource() ) )
    {
      message = createURL( leaderBoardForm, message, request );
    }
    else if ( LeaderBoardService.PAGE_TILE.equals( leaderBoardForm.getSource() ) )
    {
      message = createURL( leaderBoardForm, message, request );
    }
    else
    {
      message.setUrl( RequestUtils.getBaseURI( request ) + PageConstants.HOME_PAGE_G5_REDIRECT_URL );
    }
    messages.getMessages().add( message );
    super.writeAsJsonToResponse( messages, response );
    return null;

  }

  public ActionForward prepareEdit( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    LeaderBoardForm leaderBoardForm = (LeaderBoardForm)request.getAttribute( "leaderBoardForm" );
    String clientState = leaderBoardForm.getClientState();
    String cryptoPass = leaderBoardForm.getCryptoPass();
    final String forwardTo = "update_pax";
    if ( StringUtils.isEmpty( cryptoPass ) )
    {
      cryptoPass = leaderBoardForm.getCryptoPassword();
    }
    String source = leaderBoardForm.getSource();
    String type = leaderBoardForm.getType();
    Long leaderBoardId = null;
    if ( StringUtils.isEmpty( clientState ) && StringUtils.isEmpty( cryptoPass ) )
    {
      clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();
      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      source = (String)clientStateMap.get( "source" );

      type = (String)clientStateMap.get( "type" );
      leaderBoardId = (Long)clientStateMap.get( "boardId" );
    }
    else
    {
      leaderBoardForm.setCryptoPass( cryptoPass );
      leaderBoardForm.setCryptoPassword( cryptoPass );
      String password = ClientStatePasswordManager.getPassword();
      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      source = (String)clientStateMap.get( "source" );
      type = (String)clientStateMap.get( "type" );
      leaderBoardId = (Long)clientStateMap.get( "boardId" );

    }
    List<LeaderboardParticipantView> beans = new ArrayList<LeaderboardParticipantView>();
    leaderBoardForm.setSource( source );
    if ( leaderBoardId != null )
    {
      if ( leaderBoardForm.getParticipantsAsList().size() > 0 )
      {
        beans = leaderBoardForm.getParticipantsAsList();
      }
      else
      {

        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new LeaderBoardAssociationRequest( LeaderBoardAssociationRequest.ALL_PAX ) );
        LeaderBoard leaderBoard = getLeaderBoardService().getLeaderBoardByIdWithAssociations( leaderBoardId, associationRequestCollection );
        for ( LeaderBoardParticipant lbp : leaderBoard.getParticipants() )
        {

          leaderBoard.getSortParticipants().add( lbp );
        }
        beans = prepareBeansForPrePopulate( leaderBoard.getSortParticipants(), beans, true, source );
        leaderBoardForm.load( leaderBoard );
      }
      if ( leaderBoardForm.getNotifyPaxChecked() != null || "true".equalsIgnoreCase( leaderBoardForm.getNotifyPaxChecked() ) || StringUtils.isNoneBlank( leaderBoardForm.getNotifyMessage() ) )
      {
        leaderBoardForm.setNotifyParticipants( true );
        leaderBoardForm.setNotifyPaxChecked( "true" );
      }
      else
      {
        leaderBoardForm.setNotifyParticipants( false );
      }
      request.setAttribute( "leaderBoardForm", leaderBoardForm );
      request.setAttribute( "beans", beans );
      request.setAttribute( "status", leaderBoardForm.getStatus() );
      request.setAttribute( "pageName", CmsResourceBundle.getCmsBundle().getString( "leaderboard.label.EDIT_TITLE" ) );

      return mapping.findForward( ActionConstants.UPDATE_FORWARD );
    }

    else
    {
      leaderBoardForm = copyParticipantsToReferenceParticipants( leaderBoardForm );
      for ( LeaderboardParticipantView paxBean : leaderBoardForm.getRefPaxs() )
      {
        beans.add( paxBean );
      }

      leaderBoardForm.setLeaderBoardId( null );
      if ( leaderBoardForm.getNotifyPaxChecked() != null || "true".equalsIgnoreCase( leaderBoardForm.getNotifyPaxChecked() ) || StringUtils.isNoneBlank( leaderBoardForm.getNotifyMessage() ) )
      {
        leaderBoardForm.setNotifyParticipants( true );
        leaderBoardForm.setNotifyPaxChecked( "true" );
      }
      else
      {
        leaderBoardForm.setNotifyParticipants( false );
      }
      leaderBoardForm.setPaxCount( leaderBoardForm.getParticipantsAsList().size() );
      request.setAttribute( "leaderBoardForm", leaderBoardForm );
      request.setAttribute( "beans", beans );
      request.setAttribute( "status", leaderBoardForm.getStatus() );
      
      return mapping.findForward( ActionConstants.UPDATE_FORWARD );
    }
  }

  public ActionForward prepareRemove( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    final String forwardTo = "leaderBoardsList";
    String leaderType = request.getParameter( "leaderType" );
    LeaderBoardForm leaderBoardForm = (LeaderBoardForm)form;
    String[] leaderBoardsToRemove = null;
    if ( leaderType != null && leaderType.equalsIgnoreCase( "progress" ) )
    {
      leaderBoardsToRemove = leaderBoardForm.getDeleteLeaderBoardsProgress();
    }
    else if ( leaderType != null && leaderType.equalsIgnoreCase( "active" ) )
    {
      leaderBoardsToRemove = leaderBoardForm.getDeleteLeaderBoardsActive();
    }
    else if ( leaderType != null && leaderType.equalsIgnoreCase( "live" ) )
    {
      leaderBoardsToRemove = leaderBoardForm.getDeleteLeaderBoardsLive();
    }
    if ( leaderBoardsToRemove != null )
    {
      List leaderBoardIdList = ArrayUtil.convertStringArrayToListOfLongObjects( leaderBoardsToRemove );
      Iterator leaderBoardIdIter = leaderBoardIdList.iterator();

      while ( leaderBoardIdIter.hasNext() )
      {
        LeaderBoard leaderBoardToDelete = getLeaderBoardService().getLeaderBoardById( (Long)leaderBoardIdIter.next() );
        leaderBoardToDelete.setStatus( LeaderBoard.LEADERBOARD_EXPIRED );
        getLeaderBoardService().saveLeaderBoard( leaderBoardToDelete );
      }
    }

    return mapping.findForward( forwardTo );
  }

  public ActionForward copyLeaderBoard( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();
    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
    Long leaderBoardId = (Long)clientStateMap.get( "boardId" );
    String source = (String)clientStateMap.get( "source" );
    String type = (String)clientStateMap.get( "type" );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new LeaderBoardAssociationRequest( LeaderBoardAssociationRequest.ALL_PAX ) );
    LeaderBoard leaderBoard = getLeaderBoardService().getLeaderBoardByIdWithAssociations( leaderBoardId, associationRequestCollection );
    for ( LeaderBoardParticipant lbp : leaderBoard.getParticipants() )
    {
      leaderBoard.getSortParticipants().add( lbp );
    }
    List<LeaderboardParticipantView> beans = new ArrayList<LeaderboardParticipantView>();
    beans = prepareBeansForPrePopulate( leaderBoard.getSortParticipants(), beans, true, source );
    LeaderBoardForm leaderBoardForm = new LeaderBoardForm();
    leaderBoardForm.load( leaderBoard );
    leaderBoardForm.setLeaderBoardId( null );
    leaderBoardForm.setStatus( LeaderBoard.LEADERBOARD_UNDER_CONSTR );
    leaderBoardForm.setSource( source );
    leaderBoardForm.setType( type );
    leaderBoardForm.setClientState( clientState );
    leaderBoardForm.setCryptoPassword( cryptoPass );
    request.setAttribute( "beans", beans );
    request.setAttribute( "leaderBoardForm", leaderBoardForm );
    request.setAttribute( "pageName", CmsResourceBundle.getCmsBundle().getString( "leaderboard.label.COPY_TITLE" ) );
    return mapping.findForward( ActionConstants.EDIT_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward fetchLeaderBoardsForTile( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    LeaderBoardView leaderBoardTileView = getLeaderBoardService().getLeaderBoardsForTile( UserManager.getUserId() );
    super.writeAsJsonToResponse( leaderBoardTileView, response );
    return null;
  }

  public ActionForward getUserRole( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Set roles = new HashSet();
    roles.add( AuthorizationService.ROLE_CODE_BI_ADMIN );
    roles.add( MANAGER.toUpperCase() );
    boolean isManager = getAuthorizationService().isUserInRole( null, roles, null );

    if ( isManager )
    {
      request.setAttribute( "role", MANAGER );
    }
    else
    {
      request.setAttribute( "role", PARTICIPANT );
    }
    // get session attributes
    LeaderBoard lb = (LeaderBoard)request.getSession().getAttribute( LEADERBOARD_KEY );
    Boolean lbNewlyCreated = (Boolean)request.getSession().getAttribute( LEADERBOARD_TYPE_KEY );

    // move to request
    request.setAttribute( LEADERBOARD_KEY, lb );
    request.setAttribute( LEADERBOARD_TYPE_KEY, lbNewlyCreated );

    // remove from session
    request.getSession().removeAttribute( LEADERBOARD_KEY );
    request.getSession().removeAttribute( LEADERBOARD_TYPE_KEY );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward fetchLeaderBoardsForDetailPage( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String nameId = request.getParameter( "nameId" );
    LeaderBoardView leaderBoardDetailPageView = getLeaderBoardService().getLeaderBoardsForDetailUsingNameId( UserManager.getUserId(), nameId );
    super.writeAsJsonToResponse( leaderBoardDetailPageView, response );
    return null;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward fetchLeaderBoardByIdForDetail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String boardId = request.getParameter( "id" );
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new LeaderBoardAssociationRequest( LeaderBoardAssociationRequest.ALL_PAX ) );
    LeaderBoard leaderBoard = getLeaderBoardService().getLeaderBoardJsonForDetail( Long.parseLong( boardId ), associationRequestCollection );
    LeaderBoardSingleView view = new LeaderBoardSingleView( leaderBoard );
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  public ActionForward fetchLeaderBoardByIdForTile( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String boardId = request.getParameter( "id" );
    LeaderBoard leaderBoard = getLeaderBoardService().getLeaderBoardJsonForTile( Long.parseLong( boardId ) );
    LeaderBoardSingleView view = new LeaderBoardSingleView( leaderBoard );
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  public ActionForward processPreview( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    final String forwardTo = "preview";

    LeaderBoardForm leaderBoardForm = (LeaderBoardForm)form;
    leaderBoardForm = copyParticipantsToReferenceParticipants( leaderBoardForm );

    if ( leaderBoardForm.isNotifyParticipants() )
    {
      leaderBoardForm.setNotifyPaxChecked( "true" );
    }
    else
    {
      leaderBoardForm.setNotifyPaxChecked( "false" );
      leaderBoardForm.setNotifyMessage( "" );
    }

    LeaderBoardNewScoreComparator comparator = new LeaderBoardNewScoreComparator();
    comparator.setSortBy( leaderBoardForm.getSortOrder() );
    Collections.sort( leaderBoardForm.getRefPaxs(), comparator );
    request.setAttribute( "leaderBoardForm", leaderBoardForm );

    // set the role in the scope
    request.setAttribute( "isAdmin", isAdmin() );
    request.setAttribute( "isDelegate", isDelegate() );

    return mapping.findForward( forwardTo );
  }

  public ActionForward prepareBack( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    LeaderBoardForm leaderBoardForm = (LeaderBoardForm)form;
    String source = leaderBoardForm.getSource();
    if ( LeaderBoardService.PAGE_ADMIN.equals( source ) )
    {
      return mapping.findForward( LeaderBoardService.PAGE_ADMIN );
    }
    else if ( LeaderBoardService.PAGE_DETAIL.equals( source ) )
    {
      WebErrorMessage message = new WebErrorMessage();
      message = createURL( leaderBoardForm, message, request );
      response.sendRedirect( message.getUrl() );
      return null;
    }
    else
    {
      return mapping.findForward( LeaderBoardService.PAGE_TILE );
    }
  }

  private List<LeaderboardParticipantView> prepareBeansForPrePopulate( List<LeaderBoardParticipant> lbPaxs, List<LeaderboardParticipantView> beans, boolean isFirstTime, String source )
  {
    for ( LeaderBoardParticipant lbp : lbPaxs )
    {
      LeaderboardParticipantView bean = new LeaderboardParticipantView( lbp.getUser() );
      bean.setScore( Integer.parseInt( lbp.getScore() ) );

      if ( isFirstTime || LeaderBoardService.PAGE_ADMIN.equals( source ) )
      {
        bean.setNewScore( Integer.parseInt( lbp.getScore() ) );
      }
      beans.add( bean );
    }
    java.util.Collections.sort( beans, LBPaxNameSort );
    return beans;
  }

  private static Comparator<LeaderboardParticipantView> LBPaxNameSort = new Comparator<LeaderboardParticipantView>()
  {
    public int compare( LeaderboardParticipantView pax1, LeaderboardParticipantView pax2 )
    {
      String pax1FullName = pax1.getFullName().toUpperCase();
      String pax2FullName = pax2.getFullName().toUpperCase();
      return pax1FullName.compareTo( pax2FullName );
    }
  };

  private WebErrorMessage validateDates( String startDate, String endDate, WebErrorMessage message, String leaderBoardId, String activityDate )
  {
    if ( !StringUtils.isEmpty( endDate ) )
    {
      Fields leaderBoardEndDateFields = new Fields();
      if ( DateUtils.toDate( endDate ).before( DateUtils.toStartDate( DateUtils.getCurrentDate() ) ) )
      {
        leaderBoardEndDateFields.setName( FormFieldConstants.GLOBAL_FORM_END_DATE );
        leaderBoardEndDateFields.setText( CmsResourceBundle.getCmsBundle().getString( "system.errors.END_BEFORE_TO_DATE" ) );
        message.setText( CmsResourceBundle.getCmsBundle().getString( "system.errors.END_BEFORE_TO_DATE" ) );
        message.getFields().add( leaderBoardEndDateFields );
      }
      else
      {
        if ( DateUtils.toDate( endDate ).before( DateUtils.toDate( startDate ) ) )
        {
          leaderBoardEndDateFields.setName( FormFieldConstants.GLOBAL_FORM_END_DATE );
          leaderBoardEndDateFields.setText( CmsResourceBundle.getCmsBundle().getString( "system.errors.END_BEFORE_START_DATE" ) );
          message.setText( CmsResourceBundle.getCmsBundle().getString( "system.errors.END_BEFORE_START_DATE" ) );
          message.getFields().add( leaderBoardEndDateFields );
        }
      }
    }
    return message;
  }

  private WebErrorMessage createURL( LeaderBoardForm leaderBoardForm, WebErrorMessage message, HttpServletRequest request )
  {
    Date currentDate;
    currentDate = DateUtils.getCurrentDateTrimmed();
    if ( !StringUtil.isEmpty( leaderBoardForm.getLeaderBoardId() ) )
    {
      String appendToURL = "";
      if ( LeaderBoard.LEADERBOARD_UNDER_CONSTR.equals( leaderBoardForm.getStatus() ) )
      {
        appendToURL = "#set/pending/";
      }
      else
      {
        if ( StringUtil.isEmpty( leaderBoardForm.getEndDate() ) )
        {
          appendToURL = "#set/active/";
        }
        else
        {
          if ( DateUtils.toDate( leaderBoardForm.getEndDate() ).before( currentDate ) )
          {
            appendToURL = "#set/archived/";
          }
          else
          {
            appendToURL = "#set/active/";
          }
        }
      }
      message.setUrl( RequestUtils.getBaseURI( request ) + PageConstants.LEADERBOARD_DETAIL_PAGE_REDIRECT_URL + appendToURL + leaderBoardForm.getLeaderBoardId() );
    }
    else
    {
      message.setUrl( RequestUtils.getBaseURI( request ) + PageConstants.LEADERBOARD_DETAIL_PAGE_REDIRECT_URL );
    }
    return message;
  }

  private LeaderBoard updateScoreAndAddNewPax( LeaderBoard leaderBoard, LeaderBoardForm leaderBoardForm )
  {
    for ( LeaderboardParticipantView newLBPax : leaderBoardForm.getRefPaxs() )
    {
      boolean paxFoundInExistingList = false;
      for ( LeaderBoardParticipant existingPaxToUpdate : leaderBoard.getParticipants() )
      {
        if ( newLBPax.getId() == existingPaxToUpdate.getUser().getId().longValue() )
        {
          existingPaxToUpdate.setAsOfDate( new Date() );
          existingPaxToUpdate.setScore( newLBPax.getNewScore().toString() );
          paxFoundInExistingList = true;
        }
      }
      if ( !paxFoundInExistingList )
      {
        LeaderBoardParticipant newPaxToInsert = new LeaderBoardParticipant();
        newPaxToInsert.setActive( true );
        if ( !StringUtil.isEmpty( leaderBoardForm.getActivityDate() ) )
        {
          newPaxToInsert.setAsOfDate( DateUtils.toDate( leaderBoardForm.getActivityDate() ) );
        }
        else
        {
          newPaxToInsert.setAsOfDate( new Date() );
        }
        newPaxToInsert.setScore( newLBPax.getNewScore().toString() );
        newPaxToInsert.setLeaderboard( leaderBoard );
        Participant user = getParticipantService().getParticipantById( newLBPax.getId() );
        newPaxToInsert.setUser( user );
        leaderBoard.getParticipants().add( newPaxToInsert );
      }
    }
    return leaderBoard;
  }

  private LeaderBoard markRemovedPaxToInactive( LeaderBoard leaderBoard, LeaderBoardForm leaderBoardForm )
  {
    for ( LeaderBoardParticipant existingPax : leaderBoard.getParticipants() )
    {
      boolean paxFoundInNewList = false;
      for ( LeaderboardParticipantView newPax : leaderBoardForm.getRefPaxs() )
      {
        if ( existingPax.getUser().getId().longValue() == newPax.getId() )
        {
          paxFoundInNewList = true;
        }
      }
      if ( !paxFoundInNewList )
      {
        existingPax.setActive( false );
      }
    }
    return leaderBoard;
  }

  private LeaderBoard sortLeaderBoardPaxOnSortOrder( LeaderBoard leaderBoard )
  {
    for ( LeaderBoardParticipant lbp : leaderBoard.getParticipants() )
    {
      if ( lbp.isActive() )
      {
        leaderBoard.getSortParticipants().add( lbp );
      }
    }
    if ( leaderBoard.getSortOrder().equals( SORT_DESCENDING ) )
    {
      Collections.sort( leaderBoard.getSortParticipants(), DESC_COMPARATOR_LBP );
    }
    else
    {
      Collections.sort( leaderBoard.getSortParticipants(), ASCE_COMPARATOR_LBP );
    }
    return leaderBoard;
  }

  class LeaderBoardNewScoreComparator implements Comparator
  {
    String sortBy;

    public int compare( Object o1, Object o2 )
    {
      if ( ! ( o1 instanceof LeaderboardParticipantView ) || ! ( o2 instanceof LeaderboardParticipantView ) )
      {
        throw new ClassCastException( "Object is not a LeaderboardParticipantView object!" );
      }
      Integer newScore1 = ( (LeaderboardParticipantView)o1 ).getNewScore();
      Integer newScore2 = ( (LeaderboardParticipantView)o2 ).getNewScore();

      if ( this.getSortBy().equals( SORT_DESCENDING ) )
      {
        return newScore1.compareTo( newScore2 ) * -1;
      }
      else
      {
        return newScore1.compareTo( newScore2 );
      }

    }

    public String getSortBy()
    {
      return sortBy;
    }

    public void setSortBy( String sortBy )
    {
      this.sortBy = sortBy;
    }

  }

  private LeaderBoardForm sortLeaderBoardFormPaxOnSortOrder( LeaderBoardForm leaderBoardForm, LeaderBoard leaderBoard )
  {
    if ( leaderBoard.getSortOrder().equals( SORT_DESCENDING ) )
    {
      Collections.sort( leaderBoardForm.getRefPaxs(), DESC_COMPARATOR );
    }
    else
    {
      Collections.sort( leaderBoardForm.getRefPaxs(), ASCE_COMPARATOR );
    }
    return leaderBoardForm;
  }

  private LeaderBoard calculateRankAndAddToPaxSet( LeaderBoard leaderBoard, LeaderBoardForm leaderBoardForm )
  {
    // while updating form will be null
    if ( leaderBoardForm == null )
    {
      // calculating rank and assign it to attached set objects
      long rank = 1;
      long sameRankCount = 0;
      LeaderBoardParticipant previousPax = null;
      for ( LeaderBoardParticipant currentPax : leaderBoard.getSortParticipants() )
      {
        if ( previousPax != null && !previousPax.getScore().equals( currentPax.getScore() ) )
        {
          rank++;
          rank = rank + sameRankCount;
          sameRankCount = 0;
        }
        else if ( previousPax != null && previousPax.getScore().equals( currentPax.getScore() ) )
        {
          sameRankCount++;
        }
        for ( LeaderBoardParticipant lbp : leaderBoard.getParticipants() )
        {
          if ( currentPax.getUser().getId().equals( lbp.getUser().getId() ) )
          {
            lbp.setParticipantRank( rank );
            break;
          }
        }
        previousPax = currentPax;
      }
    }
    else
    {
      long rank = 1;
      long sameRankCount = 0;
      LeaderboardParticipantView previousPax = null;
      for ( LeaderboardParticipantView currentPax : leaderBoardForm.getRefPaxs() )
      {
        LeaderBoardParticipant newLBPaxToInsert = new LeaderBoardParticipant();

        if ( previousPax != null && previousPax.getNewScore() != null && currentPax.getNewScore() != null && previousPax.getNewScore().intValue() != currentPax.getNewScore().intValue() )
        {
          rank++;
          rank = rank + sameRankCount;
          sameRankCount = 0;
        }
        else if ( previousPax != null && previousPax.getNewScore() != null && currentPax.getNewScore() != null && previousPax.getNewScore().intValue() == currentPax.getNewScore().intValue() )
        {
          sameRankCount++;
        }

        if ( !StringUtil.isEmpty( leaderBoardForm.getActivityDate() ) )
        {
          newLBPaxToInsert.setAsOfDate( DateUtils.toDate( leaderBoardForm.getActivityDate() ) );
        }
        else
        {
          newLBPaxToInsert.setAsOfDate( new Date() );
        }
        newLBPaxToInsert.setLeaderboard( leaderBoard );
        newLBPaxToInsert.setActive( true );
        newLBPaxToInsert.setScore( currentPax.getNewScore().toString() );
        Participant pax = getParticipantService().getParticipantById( currentPax.getId() );
        newLBPaxToInsert.setUser( pax );
        newLBPaxToInsert.setParticipantRank( rank );
        leaderBoard.getParticipants().add( newLBPaxToInsert );
        previousPax = currentPax;
      }
    }
    return leaderBoard;
  }

  private LeaderBoardForm copyParticipantsToReferenceParticipants( LeaderBoardForm leaderBoardForm )
  {
    for ( LeaderboardParticipantView lbPax : leaderBoardForm.getParticipantsAsList() )
    {
      Long id = lbPax.getId();
      if ( null != id && id.intValue() != 0 )
      {
        leaderBoardForm.getRefPaxs().add( lbPax );
      }
    }
    return leaderBoardForm;
  }

  private static Comparator<LeaderboardParticipantView> ASCE_COMPARATOR = new Comparator<LeaderboardParticipantView>()
  {
    public int compare( LeaderboardParticipantView lbp1, LeaderboardParticipantView lbp2 )
    {
      return lbp1.getNewScore() - lbp2.getNewScore();
    }
  };

  private static Comparator<LeaderboardParticipantView> DESC_COMPARATOR = new Comparator<LeaderboardParticipantView>()
  {
    public int compare( LeaderboardParticipantView lbp1, LeaderboardParticipantView lbp2 )
    {
      return lbp2.getNewScore() - lbp1.getNewScore();
    }
  };

  private static Comparator<LeaderBoardParticipant> ASCE_COMPARATOR_LBP = new Comparator<LeaderBoardParticipant>()
  {
    public int compare( LeaderBoardParticipant lbp1, LeaderBoardParticipant lbp2 )
    {
      return Integer.parseInt( lbp1.getScore() ) - Integer.parseInt( lbp2.getScore() );
    }
  };

  private static Comparator<LeaderBoardParticipant> DESC_COMPARATOR_LBP = new Comparator<LeaderBoardParticipant>()
  {
    public int compare( LeaderBoardParticipant lbp1, LeaderBoardParticipant lbp2 )
    {
      return Integer.parseInt( lbp2.getScore() ) - Integer.parseInt( lbp1.getScore() );
    }
  };

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private LeaderBoardService getLeaderBoardService()
  {
    return (LeaderBoardService)getService( LeaderBoardService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)getService( AuthorizationService.BEAN_NAME );
  }
}
