
package com.biperf.core.dao.ssi.hibernate;

import java.util.Arrays;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.ssi.SSIPromotionDAO;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.ssi.SSIPromotion;

public class SSIPromotionDAOImpl extends BaseDAO implements SSIPromotionDAO
{

  @Override
  public SSIPromotion getLiveOrCompletedSSIPromotion()
  {
    Criteria criteria = getSession().createCriteria( SSIPromotion.class );
    criteria.add( Restrictions.eq( "promotionType", PromotionType.lookup( PromotionType.SELF_SERV_INCENTIVES ) ) );
    criteria.add( Restrictions.in( "promotionStatus", Arrays.asList( PromotionStatusType.lookup( PromotionStatusType.LIVE ), PromotionStatusType.lookup( PromotionStatusType.COMPLETE ) ) ) );
    Object obj = criteria.uniqueResult();
    if ( obj != null )
    {
      return (SSIPromotion)obj;
    }
    return null;
  }

  @Override
  public SSIPromotion getLiveSSIPromotion()
  {
    Criteria criteria = getSession().createCriteria( SSIPromotion.class );
    criteria.add( Restrictions.eq( "promotionType", PromotionType.lookup( PromotionType.SELF_SERV_INCENTIVES ) ) );
    criteria.add( Restrictions.eq( "promotionStatus", PromotionStatusType.lookup( PromotionStatusType.LIVE ) ) );
    Object obj = criteria.uniqueResult();
    if ( obj != null )
    {
      return (SSIPromotion)obj;
    }
    return null;
  }

}
