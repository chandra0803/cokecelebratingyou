/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/sweepstakes/SweepstakesWinnerHistoryController.java,v $
 */

package com.biperf.core.ui.promotion.sweepstakes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.dao.activity.hibernate.SweepstakesActivityQueryConstraint;
import com.biperf.core.domain.activity.SweepstakesActivity;
import com.biperf.core.domain.activity.SweepstakesAwardAmountActivity;
import com.biperf.core.domain.activity.SweepstakesMerchLevelActivity;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.PromotionSweepstakeWinner;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.SurveyPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.PromotionSweepstakeService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.util.StringUtils;

/**
 * SweepstakesWinnerHistoryController.
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
 * <td>potosky</td>
 * <td>Nov 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SweepstakesWinnerHistoryController extends BaseController
{

  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
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
      Long promotionId = null;
      Long selectedSweepstakeId = null;
      try
      {
        promotionId = (Long)clientStateMap.get( "promotionId" );
      }
      catch( ClassCastException cce )
      {
        promotionId = new Long( (String)clientStateMap.get( "promotionId" ) );
      }
      try
      {
        selectedSweepstakeId = (Long)clientStateMap.get( "selectedSweepstakeId" );
      }
      catch( ClassCastException cce )
      {
        selectedSweepstakeId = new Long( (String)clientStateMap.get( "selectedSweepstakeId" ) );
      }
      // Bugfix 17534 starts..
      SweepstakesWinnerHistoryForm swpWinnerHstryForm = (SweepstakesWinnerHistoryForm)request.getAttribute( SweepstakesWinnerHistoryForm.FORM_NAME );
      if ( swpWinnerHstryForm != null )
      {
        if ( selectedSweepstakeId == null && !StringUtils.isEmpty( swpWinnerHstryForm.getSelectedSweepstakeId() ) )
        {
          selectedSweepstakeId = new Long( swpWinnerHstryForm.getSelectedSweepstakeId() );
        }

      } // Bugfix 17534 ends..
      if ( promotionId != null )
      {
        Long sweepstakeIdValue = null;
        if ( selectedSweepstakeId == null )
        {
          PromotionSweepstake promoSweepstake = getPromotionSweepstakeService().getMostRecentlyEndedPromotionSweepstake( promotionId );

          sweepstakeIdValue = promoSweepstake.getId();
        }
        else
        {
          sweepstakeIdValue = selectedSweepstakeId;
        }

        AssociationRequestCollection assocReqs = new AssociationRequestCollection();
        assocReqs.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_SWEEPSTAKES ) );
        Promotion promo = getPromotionService().getPromotionByIdWithAssociations( promotionId, assocReqs );

        List sweepstakesList = new ArrayList();
        PromotionSweepstake sweepstake = null;
        for ( Iterator iter = promo.getPromotionSweepstakes().iterator(); iter.hasNext(); )
        {
          PromotionSweepstake sweep = (PromotionSweepstake)iter.next();
          if ( sweep.isProcessed() )
          {
            SweepstakeBean sweepBean = new SweepstakeBean();
            sweepBean.setDescription( buildDateDesc( sweep ) );
            sweepBean.setId( sweep.getId().toString() );
            sweepstakesList.add( sweepBean );
          }
          if ( sweep.getId().longValue() == sweepstakeIdValue.longValue() )
          {
            sweepstake = sweep;
          }
        }

        SweepstakesWinnerHistoryForm form = (SweepstakesWinnerHistoryForm)request.getAttribute( SweepstakesWinnerHistoryForm.FORM_NAME );

        if ( form == null )
        {
          form = new SweepstakesWinnerHistoryForm();
        }

        form.setPromotionName( promo.getName() );
        form.setPromotionId( promo.getId().toString() );
        form.setSweepstakesEndDate( DateUtils.toDisplayString( sweepstake.getEndDate() ) );
        form.setSweepstakesStartDate( DateUtils.toDisplayString( sweepstake.getStartDate() ) );

        if ( promo.isRecognitionPromotion() )
        {
          form.setPromotionType( "recognition" );
        }
        else if ( promo.isNominationPromotion() )
        {
          form.setPromotionType( "nomination" );
        }
        else if ( promo.isProductClaimPromotion() )
        {
          form.setPromotionType( "product_claim" );
        }
        else if ( promo.isSurveyPromotion() )
        {
          form.setPromotionType( "survey" );
        }

        form.setSelectedSweepstakeId( String.valueOf( sweepstakeIdValue ) );

        SweepstakesActivityQueryConstraint activityQueryConstraint = new SweepstakesActivityQueryConstraint();
        activityQueryConstraint.setPromotionSweepstakeId( sweepstake.getId() );
        List activityList = getActivityService().getActivityList( activityQueryConstraint );

        if ( promo.getAwardType() != null )
        {
          form.setAward( promo.getAwardType().getCode() );
        }
        form.setWinners( buildWinners( promo, sweepstake.getWinners(), activityList ) );

        request.setAttribute( "sweepstakesList", sweepstakesList );
        request.setAttribute( SweepstakesWinnerHistoryForm.FORM_NAME, form );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
  }

  /**
   * @param promotion
   * @param winnersList
   * @return List of FormBeans
   */
  private List buildWinners( Promotion promotion, Set winnersList, List activityList )
  {

    List winners = new ArrayList();

    if ( winnersList.size() == 0 )
    {
      return winners;
    }

    HashMap activityMap = new HashMap();
    Iterator activityIter = activityList.iterator();
    while ( activityIter.hasNext() )
    {
      SweepstakesActivity activity = (SweepstakesActivity)activityIter.next();
      // The map's key for each activity is pax id and is submitter
      // (i.e. "123true", "456false", etc.)
      if ( activity.getParticipant() != null )
      {
        activityMap.put( "" + activity.getParticipant().getId().intValue() + activity.isSubmitter(), activity );
      }
      else
      {
        activityMap.put( "" + activity.isSubmitter(), activity );
      }
    }

    for ( Iterator iter = winnersList.iterator(); iter.hasNext(); )
    {
      PromotionSweepstakeWinner winner = (PromotionSweepstakeWinner)iter.next();
      if ( !winner.isRemoved() )
      {
        Participant pax = winner.getParticipant();

        String paxDesc = "";
        if ( pax != null )
        {
          paxDesc = pax.getLastName() + ", " + pax.getFirstName();
          if ( pax.getMiddleName() != null )
          {
            paxDesc = paxDesc + " " + pax.getMiddleName().substring( 0, 1 );
          }
          if ( pax.getPositionType() != null )
          {
            paxDesc = paxDesc + " - " + pax.getPositionType();
          }
          if ( pax.getDepartmentType() != null )
          {
            paxDesc = paxDesc + " - " + pax.getDepartmentType();
          }
        }

        SweepstakesWinnerFormBean winnerFormBean = new SweepstakesWinnerFormBean();
        winnerFormBean.setId( winner.getId().toString() );
        winnerFormBean.setDescription( paxDesc );
        winnerFormBean.setWinnerType( winner.getWinnerType() );

        if ( promotion instanceof RecognitionPromotion )
        {
          String mapKey = winner.getParticipant().getId() + "false";
          SweepstakesActivity activity = (SweepstakesActivity)activityMap.get( mapKey );
          if ( activity instanceof SweepstakesAwardAmountActivity )
          {
            SweepstakesAwardAmountActivity sweepstakesAwardAmountActivity = (SweepstakesAwardAmountActivity)activity;
            if ( sweepstakesAwardAmountActivity == null || sweepstakesAwardAmountActivity.getAwardQuantity() == null )
            {
              winnerFormBean.setAward( "" );
            }
            else
            {
              winnerFormBean.setAward( sweepstakesAwardAmountActivity.getAwardQuantity() + "" );
            }
          }
          else
          {
            // To fix 21015 inorder to display winner history properly
            SweepstakesMerchLevelActivity sweepstakesMerchLevelActivity = (SweepstakesMerchLevelActivity)activity;
            if ( sweepstakesMerchLevelActivity != null && sweepstakesMerchLevelActivity.getMerchOrder() != null && sweepstakesMerchLevelActivity.getMerchOrder().getPromoMerchProgramLevel() != null )
            {
              winnerFormBean.setAward( sweepstakesMerchLevelActivity.getMerchOrder().getPromoMerchProgramLevel().getCmAssetKey() );
            }
            else
            {
              winnerFormBean.setAward( "" );
            }
          }
        }
        else if ( promotion instanceof NominationPromotion )
        {
          if ( winner.getWinnerType() != null && winner.getWinnerType().equals( PromotionSweepstakeWinner.NOMINEE_TYPE ) )
          {
            String mapKey = winner.getParticipant().getId() + "false";
            SweepstakesAwardAmountActivity activity = (SweepstakesAwardAmountActivity)activityMap.get( mapKey );
            if ( activity == null || activity.getAwardQuantity() == null )
            {
              winnerFormBean.setAward( "" );
            }
            else
            {
              winnerFormBean.setAward( activity.getAwardQuantity() + "" );
            }
          }
          else
          {
            String mapKey = winner.getParticipant().getId() + "false";
            SweepstakesAwardAmountActivity activity = (SweepstakesAwardAmountActivity)activityMap.get( mapKey );
            if ( activity == null || activity.getAwardQuantity() == null )
            {
              winnerFormBean.setAward( "" );
            }
            else
            {
              winnerFormBean.setAward( activity.getAwardQuantity() + "" );
            }
          }
        }

        else if ( promotion instanceof QuizPromotion || promotion instanceof SurveyPromotion || promotion instanceof Badge )
        {
          String mapKey = winner.getParticipant().getId() + "false";
          SweepstakesAwardAmountActivity activity = (SweepstakesAwardAmountActivity)activityMap.get( mapKey );
          if ( activity == null || activity.getAwardQuantity() == null )
          {
            winnerFormBean.setAward( "" );
          }
          else
          {
            winnerFormBean.setAward( activity.getAwardQuantity() + "" );
          }
        }

        else if ( promotion instanceof ProductClaimPromotion )
        {
          if ( winner.getWinnerType() != null && winner.getWinnerType().equals( PromotionSweepstakeWinner.TEAM_MEMBERS_TYPE ) )
          {
            String mapKey = winner.getParticipant().getId() + "true";
            SweepstakesAwardAmountActivity activity = (SweepstakesAwardAmountActivity)activityMap.get( mapKey );
            if ( activity == null || activity.getAwardQuantity() == null )
            {
              winnerFormBean.setAward( "" );
            }
            else
            {
              winnerFormBean.setAward( activity.getAwardQuantity() + "" );
            }
          }
          else
          {
            String mapKey = winner.getParticipant().getId() + "false";
            SweepstakesAwardAmountActivity activity = (SweepstakesAwardAmountActivity)activityMap.get( mapKey );
            if ( activity == null || activity.getAwardQuantity() == null )
            {
              winnerFormBean.setAward( "" );
            }
            else
            {
              winnerFormBean.setAward( activity.getAwardQuantity() + "" );
            }
          }
        }
        winners.add( winnerFormBean );
      }
    }

    return winners;
  }

  private String buildDateDesc( PromotionSweepstake sweepstake )
  {
    return DateUtils.toDisplayString( sweepstake.getStartDate() ) + " - " + DateUtils.toDisplayString( sweepstake.getEndDate() );
  }

  /**
   * Get the promotionService from the beanFactory.
   * 
   * @return PromotionService
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  /**
   * Get the promotionService from the beanFactory.
   * 
   * @return PromotionService
   */
  private PromotionSweepstakeService getPromotionSweepstakeService()
  {
    return (PromotionSweepstakeService)getService( PromotionSweepstakeService.BEAN_NAME );
  }

  /**
   * Get the activityService from the beanFactory.
   * 
   * @return ActivityService
   */
  private ActivityService getActivityService()
  {
    return (ActivityService)getService( ActivityService.BEAN_NAME );
  }

}
