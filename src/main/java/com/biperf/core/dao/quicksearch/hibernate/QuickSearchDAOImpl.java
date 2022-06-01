/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/quicksearch/hibernate/QuickSearchDAOImpl.java,v $
 */

package com.biperf.core.dao.quicksearch.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.quicksearch.QuickSearchDAO;
import com.biperf.core.domain.country.Country;

/**
 * QuickSearchDAO.
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
 * <td>wadzinsk</td>
 * <td>Sep 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class QuickSearchDAOImpl extends BaseDAO implements QuickSearchDAO
{
  private static final String SEARCH_BY_FIELD_CODE_LASTNAME = "pax_lastname";
  private static final String SEARCH_BY_FIELD_CODE_STATE = "pax_state";
  private static final String SEARCH_BY_FIELD_CODE_NODENAME = "pax_nodename";
  private static final String SEARCH_BY_FIELD_CODE_ABQNBR = "pax_banqnum";
  private static final String SEARCH_BY_FIELD_CODE_SSN = "pax_ssn";
  private static final String SEARCH_BY_FIELD_CODE_USERNAME = "pax_userid";
  private static final String SEARCH_BY_FIELD_CODE_POSTALCODE = "pax_postalcode";
  private static final String SEARCH_BY_FIELD_CODE_EMAIL = "pax_email";
  private static final String SEARCH_BY_FIELD_CODE_USERID = "pax_user_id";
  private static final String SEARCH_BY_FIELD_CODE_COUNTRY = "pax_country";

  private static final String ORDER_BY = " order by ";
  private static final String ASCENDING = " asc ";
  private static final String DESCENDING = " desc ";

  private CountryDAO countryDAO;

  public void setCountryDAO( CountryDAO countryDAO )
  {
    this.countryDAO = countryDAO;
  }

  private static final String SELECT_CLAUSE = "select new com.biperf.core.value.UserValueBean( " + "user.id, " + "INITCAP(user.lastName), " + "INITCAP(user.firstName), " + "user.middleName, "
      + "userEmailAddress.emailAddr, " + "userEmailAddress.emailType," + "lower(user.userName), " + "lower(participant.status), " + "participant.awardBanqNumberDecrypted, " + "userAddress.address.country ) ";

  private static final String LASTNAME_QUERY = SELECT_CLAUSE + "from com.biperf.core.domain.user.User user " + "left join user.userAddresses userAddress " + "join userAddress.address.country country "
      + "left join user.userEmailAddresses userEmailAddress, " + "com.biperf.core.domain.participant.Participant participant " + "where participant.id=user.id "
      + "and (userAddress.isPrimary = 1 or userAddress.isPrimary is null) " + "and (userEmailAddress.isPrimary = 1 or userEmailAddress.isPrimary is null or (select count(*) from user.userEmailAddresses)=1) "
      + "and ( (:value is not null and lower(user.lastName) like :value) " + "or (:value is null and user.lastName is not null) ) ";

  private static final String STATE_QUERY = SELECT_CLAUSE + "from com.biperf.core.domain.user.User user " + "left join user.userEmailAddresses userEmailAddress "
      + "join user.userAddresses userAddress " + "join userAddress.address.country country, " + "com.biperf.core.domain.participant.Participant participant " + "where participant.id=user.id "
      + "and (userEmailAddress.isPrimary = 1 or userEmailAddress.isPrimary is null or (select count(*) from user.userEmailAddresses)=1) " + "and ( (:value is not null and lower(userAddress.address.stateType) like concat('%_',:value)) "
      + "or (:value is null and userAddress.address.stateType is not null) ) ";

  private static final String NODENAME_QUERY = SELECT_CLAUSE + "from com.biperf.core.domain.user.User user " + "left join user.userAddresses userAddress " + "join userAddress.address.country country "
      + "left join user.userEmailAddresses userEmailAddress " + "join user.userNodes userNode, " + "com.biperf.core.domain.participant.Participant participant " + "where participant.id=user.id "
      + "and (userAddress.isPrimary = 1 or userAddress.isPrimary is null) " + "and (userEmailAddress.isPrimary = 1 or userEmailAddress.isPrimary is null or (select count(*) from user.userEmailAddresses)=1) "
      + "and ( (:value is not null and lower(userNode.node.name) like :value) " + "or (:value is null and userNode.node.name is not null) ) ";

  private static final String ABQNBR_QUERY_SELECTIVE = SELECT_CLAUSE + "from com.biperf.core.domain.user.User user " + "left join user.userAddresses userAddress "
      + "join userAddress.address.country country " + "left join user.userEmailAddresses userEmailAddress, " + "com.biperf.core.domain.participant.Participant participant "
      + "where participant.id=user.id " + "and (userAddress.isPrimary = 1 or userAddress.isPrimary is null) " + "and (userEmailAddress.isPrimary = 1 or userEmailAddress.isPrimary is null or (select count(*) from user.userEmailAddresses)=1) "
      + "and participant.awardBanqNumber = FNC_JAVA_ENCRYPT(:value) ";

  private static final String SSN_QUERY_SELECTIVE = SELECT_CLAUSE + "from com.biperf.core.domain.user.User user " + "left join user.userAddresses userAddress "
      + "join userAddress.address.country country " + "left join user.userEmailAddresses userEmailAddress, " + "com.biperf.core.domain.participant.Participant participant "
      + "where participant.id=user.id " + "and (userAddress.isPrimary = 1 or userAddress.isPrimary is null) " + "and (userEmailAddress.isPrimary = 1 or userEmailAddress.isPrimary is null or (select count(*) from user.userEmailAddresses)=1) "
      + "and user.ssn = FNC_JAVA_ENCRYPT(:value) ";

  private static final String PAX_USERID_QUERY_SELECTIVE = SELECT_CLAUSE + "from com.biperf.core.domain.user.User user " + "left join user.userAddresses userAddress "
      + "join userAddress.address.country country " + "left join user.userEmailAddresses userEmailAddress, " + "com.biperf.core.domain.participant.Participant participant "
      + "where participant.id=user.id " + "and (userAddress.isPrimary = 1 or userAddress.isPrimary is null) " + "and (userEmailAddress.isPrimary = 1 or userEmailAddress.isPrimary is null or (select count(*) from user.userEmailAddresses)=1) "
      + "and user.id = :value  ";

  private static final String USERNAME_QUERY = SELECT_CLAUSE + "from com.biperf.core.domain.user.User user " + "left join user.userAddresses userAddress " + "join userAddress.address.country country "
      + "left join user.userEmailAddresses userEmailAddress, " + "com.biperf.core.domain.participant.Participant participant " + "where participant.id=user.id "
      + "and (userAddress.isPrimary = 1 or userAddress.isPrimary is null) " + "and (userEmailAddress.isPrimary = 1 or userEmailAddress.isPrimary is null or (select count(*) from user.userEmailAddresses)=1) "
      + "and ( (:value is not null and lower(user.userName) like :value) " + "or (:value is null and user.userName is not null) ) ";

  private static final String POSTALCODE_QUERY = SELECT_CLAUSE + "from com.biperf.core.domain.user.User user " + "left join user.userAddresses userAddress "
      + "join userAddress.address.country country " + "left join user.userEmailAddresses userEmailAddress " + "join user.userAddresses userAddress, "
      + "com.biperf.core.domain.participant.Participant participant " + "where participant.id=user.id " + "and (userAddress.isPrimary = 1 or userAddress.isPrimary is null) "
      + "and (userEmailAddress.isPrimary = 1 or userEmailAddress.isPrimary is null or (select count(*) from user.userEmailAddresses)=1) " + "and ( (:value is not null and lower(userAddress.address.postalCode) like :value) "
      + "or (:value is null and userAddress.address.postalCode is not null) ) ";

  private static final String EMAIL_QUERY = SELECT_CLAUSE + "from com.biperf.core.domain.user.User user " + "left join user.userAddresses userAddress " + "join userAddress.address.country country "
      + "join user.userEmailAddresses userEmailAddress, " + "com.biperf.core.domain.participant.Participant participant " + "where participant.id=user.id " + "and userEmailAddress.isPrimary = 1 "
      + "and (userAddress.isPrimary = 1 or userAddress.isPrimary is null or (select count(*) from user.userEmailAddresses)=1) " + "and user.id in ( select emailAddress.user.id " + "from com.biperf.core.domain.user.UserEmailAddress emailAddress "
      + "where ( (emailAddress.emailType != 'rec' and (:value is not null and lower(emailAddress.emailAddr) like :value) or (:value is null and emailAddress.emailAddr is not null) ) ) ) ";

  private static final String COUNTRY_QUERY = SELECT_CLAUSE + "from com.biperf.core.domain.user.User user " + "left join user.userEmailAddresses userEmailAddress "
      + "join user.userAddresses userAddress " + "join userAddress.address.country country, " + "com.biperf.core.domain.participant.Participant participant " + "where participant.id=user.id "
      + "and (userEmailAddress.isPrimary = 1 or userEmailAddress.isPrimary is null or (select count(*) from user.userEmailAddresses)=1) " + "and (userAddress.address.country.countryCode in (:value)) ";

  /** @deprecated - Is not being updated w.r.t the current usecase, use searchByPage() instead
   * 
   * Overridden from
   * 
   * @see com.biperf.core.dao.quicksearch.QuickSearchDAO#search(java.lang.String, java.lang.String)
   * @param quickSearchSearchByFieldCode
   * @param quickSearchValue
   * @return List
   */
  @Deprecated
  public List search( String quickSearchSearchByFieldCode, String quickSearchValue )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.quicksearch." + quickSearchSearchByFieldCode );

    String searchValue = quickSearchValue;
    // SSN search is not a like search so "%" should not be appended to the search value
    if ( !quickSearchSearchByFieldCode.equals( "pax_ssn" ) && !quickSearchSearchByFieldCode.equals( "pax_banqnum" ) )
    {
      searchValue = StringUtils.trimToEmpty( quickSearchValue ).toLowerCase() + "%";
    }
    query.setParameter( "value", searchValue );

    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.quicksearch.QuickSearchDAO#searchByPage(java.lang.String,
   *      java.lang.String, int, int)
   * @param quickSearchSearchByFieldCode
   * @param quickSearchValue
   * @param pageNumber
   * @param pageSize
   * @return List
   */

  public List searchByPage( String quickSearchSearchByFieldCode, String quickSearchValue, int sortField, boolean sortAscending, int pageNumber, int pageSize )
  {
    if ( SEARCH_BY_FIELD_CODE_COUNTRY.equalsIgnoreCase( quickSearchSearchByFieldCode ) )
    {
      List<String> countryCodes = getCountryCodes( quickSearchValue );
      String value = toCSL( countryCodes );
      if ( StringUtils.isNotBlank( value ) )
      {
        String queryString = getQueryString( quickSearchSearchByFieldCode, sortField, sortAscending, quickSearchValue );
        if ( StringUtils.isNotBlank( queryString ) )
        {
          queryString = queryString.replace( ":value", value );
          Query query = getSession().createQuery( queryString );
          query.setMaxResults( pageSize );
          if ( pageNumber > 1 )
          {
            query.setFirstResult( pageSize * ( pageNumber - 1 ) );
          }
          return query.list();
        }
      }
      return new ArrayList();
    }

    String searchValue = quickSearchValue;

    // SSN and Awardbanq search is not a like search so "%" should not be appended to the search
    // value
    if ( !SEARCH_BY_FIELD_CODE_ABQNBR.equalsIgnoreCase( quickSearchSearchByFieldCode ) && !SEARCH_BY_FIELD_CODE_SSN.equalsIgnoreCase( quickSearchSearchByFieldCode )
        && !SEARCH_BY_FIELD_CODE_USERID.equalsIgnoreCase( quickSearchSearchByFieldCode ) )
    {
      searchValue = StringUtils.trimToEmpty( quickSearchValue );
      if ( searchValue.length() > 0 )
      {
        searchValue = searchValue.toLowerCase() + "%";
      }
    }

    String queryString = getQueryString( quickSearchSearchByFieldCode, sortField, sortAscending, searchValue );
    Query query;
    if ( queryString != null )
    {
      query = getSession().createQuery( queryString );
    }
    else
    {
      String namedQuery = "com.biperf.core.quicksearch." + quickSearchSearchByFieldCode;
      query = getSession().getNamedQuery( namedQuery );
    }

    query.setParameter( "value", searchValue );
    query.setMaxResults( pageSize );
    if ( pageNumber > 1 )
    {
      query.setFirstResult( pageSize * ( pageNumber - 1 ) );
    }
    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.quicksearch.QuickSearchDAO#sizeOfResult(java.lang.String,
   *      java.lang.String)
   * @param quickSearchSearchByFieldCode
   * @param quickSearchValue
   * @return int
   */

  public int sizeOfResult( String quickSearchSearchByFieldCode, String quickSearchValue )
  {
    if ( SEARCH_BY_FIELD_CODE_COUNTRY.equalsIgnoreCase( quickSearchSearchByFieldCode ) )
    {
      List<String> countryCodes = getCountryCodes( quickSearchValue );
      String value = toCSL( countryCodes );
      if ( StringUtils.isNotBlank( value ) )
      {
        String queryString = getQueryStringForFieldName( quickSearchSearchByFieldCode );
        if ( StringUtils.isNotBlank( value ) )
        {
          queryString = queryString.replace( ":value", value );
          Query query = getSession().createQuery( queryString );
          return query.list().size();
        }
      }
      return 0;
    }

    String searchValue = quickSearchValue;

    if ( !SEARCH_BY_FIELD_CODE_ABQNBR.equalsIgnoreCase( quickSearchSearchByFieldCode ) && !SEARCH_BY_FIELD_CODE_SSN.equalsIgnoreCase( quickSearchSearchByFieldCode )
        && !SEARCH_BY_FIELD_CODE_USERID.equalsIgnoreCase( quickSearchSearchByFieldCode ) )
    {
      searchValue = StringUtils.trimToEmpty( quickSearchValue );
      if ( searchValue.length() > 0 )
      {
        searchValue = searchValue.toLowerCase() + "%";
      }
    }

    String namedQuery = "com.biperf.core.quicksearch.size." + quickSearchSearchByFieldCode;
    Query query = getSession().getNamedQuery( namedQuery );
    query.setParameter( "value", searchValue );

    return ( (Integer)query.uniqueResult() ).intValue();
  }

  @SuppressWarnings( "rawtypes" )
  private List<String> getCountryCodes( String value )
  {
    List<String> list = new ArrayList<String>();

    List countries = countryDAO.getAll();
    for ( Iterator countryListIterator = countries.iterator(); countryListIterator.hasNext(); )
    {
      Country country = (Country)countryListIterator.next();
      if ( StringUtils.isEmpty( value ) || country.getI18nCountryName().toUpperCase().startsWith( value.toUpperCase() ) )
      {
        list.add( country.getCountryCode() );
      }
    }

    return list;
  }

  private String toCSL( List<String> list )
  {
    StringBuilder buffer = new StringBuilder();
    boolean isFirst = true;
    for ( String item : list )
    {
      if ( isFirst )
      {
        isFirst = false;
      }
      else
      {
        buffer.append( "," );
      }
      buffer.append( "'" );
      buffer.append( item );
      buffer.append( "'" );
    }
    return buffer.toString();
  }

  private String getQueryString( String fieldName, int sortField, boolean sortAscending, String searchValue )
  {
    String queryString = getQueryStringForFieldName( fieldName );
    if ( !SEARCH_BY_FIELD_CODE_USERID.equalsIgnoreCase( fieldName ) )
    {
      if ( queryString != null )
      {
        String sortFieldString = getSortFieldString( sortField );
        if ( sortField == 0 || sortField != 0 && sortFieldString != null )
        {
          queryString = sortField == 0
              ? queryString + ORDER_BY + "lower(user.lastName)" + ( sortAscending ? ASCENDING : DESCENDING ) + ",lower(user.firstName)" + ( sortAscending ? ASCENDING : DESCENDING )
              : queryString + ORDER_BY + sortFieldString + ( sortAscending ? ASCENDING : DESCENDING );
        }
      }
    }
    return queryString;
  }

  private String getSortFieldString( int sortField )
  {
    switch ( sortField )
    {
      case SORT_EMAIL: // Email is complicated - not allowing this for now
        return "lower(user.lastName)";
      case SORT_LOGINID:
        return "lower(user.userName)";
      case SORT_BANKACCOUNT:
        return "FNC_JAVA_DECRYPT(participant.awardBanqNumber)";
      case SORT_STATUS:
        return "lower(participant.status)";
      case SORT_COUNTRY:
        return "fnc_cms_asset_code_val_extr(country.cmAssetCode, country.nameCmKey, 'en_US')";
      default:
        return null;
    }
  }

  private String getQueryStringForFieldName( String fieldName )
  {
    if ( SEARCH_BY_FIELD_CODE_LASTNAME.equalsIgnoreCase( fieldName ) )
    {
      return LASTNAME_QUERY;
    }
    if ( SEARCH_BY_FIELD_CODE_EMAIL.equalsIgnoreCase( fieldName ) )
    {
      return EMAIL_QUERY;
    }
    if ( SEARCH_BY_FIELD_CODE_STATE.equalsIgnoreCase( fieldName ) )
    {
      return STATE_QUERY;
    }
    if ( SEARCH_BY_FIELD_CODE_NODENAME.equalsIgnoreCase( fieldName ) )
    {
      return NODENAME_QUERY;
    }
    if ( SEARCH_BY_FIELD_CODE_ABQNBR.equalsIgnoreCase( fieldName ) )
    {
      return ABQNBR_QUERY_SELECTIVE;
    }
    if ( SEARCH_BY_FIELD_CODE_SSN.equalsIgnoreCase( fieldName ) )
    {
      return SSN_QUERY_SELECTIVE;
    }
    if ( SEARCH_BY_FIELD_CODE_USERNAME.equalsIgnoreCase( fieldName ) )
    {
      return USERNAME_QUERY;
    }
    if ( SEARCH_BY_FIELD_CODE_POSTALCODE.equalsIgnoreCase( fieldName ) )
    {
      return POSTALCODE_QUERY;
    }
    if ( SEARCH_BY_FIELD_CODE_USERID.equalsIgnoreCase( fieldName ) )
    {
      return PAX_USERID_QUERY_SELECTIVE;
    }
    if ( SEARCH_BY_FIELD_CODE_COUNTRY.equalsIgnoreCase( fieldName ) )
    {
      return COUNTRY_QUERY;
    }
    return null;
  }
}
