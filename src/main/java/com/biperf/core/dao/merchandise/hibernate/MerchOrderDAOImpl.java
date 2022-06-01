/*
 * (c) 2006 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.merchandise.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.merchandise.MerchOrderDAO;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.user.PlateauRedemptionTracking;
import com.biperf.core.service.ProjectionCollection;
import com.biperf.core.utils.SqlQueryBuilder;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.MerchAwardReminderBean;

public class MerchOrderDAOImpl extends BaseDAO implements MerchOrderDAO
{
  private static final String MERCH_AWARD_REMINDER_QUERY = "select * " + "from ("
      + "select m.MERCH_ORDER_ID, m.GIFT_CODE, m.GIFT_CODE_KEY,p.PROMOTION_ID, p.PROMOTION_NAME, m.PARTICIPANT_ID, m.DATE_CREATED, m.EXPIRATION_DATE, m.DATE_LAST_REMINDED, pm.CM_ASSET_KEY "
      + "from merch_order m, promo_merch_program_level pm, promo_merch_country pc, promotion p " + "where m.MERCH_GIFT_CODE_TYPE='level' " + "and m.PARTICIPANT_ID =? " + "and m.IS_REDEEMED = 0 "
      + "and m.PROMO_MERCH_PROGRAM_LEVEL_ID = pm.PROMO_MERCH_PROGRAM_LEVEL_ID " + "and pm.PROMO_MERCH_COUNTRY_ID = pc.PROMO_MERCH_COUNTRY_ID "
      + "and pc.PROMOTION_ID = p.PROMOTION_ID  and m.ORDER_STATUS IS NULL " + "and p.promotion_type not in('goalquest','challengepoint') " + "UNION "
      + "select m.MERCH_ORDER_ID, m.GIFT_CODE, m.GIFT_CODE_KEY,p.PROMOTION_ID, p.PROMOTION_NAME, m.PARTICIPANT_ID, m.DATE_CREATED, m.EXPIRATION_DATE, m.DATE_LAST_REMINDED, NULL CM_ASSET_KEY "
      + "from merch_order m, activity_merch_order am, activity a, goalquest_paxgoal pg, goalquest_goallevel gl, promotion p " + "where m.PARTICIPANT_ID =? " + "and m.IS_REDEEMED = 0 "
      + "and m.merch_order_id = am.merch_order_id " + "and am.activity_id = a.activity_id " + "and p.promotion_id = a.promotion_id " + "and pg.goallevel_id = gl.goallevel_id "
      + "and pg.promotion_id = p.promotion_id " + "and m.ORDER_STATUS IS NULL " + "and p.promotion_type in('goalquest','challengepoint')) order by DATE_CREATED asc ";

  private JdbcTemplate jdbcTemplate;

  public MerchOrder getMerchOrderById( Long merchOrderId )
  {
    return (MerchOrder)getSession().get( MerchOrder.class, merchOrderId );
  }

  // Alerts Performance Tuning
  public MerchOrder getMerchOrderByIdWithProjections( Long merchOrderId, ProjectionCollection collection )
  {
    Criteria criteria = getSession().createCriteria( MerchOrder.class, "MerchOrder" );
    criteria.add( Restrictions.eq( "MerchOrder.id", merchOrderId ) );
    if ( null != collection )
    {
      collection.processProjections( criteria );
    }
    return (MerchOrder)criteria.uniqueResult();
  }

  public MerchOrder getMerchOrderByReferenceNumber( String referenceNumber )
  {
    MerchOrderActivityQueryConstraint constraint = new MerchOrderActivityQueryConstraint();
    constraint.setReferenceNumber( referenceNumber );
    List orders = getMerchOrderList( constraint );

    if ( !orders.isEmpty() && orders.size() == 1 )
    {
      return (MerchOrder)orders.get( 0 );
    }
    else
    {
      return null;
    }
  }

  public MerchOrder getMerchOrderByGiftCode( String giftCode )
  {
    if ( null == giftCode || ! ( giftCode.length() == 16 || giftCode.length() == 8 ) )
    {
      return null;
    }

    MerchOrderActivityQueryConstraint constraint = new MerchOrderActivityQueryConstraint();
    constraint.setGiftCodeDecrypted( giftCode.substring( 0, 8 ) );
    if ( giftCode.length() == 16 )
    {
      constraint.setGiftCodeKeyDecrypted( giftCode.substring( 8, giftCode.length() ) );
    }
    List orders = getMerchOrderList( constraint );

    if ( !orders.isEmpty() && orders.size() == 1 )
    {
      return (MerchOrder)orders.get( 0 );
    }
    else
    {
      return null;
    }
  }

  public List getMerchOrderList( MerchOrderActivityQueryConstraint queryConstraint )
  {
    return HibernateUtil.getObjectList( queryConstraint );
  }

  public MerchOrder saveMerchOrder( MerchOrder merchOrder )
  {
    return (MerchOrder)HibernateUtil.saveOrUpdateOrDeepMerge( merchOrder );
  }

  public PlateauRedemptionTracking savePlateauRedemptionTracking( PlateauRedemptionTracking plateauRedemptionTracking )
  {
    getSession().save( plateauRedemptionTracking );
    return plateauRedemptionTracking;

  }

  @SuppressWarnings( "unchecked" )
  public List<MerchAwardReminderBean> getMerchAwardReminders( Long participantId )
  {
    List bindVariables = new ArrayList();
    SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
    queryBuilder.append( MERCH_AWARD_REMINDER_QUERY );
    String queryString = queryBuilder.toString();
    bindVariables.add( participantId );
    bindVariables.add( participantId );

    RowMapper rowMapper = new MerchAwardReminderRowMapper();
    // Spring 2.5.3 onwards, RowMapperResultReader is not supported.
    // The method query takes in rowmapper directly, so we are removing RowMapperResultReader
    List<MerchAwardReminderBean> results = jdbcTemplate.query( queryString, bindVariables.toArray(), rowMapper );
    return results;
  }

  public List<MerchAwardReminderBean> getPlateauAwardRemindersForNodes( Collection<Long> nodeIds, Date pastDueDate )
  {
    final String SQL = "select * " + "from ("
        + "select m.MERCH_ORDER_ID, m.GIFT_CODE, m.GIFT_CODE_KEY,p.PROMOTION_ID, p.PROMOTION_NAME, m.PARTICIPANT_ID, m.DATE_CREATED, m.EXPIRATION_DATE, m.DATE_LAST_REMINDED, pm.CM_ASSET_KEY "
        + "from merch_order m, promo_merch_program_level pm, promo_merch_country pc, promotion p " + ", user_node un, participant pax " + "where m.MERCH_GIFT_CODE_TYPE='level' "
        + "and m.PARTICIPANT_ID = pax.USER_ID " + "and pax.status = 'active' " + "and un.IS_PRIMARY = 1 " + "and un.user_id = pax.user_id " + "and un.NODE_ID in (:nodeIds) " + "and m.IS_REDEEMED = 0 "
        + "and m.PROMO_MERCH_PROGRAM_LEVEL_ID = pm.PROMO_MERCH_PROGRAM_LEVEL_ID " + "and pm.PROMO_MERCH_COUNTRY_ID = pc.PROMO_MERCH_COUNTRY_ID "
        + "and NVL(m.EXPIRATION_DATE, TRUNC(SYSDATE)) >= TRUNC(SYSDATE) " + "and pc.PROMOTION_ID = p.PROMOTION_ID  and m.ORDER_STATUS IS NULL and TRUNC(m.DATE_CREATED) <= :pastDueDate "
        + " and p.promotion_type not in('goalquest','challengepoint') " + "UNION "
        + "select m.MERCH_ORDER_ID, m.GIFT_CODE, m.GIFT_CODE_KEY,p.PROMOTION_ID, p.PROMOTION_NAME, m.PARTICIPANT_ID, m.DATE_CREATED, m.EXPIRATION_DATE, m.DATE_LAST_REMINDED, NULL CM_ASSET_KEY "
        + "from merch_order m, activity_merch_order am, activity a, goalquest_paxgoal pg, goalquest_goallevel gl, promotion p " + ", user_node un, participant pax "
        + "WHERE m.PARTICIPANT_ID = pax.USER_ID " + "and pax.status = 'active' " + "and un.IS_PRIMARY = 1 " + "and un.user_id = pax.user_id " + "and un.NODE_ID in (:nodeIds) "
        + "and m.IS_REDEEMED = 0 " + "and m.merch_order_id = am.merch_order_id " + "and am.activity_id = a.activity_id " + "and p.promotion_id = a.promotion_id "
        + "and pg.goallevel_id = gl.goallevel_id " + "AND pg.promotion_id = p.promotion_id " + "and m.ORDER_STATUS IS NULL and TRUNC(m.DATE_CREATED) <= :pastDueDate "
        + "and NVL(m.EXPIRATION_DATE, TRUNC(SYSDATE)) >= TRUNC(SYSDATE) " + " and p.promotion_type in('goalquest','challengepoint')) order by DATE_CREATED asc ";

    NamedParameterJdbcTemplate npjdbcTemplate = new NamedParameterJdbcTemplate( jdbcTemplate.getDataSource() );
    MapSqlParameterSource parameters = new MapSqlParameterSource();
    parameters.addValue( "nodeIds", nodeIds );
    parameters.addValue( "pastDueDate", pastDueDate );

    RowMapper rowMapper = new MerchAwardReminderRowMapper();
    List<MerchAwardReminderBean> beans = npjdbcTemplate.query( SQL, parameters, rowMapper );

    return beans;
  }

  public void setDataSource( DataSource dataSource )
  {
    // TODO what if you want to use a JdbcTemplate by preference,
    // for a native extractor?
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  private class MerchAwardReminderRowMapper implements RowMapper<MerchAwardReminderBean>
  {
    /**
     * Overridden from
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     * @param rs
     * @param rowNum
     * @return Map valueMapByColumn
     * @throws SQLException
     */
    public MerchAwardReminderBean mapRow( ResultSet rs, int rowNum ) throws SQLException
    {
      MerchAwardReminderBean reminderBean = new MerchAwardReminderBean();
      reminderBean.setPromotionId( new Long( rs.getBigDecimal( "PROMOTION_ID" ).longValue() ) );
      reminderBean.setParticipantId( new Long( rs.getBigDecimal( "PARTICIPANT_ID" ).longValue() ) );
      reminderBean.setMerchOrderId( new Long( rs.getBigDecimal( "MERCH_ORDER_ID" ).longValue() ) );
      reminderBean.setCmAssetKey( rs.getString( "CM_ASSET_KEY" ) );
      reminderBean.setPromotionName( rs.getString( "PROMOTION_NAME" ) );
      reminderBean.setSubmittedDate( rs.getDate( "DATE_CREATED" ) );
      reminderBean.setExpirationDate( rs.getDate( "EXPIRATION_DATE" ) );
      reminderBean.setLastRemindedDate( rs.getDate( "DATE_LAST_REMINDED" ) );
      return reminderBean;
    }
  }

  public Long getNextBatchId()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.merchandise.getNextBatchId" );
    return (Long)query.uniqueResult();
  }

  public List<Long> getGiftCodeFailures( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.merchandise.getGiftCodeFailures" );
    query.setParameter( "promotionId", promotionId );
    return (List)query.list();
  }

  @Override
  public Long getPromotionIdByMerchOrderId( Long merchOrderId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.merchandise.getPromotionIdByMerchOrderId" );
    query.setParameter( "mid", merchOrderId );
    return (Long)query.uniqueResult();
  }

  @Override
  public List<Long> getMerchOrderIds()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.merchandise.getMerchOrderIds" );
    List<Long> results = query.list();
    return results;
  }

  @Override
  public List getMerchOrdersList( Long merchOrderId )
  {
    Criteria criteria = getSession().createCriteria( MerchOrder.class );
    criteria.add( Restrictions.eq( "id", merchOrderId ) );
    return criteria.list();
  }
  
  //Client customizations for wip #23129 starts
  public List getAllUnredeemedOrdersByPromotion( Long promoId, String mmyyyy )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.merchandise.MerchOrder.getAllUnredeemedOrdersByPromotion" );
    query.setParameter( "promoId", promoId );
    query.setParameter( "mmyyyy", mmyyyy );
    return query.list();
  }
  // Client customizations for wip #23129 ends
}
