/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/security/CmsAuthorizationUtil.java,v $
 *
 */

package com.biperf.core.security;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.biperf.core.service.security.AuthorizationService;

/**
 * CmsAuthorizationUtil <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Sep 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class CmsAuthorizationUtil
{
  // Roles from Cms
  public static GrantedAuthority CMS_CONTENT_ADMIN = new SimpleGrantedAuthority( "ROLE_ContentAdministrator" );
  public static GrantedAuthority CMS_CONTENT_APPROVER = new SimpleGrantedAuthority( "ROLE_ContentApprover" );
  public static GrantedAuthority CMS_CONTENT_EDITOR = new SimpleGrantedAuthority( "ROLE_ContentEditor" );
  public static GrantedAuthority CMS_CONTENT_VIEWER = new SimpleGrantedAuthority( "ROLE_ContentViewer" );
  public static GrantedAuthority CMS_FILE_MANAGER = new SimpleGrantedAuthority( "ROLE_FileManager" );
  public static GrantedAuthority CMS_FILE_APPROVER = new SimpleGrantedAuthority( "ROLE_FileApprover" );
  public static GrantedAuthority CMS_CUSTOM_CONTENT = new SimpleGrantedAuthority( "ROLE_CustomContentManager" );
  public static GrantedAuthority CMS_CONTENT_TRANSLATOR = new SimpleGrantedAuthority( "ROLE_ContentTranslator" );

  public static String DEFAULT_AUDIENCE_NAME = "Default Audience";
  public static String USER_AUDIENCE_NAME = "UserAudience";
  public static String PARTICIPANT_AUDIENCE_NAME = "ParticipantAudience";

  /**
   * @param authorities to be updated
   */
  public static void addCmsInvocationRolesToGrantedAuthority( Set authorities )
  {
    authorities.add( CMS_CONTENT_ADMIN );
    authorities.add( CMS_CONTENT_APPROVER );
    authorities.add( CMS_CONTENT_EDITOR );
    authorities.add( CMS_CONTENT_VIEWER );
    authorities.add( CMS_FILE_MANAGER );
    authorities.add( CMS_CUSTOM_CONTENT );
    authorities.add( CMS_CONTENT_TRANSLATOR );
  }

  /**
   * @param authorities to be updated
   */
  public static void addCmsRolesToGrantedAuthority( Set authorities )
  {
    Set roleCodes = getRoleCodesFromAuthorities( authorities );
    if ( roleCodes.contains( AuthorizationService.ROLE_CODE_LOGIN_AS ) )
    {
      // when Login-As, always get Viewer only
      authorities.add( CMS_CONTENT_VIEWER );
    }
    else if ( roleCodes.contains( AuthorizationService.ROLE_CODE_BI_ADMIN ) || roleCodes.contains( AuthorizationService.ROLE_CODE_PROJ_MGR )
        || roleCodes.contains( AuthorizationService.ROLE_CODE_CONTENT_ADMINISTRATOR ) )
    {
      // All CM roles
      authorities.add( CMS_CONTENT_ADMIN );
      authorities.add( CMS_CONTENT_APPROVER );
      authorities.add( CMS_CONTENT_EDITOR );
      authorities.add( CMS_CONTENT_VIEWER );
      authorities.add( CMS_FILE_MANAGER );
      authorities.add( CMS_FILE_APPROVER );
      authorities.add( CMS_CUSTOM_CONTENT );
      authorities.add( CMS_CONTENT_TRANSLATOR );
    }
    else if ( roleCodes.contains( AuthorizationService.ROLE_CODE_PROCESS_TEAM ) )
    {
      // ProcessTeam gets all CM Roles except ContentAdministrator
      authorities.add( CMS_CONTENT_APPROVER );
      authorities.add( CMS_CONTENT_EDITOR );
      authorities.add( CMS_CONTENT_VIEWER );
      authorities.add( CMS_FILE_MANAGER );
      authorities.add( CMS_FILE_APPROVER );
      authorities.add( CMS_CUSTOM_CONTENT );
    }
    else
    {
      // default is viewer only
      authorities.add( CMS_CONTENT_VIEWER );
    }
  }

  private static Set getRoleCodesFromAuthorities( Set authorities )
  {
    Set result = new HashSet();
    for ( Iterator i = authorities.iterator(); i.hasNext(); )
    {
      GrantedAuthority auth = (GrantedAuthority)i.next();
      String code = auth.getAuthority();
      if ( code.startsWith( "ROLE_" ) )
      {
        result.add( code.substring( 5 ) );
      }
    }
    return result;
  }

}