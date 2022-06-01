/*
 * Copyright 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.participant.hibernate;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.inactive.hibernate.CallInActivateBiwUsersProc;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImpl.ActiveDepartmentsForPublicRecogRowMapper;
import com.biperf.core.dao.reports.hibernate.BaseReportsResultTransformer;
import com.biperf.core.dao.reports.hibernate.CallPrcGQPartnerMiniProfile;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.ParticipantPreferenceCommunicationsType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.participant.BadgeView;
import com.biperf.core.domain.participant.NameIdBean;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.participant.ParticipantSearchView;
import com.biperf.core.domain.promotion.ParticipantFollowers;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserTNCHistory;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.ProjectionCollection;
import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.ui.user.PaxContactType;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.CountryValueBean;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.ParticipantRosterSearchValueBean;
import com.biperf.core.value.client.ClientPublicRecognitionDeptBean;
import com.biperf.core.value.participant.PaxAvatarData;
import com.biperf.core.value.participant.PaxIndexData;

/**
 * ParticipantDAO implementation to implement the business logic for the DAO interface.
 * 
 * @author crosenquest Apr 27, 2005
 */
public class ParticipantDAOImpl extends BaseDAO implements ParticipantDAO
{

  private DataSource dataSource;

  private JdbcTemplate jdbcTemplate;

  /* Query to retrieve mini-profile details for multiple participants */
  private static final String MINIPROFILE_QUERY_FOR_ARRAY_OF_PAX = "SELECT DISTINCT au.user_id,\n" + "                au.first_name,\n" + "                au.last_name,\n"
      + "                p.avatar_small,\n" + "                p.allow_public_recognition,\n" + "                p.allow_public_information,\n" + "                n.name primary_node,\n"
      + "                c.country_code,\n" + "                (select fnc_cms_asset_code_val_extr(c.cm_asset_code, 'COUNTRY_NAME', '%1s')  from dual)  AS country_name\n"
      + "FROM application_user au,\n" + "     participant p,\n" + "     user_node un,\n" + "     node n,\n" + "     user_address ua,\n" + "     country c\n" + "WHERE au.user_id = p.user_id\n"
      + "AND   au.user_id = ua.user_id\n" + "AND   ua.is_primary = 1\n" + "AND   c.country_id = ua.country_id\n" + "AND   un.node_id = n.node_id\n" + "AND   UN.IS_PRIMARY = 1\n"
      + "AND   un.user_id = au.user_id\n" + "AND   au.is_active = 1\n" + "AND   au.user_id IN (%2s)\n";

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  private static final Log log = LogFactory.getLog( ParticipantDAOImpl.class );

  /**
   * Saves the participant information to the database. Overridden from
   * 
   * @see com.biperf.core.dao.participant.ParticipantDAO#saveParticipant(com.biperf.core.domain.participant.Participant)
   * @param participant
   * @return Participant
   */
  public Participant saveParticipant( Participant participant )
  {
    return (Participant)HibernateUtil.saveOrUpdateOrShallowMerge( participant );
  }

