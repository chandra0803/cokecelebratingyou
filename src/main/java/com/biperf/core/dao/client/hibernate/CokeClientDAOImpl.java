
package com.biperf.core.dao.client.hibernate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.client.CallPrcAdihPayrollExtract;
import com.biperf.core.dao.client.CokeClientDAO;
import com.biperf.core.domain.client.RecognizeAnyone;
import com.biperf.core.domain.client.TccNomLevelPayout;
import com.biperf.core.domain.client.TcccApproverList;
import com.biperf.core.domain.client.TcccCurrencyExchange;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.value.ClaimRecipientValueBean;
import com.biperf.core.value.client.CokeNominationFileUploadValueBean;
import com.biperf.core.value.client.TcccLevelPayoutValueBean;

/**
 * CokeClientDAOImpl.
 * 
 * @author dudam
 * @since Feb 19, 2018
 * @version 1.0
 * 
 * This DAO is created as part of WIP #42701 
 */
public class CokeClientDAOImpl extends BaseDAO implements CokeClientDAO
{
	  private DataSource dataSource;

	  /**
	   * set the data source.
	   * 
	   * @param dataSource
	   */
	  public void setDataSource( DataSource dataSource )
	  {
	    this.dataSource = dataSource;
	  }

  // Client customization for WIP #42701 starts
  @Override
  public String getApproverUserName( Long promotionId, String divisionNumber )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.client.getDivisionApprover" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "divisionNumber", divisionNumber );
    return (String)query.uniqueResult();
  }
  // Client customization for WIP 58122
  public String getApproverUserNameForLevel( Long promotionId, String divisionNumber ,Long level)
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.client.getApproverUserNameForLevel" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "divisionNumber", divisionNumber );
    query.setParameter( "level", level );
    return (String)query.uniqueResult();
  }
  /*
  @SuppressWarnings( "unchecked" )
  @Override
  public List<TcccApproverMatrix> getLevelApprovers( Long promotionId, String divisionNumber )
  {
    Criteria criteria = getSession().createCriteria( TcccApproverMatrix.class,"TcccApproverMatrix" );
    criteria.createAlias( "TcccApproverMatrix.promotion", "promotion" );
    criteria.add( Restrictions.eq( "promotion.id", promotionId ) );
    criteria.add( Restrictions.eq( "divisionNumber", divisionNumber ) );
    criteria.add( Restrictions.isNull( "dateEnd" ) );
    return criteria.list();
  }
  */

  @SuppressWarnings( "unchecked" )
  @Override
  public List<TcccApproverList> getApproverByParticipantLoginID( String participantLoginID )
  {
    Criteria criteria = getSession().createCriteria( TcccApproverList.class );
    criteria.add( Restrictions.eq( "participantLoginID", participantLoginID ) );
    return criteria.list();
  }

  @Override
  public TcccCurrencyExchange getCurrencyExchange( String currencyCode )
  {
    Criteria criteria = getSession().createCriteria( TcccCurrencyExchange.class );
    criteria.add( Restrictions.eq( "currency", currencyCode ) );
    criteria.add( Restrictions.isNull( "dateEnd" ) );
    return (TcccCurrencyExchange)criteria.uniqueResult();
  }

  public Double getCurrencyExchangeRate( String currencyCode )
  {
    Query query = getSession().createQuery( " SELECT tce.exchangeRate FROM TcccCurrencyExchange tce WHERE tce.currency=:currencyCode AND tce.dateEnd IS NULL " );
    query.setParameter( "currencyCode", currencyCode );
    return (Double)query.uniqueResult();
  }

  public Double getUserBudgetMediaValue( Long userId )
  {
    Query query = getSession().createSQLQuery( " select c.BUDGET_MEDIA_VALUE from COUNTRY c, USER_ADDRESS ua where c.COUNTRY_ID=ua.COUNTRY_ID and ua.IS_PRIMARY=1 and ua.USER_ID=:userId " );
    query.setParameter( "userId", userId );
    return ( (BigDecimal)query.uniqueResult() ).doubleValue();
  }
  // Client customization for WIP #42701 ends

  // Client customization for WIP #43735 starts
  @SuppressWarnings( "unchecked" )
  @Override
  public List<ClaimRecipientValueBean> getYetToClaimAwards( Long participantId )
  {
    Query query = getSession().createSQLQuery( " SELECT cr.claim_item_id,cr.participant_id,cr.node_id,cr.adih_opt_out,cr.adih_cash_award_qty,"
        + " cr.adih_cash_currency,cr.adih_cash_division_num,cr.adih_cash_pax_claim,p.promotion_name,c.submission_date,c.claim_id,c.date_modified "
        + " FROM claim_recipient cr, claim_item ci, claim c, promotion p WHERE "
        + " cr.claim_item_id = ci.claim_item_id AND ci.claim_id = c.claim_id AND c.promotion_id = p.promotion_id AND "
        + " ci.approval_status_type = 'approv' AND c.is_open = 0 AND p.promotion_type = 'recognition' AND p.adih_cash_option = 1 AND "
        + " cr.adih_cash_pax_claim = 0 AND cr.participant_id =:participantId ORDER BY cr.claim_item_id " );
    query.setParameter( "participantId", participantId );
    query.setResultTransformer( new ClaimRecipientResultTransformer() );
    return query.list();
  }
  // Client customization for WIP 58122
  public List<ClaimRecipientValueBean> getYetToClaimAwardsForNomination( Long participantId )
  {
    Query query = getSession().createSQLQuery(" SELECT cr.claim_item_id,cp.participant_id,cp.node_id,ci.ADIH_LEVEL_SELECT, " 
            + " p.promotion_name,c.submission_date,c.claim_id,c.date_modified  FROM claim_recipient cr,PROMO_NOMINATION pn, "
            + "claim_item ci, claim c, promotion p,CLAIM_PARTICIPANT cp WHERE  cr.claim_item_id = ci.claim_item_id  AND "
   		 + "ci.claim_id = c.claim_id AND c.promotion_id = pn.PROMOTION_ID AND c.claim_id=cp.CLAIM_ID AND "
   		 + "pn.AWARD_GROUP_TYPE='both' AND c.promotion_id = p.promotion_id AND "
   		 + "ci.approval_status_type = 'winner' AND c.is_open = 0 AND p.promotion_type = 'nomination' AND "
   		 + "cp.participant_id =:participantId AND cr.participant_id = cp.participant_id AND p.PROMOTION_STATUS='live' AND "
   		 + "ci.ADIH_LEVEL_SELECT is not NULL AND cp.ADIH_LEVEL_PAX_CLAIM=0 "
            + "ORDER BY cr.claim_item_id " );
    query.setParameter( "participantId", participantId );
    query.setResultTransformer( new ClaimRecipientResultTransformerForNomination() );
    return query.list();
  }
  
  private class ClaimRecipientResultTransformerForNomination extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      ClaimRecipientValueBean bean = new ClaimRecipientValueBean();
      bean.setClaimItemId( ( (BigDecimal)tuple[0] ).longValue() );
      bean.setParticipantId( ( (BigDecimal)tuple[1] ).longValue() );
      bean.setNodeId( ( (BigDecimal)tuple[2] ).longValue() );
      bean.setPromotionName( (String)tuple[4] );
      bean.setSubmissionDate( (Date)tuple[5] );
      bean.setClaimId( ( (BigDecimal)tuple[6] ).longValue() );
      bean.setApprovedOn( (Date)tuple[7] );
      return bean;
    }
  }
  
  private class ClaimRecipientResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      ClaimRecipientValueBean bean = new ClaimRecipientValueBean();
      bean.setClaimItemId( ( (BigDecimal)tuple[0] ).longValue() );
      bean.setParticipantId( ( (BigDecimal)tuple[1] ).longValue() );
      bean.setNodeId( ( (BigDecimal)tuple[2] ).longValue() );
      bean.setOptOut( Boolean.valueOf( (String)tuple[3] ) );
      bean.setCashAwardQty( ( (BigDecimal)tuple[4] ).longValue() );
      bean.setCashCurrency( (String)tuple[5] );
      bean.setCashDivision( (String)tuple[6] );
      bean.setCashPaxClaimed( ( (BigDecimal)tuple[7] ).longValue() == 1 ? Boolean.TRUE : Boolean.FALSE );
      bean.setPromotionName( (String)tuple[8] );
      bean.setSubmissionDate( (Date)tuple[9] );
      bean.setClaimId( ( (BigDecimal)tuple[10] ).longValue() );
      bean.setApprovedOn( (Date)tuple[11] );
      return bean;
    }
  }
  // Client customization for WIP #43735 ends

