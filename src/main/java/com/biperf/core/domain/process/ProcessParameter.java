/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.process;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.enums.DynaPickListFactory;
import com.biperf.core.domain.enums.ProcessParameterDataType;
import com.biperf.core.domain.enums.ProcessParameterInputFormatType;
import com.biperf.core.domain.enums.ProcessParameterSourceType;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.value.FormattedValueBean;

/**
 * .
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
 * <td>Nov 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProcessParameter implements Serializable
{

  private String name;
  private String description;
  private String processParameterDataTypeCode;
  private String processParameterInputFormatTypeCode;
  private String processParameterSourceTypeCode;
  private String sourceName;
  private boolean secret;

  /**
   * @return value of name property
   */
  public String getName()
  {
    return name;
  }

  /**
   * @param name value for name property
   */
  public void setName( String name )
  {
    this.name = name;
  }

  /**
   * @return value of processParameterDataType property
   */
  public ProcessParameterDataType getProcessParameterDataType()
  {
    return ProcessParameterDataType.lookup( processParameterDataTypeCode );
  }

  /**
   * @return value of processParameterInputFormatType property
   */
  public ProcessParameterInputFormatType getProcessParameterInputFormatType()
  {
    return ProcessParameterInputFormatType.lookup( processParameterInputFormatTypeCode );
  }

  /**
   * @return value of processParameterSourceTypeCode property
   */
  public ProcessParameterSourceType getProcessParameterSourceType()
  {
    return ProcessParameterSourceType.lookup( processParameterSourceTypeCode );
  }

  /**
   * @return value of processParameterSourceTypeCode property
   */
  public String getProcessParameterSourceTypeCode()
  {
    return processParameterSourceTypeCode;
  }

  /**
   * @param processParameterSourceTypeCode value for processParameterSourceTypeCode property
   */
  public void setProcessParameterSourceTypeCode( String processParameterSourceTypeCode )
  {
    this.processParameterSourceTypeCode = processParameterSourceTypeCode;
  }

  /**
   * @param processParameterDataTypeCode value for processParameterDataTypeCode property
   */
  public void setProcessParameterDataTypeCode( String processParameterDataTypeCode )
  {
    this.processParameterDataTypeCode = processParameterDataTypeCode;
  }

  /**
   * @param processParameterInputFormatTypeCode value for processParameterInputFormatTypeCode
   *          property
   */
  public void setProcessParameterInputFormatTypeCode( String processParameterInputFormatTypeCode )
  {
    this.processParameterInputFormatTypeCode = processParameterInputFormatTypeCode;
  }

  /**
   * @return value of sourceName property
   */
  public String getSourceName()
  {
    return sourceName;
  }

  /**
   * @param sourceName value for sourceName property
   */
  public void setSourceName( String sourceName )
  {
    this.sourceName = sourceName;
  }

  /**
   * @return value of processParameterDataTypeCode property
   */
  public String getProcessParameterDataTypeCode()
  {
    return processParameterDataTypeCode;
  }

  /**
   * @return value of processParameterInputFormatTypeCode property
   */
  public String getProcessParameterInputFormatTypeCode()
  {
    return processParameterInputFormatTypeCode;
  }

  /**
   * @return value of description property
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * @param description value for description property
   */
  public void setDescription( String description )
  {
    this.description = description;
  }

  public boolean isSecret()
  {
    return secret;
  }

  public void setSecret( boolean secret )
  {
    this.secret = secret;
  }

  public List getSourceValueChoices()
  {
    if ( processParameterSourceTypeCode == null )
    {
      throw new BeaconRuntimeException( "Invalid process Parameter definition. processParameterSourceTypeCode must not be null"
          + " if processParameterInputFormatTypeCode is drop_down or check_boxes" );
    }
    if ( processParameterSourceTypeCode.equals( ProcessParameterSourceType.PICKLIST_ASSET_NAME ) )
    {
      return DynaPickListFactory.getPickList( sourceName );
    }
    else if ( processParameterSourceTypeCode.equals( ProcessParameterSourceType.NAMED_QUERY_NAME ) )
    {
      ProcessService processService = (ProcessService)BeanLocator.getBean( ProcessService.BEAN_NAME );
      return processService.getProcessParameterValueChoices( sourceName );
    }
    else
    {
      throw new BeaconRuntimeException( "Unknown processParameterSourceTypeCode: " + processParameterSourceTypeCode );
    }
  }

  public String getFormattedChoice( String sourceValueChoiceName )
  {
    String formattedChoice;

    // Could have null process parameter value
    if ( sourceValueChoiceName == null || sourceValueChoiceName.equals( "" ) )
    {
      formattedChoice = "undefined"; // so that the null process param is displayed in the process
                                     // log jsp
    }
    else if ( processParameterInputFormatTypeCode.equals( ProcessParameterInputFormatType.DROP_DOWN ) || processParameterInputFormatTypeCode.equals( ProcessParameterInputFormatType.CHECK_BOXES ) )
    {
      // Format the choice (change picklist item code to name and db source id to value)
      if ( processParameterSourceTypeCode == null )
      {
        throw new BeaconRuntimeException( "Invalid process Parameter definition. processParameterSourceTypeCode must not be null"
            + " if processParameterInputFormatTypeCode is drop_down or check_boxes" );
      }

      if ( processParameterSourceTypeCode.equals( ProcessParameterSourceType.PICKLIST_ASSET_NAME ) )
      {
        if ( sourceName.equals( "default.locale" ) )
        {
          formattedChoice = DynaPickListFactory.getPickListItem( sourceName, sourceValueChoiceName ).getName();
        }
        else
        {
          formattedChoice = DynaPickListFactory.getPickListItem( sourceName, sourceValueChoiceName.toLowerCase() ).getName();
        }
      }
      else if ( processParameterSourceTypeCode.equals( ProcessParameterSourceType.NAMED_QUERY_NAME ) )
      {
        formattedChoice = FormattedValueBean.getValueFromCollection( getSourceValueChoices(), new Long( sourceValueChoiceName ) );
      }
      else
      {
        throw new BeaconRuntimeException( "Unknown processParameterSourceTypeCode: " + processParameterSourceTypeCode );
      }
    }
    else
    {
      // Non-formatted inputFormat, just return the raw value unless secret
      if ( !secret )
      {
        formattedChoice = sourceValueChoiceName;
      }
      else
      {
        if ( sourceValueChoiceName != null )
        {
          formattedChoice = StringUtils.repeat( "*", sourceValueChoiceName.length() );
        }
        else
        {
          formattedChoice = "*******";
        }
      }
    }

    return formattedChoice;
  }
}
