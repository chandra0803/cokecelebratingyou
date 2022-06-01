
package com.biperf.core.ui.promotion.rules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.UserManager;

/**
 * 
 * @author dudam
 * @since Dec 10, 2012
 * @version 1.0
 */
public class RulesAction extends BaseDispatchAction
{

  private static final Log logger = LogFactory.getLog( RulesAction.class );

  /**
   * 
   * {@inheritDoc}
   */
  public ActionForward unspecified( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    return view( mapping, form, request, response );
  }

  /**
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   */
  public ActionForward view( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    if ( isFullPageView( request ) )
    {
      return mapping.findForward( ActionConstants.FULL_VIEW );
    }
    else
    {
      return mapping.findForward( ActionConstants.SHEET_VIEW );
    }
  }

  /**
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws IOException
   */
  public ActionForward fetchPromotionRules( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    RulesListView displayview = new RulesListView();

    if ( UserManager.getUser().isParticipant() )
    {
      List promotions = new ArrayList();
      RulesListView view = new RulesListView();
      try
      {
        promotions = getPromotionService().getPaxPromoRules( UserManager.getUser() );
      }
      catch( ServiceErrorException e )
      {
        logger.error( "Error:" + e );
      }

      for ( Object obj : promotions )
      {
        RulesView rule = new RulesView();
        Promotion promo = (Promotion)obj;
        rule.setId( promo.getId() );
        rule.setName( promo.getName() );
        rule.setContent( promo.getWebRulesText() );
        view.getPromotions().add( rule );
      }
      // Defect # 2085
      List<RulesView> proName = view.getPromotions();
      Collections.sort( proName, new Comparator()
      {
        public int compare( Object o1, Object o2 )
        {
          RulesView rule1 = (RulesView)o1;
          RulesView rule2 = (RulesView)o2;
          return rule1.getName().toLowerCase().compareTo( rule1.getName().toLowerCase() );
        }
      } );

      for ( RulesView rulesView : proName )
      {
        RulesView rule = new RulesView();
        rule.setId( rulesView.getId() );
        rule.setName( rulesView.getName() );
        rule.setContent( rulesView.getContent() );
        displayview.getPromotions().add( rule );
      }
    }

    super.writeAsJsonToResponse( displayview, response );
    return null;
  }

  private boolean isFullPageView( HttpServletRequest request )
  {
    boolean isFullPage = false;
    String fullPageView = request.getParameter( "isFullPage" );
    if ( !StringUtils.isEmpty( fullPageView ) )
    {
      isFullPage = Boolean.valueOf( fullPageView );
    }
    return isFullPage;
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

}
