/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/journal/impl/JournalAssociationRequest.java,v $
 */

package com.biperf.core.service.journal.impl;

import java.util.Iterator;
import java.util.Set;

import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.StackRankActivity;
import com.biperf.core.domain.journal.ActivityJournal;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.StackRankNode;
import com.biperf.core.domain.promotion.StackRankParticipant;
import com.biperf.core.service.BaseAssociationRequest;
import com.biperf.core.utils.ProxyUtil;

/**
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
 * <td>robinsra</td>
 * <td>Jul 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class JournalAssociationRequest extends BaseAssociationRequest
{

  private int hydrateLevel = 0;

  public static final int ALL = 1;
  public static final int PROMOTION = 2;
  public static final int BUDGET_WITH_MASTER_AND_OWNER = 3;
  public static final int ACTIVITY_JOURNALS = 4;
  public static final int PROMOTION_DEPROXY = 5;
  public static final int STACK_RANK_AWARD = 6;
  public static final int PARTICIPANT = 7;
  public static final int BILL_CODES = 8;

  public JournalAssociationRequest( int hydrateLevel )
  {
    this.hydrateLevel = hydrateLevel;
  }

  public void execute( Object domainObject )
  {
    Journal journal = (Journal)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateAll( journal );
        break;
      case ACTIVITY_JOURNALS:
        hydrateActivityJournals( journal );
        break;
      case BUDGET_WITH_MASTER_AND_OWNER:
        hydrateBudgetWithMasterAndOwner( journal );
        break;
      case PROMOTION:
        hydratePromotion( journal );
        break;
      case PROMOTION_DEPROXY:
        hydratePromotion( journal );
        journal.setPromotion( (Promotion)ProxyUtil.deproxy( journal.getPromotion() ) );
        break;
      case STACK_RANK_AWARD:
        hydrateForStackRankAward( journal );
        break;
      case PARTICIPANT:
        hydrateParticipant( journal );
        break;
      case BILL_CODES:
        hydrateBillCodes( journal );
        break;
      default:
        break;
    }
  }

  /**
   * hydrates the promotion.
   * 
   * @param journal
   */
  private void hydratePromotion( Journal journal )
  {
    initialize( journal.getPromotion() );
  }

  /**
   * hydrates the budget with the budget's budgetmaster and owner.
   * 
   * @param journal
   */
  private void hydrateBudgetWithMasterAndOwner( Journal journal )
  {
    initialize( journal.getBudget() );
    if ( journal.getBudget() != null )
    {
      initialize( journal.getBudget().getBudgetSegment().getBudgetMaster() );
      initialize( journal.getBudget().getBudgetOwner() );
    }
  }

  /**
   * hydrates the ActivityJournals.
   * 
   * @param journal
   */
  private void hydrateActivityJournals( Journal journal )
  {
    Set activityJournals = journal.getActivityJournals();
    initialize( activityJournals );
    Iterator activityJournalIter = activityJournals.iterator();
    while ( activityJournalIter.hasNext() )
    {
      ActivityJournal activityJournal = (ActivityJournal)activityJournalIter.next();
      Activity activity = activityJournal.getActivity();
      initialize( activity );
    }
  }

  /**
   * hydrates all pieces of the journal
   * 
   * @param journal
   */
  private void hydrateAll( Journal journal )
  {
    hydratePromotion( journal );
    hydrateBudgetWithMasterAndOwner( journal );
    hydrateActivityJournals( journal );
    hydrateParticipant( journal );
    hydrateBillCodes( journal );
  }

  private void hydrateForStackRankAward( Journal journal )
  {
    hydratePromotion( journal );
    hydrateActivityJournals( journal );

    // Hydrate stack rank participants and stack rank nodes.
    for ( Iterator iter = journal.getActivityJournals().iterator(); iter.hasNext(); )
    {
      ActivityJournal activityJournal = (ActivityJournal)iter.next();
      Activity activity = activityJournal.getActivity();
      if ( activity instanceof StackRankActivity )
      {
        StackRankActivity stackRankActivity = (StackRankActivity)activity;

        StackRankParticipant stackRankParticipant = stackRankActivity.getStackRankParticipant();
        initialize( stackRankParticipant );

        StackRankNode stackRankNode = stackRankParticipant.getStackRankNode();
        initialize( stackRankNode );
        initialize( stackRankNode.getStackRankParticipants() );
      }
    }
  }

  /**
   * hydrates the participant.
   * 
   * @param journal
   */
  private void hydrateParticipant( Journal journal )
  {
    initialize( journal.getParticipant() );
    if ( journal.getParticipant() != null )
    {
      initialize( journal.getParticipant().getUserAddresses() );
    }
  }

  private void hydrateBillCodes( Journal journal )
  {
    initialize( journal.getBillCodes() );
    if ( journal.getBillCodes() != null )
    {
      initialize( journal.getBillCodes() );
    }
  }
}
