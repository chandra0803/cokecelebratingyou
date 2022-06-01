
package com.biperf.core.security.admin;

public class AdminSecurityAdvisorFactoryImpl implements AdminSecurityAdvisorFactory
{
  private AdminSecurityAdvisor adminSecurityAdvisor;

  @Override
  public AdminSecurityAdvisor getInstance()
  {
    return adminSecurityAdvisor;
  }

  public AdminSecurityAdvisor getAdminSecurityAdvisor()
  {
    return adminSecurityAdvisor;
  }

  public void setAdminSecurityAdvisor( AdminSecurityAdvisor adminSecurityAdvisor )
  {
    this.adminSecurityAdvisor = adminSecurityAdvisor;
  }

}
