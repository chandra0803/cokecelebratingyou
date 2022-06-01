/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/budget/BudgetExtractParametersForm.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorActionForm;

import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.enums.BudgetMasterAwardType;
import com.biperf.core.utils.DateUtils;
import com.biperf.util.StringUtils;

/**
 * BudgetExtractParametersForm.
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
public class BudgetExtractParametersForm extends ValidatorActionForm
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  public static final String FORM_NAME = "budgetExtractParametersForm";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  Long id;
  private String budgetSegmentId;
  /*
   * The array of possible characteristics
   */
  private List characteristicList;
  private boolean allOwners;
  private List nodeList;
  private boolean headcount;
  private String ownersnode;
  private String awardsPerParticipant;
  private boolean atOwnersNodeOnly;
  private String submit;

  public String getBudgetSegmentId()
  {
    return budgetSegmentId;
  }

  public void setBudgetSegmentId( String budgetSegmentId )
  {
    this.budgetSegmentId = budgetSegmentId;
  }

  // ---------------------------------------------------------------------------
  // Load and To Domain Methods
  // ---------------------------------------------------------------------------

  public void loadDomainObject( BudgetMaster budgetMaster )
  {
    setId( budgetMaster.getId() );
    // todo jjd add flag to disable editing for non-default locales
  }

  public BudgetMaster toDomainObject()
  {
    BudgetMaster budgetMaster = new BudgetMaster();
    populateDomainObject( budgetMaster );
    return budgetMaster;
  }

  public void populateDomainObject( BudgetMaster budgetMaster )
  {
    budgetMaster.setId( id );
    budgetMaster.setAwardType( BudgetMasterAwardType.lookup( BudgetMasterAwardType.POINTS ) );
  }

  // ---------------------------------------------------------------------------
  // Validation Methods
  // ---------------------------------------------------------------------------

  /**
   * Validates the data in this form.
   *
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   * @return <code>ActionErrors</code> object that encapsulates any validation errors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( mapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( headcount )
    {
      if ( StringUtils.isEmpty( this.ownersnode ) )
      {
        actionErrors.add( "atOwnersNodeOnly", new ActionMessage( "admin.budgetextract.parameters.NO_HEADCOUNT_SELECTED" ) );
      }
    }

    int numCharacteristics = 0;
    if ( characteristicList != null )
    {
      for ( Iterator iter = characteristicList.iterator(); iter.hasNext(); )
      {
        CharacteristicFormBean characteristicFormBean = (CharacteristicFormBean)iter.next();
        if ( characteristicFormBean.isDisplaySelected() )
        {
          numCharacteristics++;
        }
        if ( !StringUtils.isEmpty( characteristicFormBean.getLowValue() ) && !StringUtils.isEmpty( characteristicFormBean.getHighValue() ) )
        {
          try
          {
            BigDecimal low = new BigDecimal( characteristicFormBean.getLowValue() );
            BigDecimal high = new BigDecimal( characteristicFormBean.getHighValue() );
            if ( low.compareTo( high ) > 0 )
            {
              actionErrors.add( "characteristicList", new ActionMessage( "admin.budgetextract.parameters.LOW_NUMBER_GT_HIGH_NUMBER_ERROR" ) );
            }
          }
          catch( NumberFormatException nfe )
          {
            // Do nothing - these have already been validated by validator
          }
        }
        if ( !StringUtils.isEmpty( characteristicFormBean.getLowDate() ) && !StringUtils.isEmpty( characteristicFormBean.getHighDate() ) )
        {
          Date low = DateUtils.toDate( characteristicFormBean.getLowDate() );
          Date high = DateUtils.toDate( characteristicFormBean.getHighDate() );
          if ( low.after( high ) )
          {
            actionErrors.add( "characteristicList", new ActionMessage( "admin.budgetextract.parameters.LOW_DATE_GT_HIGH_DATE_ERROR" ) );
          }
        }
      }
      if ( numCharacteristics > 5 )
      {
        actionErrors.add( "characteristicList", new ActionMessage( "admin.budgetextract.parameters.TOO_MANY_CHARACTERISTICS" ) );

      }

    }
    return actionErrors;
  }

  public void reset( ActionMapping map, HttpServletRequest req )
  {
    characteristicList = new ArrayList();
    nodeList = new ArrayList();
    setAllOwners( true );
    setAtOwnersNodeOnly( true );
    setHeadcount( false );
  }

  public List getCharacteristicList()
  {
    return characteristicList;
  }

  public void setCharacteristicList( List characteristicList )
  {
    this.characteristicList = characteristicList;
  }

  public CharacteristicFormBean getCharacteristic( int index )
  {
    while ( characteristicList.size() <= index )
    {
      characteristicList.add( new CharacteristicFormBean() );
    }
    return (CharacteristicFormBean)characteristicList.get( index );
  }

  public boolean isAllOwners()
  {
    return allOwners;
  }

  public void setAllOwners( boolean allOwners )
  {
    this.allOwners = allOwners;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public List getNodeList()
  {
    return nodeList;
  }

  public void setNodeList( List nodeList )
  {
    this.nodeList = nodeList;
  }

  public NodeTypeFormBean getNodeType( int index )
  {
    while ( nodeList.size() <= index )
    {
      nodeList.add( new NodeTypeFormBean() );
    }
    return (NodeTypeFormBean)nodeList.get( index );
  }

  public boolean isAtOwnersNodeOnly()
  {
    return atOwnersNodeOnly;
  }

  public void setAtOwnersNodeOnly( boolean atOwnersNodeOnly )
  {
    this.atOwnersNodeOnly = atOwnersNodeOnly;
  }

  public String getAwardsPerParticipant()
  {
    return awardsPerParticipant;
  }

  public void setAwardsPerParticipant( String awardsPerParticipant )
  {
    this.awardsPerParticipant = awardsPerParticipant;
  }

  public boolean isHeadcount()
  {
    return headcount;
  }

  public void setHeadcount( boolean headcount )
  {
    this.headcount = headcount;
  }

  public String getOwnersnode()
  {
    return ownersnode;
  }

  public void setOwnersnode( String ownersnode )
  {
    this.ownersnode = ownersnode;

    if ( ownersnode.trim().length() != 0 && ownersnode.equals( "true" ) )
    {
      this.setAtOwnersNodeOnly( true );
    }

    if ( ownersnode.trim().length() != 0 && ownersnode.equals( "false" ) )
    {
      this.setAtOwnersNodeOnly( false );
    }

  }

  public String getSubmit()
  {
    return submit;
  }

  public void setSubmit( String submit )
  {
    this.submit = submit;
  }
}
