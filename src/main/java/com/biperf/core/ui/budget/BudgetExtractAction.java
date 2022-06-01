/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/budget/BudgetExtractAction.java,v $
 */

package com.biperf.core.ui.budget;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.dao.budget.BudgetQueryConstraint;
import com.biperf.core.dao.budget.limits.BooleanCharacteristicConstraintLimits;
import com.biperf.core.dao.budget.limits.CharacteristicConstraintLimits;
import com.biperf.core.dao.budget.limits.DateCharacteristicConstraintLimits;
import com.biperf.core.dao.budget.limits.DecimalCharacteristicConstraintLimits;
import com.biperf.core.dao.budget.limits.IntegerCharacteristicConstraintLimits;
import com.biperf.core.dao.budget.limits.MultiSelectCharacteristicConstraintLimits;
import com.biperf.core.dao.budget.limits.SingleSelectCharacteristicConstraintLimits;
import com.biperf.core.dao.budget.limits.TextCharacteristicConstraintLimits;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingAttachmentInfo;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.budget.BudgetSegmentToBudgetsAssociationRequest;
import com.biperf.core.service.budget.BudgetToNodeCharacteristicsAssociationRequest;
import com.biperf.core.service.budget.BudgetToNodeUsersAssociationRequest;
import com.biperf.core.service.budget.BudgetToUserCharacteristicsAssociationRequest;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.hierarchy.NodeTypeCharacteristicService;
import com.biperf.core.service.hierarchy.NodeTypeService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ArrayUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.FileExtractUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * BudgetAction.
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
 * <td>robinsra</td>
 * <td>May 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetExtractAction extends BaseDispatchAction
{
  private static final char QUOTE = '"';
  private static final String QUOTE_COMMA_QUOTE = QUOTE + "," + QUOTE;

  // Bug#16395
  public static final String UnixFileSeparator = "/";
  public static final String WindowsFileSeparator = "\\";
  // Bug#16853
  public static final String EMAIL_SENT = "emailSent";

  public ActionForward emailExtract( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    HttpSession session = request.getSession();
    Collection budgetList = (Collection)session.getAttribute( "budgetList" );

    // If required parameters are missing, write invocation log and stop.
    if ( budgetList == null )
    {
      String msg = new String( "Required Parameters missing while emailing the budget extract" );
      log.warn( msg );
    }
    else
    {
      // Get the resultset.
      List rowsReturned = generateEmailExtract( budgetList );

      // If good return code and at least 2 rows back (1st row is always column header even though
      // there is no data)
      if ( rowsReturned.size() > 1 )
      {
        // Save the file.
        File savedFile = writeFile( rowsReturned );
        String absolutePath = "";
        long fileLength = 0;
        if ( savedFile != null )
        {
          absolutePath = savedFile.getAbsolutePath();
        }

        if ( savedFile != null && doesFileExists( savedFile.getAbsolutePath() ) )
        {
          // Email the file to the user.
          sendMessage( savedFile.getAbsolutePath(), savedFile.getName() );
          // BUG #16853
          RequestUtils.setAttribute( request, EMAIL_SENT, "true" );

        }
        else
        {
          if ( savedFile != null )
          {
            fileLength = savedFile.length();
          }
          String msg = new String( "Problem accessing the file attachment while executing the " + " Report: Budget Extract" + ". File:" + absolutePath + " File size:" + printFileSize( fileLength )
              + " The email has not been sent to the participant. Process ended.  " );
          log.warn( msg );
        }
      }
      else
      {
        // No file attachment, just send the email.
        sendMessage( null, null );

        String msg = new String( "Problems with emailing extract" );
        log.warn( msg );

      }
    }
    request.setAttribute( "fileName", generateExtractUniqueFileName() );

    return actionMapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  public ActionForward extract( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    BudgetExtractParametersForm budgetExtractParametersForm = (BudgetExtractParametersForm)actionForm;
    Long budgetSegmentId = convertStringToLong( budgetExtractParametersForm.getBudgetSegmentId() );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new BudgetSegmentToBudgetsAssociationRequest() );
    BudgetSegment budgetSegment = getBudgetMasterService().getBudgetSegmentById( budgetSegmentId, associationRequestCollection );
    BudgetMaster budgetMaster = budgetSegment.getBudgetMaster();

    Collection budgetList = null;
    if ( budgetMaster.getBudgetType().getCode().equals( BudgetType.PAX_BUDGET_TYPE ) && budgetExtractParametersForm.isAllOwners() )
    {
      associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new BudgetToNodeCharacteristicsAssociationRequest() );
      associationRequestCollection.add( new BudgetToUserCharacteristicsAssociationRequest() );
      associationRequestCollection.add( new BudgetToNodeUsersAssociationRequest() );
      budgetList = getBudgetMasterService().getBudgets( budgetSegmentId, associationRequestCollection );
    }
    else
    {
      budgetList = getBudgetsWithCharacteristics( budgetMaster.getId(), budgetExtractParametersForm, budgetMaster.getBudgetType().getCode() );
    }
    HttpSession session = request.getSession();

    budgetMaster.setBudgetName( ContentReaderManager.getText( budgetMaster.getCmAssetCode(), budgetMaster.getNameCmKey() ) );

    session.setAttribute( "budgetSegment", budgetSegment );
    session.setAttribute( "budgetMaster", budgetMaster );
    session.setAttribute( "displayHeadcountInformation", new Boolean( budgetExtractParametersForm.isHeadcount() ) );
    session.setAttribute( "budgetList",
                          createValueObjectsForBudgets( budgetList,
                                                        budgetMaster.getBudgetType().getCode(),
                                                        convertStringToLong( budgetExtractParametersForm.getAwardsPerParticipant() ),
                                                        budgetExtractParametersForm.isHeadcount(),
                                                        budgetExtractParametersForm.isAtOwnersNodeOnly(),
                                                        budgetExtractParametersForm.getCharacteristicList() ) );

    request.setAttribute( "fileName", generateExtractUniqueFileName() );
    // **********************************
    return actionMapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  private List createValueObjectsForBudgets( Collection budgetList, String budgetType, Long awardsPerPax, boolean headcountSelected, boolean ownersNodeOnly, List characteristicFormBeanList )
  {
    boolean isPax = false;
    List selectedCharacteristicList = getSelectedCharacteristicList( budgetType, characteristicFormBeanList );
    if ( budgetType.equals( BudgetType.PAX_BUDGET_TYPE ) )
    {
      isPax = true;
    }
    List budgetInformation = new ArrayList( budgetList.size() );
    for ( Iterator iter = budgetList.iterator(); iter.hasNext(); )
    {
      Budget budget = (Budget)iter.next();
      // First check the status of the budget, don't extract if it is not active
      if ( !budget.getStatus().getCode().equals( BudgetStatusType.ACTIVE ) )
      {
        continue;
      }
      // Second check if it is the participant and that participant is not active, do not extract
      if ( isPax && !budget.getUser().isActive().booleanValue() )
      {
        continue;
      }
      BudgetInformationValueObject budgetInformationValueObject = new BudgetInformationValueObject();
      budgetInformationValueObject.setBudget( budget );
      // If Headcount is selected we need to get the count of users for the budget
      if ( headcountSelected )
      {
        budgetInformationValueObject.setAwardsPerPax( awardsPerPax );
        List<Long> nodeIdLIst = new ArrayList<Long>();
        if ( isPax )
        {
          if ( budget.getUser() != null )
          {
            nodeIdLIst = getUserService().getAssignedNodesIdList( budget.getUser().getId() );
          }
        }
        else
        {
          nodeIdLIst.add( budget.getNode().getId() );
        }
        // getPaxInNodes 2nd variable asks for includeChildNodes, a direct opposite of
        // ownersNodeOnly, therefore !ownersNodeOnly should be passed here - confusing!
        budgetInformationValueObject.setPaxCount( getParticipantService().getPaxCountBasedOnNodes( nodeIdLIst, !ownersNodeOnly ) );
      }
      else
      {
        budgetInformationValueObject.setAwardsPerPax( new Long( 0 ) );
        budgetInformationValueObject.setPaxCount( new Integer( 0 ) );
      }
      budgetInformationValueObject.setCharacteristics( getSelectedCharacteristicsInBudget( budgetType, budget, selectedCharacteristicList ) );

      budgetInformation.add( budgetInformationValueObject );
      // **********************************
    }
    return budgetInformation;
  }

  /**
   * Get a List of BudgetCharacteristic for the specified budget.  There will be one BudgetCharacteristic
   * for each selectedCharacteristic in the selectedCharacteristicList.  If the selectedCharacteristic
   * is not in the passed budget the value will get a null value.
   * @param budgetType a String containing the budget type (pax or node)
   * @param budget the budget to get characteristics from
   * @param selectedCharacteristicList a List of Characteristics that should be created
   * @return a List of BudgetCharacteristics 
   */
  private List getSelectedCharacteristicsInBudget( String budgetType, Budget budget, List selectedCharacteristicList )
  {
    Set characteristicsInBudgetSet;
    if ( budgetType.equals( BudgetType.PAX_BUDGET_TYPE ) )
    {
      characteristicsInBudgetSet = budget.getUser().getUserCharacteristics();
    }
    else
    {
      characteristicsInBudgetSet = budget.getNode().getActiveNodeCharacteristics();
    }

    List characteristics = new ArrayList( selectedCharacteristicList.size() );
    for ( Iterator cIter = selectedCharacteristicList.iterator(); cIter.hasNext(); )
    {
      Characteristic currentSelectedCharacteristic = (Characteristic)cIter.next();
      BudgetCharacteristic bchar = null;
      for ( Iterator charInBudgetIter = characteristicsInBudgetSet.iterator(); charInBudgetIter.hasNext(); )
      {
        if ( budgetType.equals( BudgetType.PAX_BUDGET_TYPE ) )
        {
          UserCharacteristic characteristic = (UserCharacteristic)charInBudgetIter.next();
          if ( characteristic.getUserCharacteristicType().getId().equals( currentSelectedCharacteristic.getId() ) )
          {
            bchar = createBudgetCharacteristic( characteristic );
            break;
          }
        }
        else
        {
          NodeCharacteristic characteristic = (NodeCharacteristic)charInBudgetIter.next();
          if ( characteristic.getNodeTypeCharacteristicType().getId().equals( currentSelectedCharacteristic.getId() ) )
          {
            bchar = createBudgetCharacteristic( characteristic );
            break;
          }
        }
      }
      if ( bchar == null )
      {
        bchar = new BudgetCharacteristic();
        bchar.setCmAssetCode( currentSelectedCharacteristic.getCmAssetCode() );
        bchar.setCmAssetKey( currentSelectedCharacteristic.getNameCmKey() );
        bchar.setName( currentSelectedCharacteristic.getCmAssetCode() + "." + currentSelectedCharacteristic.getNameCmKey() );
        bchar.setValue( null );
      }
      characteristics.add( bchar );
    }
    return characteristics;
  }

  /**
   * Returns a list of Characteristic objects that the user has selected for display.
   * @param budgetType a String containing the budget type (pax or node)
   * @param characteristicFormBeanList a List containing the characteristic form beans
   * @return a List of Characteristic objects that the user selected for display
   */
  private List getSelectedCharacteristicList( String budgetType, List characteristicFormBeanList )
  {
    List selectedCharacteristicList = new ArrayList();
    for ( Iterator characteristicFormBeanIter = characteristicFormBeanList.iterator(); characteristicFormBeanIter.hasNext(); )
    {
      CharacteristicFormBean characteristicFormBean = (CharacteristicFormBean)characteristicFormBeanIter.next();
      if ( characteristicFormBean.isDisplaySelected() )
      {
        Characteristic aCharacteristic;
        if ( budgetType.equals( BudgetType.PAX_BUDGET_TYPE ) )
        {
          aCharacteristic = getUserCharacteristicService().getCharacteristicById( characteristicFormBean.getCharacteristicId() );
        }
        else
        {
          aCharacteristic = getNodeCharacteristicService().getCharacteristicById( characteristicFormBean.getCharacteristicId() );
        }
        if ( aCharacteristic != null )
        {
          selectedCharacteristicList.add( aCharacteristic );
        }
      }
    }
    return selectedCharacteristicList;
  }

  /**
   * @param characteristic
   * @return BudgetCharacteristic
   */
  public BudgetCharacteristic createBudgetCharacteristic( NodeCharacteristic characteristic )
  {
    BudgetCharacteristic budgetCharacteristic = new BudgetCharacteristic();
    budgetCharacteristic.setCmAssetCode( characteristic.getNodeTypeCharacteristicType().getCmAssetCode() );
    budgetCharacteristic.setCmAssetKey( characteristic.getNodeTypeCharacteristicType().getNameCmKey() );
    budgetCharacteristic.setName( characteristic.getNodeTypeCharacteristicType().getCmAssetCode() + "." + characteristic.getNodeTypeCharacteristicType().getNameCmKey() );
    if ( characteristic.getNodeTypeCharacteristicType().getCharacteristicDataType().getCode().equals( CharacteristicDataType.SINGLE_SELECT )
        || characteristic.getNodeTypeCharacteristicType().getCharacteristicDataType().getCode().equals( CharacteristicDataType.MULTI_SELECT ) )
    {
      List characteristicDisplayValueList = characteristic.getCharacteristicDisplayValueList();
      StringBuffer sb = new StringBuffer();
      int i = 0;
      if ( characteristicDisplayValueList != null )
      {
        for ( Iterator it = characteristic.getCharacteristicDisplayValueList().iterator(); it.hasNext(); )
        {
          i = i + 1;
          DynaPickListType dynaPickListType = (DynaPickListType)it.next();
          if ( dynaPickListType != null )
          {
            sb.append( dynaPickListType.getName() );
            if ( characteristicDisplayValueList.size() != i )
            {
              sb.append( "," );
            }
          }
        }
      }
      budgetCharacteristic.setValue( sb.toString() );
    }
    else
    {
      budgetCharacteristic.setValue( characteristic.getCharacteristicValue() );
    }
    return budgetCharacteristic;
  }

  /**
   * @param characteristic
   * @return BudgetCharacteristic
   */
  public BudgetCharacteristic createBudgetCharacteristic( UserCharacteristic characteristic )
  {
    BudgetCharacteristic budgetCharacteristic = new BudgetCharacteristic();
    budgetCharacteristic.setCmAssetCode( characteristic.getUserCharacteristicType().getCmAssetCode() );
    budgetCharacteristic.setCmAssetKey( characteristic.getUserCharacteristicType().getNameCmKey() );
    budgetCharacteristic.setName( characteristic.getUserCharacteristicType().getCmAssetCode() + "." + characteristic.getUserCharacteristicType().getNameCmKey() );
    budgetCharacteristic.setValue( characteristic.buildCharacteristicDisplayString() );
    return budgetCharacteristic;
  }

  private List getBudgetsWithCharacteristics( Long budgetSegmentId, BudgetExtractParametersForm budgetExtractParametersForm, String budgetType )
  {

    BudgetQueryConstraint budgetQueryConstraint = new BudgetQueryConstraint();
    budgetQueryConstraint.setBudgetSegmentId( budgetSegmentId );
    budgetQueryConstraint.setCharacteristicConstraintLimits( new TreeMap() );
    List characteristicList;
    Map nodeTypeFormBeanMap = null;
    if ( budgetType.equals( BudgetType.PAX_BUDGET_TYPE ) )
    {
      budgetQueryConstraint.setUserBasedConstraints( true );
      characteristicList = getUserCharacteristicService().getAllCharacteristics();
    }
    else
    {
      characteristicList = getNodeCharacteristicService().getAllCharacteristics();
      List nodeTypeIdList = new ArrayList();
      nodeTypeFormBeanMap = new HashMap();
      if ( budgetExtractParametersForm.getNodeList() != null )
      {
        for ( Iterator iter = budgetExtractParametersForm.getNodeList().iterator(); iter.hasNext(); )
        {
          NodeTypeFormBean nodeTypeFormBean = (NodeTypeFormBean)iter.next();
          if ( nodeTypeFormBean != null && nodeTypeFormBean.isSelected() )
          {
            nodeTypeIdList.add( nodeTypeFormBean.getNodeTypeId() );
            NodeType nodeType = new NodeType();
            nodeType = getNodeTypeService().getNodeTypeById( nodeTypeFormBean.getNodeTypeId() );
            nodeTypeFormBean.setNodeType( nodeType );
            nodeTypeFormBeanMap.put( nodeTypeFormBean.getNodeTypeId(), nodeTypeFormBean );
          }
        }
      }
      budgetQueryConstraint.setNodeTypeIds( nodeTypeIdList );
    }
    Map characteristicMap = new TreeMap();
    for ( Iterator iter = characteristicList.iterator(); iter.hasNext(); )
    {
      Characteristic characteristic = (Characteristic)iter.next();
      characteristicMap.put( characteristic.getId(), characteristic );
    }
    List characteristicFormBeanList = budgetExtractParametersForm.getCharacteristicList();
    if ( characteristicFormBeanList != null )
    {
      for ( Iterator iter = characteristicFormBeanList.iterator(); iter.hasNext(); )
      {
        CharacteristicFormBean characteristicFormBean = (CharacteristicFormBean)iter.next();
        if ( characteristicFormBean.isSelected() )
        {
          if ( budgetType.equals( BudgetType.PAX_BUDGET_TYPE )
              || nodeTypeFormBeanMap.get( characteristicFormBean.getNodeTypeId() ) != null && ( (NodeTypeFormBean)nodeTypeFormBeanMap.get( characteristicFormBean.getNodeTypeId() ) ).isSelected()
                  && ! ( (NodeTypeFormBean)nodeTypeFormBeanMap.get( characteristicFormBean.getNodeTypeId() ) ).isAllBudgets() )
          {
            Characteristic characteristic = (Characteristic)characteristicMap.get( characteristicFormBean.getCharacteristicId() );
            if ( characteristic.getCharacteristicDataType().isTextType() )
            {
              budgetQueryConstraint.getCharacteristicConstraintLimits().put( characteristicFormBean.getCharacteristicId(), getTextCharacteristicConstraintLimits( characteristicFormBean ) );
            }
            else if ( characteristic.getCharacteristicDataType().isDecimalType() )
            {
              budgetQueryConstraint.getCharacteristicConstraintLimits().put( characteristicFormBean.getCharacteristicId(), getDecimalCharacteristicConstraintLimits( characteristicFormBean ) );
            }
            else if ( characteristic.getCharacteristicDataType().isIntegerType() )
            {
              budgetQueryConstraint.getCharacteristicConstraintLimits().put( characteristicFormBean.getCharacteristicId(), getIntegerCharacteristicConstraintLimits( characteristicFormBean ) );
            }
            else if ( characteristic.getCharacteristicDataType().isBooleanType() )
            {
              budgetQueryConstraint.getCharacteristicConstraintLimits().put( characteristicFormBean.getCharacteristicId(), getBooleanCharacteristicConstraintLimits( characteristicFormBean ) );
            }
            else if ( characteristic.getCharacteristicDataType().isDateType() )
            {
              budgetQueryConstraint.getCharacteristicConstraintLimits().put( characteristicFormBean.getCharacteristicId(), getDateCharacteristicConstraintLimits( characteristicFormBean ) );
            }
            else if ( characteristic.getCharacteristicDataType().isSingleSelect() )
            {
              budgetQueryConstraint.getCharacteristicConstraintLimits().put( characteristicFormBean.getCharacteristicId(), getSingleSelectCharacteristicConstraintLimits( characteristicFormBean ) );
            }
            else if ( characteristic.getCharacteristicDataType().isMultiSelect() )
            {
              budgetQueryConstraint.getCharacteristicConstraintLimits().put( characteristicFormBean.getCharacteristicId(), getMultiSelectCharacteristicConstraintLimits( characteristicFormBean ) );
            }
          }
        }
      }
    }
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new BudgetToNodeCharacteristicsAssociationRequest() );
    associationRequestCollection.add( new BudgetToUserCharacteristicsAssociationRequest() );
    associationRequestCollection.add( new BudgetToNodeUsersAssociationRequest() );
    return getBudgetMasterService().getBudgetList( budgetQueryConstraint, associationRequestCollection );
  }

  /**
   * Create a CharacteristicConstraintLimits for a text type characteristic.
   * @param characteristicFormBean The CharacteristicFormBean containing the constraint information
   * @return CharacteristicConstraintLimits containing the created constraint
   */
  private CharacteristicConstraintLimits getTextCharacteristicConstraintLimits( CharacteristicFormBean characteristicFormBean )
  {
    TextCharacteristicConstraintLimits characteristicConstraintLimits = new TextCharacteristicConstraintLimits();
    characteristicConstraintLimits.setCharacteristicId( characteristicFormBean.getCharacteristicId() );
    characteristicConstraintLimits.setNodeTypeId( characteristicFormBean.getNodeTypeId() );
    characteristicConstraintLimits.setTextValue( characteristicFormBean.getValue() );
    int optionValue = convertStringToLong( characteristicFormBean.getOptionValue() ).intValue();
    switch ( optionValue )
    {
      case CharacteristicFormBean.CONTAINS:
        characteristicConstraintLimits.setType( TextCharacteristicConstraintLimits.TYPE_CONTAINS );
        break;
      case CharacteristicFormBean.DOES_NOT_CONTAIN:
        characteristicConstraintLimits.setType( TextCharacteristicConstraintLimits.TYPE_DOES_NOT_CONTAIN );
        break;
      case CharacteristicFormBean.STARTS_WITH:
        characteristicConstraintLimits.setType( TextCharacteristicConstraintLimits.TYPE_STARTS_WITH );
        break;
      case CharacteristicFormBean.DOES_NOT_START_WITH:
        characteristicConstraintLimits.setType( TextCharacteristicConstraintLimits.TYPE_DOES_NOT_START_WITH );
        break;
      case CharacteristicFormBean.ENDS_WITH:
        characteristicConstraintLimits.setType( TextCharacteristicConstraintLimits.TYPE_ENDS_WITH );
        break;
      case CharacteristicFormBean.DOES_NOT_END_WITH:
        characteristicConstraintLimits.setType( TextCharacteristicConstraintLimits.TYPE_DOES_NOT_END_WITH );
        break;
      default:
        break;
    }

    return characteristicConstraintLimits;
  }

  /**
   * Create a CharacteristicConstraintLimits for a decimal type characteristic.
   * @param characteristicFormBean The CharacteristicFormBean containing the constraint information
   * @return CharacteristicConstraintLimits containing the created constraint
   */
  private CharacteristicConstraintLimits getDecimalCharacteristicConstraintLimits( CharacteristicFormBean characteristicFormBean )
  {
    DecimalCharacteristicConstraintLimits characteristicConstraintLimits = new DecimalCharacteristicConstraintLimits();
    characteristicConstraintLimits.setCharacteristicId( characteristicFormBean.getCharacteristicId() );
    characteristicConstraintLimits.setNodeTypeId( characteristicFormBean.getNodeTypeId() );
    if ( characteristicFormBean.getLowValue() != null && characteristicFormBean.getLowValue().trim().length() != 0 )
    {
      try
      {
        BigDecimal decimalValue = new BigDecimal( characteristicFormBean.getLowValue() );
        characteristicConstraintLimits.setMinDecimalValue( decimalValue );
      }
      catch( NumberFormatException nfe )
      {
        // Not valid number - should never happen
      }
    }
    if ( characteristicFormBean.getHighValue() != null && characteristicFormBean.getHighValue().trim().length() != 0 )
    {
      try
      {
        BigDecimal decimalValue = new BigDecimal( characteristicFormBean.getHighValue() );
        characteristicConstraintLimits.setMaxDecimalValue( decimalValue );
      }
      catch( NumberFormatException nfe )
      {
        // Not valid number - should never happen
      }
    }
    return characteristicConstraintLimits;
  }

  /**
   * Create a CharacteristicConstraintLimits for a integer type characteristic.
   * @param characteristicFormBean The CharacteristicFormBean containing the constraint information
   * @return CharacteristicConstraintLimits containing the created constraint
   */
  private CharacteristicConstraintLimits getIntegerCharacteristicConstraintLimits( CharacteristicFormBean characteristicFormBean )
  {
    IntegerCharacteristicConstraintLimits characteristicConstraintLimits = new IntegerCharacteristicConstraintLimits();
    characteristicConstraintLimits.setCharacteristicId( characteristicFormBean.getCharacteristicId() );
    characteristicConstraintLimits.setNodeTypeId( characteristicFormBean.getNodeTypeId() );
    if ( characteristicFormBean.getLowValue() != null && characteristicFormBean.getLowValue().trim().length() != 0 )
    {
      try
      {
        BigDecimal decimalValue = new BigDecimal( characteristicFormBean.getLowValue() );
        if ( decimalValue != null )
        {
          characteristicConstraintLimits.setMinIntegerValue( new Integer( decimalValue.intValue() ) );
        }
      }
      catch( NumberFormatException nfe )
      {
        // Not valid number - should never happen
      }
    }
    if ( characteristicFormBean.getHighValue() != null && characteristicFormBean.getHighValue().trim().length() != 0 )
    {
      try
      {
        BigDecimal decimalValue = new BigDecimal( characteristicFormBean.getHighValue() );
        if ( decimalValue != null )
        {
          characteristicConstraintLimits.setMaxIntegerValue( new Integer( decimalValue.intValue() ) );
        }
      }
      catch( NumberFormatException nfe )
      {
        // Not valid number - should never happen
      }
    }
    return characteristicConstraintLimits;
  }

  /**
   * Create a CharacteristicConstraintLimits for a date type characteristic.
   * @param characteristicFormBean The CharacteristicFormBean containing the constraint information
   * @return CharacteristicConstraintLimits containing the created constraint
   */
  private CharacteristicConstraintLimits getDateCharacteristicConstraintLimits( CharacteristicFormBean characteristicFormBean )
  {
    DateCharacteristicConstraintLimits characteristicConstraintLimits = new DateCharacteristicConstraintLimits();
    characteristicConstraintLimits.setCharacteristicId( characteristicFormBean.getCharacteristicId() );
    characteristicConstraintLimits.setNodeTypeId( characteristicFormBean.getNodeTypeId() );
    if ( characteristicFormBean.getLowDate() != null && characteristicFormBean.getLowDate().trim().length() != 0 )
    {
      Date dateValue = DateUtils.toDate( characteristicFormBean.getLowDate() );
      characteristicConstraintLimits.setMinDateValue( dateValue );
    }
    if ( characteristicFormBean.getHighDate() != null && characteristicFormBean.getHighDate().trim().length() != 0 )
    {
      Date dateValue = DateUtils.toDate( characteristicFormBean.getHighDate() );
      characteristicConstraintLimits.setMaxDateValue( dateValue );
    }
    return characteristicConstraintLimits;
  }

  /**
   * Create a CharacteristicConstraintLimits for a boolean type characteristic.
   * @param characteristicFormBean The CharacteristicFormBean containing the constraint information
   * @return CharacteristicConstraintLimits containing the created constraint
   */
  private CharacteristicConstraintLimits getBooleanCharacteristicConstraintLimits( CharacteristicFormBean characteristicFormBean )
  {
    BooleanCharacteristicConstraintLimits characteristicConstraintLimits = new BooleanCharacteristicConstraintLimits();
    characteristicConstraintLimits.setCharacteristicId( characteristicFormBean.getCharacteristicId() );
    characteristicConstraintLimits.setNodeTypeId( characteristicFormBean.getNodeTypeId() );
    characteristicConstraintLimits.setBooleanValue( new Boolean( characteristicFormBean.isBooleanSet() ) );
    return characteristicConstraintLimits;
  }

  /**
   * Create a CharacteristicConstraintLimits for a singleSelect type characteristic.
   * @param characteristicFormBean The CharacteristicFormBean containing the constraint information
   * @return CharacteristicConstraintLimits containing the created constraint
   */
  private CharacteristicConstraintLimits getSingleSelectCharacteristicConstraintLimits( CharacteristicFormBean characteristicFormBean )
  {
    SingleSelectCharacteristicConstraintLimits characteristicConstraintLimits = new SingleSelectCharacteristicConstraintLimits();
    characteristicConstraintLimits.setCharacteristicId( characteristicFormBean.getCharacteristicId() );
    characteristicConstraintLimits.setNodeTypeId( characteristicFormBean.getNodeTypeId() );
    fillValuesAllowed( characteristicConstraintLimits, characteristicFormBean );
    return characteristicConstraintLimits;
  }

  /**
   * Create a CharacteristicConstraintLimits for a multiSelect type characteristic.
   * @param characteristicFormBean The CharacteristicFormBean containing the constraint information
   * @return CharacteristicConstraintLimits containing the created constraint
   */
  private CharacteristicConstraintLimits getMultiSelectCharacteristicConstraintLimits( CharacteristicFormBean characteristicFormBean )
  {
    MultiSelectCharacteristicConstraintLimits characteristicConstraintLimits = new MultiSelectCharacteristicConstraintLimits();
    characteristicConstraintLimits.setCharacteristicId( characteristicFormBean.getCharacteristicId() );
    characteristicConstraintLimits.setNodeTypeId( characteristicFormBean.getNodeTypeId() );
    fillValuesAllowed( characteristicConstraintLimits, characteristicFormBean );
    int optionValue = convertStringToLong( characteristicFormBean.getOptionValue() ).intValue();
    characteristicConstraintLimits.setAllof( optionValue == CharacteristicFormBean.IS_ALL_OF );
    return characteristicConstraintLimits;
  }

  /**
   * Fill the values allowed on the passed SingleSelectCharacteristicConstraintLimits using data 
   * from the characteristicFormBean
   * @param characteristicConstraintLimits The SingleSelectCharacteristicConstraintLimits to fill
   * @param characteristicFormBean
   */
  private void fillValuesAllowed( SingleSelectCharacteristicConstraintLimits characteristicConstraintLimits, CharacteristicFormBean characteristicFormBean )
  {
    ArrayList values = new ArrayList();
    List checkedItems = characteristicFormBean.getCheckList();
    if ( checkedItems != null )
    {
      for ( int i = 0; i < checkedItems.size(); i++ )
      {
        CheckItem checkItem = (CheckItem)checkedItems.get( i );
        if ( checkItem.getChecked() )
        {
          values.add( characteristicFormBean.getPossibleValue( i ) );
        }
      }
    }
    characteristicConstraintLimits.setValuesAllowed( ArrayUtil.convertListToStringArray( values ) );
  }

  /**
   * Converts a String into a Long equivalent
   * 
   * @param s
   * @return Long value of the input string, 0 if any errors
   */
  private Long convertStringToLong( String s )
  {
    Long l = new Long( 0 );
    try
    {
      l = new Long( s );
    }
    catch( NumberFormatException e )
    {
      l = new Long( 0 );
    }
    return l;
  } // end convertStringToLong

  /**
   * Constructs and returns a list of budgets.  
   * 
   * The first row returned is the header information
   *    for the email extract while subsequent rows are budget extract information
   *    
   * @param budgetList
   * @return the budget extract information
   */
  public List generateEmailExtract( Collection budgetList )
  {
    ArrayList results = new ArrayList();
    boolean firstRow = true;
    for ( Iterator budgetObjIter = budgetList.iterator(); budgetObjIter.hasNext(); )
    {
      BudgetInformationValueObject budgetInfo = (BudgetInformationValueObject)budgetObjIter.next();

      String detailRow = "";
      if ( firstRow )
      {
        results.add( getHeaderInformationForExtract( budgetInfo ) );
        firstRow = false;
      }

      Budget budget = budgetInfo.getBudget();
      BudgetSegment budgetSegment = budget.getBudgetSegment();
      BudgetMaster budgetMaster = budgetSegment.getBudgetMaster();

      if ( budgetMaster.getBudgetType().getCode().equals( BudgetType.PAX_BUDGET_TYPE ) )
      {
        detailRow = QUOTE + nullCheck( budgetInfo.getBudget().getUser().getUserName() ) + QUOTE_COMMA_QUOTE;
      }
      else
      {
        detailRow = QUOTE + nullCheck( budgetInfo.getBudget().getNode().getName() ) + QUOTE_COMMA_QUOTE;
      }

      detailRow += nullCheck( budgetInfo.getAmount() ) + QUOTE_COMMA_QUOTE;

      if ( budgetMaster.getBudgetType().getCode().equals( BudgetType.PAX_BUDGET_TYPE ) )
      {
        detailRow += nullCheck( budgetInfo.getBudget().getBudgetOwner().getNameLFMWithComma() ) + QUOTE_COMMA_QUOTE;
      }
      else
      {
        detailRow += nullCheck( ContentReaderManager.getText( budgetInfo.getBudget().getNode().getNodeType().getCmAssetCode(), "NODE_TYPE_NAME" ) ) + QUOTE_COMMA_QUOTE;
      }

      detailRow += nullCheck( budgetInfo.getPaxCount() ) + QUOTE_COMMA_QUOTE;
      detailRow += nullCheck( budgetInfo.getAwardsPerPax() ) + QUOTE_COMMA_QUOTE;
      detailRow += budgetInfo.getBudget().getOriginalValue() + QUOTE_COMMA_QUOTE;
      detailRow += budgetInfo.getBudget().getCurrentValue() + QUOTE_COMMA_QUOTE;

      // characteristics
      for ( Iterator charIter = budgetInfo.getCharacteristics().iterator(); charIter.hasNext(); )
      {
        BudgetCharacteristic budgetChar = (BudgetCharacteristic)charIter.next();
        detailRow += nullCheck( budgetChar.getValue() ) + QUOTE_COMMA_QUOTE;
      }

      if ( budgetMaster.getBudgetType().getCode().equals( BudgetType.NODE_BUDGET_TYPE ) )
      {
        if ( budgetInfo.getBudget().getNode().getNodeOwner() != null )
        {
          detailRow += nullCheck( budgetInfo.getBudget().getNode().getNodeOwner().getNameLFMWithComma() ) + QUOTE_COMMA_QUOTE;
        }
        else
        {
          detailRow += QUOTE_COMMA_QUOTE;
        }
      }
      detailRow += nullCheck( budgetMaster.getBudgetName() ) + QUOTE_COMMA_QUOTE;
      detailRow += nullCheck( budgetSegment.getDisplaySegmentName() ) + QUOTE;
      results.add( detailRow );
    }

    return results;
  }

  /**
   * Returns empty string if value is null
   * 
   * @param param
   * @return
   */
  private String nullCheck( String param )
  {
    if ( param == null )
    {
      return new String( "" );
    }
    return param;
  }

  /**
   * Returns empty string if value is null
   * 
   * @param param
   * @return
   */
  private String nullCheck( Integer param )
  {
    if ( param == null )
    {
      return new String( "" );
    }
    return param.toString();
  }

  /**
   * Returns empty string if value is null
   * 
   * @param param
   * @return
   */
  private String nullCheck( Long param )
  {
    if ( param == null )
    {
      return new String( "" );
    }
    return param.toString();
  }

  /**
   * Constructs the Header of the budget email extract
   * 
   * @param budgetInfo
   * @return String
   */

  public String getHeaderInformationForExtract( BudgetInformationValueObject budgetInfo )
  {
    // Create header string for the CSV or Excel file to be extracted.
    String header = "";
    Budget budget = budgetInfo.getBudget();
    BudgetMaster budgetMaster = budget.getBudgetSegment().getBudgetMaster();

    if ( budgetMaster.getBudgetType().getCode().equals( BudgetType.PAX_BUDGET_TYPE ) )
    {
      header = ContentReaderManager.getText( "user.profile", "USER_NAME_LABEL" ) + ",";
    }
    else
    {
      header = ContentReaderManager.getText( "node.list", "NAME" ) + ",";
    }
    header += ContentReaderManager.getText( "admin.budget.details", "AMOUNT" ) + ",";
    if ( budgetMaster.getBudgetType().getCode().equals( BudgetType.PAX_BUDGET_TYPE ) )
    {
      header += ContentReaderManager.getText( "admin.budgetmaster.details", "OWNER" ) + ",";
    }
    else
    {
      header += ContentReaderManager.getText( "node.list", "NODE_TYPE" ) + ",";
    }
    header += ContentReaderManager.getText( "admin.budget.details", "PAX_COUNT" ) + ",";
    header += ContentReaderManager.getText( "admin.budget.details", "PAX_AWARDS" ) + ",";
    header += ContentReaderManager.getText( "admin.budgetmaster.details", "ORIGINAL" ) + ",";
    header += ContentReaderManager.getText( "admin.budgetmaster.details", "CURRENT" ) + ",";

    // characteristics
    for ( Iterator charIter = budgetInfo.getCharacteristics().iterator(); charIter.hasNext(); )
    {
      BudgetCharacteristic budgetChar = (BudgetCharacteristic)charIter.next();
      header += ContentReaderManager.getText( budgetChar.getCmAssetCode(), budgetChar.getCmAssetKey() ) + ",";
    }

    if ( budgetMaster.getBudgetType().getCode().equals( BudgetType.NODE_BUDGET_TYPE ) )
    {
      header += ContentReaderManager.getText( "node.view", "OWNER" ) + ",";
    }
    header += ContentReaderManager.getText( "admin.budgetmaster.details", "NAME" ) + ",";
    header += ContentReaderManager.getText( "admin.budgetmaster.details", "BUDGET_SEGMENT_DISPLAY_NAME" );

    return header;
  }

  /** 
   * Constructs and saves the file to the current directory on the app server.
   * 
   * @return the file saved
   */
  private File writeFile( List results )
  {
    String fileName = "";
    String failureMsg = "";
    File extractFile = null;
    String extractLocation = getExtractLocation();
    try
    {
      // Create an unique filename for the extract file.
      fileName = generateUniqueFileName();

      FileExtractUtils.createDirIfNeeded( extractLocation );

      // Writes a file with the resultset from stored proc.
      extractFile = new File( extractLocation, fileName ); // 28946

      // Build failure message just in case.
      failureMsg = new String( "An exception occurred while attempting to write file for " + " Report: Budget Extract" + " to File:" + extractFile.getAbsolutePath() );

      boolean success = extractFile.createNewFile();
      if ( success )
      {
        BufferedWriter writer = new BufferedWriter( new FileWriter( extractFile ) );

        for ( int i = 0; i < results.size(); i++ )
        {
          String row = (String)results.get( i );
          writer.write( row );
          writer.newLine();
        }
        writer.close();

        String msg = new String( "Executing the " + " Report: Budget Extract" + " File:" + extractFile.getAbsolutePath() + " has been saved successfully." + " File size:"
            + printFileSize( extractFile.length() ) );
        log.info( msg );
      }
      else
      {
        log.error( failureMsg );
      }
    }
    catch( IOException e )
    {
      log.error( failureMsg, e );
    }
    catch( Exception e )
    {
      log.error( failureMsg, e );
    }
    return extractFile;
  }

  /**
   * Creates a .csv file name that is unique to: - the client name - the report requested - the current datetime.
   * 
   * @return an unique file name
   */
  private String generateUniqueFileName()
  {
    StringBuffer fileName = new StringBuffer();
    fileName.append( getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    fileName.append( "_" );
    fileName.append( "Budget_Extract" );
    fileName.append( "_" );
    fileName.append( DateUtils.getCurrentDate().getTime() );
    fileName.append( ".csv" );
    return fileName.toString();
  }

  private String generateExtractUniqueFileName()
  {
    StringBuffer fileName = new StringBuffer();
    fileName.append( getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    fileName.append( "_" );
    fileName.append( "Budget_Extract" );
    fileName.append( "_" );
    fileName.append( DateUtils.getCurrentDate().getTime() );
    return fileName.toString();
  }

  /**
   * This method returns user defined path where the csv file should be saved. i.e. /tmp/ on Unix.
   * It makes sure the file separator (On Windows this is \  On Unix this is /) works with the
   * current operating system
   * 
   * @return a system variable driven path where the extract will be saved
   */
  public String getExtractLocation()
  {
    String extractLocation = null;

    // On Windows this is \ On Unix this is /
    String currentSystemFileSeparator = File.separator;

    // user defined path where the csv file should be saved. i.e. /tmp/ on Unix
    extractLocation = System.getProperty( "appdatadir" ); // 28946
    // make sure the user defined directory works with the current system
    if ( !StringUtils.isBlank( extractLocation ) )
    {
      // e.g. Developers running on localhosts on Windows
      // but the system variable specifies an Unix file separator
      if ( extractLocation.indexOf( UnixFileSeparator ) >= 0 && currentSystemFileSeparator.equals( WindowsFileSeparator ) )
      {
        extractLocation = extractLocation.replace( '/', '\\' );
      }
      // e.g. QA, PPRD, PROD - CTECH environments running on Unix
      // but the system variable specifies a Windows file separator
      if ( extractLocation.indexOf( WindowsFileSeparator ) >= 0 && currentSystemFileSeparator.equals( UnixFileSeparator ) )
      {
        extractLocation = extractLocation.replace( '\\', '/' );
      }
    }

    return extractLocation;
  }

  /**
   * Returns a formatted string displaying the size in words such as KB/MB/GB/more
   * 
   * @param lengthInBytes
   * @return a string representing the length of the file.
   */
  protected String printFileSize( long lengthInBytes )
  {
    float lengthInFloat = (float)lengthInBytes;
    String fileSize = "";
    final int kilobyte = 1024;
    final int megabyte = 1024 * kilobyte;
    final int gigabyte = 1024 * megabyte;

    if ( lengthInFloat < kilobyte )
    {
      fileSize = lengthInFloat + " Bytes";
    }
    else if ( lengthInFloat < megabyte )
    {
      fileSize = "About " + new Integer( Math.round( lengthInFloat / kilobyte ) ).toString() + " KB";
    }
    else if ( lengthInFloat < gigabyte )
    {
      fileSize = "About " + new Integer( Math.round( lengthInFloat / megabyte ) ).toString() + " MB";
    }
    else if ( lengthInFloat < gigabyte * 1024 )
    {
      fileSize = "About " + new Integer( Math.round( lengthInFloat / gigabyte ) ).toString() + " GB";
    }
    else
    {
      fileSize = "File Too Large - in Terabyte! File size in bytes:" + fileSize;
    }

    return fileSize;
  }

  /**
   * Sends an e-mail message to the user that launched this process with the file attachment of the
   * dataextract
   */
  private void sendMessage( String fullFileName, String attachmentFileName )
  {
    // Set up mailing-level personalization data.
    User runByUser = getUserService().getUserById( UserManager.getUserId() );
    Map objectMap = new HashMap();
    objectMap.put( "firstName", runByUser.getFirstName() );
    objectMap.put( "lastName", runByUser.getLastName() );
    objectMap.put( "reportName", "Budget Extract" );
    // objectMap.put( "reportParms", getReportParms() ); //removed per BA review with Dolan and
    // Chrissy
    objectMap.put( "contactUsUrl",
                   getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal()
                       + getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ).getStringVal() );

    // Compose the e-mail message.
    Mailing mailing = composeMail( MessageService.REPORT_EXTRACT_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    mailing.addMailingRecipient( addRecipient( runByUser ) );

    // Is there a file to attach?
    if ( fullFileName == null )
    {
      objectMap.put( "noDataFound", "true" );
    }
    else
    {
      // Attach the file to the e-mail.
      mailing.addMailingAttachmentInfo( addMailingAttachmentInfo( mailing, fullFileName, attachmentFileName ) );
    }

    // Send the e-mail message.
    try
    {
      mailing = getMailingService().submitMailingWithoutScheduling( mailing, objectMap );

      // process mailing
      getMailingService().processMailing( mailing.getId() );

      String msg = new String( "Process Budget Extract" + " Report: Budget Extract" + " sent an email message to " + runByUser.getFirstName() + " " + runByUser.getLastName() + "." + " (mailing ID = "
          + mailing.getId() + ")" );

      log.info( msg );
    }
    catch( Exception e )
    {
      String msg = new String( "An exception occurred while sending a " + " Report: Budget Extract" + " (mailing ID = " + mailing.getId() + ")" );
      log.error( msg, e );
    }
  }

  /**
   * Determine if the file exists in the file system
   * 
   * @param fileName
   * @return true if the file exists on the file system
   */
  protected boolean doesFileExists( String fileName )
  {
    if ( !fileName.equals( "" ) )
    {
      File fileOrDir = new File( fileName );

      if ( fileOrDir.exists() && fileOrDir.isFile() )
      {
        // File exists
        return true;
      }
    }
    // File does not exist or is not a File
    return false;
  }

  /**
   * @param mailing
   * @param fullFileName - this is the absolute path of the file (internal to the system incl. directory names)
   * @param attachmentFileName - this is the name used on the file attachment in the email (visible to users)
   * @return MailingAttachmentInfo
   */
  protected MailingAttachmentInfo addMailingAttachmentInfo( Mailing mailing, String fullFileName, String attachmentFileName )
  {
    MailingAttachmentInfo mailingAttachmentInfo = new MailingAttachmentInfo();
    mailingAttachmentInfo.setFullFileName( fullFileName );
    mailingAttachmentInfo.setAttachmentFileName( attachmentFileName );
    mailingAttachmentInfo.setMailing( mailing );
    return mailingAttachmentInfo;
  }

  /**
   * Takes in a user and returns a mailing recipient object suitable for mailing service
   * 
   * @param recipient
   * @return a mailingRecipient object
   */
  protected MailingRecipient addRecipient( User recipient )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    String localeCode = getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
    if ( recipient.getLanguageType() != null )
    {
      localeCode = recipient.getLanguageType().getCode();
    }
    mailingRecipient.setLocale( localeCode );
    mailingRecipient.setUser( recipient );

    return mailingRecipient;
  }

  /**
   * Adds the message by name and mailing type to a mailing object
   * 
   * @param cmAssetCode
   * @param mailingType
   * @return a mailing object that is mostly assembled, except for the mailingRecipient(s)
   */
  protected Mailing composeMail( String cmAssetCode, String mailingType )
  {
    Mailing mailing = composeMail();

    mailing.setMailingType( MailingType.lookup( mailingType ) );

    Message message = getMessageService().getMessageByCMAssetCode( cmAssetCode );
    mailing.setMessage( message );

    return mailing;
  }

  /**
   * Creates a new mailing object and add Guid, Sender and Delivery Date to it
   * 
   * @return a partially assembled mailing object
   */
  private Mailing composeMail()
  {
    Mailing mailing = new Mailing();

    // Needs Guid due to lack of a business key
    mailing.setGuid( GuidUtils.generateGuid() );

    // Sender
    String sender = getSystemVariableService().getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal();
    mailing.setSender( sender );

    // Delivery Date - Assumes Now (i.e. immediate delivery)
    Timestamp deliveryDate = new Timestamp( com.biperf.core.utils.DateUtils.getCurrentDate().getTime() );
    mailing.setDeliveryDate( deliveryDate );

    return mailing;

  }

  /**
   * Retrieves a Budget Master Service
   * 
   * @return BudgetMasterService
   */
  private BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)getService( BudgetMasterService.BEAN_NAME );
  } // end getBudgetMasterService

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
   * Gets a SystemVariableService
   * 
   * @return SystemVariableService
   */
  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  /**
   * Gets a MessageService
   * 
   * @return MessageService
   */
  private MessageService getMessageService()
  {
    return (MessageService)getService( MessageService.BEAN_NAME );
  }

  /**
   * Gets a MailingService
   * 
   * @return MailingService
   */
  private MailingService getMailingService()
  {
    return (MailingService)getService( MailingService.BEAN_NAME );
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
   * Gets a ParticipantService
   * 
   * @return ParticipantService
   */
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
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

  /**
   * Gets a UserService
   * 
   * @return UserService
   */
  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

} // end class BudgetExtractAction
