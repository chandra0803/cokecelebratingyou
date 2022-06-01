
package com.biperf.core.dao.ecard.hibernate;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.ecard.ECardDAO;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.value.ecard.OwnCardImageData;

@Component
public class ECardDAOImpl extends BaseDAO implements ECardDAO
{
  private static final Log logger = LogFactory.getLog( ECardDAOImpl.class );

  private DataSource dataSource;
  private JdbcTemplate jdbcTemplate;

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  @Override
  public void save( ECard ecard )
  {
    getSession().saveOrUpdate( ecard );
  }

  @Override
  public Long getCardById( Long id )
  {
    ECard ecard = (ECard)getSession().get( ECard.class, id );
    return ecard != null ? ecard.getVersion() : 0;
  }

  /*
   * Below methods are used for own card migration. These methods only used for migration alone, so
   * kept in the thin layer instead of appropriate claims.
   */

  @Override
  public List<OwnCardImageData> getNotMigratedRecogOwnCardData()
  {
    getSession().flush();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getOwnCards" );
    query.setResultTransformer( Transformers.aliasToBean( OwnCardImageData.class ) );
    return (List<OwnCardImageData>)query.list();
  }

  @Override
  public void updateOwnCard( Long claimId, String cardUrl )
  {
    String query = "update recognition_claim set own_card_name = ? where claim_id = ?";

    Object[] params = { cardUrl, claimId };

    try
    {
      jdbcTemplate.update( query, params );
    }
    catch( DataAccessException e )
    {
      logger.error( "DAO Layer, the Recog claim id : " + claimId + " : " + e );
    }

  }

  @Override
  public List<OwnCardImageData> getNotMigratedNomOwnCardData()
  {
    getSession().flush();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getOwnCardsForNomination" );
    query.setResultTransformer( Transformers.aliasToBean( OwnCardImageData.class ) );
    return (List<OwnCardImageData>)query.list();
  }

  @Override
  public void updateOwnCardForNomination( long claimId, String cardUrl )
  {
    String query = "update nomination_claim set own_card_name = ? where claim_id = ?";

    Object[] params = { cardUrl, claimId };

    try
    {
      jdbcTemplate.update( query, params );
    }
    catch( DataAccessException e )
    {
      logger.error( "DAO Layer, the Nom claim id : " + claimId + " : " + e );
    }

  }

  /*
   * Above methods are used for own card migration. These methods only used for migration alone, so
   * kept in the thin layer instead of appropriate claims.
   */
}
