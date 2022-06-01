
package com.biperf.core.service.promotion.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.promotion.PayoutProcessingService;
import com.biperf.core.service.promotion.PromotionEngineService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.engine.ManagerOverrideFacts;

/**
 * PayoutProcessingServiceImpl.
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
 * <td>asondgeroth</td>
 * <td>Jul 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PayoutProcessingServiceImpl implements PayoutProcessingService
{
  private ActivityDAO activityDAO = null;
  private AudienceService audienceService;
  private ParticipantDAO participantDAO = null;
  private PromotionEngineService promotionEngineService = null;
  private PromotionService promotionService;

  /**
   * @return AudienceService
   */
  public AudienceService getAudienceService()
  {
    return this.audienceService;
  }

  /**
   * @param audienceService
   */
  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  /**
   * @return ActivityDAO
   */
  public ActivityDAO getActivityDAO()
  {
    return activityDAO;
  }

  /**
   * @param activityDAO
   */
  public void setActivityDAO( ActivityDAO activityDAO )
  {
    this.activityDAO = activityDAO;
  }

  /**
   * @return ParticipantDAO
   */
  public ParticipantDAO getParticipantDAO()
  {
    return participantDAO;
  }

  /**
   * @param participantDAO
   */
  public void setParticipantDAO( ParticipantDAO participantDAO )
  {
    this.participantDAO = participantDAO;
  }

  /**
   * @param promotionEngineService
   */
  public void setPromotionEngineService( PromotionEngineService promotionEngineService )
  {
    this.promotionEngineService = promotionEngineService;
  }

  /**
   * @param promotionService value for promotionService property
   */
  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.PayoutProcessingService#processManagerOverride(java.lang.Long,
   *      java.util.Date, java.util.Date)
   * @param promotionId
   * @param startDate
   * @param endDate
   * @return Set of PromotionCalculationResults objects
   */
  public Set processManagerOverride( Long promotionId, Date startDate, Date endDate ) throws ServiceErrorException
  {
    Promotion promotion = promotionService.getPromotionById( promotionId );
    List managers = fetchManagerOverrideManagers( promotion, startDate, endDate );

    Set savedPayouts = new LinkedHashSet();

    for ( int i = 0; i < managers.size(); i++ )
    {
      Participant manager = (Participant)managers.get( i );
      ManagerOverrideFacts facts = new ManagerOverrideFacts( manager, startDate, endDate );
      savedPayouts.addAll( promotionEngineService.calculatePayoutAndSaveResults( facts, promotion, manager, PromotionPayoutType.lookup( PromotionPayoutType.MANAGER_OVERRIDE ).getCode() ) );
    }

    promotionEngineService.depositPostedPayouts( savedPayouts );

    return savedPayouts;
  }

  /**
   * @param promotion
   * @param startDate
   * @param endDate
   * @return List
   */
  private List fetchManagerOverrideManagers( Promotion promotion, Date startDate, Date endDate )
  {
    List managers = new ArrayList();
    Set activities = activityDAO.getUnpostedManagerOverrideActivities( promotion.getId(), startDate, endDate );
    Iterator iter = activities.iterator();
    while ( iter.hasNext() )
    {
      ManagerOverrideActivity managerOverrideActivity = (ManagerOverrideActivity)iter.next();
      Participant manager = managerOverrideActivity.getParticipant();
      if ( !managers.contains( manager ) )
      {
        managers.add( manager );
      }
    }
    return managers;
  }

}
