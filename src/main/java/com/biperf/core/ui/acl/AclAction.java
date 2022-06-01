/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/acl/AclAction.java,v $
 */

package com.biperf.core.ui.acl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.exception.ConstraintViolationException;

import com.biperf.core.domain.user.Acl;
import com.biperf.core.service.security.AclService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * Action to manage ACL requests.
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
 * <td>crosenquest</td>
 * <td>Apr 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AclAction extends BaseDispatchAction
{

  /**
   * Prepare the display for updating an Acl.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    AclForm aclForm = (AclForm)form;
    aclForm.setUpdate( true );

    String forwardTo = ActionConstants.UPDATE_FORWARD;
    ActionMessages errors = new ActionMessages();

    Long aclId = null;

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
        aclId = (Long)clientStateMap.get( "aclId" );
      }
      catch( ClassCastException cce )
      {
        String s = (String)clientStateMap.get( "aclId" );
        aclId = new Long( s );
      }

      if ( aclId == null )
      {
        errors.add( "errorMessage", new ActionMessage( "user.acl.required.param.aclid.invalid" ) );
        return mapping.findForward( ActionConstants.FAIL_CREATE );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    catch( IllegalArgumentException e )
    {
      errors.add( "errorMessage", new ActionMessage( "user.acl.required.param.aclid.invalid" ) );
      forwardTo = ActionConstants.FAIL_CREATE;
    }

    Acl aclToUpdate = getAclService().getAclById( aclId );

    aclForm.load( aclToUpdate );
    aclForm.setMethod( "save" );

    request.setAttribute( AclForm.FORM_NAME, aclForm );

    ActionForward forward = mapping.findForward( forwardTo );

    // get the actionForward to display the create pages.
    return forward;
  }

  /**
   * Update the Acl with the data provided through the Form.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    ActionMessages errors = new ActionMessages();
    AclForm aclForm = (AclForm)form;

    String forwardTo = ActionConstants.SUCCESS_UPDATE;

    try
    {

      Acl acl = aclForm.toDomainObject();

      getAclService().saveAcl( acl );

    }
    catch( Exception e )
    {

      errors.add( "errorMessage", new ActionMessage( "acl.update.failed" ) );
      forwardTo = ActionConstants.FAIL_UPDATE;

    }

    if ( errors.size() != 0 )
    {
      saveErrors( request, errors );
    }
    else
    {

      aclForm = new AclForm();
      aclForm.setMethod( "search" );
      request.setAttribute( AclForm.FORM_NAME, aclForm );

    }

    return mapping.findForward( forwardTo );
  }

  /**
   * Forward to displayCreate pages.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    String forwardTo = ActionConstants.CREATE_FORWARD;

    AclForm aclForm = (AclForm)form;
    aclForm.load( new Acl() );
    aclForm.setMethod( "save" );
    aclForm.setUpdate( false );

    request.setAttribute( "aclForm", aclForm );

    ActionForward forward = mapping.findForward( forwardTo );

    return forward;
  }

  /**
   * Display the acls already configured within the application.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    String forwardTo = ActionConstants.SUCCESS_SEARCH;

    // determine what activity status to search for
    List aclList = getAclService().getAll();

    request.setAttribute( "aclList", aclList );

    ActionForward forward = mapping.findForward( forwardTo );

    return forward;
  }

  /**
   * Save the acl with the data provided from the form.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    ActionMessages errors = new ActionMessages();

    if ( isCancelled( request ) )
    {
      forwardTo = ActionConstants.CANCEL_FORWARD;
    }
    else
    {

      AclService aclService = getAclService();
      Acl acl = ( (AclForm)form ).toDomainObject();

      try
      {
        aclService.saveAcl( acl );
      }
      catch( ConstraintViolationException cve )
      {
        log.warn( "User tried to save an ACL with an already existing name: " + cve );
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "admin.acl.UNIQUE_CONSTRAINT" ) );

      }
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_FORWARD;
    }
    else
    {
      request.setAttribute( AclForm.FORM_NAME, new AclForm() );
    }

    ActionForward forward = mapping.findForward( forwardTo );

    return forward;
  }

  /**
   * Create the Acl from the user's form submission.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward create( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    String forwardTo = ActionConstants.SUCCESS_CREATE;

    ActionMessages errors = new ActionMessages();

    AclForm aclForm = (AclForm)form;

    Acl userAcl = aclForm.toDomainObject();
    userAcl.setVersion( null );

    try
    {
      getAclService().saveAcl( userAcl );
    }
    catch( Exception e )
    {

      errors.add( "errorMessage", new ActionMessage( "acl.save.failed" ) );
      forwardTo = ActionConstants.FAIL_CREATE;

    }

    if ( errors.size() != 0 )
    {
      saveErrors( request, errors );
    }
    else
    {
      request.setAttribute( AclForm.FORM_NAME, new AclForm() );
    }

    // get the actionForward to display the create pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * Forward to the displaySearch page.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displaySearch( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    AclForm aclForm = (AclForm)form;
    aclForm.load( new Acl() );
    aclForm.setMethod( "search" );

    request.setAttribute( AclForm.FORM_NAME, aclForm );

    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.SEARCH_FORWARD );
  }

  /**
   * Get the AclService.
   * 
   * @return AclService
   */
  private AclService getAclService()
  {
    return (AclService)getService( AclService.BEAN_NAME );
  }

}