@Override
public Map launchAndProcessCokePayrollFileExtract() {
	CallPrcAdihPayrollExtract proc = new CallPrcAdihPayrollExtract(dataSource);
	return proc.executeProcedure();
}

/* TCCC  - customization end */
@Override
public void saveRecognizeAnyoneEmail(RecognizeAnyone recognizeAnyone) {
	getSession().saveOrUpdate( recognizeAnyone );
 }
/* TCCC  - customization end */

  // Client customization start - WIP 58122
  @Override
  public List<TccNomLevelPayout> getLevelTotalPoints( Long promotionId )
  {
    Query query = null;
    query = getSession().getNamedQuery( "com.biperf.core.domain.client.getLevelTotalPoints" );
    query.setParameter( "promotionId", promotionId );
    List<TccNomLevelPayout> nomLevelPayouts = query.list();
    return nomLevelPayouts;
  }

  public TccNomLevelPayout save( TccNomLevelPayout tccNomLevelPayout )
  {

    Session session = HibernateSessionManager.getSession();
    try
    {
      session.saveOrUpdate( tccNomLevelPayout );
    }
    catch( NonUniqueObjectException e )
    {
      tccNomLevelPayout = (TccNomLevelPayout)session.merge( tccNomLevelPayout );
    }
    // Do a flush to force create of a history record
    session.flush();

    return tccNomLevelPayout;
  }

  public TccNomLevelPayout getLevelPayoutById( Long levelPayoutId )
  {
    Session session = HibernateSessionManager.getSession();
    TccNomLevelPayout tccNomLevelPayout = (TccNomLevelPayout)session.get( TccNomLevelPayout.class, levelPayoutId );
    return tccNomLevelPayout;
  }

  public List<TcccLevelPayoutValueBean> getLevelPayoutByPromotionId( String promotionId )
  {
    Query query = getSession()
        .createSQLQuery( " select level_payout_id,promotion_id,level_description,total_points from adih_level_payout where promotion_id =:promotionId order by level_payout_id " );
    query.setParameter( "promotionId", promotionId );
    query.setResultTransformer( new LevelPayoutResultTransformer() );
    return query.list();
  }

  private class LevelPayoutResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      TcccLevelPayoutValueBean bean = new TcccLevelPayoutValueBean();
      bean.setLevelPayoutId( ( (BigDecimal)tuple[0] ).longValue() );
      bean.setPromotionId( ( (String)tuple[1] ) );
      bean.setLevelDescription( ( (String)tuple[2] ) );
      bean.setTotalPoints( ( (BigDecimal)tuple[3] ).longValue() );

      return bean;
    }
  }
  // Client customization end - WIP 58122

  // Client customization for WIP #59420 starts
  public CokeNominationFileUploadValueBean getNominationFileUploadDetails( Long promotionId )
  {
    Query query = getSession()
        .createSQLQuery( " select promotion_id,adih_upload_file_types,adih_file_min_number,adih_file_max_number from promo_nomination where promotion_id=:promotionId " );
    query.setParameter( "promotionId", promotionId );
    query.setResultTransformer( new FileUploadResultTransformer() );
    return (CokeNominationFileUploadValueBean)query.uniqueResult();
  }

  private class FileUploadResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      CokeNominationFileUploadValueBean bean = new CokeNominationFileUploadValueBean();
      bean.setPromotionId( ( (BigDecimal)tuple[0] ).longValue() );
      bean.setAllowedFileTypes( (String)tuple[1] );
      bean.setFileMinNumber( ( (BigDecimal)tuple[2] ).intValue() );
      bean.setFileMaxNumber( ( (BigDecimal)tuple[3] ).intValue() );
      return bean;
    }
  }
  // Client customization for WIP #59420 ends

}
