/*
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.participant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.PageConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * ParticipantProfileView.java.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>sharafud</th>
 * <th>3/8/2013</th>
 * <th>1.0</th>
 *
 */
@JsonInclude( value = Include.NON_EMPTY )
public class DelegatorView implements Serializable, Comparable<DelegatorView>
{

  private static final long serialVersionUID = 1L;

  private long id;
  private String firstName;
  private String lastName;
  private String delegatorUrl;
  private boolean allowDelegate;

  public long getId()
  {
    return id;
  }

  public void setId( long id )
  {
    this.id = id;
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

  public String getDelegatorUrl()
  {
    Map delegateMap = new HashMap();
    delegateMap.put( "id", id + "" );
    delegatorUrl = ClientStateUtils.generateEncodedLink( "", PageConstants.DELEGATE_LAUNCH, delegateMap );
    return delegatorUrl;
  }

  public void setDelegatorUrl( String delegatorUrl )
  {
    this.delegatorUrl = delegatorUrl;
  }

  @Override
  public int compareTo( DelegatorView delegatorView )
  {
    return this.firstName.compareTo( delegatorView.firstName );
  }

  public boolean isAllowDelegate()
  {
    return allowDelegate;
  }

  public void setAllowDelegate( boolean allowDelegate )
  {
    this.allowDelegate = allowDelegate;
  }

}
