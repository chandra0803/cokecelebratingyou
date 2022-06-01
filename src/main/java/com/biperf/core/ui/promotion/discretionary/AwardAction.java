/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/discretionary/AwardAction.java,v $
 */

package com.biperf.core.ui.promotion.discretionary;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.JournalTransactionType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.util.StringUtils;

/**
 * AwardAction.
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
 * <td>wadzinsk</td>
 * <td>Jul 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AwardAction extends BaseDispatchAction
{

  public static final String OPT_OUT_TEXT = "Opt Out Of Awards";

  /**
   * Overridden from
   * 
   * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping,
   *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse)
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( mapping, actionForm, request, response );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    AwardForm awardForm = (AwardForm)form;

    long minimumAwardAmount = 0;
    minimumAwardAmount = getSystemVariableService().getPropertyByName( SystemVariableService.DISCRETIONARY_AWARD_MIN ).getLongVal();
    awardForm.setDiscretionaryAwardMin( minimumAwardAmount );

    long maximumAwardAmount = 0;
    maximumAwardAmount = getSystemVariableService().getPropertyByName( SystemVariableService.DISCRETIONARY_AWARD_MAX ).getLongVal();
    awardForm.setDiscretionaryAwardMax( maximumAwardAmount );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward depositAward( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    AwardForm awardForm = (AwardForm)form;

    Participant pax = getParticipantService().getParticipantById( awardForm.getUserId() );
    Journal journalEntry = new Journal();

    journalEntry.setGuid( GuidUtils.generateGuid() );
    journalEntry.setParticipant( pax );
    journalEntry.setAccountNumber( pax.getAwardBanqNumber() );
    journalEntry.setTransactionDate( new Date() );

    Promotion promotion = getPromotionService().getPromotionById( new Long( Long.parseLong( awardForm.getPromotion() ) ) );
    journalEntry.setPromotion( promotion );

    journalEntry.setTransactionType( JournalTransactionType.lookup( JournalTransactionType.DEPOSIT ) );
    journalEntry.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.POST ) );
    journalEntry.setAwardPayoutType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );

    // Bug 53489 set promotion-name based on recipient Locale
    String promotionName = promotion.getPromotionName();
    if ( pax != null && pax.getLanguageType() != null )
    {
      promotionName = getPromotionService().getPromotionNameByLocale( promotion.getPromoNameAssetCode(), pax.getLanguageType().getCode() );
      promotionName = StringUtils.isEmpty( promotionName ) ? promotion.getPromotionName() : promotionName;
    }

    if ( pax.isOptOutAwards() )
    {
      journalEntry.setTransactionAmount( new Long( 0 ) );
      journalEntry.setTransactionDescription( promotionName + "-" + OPT_OUT_TEXT );
    }
    else
    {
      journalEntry.setTransactionAmount( new Long( Long.parseLong( awardForm.getAmount() ) ) );
      journalEntry.setTransactionDescription( promotionName );
    }

    journalEntry.setComments( awardForm.getComments() );
    journalEntry.setJournalType( Journal.DISCRETIONARY );

    try
    {
      getJournalService().saveAndDepositJournalEntry( journalEntry );
    }
    catch( ServiceErrorException e )
    {
      ActionMessages errors = new ActionMessages();
      List<ServiceError> serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", awardForm.getUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String[] params = { queryString, "depositSuccess=true" };
    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, params );
  }

  /**
   * Get the PromotionService.
   * 
   * @return PromotionService
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  /**
   * Get the ParticipantService.
   * 
   * @return ParticipantService
   */
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  /**
   * Get the JournalService.
   * 
   * @return JournalService
   */
  private JournalService getJournalService()
  {
    return (JournalService)getService( JournalService.BEAN_NAME );
  }

  /**
   * Get the SystemVariableService.
   * 
   * @return SystemVariableService
   */
  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
}
