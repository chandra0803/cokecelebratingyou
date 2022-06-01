/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/sweepstakes/PendingWinnersAction.java,v $
 */

package com.biperf.core.ui.promotion.sweepstakes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.PromotionSweepstakeService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.util.StringUtils;

/**
 * PendingWinnersAction.
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
 * <td>jenniget</td>
 * <td>Nov 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PendingWinnersAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( PendingWinnersAction.class );

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    PendingWinnersForm winnerForm = (PendingWinnersForm)request.getAttribute( PendingWinnersForm.FORM_NAME );
    if ( winnerForm == null )
    {
      winnerForm = new PendingWinnersForm();
    }
    Long promoId;
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
      try
      {
        promoId = (Long)clientStateMap.get( "promotionId" );
      }
      catch( ClassCastException cce )
      {
        promoId = new Long( (String)clientStateMap.get( "promotionId" ) );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    /*
     * Bug # 34020 start to speed this up AssociationRequestCollection assocReqs = new
     * AssociationRequestCollection(); assocReqs .add( new PromotionAssociationRequest(
     * PromotionAssociationRequest.PROMOTION_SWEEPSTAKES ) ); Promotion promo =
     * getPromotionService().getPromotionByIdWithAssociations( promoId, assocReqs );
     */
    Promotion promo = getPromotionService().getPromotionById( promoId );
    /* Bug # 34020 end */
    winnerForm.load( promo );

    request.setAttribute( PendingWinnersForm.FORM_NAME, form );
    return mapping.findForward( forwardTo );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    return display( mapping, form, request, response );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward refresh( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "refresh";

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      return mapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD ); // EARLY EXIT
    }

    PendingWinnersForm winnersForm = (PendingWinnersForm)form;

    List winnerBeans = winnersForm.getWinnerBeans();

    List winnersToRemove = new ArrayList();
    List winnersToReplace = new ArrayList();
    List winnersType = new ArrayList();
    List winnerBeanIds = new ArrayList();

    Iterator iter = winnerBeans.iterator();
    while ( iter.hasNext() )
    {
      PendingWinnerFormBean formBean = (PendingWinnerFormBean)iter.next();
      winnerBeanIds.add( new Long( formBean.getId() ) );
      if ( formBean.isRemove() )
      {
        winnersToRemove.add( new Long( formBean.getId() ) );
      }
      else if ( formBean.isReplace() )
      {
        winnersToReplace.add( new Long( formBean.getId() ) );
        winnersType.add( formBean.getWinnerType() );
      }
    }
    if ( !winnersToRemove.isEmpty() )
    {
      try
      {
        getPromotionSweepstakeService().removeWinners( winnersToRemove, new Long( winnersForm.getPromotionId() ) );
      }
      catch( UniqueConstraintViolationException e )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
      }
    }
    if ( !winnersToReplace.isEmpty() )
    {
      try
      {
        if ( !StringUtils.isEmpty( winnersForm.getPromotionType() ) && ( winnersForm.getPromotionType().equalsIgnoreCase( PromotionType.QUIZ )
            || winnersForm.getPromotionType().equalsIgnoreCase( PromotionType.SURVEY ) || winnersForm.getPromotionType().equalsIgnoreCase( PromotionType.BADGE ) ) )
        {
          getPromotionSweepstakeService().replaceWinners( winnersToReplace, new Long( winnersForm.getPromotionId() ), winnerBeanIds, winnerBeans.size(), 0 );
        }
        else
        {
          getPromotionSweepstakeService().replaceWinners( winnersToReplace,
                                                          new Long( winnersForm.getPromotionId() ),
                                                          winnersType,
                                                          getWinnersDisplayed( winnerBeans, true ),
                                                          getWinnersDisplayed( winnerBeans, false ) );
        }
      }
      catch( UniqueConstraintViolationException e )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
      }
      catch( ServiceErrorException e )
      {
        List serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
        saveErrors( request, errors );

      }
    }
    // we need to get the updated data soon after the refresh
    display( mapping, winnersForm, request, response );

    return mapping.findForward( ActionConstants.SUCCESS_UPDATE );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward process( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "process";

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      return mapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD ); // EARLY EXIT
    }

    PendingWinnersForm winnersForm = (PendingWinnersForm)form;
    try
    {
      getPromotionSweepstakeService().scheduleProcessAward( new Long( winnersForm.getPromotionId() ) );
    }
    catch( UniqueConstraintViolationException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD ); // EARLY EXIT
    }
    catch( ServiceErrorException see )
    {
      List l = (ArrayList)see.getServiceErrors();
      ServiceError s = (ServiceError)l.get( 0 );
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.SWEEPS_WINNER_NO_ADDRESS", s.getKey() ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD ); // EARLY EXIT
    }
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public int getWinnersDisplayed( List winnerBeans, boolean isGiver )
  {
    int giversDisplayed = 0;
    int receiversDisplayed = 0;

    for ( Iterator iter = winnerBeans.iterator(); iter.hasNext(); )
    {
      PendingWinnerFormBean pendingWinnerFormBean = (PendingWinnerFormBean)iter.next();
      if ( pendingWinnerFormBean.getWinnerType().equals( "Giver" ) || pendingWinnerFormBean.getWinnerType().equals( "Nominator" ) )
      {
        giversDisplayed++;
      }
      else
      {
        receiversDisplayed++;
      }

    }
    if ( isGiver )
    {
      return giversDisplayed;
    }

    return receiversDisplayed;
  }

  /**
   * Get the promotionSweepstakeService from the beanFactory.
   * 
   * @return promotionSweepstakeService
   */
  private PromotionSweepstakeService getPromotionSweepstakeService()
  {
    return (PromotionSweepstakeService)getService( PromotionSweepstakeService.BEAN_NAME );
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
}
