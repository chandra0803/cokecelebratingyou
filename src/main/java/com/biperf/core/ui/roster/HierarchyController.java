
package com.biperf.core.ui.roster;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.enums.CharacteristicVisibility;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BadRequestException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.NodeToNodeCharacteristicAssociationRequest;
import com.biperf.core.ui.roster.exception.RosterException;
import com.biw.digs.rest.enums.DataFormatEnum;
import com.biw.digs.rest.enums.DataTypeEnum;
import com.biw.digs.rest.request.CreateHierarchyAttributeDefinitionRequest;
import com.biw.digs.rest.request.CreateHierarchyNodeAttributeDefinitionRequest;
import com.biw.digs.rest.request.CreateHierarchyNodeAttributeRequest;
import com.biw.digs.rest.request.CreateHierarchyNodeRequest;
import com.biw.digs.rest.request.HierarchyCreateRequest;
import com.biw.digs.rest.request.HierarchyNodeRequest;
import com.biw.digs.rest.request.HierarchySearchRequest;
import com.biw.digs.rest.request.HierarchyStructureCreateRequest;
import com.biw.digs.rest.request.UpdateHierarchyNodeRequest;
import com.biw.digs.rest.response.AttributeDescriptionView;
import com.biw.digs.rest.response.AttributeDescriptions;
import com.biw.digs.rest.response.AttributeView;
import com.biw.digs.rest.response.Hierarchies;
import com.biw.digs.rest.response.HierarchyNodePersonsView;
import com.biw.digs.rest.response.HierarchyNodeView;
import com.biw.digs.rest.response.HierarchyNodesView;
import com.biw.digs.rest.response.HierarchyView;
import com.biw.digs.rest.response.Persons;
import com.biw.digs.rest.service.HierarchyService;

@Controller
public class HierarchyController extends RosterBaseController implements HierarchyService
{

  private static final Log log = LogFactory.getLog( HierarchyController.class );

  @SuppressWarnings( "unchecked" )
  @Override
  public Hierarchies getAllHierarchies( UUID companyId )
  {

    Hierarchies response = new Hierarchies();
    response.setHierarchies( buildHierarchyView( hierarchyService.getActiveHierarchies() ) );

    return response;
  }

  // TODO : Half implementation will re-work later based on the digs implementation.
  @Override
  public HierarchyView createHierarchy( UUID companyId, HierarchyCreateRequest request )
  {
    Hierarchy response = null;
    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( request.getName() );
    try
    {
      response = hierarchyService.save( hierarchy );
    }
    catch( ServiceErrorException e )
    {
      throw new BadRequestException( "Create hierarchy is unsuccessful." );
    }

    return buildHierarchyView( response );
  }

  @Override
  public HierarchyView getHierarchyView( UUID companyId, UUID id )
  {
    return buildHierarchyViewComplete( hierarchyService.getHierarchyByRosterHierarchyId( id ) );
  }

  @Override
  public HierarchyView updateHierarchy( UUID companyId, UUID id, HierarchyCreateRequest request )
  {
    Hierarchy updateObject = null, responseObject = null;

    try
    {
      updateObject = hierarchyService.getHierarchyByRosterHierarchyId( id );
      updateObject.setName( request.getName() );
      responseObject = hierarchyService.save( updateObject );
    }
    catch( ServiceErrorException e )
    {
      throw new RosterException( "Update hierarchy is unsuccessful." );
    }

    return buildHierarchyView( responseObject );
  }

  @Override
  public void deleteHierarchy( UUID companyId, UUID id )
  {

    try
    {
      Hierarchy hierarchy = hierarchyService.getHierarchyByRosterHierarchyId( id );

      if ( !hierarchy.isDeleted() )
      {
        hierarchyService.delete( hierarchy );
      }
      else
      {
        throw new RosterException( "Hierarchy id has already been deleted." );
      }

    }
    catch( ServiceErrorException e )
    {
      throw new RosterException( "Invalid hierarchy id" );
    }

  }

