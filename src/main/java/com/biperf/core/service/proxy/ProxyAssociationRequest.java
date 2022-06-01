/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/proxy/ProxyAssociationRequest.java,v $
 */

package com.biperf.core.service.proxy;

import java.util.Iterator;

import org.hibernate.Hibernate;

import com.biperf.core.domain.proxy.Proxy;
import com.biperf.core.domain.proxy.ProxyModule;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * ProxyAssociationRequest.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>sedey</td>
 * <td>Nov 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProxyAssociationRequest extends BaseAssociationRequest
{
  protected int hydrateLevel = 0;

  /**
   * Hyrate Level: ALL
   */
  public static final int ALL = 1;

  /**
   * Hyrate Level: ProxyModules
   */
  public static final int PROXY_MODULE = 2;

  /**
   * Hyrate Level: ProxyModulePromotions
   */
  public static final int PROXY_MODULE_PROMOTION = 3;

  /**
   * Hydrate Level: User and Proxy User Address
   */
  public static final int PROXY_USER_ADDRESS = 4;

  /**
   * Hydrate Level: User and Proxy User Employer
   */
  public static final int PROXY_USER_EMPLOYER_INFO = 5;

  /**
   * Constructor with hydrateLevel as arg
   * 
   * @param hydrateLevel
   */
  public ProxyAssociationRequest( int hydrateLevel )
  {
    super();
    this.hydrateLevel = hydrateLevel;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    Proxy proxy = (Proxy)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateProxyModules( proxy );
        hydrateProxyModulePromotions( proxy );
        break;

      case PROXY_MODULE:
        hydrateProxyModules( proxy );
        break;

      case PROXY_MODULE_PROMOTION:
        hydrateProxyModulePromotions( proxy );
        break;
      case PROXY_USER_ADDRESS:
        hydrateUserAndProxyUserAddresses( proxy );
        break;
      case PROXY_USER_EMPLOYER_INFO:
        hydrateUserAndProxyUserEmployerInfo( proxy );
        break;
      default:
        break;
    } // switch
  }

  /**
   * Loads the proxyModules for a proxy
   * 
   * @param proxy
   */
  private void hydrateProxyModules( Proxy proxy )
  {
    initialize( proxy.getProxyModules() );
  }

  /**
   * Loads the proxyModulePromotions for a proxy
   * 
   * @param proxy
   */
  private void hydrateProxyModulePromotions( Proxy proxy )
  {
    Iterator proxyModuleIter = proxy.getProxyModules().iterator();
    while ( proxyModuleIter.hasNext() )
    {
      ProxyModule proxyModule = (ProxyModule)proxyModuleIter.next();
      initialize( proxyModule.getProxyModulePromotions() );
    }
  }

  private void hydrateUserAndProxyUserAddresses( Proxy proxy )
  {
    Hibernate.initialize( proxy.getProxyUser().getUserAddresses() );
  }

  private void hydrateUserAndProxyUserEmployerInfo( Proxy proxy )
  {
    Hibernate.initialize( proxy.getProxyUser().getParticipantEmployers() );
  }

}
