/*
 * (c) 2008 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/PromotionPartnerUpdateAssociation.java,v $
 */

package com.biperf.core.service.promotion;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionPartnerPayout;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.UpdateAssociationRequest;

/**
 * PromotionPartnerUpdateAssociation.
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
 * <td>reddy</td>
 * <td>Feb 26, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class PromotionPartnerUpdateAssociation extends UpdateAssociationRequest
{
  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Constructs a <code>PromotionPartnerUpdateAssociation</code> object.
   * 
   * @param detachedPromotion a detached {@link Promotion} object.
   */
  /**
   * @param detachedPromotion
   */
  public PromotionPartnerUpdateAssociation( Promotion detachedPromotion )
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

    if ( attachedPromotion.isGoalQuestPromotion() || attachedPromotion.isChallengePointPromotion() )
    {
      updateGoalQuestPromotion( (GoalQuestPromotion)attachedPromotion );
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

    attachedPromotion.setPartnerPayoutStructure( detachedPromotion.getPartnerPayoutStructure() );
    attachedPromotion.setPartnerEarnings( detachedPromotion.getPartnerEarnings() );
    updatePartnerGoalLevels( detachedPromotion, attachedPromotion );
  }

  /**
   * Updates thepartner goalLevels for the specified promotion. This will update updated levels, add
   * new levels, and remove deleted levels.
   * 
   * @param detachedPromotion
   * @param attachedPromotion
   */
  private void updatePartnerGoalLevels( GoalQuestPromotion detachedPromotion, GoalQuestPromotion attachedPromotion )
  {
    Map updatedLevels = new TreeMap();
    Set detachedPartnerGoalLevels = detachedPromotion.getPartnerGoalLevels();
    Set attachedPartnerGoalLevels = attachedPromotion.getPartnerGoalLevels();
    if ( detachedPartnerGoalLevels != null )
    {
      for ( Iterator detachedIter = detachedPartnerGoalLevels.iterator(); detachedIter.hasNext(); )
      {
        PromotionPartnerPayout detachedPartnerGoalLevel = (PromotionPartnerPayout)detachedIter.next();
        if ( detachedPartnerGoalLevel.getId() == null || detachedPartnerGoalLevel.getId().longValue() == 0 )
        {
          attachedPromotion.addPartnerGoalLevel( detachedPartnerGoalLevel );
        }
        else
        {
          updatedLevels.put( detachedPartnerGoalLevel.getId(), detachedPartnerGoalLevel );
        }
      }
      for ( Iterator attachedIter = attachedPartnerGoalLevels.iterator(); attachedIter.hasNext(); )
      {
        PromotionPartnerPayout attachedOverrideGoalLevel = (PromotionPartnerPayout)attachedIter.next();
        if ( attachedOverrideGoalLevel.getId() != null && attachedOverrideGoalLevel.getId().longValue() != 0 )
        {
          PromotionPartnerPayout detachedOverrideGoalLevel = (PromotionPartnerPayout)updatedLevels.get( attachedOverrideGoalLevel.getId() );
          if ( detachedOverrideGoalLevel != null )
          {
            updatePartnerGoalLevel( detachedOverrideGoalLevel, attachedOverrideGoalLevel );
          }
        }
      }
    }

  }

  public void updatePartnerGoalLevel( PromotionPartnerPayout detachedOverrideGoalLevel, PromotionPartnerPayout attachedOverrideGoalLevel )
  {
    if ( detachedOverrideGoalLevel != null && attachedOverrideGoalLevel != null )
    {
      attachedOverrideGoalLevel.setSequenceNumber( detachedOverrideGoalLevel.getSequenceNumber() );
      attachedOverrideGoalLevel.setGoalLevelNameKey( detachedOverrideGoalLevel.getGoalLevelNameKey() );
      attachedOverrideGoalLevel.setGoalLevelDescriptionKey( detachedOverrideGoalLevel.getGoalLevelDescriptionKey() );
      attachedOverrideGoalLevel.setGoalLevelcmAssetCode( detachedOverrideGoalLevel.getGoalLevelcmAssetCode() );
      attachedOverrideGoalLevel.setPartnerAwardAmount( detachedOverrideGoalLevel.getPartnerAwardAmount() );

    }
  }

}
