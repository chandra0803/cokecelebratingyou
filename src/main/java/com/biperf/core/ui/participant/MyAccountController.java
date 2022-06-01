/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/MyAccountController.java,v $
 */

package com.biperf.core.ui.participant;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.dao.claim.hibernate.RecognitionClaimQueryConstraint;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.GoalquestHistoryValueObject;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.merchandise.ActivityMerchOrder;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.goalquest.GoalLevelService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.claim.RecognitionHistoryListController.ValueObjectBuilder;
import com.biperf.core.ui.claim.RecognitionHistoryValueObject;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;

/**
 * MyAccountController.
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
 * <td>Sep 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MyAccountController extends BaseController
{

  private static final Log logger = LogFactory.getLog( MyAccountController.class );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    request.setAttribute( "isConvertCertUsed", new Boolean( getSystemVariableService().getPropertyByName( SystemVariableService.AWARDBANQ_CONVERTCERT_IS_USED ).getBooleanVal() ) );
    request.setAttribute( "isGiftCodeOnlyPax", new Boolean( UserManager.getUser().isParticipant() && UserManager.getUser().isGiftCodeOnlyPax() ) );

    // Choose Sub Nav
    String subNavSelected = (String)tileContext.getAttribute( "subNavSelected" );
    request.setAttribute( "subNavSelected", subNavSelected );
    logger.debug( "subNavSelected=" + subNavSelected );
  }

  public Date getEndDate( HttpServletRequest request )
  {
    Date endDate = null;

    String endDateString = request.getParameter( "endDate" );
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

  public Date getStartDate( HttpServletRequest request )
  {
    Date startDate = null;

    String startDateString = request.getParameter( "startDate" );
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

  public static class RecognitionReceiveBuilder implements ValueObjectBuilder
  {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private Long recipientId;

    private Date startDate;

    private Date endDate;

    private Long[] promotionIds;

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    /**
     * Constructs a <code>RecognitionReceiveBuilder</code> object.
     * 
     * @param recipientId
     *            the ID of the user who is the recognition recipient.
     * @param startDate
     *            if specified, return claims that were submitted on or
     *            after this date.
     * @param endDate
     *            if specified, return claims that were submitted on or
     *            before this date.
     */
    public RecognitionReceiveBuilder( Long recipientId, Date startDate, Date endDate, Long[] promotionIds )
    {
      this.recipientId = recipientId;
      this.startDate = startDate;
      this.endDate = endDate;
      this.promotionIds = promotionIds;
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

      List claimList = getClaimList();
      List claimRecipientList = getClaimRecipientList( claimList );

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
        boolean isMerchandise = false;
        if ( claimRecipient.getClaim().getPromotion().isRecognitionPromotion() )
        {
          RecognitionPromotion recPromotion = (RecognitionPromotion)claimRecipient.getClaim().getPromotion();
          if ( recPromotion.getAwardType().getCode().equals( PromotionAwardsType.MERCHANDISE ) )
          {
            isMerchandise = true;
            if ( recPromotion.isSelfRecognitionEnabled() )
            {
              if ( claimRecipient.getRecipient().getId().longValue() == recipientId.longValue() )
              {
                flag = true;

              }
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
        valueObject.setFormatSubmissionDate( DateUtils.toDisplayString( valueObject.getSubmissionDate(), UserManager.getLocale() ) );
        if ( isMerchandise )
        {
          valueObjects.add( valueObject );
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
     *         <code>List</code> of {@link RecognitionClaim} objects.
     */
    private List getClaimList()
    {
      // Setup the query constraint.
      RecognitionClaimQueryConstraint queryConstraint = new RecognitionClaimQueryConstraint();

      queryConstraint.setRecipientId( recipientId );
      queryConstraint.setOpen( Boolean.FALSE );

      /*
       * if (promotionId != null && promotionId.longValue() != 0) { queryConstraint
       * .setIncludedPromotionIds(new Long[] { promotionId }); }
       */
      if ( promotionIds != null && promotionIds.length > 0 )
      {
        Long[] includedPromotionIds = promotionIds;
        queryConstraint.setIncludedPromotionIds( includedPromotionIds );
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

  }

  public static class GoalQuestBuilder implements ValueObjectBuilder
  {
    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private Long userId;

    private Long[] promotionIds;

    private Date startDate;

    private Date endDate;

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    /**
     * Constructs a <code>GoalQuestBuilder</code> object.
     * 
     * @param userId
     *            the ID of the user who achieved the GQ/CP.
     * @param promotionIds
     *            if specified, return activity for only this promotions.
     * @param startDate
     *            if specified, return GQ/CP activity that goals on or
     *            after this date.
     * @param endDate
     *            if specified, return GQ/CP activity that goals on or
     *            before this date.
     */
    public GoalQuestBuilder( Long userId, Date startDate, Date endDate, Long[] promotionIds )
    {
      this.userId = userId;
      this.promotionIds = promotionIds;
      this.startDate = startDate;
      this.endDate = endDate;
    }

    /**
     * Returns information about the specified GQ/CP Activity.
     * 
     * @return information about the specified GQ/CP activities, as a
     *         <code>List</code> of {@link GoalquestHistoryValueObject}
     *         objects.
     */
    public List buildValueObjects()
    {
      List valueObjects = new ArrayList();
      String promotionId = "";
      List<Long> longList = convertArray( promotionIds );
      for ( Iterator<Long> iter = longList.iterator(); iter.hasNext(); )
      {
        promotionId = promotionId + iter.next().toString();
        if ( iter.hasNext() )
        {
          promotionId = promotionId + ",";
        }
      }

      List<Activity> gqActivityList = getActivityService().getGQAndCPActivities( userId,
                                                                                 promotionId,
                                                                                 DateUtils.toDisplayString( startDate, UserManager.getLocale() ),
                                                                                 DateUtils.toDisplayString( endDate, UserManager.getLocale() ),
                                                                                 UserManager.getLocale().toString() );

      for ( Iterator<Activity> iter = gqActivityList.iterator(); iter.hasNext(); )
      {
        Activity activity = iter.next();

        GoalquestHistoryValueObject valueObject = new GoalquestHistoryValueObject( activity );
        valueObject.setFormatSubmissionDate( DateUtils.toDisplayString( valueObject.getSubmissionDate(), UserManager.getLocale() ) );
        List<BigDecimal> merchOrderActivities = new ArrayList<BigDecimal>();
        for ( Iterator<ActivityMerchOrder> merchOrderIter = activity.getActivityMerchOrders().iterator(); merchOrderIter.hasNext(); )
        {
          ActivityMerchOrder activityMerchOrder = merchOrderIter.next();
          merchOrderActivities.add( new BigDecimal( activityMerchOrder.getMerchOrder().getPoints() ) );
        }

        for ( Iterator<AbstractGoalLevel> goalLevelIter = getGoalLevelService().getGoalLevelsByPromotionId( activity.getPromotion().getId() ).iterator(); goalLevelIter.hasNext(); )
        {
          AbstractGoalLevel abstractGoalLevel = goalLevelIter.next();
          if ( ( (GoalLevel)abstractGoalLevel ).getAward() != null && merchOrderActivities.contains( ( (GoalLevel)abstractGoalLevel ).getAward() ) )
          {
            valueObject.setLevelName( abstractGoalLevel.getGoalLevelName() );
          }
        }
        valueObjects.add( valueObject );
      }
      return valueObjects;
    }

    public static ArrayList<Long> convertArray( Long[] array )
    {
      ArrayList<Long> result = new ArrayList<Long>( array.length );
      for ( long item : array )
      {
        result.add( item );
      }
      return result;
    }
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private static ActivityService getActivityService()
  {
    return (ActivityService)getService( ActivityService.BEAN_NAME );
  }

  private static UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private static GoalLevelService getGoalLevelService()
  {
    return (GoalLevelService)getService( GoalLevelService.BEAN_NAME );
  }
}
