/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/ParticipantGoalQuestProgressForm.java,v $
 */

package com.biperf.core.ui.participant;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.AchievementPrecision;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.SelectGoalUtil;
import com.biperf.core.utils.ServiceLocator;
import com.biperf.util.StringUtils;

/**
 * ParticipantGoalQuestProgressForm.
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
 * <td>viswanat</td>
 * <td>Feb 21, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantGoalQuestProgressForm extends BaseForm
{
  private Long userId;
  private Long promotionId;
  private String method;
  private String addReplaceType;
  private String newQuantity;

  /**
   * Overridden from @see com.biperf.core.ui.BaseForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   * @return ActionErrors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );
    if ( errors == null )
    {
      errors = new ActionErrors();
    }
    if ( promotionId != null && promotionId.longValue() > 0 )
    {
      GoalQuestPromotion gqPromo = SelectGoalUtil.getPromotion( promotionId );
      if ( gqPromo.getAchievementPrecision() != null && gqPromo.getAchievementPrecision().getCode().equals( AchievementPrecision.ZERO ) )
      {
        int newBaseQuantity;
        try
        {
          newBaseQuantity = Integer.parseInt( newQuantity );
          if ( !StringUtils.isEmpty( newQuantity ) && Double.parseDouble( newQuantity ) == 0 )
          {
            errors.add( "newQuantity", new ActionMessage( "participant.goalquest.promo.detail.NEW_QUANTITY_INTEGER" ) );
          }
        }
        catch( NumberFormatException e )
        {
          errors.add( "newQuantity", new ActionMessage( "participant.goalquest.promo.detail.NEW_QUANTITY_INTEGER" ) );
        }

      }
    }

    return errors;
  }

  public String getAddReplaceType()
  {
    return addReplaceType;
  }

  public void setAddReplaceType( String addReplaceType )
  {
    this.addReplaceType = addReplaceType;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getNewQuantity()
  {
    return newQuantity;
  }

  public void setNewQuantity( String newQuantity )
  {
    this.newQuantity = newQuantity;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public void clearValues()
  {
    this.newQuantity = null;
    this.addReplaceType = null;
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)ServiceLocator.getService( AudienceService.BEAN_NAME );
  }
}
