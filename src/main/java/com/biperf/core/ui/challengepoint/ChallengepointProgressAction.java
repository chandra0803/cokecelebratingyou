
package com.biperf.core.ui.challengepoint;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.challengepoint.ChallengepointProgress;
import com.biperf.core.domain.enums.AchievementPrecision;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.service.challengepoint.ChallengePointService;
import com.biperf.core.service.challengepoint.ChallengepointProgressService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.ServiceLocator;

/**
 * ChallengepointProgressAction.
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
 * <td>Jul 17, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class ChallengepointProgressAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( ChallengepointProgressAction.class );

  /**
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control sho uld be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward saveChallengepointProgress( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ChallengepointProgressForm challengepointProgressForm = (ChallengepointProgressForm)form;
    ActionMessages messages = new ActionMessages();
    ChallengepointProgress cpProgressTosave = null;
    try
    {
      cpProgressTosave = buildChallengepointProgress( challengepointProgressForm );

      getChallengepointProgressService().saveChallengepointProgress( cpProgressTosave );

    }
    catch( Exception e )
    {
      logger.error( "Error occurred while saving Challengepoint Pax Progress", e );
      messages.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION ) );
    }
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "promotionId", challengepointProgressForm.getPromotionId() );
    clientStateParameterMap.put( "userId", challengepointProgressForm.getUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    if ( !messages.isEmpty() )
    {
      saveErrors( request, messages );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    challengepointProgressForm.clearValues();
    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { queryString } );
  }

  /**
   * Get the ChallengePointService from the beanLocator.
   * 
   * @return ChallengePointService
   */
  private ChallengepointProgressService getChallengepointProgressService()
  {
    return (ChallengepointProgressService)getService( ChallengepointProgressService.BEAN_NAME );
  }

  private ChallengePointService getChallengePointService()
  {
    return (ChallengePointService)getService( ChallengePointService.BEAN_NAME );
  }

  private ChallengepointProgress buildChallengepointProgress( ChallengepointProgressForm challengepointProgressForm )
  {
    ChallengepointProgress challengepointProgress = new ChallengepointProgress();
    challengepointProgress.setType( challengepointProgressForm.getAddReplaceType() );
    ChallengePointPromotion promotion = getChallengePointService().getPromotion( challengepointProgressForm.getPromotionId() );
    challengepointProgress.setChallengePointPromotion( promotion );
    challengepointProgress.setParticipant( getParticipantService().getParticipantById( challengepointProgressForm.getUserId() ) );
    AchievementPrecision achPrecision = AchievementPrecision.lookup( AchievementPrecision.ZERO );
    if ( promotion.getAchievementPrecision() != null )
    {
      achPrecision = promotion.getAchievementPrecision();
    }
    BigDecimal newQuantity = new BigDecimal( challengepointProgressForm.getNewQuantity() ).setScale( achPrecision.getPrecision(), promotion.getRoundingMethod().getBigDecimalRoundingMode() );
    challengepointProgress.setQuantity( newQuantity );
    Date submissionDate = getChallengepointProgressService().getProgressLastSubmissionDate( challengepointProgressForm.getPromotionId() );
    if ( submissionDate == null )
    {
      submissionDate = DateUtils.getCurrentDate();
    }
    challengepointProgress.setSubmissionDate( submissionDate );
    return challengepointProgress;
  }

  /**
   * Get the ParticipantService from the beanLocator.
   * 
   * @return ParticipantService
   */
  private static ParticipantService getParticipantService()
  {
    return (ParticipantService)ServiceLocator.getService( ParticipantService.BEAN_NAME );
  }
}
