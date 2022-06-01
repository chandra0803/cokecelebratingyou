/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/country/hibernate/CountryDAOImpl.java,v $
 */

package com.biperf.core.dao.country.hibernate;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.reports.hibernate.BaseReportsResultTransformer;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.CountryStatusType;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.CountryValueBean;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * CountryDAOImpl.
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
 * <td>Jun 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class CountryDAOImpl extends BaseDAO implements CountryDAO
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.country.CountryDAO#getCountryById(java.lang.Long)
   * @param id
   * @return Country
   */
  public Country getCountryById( Long id )
  {

    return (Country)getSession().get( Country.class, id );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.country.CountryDAO#getCountryByCode(java.lang.String)
   * @param countryCode
   * @return Country
   */
  public Country getCountryByCode( String countryCode )
  {

    return (Country)getSession().getNamedQuery( "com.biperf.core.domain.country.CountryByCode" ).setString( "countryCode", countryCode.toLowerCase() ).uniqueResult();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.country.CountryDAO#getCountryByAwardbanqAbbrev(java.lang.String)
   * @param awardbanqAbbrev
   * @return Country
   */
  public Country getCountryByAwardbanqAbbrev( String awardbanqAbbrev )
  {

    return (Country)getSession().getNamedQuery( "com.biperf.core.domain.country.CountryByAwardbanqAbbrev" ).setString( "awardbanqAbbrev", awardbanqAbbrev.toUpperCase() ).uniqueResult();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.country.CountryDAO#getAll()
   * @return List
   */
  public List getAll()
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.country.AllCountryList" ).list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.country.CountryDAO#getAllActive()
   * @return List
   */
  public List<Country> getAllActive()
  {

    return getSession().getNamedQuery( "com.biperf.core.domain.country.AllActiveCountryList" ).list();
  }

  /**
   * 
   * Overridden from @see com.biperf.core.dao.country.CountryDAO#getActiveCountriesForAllActivePax()
   * @return
   */
  public List getActiveCountriesForAllActivePax()
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.country.AllActiveCountriesForAllActivePax" ).list();
  }

  /**
   * 
   * Overridden from @see com.biperf.core.dao.country.CountryDAO#getActiveCountriesForPaxBasedAudience(java.lang.Long)
   * @param audienceId
   * @return
   */
  public List getActiveCountriesForPaxBasedAudience( Long audienceId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.country.getActiveCountriesForPaxBasedAudience" );
    query.setLong( "audienceId", audienceId.longValue() );

    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.country.CountryDAO#saveCountry(com.biperf.core.domain.country.Country)
   * @param country
   * @return Country
   */
  public Country saveCountry( Country country )
  {
    return (Country)HibernateUtil.saveOrUpdateOrDeepMerge( country );
  }

  @Override
  public List<Country> getActiveCountriesForSmsAvailable()
  {
    Criteria criteria = getSession().createCriteria( Country.class );
    criteria.add( Restrictions.eq( "status", CountryStatusType.lookup( CountryStatusType.ACTIVE ) ) );
    criteria.add( Restrictions.eq( "smsCapable", true ) );
    return criteria.list();
  }

  @Override
  public List getActiveCountriesForSmsChecked()
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.country.getActiveCountriesForAllowSmsChecked" ).list();
  }

  @Override
  public boolean checkUserSupplier( String countryCode, String supplierName )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session
        .createSQLQuery( " select c.* from country_suppliers cs, country c where cs.supplier_id=(select supplier_id from supplier where supplier_name = :supplierName) and cs.country_id=c.country_id and c.country_code=:countryCode and cs.is_primary=1 " )
        .addEntity( Country.class );
    query.setParameter( "countryCode", countryCode );
    query.setParameter( "supplierName", supplierName );
    return (Country)query.uniqueResult() != null;
  }

  public BigDecimal getBudgetMediaValueByUserId( final Long userId )
  {

    SQLQuery sqlQuery = getSession().createSQLQuery( " SELECT country.budget_media_value as mediaValue " + " FROM Country country, User_Address a " + " where country.country_id = a.country_id "
        + " and a.user_id = :userId " + " and a.is_primary = 1" );
    sqlQuery.setLong( "userId", userId );
    Object result = sqlQuery.uniqueResult();
    if ( result != null )
    {
      return (BigDecimal)result;
    }
    else
    {
      sqlQuery = getSession().createSQLQuery( " SELECT country.budget_media_value as mediaValue " + " FROM Country country " + " where cm_asset_code = 'country_data.country.usa' " );
      result = sqlQuery.uniqueResult();
      if ( result != null )
      {
        return (BigDecimal)result;
      }
      return BigDecimal.ONE;
    }
  }
  
  @Override
  public boolean checkBiiExperience( String countryCode, String programId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.createSQLQuery( " select c.* from  country c where c.country_code=:countryCode and c.program_id=:programId and c.display_experiences=1" ).addEntity( Country.class );
    query.setParameter( "countryCode", countryCode );
    query.setParameter( "programId", programId );
    return (Country)query.uniqueResult() != null;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<CountryValueBean> getAllActiveCountriesCodesAbbrevs()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.country.allActiveCountriesCodesAbbrevs" );
    query.setResultTransformer( (ResultTransformer)new ActiveCountriesCodeAbbrevsRowMapper() );

    return query.list();
  }

  @SuppressWarnings( "serial" )
  public class ActiveCountriesCodeAbbrevsRowMapper extends BaseReportsResultTransformer
  {
    public CountryValueBean transformTuple( Object[] tuple, String[] aliases )
    {
      CountryValueBean country = new CountryValueBean();
      country.setCountryCode( extractString( tuple[0] ) );
      country.setCountryName( ContentReaderManager.getText( extractString( tuple[1] ), "COUNTRY_NAME" ) );
      country.setAwardbanqAbbrev( extractString( tuple[2] ) );
      return country;
    }
  }
}
