/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/proxy/ProxyService.java,v $
 */

package com.biperf.core.service.proxy;

import java.util.List;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.proxy.Proxy;
import com.biperf.core.domain.proxy.ProxyUsersView;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.value.PromotionMenuBean;

/**
 * ProxyService.
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
public interface ProxyService extends SAO
{
  public static final String BEAN_NAME = "proxyService";

  /**
   * Get the Proxy from the database by the id.
   * 
   * @param id
   * @return Proxy
   */
  public Proxy getProxyById( Long id );

  /**
   * Get the Proxy with associations from the database by the id.
   * 
   * @param id
   * @param associationRequestCollection
   * @return Proxy
   */
  public Proxy getProxyByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Get proxy by user and proxyUser Id
   * 
   * @param userId
   * @param proxyUserId
   * @return Proxy
   */
  public Proxy getProxyByUserAndProxyUser( Long userId, Long proxyUserId );

  /**
   * Get proxy by user and proxyUser id with associations
   * 
   * @param userId
   * @param proxyUserId
   * @param associationRequestCollection
   * @return Proxy
   */
  public Proxy getProxyByUserAndProxyUserWithAssociations( Long userId, Long proxyUserId, AssociationRequestCollection associationRequestCollection );

  public ProxyUsersView getCreatedProxyView( Proxy proxy );

  /**
   * Get all proxies by userid
   * 
   * @param userId
   * @return List
   */
  public List getProxiesByUserId( Long userId );

  /**
   * @param userId
   * @return
   */
  public List getProxiesByUserIdWithAssociation( Long userId, AssociationRequestCollection associationRequestCollection );

  /**
   * Get all users with proxyUserId
   * 
   * @param proxyUserId
   * @return List
   */
  public List getUsersByProxyUserId( Long proxyUserId );

  /**
   * Saves the proxy to the database.
   * 
   * @param proxy
   * @return Proxy
   * @throws ServiceErrorException
   */
  public Proxy saveProxy( Proxy proxy ) throws ServiceErrorException;

  /**
   * Saves the proxy with an assocition to the database.
   * 
   * @param proxyId
   * @param updateAssociationRequest
   * @return Proxy
   * @throws ServiceErrorException
   */
  public Proxy saveProxy( Long proxyId, UpdateAssociationRequest updateAssociationRequest ) throws ServiceErrorException;

  /**
   * Deletes the Proxy from the database.
   * 
   * @param proxyId
   * @throws ServiceErrorException
   */
  public void deleteProxy( Long proxyId ) throws ServiceErrorException;

  /**
   * Returns a list of claimable recognition promotions
   * 
   * @param pax
   * @return List
   */
  public List getRecognitionPromotionsByPax( Participant pax );

  /**
   * Returns a list of claimable nomination promotions
   * 
   * @param pax
   * @return List
   */
  public List getNominationPromotionsByPax( Participant pax );

  public List getSSIPromotionsByPax( Participant pax );

  /**
   * Returns a list of claimable product claim promotions
   * 
   * @param pax
   * @return List
   */
  public List getProductClaimPromotionsByPax( Participant pax );

  /**
   * Deletes a list of Proxies from the database.
   * 
   * @param proxyIdList
   * @throws ServiceErrorException
   */
  public void deleteProxies( List proxyIdList ) throws ServiceErrorException;

  /**
   * @param userId
   * @param proxyUserId
   * @return Long
   */
  public Long getDuplicateProxyUserCount( Long userId, Long proxyUserId );

  public List<PromotionMenuBean> getPromotionsAllowedForDelegate( List<PromotionMenuBean> promotions, Proxy proxy );

  public List<Promotion> getPromotionsAllowedForDelegateEZ( List<Promotion> promotions, Proxy proxy );

  public boolean isRecognitionAllowedForDelegate();

}
