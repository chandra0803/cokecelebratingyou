
package com.biperf.core.security.admin;

import javax.servlet.ServletRequest;

public interface AdminSecurityAdvisor
{
  public boolean isValidAdminIp( ServletRequest servletRequest );
}
