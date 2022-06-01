
package com.biperf.core.ui.client;

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

import com.biperf.core.domain.client.TccNomLevelPayout;
import com.biperf.core.domain.client.TcccQcardBatch;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.client.CokeClientService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.promotion.PromotionPayoutForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.value.client.TcccLevelPayoutValueBean;

public class TcccLevelPayoutAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( TcccLevelPayoutAction.class );

  public ActionForward unspecified( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( actionMapping, actionForm, request, response );
  }

  public ActionForward display( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    String promotionId = null;

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
        promotionId = (String)clientStateMap.get( "promotionId" );
      }
      catch( ClassCastException cce )
      {
        promotionId = (String)clientStateMap.get( "promotionId" );
      }

      TcccLevelPayoutForm tcccLevelPayoutForm = (TcccLevelPayoutForm)actionForm;
      TccNomLevelPayout tccNomLevelPayout = null;
      List<TcccLevelPayoutValueBean> levelPayoutList = new ArrayList<TcccLevelPayoutValueBean>();

      levelPayoutList = getCokeClientService().getLevelPayoutByPromotionId( promotionId );
      tcccLevelPayoutForm.load( levelPayoutList, promotionId );
      int payoutLevelListSize = tcccLevelPayoutForm.getPayoutLevelListSize();
      if ( payoutLevelListSize > 0 )
      {
        request.setAttribute( "lastSegmentIndex", payoutLevelListSize - 1 );
      }
      else
      {
        request.setAttribute( "lastSegmentIndex", 0 );
      }

    }
    catch( Exception e )
    {

    }

    return mapping.findForward( forwardTo );
  }

  public ActionForward addAnotherLevelPayout( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( "success" );
    ActionMessages errors = new ActionMessages();
    TcccLevelPayoutForm tcccLevelPayoutForm = (TcccLevelPayoutForm)actionForm;

    tcccLevelPayoutForm.addEmptyPayoutLevel();

    /*
     * if ( !errors.isEmpty() ) { saveErrors( request, errors ); forward = mapping.findForward(
     * "failure_add_another" ); }
     */

    return forward;
  }

  public ActionForward save( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    TcccLevelPayoutForm tcccLevelPayoutForm = (TcccLevelPayoutForm)actionForm;

    List<TcccLevelPayoutValueBean> levelPayoutList = new ArrayList<TcccLevelPayoutValueBean>();
    TcccLevelPayoutValueBean levelPayoutVB = new TcccLevelPayoutValueBean();
    levelPayoutList = tcccLevelPayoutForm.getPayoutLevelList();
    try
    {
      for ( Iterator levelPayoutIterator = levelPayoutList.iterator(); levelPayoutIterator.hasNext(); )
      {
        levelPayoutVB = (TcccLevelPayoutValueBean)levelPayoutIterator.next();
        saveLevelPayout( levelPayoutVB );
      }
    }
    catch( Exception e )
    {
      logger.error( e.getMessage() );
    }

    int payoutLevelListSize = tcccLevelPayoutForm.getPayoutLevelListSize();
    if ( payoutLevelListSize > 0 )
    {
      request.setAttribute( "lastSegmentIndex", payoutLevelListSize - 1 );
    }
    else
    {
      request.setAttribute( "lastSegmentIndex", 0 );
    }

    return mapping.findForward( forwardTo );

  }

  private TccNomLevelPayout saveLevelPayout( TcccLevelPayoutValueBean tcccLevelPayoutVB )
  {

    Promotion promotion = null;
    TccNomLevelPayout tccNomLevelPayout = null;
    if ( tcccLevelPayoutVB.getLevelPayoutId() != null && tcccLevelPayoutVB.getLevelPayoutId() > 0 )
    {
      // tccNomLevelPayout.setId( tcccLevelPayoutVB.getLevelPayoutId() );
      tccNomLevelPayout = getCokeClientService().getLevelPayoutById( new Long( tcccLevelPayoutVB.getLevelPayoutId() ) );
    }
    else
    {
      tccNomLevelPayout = new TccNomLevelPayout();
    }
    promotion = getPromotionService().getPromotionById( new Long( tcccLevelPayoutVB.getPromotionId() ) );
    tccNomLevelPayout.setPromotion( promotion );
    tccNomLevelPayout.setLevelDescription( tcccLevelPayoutVB.getLevelDescription() );
    tccNomLevelPayout.setTotalPoints( tcccLevelPayoutVB.getTotalPoints() );
    tccNomLevelPayout = getCokeClientService().save( tccNomLevelPayout );
    return tccNomLevelPayout;

  }

  private CokeClientService getCokeClientService()
  {
    return (CokeClientService)getService( CokeClientService.BEAN_NAME );
  }

  protected static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

}
