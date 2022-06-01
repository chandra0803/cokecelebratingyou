/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/promotion/sweepstakes/CreateWinnersController.java,v $
 */

package com.biperf.core.ui.promotion.sweepstakes;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * CreateWinnersController.
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
 * <td>Nnov 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CreateWinnersController extends BaseController
{
  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Fetches data for the Create Winners List page.
   *
   * @param tileContext the context for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the context for servlets of this web application.
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    RequestWrapper requestWrapper = new RequestWrapper( request );

    Long promotionId = requestWrapper.getPromotionId();
    Promotion promotion = getPromotionService().getPromotionById( promotionId );

    // Initialize the create winners form.
    CreateWinnersForm createWinnersForm = requestWrapper.getCreateWinnersForm();
    createWinnersForm.setPromotionId( promotionId.toString() );
    request.setAttribute( CreateWinnersForm.FORM_NAME, createWinnersForm );

    // Initialize request attributes used by the create winners form.
    request.setAttribute( "promotion", promotion );
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Get the promotionService from the beanFactory.
   * 
   * @return PromotionService
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  // ---------------------------------------------------------------------------
  // Inner Classes
  // ---------------------------------------------------------------------------

  /**
   * Adds behavior needed by the <code>CreateWinnersController</code> class to
   * a {@link HttpServletRequest} object.
   */
  private static class RequestWrapper extends HttpServletRequestWrapper
  {
    /**
     * Constructs a <code>RequestWrapper</code> object.
     *
     * @param request  the <code>HttpServletRequest</code> to be wrapped.
     */
    RequestWrapper( HttpServletRequest request )
    {
      super( request );
    }

    /**
     * Returns a promotion ID.
     *
     * @return a promotion ID.
     */
    public Long getPromotionId()
    {
      Long promotionId = null;
      try
      {
        String clientState = RequestUtils.getRequiredParamString( (HttpServletRequest)getRequest(), "clientState" );
        String cryptoPass = RequestUtils.getOptionalParamString( (HttpServletRequest)getRequest(), "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();
        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        // Deserialize the client state.
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        try
        {
          promotionId = (Long)clientStateMap.get( "promotionId" );
        }
        catch( ClassCastException cce )
        {
          String promotionIdString = (String)clientStateMap.get( "promotionId" );
          promotionId = new Long( promotionIdString );
        }
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
      return promotionId;
    }

    /**
     * Returns the create winners form associated with this HTTP request. If none
     * exists, this method creates one.
     *
     * @return the create winners form associated with this HTTP request.
     */
    CreateWinnersForm getCreateWinnersForm()
    {
      CreateWinnersForm createWinnersForm = (CreateWinnersForm)getAttribute( CreateWinnersForm.FORM_NAME );
      if ( createWinnersForm == null )
      {
        createWinnersForm = new CreateWinnersForm();
      }

      return createWinnersForm;
    }
  }
}