  /**
   * Get all participants.
   * 
   * @return a list of all participants as a <code>List</code> of {@link Participant} objects.
   */
  public List getAll()
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.user.AllParticipants" );
    return query.list();
  }

  /**
   * Get all active participants.
   * 
   * @return a list of all active participants as a <code>List</code> of {@link Participant}
   *         objects.
   */
  @SuppressWarnings( "unchecked" )
  public List<Participant> getAllActive()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.AllActiveParticipants" );
    return query.list();
  }

  /**
   * Get all active participants and Welcome email not sent .
   * 
   * @return a list of all active participants as a <code>List</code> of {@link Participant}
   *         objects.
   */
  public List getAllActiveAndWelcomeEmailNotSent()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.AllActiveParticipantsWelcomeEmailNotSent" );
    return query.list();
  }

  /**
   * Get the participant information from the database corresponding the the id. Overridden from
   * 
   * @see com.biperf.core.dao.participant.ParticipantDAO#getParticipantById(java.lang.Long)
   * @param id
   * @return Participant
   */
  public Participant getParticipantById( Long id )
  {
    return (Participant)getSession().get( Participant.class, id );
  }

  /**
   * Get the runtime proxy of participant
   * 
   * 
   * @param id
   * @return Participant
   */
  public Participant getParticipantProxyById( Long id )
  {
    return (Participant)getSession().load( Participant.class, id );
  }

  /**
   * Get the Participant by their Username.
   * 
   * @param userName
   * @return Participant
   */
  public Participant getParticipantByUserName( String userName )
  {
    String upperUserName = userName.toUpperCase();
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.user.ParticipantLookupByUserName" );
    query.setString( "userName", upperUserName );
    Participant participant = (Participant)query.uniqueResult();
    return participant;
  }

  /**
   * Get the Participant by their ssn.
   * 
   * @param ssn
   * @return Participant
   */
  public Participant getParticipantBySSN( String ssn )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.user.ParticipantLookupBySSN" );
    query.setString( "ssn", ssn.trim() );
    Participant participant = (Participant)query.uniqueResult();
    return participant;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.participant.ParticipantDAO#getParticipantOverviewById(java.lang.Long)
   * @param id
   * @return Participant -fully hydrated
   * NOTE: Only use this if you need fully hydrated Participant object. 
   * Otherwise use getParticipantByIdWithAssociations
   */
  public Participant getParticipantOverviewById( Long id )
  {
    Participant pax = (Participant)getSession().get( Participant.class, id );

    if ( pax != null )
    {
      Hibernate.initialize( pax.getUserAddresses() );
      Hibernate.initialize( pax.getUserEmailAddresses() );
      Hibernate.initialize( pax.getUserPhones() );
      Hibernate.initialize( pax.getUserNodes() );
      Hibernate.initialize( pax.getUserCharacteristics() );
      Hibernate.initialize( pax.getParticipantEmployers() );
    }

    return pax;
  }

  /**
   * Search the database with the given search criteria params. Overridden from
   * 
   * @see com.biperf.core.dao.participant.ParticipantDAO#searchParticipant(com.biperf.core.service.participant.ParticipantSearchCriteria)
   * @param searchCriteria
   * @return List
   */
  public List searchParticipant( ParticipantSearchCriteria searchCriteria )
  {
    return searchParticipant( searchCriteria, false );
  }

  /**
   * Search the database with the given search criteria params. Overridden from
   * 
   * @see com.biperf.core.dao.participant.ParticipantDAO#searchParticipant(com.biperf.core.service.participant.ParticipantSearchCriteria)
   * @param searchCriteria
   * @return List
   */
  public List searchParticipant( ParticipantSearchCriteria searchCriteria, boolean isCriteriaStartingWith )
  {
    if ( isCriteriaStartingWith )
    {
      return searchParticipant( searchCriteria, MatchMode.START );
    }
    else
    {
      return searchParticipant( searchCriteria, MatchMode.ANYWHERE );
    }
  }

  /**
   * Search the database with the given search criteria params. Overridden from
   * 
   * @see com.biperf.core.dao.participant.ParticipantDAO#searchParticipant(com.biperf.core.service.participant.ParticipantSearchCriteria)
   * @param searchCriteria
   * @return List
   * NOTE: this is not used on G5 ajax search
   */
  private List searchParticipant( ParticipantSearchCriteria searchCriteria, MatchMode matchMode )
  {
    boolean runQuery = false;
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( Participant.class );

    String userName = searchCriteria.getUserName();
    if ( !StringUtils.isEmpty( userName ) )
    {
      criteria.add( Restrictions.like( "userName", userName.toUpperCase(), matchMode ) );
      runQuery = true;
    }

    String lastName = searchCriteria.getLastName();
    if ( !StringUtils.isEmpty( lastName ) )
    {
      criteria.add( Restrictions.ilike( "lastName", lastName, matchMode ) );
      runQuery = true;
    }

    String firstName = searchCriteria.getFirstName();
    if ( !StringUtils.isEmpty( firstName ) )
    {
      criteria.add( Restrictions.ilike( "firstName", firstName, matchMode ) );
      runQuery = true;
    }

    String emailAddr = searchCriteria.getEmailAddr();
    if ( !StringUtils.isEmpty( emailAddr ) )
    {
      criteria.createAlias( "userEmailAddresses", "userEmailAddress" ).add( Restrictions.ilike( "userEmailAddress.emailAddr", emailAddr, matchMode ) );
      runQuery = true;
    }

    criteria.add( Restrictions.eq( "active", new Boolean( true ) ) );

    /*
     * commenting search by ssn as this should not be allowed as per business rule. String ssn =
     * searchCriteria.getSsn(); if ( null != ssn && !"".equals( ssn ) ) { criteria.add(
     * Restrictions.ilike( "ssn", ssn, MatchMode.ANYWHERE ) ); runQuery = true; }
     */

    String postalCode = searchCriteria.getPostalCode();
    String countryCode = searchCriteria.getCountry();

    if ( !StringUtils.isEmpty( postalCode ) || !StringUtils.isEmpty( countryCode ) )
    {
      Criteria addressAlias = criteria.createAlias( "userAddresses", "userAddress" );
      if ( !StringUtils.isEmpty( postalCode ) )
      {
        addressAlias.add( Restrictions.like( "userAddress.address.postalCode", postalCode, matchMode ) );
        runQuery = true;
      }

      if ( !StringUtils.isEmpty( countryCode ) )
      {
        Country country = getCountryDao().getCountryByCode( countryCode );
        if ( country != null )
        {
          addressAlias.add( Restrictions.eq( "userAddress.address.country", country ) );
          runQuery = true;
        }
      }
    }
    String department = searchCriteria.getDepartment();
    if ( !StringUtils.isEmpty( department ) )
    {
      criteria.add( Restrictions.eq( "departmentType", department ) );
      runQuery = true;
    }

    String jobPosition = searchCriteria.getJobPosition();
    if ( StringUtils.isNotBlank( jobPosition ) )
    {
      criteria.add( Restrictions.eq( "positionType", jobPosition ) );
      runQuery = true;
    }

    if ( searchCriteria.getNodeList() != null && !searchCriteria.getNodeList().isEmpty() )
    {
      Criteria userNodeAlias = criteria.createAlias( "userNodes", "userNode" );
      userNodeAlias.add( Restrictions.in( "userNode.node", searchCriteria.getNodeList() ) );
      runQuery = true;
    }

    if ( searchCriteria.getNodeId() != null )
    {
      Criteria userNodeAlias = criteria.createAlias( "userNodes", "userNode" );
      if ( searchCriteria.isNodeIdAndBelow() )
      {
        // only way to do connect by query in criteria?

        userNodeAlias.add( Restrictions.sqlRestriction( "node_id in (select inner.node_id from node inner start with inner.node_id in (" + searchCriteria.getNodeId()
            + ") connect by prior inner.node_id=inner.parent_node_id)" ) );
      }
      else
      {
        criteria.add( Restrictions.eq( "userNode.node.id", searchCriteria.getNodeId() ) );
      }
      runQuery = true;
    }
    if ( searchCriteria.getBudgetSegmentId() != null )
    {
      criteria.add( Restrictions.sqlRestriction( "{alias}.user_id in (select inner.user_id from budget inner where inner.budget_segment_id = ?)",
                                                 searchCriteria.getBudgetSegmentId(),
                                                 StandardBasicTypes.LONG ) );
    }
    if ( searchCriteria.isSortByLastNameFirstName() )
    {
      criteria.addOrder( Order.asc( "lastName" ) ).addOrder( Order.asc( "firstName" ) );

    }
    else
    {
      criteria.addOrder( Order.asc( "userName" ) );
    }
    List result = null;
    if ( runQuery )
    {
      Set resultSet = new LinkedHashSet( criteria.list() );
      result = new ArrayList( resultSet );
    }
    else
    {
      result = new ArrayList(); // empty list
    }

    return result;
  }

  public List<Participant> searchParticipants( ParticipantSearchCriteria searchCriteria )
  {
    boolean runQuery = false;
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( Participant.class );

    if ( !StringUtils.isEmpty( searchCriteria.getLastName() ) )
    {
      criteria.add( Restrictions.eq( "lastName", searchCriteria.getLastName() ).ignoreCase() );
      runQuery = true;
    }

    if ( !StringUtils.isEmpty( searchCriteria.getFirstName() ) )
    {
      criteria.add( Restrictions.eq( "firstName", searchCriteria.getFirstName() ).ignoreCase() );
      runQuery = true;
    }

    criteria.add( Restrictions.eq( "active", new Boolean( true ) ) );

    String countryCode = searchCriteria.getCountry();

    if ( countryCode != null && !countryCode.equals( "" ) )
    {
      Criteria addressAlias = criteria.createAlias( "userAddresses", "userAddress" );

      if ( !StringUtils.isEmpty( countryCode ) )
      {
        Long id = Long.parseLong( countryCode );
        Country country = getCountryDao().getCountryById( id );
        if ( country != null )
        {
          addressAlias.add( Restrictions.eq( "userAddress.address.country", country ) );
          runQuery = true;
        }
      }
    }

    if ( !StringUtils.isEmpty( searchCriteria.getDepartment() ) )
    {
      criteria.add( Restrictions.eq( "departmentType", searchCriteria.getDepartment() ).ignoreCase() );
      runQuery = true;
    }

    if ( StringUtils.isNotBlank( searchCriteria.getJobPosition() ) )
    {
      criteria.add( Restrictions.eq( "positionType", searchCriteria.getJobPosition() ).ignoreCase() );
      runQuery = true;
    }

    if ( searchCriteria.getNodeId() != null )
    {
      Criteria userNodeAlias = criteria.createAlias( "userNodes", "userNode" );
      criteria.add( Restrictions.eq( "userNode.node.id", searchCriteria.getNodeId() ) );
      runQuery = true;
    }

    if ( searchCriteria.getNodeList() != null && !searchCriteria.getNodeList().isEmpty() )
    {
      Criteria userNodeAlias = criteria.createAlias( "userNodes", "userNode" );
      userNodeAlias.add( Restrictions.in( "userNode.node", searchCriteria.getNodeList() ) );
      runQuery = true;
    }

    criteria.addOrder( Order.asc( "lastName" ) ).addOrder( Order.asc( "firstName" ) );

    List result = null;
    if ( runQuery )
    {
      Set resultSet = new LinkedHashSet( criteria.list() );
      result = new ArrayList( resultSet );
    }
    else
    {
      result = new ArrayList(); // empty list
    }

    for ( Object o : result )
    {
      if ( o instanceof Participant )
      {
        // Hibernate.initialize( ( (Participant)o ).getPrimaryAddress() );
        Hibernate.initialize( ( (Participant)o ).getUserNodes() );
        // Bugfix#55632
        // Hibernate.initialize( ( (Participant)o ).getParticipantEmployers() );
      }
    }

    return result;
  }

  public List populateCountriesForUsers( Long[] ids )
  {

    Query query = HibernateSessionManager.getSession().getNamedQuery( "com.biperf.core.domain.participant.getCountryBasedOnParticipantIDs" );
    query.setResultTransformer( (ResultTransformer)new ParticipantSearchAddressRowMapper() );
    query.setParameterList( "ids", ids );

    return query.list();
  }

  private class ParticipantSearchAddressRowMapper extends BaseReportsResultTransformer
  {
    public Map<Long, CountryValueBean> transformTuple( Object[] tuple, String[] aliases )
    {
      CountryValueBean country = new CountryValueBean();
      Map<Long, CountryValueBean> paxCountry = new HashMap<Long, CountryValueBean>();

      Long paxId = extractLong( tuple[0] );
      country.setCountryCode( extractString( tuple[1] ) );
      country.setCmAssetCode( extractString( tuple[2] ) );
      country.setBudgetMediaValue( extractBigDecimal( tuple[3] ) );
      paxCountry.put( paxId, country );

      return paxCountry;
    }

  }

  public List<String> searchCriteriaAutoComplete( ParticipantSearchCriteria searchCriteria )
  {

    RowMapper rowMapper = (RowMapper)new NameIdBeanRowMapper();

    Session session = HibernateSessionManager.getSession();
    List<String> results = null;
    Query query = null;
    String queryParam = null;

    String lastName = searchCriteria.getLastName();
    if ( !StringUtils.isEmpty( lastName ) )
    {
      queryParam = lastName.toUpperCase() + "%";
      queryParam = queryParam.replaceAll( "'", "''" ); // escape single quotes
      query = session.createSQLQuery( "select distinct initcap( last_name ) as lastName from application_user where upper(last_name) like '" + queryParam + "' ORDER BY lastName" );
      results = query.list();
    }

    String firstName = searchCriteria.getFirstName();
    if ( !StringUtils.isEmpty( firstName ) )
    {
      queryParam = firstName.toUpperCase() + "%";
      queryParam = queryParam.replaceAll( "'", "''" ); // escape single quotes
      query = session.createSQLQuery( "select distinct initcap( first_name ) from application_user where upper(first_name) like '" + queryParam + "'" );
      results = query.list();
    }
    String location = searchCriteria.getCountry();
    if ( !StringUtils.isEmpty( location ) )
    {
      queryParam = location.toUpperCase() + "%";
      queryParam = queryParam.replaceAll( "'", "''" ); // escape single quotes
      String sqlQuery = "select distinct node_id as id, name from node where is_deleted = 0 and upper(name) like '" + queryParam + "'";
      results = jdbcTemplate.query( sqlQuery, rowMapper );
    }
    String country = searchCriteria.getPostalCode();
    if ( !StringUtils.isEmpty( country ) )
    {
      queryParam = country.toUpperCase() + "%";
      queryParam = queryParam.replaceAll( "'", "''" ); // escape single quotes
      String sqlQuery = "select distinct  country_id as id, cms_value as name from VW_CMS_ASSET_VALUE v, country c where v.asset_code = c.cm_asset_code and upper(cms_value) like '" + queryParam + "'";
      results = jdbcTemplate.query( sqlQuery, rowMapper );
    }
    String groupName = searchCriteria.getGroupName();
    if ( !StringUtils.isEmpty( groupName ) )
    {
      queryParam = groupName.toUpperCase() + "%";
      queryParam = queryParam.replaceAll( "'", "''" ); // escape single quotes
      String sqlQuery = "select group_id as id, initcap( group_name ) as name from participant_group where group_creator_id = " + UserManager.getUserId() + " and   upper(group_name) like '"
          + queryParam + "' ORDER BY name";
      results = jdbcTemplate.query( sqlQuery, rowMapper );
    }
    return results;
  }

  private class NameIdBeanRowMapper implements RowMapper<NameIdBean>
  {

    @Override
    public NameIdBean mapRow( ResultSet rs, int rowNum ) throws SQLException
    {
      NameIdBean bean = new NameIdBean();
      bean.setId( rs.getLong( "ID" ) );
      bean.setName( rs.getString( "NAME" ) );
      return bean;
    }
  }

  /**
   * @param nodeId
   * @return list of Pax objects for node
   */
  public List getActiveParticipantsForNode( Long nodeId )
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.user.activeParticipantsOfNode" ).setLong( "nodeId", nodeId.longValue() ).list();

  }

  /**
   * Returns a reference to the country DAO.
   * 
   * @return a reference to the country DAO.
   */
  protected CountryDAO getCountryDao()
  {
    return (CountryDAO)ApplicationContextFactory.getApplicationContext().getBean( CountryDAO.BEAN_NAME );
  }

  /**
   * Get userID of all active participants NOT enrolled in the awardBanq system.
   * 
   * @return a list of userIDs belonging to all active participants not enrolled in awardBanq system
   */
  public List getAllActivePaxNotEnrolledInBanqSystem()
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.user.activeParticipantsNotInBanq" ).list();
  }

  public List<Long> getAllActivePaxIds()
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.user.getAllActivePaxIds" ).list();
  }

  public List<Long> getAllPaxIds()
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.user.getAllPaxIds" ).list();
  }

  /**
   * Overridden from
   * 
   * @param participantPreferenceCommunicationsType
   * @param associationRequestCollection
   * 
   * @return List
   */
  public List getAllActivePaxWithCommunicationPreferenceInCampaign( String campaignNumber,
                                                                    ParticipantPreferenceCommunicationsType participantPreferenceCommunicationsType,
                                                                    AssociationRequestCollection associationRequestCollection,
                                                                    int pageNumber,
                                                                    int pageSize )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.activeParticipantsWithCommunicationPreferenceInCampaign" );
    query.setParameter( "participantPreferenceCommunicationsType", participantPreferenceCommunicationsType.getCode() );
    query.setParameter( "campaignNumber", campaignNumber.trim() );

    query.setMaxResults( pageSize );
    if ( pageNumber > 1 )
    {
      query.setFirstResult( pageSize * ( pageNumber - 1 ) );
    }

    List participantList = query.list();
    Iterator iter = participantList.iterator();
    while ( iter.hasNext() )
    {
      if ( associationRequestCollection != null )
      {
        associationRequestCollection.process( (Participant)iter.next() );
      }

    }
    return participantList;
  }

  /**
   * Calls the stored procedure to verify participant file
   * 
   * @param importFileId
   * @param loadType
   * @param userId
   * @return Map result set
   */
  public Map verifyImportFile( Long importFileId, String loadType, Long userId, Long hierarchyId )
  {
    CallPrcParticipantVerifyImport participantVerifyProc = new CallPrcParticipantVerifyImport( dataSource );
    return participantVerifyProc.executeProcedure( importFileId, loadType, userId, hierarchyId );
  }

  /**
   * @param lastId
   * @param batchSize
   * @return List
   */
  public List getAllParticipantsInAwardbanqSystem( Long lastId, int batchSize )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = null;
    if ( lastId == null || lastId.longValue() <= 0 )
    {
      query = session.getNamedQuery( "com.biperf.core.domain.user.AllParticipantsInBanqSystemByBatch" );
      query.setMaxResults( batchSize );
    }
    else
    {
      log.error( "****lastUserId****=" + lastId.toString() );

      query = session.getNamedQuery( "com.biperf.core.domain.user.AllParticipantsInBanqSystemByBatchFromId" );
      query.setLong( "lastId", lastId.longValue() );
      query.setMaxResults( batchSize );
    }
    return query.list();
  }

  /**
  * get most recent participant_employer record and return the termination_date
  * @param userId
  * @return Date
  */
  public Date getCurrentParticipantEmployerTermDate( Long userId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.user.CurrentParticipantEmployerTermDate" );
    query.setParameter( "userId", userId );
    Date termDate = (Date)query.uniqueResult();
    return termDate;
  }

  /**
   * Get Node owner for a given node.
   * 
   * @return {@link User} object.
   */
  public User getNodeOwner( Long nodeId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.user.getOwnerForNode" );
    query.setLong( "nodeId", nodeId.longValue() );

    return (User)query.uniqueResult();
  }

  /**
   * Get Node owner for a given node.
   * 
   * @return {@link User} object.
   */
  public Set getNodeManager( Long nodeId, String roleType )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.user.getManagerForNode" );
    query.setLong( "nodeId", nodeId.longValue() );
    query.setParameter( "roleType", roleType );
    return new LinkedHashSet( query.list() );
  }

  public List rosterSearch( Long nodeId, String participantStatus, Long excludeUserId, int sortField, boolean sortAscending, int pageNumber, int pageSize )
  {
    String sql = buildRosterListQuery();
    Query query = getSession().createSQLQuery( sql );
    query.setResultTransformer( (ResultTransformer)new ParticipantRosterSearchRowMapper() );
    query.setLong( "nodeId", nodeId );
    query.setLong( "excludeUserId", excludeUserId );
    query.setString( "participantStatus", participantStatus );
    query.setMaxResults( pageSize );
    if ( pageNumber > 1 )
    {
      query.setFirstResult( pageSize * ( pageNumber - 1 ) );
    }
    return query.list();
  }

  public Long rosterSearchParticipantCount( Long nodeId, String participantStatus, Long excludeUserId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.participant.rosterSearchByNodeAndStatusCount" );
    query.setLong( "nodeId", nodeId );
    query.setLong( "excludeUserId", excludeUserId );
    query.setString( "participantStatus", participantStatus );
    if ( query.uniqueResult() != null )
    {
      return ( (Long)query.uniqueResult() ).longValue();
    }
    else
    {
      return 0L;
    }
  }

  public Map markAsPlateauAwardsOnly()
  {
    CallPrcMarkAsPlateauAwardsOnly callPrcMarkAsPlateauAwardsOnly = new CallPrcMarkAsPlateauAwardsOnly( dataSource );
    return callPrcMarkAsPlateauAwardsOnly.executeProcedure();
  }

  public List<ParticipantSearchView> getParticipatForMiniProfile( Long participantId )
  {
    String sql = buildPaxMiniProfileQuery( UserManager.getLocale() );
    log.debug( " Participant Mini Profile sql: " + sql );
    Query query = getSession().createSQLQuery( sql );
    query.setResultTransformer( (ResultTransformer)new ParticipantMiniProfileRowMapper() );
    query.setParameter( "userId", participantId );
    query.setParameter( "userInSession", UserManager.getUserId() );
    // query.setParameter( "sessionLocale", UserManager.getLocale().toString() );
    List<ParticipantSearchView> participantsView = query.list();

    // Bug # 4699 - When translation content is not Loaded, display English.
    if ( participantsView != null && participantsView.isEmpty() )
    {
      sql = buildPaxMiniProfileQuery( UserManager.getDefaultLocale() );
      log.debug( " Participant Mini Profile sql: " + sql );
      query = getSession().createSQLQuery( sql );
      query.setResultTransformer( (ResultTransformer)new ParticipantMiniProfileRowMapper() );
      query.setParameter( "userId", participantId );
      query.setParameter( "userInSession", UserManager.getUserId() );
      participantsView = query.list();
    }

    return participantsView;
  }

  public List<ParticipantSearchView> getParticipatForMiniProfile( String participantIds )
  {
    Query query = getSession().createSQLQuery( String.format( MINIPROFILE_QUERY_FOR_ARRAY_OF_PAX, UserManager.getLocale().toString(), participantIds ) );
    query.setResultTransformer( (ResultTransformer)new ParticipantMorePaxMiniProfileRowMapper() );
    return query.list();
  }

  private class ParticipantRosterSearchRowMapper extends BaseReportsResultTransformer
  {
    public ParticipantRosterSearchValueBean transformTuple( Object[] tuple, String[] aliases )
    {
      ParticipantRosterSearchValueBean valueBean = new ParticipantRosterSearchValueBean();
      valueBean.setId( extractLong( tuple[0] ) );
      valueBean.setFirstName( extractString( tuple[1] ) );
      valueBean.setLastName( extractString( tuple[2] ) );
      valueBean.setMiddleName( extractString( tuple[3] ) );
      valueBean.setEmailAddress( extractString( tuple[4] ) );
      valueBean.setUserName( extractString( tuple[5] ) );
      valueBean.setUserLoggedInTime( extractDate( tuple[6] ) );
      valueBean.setRole( extractString( tuple[7] ) );
      valueBean.setJobPosition( extractString( tuple[8] ) );
      return valueBean;
    }
  }

  private class ParticipantMiniProfileRowMapper extends BaseReportsResultTransformer
  {
    public ParticipantSearchView transformTuple( Object[] tuple, String[] aliases )
    {
      ParticipantSearchView valueBean = new ParticipantSearchView();
      valueBean.setId( extractLong( tuple[0] ) );
      valueBean.setFirstName( extractString( tuple[1] ) );
      valueBean.setLastName( extractString( tuple[2] ) );
      valueBean.setAvatarUrl( extractString( tuple[3] ) );
      valueBean.setLargeAvatarUrl( extractString( tuple[4] ) );
      valueBean.setAllowPublicRecognition( extractBoolean( tuple[5] ) );
      valueBean.setAllowPublicInformation( extractBoolean( tuple[6] ) );
      valueBean.setFollowed( extractBoolean( tuple[14] ) );

      NameableBean node = new NameableBean( extractLong( tuple[7] ), extractString( tuple[8] ) );
      List<NameableBean> nodes = new ArrayList<NameableBean>();
      nodes.add( node );
      valueBean.setNodes( nodes );

      Participant pax = getParticipantById( new Long( extractLong( tuple[0] ) ) );
      valueBean.setJobName( getJobName( pax.getParticipantEmployers() ) );
      valueBean.setDepartmentName( getDepartmentName( pax.getParticipantEmployers() ) );
      valueBean.setCountryCode( extractString( tuple[9] ) );
      valueBean.setCountryName( extractString( tuple[10] ) );

      // The query for multiple participants will not return badge information. Only map badges for
      // the single-pax mini profile
      if ( tuple.length > 11 )
      {
        Long badgeId = extractLong( tuple[11] );
        if ( null != badgeId && badgeId.longValue() != 0 )
        {
          String badgeName = extractString( tuple[12] );
          String badgeLibCmKey = extractString( tuple[13] );

          valueBean.addBadge( new BadgeView( badgeId, badgeName, badgeLibCmKey ) );
        }
      }
      return valueBean;
    }
  }

  private class ParticipantMorePaxMiniProfileRowMapper extends BaseReportsResultTransformer
  {
    public ParticipantSearchView transformTuple( Object[] tuple, String[] aliases )
    {
      ParticipantSearchView valueBean = new ParticipantSearchView();
      valueBean.setId( extractLong( tuple[0] ) );
      valueBean.setFirstName( extractString( tuple[1] ) );
      valueBean.setLastName( extractString( tuple[2] ) );
      valueBean.setAvatarUrl( extractString( tuple[3] ) );
      valueBean.setAllowPublicRecognition( extractBoolean( tuple[4] ) );
      valueBean.setAllowPublicInformation( extractBoolean( tuple[5] ) );
      valueBean.setFollowed( extractBoolean( tuple[5] ) );

      String nodeName = extractString( tuple[6] );
      NameableBean node = new NameableBean( 1L, nodeName );
      List<NameableBean> nodes = new ArrayList<NameableBean>();
      nodes.add( node );
      valueBean.setNodes( nodes );
      valueBean.setOrgName( nodeName );

      Participant pax = getParticipantById( new Long( extractLong( tuple[0] ) ) );
      valueBean.setJobName( getJobName( pax.getParticipantEmployers() ) );
      valueBean.setDepartmentName( getDepartmentName( pax.getParticipantEmployers() ) );
      valueBean.setCountryCode( extractString( tuple[7] ) );
      valueBean.setCountryName( extractString( tuple[8] ) );

      // The query for multiple participants will not return badge information. Only map badges for
      // the single-pax mini profile
      if ( tuple.length > 9 )
      {
        Long badgeId = extractLong( tuple[9] );
        if ( null != badgeId && badgeId.longValue() != 0 )
        {
          String badgeName = extractString( tuple[10] );
          String badgeLibCmKey = extractString( tuple[11] );

          valueBean.addBadge( new BadgeView( badgeId, badgeName, badgeLibCmKey ) );
        }
      }
      return valueBean;
    }
  }

  private String buildPaxMiniProfileQuery( Locale displayLocale )
  {
    StringBuilder sql = new StringBuilder();
    sql.append( " select distinct SS.*,decode(pf.participant_id,null, 0, 1) flag FROM " );
    sql.append( " (SELECT au.user_id, " );
    sql.append( " au.first_name, " );
    sql.append( " au.last_name, " );
    sql.append( " p.avatar_small, " );
    sql.append( " p.avatar_original, " );
    sql.append( " p.allow_public_recognition, " );
    sql.append( " p.allow_public_information, " );
    sql.append( " n.node_id, " );
    sql.append( " n.name primary_node, " );
    sql.append( " c.country_code, " );
    sql.append( " vc.cms_value as country_name, " );
    sql.append( " br.promotion_id, " );
    sql.append( " br.badge_name, " );
    sql.append( " br.cm_asset_key" ); // TODO proper image value
    sql.append( " FROM application_user au, " );
    sql.append( " participant p, " );
    sql.append( " user_node un, node n, " );
    sql.append( " user_address ua, " );
    sql.append( " participant_badge pbb, " );
    sql.append( " badge_rule br, " );
    sql.append( " country c, " );
    sql.append( " vw_cms_asset_value vc " );
    sql.append( " WHERE     au.user_id = p.user_id " );
    sql.append( " AND au.user_id = ua.user_id " );
    sql.append( " AND ua.is_primary = 1 " );
    sql.append( " AND au.user_id = pbb.participant_id(+) " );
    sql.append( " AND pbb.badge_rule_id = br.badge_rule_id(+) " );
    sql.append( " AND pbb.is_earned(+)=1 " );
    sql.append( " AND c.country_id = ua.country_id " );
    sql.append( " AND vc.asset_code=c.cm_asset_code " );
    sql.append( " AND lower(vc.locale)=lower('" ).append( displayLocale.toString() ).append( "') " );
    sql.append( " AND un.node_id = n.node_id AND UN.IS_PRIMARY = 1 AND un.user_id = au.user_id " );
    sql.append( " AND au.user_id = :userId order by earned_date desc) ss " );
    sql.append( " LEFT OUTER JOIN participant_followers pf ON " );
    sql.append( " ( SS.USER_ID = pf.follower_id AND pf.participant_id=:userInSession) where rownum<4 " );
    return sql.toString();
  }

  private String buildRosterListQuery()
  {
    StringBuilder sql = new StringBuilder();
    sql.append( " SELECT distinct p.user_id as id, au.first_name as firstName, au.last_name as lastName, au.middle_name as middleName, " );
    sql.append( " ea.email_addr as email, au.user_name as userName, la.login_date_time as lastLogin, " );
    sql.append( " un.role as role, vpe.position_type as jobTitle " );
    sql.append( " FROM participant p, application_user au, user_node un, user_email_Address ea, " );
    sql.append( " vw_curr_pax_employer vpe , (select user_id, max(login_date_time) " );
    sql.append( " login_date_time from login_activity group by user_id ) la " );
    sql.append( " WHERE    p.status = :participantStatus " );
    sql.append( " AND p.user_id != :excludeUserId " );
    sql.append( " AND p.user_id = ea.user_id(+) " );
    sql.append( " AND (ea.is_primary = 1 OR ea.is_primary IS NULL) " );
    sql.append( " AND un.node_id = :nodeId " );
    sql.append( " AND p.user_id = un.user_id " );
    sql.append( " AND au.user_id = p.user_id " );
    sql.append( " AND au.user_id = la.user_id(+) " );
    sql.append( " AND au.user_id = vpe.user_id(+) " );
    return sql.toString();
  }

  public static String getDepartmentName( List<ParticipantEmployer> paxEmployers )
  {
    String departmentName = "";
    for ( Iterator<ParticipantEmployer> it = paxEmployers.iterator(); it.hasNext(); )
    {
      ParticipantEmployer paxEmployer = it.next();
      if ( paxEmployer.getTerminationDate() == null )
      {
        departmentName = ( paxEmployer.getDepartmentType() != null ) ? paxEmployer.getDepartmentType() : ""; // customization
      }
    }
    return departmentName;
  }

  public static String getJobName( List<ParticipantEmployer> paxEmployers )
  {
    String jobName = "";
    for ( Iterator<ParticipantEmployer> it = paxEmployers.iterator(); it.hasNext(); )
    {
      ParticipantEmployer paxEmployer = it.next();
      if ( paxEmployer.getTerminationDate() == null )
      {
        jobName = ( paxEmployer.getPositionType() != null ) ? paxEmployer.getPositionType() : ""; //customization
      }
    }
    return jobName;
  }

  /**
   * 
   * {@inheritDoc}
   */
  public List getNodePaxCount( Long userId )
  {
    Query query = getSession()
        .createSQLQuery( "SELECT COUNT(DISTINCT au.user_id) nodepaxcount FROM   user_node un1, (SELECT node_id FROM   user_node WHERE  user_id = :userId) un,application_user au WHERE  un1.node_id = un.node_id AND un1.user_id = au.user_id AND au.is_active = 1 UNION ALL SELECT COUNT(DISTINCT au.user_id) nodeAndBelowPaxCount FROM application_user au, user_node un, (SELECT node_id FROM   node CONNECT BY PRIOR node_id = parent_node_id START WITH node_id IN (SELECT node_id FROM user_node WHERE user_id = :userId)) un2 WHERE au.user_id = un.user_id AND un.node_id = un2.node_id AND au.is_active = 1" );
    query.setParameter( "userId", userId );
    return query.list();
  }

  public Long getPaxCountBasedOnCriteria( ParticipantSearchCriteria criteria )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.participant.getPaxCountBasedOnCriteria" );
    if ( StringUtils.isNotBlank( criteria.getFirstName() ) )
    {
      query.setParameter( "firstName", criteria.getFirstName().toUpperCase() );
    }
    else
    {
      query.setParameter( "firstName", "" );
    }
    if ( StringUtils.isNotBlank( criteria.getLastName() ) )
    {
      query.setParameter( "lastName", criteria.getLastName().toUpperCase() );
    }
    else
    {
      query.setParameter( "lastName", "" );
    }
    if ( StringUtils.isNotBlank( criteria.getCountry() ) )
    {
      query.setParameter( "countryCode", criteria.getCountry().toUpperCase() );
    }
    else
    {
      query.setParameter( "countryCode", "" );
    }
    if ( StringUtils.isNotBlank( criteria.getDepartment() ) )
    {
      query.setParameter( "department", criteria.getDepartment() );
    }
    else
    {
      query.setParameter( "department", "" );
    }
    if ( StringUtils.isNotBlank( criteria.getJobPosition() ) )
    {
      query.setParameter( "position", criteria.getJobPosition() );
    }
    else
    {
      query.setParameter( "position", "" );
    }
    if ( criteria.getNodeId() != null && criteria.getNodeId() > 0 )
    {
      query.setParameter( "nodeId", criteria.getNodeId() );
    }
    else
    {
      query.setParameter( "nodeId", "" );
    }
    return ( (Long)query.uniqueResult() ).longValue();
  }

  public Long getPaxCountBasedOnEmailCriteria( ParticipantSearchCriteria paxCriteria )
  {
    Criteria criteria = getSession().createCriteria( UserEmailAddress.class );
    criteria.add( Restrictions.eq( "emailAddr", paxCriteria.getEmailAddr() ).ignoreCase() );
    criteria.createAlias( "user", "user" );
    criteria.add( Restrictions.eq( "user.active", paxCriteria.isActive() ) );
    criteria.setProjection( Projections.rowCount() );
    return (Long)criteria.uniqueResult();
  }

  /**
   * Get Participant by SSO Id.
   * 
   * @param ssoId
   * @return Participant
   */
  public List<Participant> getUserBySSOId( String ssoId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.user.UserLookupBySSOId" );
    query.setString( "ssoId", ssoId );
    return (List<Participant>)query.list();
  }

  public List getAllForCharIDAndValue( Long id, String charName )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.characteristic.getAllForCharIDAndValue" );
    query.setString( "charID", id.toString() );
    query.setString( "charValue", charName.toLowerCase() );
    return query.list();
  }

  @Override
  public Participant getParticipantByIdWithProjections( Long participantId, ProjectionCollection collection )
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( Participant.class );
    criteria.add( Restrictions.eq( "id", participantId ) );
    if ( null != collection )
    {
      collection.processProjections( criteria );
    }
    return (Participant)criteria.uniqueResult();
  }

  @Override
  public Participant getParticipantByUserNameWithProjections( String userName, ProjectionCollection collection )
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( Participant.class );
    criteria.add( Restrictions.eq( "userName", userName.toUpperCase() ) );
    if ( null != collection )
    {
      collection.processProjections( criteria );
    }
    return (Participant)criteria.uniqueResult();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Participant> getParticipantsByIdWithProjections( List<Long> participantIds, ProjectionCollection collection )
  {
    if ( participantIds == null || participantIds.isEmpty() )
    {
      return new ArrayList<Participant>();
    }
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( Participant.class );

    int limit = 999;
    Criterion criterion = null;

    int listSize = participantIds.size();
    if ( listSize > limit )
    {
      for ( int i = 0; i < listSize; i += limit )
      {
        List<Long> subList;
        if ( listSize > i + limit )
        {
          subList = participantIds.subList( i, i + limit );
        }
        else
        {
          subList = participantIds.subList( i, listSize );
        }
        if ( criterion != null )
        {
          criterion = Restrictions.or( criterion, Restrictions.in( "id", subList ) );
        }
        else
        {
          criterion = Restrictions.in( "id", subList );
        }
      }
      criteria.add( criterion );
    }
    else
    {
      criteria.add( Restrictions.in( "id", participantIds ) );
    }

    if ( null != collection )
    {
      collection.processProjections( criteria );
    }
    return (List<Participant>)criteria.list();
  }

  public boolean isParticipantFollowed( Long userId, Long loggedinUserId )
  {
    boolean isFollowed = false;
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.ParticipantFollowerByUserId" );
    query.setParameter( "userId", userId );
    query.setParameter( "loggedinUserId", loggedinUserId );
    if ( query.uniqueResult() != null )
    {
      isFollowed = true;
    }
    return isFollowed;
  }

  public String getLNameFNameByPaxId( Long participantId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.user.getLNameFNameByPaxId" );
    query.setParameter( "participantId", participantId );
    return (String)query.uniqueResult();
  }

  public Date getActiveHireDate( Long userId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.user.getActiveHireDate" );
    query.setParameter( "userId", userId );
    return (Date)query.uniqueResult();
  }

  @Override
  public List<Participant> getAllParticipantsByAudienceIds( List<Long> audienceIdList )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.participant.getParticipantsbyAudienceIds" );
    query.setParameterList( "audienceIds", audienceIdList );
    query.setResultTransformer( new ParticipantAudienceRowMapper() );
    return query.list();
  }

  private class ParticipantAudienceRowMapper extends BaseReportsResultTransformer
  {
    public Participant transformTuple( Object[] tuple, String[] aliases )
    {
      Participant valueBean = new Participant();
      valueBean.setId( extractLong( tuple[0] ) );
      valueBean.setFirstName( extractString( tuple[1] ) );
      valueBean.setLastName( extractString( tuple[2] ) );
      valueBean.setUserName( extractString( tuple[3] ) );
      return valueBean;
    }
  }

  public Map getParticipatForMiniProfile( String promotionId, String userId, String userLocale )
  {
    CallPrcGQPartnerMiniProfile gqPartnerMiniProfile = new CallPrcGQPartnerMiniProfile( dataSource );
    return gqPartnerMiniProfile.executeProcedure( promotionId, userId, userLocale );
  }

  @SuppressWarnings( "unchecked" )
  public List<Long> getAllActivePaxIdsInCampaignForEstatements( String campaignNumber, String sendOnlyPaxWithPoints, Long startingUserId )
  {
    Query query = null;
    if ( sendOnlyPaxWithPoints.equals( "no" ) )
    {
      query = getSession().getNamedQuery( "com.biperf.core.domain.user.getAllActivePaxIdsInCampaignForEstatements" );
    }
    else
    {
      query = getSession().getNamedQuery( "com.biperf.core.domain.user.getAllActivePaxIdsEnrolledInCampaignForEstatements" );
    }
    query.setParameter( "campaignNumber", campaignNumber.trim().toUpperCase() );
    query.setParameter( "startingUserId", startingUserId );

    return query.list();
  }


  /* coke customization start */
  /**
   * is pax opted out.
   * 
   * @param userId
   * @return boolean
   */
  public boolean isOptedOut( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.IsOptedOut" );
    query.setParameter( "userId", userId );

    String results = (String)query.uniqueResult();
    boolean isOptedOut = false;
    if ( StringUtils.isNotEmpty( results ) && results.equals( "true" ) )
    {
      isOptedOut = true;
    }
    return isOptedOut;
  }
  /* coke customization end */

  // Client customization for WIP #26597 starts
  public String getCharacteristicValueByUserIdAndCharacteristicDescription( Long userId, String characteristicDescription )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getCharacteristicValueByUserIdAndCharacteristicDescription" );
    query.setParameter( "userId", userId );
    query.setParameter( "characteristicDescription", characteristicDescription );
    return (String)query.uniqueResult();
  }
  // Client customization for WIP #26597 ends

  // Client customization for wip #26532 starts
  public boolean isAllowePurlOutsideInvites( Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.participant.IsAllowedOutsideInvites" );
    query.setParameter( "participantId", participantId );
    Integer result = (Integer)query.uniqueResult();
    if ( result.intValue() == 1 )
      return true;
    else
      return false;
  }
  // Client customization for wip #26532 ends
  @Override
  public Long getPendingNominationCountForApprover( Long approverId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.approver.getPendingNominationCountForApprover" );
    query.setParameter( "p_in_user_id", approverId );
    // query.setResultTransformer( new ParticipantAudienceRowMapper() );
    return (Long)query.uniqueResult();
  }

  public List<Long> getAllEligibleApproversForCustomApproval( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.approver.getAllEligibleApproversForCustomApproval" );
    query.setParameter( "promotionId", promotionId );
    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<PaxIndexData> getParticipantIndexData( List<Long> userIds )
  {
    if ( CollectionUtils.isEmpty( userIds ) )
    {
      return new ArrayList<PaxIndexData>();
    }

    CallPrcParticipantIndexingData prc = new CallPrcParticipantIndexingData( dataSource );
    Map<String, Object> prcResultMap = prc.executeProcedure( userIds );

    BigDecimal returnCode = (BigDecimal)prcResultMap.get( CallPrcParticipantIndexingData.P_OUT_RETURN_CODE );

    if ( 99 == returnCode.intValue() )
    {
      log.error( "Error occured in Procedure : prc_get_user_info  for userIds : " + userIds.toString() );
    }
    Map<Long, List<String>> userPathMapping = (Map<Long, List<String>>)prcResultMap.get( CallPrcParticipantIndexingData.P_OUT_PATH );
    List<PaxIndexData> paxDataList = (List<PaxIndexData>)prcResultMap.get( CallPrcParticipantIndexingData.P_OUT_DATA );
    paxDataList.parallelStream().forEach( p -> p.setPaths( userPathMapping.get( p.getUserId() ) ) );
    return paxDataList;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Long> getFollowersByUserId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.ParticipantFollowers.followersUserId" );
    query.setParameter( "userId", userId );

    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Participant> getParticipantsIAmFollowing( Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.ParticipantFollowers.followers" );
    query.setParameter( "participantId", participantId );
    List<Participant> particpantList = query.list();
    for ( Participant bean : particpantList )
    {
      Hibernate.initialize( bean.getUserAddresses() );
      Hibernate.initialize( bean.getUserNodes() );
      Hibernate.initialize( bean.getParticipantEmployers() );
    }
    return particpantList;
  }

  @Override
  public ParticipantFollowers getById( Long participantId, Long followerId ) throws ServiceErrorException
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.ParticipantFollowers.getParticipantFollowerByParticipantAndFollower" );
    query.setParameter( "participantId", participantId );
    query.setParameter( "followerId", followerId );
    return (ParticipantFollowers)query.list().get( 0 );
  }

  @Override
  public long getFollowersCountByUserId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.ParticipantFollowers.followersCount" );
    query.setParameter( "userId", userId );
    Object result = query.uniqueResult();
    if ( result != null )
    {
      return ( (Long)result ).longValue();
    }
    else
    {
      return 0;
    }
  }

  @Override
  public void addParticipantFollowee( ParticipantFollowers addParticiapantFollowee ) throws ServiceErrorException
  {
    getSession().save( addParticiapantFollowee );
  }

  @Override
  @SuppressWarnings( "unchecked" )
  public List<ParticipantFollowers> getParticipantsWhoAreFollowingMe( Long currentUserId )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( ParticipantFollowers.class );
    criteria.add( Restrictions.eq( "follower.id", currentUserId ) );
    return criteria.list();
  }

  @Override
  public void removeParticipantFollowee( ParticipantFollowers removeParticiapantFollowee ) throws ServiceErrorException
  {
    getSession().delete( removeParticiapantFollowee );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Long> getAllParticipantIDsByLastName( String creatorLastName )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = null;
    String queryParam = null;

    queryParam = "%" + creatorLastName.toUpperCase() + "%";
    queryParam = queryParam.replaceAll( "'", "''" ); // escape single quotes
    queryParam = queryParam.replaceAll( "\\.", "" ); // escape dots
    queryParam = queryParam.replaceAll( "\\*", "" ); // escape *
    query = session.createSQLQuery( "select user_id from application_user where upper(last_name) like '" + queryParam + "'" ).addScalar( "user_id", LongType.INSTANCE );

    return query.list();
  }

  @Override
  public String getLNameFNameByPaxIdWithComma( Long contestOwnerId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.user.getLNameFNameByPaxIdWithComma" );
    query.setParameter( "participantId", contestOwnerId );
    return (String)query.uniqueResult();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Long> findPaxIdsWhoDisabledPublicProfile( List<Long> forPaxIds )
  {
    if ( CollectionUtils.isEmpty( forPaxIds ) )
    {
      return new ArrayList<Long>();
    }

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.participant.findPaxIdsWhoDisabledPublicProfile" );
    query.setParameter( "flag", false );
    query.setParameterList( "forPaxIds", forPaxIds );
    return query.list();
  }

  public void saveTNCHistory( UserTNCHistory userTNCHistory )
  {
    getSession().save( userTNCHistory );
  }

  public boolean isPaxOptedOutOfAwards( Long paxId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.isPaxOptedOutOfAwards" );
    query.setParameter( "paxId", paxId );
    return BooleanUtils.toBoolean( (Integer)query.uniqueResult() );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<PaxContactType> getValidUserContactMethodsByEmail( String email )
  {
    CallPrcGetPaxContactInfo procedure = new CallPrcGetPaxContactInfo( dataSource );
    Map<String, Object> map = procedure.executeProcedure( email );
    return (List<PaxContactType>)map.get( "p_out_result_set" );
  }

  @Override
  public Map<String, Object> getGToHoneycombSyncPaxData( List<Long> userIds )
  {
    CallPrcGHoneycombSync procedure = new CallPrcGHoneycombSync( dataSource );
    return procedure.executeProcedure( userIds );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<PaxContactType> getValidUniqueUserContactMethodsByUserId( Long userId )
  {
    CallPrcGetPaxContactInfo procedure = new CallPrcGetPaxContactInfo( dataSource );
    Map<String, Object> map = procedure.executeProcedureUniqueContacts( userId );
    return (List<PaxContactType>)map.get( "p_out_result_set" );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<PaxContactType> getValidUserContactMethodsByUserId( Long userId )
  {
    CallPrcGetPaxContactInfo procedure = new CallPrcGetPaxContactInfo( dataSource );
    Map<String, Object> map = procedure.executeProcedure( userId );
    return (List<PaxContactType>)map.get( "p_out_result_set" );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<PaxContactType> getValidUserContactMethodsByPhone( String phoneNumber )
  {
    phoneNumber = phoneNumber.replaceFirst( "^0+", "" );
    CallPrcGetUserContactByPhone phoneProcedure = new CallPrcGetUserContactByPhone( dataSource );
    Map<String, Object> phoneProcedureResults = phoneProcedure.executeProcedureAll( phoneNumber );
    return (List<PaxContactType>)phoneProcedureResults.get( "p_out_result_set" );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<PaxContactType> getValidUniqueUserContactMethodsByEmailOrPhone( String emailOrPhone )
  {
    List<PaxContactType> uniqueContactMethods = new ArrayList<>();

    // We _could_ try looking at the string and figure out if it's an email or phone. For now, we'll
    // just call both procs and merge the results.
    CallPrcGetPaxContactInfo emailProcedure = new CallPrcGetPaxContactInfo( dataSource );
    Map<String, Object> emailProcedureResults = emailProcedure.executeProcedureUniqueContacts( emailOrPhone );
    uniqueContactMethods.addAll( (List<PaxContactType>)emailProcedureResults.get( "p_out_result_set" ) );

    CallPrcGetUserContactByPhone phoneProcedure = new CallPrcGetUserContactByPhone( dataSource );
    String strippedPhone = emailOrPhone.replaceFirst( "^0+", "" );
    Map<String, Object> phoneProcedureResults = phoneProcedure.executeProcedureUnique( strippedPhone );
    uniqueContactMethods.addAll( (List<PaxContactType>)phoneProcedureResults.get( "p_out_result_set" ) );

    return uniqueContactMethods;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<PaxContactType> getAdditionalContactMethodsByEmailOrPhone( String emailOrPhone )
  {
    List<PaxContactType> contactMethods = new ArrayList<>();

    CallPrcGetPaxContactInfo emailProcedure = new CallPrcGetPaxContactInfo( dataSource );
    Map<String, Object> emailProcedureResults = emailProcedure.executeProcedure( emailOrPhone );
    contactMethods.addAll( (List<PaxContactType>)emailProcedureResults.get( "p_out_result_set" ) );

    // If there are any letters, we know it's an email for sure, and we can skip this bit
    if ( !emailOrPhone.matches( "(.*?)[a-zA-Z@](.*?)" ) )
    {
      CallPrcGetUserContactByPhone phoneProcedure = new CallPrcGetUserContactByPhone( dataSource );
      String trimmedPhoneNumber = emailOrPhone.replaceFirst( "^0+", "" ).replaceAll( "\\D", "" );
      Map<String, Object> phoneProcedureResults = phoneProcedure.executeProcedureAll( trimmedPhoneNumber );
      contactMethods.addAll( (List<PaxContactType>)phoneProcedureResults.get( "p_out_result_set" ) );
    }

    return contactMethods;
  }

  @Override
  public boolean isParticipantRecoveryContactsAvailable( Long paxId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.participant.isParticipantRecoveryOptionsAvailable" );
    query.setParameter( "paxId", paxId );
    return ( (Integer)query.uniqueResult() ).intValue() > 0;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<PaxContactType> getContactsAutocompleteEmail( String initialQuery, String searchQuery )
  {
    CallPrcGetUserContactAutoCompleteEmail procedure = new CallPrcGetUserContactAutoCompleteEmail( dataSource );
    Map<String, Object> map = procedure.executeProcedure( initialQuery, searchQuery );
    return (List<PaxContactType>)map.get( "p_out_result_set" );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<PaxContactType> getContactsAutocompletePhone( String initialQuery, String searchQuery )
  {
    CallPrcGetUserContactAutoCompletePhone procedure = new CallPrcGetUserContactAutoCompletePhone( dataSource );
    Map<String, Object> map = procedure.executeProcedure( initialQuery, searchQuery );
    return (List<PaxContactType>)map.get( "p_out_result_set" );
  }

  public String getHeroModuleAppAudienceTypeByUserId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.participant.findHeroModuleAppAudienceTypeByUserId" );
    query.setParameter( "pax_user_id", userId );

    return (String)query.uniqueResult();

  }

  @Override
  public List<UserNode> getAllManagerAndOwner()
  {
    Criteria objCriteria = getSession().createCriteria( UserNode.class );
    objCriteria.add( Restrictions.eq( "active", Boolean.TRUE ) );
    objCriteria.add( Restrictions.or( Restrictions.eq( "hierarchyRoleType", HierarchyRoleType.lookup( HierarchyRoleType.MANAGER ) ),
                                      Restrictions.eq( "hierarchyRoleType", HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) ) ) );
    objCriteria.add( Restrictions.eq( "isPrimary", Boolean.TRUE ) );
    return objCriteria.list();
  }

  @Override
  public Map inActivateBiwUsers( Long runByuserId )
  {
    CallInActivateBiwUsersProc inActivateBiwUsersProc = new CallInActivateBiwUsersProc( dataSource );
    return inActivateBiwUsersProc.executeProcedure( runByuserId );
  }
  //client customization start - wip 52159
  public List<Participant> getUsersByCharacteristicIdAndValue( Long charId, String charValue )
  {
	 Query query = getSession().getNamedQuery( "com.biperf.core.domain.participant.getUsersByCharacteristicIdAndValue" );
	 query.setParameter( "charId", charId );
	 query.setParameter( "charValue", charValue );
	 query.setResultTransformer( new ParticipantAudienceRowMapper() );
	 return query.list();  
  }
  //client customization end - wip 52159
  @Override
  public List<PaxAvatarData> getNotMigratedPaxAvatarData()
  {
    getSession().flush();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getNotMigratedPaxAvatarData" );
    query.setResultTransformer( Transformers.aliasToBean( PaxAvatarData.class ) );
    return (List<PaxAvatarData>)query.list();

  } 
  @Override
  public void updateMigratedPaxAvatarData( Long userId, String avatarOriginal, String avatarSmall )
  {
    String query = "update participant set avatar_original = ? , avatar_small = ?, date_modified = sysdate where user_id = ?";

    Object[] params = { avatarOriginal, avatarSmall, userId };

    try
    {
      jdbcTemplate.update( query, params );
    }
    catch( DataAccessException e )
    {
      log.error( "DAO Layer, the participant id : " + userId + " : " + e );
    }

  }

  @Override
  public List<Long> getAllEligibleApproversForCustomApprovalWithOpenClaims( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.approver.getAllEligibleApproversForCustomApprovalWithOpenClaims" );
    query.setParameter( "promotionId", promotionId );
    return query.list();
  }

  @Override
  public List<PaxAvatarData> getUpdatedRosterUserIdPaxAvatarData()
  {
    getSession().flush();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getUpdatedRosterUserIdPaxAvatarData" );
    query.setResultTransformer( Transformers.aliasToBean( PaxAvatarData.class ) );
    return (List<PaxAvatarData>)query.list();
  }
  /* Customization for WIP 39735 starts here */
  @Override
  public List<Participant> getNodeMemberForPurlMgrRecipient( Long purlRecipientId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.participant.getNodeMemberForPurlMgrRecipient" );
    query.setParameter( "purlRecipientId", purlRecipientId );
    query.setResultTransformer( new ParticipantAudienceRowMapper() );
    return query.list();
  }
  /* Customization for WIP 39735 ends here */
  
  @SuppressWarnings( "unchecked" )
  public List<ClientPublicRecognitionDeptBean> getAllActiveDepartmentsForPublicRecognition()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.participant.getAllActiveDepartmentsForPublicRecognition" );
    query.setResultTransformer( (ResultTransformer)new ActiveDepartmentsForPublicRecogRowMapper() );

    return query.list();
  }

  @SuppressWarnings( "serial" )
  public class ActiveDepartmentsForPublicRecogRowMapper extends BaseReportsResultTransformer
  {
    public ClientPublicRecognitionDeptBean transformTuple( Object[] tuple, String[] aliases )
    {
      ClientPublicRecognitionDeptBean deptBean = new ClientPublicRecognitionDeptBean();
      String deptName = extractString( tuple[0] );
      deptBean.setCode( deptName );
      deptBean.setName( deptName );
      deptBean.setDesc( deptName );
      deptBean.setActive( "true" );
      //deptBean.setSortOrder();
      deptBean.setPickListAssetCode( "picklist.department.type" );
      deptBean.setDescription( deptName );
      deptBean.setPickListItemsAssetCode( "picklist.department.type.items" );

      return deptBean;
    }
  }
  
  //customization - tuning start
  @SuppressWarnings( "unchecked" )
  public List<String> getByPositionTypeForAutoComplete( String startsWith )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.participant.getByPositionTypeForAutoComplete" );
    query.setParameter( "startsWith", startsWith.toLowerCase() );

    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  public List<String> getByDepartmentTypeForAutoComplete( String startsWith )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.participant.getByDepartmentTypeForAutoComplete" );
    query.setParameter( "startsWith", startsWith.toLowerCase() );

    return query.list();
  }
  //customization end

}