  @SuppressWarnings( "unchecked" )
  @Override
  public Hierarchies searchHierarchies( UUID companyId, HierarchySearchRequest request )
  {

    Hierarchies response = new Hierarchies();
    response
        .setHierarchies( buildHierarchyView( hierarchyService.getActiveHierarchies(),
                                             Objects.nonNull( request.getHierarchyId() ) ? hierarchyService.getHierarchyIdByRosterHierarchyId( request.getHierarchyId() ) : null,
                                             request.getName() ) );

    return response;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public Persons getAllPersons( UUID companyId, UUID id )
  {
    List<User> userList = new ArrayList<User>();
    Persons response = new Persons();

    List<Node> nodeList = nodeService.getNodesAsHierarchy( hierarchyService.getHierarchyIdByRosterHierarchyId( id ) );
    if ( !nodeList.isEmpty() )
    {
      nodeList.forEach( val -> userList.addAll( userService.getAllUsersOnNode( val.getId() ) ) );
    }

    response.setPersons( buildPersonView( userList ) );

    return response;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public HierarchyNodePersonsView getHierarchyNodeMembers( UUID companyId, UUID id, UUID nodeId )
  {
    Node node = null;
    node = nodeService.getNodeByRosterNodeId( nodeId );

    Long hierarchyId = hierarchyService.getHierarchyIdByRosterHierarchyId( id );

    if ( !node.getRosterNodeId().equals( nodeId ) )
    {
      node = nodeService.getNodeByHierarchyIdAndNodeId( hierarchyId, node.getId() );
    }

    if ( Objects.isNull( node ) )
    {
      throw new RosterException( "HierarchyNode with id '" + node.getId() + "' and hierarchy id " + hierarchyId + " not found." );
    }

    HierarchyNodePersonsView response = new HierarchyNodePersonsView();
    response.setHierarchyId( id );
    response.setId( nodeId );
    response.setName( node.getName() );
    response.setParentId( ( node.getParentNode() != null ) ? node.getParentNode().getRosterNodeId() : null );
    List<User> responseUsers = userService.getAllUsersOnNode( node.getId() );
    if ( !responseUsers.isEmpty() )
    {
      responseUsers.forEach( person -> response.getPersons().add( buildPersonView( person ) ) );
    }

    return response;
  }

  @Override
  public HierarchyView createHierarchyStructure( UUID companyId, HierarchyStructureCreateRequest request )
  {
    Hierarchy createHierarchyObj = null;
    NodeType rosterNodetype = null;

    validStructureRequest( request.getName() );

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( request.getName() );

    try
    {
      createHierarchyObj = hierarchyService.save( hierarchy );
    }
    catch( ServiceErrorException e )
    {
      throw new RosterException( "Create hierarchy is unsuccessful." );
    }

    rosterNodetype = createOrGetRosterNodeType( nodeTypeService.getAll() );

    for ( HierarchyNodeRequest node : request.getHierarchyNodes() )
    {
      Node savedRootNode = nodeService.saveNode( getNodeDomainObject( node, createHierarchyObj.getId(), createHierarchyObj, rosterNodetype ), 0L, true );
      buildChildNodes( node, savedRootNode, createHierarchyObj, rosterNodetype );
    }

    return buildHierarchyViewComplete( hierarchyService.getById( createHierarchyObj.getId() ) );
  }

  @Override
  public AttributeDescriptions getAttributeDescriptionsForHierarchies( UUID companyId )
  {
    throw new RosterException( "Hierarchy attribute descriptions are not supported in this system." );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public AttributeDescriptions getAttributeDescriptionsForHierarchyNodes( UUID companyId )
  {

    AttributeDescriptions response = new AttributeDescriptions();
    AssociationRequestCollection nodeAssociationRequestCollection = new AssociationRequestCollection();
    nodeAssociationRequestCollection.add( new NodeToNodeCharacteristicAssociationRequest() );

    List<Node> nodeList = nodeService.getAll( nodeAssociationRequestCollection );

    if ( !nodeList.isEmpty() )
    {
      nodeList.stream().forEach( attribute -> response.getAttributes().add( buildAttributeDescriptionView( attribute ) ) );
    }

    return response;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public HierarchyNodesView getHierarchyNodes( UUID companyId, UUID id )
  {
    HierarchyNodesView nodes = new HierarchyNodesView();
    List<Node> nodeList = nodeService.getNodesByHierarchy( hierarchyService.getHierarchyIdByRosterHierarchyId( id ) );
    if ( !nodeList.isEmpty() )
    {
      nodes.getHierarchies().addAll( nodeList.stream().map( this::buildHierarchyNodeDetail ).collect( Collectors.toList() ) );
    }

    return nodes;
  }

  // Soft delete
  @Override
  public void deleteHierarchyNode( UUID companyId, UUID id, UUID nodeId )
  {
    Node node = null;
    node = nodeService.getNodeByRosterNodeId( nodeId );
    if ( !node.getRosterNodeId().equals( nodeId ) )
    {
      node = nodeService.getNodeByHierarchyIdAndNodeId( hierarchyService.getHierarchyIdByRosterHierarchyId( id ), node.getId() );
    }

    if ( Objects.nonNull( node ) )
    {
      nodeService.deleteNode( node );
    }
    else
    {
      throw new RosterException( "Node is not exist under this hierarchy." );
    }

  }

  @Override
  public HierarchyNodeView createHierarchyNode( UUID companyId, UUID id, CreateHierarchyNodeRequest request )
  {
    NodeType rosterNodetype = null;
    Node rootNode = null;
    Node savedNode = null;

    Hierarchy hierarchy = hierarchyService.getHierarchyByRosterHierarchyId( id );

    if ( Objects.nonNull( hierarchy ) )
    {
      rootNode = getRosterRootNode( hierarchy );
      rosterNodetype = createOrGetRosterNodeType( nodeTypeService.getAll() );

      savedNode = createHierarchyNodeWithCharacteristics( request.getName(),
                                                          Objects.nonNull( request.getParentId() ) ? nodeService.getNodeIdByRosterNodeId( request.getParentId() ) : null,
                                                          rootNode,
                                                          rosterNodetype,
                                                          hierarchy,
                                                          request.getHierarchyNodeAttributes() );
      return buildHierarchyNodeView( savedNode );
    }
    else
    {
      throw new RosterException( "Hierarchy is not exist." );
    }

  }

  /*
   * TODO : Not implemented since update an existing Hierarchy Node including
   * adding/updating/removing the parent HierarchyNode association and attributes, will create issue
   * in DM Hierarchy structure. We’ll take it up later.
   */

  @SuppressWarnings( "unchecked" )
  @Override
  public HierarchyNodeView updateHierarchyNode( UUID companyId, UUID id, UUID nodeId, UpdateHierarchyNodeRequest request )
  {
    throw new RosterException( "Not allowed, since update an existing Hierarchy Node including adding/updating/removing the parent HierarchyNode association and attributes, will create issue in DM Hierarchy structure." );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public AttributeView createAttributeForHierarchyNode( UUID companyId, UUID id, UUID nodeId, CreateHierarchyNodeAttributeRequest request )
  {
    NodeType rosterNodetype = null;
    Node rootNode = null;
    Node savedNode = null;
    Optional<NodeCharacteristic> optionalNodeCharacteristic = null;

    Hierarchy hierarchy = hierarchyService.getHierarchyByRosterHierarchyId( id );
    Long inputNodeId = nodeService.getNodeIdByRosterNodeId( nodeId );
    Node node = nodeService.getNodeByHierarchyIdAndNodeId( hierarchy.getId(), inputNodeId );

    if ( Objects.nonNull( node ) )
    {
      rosterNodetype = createOrGetRosterNodeType( nodeTypeService.getAll() );
      rootNode = getRosterRootNode( hierarchy );
      node = getNodeWithCharacteristicsAssociationsById( node.getId() );

      List<CreateHierarchyNodeAttributeRequest> createHierarchyNodeAttributeRequestList = new ArrayList<CreateHierarchyNodeAttributeRequest>();
      CreateHierarchyNodeAttributeRequest createHierarchyNodeAttributeRequest = new CreateHierarchyNodeAttributeRequest();
      createHierarchyNodeAttributeRequest.setName( request.getName() );
      createHierarchyNodeAttributeRequest.setValue( request.getValue() );

      createHierarchyNodeAttributeRequestList.add( createHierarchyNodeAttributeRequest );

      Long parentNodeId = node.getParentNode().getId();

      Set<NodeCharacteristic> inNodeSet = new HashSet<>();
      List<Characteristic> allCharacterstics = nodeTypeCharacteristicService.getAllCharacteristics();
      Characteristic characteristic = allCharacterstics.stream().filter( obj -> obj.getCharacteristicName().equalsIgnoreCase( request.getName() ) ).findFirst().orElse( null );

      if ( Objects.nonNull( characteristic ) )
      {
        Set<NodeCharacteristic> nodeCharSet = (Set<NodeCharacteristic>)node.getNodeCharacteristics();

        NodeCharacteristic existingNode = nodeCharSet.stream().filter( inObj -> inObj.getNodeTypeCharacteristicType().getId().equals( characteristic.getId() ) ).findFirst().orElse( null );

        if ( Objects.nonNull( existingNode ) )
        {
          existingNode.setCharacteristicValue( request.getValue() );

          inNodeSet.add( existingNode );
        }

        node.setNodeCharacteristics( inNodeSet );
      }
      else
      {
        node.setNodeCharacteristics( saveOrUpdateNodeCharacteristics( !createHierarchyNodeAttributeRequestList.isEmpty() ? createHierarchyNodeAttributeRequestList : null, node, rosterNodetype ) );
      }

      savedNode = nodeService.saveNode( node, Objects.nonNull( parentNodeId ) ? parentNodeId : Objects.nonNull( rootNode ) ? rootNode.getId() : 0L, true );

      Set<NodeCharacteristic> nodeCharacteristics = (Set<NodeCharacteristic>)savedNode.getActiveNodeCharacteristics();

      optionalNodeCharacteristic = nodeCharacteristics.stream().filter( attribute -> request.getName().equalsIgnoreCase( attribute.getNodeTypeCharacteristicType().getCharacteristicName() )
          && request.getValue().equalsIgnoreCase( attribute.getCharacteristicValue() ) ).findFirst();

      return buildHierarchyNodeAttributeView( optionalNodeCharacteristic.get() );

    }
    else
    {
      throw new RosterException( "Node is not exist." );
    }

  }

  @Override
  public AttributeDescriptionView createHierarchyAttributeDescription( UUID companyId, CreateHierarchyAttributeDefinitionRequest request )
  {
    throw new RosterException( "Hierarchy Attribute description is not suppourted." );
  }

  @Override
  public AttributeDescriptionView createHierarchyNodeAttributeDescription( UUID companyId, CreateHierarchyNodeAttributeDefinitionRequest request )
  {
    if ( log.isDebugEnabled() )
    {
      log.debug( "companyId : " + companyId );
    }
    AttributeDescriptionView view = new AttributeDescriptionView();
    AssociationRequestCollection nodeAssociationRequestCollection = new AssociationRequestCollection();
    nodeAssociationRequestCollection.add( new NodeToNodeCharacteristicAssociationRequest() );

    if ( request.getName().isEmpty() )
    {
      throw new RosterException( "Attribute Definition name cannot be blank" );
    }
    if ( request.getDataType().isEmpty() )
    {
      throw new RosterException( "Attribute Definition dataType cannot be blank" );
    }
    if ( request.getDataFormat().isEmpty() )
    {
      throw new RosterException( "Attribute Definition dataFormat cannot be blank" );
    }
    NodeTypeCharacteristicType nodeCharacteristicType = new NodeTypeCharacteristicType();
    nodeCharacteristicType.setCharacteristicName( request.getName() );
    List<Node> nodeList = nodeService.getAll( nodeAssociationRequestCollection );
    for ( Node node : nodeList )
    {
      if ( node.getName().equalsIgnoreCase( request.getName() ) )
      {
        throw new BadRequestException( "Attribute Description '" + request.getName() + " already exists" );
      }
    }
    // validate
    if ( !DataTypeEnum.isValidEnum( request.getDataType() ) )
    {
      throw new BadRequestException( "Provided Attribute Description has an invalid data type of '" + request.getDataType() + "'.  Valid values are "
          + buildEnumStringValues( DataTypeEnum.values() ) );
    }
    if ( !DataFormatEnum.isValidEnum( request.getDataFormat() ) )
    {
      throw new BadRequestException( "Provided Attribute Description has an invalid data format of '" + request.getDataFormat() + "'.  Valid values are "
          + buildEnumStringValues( DataFormatEnum.values() ) );
    }
    @SuppressWarnings( "resource" )
    Scanner scanner = new Scanner( request.getDataType() );

    if ( scanner.hasNextBoolean() )
    {
      nodeCharacteristicType.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.BOOLEAN ) );
    }
    else if ( scanner.hasNextInt() )
    {
      nodeCharacteristicType.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.INTEGER ) );
    }
    else if ( scanner.hasNextFloat() || scanner.hasNextDouble() )
    {
      nodeCharacteristicType.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.DECIMAL ) );
    }
    else if ( isDate( request.getDataType() ) )
    {
      nodeCharacteristicType.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.DATE ) );
    }
    else
    {
      nodeCharacteristicType.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.TEXT ) );
      nodeCharacteristicType.setMaxSize( new Long( 18 ) );// max value in database
    }
    nodeCharacteristicType.setVisibility( CharacteristicVisibility.lookup( CharacteristicVisibility.VISIBLE ) );
    nodeCharacteristicType.setIsRequired( Boolean.FALSE );
    nodeCharacteristicType.setActive( Boolean.TRUE );
    for ( Node node : nodeList )
    {
      if ( node.getName().equalsIgnoreCase( request.getName() ) )
      {
        nodeCharacteristicType.setDomainId( node.getNodeType().getId() );
      }
    }

    try
    {
      Characteristic characteristic = nodeTypeCharacteristicService.saveCharacteristic( nodeCharacteristicType );
      view.setDataFormat( characteristic.getCharacteristicName() );
      view.setDataType( characteristic.getCharacteristicDataType().getCode() );
      view.setDataFormat( characteristic.getCharacteristicDataType().getName() );
    }
    catch( ServiceErrorException e )
    {
      e.printStackTrace();
    }

    return view;
  }

}
