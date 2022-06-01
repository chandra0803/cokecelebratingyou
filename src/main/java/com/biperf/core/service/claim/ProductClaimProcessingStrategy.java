/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/claim/ProductClaimProcessingStrategy.java,v $
 */

package com.biperf.core.service.claim;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.activity.hibernate.ManagerOverrideActivityQueryConstraint;
import com.biperf.core.dao.claim.MinimumQualifierStatusDAO;
import com.biperf.core.dao.journal.hibernate.JournalQueryConstraint;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.MinimumQualifierStatus;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.product.ProductService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.engine.PayoutCalculationFacts;
import com.biperf.core.service.promotion.engine.PayoutCalculationResult;
import com.biperf.core.service.promotion.engine.SalesFacts;
import com.biperf.core.service.util.BudgetCalculator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.UserManager;

/**
 * ProductClaimProcessingStrategy.
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
 * <td>Oct 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class ProductClaimProcessingStrategy extends AbstractClaimProcessingStrategy
{
  private static final Log log = LogFactory.getLog( RecognitionClaimProcessingStrategy.class );

  private AwardBanQServiceFactory awardBanQServiceFactory;
  private BudgetCalculator budgetCalculator;
  private MinimumQualifierStatusDAO minimumQualifierStatusDAO;
  private PromotionService promotionService;
  private ProductService productService;
  private ParticipantDAO participantDAO;
  private UserDAO userDAO;

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.claim.AbstractClaimProcessingStrategy#getPayoutCalculationFacts(Approvable,
   *      com.biperf.core.domain.promotion.Promotion,
   *      com.biperf.core.domain.participant.Participant)
   * @param approvable
   * @param promotion
   * @param participant
   * @return PayoutCalculationFacts
   */
  protected PayoutCalculationFacts getPayoutCalculationFacts( Approvable approvable, Promotion promotion, Participant participant )
  {
    ProductClaim productClaim = (ProductClaim)approvable;
    // Bug # 20318 && Bug # 21225 --START
    // productClaim.setPromotion(promotion);
    // END
    Participant manager = null;
    if ( participant.equals( productClaim.getSubmitter() ) && ( (ProductClaimPromotion)promotion ).isPayoutManager() )
    {
      manager = getManagerOverridePerson( productClaim );
    }

    Map minQualifierStatusByPromoPayoutGroup = minimumQualifierStatusDAO.getMinQualifierStatusByPromoPayoutGroup( productClaim.getSubmitter().getId(), promotion.getId() );

    return new SalesFacts( productClaim, participant, manager, minQualifierStatusByPromoPayoutGroup );
  }

  /**
   * Withdraw from the promotion's budget the amount paid out for given approvable.
   *
   * @param payoutCalculationResults
   * @param approvable
   */
  protected void calculateBudget( Set payoutCalculationResults, Approvable approvable )
  {
    budgetCalculator.calculateBudget( payoutCalculationResults, approvable );
  }

  /**
   * Generates activities for the given claim.
   *
   * @param approvable a claim.
   * @return the activities generated from the given claim, as a <code>List</code> of
   *         {@link Activity} objects.
   */
  protected List createActivitiesForApprovable( Approvable approvable )
  {
    ProductClaim productClaim = (ProductClaim)approvable;
    List activities = new ArrayList();

    Set masterAndChildPromotions = new LinkedHashSet();
    Promotion masterPromotion = productClaim.getPromotion();
    masterAndChildPromotions.add( masterPromotion );
    if ( masterPromotion.isProductClaimPromotion() )
    {
      masterAndChildPromotions.addAll( ( (ProductClaimPromotion)masterPromotion ).getChildPromotions() );
    }

    for ( Iterator iter = masterAndChildPromotions.iterator(); iter.hasNext(); )
    {
      ProductClaimPromotion promotion = (ProductClaimPromotion)iter.next();
      if ( !promotion.isUnderConstruction() && promotionService.isPromotionClaimableByParticipant( promotion, productClaim.getSubmitter(), productClaim.getSubmissionDate() ) )
      {

        // First get valid participants
        List validParticipants = getValidParticipantsForApprovable( productClaim, promotion );

        // Assume master promo's products are date valid (since only date vaaid are shown), but
        // confirm dates on child promo. So, for child promos, load valid products
        boolean isChildPromotion = promotion.hasParent();
        String timeZoneID = UserManager.getTimeZoneID();
        Date referenceDate = DateUtils.applyTimeZone( productClaim.getSubmissionDate(), timeZoneID );
        List products = new ArrayList();
        if ( isChildPromotion )
        {
          products = productService.getProductsByPromotionAndDateRange( promotion.getId(), referenceDate );
        }

        Iterator claimProductIterator = productClaim.getClaimProducts().iterator();
        while ( claimProductIterator.hasNext() )
        {
          ClaimProduct claimProduct = (ClaimProduct)claimProductIterator.next();
          if ( claimProduct.getApprovalStatusType().getCode().equals( ApprovalStatusType.APPROVED ) )
          {
            if ( !isChildPromotion || products.contains( claimProduct.getProduct() ) )
            {

              Iterator participantIterator = validParticipants.iterator();
              while ( participantIterator.hasNext() )
              {
                Participant participant = (Participant)participantIterator.next();

                SalesActivity activity = new SalesActivity( GuidUtils.generateGuid() );

                activity.setClaim( productClaim );
                activity.setProduct( claimProduct.getProduct() );
                activity.setPromotion( promotion );
                activity.setParticipant( participant );
                activity.setNode( productClaim.getNode() );
                activity.setQuantity( new Long( claimProduct.getQuantity() ) );
                activity.setSubmissionDate( productClaim.getSubmissionDate() );
                activity.setSubmitter( participant.equals( productClaim.getSubmitter() ) );

                activities.add( activity );
              }
            }
          }
        }
      }
    }

    return activities;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.claim.AbstractClaimProcessingStrategy#getPromotionPayoutType(com.biperf.core.domain.promotion.Promotion)
   * @param promotion
   * @return String promotion payoutType
   */
  public String getPromotionPayoutType( Promotion promotion )
  {
    ProductClaimPromotion productClaimPromotion = (ProductClaimPromotion)promotion;

    return productClaimPromotion.getPayoutType().getCode();
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.claim.AbstractClaimProcessingStrategy#getValidParticipantsForApprovable(Approvable,
   *      com.biperf.core.domain.promotion.Promotion)
   * @param approvable
   * @param promotion
   * @return List
   */
  protected List getValidParticipantsForApprovable( Approvable approvable, Promotion promotion )
  {
    ProductClaim productClaim = (ProductClaim)approvable;

    // Order is critial for Minimum qualifier handling - submitter must be first, so that we know
    // whether or
    // not we need to hold team member journals in a "pending min qualifer" state.
    List validParticipants = new ArrayList();

    if ( promotionService.isParticipantMemberOfPromotionAudience( productClaim.getSubmitter(), promotion, true, productClaim.getNode() ) )
    {
      validParticipants.add( productClaim.getSubmitter() );
    }

    Iterator iter = productClaim.getClaimParticipants().iterator();
    while ( iter.hasNext() )
    {
      ProductClaimParticipant productClaimParticipant = (ProductClaimParticipant)iter.next();
      Participant teamMember = productClaimParticipant.getParticipant();
      if ( promotionService.isParticipantMemberOfPromotionAudience( teamMember, promotion, false, productClaim.getNode() ) )
      {
        validParticipants.add( teamMember );
      }
    }

    return validParticipants;
  }

  /**
   * Get the "manager override", which is actually the submitter node's owner, if one exists. The
   * owner must be a participant.
   *
   * @param claim
   * @return Participant
   */
  private Participant getManagerOverridePerson( Claim claim )
  {
    Node submittersNode = claim.getSubmittersNode();

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
    associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );

    List owners = userDAO.getAllUsersOnNodeHavingRole( submittersNode.getId(), HierarchyRoleType.lookup( HierarchyRoleType.OWNER ), associationRequestCollection );
    Participant manager = null;
    if ( !owners.isEmpty() )
    {
      // Only ever one owner, so just take first.
      User user = (User)owners.iterator().next();

      // owner must be active and be a participant.
      if ( BooleanUtils.isTrue( user.isActive() ) && user.isParticipant() )
      {
        // user is a Participant, try down casting it.
        manager = participantDAO.getParticipantById( user.getId() );
      }
    }

    return manager;
  }

  /**
   * For this postProcess implementation for Product claim if the min qualifer is used and has been
   * met for the submitter (with retro on), then update all previous pending records for the
   * submitter and any team member and manager overide person. Overridden from
   *
   * @see com.biperf.core.service.claim.AbstractClaimProcessingStrategy#postProcess(java.util.Set,
   *      Approvable)
   * @param payoutCalculationResults
   * @param approvable
   */
  public void postProcess( Set payoutCalculationResults, Approvable approvable ) throws ServiceErrorException
  {
    try
    {
      ProductClaim productClaim = (ProductClaim)approvable;
      for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
      {
        PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();
        Participant participant = payoutCalculationResult.getPayoutCalculationAudit().getParticipant();
        if ( participant.equals( productClaim.getSubmitter() ) )
        {
          MinimumQualifierStatus minimumQualifierStatus = payoutCalculationResult.getMinimumQualifierStatus();
          // will be null if min qualifier not used or has been exceeded.
          if ( minimumQualifierStatus != null )
          {
            if ( minimumQualifierStatus.isMinQualifierMet() )
            { // Bug # 34032 fix
              try
              {
                updatePendingMinimimQualiferRecords( payoutCalculationResult, productClaim, participant.getId() );
              }
              catch( ServiceErrorException e )
              {
                //
              }
            }
          }
        }
      }
    }
    catch( Exception ex )
    {
      log.error( "Error in postProcess ", ex );
      throw new ServiceErrorException( ex.getMessage(), ex );
    }
  }

  private void updatePendingMinimimQualiferRecords( PayoutCalculationResult payoutCalculationResult, Claim claim, Long participantId ) throws ServiceErrorException// Bug
                                                                                                                                                                   // #
                                                                                                                                                                   // 34032
                                                                                                                                                                   // fix
  {
    PromotionPayoutGroup promotionPayoutGroup = payoutCalculationResult.getPromotionPayoutGroup();

    // **submitter/team member journals

    // query all "pending min qualifier" journals which tie back to promotionPayoutGroup.
    JournalQueryConstraint pendingMinQualifierJournalQueryConstraint = new JournalQueryConstraint();
    pendingMinQualifierJournalQueryConstraint.setJournalStatusTypesIncluded( new JournalStatusType[] { JournalStatusType.lookup( JournalStatusType.PENDING_MINIMUM_QUALIFIER ) } );
    pendingMinQualifierJournalQueryConstraint.setAuditPromotionPayoutGroupId( promotionPayoutGroup.getId() );

    /* Bug # 34032 start */
    pendingMinQualifierJournalQueryConstraint.setParticipantId( participantId );
    /* Bug # 34032 end */

    List toBeReleasedJournals = journalService.getJournalList( pendingMinQualifierJournalQueryConstraint );
    for ( Iterator iter = toBeReleasedJournals.iterator(); iter.hasNext(); )
    {
      Journal toBeReleasedJournal = (Journal)iter.next();
      // deposit award type, so post the journal.
      toBeReleasedJournal.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.POST ) );
      toBeReleasedJournal = journalService.postJournal( toBeReleasedJournal );
    }

    // **Manager override activities

    // Query all "min qualifier" Manager override activities which tie back to promotionPayoutGroup.
    ManagerOverrideActivityQueryConstraint activityQueryConstraint = new ManagerOverrideActivityQueryConstraint();
    activityQueryConstraint.setStatusPromotionPayoutGroupId( promotionPayoutGroup.getId() );

    List managerOverrideActivities = activityService.getActivityList( activityQueryConstraint );
    for ( Iterator iter = managerOverrideActivities.iterator(); iter.hasNext(); )
    {
      // A MO activity refers to their a qualifier status, so it will already be marked as
      // "released", since the
      // status shows "qualifer met". However, we update the activity submission date so that the
      // MO process will pick it up - MO process is based on start date and end date. It currently
      // skips
      // any MO activity with a min qualifier status shoing qualifier not met.
      ManagerOverrideActivity managerOverrideActivity = (ManagerOverrideActivity)iter.next();
      // Update the Submission Date for the Claim in processing
      if ( claim.getId().longValue() == managerOverrideActivity.getClaim().getId().longValue() )
      {
        managerOverrideActivity.setSubmissionDate( new Date() );
        activityService.saveActivity( managerOverrideActivity );
      }
    }
  }

  // ---------------------------------------------------------------------------
  // DAO Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * @param minimumQualifierStatusDAO value for minimumQualifierStatusDAO property
   */
  public void setMinimumQualifierStatusDAO( MinimumQualifierStatusDAO minimumQualifierStatusDAO )
  {
    this.minimumQualifierStatusDAO = minimumQualifierStatusDAO;
  }

  public void setParticipantDAO( ParticipantDAO participantDAO )
  {
    this.participantDAO = participantDAO;
  }

  // ---------------------------------------------------------------------------
  // Service Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the deployment-dependent implementation of the AwardBanQ service.
   *
   * @return the deployment-dependent implementation of the AwardBanQ service.
   */
  public AwardBanQService getAwardBanQService()
  {
    return awardBanQServiceFactory.getAwardBanQService();
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }

  public void setBudgetCalculator( BudgetCalculator budgetCalculator )
  {
    this.budgetCalculator = budgetCalculator;
  }

  public void setProductService( ProductService productService )
  {
    this.productService = productService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setUserDAO( UserDAO userDAO )
  {
    this.userDAO = userDAO;
  }
}
