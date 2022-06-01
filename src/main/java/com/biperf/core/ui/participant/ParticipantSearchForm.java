/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/participant/ParticipantSearchForm.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.participant;

import java.util.HashMap;
import java.util.Map;

import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.ui.BaseForm;

/**
 * ParticipantSearchForm.
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
 * <td>May 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantSearchForm extends BaseForm
{
  private String firstName;
  private String lastName;
  private String userName;
  private String ssn;
  private String emailAddr;
  private String country;
  private String postalCode;
  private String returnActionUrl;
  private String returnActionMapping;
  private Long nodeId;
  private String forwardActionUrl;
  private final Map params = new HashMap(); // parameters that are passed to

  // pax search by the caller that can be
  // propagated on the search result links

  public String getCountry()
  {
    return country;
  }

  public void setCountry( String country )
  {
    this.country = country;
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

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getPostalCode()
  {
    return postalCode;
  }

  public void setPostalCode( String postalCode )
  {
    this.postalCode = postalCode;
  }

  public String getReturnActionUrl()
  {
    return returnActionUrl;
  }

  public void setReturnActionUrl( String returnActionUrl )
  {
    this.returnActionUrl = returnActionUrl;
  }

  public String getSsn()
  {
    return ssn;
  }

  public void setSsn( String ssn )
  {
    this.ssn = ssn;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public final Map getParamsMap()
  {
    return params;
  }

  public Object getParams( String key )
  {
    return params.get( key );
  }

  public void setParams( String key, Object value )
  {
    params.put( key, value );
  }

  public ParticipantSearchCriteria toDomainObject()
  {
    ParticipantSearchCriteria searchCriteria = new ParticipantSearchCriteria();
    searchCriteria.setFirstName( firstName );
    searchCriteria.setLastName( lastName );
    searchCriteria.setUserName( userName );
    searchCriteria.setEmailAddr( emailAddr );
    searchCriteria.setSsn( ssn );
    searchCriteria.setCountry( country );
    searchCriteria.setPostalCode( postalCode );
    return searchCriteria;
  }

  public String getForwardActionUrl()
  {
    return forwardActionUrl;
  }

  public void setForwardActionUrl( String forwardActionUrl )
  {
    this.forwardActionUrl = forwardActionUrl;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getReturnActionMapping()
  {
    return returnActionMapping;
  }

  public void setReturnActionMapping( String returnActionMapping )
  {
    this.returnActionMapping = returnActionMapping;
  }

}
