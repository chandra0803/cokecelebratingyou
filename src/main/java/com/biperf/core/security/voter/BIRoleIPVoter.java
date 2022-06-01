
package com.biperf.core.security.voter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import com.biperf.core.domain.user.Role;
import com.biperf.core.security.admin.AdminSecurityAdvisorFactory;
import com.biperf.core.utils.UserManager;

/**
 * This class follows the standard Spring RoleVoter class, but also throws on checks for
 * URLs that are Client Admin or BI Admin restricted.  It's purpose is to DENY access to 
 * these resources if the requestor's IP address doesn't come from one of the approved list
 */
public class BIRoleIPVoter implements AccessDecisionVoter<Object> // InitializingBean
{
  private AdminSecurityAdvisorFactory adminSecurityAdvisorFactory;
  // ~ Instance fields
  // ================================================================================================
  private Map<String, Role> roleMap = new ConcurrentHashMap<String, Role>();
  private String rolePrefix = "ROLE_";

  // ~ Methods
  // ========================================================================================================

  public String getRolePrefix()
  {
    return rolePrefix;
  }

  /**
   * Allows the default role prefix of <code>ROLE_</code> to be overridden.
   * May be set to an empty value, although this is usually not desirable.
   *
   * @param rolePrefix the new prefix
   */
  public void setRolePrefix( String rolePrefix )
  {
    this.rolePrefix = rolePrefix;
  }

  public boolean supports( ConfigAttribute attribute )
  {
    return attribute.getAttribute() != null && attribute.getAttribute().startsWith( getRolePrefix() );
  }

  /**
   * This implementation supports any type of class, because it does not query
   * the presented secure object.
   *
   * @param clazz the secure object
   *
   * @return always <code>true</code>
   */
  public boolean supports( Class<?> clazz )
  {
    return true;
  }

  public int vote( Authentication authentication, Object object, Collection<ConfigAttribute> attributes )
  {
    if ( authentication == null )
    {
      return ACCESS_DENIED;
    }

    int result = ACCESS_ABSTAIN;
    Collection<? extends GrantedAuthority> authorities = extractAuthorities( authentication );

    for ( ConfigAttribute attribute : attributes )
    {
      if ( this.supports( attribute ) )
      {
        result = ACCESS_DENIED;
        // Attempt to find a matching granted authority
        for ( GrantedAuthority authority : authorities )
        {
          if ( attribute.getAttribute().equals( authority.getAuthority() ) )
          {
            // if admin, check IP?
            if ( isAdminRestrictedURl( authorities ) )
            {
              if ( isValidAdminIP( (FilterInvocation)object ) )
              {
                return ACCESS_GRANTED; // admin user is coming from an approved IP address
              }
              else
              {
                return ACCESS_DENIED; // admin user is coming from an outside IP address
              }
            }
            else // non-admin url, proceed
            {
              return ACCESS_GRANTED;
            }
          }
        }
      }
    }
    return result;
  }

  protected boolean isAdminRestrictedURl( Collection<? extends GrantedAuthority> authorities )
  {
    for ( GrantedAuthority authority : authorities )
    {
      if ( roleMap.containsKey( authority.getAuthority() ) )
      {
        return true;
      }
    }

    return false;
  }

  /*
   * This method determines if the IP is acceptable. However there are a few situations short-cuts
   * to this: 1. Participants can ALWAYS access the URL 2. /homePage.do is a special case. The
   * forward needs to go through the IP check 3. /logout.do - no need to check this, everyone can
   * logout!
   */
  protected boolean isValidAdminIP( FilterInvocation filterInvocation )
  {
    // allow all pax access to the URLs (this is odd, we're giving more access to the client pax)
    if ( UserManager.getUser() != null && UserManager.getUser().isParticipant() )
    {
      return true;
    }
    String url = filterInvocation.getRequestUrl();
    // special switch case here for the home page
    if ( url.toLowerCase().matches( "/homepage.do*" ) )
    {
      if ( UserManager.getUser() != null && UserManager.getUser().isUser() )
      {
        // validate IP
        return adminSecurityAdvisorFactory.getInstance().isValidAdminIp( filterInvocation.getHttpRequest() );
      }
    }
    // logout can always be accessed
    if ( url.toLowerCase().matches( "/logout.do*" ) )
    {
      return true;
    }
    // validate the IP here
    return adminSecurityAdvisorFactory.getInstance().isValidAdminIp( filterInvocation.getHttpRequest() );
  }

  Collection<? extends GrantedAuthority> extractAuthorities( Authentication authentication )
  {
    return authentication.getAuthorities();
  }

  public void setRoles( List<Role> roles )
  {
    for ( Role role : roles )
    {
      this.roleMap.put( rolePrefix + role.getCode(), role );
    }
  }

  public void setAdminSecurityAdvisorFactory( AdminSecurityAdvisorFactory adminSecurityAdvisorFactory )
  {
    this.adminSecurityAdvisorFactory = adminSecurityAdvisorFactory;
  }

}
