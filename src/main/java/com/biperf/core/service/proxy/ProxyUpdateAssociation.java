/**
 * 
 */

package com.biperf.core.service.proxy;

import java.util.Iterator;

import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.proxy.Proxy;
import com.biperf.core.domain.proxy.ProxyModule;
import com.biperf.core.domain.proxy.ProxyModulePromotion;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.utils.BeanLocator;

/**
 * ProxyUpdateAssociation.
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
 * <td>Nov 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProxyUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * Constructor
   * 
   * @param proxy
   */
  public ProxyUpdateAssociation( Proxy proxy )
  {
    super( proxy );
  }

  /**
   * execute the association request and update the specified proxy object into the associated proxy
   * object
   * 
   * @param attachedDomain
   */
  public void execute( BaseDomain attachedDomain )
  {
    Proxy attachedProxy = (Proxy)attachedDomain;

    updateProxy( attachedProxy );
  }

  private void updateProxy( Proxy attachedProxy )
  {
    Proxy detachedProxy = (Proxy)getDetachedDomain();

    if ( detachedProxy.getId() == null || detachedProxy.getId().longValue() == 0 )
    {
      // only for a new proxy will the user have to be looked up

      attachedProxy.setUser( getParticipantDAO().getParticipantById( detachedProxy.getUser().getId() ) );
      attachedProxy.setProxyUser( getParticipantDAO().getParticipantById( detachedProxy.getProxyUser().getId() ) );
    }
    attachedProxy.setAllModules( detachedProxy.isAllModules() );
    attachedProxy.setCoreAccess( detachedProxy.getCoreAccess() );
    attachedProxy.setAllowLeaderboard( detachedProxy.isAllowLeaderboard() );

    // Iterate over the detached modules and update as needed
    if ( detachedProxy.getProxyModules() != null && detachedProxy.getProxyModules().size() > 0 )
    {
      // remove any modules and promotions from the attached that are not in the detached
      if ( attachedProxy.getProxyModules() != null && attachedProxy.getProxyModules().size() > 0 )
      {
        Iterator attachedModuleIter = attachedProxy.getProxyModules().iterator();
        while ( attachedModuleIter.hasNext() )
        {
          ProxyModule attachedModule = (ProxyModule)attachedModuleIter.next();
          if ( !detachedProxy.getProxyModules().contains( attachedModule ) )
          {
            attachedModuleIter.remove();
          }
          else
          {
            Iterator detachedModIter = detachedProxy.getProxyModules().iterator();
            while ( detachedModIter.hasNext() )
            {
              ProxyModule detachedModule = (ProxyModule)detachedModIter.next();
              if ( detachedModule.getId().equals( attachedModule.getId() ) )
              {
                Iterator attachedProxyPromoIter = attachedModule.getProxyModulePromotions().iterator();
                while ( attachedProxyPromoIter.hasNext() )
                {
                  ProxyModulePromotion attachedPromo = (ProxyModulePromotion)attachedProxyPromoIter.next();
                  if ( !detachedModule.getProxyModulePromotions().contains( attachedPromo ) )
                  {
                    attachedProxyPromoIter.remove();
                  }
                }
              }
            }
          }
        }
      }

      // Add the new modules and promotions
      Iterator detachedModuleIter = detachedProxy.getProxyModules().iterator();
      while ( detachedModuleIter.hasNext() )
      {
        ProxyModule detachedModule = (ProxyModule)detachedModuleIter.next();
        if ( attachedProxy.getProxyModules() != null && attachedProxy.getProxyModules().size() > 0 )
        {
          if ( attachedProxy.getProxyModules().contains( detachedModule ) )
          {
            Iterator attachedModuleIter = attachedProxy.getProxyModules().iterator();
            while ( attachedModuleIter.hasNext() )
            {
              ProxyModule attachedModule = (ProxyModule)attachedModuleIter.next();
              if ( attachedModule.getId().equals( detachedModule.getId() ) )
              {
                attachedModule.setAllPromotions( detachedModule.isAllPromotions() );
                if ( attachedModule.isAllPromotions() )
                {
                  attachedModule.getProxyModulePromotions().clear();
                }
                else
                {
                  if ( detachedModule.getProxyModulePromotions() != null && detachedModule.getProxyModulePromotions().size() > 0 )
                  {
                    Iterator detachedPromoIter = detachedModule.getProxyModulePromotions().iterator();
                    while ( detachedPromoIter.hasNext() )
                    {
                      ProxyModulePromotion detachedModulePromotion = (ProxyModulePromotion)detachedPromoIter.next();
                      if ( attachedModule.getProxyModulePromotions() != null && attachedModule.getProxyModulePromotions().size() > 0 )
                      {
                        if ( !attachedModule.getProxyModulePromotions().contains( detachedModulePromotion ) )
                        {
                          detachedModulePromotion.setPromotion( getPromotionDAO().getPromotionById( detachedModulePromotion.getPromotion().getId() ) );
                          attachedModule.addProxyModulePromotion( detachedModulePromotion );
                        }
                      }
                    }
                  }
                  else
                  {
                    attachedModule.getProxyModulePromotions().clear();
                  }
                }
              }
            }
          }
          else
          {
            if ( detachedModule.getProxyModulePromotions() != null && detachedModule.getProxyModulePromotions().size() > 0 )
            {
              Iterator detachedModulePromoIter = detachedModule.getProxyModulePromotions().iterator();
              while ( detachedModulePromoIter.hasNext() )
              {
                ProxyModulePromotion detachedModulePromo = (ProxyModulePromotion)detachedModulePromoIter.next();
                detachedModulePromo.setPromotion( getPromotionDAO().getPromotionById( detachedModulePromo.getPromotion().getId() ) );
              }
            }
            attachedProxy.addProxyModule( detachedModule );
          }
        }
        else
        {
          if ( detachedModule.getProxyModulePromotions() != null && detachedModule.getProxyModulePromotions().size() > 0 )
          {
            Iterator detachedModulePromoIter = detachedModule.getProxyModulePromotions().iterator();
            while ( detachedModulePromoIter.hasNext() )
            {
              ProxyModulePromotion detachedModulePromo = (ProxyModulePromotion)detachedModulePromoIter.next();
              detachedModulePromo.setPromotion( getPromotionDAO().getPromotionById( detachedModulePromo.getPromotion().getId() ) );
            }
          }
          attachedProxy.addProxyModule( detachedModule );
        }
      }
    }
    else
    {
      attachedProxy.getProxyModules().clear();
    }
  }

  /**
   * Retrieves a UserDAO
   * 
   * @return UserDAO
   */
  private ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)BeanLocator.getBean( ParticipantDAO.BEAN_NAME );
  }

  /**
   * Retrieves a PromotionDAO
   * 
   * @return PromotionDAO
   */
  private PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)BeanLocator.getBean( PromotionDAO.BEAN_NAME );
  }
}
