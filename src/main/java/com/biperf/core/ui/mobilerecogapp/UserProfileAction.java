
package com.biperf.core.ui.mobilerecogapp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.profile.PublicProfileAction;
import com.biperf.core.utils.UserManager;

public class UserProfileAction extends BaseDispatchAction
{

  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    HttpServletRequest requestWrapper = new HttpServletRequestWrapper( request )
    {

      @Override
      public String getParameter( String name )
      {
        if ( "participantId".equals( name ) )
        {
          return UserManager.getUserId().toString();
        }
        return this.getRequest().getParameter( name );
      }
    };

    PublicProfileAction ppa = new PublicProfileAction();
    return ppa.populatePax( mapping, form, requestWrapper, response );
  }

}
