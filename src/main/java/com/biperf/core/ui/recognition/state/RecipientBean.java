
package com.biperf.core.ui.recognition.state;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.utils.BudgetUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RecipientBean implements Serializable
{
  private static final Log log = LogFactory.getLog( RecipientBean.class );

  private Long userId;
  private Long awardQuantity;
  private Long awardLevel;
  private Long awardLevelId;
  private Long awardLevelValue;
  private String awardLevelName;
  private String countryCode;
  private double countryRatio;
  private String countryName;
  private Long nodeId;
  private String firstName;
  private String lastName;
  private String emailAddr;
  private String nodes;
  private String jobName;
  private String departmentName;
  private Map<Integer, CalculatorResult> calculatorResults = new ConcurrentHashMap<Integer, CalculatorResult>();
  public boolean optOutAwards;
  public String avatarUrl;
  /* Client customizations for WIP #42701 starts */
  private String currency;
  private Long awardMin;
  private Long awardMax;
  /* Client customizations for WIP #42701 ends */
  /* Client customizations for WIP #26532 starts */
  private Boolean purlAllowOutsideDomains;
  /* Client customizations for WIP #26532 ends */
  public Long getAwardLevel()
  {
    return awardLevel;
  }

  public void setAwardLevel( Long awardLevel )
  {
    this.awardLevel = awardLevel;
  }

  public Long getAwardLevelId()
  {
    return awardLevelId;
  }

  public void setAwardLevelId( Long awardLevelId )
  {
    this.awardLevelId = awardLevelId;
  }

  public Long getAwardLevelValue()
  {
    return awardLevelValue;
  }

  public void setAwardLevelValue( Long awardLevelValue )
  {
    this.awardLevelValue = awardLevelValue;
  }

  public String getAwardLevelName()
  {
    return awardLevelName;
  }

  public void setAwardLevelName( String awardLevelName )
  {
    this.awardLevelName = awardLevelName;
  }

  public Long getAwardQuantity()
  {
    return awardQuantity;
  }

  public void setAwardQuantity( Long awardQuantity )
  {
    this.awardQuantity = awardQuantity;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public double getCountryRatio()
  {
    return countryRatio;
  }

  public void setCountryRatio( double countryRatio )
  {
    this.countryRatio = countryRatio;
  }

  public String getCountryName()
  {
    return countryName;
  }

  public void setCountryName( String countryName )
  {
    this.countryName = countryName;
  }

  public String getEmailAddr()
  {
    return emailAddr;
  }

  public void setEmailAddr( String emailAddr )
  {
    this.emailAddr = emailAddr;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getNodes()
  {
    return nodes;
  }

  public void setNodes( String nodes )
  {
    this.nodes = nodes;
  }

  public String getJobName()
  {
    return jobName;
  }

  public void setJobName( String jobName )
  {
    this.jobName = jobName;
  }

  public String getDepartmentName()
  {
    return departmentName;
  }

  public void setDepartmentName( String departmentName )
  {
    this.departmentName = departmentName;
  }

  public Integer getCalculatedBudgetDeduction()
  {
    BigDecimal convertedAwardQuantity = getCalculatedBudgetDeductionNotRounded();
    return BudgetUtils.getBudgetDisplayValue( convertedAwardQuantity );
  }

  public BigDecimal getCalculatedBudgetDeductionNotRounded()
  {
    Long awardValue = awardQuantity != null ? awardQuantity : awardLevel;
    awardValue = ( awardValue != null ) ? awardValue : 0L;
    return BudgetUtils.applyMediaConversion( BigDecimal.valueOf( awardValue ), BigDecimal.valueOf( countryRatio ) );
  }

  public CalculatorResult getCalculatorResultBeans( int index )
  {
    CalculatorResult result = calculatorResults.get( index );
    if ( result == null )
    {
      result = new CalculatorResult();
      calculatorResults.put( index, result );
    }
    return result;
  }

  public List<CalculatorResult> getCalculatorResults()
  {
    return new ArrayList<CalculatorResult>( calculatorResults.values() );
  }

  public void setCalculatorResults( Map<Integer, CalculatorResult> calculatorResults )
  {
    this.calculatorResults = calculatorResults;
  }

  public String getNodeName()
  {
    String nodeName = "";
    try
    {
      // converts the nodes json to objects
      ObjectMapper mapper = new ObjectMapper();
      List<NodeBean> nodeBeans = mapper.readValue( nodes, new TypeReference<List<NodeBean>>()
      {
      } );

      for ( NodeBean nodeBean : nodeBeans )
      {
        if ( nodeBean.getId().equals( this.getNodeId() ) )
        {
          nodeName = nodeBean.getName();
          break;
        }
      }
    }
    catch( Throwable t )
    {
      log.error( "ERROR: " + t.toString() );
    }

    return nodeName;
  }

  public static class NodeBean
  {
    private Long id;
    private String name;

    public Long getId()
    {
      return id;
    }

    public void setId( Long id )
    {
      this.id = id;
    }

    public String getName()
    {
      return name;
    }

    public void setName( String name )
    {
      this.name = name;
    }
  }

  public boolean isOptOutAwards()
  {
    return optOutAwards;
  }

  public void setOptOutAwards( boolean optOutAwards )
  {
    this.optOutAwards = optOutAwards;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  /* Client customizations for WIP #42701 starts */
  public String getCurrency()
  {
    return currency;
  }

  public void setCurrency( String currency )
  {
    this.currency = currency;
  }

  public Long getAwardMin()
  {
    return awardMin;
  }

  public void setAwardMin( Long awardMin )
  {
    this.awardMin = awardMin;
  }

  public Long getAwardMax()
  {
    return awardMax;
  }

  public void setAwardMax( Long awardMax )
  {
    this.awardMax = awardMax;
  }
  /* Client customizations for WIP #42701 ends */


  /* Client customizations for WIP #26532 starts */
  public Boolean getPurlAllowOutsideDomains()
  {
    return purlAllowOutsideDomains;
  }

  public void setPurlAllowOutsideDomains( Boolean purlAllowOutsideDomains )
  {
    this.purlAllowOutsideDomains = purlAllowOutsideDomains;
  }
  /* Client customizations for WIP #26532 ends */ 
}
