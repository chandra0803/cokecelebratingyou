
package com.biperf.core.dao.mtc.hibernate;

import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.mtc.MTCVideoDAO;
import com.biperf.core.domain.mtc.MTCVideo;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

public class MTCVideoDAOImpl extends BaseDAO implements MTCVideoDAO
{
  private static final Log logger = LogFactory.getLog( MTCVideoDAOImpl.class );

  @Override
  public MTCVideo save( MTCVideo mtcVideo )
  {
    return (MTCVideo)HibernateUtil.saveOrUpdateOrShallowMerge( mtcVideo );
  }

  @Override
  public MTCVideo getMTCVideoByRequestId( String requestId )
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( MTCVideo.class );
    criteria.add( Restrictions.eq( "requestId", requestId ) );

    return (MTCVideo)criteria.uniqueResult();
  }

  @Override
  public MTCVideo save( String requestId, Map<String, Object> details )
  {
    MTCVideo mtcVideo = new MTCVideo();
    mtcVideo.setRequestId( requestId );
    mtcVideo.setMp4Url( (String)details.get( "mp4" ) );
    mtcVideo.setWebmUrl( (String)details.get( "webm" ) );
    mtcVideo.setThumbNailImageUrl( (String)details.get( "thumbnail" ) );
    mtcVideo.setExpiryDate( (Date)details.get( "expiryDate" ) );
    mtcVideo.setOriginalFormat( (String)details.get( "originalFormat" ) );
    return (MTCVideo)HibernateUtil.saveOrUpdateOrShallowMerge( mtcVideo );
  }

  @Override
  public String getMTCVideoByRequestIdAndType( String requestId, String type )
  {
    MTCVideo mtc = getMTCVideoByRequestId( requestId );
    
    return type.equals( "mp4" ) ? mtc.getMp4Url() : mtc.getWebmUrl();
  }

}
