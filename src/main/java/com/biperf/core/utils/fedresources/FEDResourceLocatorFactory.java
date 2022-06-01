
package com.biperf.core.utils.fedresources;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.service.SAO;

public interface FEDResourceLocatorFactory extends SAO
{
  public static final String BEAN_NAME = "fedResourceLocatorFactory";

  public UserFEDResources getUserFEDResources( HttpServletRequest httpRequest );

  public UserFEDResources getUserFEDResourcesByAuthenticatedUser();

}
