/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/budget/BudgetExtractParametersController.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.budget;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.budget.BudgetSegmentToBudgetsAssociationRequest;
import com.biperf.core.service.hierarchy.NodeTypeCharacteristicService;
import com.biperf.core.service.hierarchy.NodeTypeService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.ActionFormUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * BudgetMasterViewController.
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
 * <td>meadows</td>
 * <td>Aug 15, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetExtractParametersController extends BaseController
{

  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    BudgetExtractParametersForm budgetExtractParametersForm = (BudgetExtractParametersForm)ActionFormUtils.getActionForm( request, servletContext, "budgetExtractParametersForm" );
    // budgetExtractParametersForm.setAllOwners(true);
    Long budgetSegmentId = null;
    try
    {
      String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( clientState != null && !clientState.equals( "" ) )
      {
        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        // Deserialize the client state.
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        budgetSegmentId = (Long)clientStateMap.get( "budgetSegmentId" );
      }
      else
      {
        budgetSegmentId = new Long( budgetExtractParametersForm.getBudgetSegmentId() );
      }

    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new BudgetSegmentToBudgetsAssociationRequest() );
    BudgetSegment budgetSegment = getBudgetMasterService().getBudgetSegmentById( budgetSegmentId, associationRequestCollection );
    request.setAttribute( "budgetSegment", budgetSegment );

    BudgetMaster budgetMaster = budgetSegment.getBudgetMaster();
    request.setAttribute( "budgetMaster", budgetMaster );
    List nodeTypes;
    if ( budgetMaster.getBudgetType().isPaxBudgetType() )
    {
      // Dummy up nodeType list for Pax types to simplify JSP code
      nodeTypes = new ArrayList();
      nodeTypes.add( Boolean.TRUE );
      request.setAttribute( "characteristicList", getUserCharacteristicList() );
    }
    else
    {
      List tempNodeTypes = getNodeTypeService().getAll();
      nodeTypes = new ArrayList( tempNodeTypes.size() );
      for ( Iterator iter = tempNodeTypes.iterator(); iter.hasNext(); )
      {
        NodeType tempNodeType = (NodeType)iter.next();
        // check to see that at least one of our budgets has the node type before adding.
        boolean nodeFound = false;
        for ( Iterator budgetListIter = budgetSegment.getBudgets().iterator(); budgetListIter.hasNext(); )
        {
          Budget budget = (Budget)budgetListIter.next();
          if ( budget.getNode().getNodeType().equals( tempNodeType ) )
          {
            nodeFound = true;
            break;
          }
        }

        if ( nodeFound )
        {
          NodeTypeFormBean nodeTypeFormBean = new NodeTypeFormBean();
          nodeTypeFormBean.setNodeType( tempNodeType );
          nodeTypeFormBean.setAllBudgets( true );
          nodeTypes.add( nodeTypeFormBean );
        }
      }
      request.setAttribute( "characteristicMap", getNodeCharacteristicMap( nodeTypes ) );
    }
    if ( BudgetType.PAX_BUDGET_TYPE.equals( budgetMaster.getBudgetType().getCode() ) )
    {
      request.setAttribute( "paxType", Boolean.TRUE );
    }
    else
    {
      request.setAttribute( "paxType", Boolean.FALSE );
    }
    request.setAttribute( "nodeList", nodeTypes );
  }

  private CharacteristicFormBean[] getUserCharacteristicList()
  {
    List characteristics = getUserCharacteristicService().getAllCharacteristics();
    CharacteristicFormBean[] characteristicFormBeans = new CharacteristicFormBean[0];
    if ( characteristics != null )
    {
      characteristicFormBeans = new CharacteristicFormBean[characteristics.size()];
      int index = 0;
      for ( Iterator iter = characteristics.iterator(); iter.hasNext(); )
      {
        Characteristic characteristic = (Characteristic)iter.next();
        CharacteristicFormBean characteristicFormBean = new CharacteristicFormBean();
        characteristicFormBean.setCmAssetCode( characteristic.getCmAssetCode() );
        characteristicFormBean.setNameCmKey( characteristic.getNameCmKey() );
        characteristicFormBean.setDataType( characteristic.getCharacteristicDataType() );
        characteristicFormBean.setCharacteristicId( characteristic.getId() );
        List valueList = new ArrayList();
        if ( characteristic.getCharacteristicDataType().isSelectType() )
        {
          List pickListItems = DynaPickListType.getList( characteristic.getPlName() );
          for ( Iterator itemIter = pickListItems.iterator(); itemIter.hasNext(); )
          {
            DynaPickListType dynaPickListType = (DynaPickListType)itemIter.next();
            valueList.add( new CheckItem( dynaPickListType.getCode(), dynaPickListType.getName() ) );
          }
          characteristicFormBean.setCheckList( valueList );
        }
        characteristicFormBeans[index++] = characteristicFormBean;
      }
    }
    return characteristicFormBeans;
  }

  private Map getNodeCharacteristicMap( List nodeTypes )
  {
    Map characteristicsMap = new TreeMap( new Comparator()
    {
      public int compare( Object o1, Object o2 )
      {
        NodeTypeFormBean e1 = (NodeTypeFormBean)o1;
        NodeTypeFormBean e2 = (NodeTypeFormBean)o2;

        return e1.getNodeType().getId().compareTo( e2.getNodeType().getId() );
      }
    } );
    for ( Iterator nodeTypeIter = nodeTypes.iterator(); nodeTypeIter.hasNext(); )
    {
      NodeTypeFormBean nodeTypeFormBean = (NodeTypeFormBean)nodeTypeIter.next();
      List characteristics = getNodeCharacteristicService().getAllNodeTypeCharacteristicTypesByNodeTypeId( nodeTypeFormBean.getNodeType().getId() );
      if ( characteristics != null && characteristics.size() > 0 )
      {
        List characteristicFormBeans;
        if ( characteristics != null )
        {
          characteristicFormBeans = new ArrayList( characteristics.size() );
          for ( Iterator iter = characteristics.iterator(); iter.hasNext(); )
          {
            Characteristic characteristic = (Characteristic)iter.next();
            CharacteristicFormBean characteristicFormBean = new CharacteristicFormBean();
            characteristicFormBean.setCmAssetCode( characteristic.getCmAssetCode() );
            characteristicFormBean.setNameCmKey( characteristic.getNameCmKey() );
            characteristicFormBean.setDataType( characteristic.getCharacteristicDataType() );
            characteristicFormBean.setBooleanSet( true );

            characteristicFormBean.setCharacteristicId( characteristic.getId() );
            List valueList = new ArrayList();
            if ( characteristic.getCharacteristicDataType().isSelectType() )
            {
              List pickListItems = DynaPickListType.getList( characteristic.getPlName() );
              for ( Iterator itemIter = pickListItems.iterator(); itemIter.hasNext(); )
              {
                DynaPickListType dynaPickListType = (DynaPickListType)itemIter.next();
                valueList.add( new CheckItem( dynaPickListType.getCode(), dynaPickListType.getName() ) );
              }
              characteristicFormBean.setCheckList( valueList );
            }
            characteristicFormBeans.add( characteristicFormBean );
          }
          characteristicsMap.put( nodeTypeFormBean, characteristicFormBeans );
        }
      }
    }
    return characteristicsMap;
  }

  /**
   * Gets a Budget Service
   * 
   * @return BudgetService
   */
  private BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)getService( BudgetMasterService.BEAN_NAME );
  } // end

  /**
   * Gets a UserCharacteristicService
   * 
   * @return UserCharacteristicService
   */
  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }

  /**
   * Gets a NodeTypeCharacteristicService
   * 
   * @return NodeTypeCharacteristicService
   */
  private NodeTypeCharacteristicService getNodeCharacteristicService()
  {
    return (NodeTypeCharacteristicService)getService( NodeTypeCharacteristicService.BEAN_NAME );
  }

  /**
   * Gets a NodeTypeService
   * 
   * @return NodeTypeService
   */
  private NodeTypeService getNodeTypeService()
  {
    return (NodeTypeService)getService( NodeTypeService.BEAN_NAME );
  }
}
