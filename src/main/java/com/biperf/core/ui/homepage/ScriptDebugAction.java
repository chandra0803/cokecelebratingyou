/*
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.homepage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.utils.WebResponseConstants;

/**
 * This action is used for setting js debug on user session, to allow debugging without having to 
 * change the system variable and impacting all users.
 *
 */
public class ScriptDebugAction extends BaseDispatchAction
{
  public ActionForward unspecified( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return reset( mapping, form, request, response );
  }

  // reset frontEndDebug attribute from session
  public ActionForward reset( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    request.getSession().removeAttribute( "isfrontEndDebug" );

    super.writeAsJsonToResponse( buildResponse( String.valueOf( Boolean.FALSE ) ), response );
    return null;
  }

  // set frontEndDebug to true for the session to load individual js for debugging in ctech env
  public ActionForward update( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    request.getSession().setAttribute( "isfrontEndDebug", Boolean.TRUE );

    super.writeAsJsonToResponse( buildResponse( String.valueOf( Boolean.TRUE ) ), response );
    return null;
  }

  private WebErrorMessageList buildResponse( String text )
  {
    WebErrorMessageList messages = new WebErrorMessageList();

    WebErrorMessage message = new WebErrorMessage();
    message.setType( WebResponseConstants.RESPONSE_TYPE_SUCCESS );
    message.setText( text );
    messages.getMessages().add( message );

    return messages;
  }

}
