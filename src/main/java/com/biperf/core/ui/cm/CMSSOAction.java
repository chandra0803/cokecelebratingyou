
package com.biperf.core.ui.cm;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.security.CMSSOToken;
import com.objectpartners.cms.util.security.CMTokenBuilder;

/**
 * The Action class for SSO into Content Manager.
 */
public class CMSSOAction extends BaseDispatchAction
{
  public ActionForward sso( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    String password = request.getContextPath() + CmsUtil.getCmsProperty( "sso.salt" );
    CMTokenBuilder builder = new CMTokenBuilder();
    String userName = UserManager.getUserName();
    CMSSOToken token = new CMSSOToken( "beacon", userName, new Date() );

    String encryptedToken = builder.formatEncrypted( token, password );
    encryptedToken = URLEncoder.encode( encryptedToken, "UTF-8" );
    String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "-cm/sso.do?appcode=beacon&token=" + encryptedToken;

    response.sendRedirect( url );

    return null;
  }
}
