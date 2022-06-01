/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/participant/hibernate/ListBuilderDAOImplTest.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.participant.hibernate;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.ListBuilderDAO;
import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.participant.AudienceCriteria;
import com.biperf.core.domain.participant.AudienceCriteriaCharacteristic;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * ListBuilderDAOImplTest.
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
 * <td>sharma</td>
 * <td>Jun 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ListBuilderDAOImplTest extends BaseDAOTest
{
  private static final String LIVE_TEST_NODE_NAME_SUBSTRING = "mid";
  private static final int LIVE_TEST_NODE_TYPE_ID = 5003;
  private static final int LIVE_TEST_ROOT_NODE_ID = 5001;
  public static final Long LIVE_TEST_HIERARCHY_ID = new Long( 5001 );

  public void testSearchNoCriteria()
  {
    // TODO: Depends on live data, setup database with test data to query....

    AudienceCriteria audienceCriteria = new AudienceCriteria();

    List paxList = getListBuilderDAO().searchParticipants( audienceCriteria, LIVE_TEST_HIERARCHY_ID, false );

    System.out.println( "\n\npaxList.count = " + paxList.size() );
    assertTrue( !paxList.isEmpty() );
  }

  public void testSearchAllCriteriaFields()
  {
    // TODO: Depends on live data, setup database with test data to query....

    AudienceCriteria audienceCriteria = new AudienceCriteria();
    audienceCriteria.setEmployerId( new Long( 167 ) );
    audienceCriteria.setDepartmentType( "eng" );
    audienceCriteria.setPositionType( "superv" );

    AudienceCriteriaCharacteristic audienceCriteriaCharacteristic1 = new AudienceCriteriaCharacteristic();
    UserCharacteristicType userCharacteristicType1 = new UserCharacteristicType();
    userCharacteristicType1.setId( new Long( 5520 ) );
    audienceCriteriaCharacteristic1.setCharacteristic( userCharacteristicType1 );
    audienceCriteriaCharacteristic1.setCharacteristicValue( "f" );

    AudienceCriteriaCharacteristic audienceCriteriaCharacteristic2 = new AudienceCriteriaCharacteristic();
    UserCharacteristicType userCharacteristicType2 = new UserCharacteristicType();
    userCharacteristicType2.setId( new Long( 5522 ) );
    audienceCriteriaCharacteristic2.setCharacteristic( userCharacteristicType2 );
    audienceCriteriaCharacteristic2.setCharacteristicValue( "red" );

    Set characteristicCriterias = new LinkedHashSet();
    characteristicCriterias.add( audienceCriteriaCharacteristic1 );
    characteristicCriterias.add( audienceCriteriaCharacteristic2 );
    audienceCriteria.setCharacteristicCriterias( characteristicCriterias );

    audienceCriteria.setNodeRole( HierarchyRoleType.lookup( "mgr" ) );
    List paxList = getListBuilderDAO().searchParticipants( audienceCriteria, LIVE_TEST_HIERARCHY_ID, false );

    System.out.println( "\n\npaxList.count = " + paxList.size() );
    assertTrue( !paxList.isEmpty() );
  }

  public void testSearchOnlyNodeName()
  {
    // TODO: Depends on live data, setup database with test data to query....

    AudienceCriteria audienceCriteria = new AudienceCriteria();
    audienceCriteria.setNodeName( LIVE_TEST_NODE_NAME_SUBSTRING );

    List paxList = getListBuilderDAO().searchParticipants( audienceCriteria, LIVE_TEST_HIERARCHY_ID, false );

    System.out.println( "\n\npaxList.count = " + paxList.size() );
    assertTrue( !paxList.isEmpty() );
  }

  public void testSearchOnlyNodeId()
  {
    // TODO: Depends on live data, setup database with test data to query....

    AudienceCriteria audienceCriteria = new AudienceCriteria();
    audienceCriteria.setNodeId( new Long( 5004 ) );

    List paxList = getListBuilderDAO().searchParticipants( audienceCriteria, LIVE_TEST_HIERARCHY_ID, false );

    System.out.println( "\n\npaxList.count = " + paxList.size() );
    assertTrue( !paxList.isEmpty() );
  }

  public void testSearchOnlyNodeTypeId()
  {
    // TODO: Depends on live data, setup database with test data to query....

    AudienceCriteria audienceCriteria = new AudienceCriteria();
    audienceCriteria.setNodeTypeId( new Long( 5003 ) );

    List paxList = getListBuilderDAO().searchParticipants( audienceCriteria, LIVE_TEST_HIERARCHY_ID, false );

    System.out.println( "\n\npaxList.count = " + paxList.size() );
    assertTrue( !paxList.isEmpty() );
  }

  public void testSearchFirstLastName()
  {
    // TODO: Depends on live data, setup database with test data to query....

    AudienceCriteria audienceCriteria = new AudienceCriteria();
    audienceCriteria.setFirstName( "quEryresult-f" );
    audienceCriteria.setLastName( "quEryresult-l" );

    List paxList = getListBuilderDAO().searchParticipants( audienceCriteria, LIVE_TEST_HIERARCHY_ID, false );

    System.out.println( "\n\npaxList.count = " + paxList.size() );
    assertTrue( !paxList.isEmpty() );
  }

  public void testSearchOnlyNodeNameAllChildren()
  {
    // TODO: Depends on live data, setup database with test data to query....

    AudienceCriteria audienceCriteria = new AudienceCriteria();
    audienceCriteria.setNodeName( LIVE_TEST_NODE_NAME_SUBSTRING );
    audienceCriteria.setChildNodesIncluded( true );

    List paxList = getListBuilderDAO().searchParticipants( audienceCriteria, LIVE_TEST_HIERARCHY_ID, false );

    System.out.println( "\n\npaxList.count = " + paxList.size() );
    assertTrue( !paxList.isEmpty() );
  }

  public void testSearchOnlyNodeIdAllChildren()
  {
    // TODO: Depends on live data, setup database with test data to query....

    AudienceCriteria audienceCriteria = new AudienceCriteria();
    audienceCriteria.setNodeId( new Long( LIVE_TEST_ROOT_NODE_ID ) );
    audienceCriteria.setChildNodesIncluded( true );

    List paxList = getListBuilderDAO().searchParticipants( audienceCriteria, LIVE_TEST_HIERARCHY_ID, false );

    System.out.println( "\n\npaxList.count = " + paxList.size() );
    assertTrue( !paxList.isEmpty() );
  }

  public void testSearchNodeIdNodeTypeIdAllChildren()
  {
    // TODO: Depends on live data, setup database with test data to query....

    AudienceCriteria audienceCriteria = new AudienceCriteria();
    audienceCriteria.setNodeId( new Long( LIVE_TEST_ROOT_NODE_ID ) );
    audienceCriteria.setNodeTypeId( new Long( 5003 ) );
    audienceCriteria.setChildNodesIncluded( true );

    List paxList = getListBuilderDAO().searchParticipants( audienceCriteria, LIVE_TEST_HIERARCHY_ID, false );

    System.out.println( "\n\npaxList.count = " + paxList.size() );
    assertTrue( !paxList.isEmpty() );
  }

  public void testSearchNodeTypeIdWithNodeTypeCharacteristics()
  {
    // TODO: Depends on live data, setup database with test data to query....

    AudienceCriteria audienceCriteria = new AudienceCriteria();
    audienceCriteria.setNodeTypeId( new Long( LIVE_TEST_NODE_TYPE_ID ) );

    AudienceCriteriaCharacteristic audienceCriteriaCharacteristic1 = new AudienceCriteriaCharacteristic();
    NodeTypeCharacteristicType nodeTypeCharacteristicType1 = new NodeTypeCharacteristicType();
    nodeTypeCharacteristicType1.setId( new Long( 5009 ) );
    audienceCriteriaCharacteristic1.setCharacteristic( nodeTypeCharacteristicType1 );
    audienceCriteriaCharacteristic1.setCharacteristicValue( "true" );

    AudienceCriteriaCharacteristic audienceCriteriaCharacteristic2 = new AudienceCriteriaCharacteristic();
    NodeTypeCharacteristicType nodeTypeCharacteristicType2 = new NodeTypeCharacteristicType();
    nodeTypeCharacteristicType2.setId( new Long( 5010 ) );
    audienceCriteriaCharacteristic2.setCharacteristic( nodeTypeCharacteristicType2 );
    audienceCriteriaCharacteristic2.setCharacteristicValue( "123" );

    Set characteristicCriterias = new LinkedHashSet();
    characteristicCriterias.add( audienceCriteriaCharacteristic1 );
    characteristicCriterias.add( audienceCriteriaCharacteristic2 );
    audienceCriteria.setCharacteristicCriterias( characteristicCriterias );

    List paxList = getListBuilderDAO().searchParticipants( audienceCriteria, LIVE_TEST_HIERARCHY_ID, false );

    System.out.println( "\n\npaxList.count = " + paxList.size() );
    assertTrue( !paxList.isEmpty() );
  }

  // TODO: test each criteria stand-alone.

  public void testSearchWithCriteriaAudience()
  {
    CriteriaAudience criteriaAudience = new CriteriaAudience();
    criteriaAudience.addAudienceCriteria( buildFirstNameAudienceCriteria( "quEryresult-f-main" ) );
    criteriaAudience.addAudienceCriteria( buildFirstNameAudienceCriteria( "quEryresult-f-child_node" ) );

    List paxList = getListBuilderDAO().searchParticipants( criteriaAudience, LIVE_TEST_HIERARCHY_ID, true, null, false, false );

    System.out.println( "\n\npaxList.count = " + paxList.size() );
    assertEquals( 2, paxList.size() );
  }

  public void testSearchWithCriteriaAudienceAndCriteriaAudienceFilter()
  {
    CriteriaAudience criteriaAudience = new CriteriaAudience();
    criteriaAudience.addAudienceCriteria( buildFirstNameAudienceCriteria( "quEryresult-f-main" ) );
    criteriaAudience.addAudienceCriteria( buildFirstNameAudienceCriteria( "quEryresult-f-child_node" ) );

    CriteriaAudience filterAudience = new CriteriaAudience();
    filterAudience.addAudienceCriteria( buildFirstNameAudienceCriteria( "quEryresult-f-main" ) );

    List paxList = getListBuilderDAO().searchParticipants( criteriaAudience, LIVE_TEST_HIERARCHY_ID, true, Collections.singleton( filterAudience ), false, false );

    System.out.println( "\n\npaxList.count = " + paxList.size() );
    assertEquals( 1, paxList.size() );
  }

  public void testSearchWithOpenCriteriaAudienceAndCriteriaAudienceFilter()
  {
    CriteriaAudience criteriaAudience = new CriteriaAudience();

    CriteriaAudience filterAudience = new CriteriaAudience();
    filterAudience.addAudienceCriteria( buildFirstNameAudienceCriteria( "quEryresult-f-main" ) );

    List paxList = getListBuilderDAO().searchParticipants( criteriaAudience, LIVE_TEST_HIERARCHY_ID, true, Collections.singleton( filterAudience ), false, false );

    System.out.println( "\n\npaxList.count = " + paxList.size() );
    assertEquals( 1, paxList.size() );
  }

  public void testSearchWithCriteriaAudiencesAndAudienceFilters()
  {

    CriteriaAudience criteriaAudience1 = new CriteriaAudience();
    criteriaAudience1.setName( "criteriaAudience1" );
    criteriaAudience1.addAudienceCriteria( buildFirstNameAudienceCriteria( "quEryresult-f-main" ) );
    criteriaAudience1.addAudienceCriteria( buildFirstNameAudienceCriteria( "quEryresult-f-child_node" ) );

    CriteriaAudience criteriaAudience2 = new CriteriaAudience();
    criteriaAudience2.setName( "criteriaAudience2" );
    criteriaAudience2.addAudienceCriteria( buildFirstNameAudienceCriteria( "quEryresult-f-main" ) );
    criteriaAudience2.addAudienceCriteria( buildFirstNameAudienceCriteria( "quEryresult-f-child_node" ) );

    Set audiences = new LinkedHashSet();
    audiences.add( criteriaAudience1 );
    audiences.add( criteriaAudience2 );

    CriteriaAudience filterAudience1 = new CriteriaAudience();
    filterAudience1.addAudienceCriteria( buildFirstNameAudienceCriteria( "quEryresult-f-main" ) );
    filterAudience1.setName( "filterAudience1" );

    CriteriaAudience filterAudience2 = new CriteriaAudience();
    filterAudience2.addAudienceCriteria( buildFirstNameAudienceCriteria( "quEryresult-f-main" ) );
    filterAudience2.setName( "filterAudience2" );

    Set filterAudiences = new LinkedHashSet();
    filterAudiences.add( filterAudience1 );
    filterAudiences.add( filterAudience2 );

    List paxList = getListBuilderDAO().searchParticipants( audiences, LIVE_TEST_HIERARCHY_ID, true, filterAudiences, false );

    System.out.println( "\n\npaxList.count = " + paxList.size() );
    assertEquals( 1, paxList.size() );
  }

  public void testSearchWithCriteriaAudienceAndPaxAudienceFilter()
  {
    CriteriaAudience criteriaAudience = new CriteriaAudience();
    criteriaAudience.addAudienceCriteria( buildFirstNameAudienceCriteria( "quEryresult-f-main" ) );
    criteriaAudience.addAudienceCriteria( buildFirstNameAudienceCriteria( "quEryresult-f-child_node" ) );

    PaxAudience filterAudience = new PaxAudience();
    filterAudience.setId( new Long( 1 ) );

    List paxList = getListBuilderDAO().searchParticipants( criteriaAudience, LIVE_TEST_HIERARCHY_ID, true, Collections.singleton( filterAudience ), false, false );

    System.out.println( "\n\npaxList.count = " + paxList.size() );
    assertEquals( 1, paxList.size() );
  }

  public static AudienceCriteria buildFirstNameAudienceCriteria( String firstName )
  {
    AudienceCriteria audienceCriteria = new AudienceCriteria();
    audienceCriteria.setFirstName( firstName );

    return audienceCriteria;
  }

  private ListBuilderDAO getListBuilderDAO()
  {
    return (ListBuilderDAO)ApplicationContextFactory.getApplicationContext().getBean( "listBuilderDAO" );
  }

}
