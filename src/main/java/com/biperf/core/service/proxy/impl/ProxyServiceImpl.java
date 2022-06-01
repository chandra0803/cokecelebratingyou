/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/proxy/impl/ProxyServiceImpl.java,v $
 */

package com.biperf.core.service.proxy.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.promotion.hibernate.NominationPromotionQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.ProductClaimPromotionQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.RecognitionPromotionQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.SSIPromotionQueryConstraint;
import com.biperf.core.dao.proxy.ProxyDAO;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.proxy.Proxy;
import com.biperf.core.domain.proxy.ProxyModule;
import com.biperf.core.domain.proxy.ProxyModulePromotion;
import com.biperf.core.domain.proxy.ProxyUsersView;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.proxy.ProxyAssociationRequest;
import com.biperf.core.service.proxy.ProxyService;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;

/**
 * ProxyServiceImpl.
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
public class ProxyServiceImpl implements ProxyService
{
  private ProxyDAO proxyDAO = null;
  private PromotionService promotionService = null;

  public ProxyDAO getProxyDAO()
  {
    return proxyDAO;
  }

  public void setProxyDAO( ProxyDAO proxyDAO )
  {
    this.proxyDAO = proxyDAO;
  }

  public PromotionService getPromotionService()
  {
    return promotionService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.proxy.ProxyService#getProxyById(java.lang.Long)
   * @param id
   * @return Proxy
   */
  public Proxy getProxyById( Long id )
  {
    return proxyDAO.getProxyById( id );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.proxy.ProxyService#getProxyByIdWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return Proxy
   */
  public Proxy getProxyByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    return this.proxyDAO.getProxyByIdWithAssociations( id, associationRequestCollection );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.proxy.ProxyService#getProxyByUserAndProxyUser(java.lang.Long,
   *      java.lang.Long)
   * @param userId
   * @param proxyUserId
   * @return Proxy
   */
  public Proxy getProxyByUserAndProxyUser( Long userId, Long proxyUserId )
  {
    return getProxyByUserAndProxyUserWithAssociations( userId, proxyUserId, null );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.proxy.ProxyService#getProxyByUserAndProxyUserWithAssociations(java.lang.Long,
   *      java.lang.Long, com.biperf.core.service.AssociationRequestCollection)
   * @param userId
   * @param proxyUserId
   * @param associationRequestCollection
   * @return Proxy
   */
  public Proxy getProxyByUserAndProxyUserWithAssociations( Long userId, Long proxyUserId, AssociationRequestCollection associationRequestCollection )
  {
    return this.proxyDAO.getProxyByUserAndProxyUserWithAssociations( userId, proxyUserId, associationRequestCollection );
  }

  /**
   * {@inheritDoc}
   */
  public ProxyUsersView getCreatedProxyView( Proxy proxy )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ProxyAssociationRequest( ProxyAssociationRequest.PROXY_USER_ADDRESS ) );
    associationRequestCollection.add( new ProxyAssociationRequest( ProxyAssociationRequest.PROXY_USER_EMPLOYER_INFO ) );
    proxy = this.proxyDAO.getProxyByIdWithAssociations( proxy.getId(), associationRequestCollection );
    List<Proxy> proxies = new ArrayList<Proxy>();
    proxies.add( proxy );
    ProxyUsersView view = new ProxyUsersView( proxies );
    return view;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.proxy.ProxyService#getProxiesByUserId(java.lang.Long)
   * @param userId
   * @return List
   */
  public List getProxiesByUserId( Long userId )
  {
    List proxies = this.proxyDAO.getProxiesByUserId( userId );

    return proxies;
  }

  /**
   * @param userId
   * @return
   */
  public List getProxiesByUserIdWithAssociation( Long userId, AssociationRequestCollection associationRequestCollection )
  {
    List proxies = this.proxyDAO.getProxiesByUserId( userId );
    for ( Iterator proxy = proxies.iterator(); proxy.hasNext(); )
    {
      Proxy proxyItem = (Proxy)proxy.next();
      for ( Iterator iterator = associationRequestCollection.iterator(); iterator.hasNext(); )
      {
        AssociationRequest req = (AssociationRequest)iterator.next();
        req.execute( proxyItem );
      }
    }
    return proxies;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.proxy.ProxyService#getUsersByProxyUserId(java.lang.Long)
   * @param proxyUserId
   * @return List
   */
  public List getUsersByProxyUserId( Long proxyUserId )
  {
    return this.proxyDAO.getUsersByProxyUserId( proxyUserId );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.proxy.ProxyService#saveProxy(com.biperf.core.domain.proxy.Proxy)
   * @param proxy
   * @return Proxy
   * @throws ServiceErrorException
   */
  public Proxy saveProxy( Proxy proxy ) throws ServiceErrorException
  {
    Proxy savedProxy = this.proxyDAO.save( proxy );

    return savedProxy;
  }

  /**
   * Saves the proxy with an association to the database.
   * 
   * @param proxyId
   * @param updateAssociationRequest
   * @return Proxy
   * @throws com.biperf.core.exception.ServiceErrorException
   */
  public Proxy saveProxy( Long proxyId, UpdateAssociationRequest updateAssociationRequest ) throws ServiceErrorException
  {
    if ( updateAssociationRequest != null )
    {
      Proxy proxy = null;

      if ( proxyId != null && proxyId.longValue() != 0 )
      {
        proxy = this.getProxyById( proxyId );
      }
      else
      {
        proxy = new Proxy();
      }

      updateAssociationRequest.execute( proxy );

      return saveProxy( proxy );
    }

    return null;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.proxy.ProxyService#getRecognitionPromotionsByPax(com.biperf.core.domain.participant.Participant)
   * @param pax
   * @return List
   */
  public List getRecognitionPromotionsByPax( Participant pax )
  {
    RecognitionPromotionQueryConstraint recQueryConstraint = new RecognitionPromotionQueryConstraint();
    recQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );
    return getPromotionsByTypeAndPax( pax, recQueryConstraint );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.proxy.ProxyService#getProductClaimPromotionsByPax(com.biperf.core.domain.participant.Participant)
   * @param pax
   * @return List
   */
  public List getProductClaimPromotionsByPax( Participant pax )
  {
    ProductClaimPromotionQueryConstraint masterOnlyProductClaimPromoQueryConstraint = new ProductClaimPromotionQueryConstraint();
    masterOnlyProductClaimPromoQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );
    masterOnlyProductClaimPromoQueryConstraint.setMasterOrChildConstraint( Boolean.TRUE );

    return getPromotionsByTypeAndPax( pax, masterOnlyProductClaimPromoQueryConstraint );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.proxy.ProxyService#getNominationPromotionsByPax(com.biperf.core.domain.participant.Participant)
   * @param pax
   * @return List
   */
  public List getNominationPromotionsByPax( Participant pax )
  {
    NominationPromotionQueryConstraint nomQueryConstraint = new NominationPromotionQueryConstraint();
    nomQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );
    return getPromotionsByTypeAndPax( pax, nomQueryConstraint );
  }

  public List getSSIPromotionsByPax( Participant pax )
  {
    SSIPromotionQueryConstraint ssiQueryConstraint = new SSIPromotionQueryConstraint();
    ssiQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );
    return getPromotionsByTypeAndPax( pax, ssiQueryConstraint );
  }

  private List getPromotionsByTypeAndPax( Participant pax, PromotionQueryConstraint queryConstraint )
  {
    List availablePromos = new ArrayList();

    List promos = getPromotionService().getPromotionList( queryConstraint );
    for ( int i = 0; i < promos.size(); i++ )
    {
      Promotion promotion = (Promotion)promos.get( i );

      if ( checkIfShowSubmitClaim( promotion, pax ) )
      {
        availablePromos.add( promotion );
      }
    }

    return availablePromos;
  }

  private boolean checkIfShowSubmitClaim( Promotion promotion, Participant participant )
  {
    return getPromotionService().isPromotionClaimableByParticipant( promotion.getId(), participant );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.proxy.ProxyService#deleteProxy(java.lang.Long)
   * @param proxyId
   * @throws ServiceErrorException
   */
  public void deleteProxy( Long proxyId ) throws ServiceErrorException
  {
    Proxy proxyToDelete = this.proxyDAO.getProxyById( proxyId );

    this.proxyDAO.delete( proxyToDelete );
  }

  /**
   * Deletes a list of proxies. Overridden from
   * 
   * @see com.biperf.core.service.proxy.ProxyService#deleteProxies(java.util.List)
   * @param proxyIdList - List of proxy.id
   * @throws ServiceErrorException
   */
  public void deleteProxies( List proxyIdList ) throws ServiceErrorException
  {
    Iterator proxyIdIter = proxyIdList.iterator();

    while ( proxyIdIter.hasNext() )
    {
      Proxy proxyToDelete = this.proxyDAO.getProxyById( (Long)proxyIdIter.next() );
      if ( proxyToDelete != null )
      {
        this.proxyDAO.delete( proxyToDelete );
      }
    }
  }

  /**
   * Overridden from @see com.biperf.core.service.proxy.ProxyService#getDuplicateProxyUserCount(java.lang.Long, java.lang.Long)
   * @param userId
   * @param proxyUserId
   * @return Long
   */
  public Long getDuplicateProxyUserCount( Long userId, Long proxyUserId )
  {
    return this.proxyDAO.getDuplicateProxyUserCount( userId, proxyUserId );
  }

  public List<PromotionMenuBean> getPromotionsAllowedForDelegate( List<PromotionMenuBean> promotions, Proxy proxy )
  {
    List<PromotionMenuBean> filteredPromotions = new ArrayList<PromotionMenuBean>();

    if ( proxy.isAllModules() )
    {
      return promotions;
    }
    else
    {
      Set<ProxyModule> allowedModules = proxy.getProxyModules();
      for ( ProxyModule allowedModule : allowedModules )
      {
        if ( allowedModule.getPromotionType() != null )
        {
          for ( PromotionMenuBean promotion : promotions )
          {
            if ( allowedModule.getPromotionType().getCode().equalsIgnoreCase( PromotionType.NOMINATION ) && allowedModule.isAllPromotions() && promotion.getPromotion().isNominationPromotion() )
            {
              filteredPromotions.add( promotion );
            }
            else if ( allowedModule.getPromotionType().getCode().equalsIgnoreCase( PromotionType.SELF_SERV_INCENTIVES ) && allowedModule.isAllPromotions()
                && promotion.getPromotion().isSSIPromotion() )
            {
              filteredPromotions.add( promotion );
            }
            else if ( allowedModule.getPromotionType().getCode().equalsIgnoreCase( PromotionType.RECOGNITION ) && allowedModule.isAllPromotions() && promotion.getPromotion().isRecognitionPromotion() )
            {
              filteredPromotions.add( promotion );
            }
            else if ( allowedModule.getPromotionType().getCode().equalsIgnoreCase( PromotionType.PRODUCT_CLAIM ) && allowedModule.isAllPromotions()
                && promotion.getPromotion().isProductClaimPromotion() )
            {
              filteredPromotions.add( promotion );
            }
            else if ( !allowedModule.isAllPromotions() )
            {
              Set<ProxyModulePromotion> allowedModulePromotions = allowedModule.getProxyModulePromotions();
              for ( ProxyModulePromotion proxyPromotion : allowedModulePromotions )
              {
                if ( proxyPromotion.getPromotion().getId().intValue() == promotion.getPromotion().getId().intValue() )
                {
                  filteredPromotions.add( promotion );
                  break;
                }
              }
            }
          }
        }
      }
    }

    return filteredPromotions;
  }

  public List<Promotion> getPromotionsAllowedForDelegateEZ( List<Promotion> promotions, Proxy proxy )
  {
    List<Promotion> filteredPromotions = new ArrayList<Promotion>();

    if ( proxy.isAllModules() )
    {
      return promotions;
    }
    else
    {
      Set<ProxyModule> allowedModules = proxy.getProxyModules();
      for ( ProxyModule allowedModule : allowedModules )
      {
        if ( allowedModule.getPromotionType() != null )
        {
          for ( Promotion promotion : promotions )
          {
            if ( allowedModule.getPromotionType().getCode().equalsIgnoreCase( PromotionType.NOMINATION ) && allowedModule.isAllPromotions() && promotion.isNominationPromotion() )
            {
              filteredPromotions.add( promotion );
            }
            else if ( allowedModule.getPromotionType().getCode().equalsIgnoreCase( PromotionType.SELF_SERV_INCENTIVES ) && allowedModule.isAllPromotions() && promotion.isSSIPromotion() )
            {
              filteredPromotions.add( promotion );
            }
            else if ( allowedModule.getPromotionType().getCode().equalsIgnoreCase( PromotionType.RECOGNITION ) && allowedModule.isAllPromotions() && promotion.isRecognitionPromotion() )
            {
              filteredPromotions.add( promotion );
            }
            else
            {
              if ( !allowedModule.isAllPromotions() )
              {
                Set<ProxyModulePromotion> allowedModulePromotions = allowedModule.getProxyModulePromotions();
                for ( ProxyModulePromotion proxyPromotion : allowedModulePromotions )
                {
                  if ( proxyPromotion.getPromotion().getId().intValue() == promotion.getId().intValue() )
                  {
                    filteredPromotions.add( promotion );
                    break;
                  }
                }
              }
            }
          }
        }
      }
    }

    return filteredPromotions;
  }

  public boolean isRecognitionAllowedForDelegate()
  {
    Proxy proxy = null;
    AssociationRequestCollection proxyAssociationRequestCollection = new AssociationRequestCollection();
    proxyAssociationRequestCollection.add( new ProxyAssociationRequest( ProxyAssociationRequest.PROXY_MODULE ) );
    proxy = this.getProxyByUserAndProxyUserWithAssociations( UserManager.getUserId(), UserManager.getUser().getOriginalAuthenticatedUser().getUserId(), proxyAssociationRequestCollection );

    if ( proxy.isAllModules() )
    {
      return true;
    }
    else
    {
      Set<ProxyModule> allowedModules = proxy.getProxyModules();
      for ( ProxyModule allowedModule : allowedModules )
      {
        if ( allowedModule.getPromotionType() != null )
        {
          if ( allowedModule.getPromotionType().getCode().equalsIgnoreCase( PromotionType.RECOGNITION ) )
          {
            return true;
          }
        }
      }
    }
    return false;
  }

}
