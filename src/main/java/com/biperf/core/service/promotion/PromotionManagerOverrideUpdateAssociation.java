/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/PromotionManagerOverrideUpdateAssociation.java,v $
 */

package com.biperf.core.service.promotion;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.ManagerOverrideStructure;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.ManagerOverrideGoalLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * PromotionManagerOverrideUpdateAssociation.
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
 * <td>sedey</td>
 * <td>July 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionManagerOverrideUpdateAssociation extends UpdateAssociationRequest
{
  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Constructs a <code>PromotionManagerOverrideUpdateAssociation</code> object.
   * 
   * @param detachedPromotion a detached {@link Promotion} object.
   */
  public PromotionManagerOverrideUpdateAssociation( Promotion detachedPromotion )
  {
    super( detachedPromotion );
  }

  /**
   * Use the detached {@link Promotion} object to update the attached {@link Promotion} object.
   * 
   * @param attachedDomain the attached version of the {@link Promotion} object.
   */
  public void execute( BaseDomain attachedDomain )
  {
    Promotion attachedPromotion = (Promotion)attachedDomain;

    if ( attachedPromotion.isGoalQuestPromotion() )
    {
      updateGoalQuestPromotion( (GoalQuestPromotion)attachedPromotion );
    }
    if ( attachedPromotion.isChallengePointPromotion() )
    {
      updateChallengePointPromotion( (ChallengePointPromotion)attachedPromotion );
    }
  }

  /**
   * Uses attached and detached versions of a {@link Promotion} object to perform additional
   * validation on the object. 
   * 
   * @param attachedDomain the attached version of the {@link Promotion} object.
   * @throws com.biperf.core.exception.ServiceErrorExceptionWithRollback if a validation error
   *           occurs.
   */
  public void validate( BaseDomain attachedDomain ) throws ServiceErrorExceptionWithRollback
  {

  }

  /**
   * @param attachedPromotion
   */
  private void updateGoalQuestPromotion( GoalQuestPromotion attachedPromotion )
  {
    GoalQuestPromotion detachedPromotion = (GoalQuestPromotion)getDetachedDomain();

    attachedPromotion.setOverrideStructure( detachedPromotion.getOverrideStructure() );
    attachedPromotion.setLevelOneMgrAward( detachedPromotion.getLevelOneMgrAward() );
    attachedPromotion.setLevelTwoMgrAward( detachedPromotion.getLevelTwoMgrAward() );
    if ( detachedPromotion.getOverrideStructure() != null && detachedPromotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.AWARD_PER_ACHIEVER ) )
    {
      updateGoalLevels( detachedPromotion, attachedPromotion );
    }
    updateManagerOverrideGoalLevels( detachedPromotion, attachedPromotion );
  }

  /**
   * @param attachedPromotion
   */
  private void updateChallengePointPromotion( ChallengePointPromotion attachedPromotion )
  {
    ChallengePointPromotion detachedPromotion = (ChallengePointPromotion)getDetachedDomain();

    attachedPromotion.setOverrideStructure( detachedPromotion.getOverrideStructure() );
    attachedPromotion.setLevelOneMgrAward( detachedPromotion.getLevelOneMgrAward() );
    attachedPromotion.setLevelTwoMgrAward( detachedPromotion.getLevelTwoMgrAward() );
    attachedPromotion.setTotalTeamProductionMeasure( detachedPromotion.getTotalTeamProductionMeasure() );
    attachedPromotion.setTotalTeamProduction( detachedPromotion.getTotalTeamProduction() );
    attachedPromotion.setManagerAward( detachedPromotion.getManagerAward() );

    if ( detachedPromotion.getOverrideStructure() != null && detachedPromotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.AWARD_PER_ACHIEVER ) )
    {
      updateChallengePointLevels( detachedPromotion, attachedPromotion );
    }
    updateManagerOverrideGoalLevels( detachedPromotion, attachedPromotion );
  }

  /**
   * 
   * Updates the goalLevels for the specified promotion.  This will update updated levels, add new levels, and remove deleted
   * levels.
   *  
   * @param detachedPromotion
   * @param attachedPromotion
   */
  private void updateGoalLevels( GoalQuestPromotion detachedPromotion, GoalQuestPromotion attachedPromotion )
  {
    Map updatedLevels = new TreeMap();
    Set detachedGoalLevels = detachedPromotion.getGoalLevels();
    Set attachedGoalLevels = attachedPromotion.getGoalLevels();
    if ( detachedGoalLevels != null )
    {
      for ( Iterator detachedIter = detachedGoalLevels.iterator(); detachedIter.hasNext(); )
      {
        GoalLevel detachedGoalLevel = (GoalLevel)detachedIter.next();
        updatedLevels.put( detachedGoalLevel.getId(), detachedGoalLevel );
      }
    }
    for ( Iterator attachedIter = attachedGoalLevels.iterator(); attachedIter.hasNext(); )
    {
      GoalLevel attachedGoalLevel = (GoalLevel)attachedIter.next();
      if ( attachedGoalLevel.getId() != null && attachedGoalLevel.getId().longValue() != 0 )
      {
        GoalLevel detachedGoalLevel = (GoalLevel)updatedLevels.get( attachedGoalLevel.getId() );
        if ( detachedGoalLevel != null )
        {
          updateGoalLevel( detachedGoalLevel, attachedGoalLevel );
        }
      }
    }
  }

  public void updateGoalLevel( GoalLevel detachedGoalLevel, GoalLevel attachedGoalLevel )
  {
    if ( detachedGoalLevel != null && attachedGoalLevel != null )
    {
      attachedGoalLevel.setManagerAward( detachedGoalLevel.getManagerAward() );
    }
  }

  /**
   * 
   * Updates the manager override goalLevels for the specified promotion.  This will update updated levels,
   *  add new levels, and remove deleted levels.
   *  
   * @param detachedPromotion
   * @param attachedPromotion
   */
  private void updateManagerOverrideGoalLevels( GoalQuestPromotion detachedPromotion, GoalQuestPromotion attachedPromotion )
  {
    Map updatedLevels = new TreeMap();
    Set detachedOverrideGoalLevels = detachedPromotion.getManagerOverrideGoalLevels();
    Set attachedOverrideGoalLevels = attachedPromotion.getManagerOverrideGoalLevels();
    // code fix for bug 18707 boolean levelAdded & levelRemoved validations are added as a fix to
    // this bug
    boolean levelAdded = false;
    boolean levelRemoved = false;
    if ( detachedOverrideGoalLevels != null )
    {
      for ( Iterator detachedIter = detachedOverrideGoalLevels.iterator(); detachedIter.hasNext(); )
      {
        ManagerOverrideGoalLevel detachedOverrideGoalLevel = (ManagerOverrideGoalLevel)detachedIter.next();
        if ( detachedOverrideGoalLevel.getId() == null || detachedOverrideGoalLevel.getId().longValue() == 0 )
        {
          attachedPromotion.addManagerOverrideGoalLevel( detachedOverrideGoalLevel );
          levelAdded = true;
        }
        else
        {
          updatedLevels.put( detachedOverrideGoalLevel.getId(), detachedOverrideGoalLevel );
        }
      }
    }
    for ( Iterator attachedIter = attachedOverrideGoalLevels.iterator(); attachedIter.hasNext(); )
    {
      ManagerOverrideGoalLevel attachedOverrideGoalLevel = (ManagerOverrideGoalLevel)attachedIter.next();
      if ( attachedOverrideGoalLevel.getId() != null && attachedOverrideGoalLevel.getId().longValue() != 0 )
      {
        ManagerOverrideGoalLevel detachedOverrideGoalLevel = (ManagerOverrideGoalLevel)updatedLevels.get( attachedOverrideGoalLevel.getId() );
        if ( detachedOverrideGoalLevel != null )
        {
          updateManagerOverrideGoalLevel( detachedOverrideGoalLevel, attachedOverrideGoalLevel );
        }
        else
        {
          attachedIter.remove();
          levelRemoved = true;
        }
      }
    }
    if ( levelRemoved )
    {
      Set newAttachedGoalLevels = new HashSet( attachedPromotion.getManagerOverrideGoalLevels() );
      attachedPromotion.getManagerOverrideGoalLevels().clear();
      HibernateSessionManager.getSession().flush();
      // not setting the Set directly since dereferencing a collection when transtive persistence is
      // 'all-delete-orphan'
      // is not allowed in hibernate
      for ( Iterator i = newAttachedGoalLevels.iterator(); i.hasNext(); )
      {
        ManagerOverrideGoalLevel newManagerOverrideGoalLevel = (ManagerOverrideGoalLevel)i.next();
        // set id to null since these are supposed to be new elements. This will avoid
        // StaleObjectStateException.
        newManagerOverrideGoalLevel.setId( null );
        attachedPromotion.addManagerOverrideGoalLevel( newManagerOverrideGoalLevel );
      }
    }
    if ( ( levelAdded || levelRemoved ) && ( attachedPromotion.isLive() || attachedPromotion.isComplete() ) )
    {
      attachedPromotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.UNDER_CONSTRUCTION ) );
    }
  }

  public void updateManagerOverrideGoalLevel( ManagerOverrideGoalLevel detachedOverrideGoalLevel, ManagerOverrideGoalLevel attachedOverrideGoalLevel )
  {
    if ( detachedOverrideGoalLevel != null && attachedOverrideGoalLevel != null )
    {
      attachedOverrideGoalLevel.setSequenceNumber( detachedOverrideGoalLevel.getSequenceNumber() );
      attachedOverrideGoalLevel.setGoalLevelNameKey( detachedOverrideGoalLevel.getGoalLevelNameKey() );
      attachedOverrideGoalLevel.setGoalLevelDescriptionKey( detachedOverrideGoalLevel.getGoalLevelDescriptionKey() );
      attachedOverrideGoalLevel.setGoalLevelcmAssetCode( detachedOverrideGoalLevel.getGoalLevelcmAssetCode() );
      attachedOverrideGoalLevel.setManagerAward( detachedOverrideGoalLevel.getManagerAward() );
      attachedOverrideGoalLevel.setTeamAchievementPercent( detachedOverrideGoalLevel.getTeamAchievementPercent() );
      if ( attachedOverrideGoalLevel.getId().equals( detachedOverrideGoalLevel.getId() ) )
      {
        attachedOverrideGoalLevel.setMoStartRank( detachedOverrideGoalLevel.getMoStartRank() );
        attachedOverrideGoalLevel.setMoEndRank( detachedOverrideGoalLevel.getMoEndRank() );
        attachedOverrideGoalLevel.setMoAwards( detachedOverrideGoalLevel.getMoAwards() );
      }
    }
  }

  /**
   * 
   * Updates the goalLevels for the specified promotion.  This will update updated levels, add new levels, and remove deleted
   * levels.
   *  
   * @param detachedPromotion
   * @param attachedPromotion
   */
  private void updateChallengePointLevels( ChallengePointPromotion detachedPromotion, ChallengePointPromotion attachedPromotion )
  {
    Map<Long, GoalLevel> updatedLevels = new TreeMap<Long, GoalLevel>();
    Set<AbstractGoalLevel> detachedLevels = detachedPromotion.getGoalLevels();
    Set<AbstractGoalLevel> attachedLevels = attachedPromotion.getGoalLevels();
    if ( detachedLevels != null )
    {
      for ( Iterator<AbstractGoalLevel> detachedIter = detachedLevels.iterator(); detachedIter.hasNext(); )
      {
        GoalLevel detachedLevel = (GoalLevel)detachedIter.next();
        updatedLevels.put( detachedLevel.getId(), detachedLevel );
      }
    }
    for ( Iterator<AbstractGoalLevel> attachedIter = attachedLevels.iterator(); attachedIter.hasNext(); )
    {
      GoalLevel attachedLevel = (GoalLevel)attachedIter.next();
      if ( attachedLevel.getId() != null && attachedLevel.getId().longValue() != 0 )
      {
        GoalLevel detachedLevel = updatedLevels.get( attachedLevel.getId() );
        if ( detachedLevel != null )
        {
          updateChallengePointLevel( detachedLevel, attachedLevel );
        }
      }
    }
  }

  public void updateChallengePointLevel( GoalLevel detachedLevel, GoalLevel attachedLevel )
  {
    if ( detachedLevel != null && attachedLevel != null )
    {
      attachedLevel.setManagerAward( detachedLevel.getManagerAward() );
    }
  }

}
