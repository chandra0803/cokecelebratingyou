/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/multimedia/hibernate/MultimediaDAOImpl.java,v $
 */

package com.biperf.core.dao.multimedia.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.multimedia.MultimediaDAO;
import com.biperf.core.domain.multimedia.Card;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.multimedia.EcardLocale;
import com.biperf.core.domain.multimedia.VideoUploadDetail;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * MultimediaDAOImpl.
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
 * <td>zahler</td>
 * <td>Oct 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class MultimediaDAOImpl extends BaseDAO implements MultimediaDAO
{
  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.multimedia.MultimediaDAO#getAllActiveECards()
   * @return List
   */
  public List getAllActiveECards()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.multimedia.AllActiveECards" );

    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.multimedia.MultimediaDAO#getCardById(java.lang.Long)
   * @param cardId
   * @return Card
   */
  public Card getCardById( Long cardId )
  {
    Card card = (Card)getSession().get( Card.class, cardId );

    return card;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.multimedia.MultimediaDAO#saveCard(com.biperf.core.domain.multimedia.Card)
   * @param card
   * @return Card
   */
  public Card saveCard( Card card )
  {
    return (Card)HibernateUtil.saveOrUpdateOrShallowMerge( card );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.multimedia.MultimediaDAO#getAllActiveECards()
   * @return List
   */
  public List getAllActiveMobileECards()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.multimedia.AllActiveMobileECards" );

    return query.list();
  }

  @Override
  public VideoUploadDetail saveVideoUploadDetail( VideoUploadDetail videoUploadDetail )
  {
    return (VideoUploadDetail)HibernateUtil.saveOrUpdateOrShallowMerge( videoUploadDetail );
  }

  @Override
  public VideoUploadDetail getVideoUploadDetailById( Long videoUploadDetailId )
  {
    return (VideoUploadDetail)getSession().get( VideoUploadDetail.class, videoUploadDetailId );
  }

  @Override
  public String getThumbnailByVideo( String videoUrl )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.multimedia.thumbnailByVideo" );
    query.setString( "videoUrl", videoUrl );
    return (String)query.uniqueResult();
  }

  @Override
  public Boolean isEcardExistForLocale( Long cardId, String recipientLocale )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.multimedia.IsEcardExistForLocale" );
    query.setLong( "cardId", cardId );
    query.setString( "locale", recipientLocale );
    return query.list().size() > 0 ? Boolean.TRUE : Boolean.FALSE;
  }

  @Override
  public List<Long> getAllECardIds()
  {
    getSession().flush();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.multimedia.getAllECardIds" );
    return query.list();
  }

  @Override
  public ECard getECardById( Long cardId )
  {
    return (ECard)getSession().get( ECard.class, cardId );
  }

  @Override
  public void saveEcardLocale( EcardLocale locale )
  {
    getSession().saveOrUpdate( locale );
  }

}
