
package com.biperf.core.dao.promotion.hibernate;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.promotion.CelebrationManagerMessageDAO;
import com.biperf.core.domain.promotion.CelebrationManagerMessage;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.CelebrationManagerReminderBean;

public class CelebrationManagerMessageDAOImpl extends BaseDAO implements CelebrationManagerMessageDAO
{
  public CelebrationManagerMessage saveCelebrationManagerMessage( CelebrationManagerMessage celebrationManagerMessage )
  {
    return (CelebrationManagerMessage)HibernateUtil.saveOrUpdateOrShallowMerge( celebrationManagerMessage );
  }

  public CelebrationManagerMessage getCelebrationManagerMessageById( Long id )
  {
    return (CelebrationManagerMessage)getSession().get( CelebrationManagerMessage.class, id );
  }

  public List<CelebrationManagerReminderBean> getCelebrationManagerRemindersList( Long participantId )
  {
    String sql = buildCelebrationManagerRemindersListQuery();
    Query query = getSession().createSQLQuery( sql );
    query.setParameter( "userId", participantId );
    query.setResultTransformer( new CelebrationManagerReminderBeanMapper() );
    return query.list();
  }

  private String buildCelebrationManagerRemindersListQuery()
  {
    StringBuffer sql = new StringBuffer();

    sql.append( "select cm.celebration_manager_message_id, " );
    sql.append( " cm.recipient_id, " );
    sql.append( " cm.promotion_id " );
    sql.append( " from celebration_manager_message cm , application_user au " );
    sql.append( " where trunc(cm.message_collect_expire_date) >= trunc(sysdate) " );
    sql.append( " and (( manager_id = :userId and manager_message is null) " );
    sql.append( " or ( manager_above_id = :userId and manager_above_message is null)) " );
    sql.append( " and au.user_id = cm.recipient_id " );
    sql.append( " and au.is_active = 1 " );

    return sql.toString();
  }

  private class CelebrationManagerReminderBeanMapper extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      CelebrationManagerReminderBean managerValue = new CelebrationManagerReminderBean();
      managerValue.setManagerMessageId( extractLong( tuple[0] ) );
      managerValue.setRecipientId( extractLong( tuple[1] ) );
      managerValue.setPromotionId( extractLong( tuple[2] ) );
      return managerValue;
    }
  }

  public List<CelebrationManagerMessage> getCelebrationManagerByPromotion( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.CelebrationManagerMessage.getCelebrationManagerByPromotion" );
    query.setParameter( "promotionId", promotionId );

    return query.list();
  }

  @Override
  public String getServiceAnniversaryEcardOrDefaultCelebrationEcard( String ecardFlashName, Long promotionId, String locale )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.CelebrationManagerMessage.getDefaultCelebrationEcard" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "locale", locale );
    if ( StringUtils.isNotEmpty( ecardFlashName ) )
    {
      query.setParameter( "ecardFlashName", "%" + ecardFlashName + "%" );
    }
    else
    {
      query.setParameter( "ecardFlashName", ecardFlashName );
    }

    return (String)query.uniqueResult();
  }

}
