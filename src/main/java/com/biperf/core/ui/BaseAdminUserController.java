/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/BaseController.java,v $
 */

package com.biperf.core.ui;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.utils.UserManager;

public abstract class BaseAdminUserController extends BaseController
{
  protected void setContactMethods( HttpServletRequest request )
  {
    if ( UserManager.isUserInRole( AuthorizationService.ROLE_CODE_MODIFY_RECOVERY_CONTACTS ) )
    {
      request.setAttribute( "emailTypeList", EmailAddressType.getList() );
      request.setAttribute( "phoneTypeList", PhoneType.getList() );
    }
    else
    {
      request.setAttribute( "emailTypeList", EmailAddressType.getPrimaryList() );
      request.setAttribute( "phoneTypeList", PhoneType.getPrimaryList() );
    }
  }
}
