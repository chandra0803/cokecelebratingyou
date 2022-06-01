
package com.biperf.core.utils;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.value.UserJSResources;

public interface JSResourceLocatorFactory
{

  static final String BEAN_NAME = "jSResourceLocatorFactory";

  UserJSResources getUserJSResources( HttpServletRequest httpRequest );

  UserJSResources getUserJSResourcesByAuthenticatedUser();

}
