/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/RecognitionHistoryListController.java,v $
 */

package com.biperf.core.ui.claim;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.dao.claim.hibernate.JournalClaimGroupQueryConstraint;
import com.biperf.core.dao.claim.hibernate.JournalClaimQueryConstraint;
import com.biperf.core.dao.claim.hibernate.NominationClaimQueryConstraint;
import com.biperf.core.dao.claim.hibernate.RecognitionClaimQueryConstraint;
import com.biperf.core.dao.journal.hibernate.JournalQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.NominationPromotionQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.RecognitionPromotionQueryConstraint;
import com.biperf.core.domain.activity.MerchandiseActivity;
import com.biperf.core.domain.claim.ApprovableItemApprover;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.NominationAwardGroupSizeType;
import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.journal.ActivityJournal;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.process.NominationAutoNotificationProcess;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimGroupAssociationRequest;
import com.biperf.core.service.claim.ClaimGroupService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.journal.impl.JournalAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.biperf.util.StringUtils;

/**
 * Pax's view of trx history for Recognitions or Nominations.
 * 
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
 * <td>zahler</td>
 * <td>Aug 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 *          20:42:19 ernste Exp $
 */
@SuppressWarnings( { "rawtypes", "unchecked" } )
public class RecognitionHistoryListController extends BaseController
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * If <code>mode</code> is SENT_MODE, then the Nomination/Recognition
   * History page shows nominations submitted/recognitions sent.
   */
  public static final String SENT_MODE = "sent";

  /**
   * If <code>mode</code> is RECEIVED_MODE, then the Nomination/Recognition
   * History page shows nominations won/recognitions received.
   */
  public static final String RECEIVED_MODE = "received";

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Fetches data for the Nomination History page and the Recognition History
   * page.
   * 
   * @param tileContext
   *            the context for the tile associated with this controller.
   * @param request
   *            the HTTP request we are processing.
   * @param response
   *            the HTTP response we are creating.
   * @param servletContext
   *            the context for servlets of this web application.
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    RequestWrapper requestWrapper = new RequestWrapper( request );

    // Get parameters.
    RecognitionHistoryForm form = requestWrapper.getRecognitionHistoryForm();

    String promotionTypeCode = "";
    String mode = "";
    Long submitterId = null;
    Long proxyUserId = null;
    Long promotionId = null;
    try
    {
      String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
      if ( clientState != null && clientState.length() > 0 )
      {
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();

        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        // Deserialize the client state.
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        promotionTypeCode = (String)clientStateMap.get( "promotionTypeCode" );
        mode = (String)clientStateMap.get( "mode" );
        try
        {
          submitterId = (Long)clientStateMap.get( "submitterId" );
        }
        catch( ClassCastException cce )
        {
          submitterId = new Long( (String)clientStateMap.get( "submitterId" ) );
        }
        try
        {
          proxyUserId = (Long)clientStateMap.get( "proxyUserId" );
        }
        catch( ClassCastException cce )
        {
          String temp = (String)clientStateMap.get( "proxyUserId" );
          if ( !StringUtils.isEmpty( temp ) )
          {
            proxyUserId = new Long( temp );
          }
        }
        try
        {
          promotionId = (Long)clientStateMap.get( "promotionId" );
        }
        catch( ClassCastException cce )
        {
          if ( ! ( (String)clientStateMap.get( "promotionId" ) ).startsWith( "all" ) )
          {
            promotionId = new Long( (String)clientStateMap.get( "promotionId" ) );
          }
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    if ( submitterId == null )
    {
      submitterId = UserManager.getUser().getUserId();
    }
    if ( StringUtils.isEmpty( promotionTypeCode ) )
    {
      promotionTypeCode = requestWrapper.getPromotionTypeCode();
    }
    if ( StringUtils.isEmpty( mode ) )
    {
      mode = requestWrapper.getMode();
    }
    Date startDate = requestWrapper.getStartDate();
    Date endDate = requestWrapper.getEndDate();

    // Setup the page.
    form.setSubmitterId( submitterId );
    form.setProxyUserId( proxyUserId );
    form.setPromotionTypeCode( promotionTypeCode );
    promotionId = (Long)request.getAttribute( "promotionId" );
    mode = (String)request.getAttribute( "mode" );
    promotionTypeCode = (String)request.getAttribute( "promotionTypeCode" );
    form.setMode( mode );

    request.setAttribute( "submitterName", getParticipantName( submitterId ) );

    // Setup the search criteria form.
    Date formStartDate = startDate != null ? startDate : getDefaultStartDate();
    form.setStartDate( DateUtils.toDisplayString( formStartDate ) );

    Date formEndDate = endDate != null ? endDate : getDefaultEndDate();
    // make sure end date has the maximum time of the day 23:59:59
    Date endDateMaxTime = formEndDate;
    endDateMaxTime = DateUtils.toEndDate( formEndDate );

    form.setEndDate( DateUtils.toDisplayString( formEndDate ) );
    // Bug fix # 22831
    List promotionList = getPromotionList( promotionTypeCode );
    PropertyComparator.sort( promotionList, new MutableSortDefinition( "name", false, true ) );
    requestWrapper.setAttribute( "promotionList", promotionList );

    request.setAttribute( "JstlDatePattern", DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
    if ( promotionId == null && form.getPromotionId() != null && form.getPromotionId().longValue() != 0 )
    { // promotionId might come from the DDL on the form that was
      // submitted
      promotionId = form.getPromotionId();
    }
    else
    {
      form.setPromotionId( promotionId );
    }
    try
    {
      if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) )
      {
        if ( mode.equals( RECEIVED_MODE ) )
        {

          Integer recogntionReceivedCount = getClaimService().getPublicRecognitionClaimsReceivedbyUserId( promotionId,
                                                                                                          form.getSubmitterId(),
                                                                                                          DateUtils.toStartDate( form.getStartDate() ),
                                                                                                          DateUtils.toEndDate( form.getEndDate() ),
                                                                                                          PromotionType.RECOGNITION,
                                                                                                          ApprovalStatusType.APPROVED );
          request.setAttribute( "count", recogntionReceivedCount );

        }
        else if ( mode.equals( SENT_MODE ) )
        {
          Integer recogntionSentCount = getClaimService().getPublicRecognitionClaimsSentByUserId( promotionId,
                                                                                                  form.getSubmitterId(),
                                                                                                  DateUtils.toStartDate( form.getStartDate() ),
                                                                                                  DateUtils.toEndDate( form.getEndDate() ),
                                                                                                  PromotionType.RECOGNITION,
                                                                                                  ApprovalStatusType.APPROVED );
          request.setAttribute( "count", recogntionSentCount );
        }
      }

    }
    catch( ParseException e )
    {

    }

    // Setup the history list.
    ValueObjectBuilder builder = null;

    if ( promotionTypeCode.equals( PromotionType.NOMINATION ) )
    {
      if ( mode.equals( RECEIVED_MODE ) )
      {
        builder = new NominationReceiveBuilder( form.getSubmitterId(), form.getPromotionId(), DateUtils.toDate( form.getStartDate() ), endDateMaxTime );
      }
      else if ( mode.equals( SENT_MODE ) )
      {
        builder = new NominationSentBuilder( form.getSubmitterId(), form.getProxyUserId(), form.getPromotionId(), DateUtils.toDate( form.getStartDate() ), endDateMaxTime );
      }
    }
    else if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) )
    {
      if ( mode.equals( RECEIVED_MODE ) )
      {
        builder = new RecognitionReceiveBuilder( form.getSubmitterId(), form.getPromotionId(), DateUtils.toDate( form.getStartDate() ), endDateMaxTime );
      }
      else if ( mode.equals( SENT_MODE ) )
      {
        builder = new RecognitionSentBuilder( form.getSubmitterId(), form.getProxyUserId(), form.getPromotionId(), DateUtils.toDate( form.getStartDate() ), endDateMaxTime );
      }
    }
    if ( builder != null && builder.buildValueObjects() != null )
    {
      for ( Iterator valueObjectster = builder.buildValueObjects().iterator(); valueObjectster.hasNext(); )
      {
        RecognitionHistoryValueObject historyValueObject = (RecognitionHistoryValueObject)valueObjectster.next();
        if ( historyValueObject.isReversal() )
        {
          request.setAttribute( "toShowRCopy", "Yes" );
          break;
        }
      }
      request.setAttribute( "valueObjects", builder.buildValueObjects() );
    }
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the default end date.
   * 
   * @return the default end date.
   */
  private Date getDefaultEndDate()
  {
    return DateUtils.toEndDate( DateUtils.getCurrentDate() );
  }

  /**
   * Returns the default start date.
   * 
   * @return the default start date.
   */
  private Date getDefaultStartDate()
  {
    SystemVariableService systemVariableService = (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
    Date launchDate = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_LAUNCH_DATE ).getDateVal();
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.add( GregorianCalendar.MONTH, -1 );
    Date todayMinusMonth = DateUtils.toStartDate( calendar.getTime() );

    return todayMinusMonth.after( launchDate ) ? todayMinusMonth : launchDate;
  }

  /**
   * Returns the name of the specified participant, or null if the participant
   * ID is null or specifies no participant.
   * 
   * @param participantId
   *            identifies a participant.
   * @return the name of the specified participant, or null if the participant
   *         ID is null or specifies no participant.
   */
  private String getParticipantName( Long participantId )
  {
    String participantName = null;

    Participant participant = getParticipantService().getParticipantById( participantId );
    if ( participant != null )
    {
      participantName = participant.getNameLFMWithComma();
    }

    return participantName;
  }

  /**
   * Returns a list of live and expired promotions.
   * 
   * @param promotionTypeCode
   *            the abstract recognition type: nomination or recognition.
   * @return a list of live and expired promotions.
   */
  private List getPromotionList( String promotionTypeCode )
  {
    PromotionQueryConstraint promotionQueryConstraint = promotionTypeCode.equals( PromotionType.NOMINATION )
        ? (PromotionQueryConstraint)new NominationPromotionQueryConstraint()
        : (PromotionQueryConstraint)new RecognitionPromotionQueryConstraint();

    promotionQueryConstraint
        .setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ), PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );

    return getPromotionService().getPromotionList( promotionQueryConstraint );
  }

  // ---------------------------------------------------------------------------
  // Service Getter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the participant service.
   * 
   * @return a reference to the participant service.
   */
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  /**
   * Returns the promotion service.
   * 
   * @return a reference to the promotion service.
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  // ---------------------------------------------------------------------------
  // Inner Classes
  // ---------------------------------------------------------------------------

  /**
   * Builds value objects used by the Nominations Received list on the
   * Nomination History page.
   */
  public static class NominationReceiveBuilder implements ValueObjectBuilder
  {
    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private Long recipientId;

    private Long promotionId;

    private Date startDate;

    private Date endDate;

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    /**
     * Constructs a <code>NominationReceiveBuilder</code> object.
     * 
     * @param recipientId
     *            the ID of the user who is the nomination recipient.
     * @param promotionId
     *            if specified, return claims for only this promotion.
     * @param startDate
     *            if specified, return claims that were submitted on or
     *            after this date.
     * @param endDate
     *            if specified, return claims that were submitted on or
     *            before this date.
     */
    public NominationReceiveBuilder( Long recipientId, Long promotionId, Date startDate, Date endDate )
    {
      this.recipientId = recipientId;
      this.promotionId = promotionId;
      this.startDate = startDate;
      this.endDate = endDate;
    }

    /**
     * Returns information about the specified nomination claims.
     * 
     * @return information about the specified nomination claims, as a
     *         <code>List</code> of {@link RecognitionHistoryValueObject}
     *         objects.
     */
    public List buildValueObjects()
    {
      List valueObjects = new ArrayList();
      String awardGroupSizeType = null;
      // Get information about nominations received where claims are
      // approved cumulatively.
      List claimGroupList = getClaimGroupList( JournalStatusType.POST );
      for ( Iterator iter = claimGroupList.iterator(); iter.hasNext(); )
      {
        ClaimGroup claimGroup = (ClaimGroup)iter.next();

        if ( awardGroupSizeType == null )
        {
          NominationPromotion nominationPromotion = (NominationPromotion)claimGroup.getPromotion();
          if ( nominationPromotion.getAwardGroupSizeType() != null )
          {
            awardGroupSizeType = nominationPromotion.getAwardGroupSizeType().getCode();
          }
        }

        RecognitionHistoryValueObject valueObject = new RecognitionHistoryValueObject( claimGroup );

        // Check if any of the claims have been reversed, mark the value object as reversed
        Iterator<NominationClaim> claimIt = claimGroup.getClaims().iterator();
        while ( claimIt.hasNext() )
        {
          NominationClaim nomClaim = claimIt.next();
          if ( nomClaim.isReversal() )
          {
            valueObject.setReversal( true );
            // Independent claim will search for journal ID here... cumulative sets it at value
            // object creation
            break;
          }
        }

        valueObjects.add( valueObject );

      }

      // Get information about nominations received where claims are
      // approved independently.
      List claimList = getClaimList( JournalStatusType.POST, awardGroupSizeType );
      for ( Iterator iter = claimList.iterator(); iter.hasNext(); )
      {
        NominationClaim claim = (NominationClaim)iter.next();
        if ( claim.getClaimRecipients() != null )
        {
          for ( Iterator claimRecipientIter = claim.getClaimRecipients().iterator(); claimRecipientIter.hasNext(); )
          {
            ClaimRecipient claimRecipient = (ClaimRecipient)claimRecipientIter.next();

            Set<ApprovableItemApprover> approvableItemApprovers = claimRecipient.getApprovableItemApprovers();
            for ( ApprovableItemApprover item : approvableItemApprovers )
            {
              if ( this.recipientId.equals( claimRecipient.getRecipient().getId() ) )
              {
                RecognitionHistoryValueObject valueObject = new RecognitionHistoryValueObject( claim, item, claimRecipient );
                if ( claim.isReversal() )
                {
                  Long journalId = getJournalService().getJournalIdForReversedClaim( claim.getId(), recipientId, valueObject.getApprovalRound() );
                  valueObject.setJournalId( journalId );
                }
                valueObjects.add( valueObject );
              }
            }

          }
        }
      }
      // Get information about non claim related nominations
      valueObjects.addAll( getNonClaimRelatedDeposits( this.recipientId ) );
      return valueObjects;
    }
    // -------------------------------------------------------------------------
    // Private Methods
    // -------------------------------------------------------------------------

    private boolean isPrivateUntil( Date notificationDate )
    {

      if ( notificationDate != null )
      {
        // Add Notification process time if any so that if today is the notification day, we don't
        // send
        // until the time of day of the process is passed.
        Date now = new Date();
        Long timeOfDayMillis = getProcessService().getTimeOfDayMillisOfFirstSchedule( NominationAutoNotificationProcess.BEAN_NAME );
        if ( timeOfDayMillis == null )
        {
          timeOfDayMillis = new Long( 0 );
        }

        Date processingDateWithTime = new Date( notificationDate.getTime() + timeOfDayMillis.longValue() );
        if ( now.before( processingDateWithTime ) )
        {
          return false;
        }
      }

      return true;
    }

    /**
     * Builds a {@link JournalClaimQueryConstraint} object that selects
     * {@link Claim} objects.
     * 
     * @param awardGroupType
     *            either "individual" or "team."
     * @return a {@link JournalClaimQueryConstraint} object that selects
     *         {@link Claim} objects.
     */
    private JournalClaimQueryConstraint buildJournalClaimQueryConstraint( NominationAwardGroupType awardGroupType, String journalStatusType, NominationAwardGroupSizeType awardGroupSizeType )
    {
      JournalClaimQueryConstraint queryConstraint = new JournalClaimQueryConstraint( awardGroupType, awardGroupSizeType );

      queryConstraint.setRecipientId( recipientId );
      queryConstraint.setJournalStatusType( journalStatusType );

      if ( promotionId != null && promotionId.longValue() != 0 )
      {
        queryConstraint.setPromotionId( promotionId );
      }

      if ( startDate != null )
      {
        queryConstraint.setApprovalStartDate( startDate );
      }

      if ( endDate != null )
      {
        queryConstraint.setApprovalEndDate( endDate );
      }

      return queryConstraint;
    }

    /**
     * Returns the claim groups specified by the state of this object.
     * 
     * @return the claim groups specified by the state of this object, as a
     *         <code>List</code> of {@link ClaimGroup} objects.
     */
    private List getClaimGroupList( String journalStatusType )
    {
      // Setup the query constraint.
      JournalClaimGroupQueryConstraint queryConstraint = new JournalClaimGroupQueryConstraint();

      queryConstraint.setParticipantId( recipientId );
      queryConstraint.setJournalStatusType( journalStatusType );

      if ( promotionId != null && promotionId.longValue() != 0 )
      {
        queryConstraint.setPromotionId( promotionId );
      }

      if ( startDate != null )
      {
        queryConstraint.setApprovalStartDate( startDate );
      }

      if ( endDate != null )
      {
        queryConstraint.setApprovalEndDate( endDate );
      }

      // Setup the association request collection.
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ClaimGroupAssociationRequest( ClaimGroupAssociationRequest.CLAIMS ) );

      // Get the claim group list.
      return getClaimGroupService().getClaimGroupList( queryConstraint, associationRequestCollection );
    }

    /**
     * Returns the claims specified by the state of this object.
     * 
     * @return the claims specified by the state of this object, as a
     *         <code>List</code> of {@link NominationClaim} objects.
     */
    private List getClaimList( String journalStatusType, String awardGroupSizeType )
    {
      List claimList = new ArrayList();

      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );

      ClaimService claimService = getClaimService();

      claimList.addAll( claimService.getClaimList( buildJournalClaimQueryConstraint( NominationAwardGroupType.lookup( NominationAwardGroupType.INDIVIDUAL ), journalStatusType, null ),
                                                   associationRequestCollection ) );

      // If size type is provided, use it. Otherwise, get results for all size type combinations as
      // well
      if ( awardGroupSizeType != null )
      {
        claimList.addAll( claimService.getClaimList( buildJournalClaimQueryConstraint( NominationAwardGroupType.lookup( NominationAwardGroupType.INDIVIDUAL_OR_TEAM ),
                                                                                       journalStatusType,
                                                                                       NominationAwardGroupSizeType.lookup( awardGroupSizeType ) ),
                                                     associationRequestCollection ) );

        claimList.addAll( claimService.getClaimList( buildJournalClaimQueryConstraint( NominationAwardGroupType.lookup( NominationAwardGroupType.TEAM ),
                                                                                       journalStatusType,
                                                                                       NominationAwardGroupSizeType.lookup( awardGroupSizeType ) ),
                                                     associationRequestCollection ) );
      }
      else
      {
        claimList.addAll( claimService.getClaimList(
                                                     buildJournalClaimQueryConstraint( NominationAwardGroupType.lookup( NominationAwardGroupType.INDIVIDUAL_OR_TEAM ),
                                                                                       journalStatusType,
                                                                                       NominationAwardGroupSizeType.lookup( NominationAwardGroupSizeType.LIMITED ) ),
                                                     associationRequestCollection ) );

        claimList.addAll( claimService.getClaimList(
                                                     buildJournalClaimQueryConstraint( NominationAwardGroupType.lookup( NominationAwardGroupType.INDIVIDUAL_OR_TEAM ),
                                                                                       journalStatusType,
                                                                                       NominationAwardGroupSizeType.lookup( NominationAwardGroupSizeType.UNLIMITED ) ),
                                                     associationRequestCollection ) );

        claimList.addAll( claimService.getClaimList(
                                                     buildJournalClaimQueryConstraint( NominationAwardGroupType.lookup( NominationAwardGroupType.TEAM ),
                                                                                       journalStatusType,
                                                                                       NominationAwardGroupSizeType.lookup( NominationAwardGroupSizeType.LIMITED ) ),
                                                     associationRequestCollection ) );

        claimList.addAll( claimService.getClaimList(
                                                     buildJournalClaimQueryConstraint( NominationAwardGroupType.lookup( NominationAwardGroupType.TEAM ),
                                                                                       journalStatusType,
                                                                                       NominationAwardGroupSizeType.lookup( NominationAwardGroupSizeType.UNLIMITED ) ),
                                                     associationRequestCollection ) );
      }

      return claimList;
    }

    /**
     * @param participantId
     * @return List of 'faked' value object that are really non-claim
     *         related deposits such as file load, discretionary awards
     */
    private List getNonClaimRelatedDeposits( Long participantId )
    {
      List depositList = new ArrayList();

      // Get any deposits for this promotion not linked to any quiz
      // taking.
      // eg. file load, discretionary
      JournalQueryConstraint journalQueryConstraint = new JournalQueryConstraint();
      journalQueryConstraint.setParticipantId( participantId );
      journalQueryConstraint.setNotResultOfClaim( true );
      journalQueryConstraint.setJournalStatusTypesIncluded( new JournalStatusType[] { JournalStatusType.lookup( JournalStatusType.POST ) } );
      journalQueryConstraint.setPromotionId( promotionId );
      journalQueryConstraint.setStartDate( startDate );
      journalQueryConstraint.setEndDate( endDate );

      // need to do the association to read the Promotion obj from Journal
      // obj in jsp
      AssociationRequestCollection journalAssociationRequestCollection = new AssociationRequestCollection();
      journalAssociationRequestCollection.add( new JournalAssociationRequest( JournalAssociationRequest.PROMOTION ) );

      List journalList = getJournalService().getJournalList( journalQueryConstraint, journalAssociationRequestCollection );
      for ( Iterator journalIter = journalList.iterator(); journalIter.hasNext(); )
      {
        Journal journal = (Journal)journalIter.next();
        // only want nomination promotion type deposits
        if ( journal.getPromotion() != null && journal.getPromotion().isNominationPromotion() )
        {
          RecognitionHistoryValueObject valueObject = new RecognitionHistoryValueObject( journal );
          depositList.add( valueObject );
        }
      }
      return depositList;
    }

    /**
     * Returns the claim group service.
     * 
     * @return a reference to the claim group service.
     */
    private ClaimGroupService getClaimGroupService()
    {
      return (ClaimGroupService)getService( ClaimGroupService.BEAN_NAME );
    }

    /**
     * Returns the claim service.
     * 
     * @return a reference to the claim service.
     */
    private ClaimService getClaimService()
    {
      return (ClaimService)getService( ClaimService.BEAN_NAME );
    }

    /**
     * Returns the journal service.
     * 
     * @return a reference to the journal service.
     */
    private JournalService getJournalService()
    {
      return (JournalService)getService( JournalService.BEAN_NAME );
    }

    /**
     * Returns the promotion service.
     * 
     * @return a reference to the promotion service.
     */
    private PromotionService getPromotionService()
    {
      return (PromotionService)getService( PromotionService.BEAN_NAME );
    }

  }

  /**
   * Builds value objects used by the claim list on the Nomination History
   * page and the Recognition History page.
   */
  public static interface ValueObjectBuilder
  {
    /**
     * Returns information about the specified recognition claims.
     * 
     * @return information about the specified recognition claims, as a
     *         <code>List</code> of {@link RecognitionHistoryValueObject}
     *         objects.
     */
    public List buildValueObjects();
  }

  /**
   * Builds value objects used by the Nominations Sent list on the Nomination
   * History page.
   */
  public static class NominationSentBuilder implements ValueObjectBuilder
  {
    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private Long submitterId;

    private Long proxyUserId;

    private Long promotionId;

    private Date startDate;

    private Date endDate;

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    /**
     * Constructs a <code>NominationSentBuilder</code> object.
     * 
     * @param submitterId
     *            the ID of the user who submitted the claims.
     * @param proxyUserId
     *            the ID of the user who submitted the claims on behalf of
     *            the submitter.
     * @param promotionId
     *            if specified, return claims for only this promotion.
     * @param startDate
     *            if specified, return claims that were submitted on or
     *            after this date.
     * @param endDate
     *            if specified, return claims that were submitted on or
     *            before this date.
     */
    public NominationSentBuilder( Long submitterId, Long proxyUserId, Long promotionId, Date startDate, Date endDate )
    {
      this.submitterId = submitterId;
      this.proxyUserId = proxyUserId;
      this.promotionId = promotionId;
      this.startDate = startDate;
      this.endDate = endDate;
    }

    /**
     * Returns information about the specified nomination claims.
     * 
     * @return information about the specified nomination claims, as a
     *         <code>List</code> of {@link RecognitionHistoryValueObject}
     *         objects.
     */
    public List buildValueObjects()
    {
      List valueObjects = new ArrayList();

      List claimList = getClaimList();
      for ( Iterator iter = claimList.iterator(); iter.hasNext(); )
      {
        NominationClaim claim = (NominationClaim)iter.next();
        if ( claim.getClaimRecipients() != null && !claim.getClaimRecipients().isEmpty() )
        {
          ClaimRecipient claimRecipient = (ClaimRecipient)claim.getClaimRecipients().iterator().next();

          if ( claimRecipient != null )
          {
            Set<ApprovableItemApprover> approvableItemApprovers = claimRecipient.getApprovableItemApprovers();
            for ( ApprovableItemApprover item : approvableItemApprovers )
            {
              valueObjects.add( new RecognitionHistoryValueObject( claim, item, claimRecipient ) );
            }
          }
        }
      }
      return valueObjects;
    }

    // -------------------------------------------------------------------------
    // Private Methods
    // -------------------------------------------------------------------------

    /**
     * Returns the claims specified by the state of this object.
     * 
     * @return the claims specified by the state of this object, as a
     *         <code>List</code> of {@link NominationClaim} objects.
     */
    private List getClaimList()
    {
      // Setup the query constraint.
      NominationClaimQueryConstraint queryConstraint = new NominationClaimQueryConstraint();

      queryConstraint.setSubmitterId( submitterId );
      queryConstraint.setProxyUserId( proxyUserId );

      if ( promotionId != null && promotionId.longValue() != 0 )
      {
        queryConstraint.setIncludedPromotionIds( new Long[] { promotionId } );
      }

      queryConstraint.setStartDate( startDate );
      queryConstraint.setEndDate( endDate );

      // Setup the association request collection.
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );

      // Get the claim list.
      return getClaimService().getClaimListWithAssociations( queryConstraint, associationRequestCollection );
    }

    /**
     * Returns the claim service.
     * 
     * @return a reference to the claim service.
     */
    private ClaimService getClaimService()
    {
      return (ClaimService)getService( ClaimService.BEAN_NAME );
    }
  }

  /**
   * Builds value objects used by the Recognitions Received list on the
   * Recognition History page.
   */
  private static class RecognitionReceiveBuilder implements ValueObjectBuilder
  {
    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private Long recipientId;

    private Long promotionId;

    private Date startDate;

    private Date endDate;

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    /**
     * Constructs a <code>RecognitionReceiveBuilder</code> object.
     * 
     * @param recipientId
     *            the ID of the user who is the recognition recipient.
     * @param promotionId
     *            if specified, return claims for only this promotion.
     * @param startDate
     *            if specified, return claims that were submitted on or
     *            after this date.
     * @param endDate
     *            if specified, return claims that were submitted on or
     *            before this date.
     */
    public RecognitionReceiveBuilder( Long recipientId, Long promotionId, Date startDate, Date endDate )
    {
      this.recipientId = recipientId;
      this.promotionId = promotionId;
      this.startDate = startDate;
      this.endDate = endDate;
    }

    /**
     * Returns information about the specified recognition claims.
     * 
     * @return information about the specified recognition claims, as a
     *         <code>List</code> of {@link RecognitionHistoryValueObject}
     *         objects.
     */
    public List buildValueObjects()
    {
      List valueObjects = new ArrayList();

      List claimRecipientList = getClaimRecipientList( getClaimList() );
      for ( Iterator iter = claimRecipientList.iterator(); iter.hasNext(); )
      {
        ClaimRecipient claimRecipient = (ClaimRecipient)iter.next();
        String timeZoneID = getUserService().getUserTimeZone( claimRecipient.getRecipient().getId() );
        Date referenceDate = DateUtils.applyTimeZone( new Date(), timeZoneID );
        RecognitionHistoryValueObject valueObject = new RecognitionHistoryValueObject( claimRecipient );
        if ( ( (RecognitionClaim)claimRecipient.getClaim() ).isReversal() )
        {
          valueObject.setReversal( true );
        }
        List activities = null;
        boolean flag = false;
        if ( claimRecipient.getClaim().getPromotion().isRecognitionPromotion() )
        {
          RecognitionPromotion recPromotion = (RecognitionPromotion)claimRecipient.getClaim().getPromotion();
          if ( recPromotion.isSelfRecognitionEnabled() )
          {
            if ( claimRecipient.getRecipient().getId().longValue() == recipientId.longValue() )
            {
              flag = true;

            }
          }
        }
        if ( flag )
        {
          activities = getActivityService().getMerchOrderActivitiesByClaimIdAndUserId( claimRecipient.getClaim().getId(), claimRecipient.getRecipient().getId(), null );
        }
        else
        {
          activities = getActivityService().getMerchOrderActivitiesByClaimIdAndUserId( claimRecipient.getClaim().getId(), claimRecipient.getRecipient().getId() );
        }
        valueObject.setMerchGiftCodeActivityList( activities );
        valueObjects.add( valueObject );
      }
      // Get information about non claim related recognitions
      valueObjects.addAll( getNonClaimRelatedDeposits( this.recipientId ) );
      return valueObjects;
    }

    // -------------------------------------------------------------------------
    // Private Methods
    // -------------------------------------------------------------------------

    /**
     * Returns the claims specified by the state of this object.
     * 
     * @return the claims specified by the state of this object, as a
     *         <code>List</code> of {@link RecognitionClaim} objects.
     */
    private List getClaimList()
    {
      // Setup the query constraint.
      RecognitionClaimQueryConstraint queryConstraint = new RecognitionClaimQueryConstraint();

      queryConstraint.setRecipientId( recipientId );
      queryConstraint.setOpen( Boolean.FALSE );

      if ( promotionId != null && promotionId.longValue() != 0 )
      {
        queryConstraint.setIncludedPromotionIds( new Long[] { promotionId } );
      }

      queryConstraint.setStartDate( startDate );
      queryConstraint.setEndDate( endDate );

      // Setup the association request collection.
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );

      // Get the claim list.
      return getClaimService().getClaimListWithAssociations( queryConstraint, associationRequestCollection );
    }

    /**
     * Returns a list of the recipients from the given claims.
     * 
     * @return a list of the recipients from the given claims.
     */
    private List getClaimRecipientList( List claimList )
    {
      ArrayList claimRecipientList = new ArrayList();

      for ( Iterator claimIter = claimList.iterator(); claimIter.hasNext(); )
      {
        RecognitionClaim claim = (RecognitionClaim)claimIter.next();
        for ( Iterator claimRecipientIter = claim.getClaimRecipients().iterator(); claimRecipientIter.hasNext(); )
        {
          ClaimRecipient claimRecipient = (ClaimRecipient)claimRecipientIter.next();

          Long localRecipientId = claimRecipient.getRecipient().getId();
          Long claimId = claimRecipient.getClaim().getId();

          if ( claimRecipient.getApprovalStatusType().isAbstractApproved() && localRecipientId.equals( recipientId ) && !getClaimService().hasPendingJournalForClaim( localRecipientId, claimId ) )
          {
            claimRecipientList.add( claimRecipient );
          }
        }
      }

      return claimRecipientList;
    }

    /**
     * Returns the claim service.
     * 
     * @return a reference to the claim service.
     */
    private ClaimService getClaimService()
    {
      return (ClaimService)getService( ClaimService.BEAN_NAME );
    }

    /**
     * @param participantId
     * @return List of 'faked' value object that are really non-claim
     *         related deposits such as file load, discretionary awards
     */
    private List getNonClaimRelatedDeposits( Long participantId )
    {
      List depositList = new ArrayList();

      // Get any deposits for this promotion not linked to any quiz
      // taking.
      // eg. file load, discretionary
      JournalQueryConstraint journalQueryConstraint = new JournalQueryConstraint();
      journalQueryConstraint.setParticipantId( participantId );
      journalQueryConstraint.setNotResultOfClaim( true );
      journalQueryConstraint.setJournalStatusTypesIncluded( new JournalStatusType[] { JournalStatusType.lookup( JournalStatusType.POST ) } );
      journalQueryConstraint.setPromotionId( promotionId );
      journalQueryConstraint.setStartDate( startDate );
      journalQueryConstraint.setEndDate( endDate );

      // need to do the association to read the Promotion obj from Journal
      // obj in jsp
      AssociationRequestCollection journalAssociationRequestCollection = new AssociationRequestCollection();
      journalAssociationRequestCollection.add( new JournalAssociationRequest( JournalAssociationRequest.PROMOTION_DEPROXY ) );
      journalAssociationRequestCollection.add( new JournalAssociationRequest( JournalAssociationRequest.ACTIVITY_JOURNALS ) );

      List journalList = getJournalService().getJournalList( journalQueryConstraint, journalAssociationRequestCollection );
      for ( Iterator journalIter = journalList.iterator(); journalIter.hasNext(); )
      {
        Journal journal = (Journal)journalIter.next();
        // only want recognition promotion type deposits
        if ( journal.getPromotion() != null && journal.getPromotion().isRecognitionPromotion() )
        {
          RecognitionHistoryValueObject valueObject = new RecognitionHistoryValueObject( journal );
          valueObject.setMerchGiftCodeActivityList( getMerchOrderActivityList( journal ) );
          depositList.add( valueObject );
        }
      }
      return depositList;
    }

    private List getMerchOrderActivityList( Journal journal )
    {
      List merchOrderActivityList = new ArrayList();
      AbstractRecognitionPromotion recognitionPromotion = (AbstractRecognitionPromotion)journal.getPromotion();
      if ( recognitionPromotion.isAwardActive() && recognitionPromotion.getAwardType().isMerchandiseAwardType()
          && RecognitionPromotion.AWARD_STRUCTURE_LEVEL.equals( recognitionPromotion.getAwardStructure() ) )
      {
        for ( Iterator activityJournalIter = journal.getActivityJournals().iterator(); activityJournalIter.hasNext(); )
        {
          ActivityJournal currentActivityJournal = (ActivityJournal)activityJournalIter.next();
          if ( currentActivityJournal.getActivity() instanceof MerchandiseActivity )
          {
            merchOrderActivityList.add( currentActivityJournal.getActivity() );
          }
        }
      }
      return merchOrderActivityList;
    }

    /**
     * Returns the journal service.
     * 
     * @return a reference to the journal service.
     */
    private JournalService getJournalService()
    {
      return (JournalService)getService( JournalService.BEAN_NAME );
    }

  }

  /**
   * Builds value objects used by the Recognitions Sent list on the
   * Recognition History page.
   */
  private static class RecognitionSentBuilder implements ValueObjectBuilder
  {
    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private Long submitterId;

    private Long proxyUserId;

    private Long promotionId;

    private Date startDate;

    private Date endDate;

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    /**
     * Constructs a <code>RecognitionSentBuilder</code> object.
     * 
     * @param submitterId
     *            the ID of the user who submitted the claims.
     * @param proxyUserId
     *            the ID of the user who submitted the claims on behalf of
     *            the submitter.
     * @param promotionId
     *            if specified, return claims for only this promotion.
     * @param startDate
     *            if specified, return claims that were submitted on or
     *            after this date.
     * @param endDate
     *            if specified, return claims that were submitted on or
     *            before this date.
     */
    public RecognitionSentBuilder( Long submitterId, Long proxyUserId, Long promotionId, Date startDate, Date endDate )
    {
      this.submitterId = submitterId;
      this.proxyUserId = proxyUserId;
      this.promotionId = promotionId;
      this.startDate = startDate;
      this.endDate = endDate;
    }

    /**
     * Returns information about the specified recognition claims.
     * 
     * @return information about the specified recognition claims, as a
     *         <code>List</code> of {@link RecognitionHistoryValueObject}
     *         objects.
     */
    public List buildValueObjects()
    {
      List valueObjects = new ArrayList();

      List claimRecipientList = getClaimRecipientList( getClaimList() );
      for ( Iterator iter = claimRecipientList.iterator(); iter.hasNext(); )
      {
        ClaimRecipient claimRecipient = (ClaimRecipient)iter.next();
        RecognitionHistoryValueObject valueObject = new RecognitionHistoryValueObject( claimRecipient );
        Long recipientUserId = null;
        if ( ( (RecognitionClaim)claimRecipient.getClaim() ).isReversal() )
        {
          valueObject.setReversal( true );
        }
        if ( claimRecipient.getRecipient() != null )
        {
          recipientUserId = claimRecipient.getRecipient().getId();
        }
        List activities = null;
        if ( claimRecipient.getRecipient() != null )
        {
          boolean flag = false;
          if ( claimRecipient.getClaim().getPromotion().isRecognitionPromotion() )
          {
            RecognitionPromotion recPromotion = (RecognitionPromotion)claimRecipient.getClaim().getPromotion();
            if ( recPromotion.isSelfRecognitionEnabled() )
            {
              if ( claimRecipient.getRecipient().getId().longValue() == submitterId.longValue() )
              {
                flag = true;

              }
            }
          }
          if ( flag )
          {
            activities = getActivityService().getMerchOrderActivitiesByClaimIdAndUserId( claimRecipient.getClaim().getId(), claimRecipient.getRecipient().getId(), null );
          }
          else
          {
            activities = getActivityService().getMerchOrderActivitiesByClaimIdAndUserId( claimRecipient.getClaim().getId(), claimRecipient.getRecipient().getId() );
          }
        }
        valueObject.setMerchGiftCodeActivityList( activities );
        valueObjects.add( valueObject );
      }

      return valueObjects;
    }

    // -------------------------------------------------------------------------
    // Private Methods
    // -------------------------------------------------------------------------

    /**
     * Returns the claims specified by the state of this object.
     * 
     * @return the claims specified by the state of this object, as a
     *         <code>List</code> of {@link RecognitionClaim} objects.
     */
    private List getClaimList()
    {
      // Setup the query constraint.
      RecognitionClaimQueryConstraint queryConstraint = new RecognitionClaimQueryConstraint();

      queryConstraint.setSubmitterId( submitterId );
      queryConstraint.setProxyUserId( proxyUserId );

      if ( promotionId != null && promotionId.longValue() != 0 )
      {
        queryConstraint.setIncludedPromotionIds( new Long[] { promotionId } );
      }

      queryConstraint.setStartDate( startDate );
      queryConstraint.setEndDate( endDate );

      // Setup the association request collection.
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );

      // Get the claim list.
      return getClaimService().getClaimListWithAssociations( queryConstraint, associationRequestCollection );
    }

    /**
     * Returns a list of the recipients from the given claims.
     * 
     * @return a list of the recipients from the given claims.
     */
    private List getClaimRecipientList( List claimList )
    {
      ArrayList claimRecipientList = new ArrayList();

      for ( Iterator claimIter = claimList.iterator(); claimIter.hasNext(); )
      {
        RecognitionClaim claim = (RecognitionClaim)claimIter.next();
        claimRecipientList.addAll( claim.getClaimRecipients() );
      }

      return claimRecipientList;
    }

    /**
     * Returns the claim service.
     * 
     * @return a reference to the claim service.
     */
    private ClaimService getClaimService()
    {
      return (ClaimService)getService( ClaimService.BEAN_NAME );
    }
  }

  /**
   * Adds behavior specific to this controller to an
   * <code>HttpServletRequest</code> object.
   */
  private static class RequestWrapper extends HttpServletRequestWrapper
  {
    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    /**
     * Keys to request attributes and parameters.
     */
    private static final String PROMOTION_TYPE_CODE = "promotionTypeCode";

    private static final String MODE = "mode";

    private static final String START_DATE = "startDate";

    private static final String END_DATE = "endDate";

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private static final Log logger = LogFactory.getLog( RequestWrapper.class );

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    /**
     * Constructs a <code>RequestWrapper</code> object.
     * 
     * @param request
     */
    public RequestWrapper( HttpServletRequest request )
    {
      super( request );
    }

    /**
     * Returns the latest date on which claims in the claim list were
     * submitted. Returns now if the user did not specify an end date;
     * returns null if the user enterred an invalid date.
     * 
     * @return the latest date on which claims in the claim list are
     *         submitted.
     */
    public Date getEndDate()
    {
      Date endDate = null;

      String endDateString = getParameter( END_DATE );
      if ( endDateString != null && endDateString.length() > 0 )
      {
        try
        {
          endDate = DateUtils.toEndDate( endDateString );
        }
        catch( ParseException e )
        {
          logger.warn( "Invalid end date." );
        }
      }

      return endDate;
    }

    /**
     * Returns the mode.
     * 
     * @return the mode.
     */
    public String getMode()
    {
      String mode = getParameter( MODE );
      if ( mode == null || mode.length() == 0 )
      {
        mode = SENT_MODE;
      }

      return mode;
    }

    /**
     * Returns the Recognition History form.
     * 
     * @return the Recognition History form.
     */
    public RecognitionHistoryForm getRecognitionHistoryForm()
    {
      RecognitionHistoryForm form = (RecognitionHistoryForm)getAttribute( RecognitionHistoryForm.FORM_NAME );
      if ( form == null )
      {
        form = new RecognitionHistoryForm();
        setAttribute( RecognitionHistoryForm.FORM_NAME, form );
      }

      return form;
    }

    /**
     * Returns the earliest date on which claims in the claim list were
     * submitted. Returns null if the user enterred an invalid start date.
     * 
     * @return the earliest date on which claims in the claim list are
     *         submitted.
     */
    public Date getStartDate()
    {
      Date startDate = null;

      String startDateString = getParameter( START_DATE );
      if ( startDateString != null && startDateString.length() > 0 )
      {
        try
        {
          startDate = DateUtils.toStartDate( startDateString );
        }
        catch( ParseException e )
        {
          logger.warn( "Invalid start date." );
        }
      }

      return startDate;
    }

    /**
     * Returns "nomination" if the user is viewing nomination history pages
     * and "recognition" if the user is viewing recognition history pages.
     * 
     * @return "nomination" if the user is viewing nomination history pages
     *         and "recognition" if the user is viewing recognition history
     *         pages.
     */
    public String getPromotionTypeCode()
    {
      String promotionTypeCode = getParameter( PROMOTION_TYPE_CODE );
      if ( promotionTypeCode == null || promotionTypeCode.length() == 0 )
      {
        promotionTypeCode = PromotionType.RECOGNITION;
      }

      return promotionTypeCode;
    }
  }

  /**
   * Get the ActivityService from the beanLocator.
   * 
   * @return ActivityService
   */
  private static ActivityService getActivityService()
  {
    return (ActivityService)getService( ActivityService.BEAN_NAME );
  }

  private static ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }

  private static UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }
}
