/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/participant/ParticipantBalancesForm.java,v $
 */

package com.biperf.core.ui.participant;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.ui.BaseForm;

@SuppressWarnings( "serial" )
public class ParticipantActivationForm extends BaseForm
{
  private String method;
  private Long addUserCharacteristic = null;

  private List<ParticipantIdentifierBean> participantIdentifierBeans = new ArrayList<ParticipantIdentifierBean>();
  private List<Characteristic> userCharacteristics = new ArrayList<Characteristic>();

  public Long getAddUserCharacteristic()
  {
    return addUserCharacteristic;
  }

  public void setAddUserCharacteristic( Long addUserCharacteristic )
  {
    this.addUserCharacteristic = addUserCharacteristic;
  }

  public List<ParticipantIdentifierBean> getParticipantIdentifierBeans()
  {
    return participantIdentifierBeans;
  }

  public void setParticipantIdentifierBeans( List<ParticipantIdentifierBean> participantIdentifierBeans )
  {
    this.participantIdentifierBeans = participantIdentifierBeans;
  }

  public List<Characteristic> getUserCharacteristics()
  {
    return userCharacteristics;
  }

  public void setUserCharacteristics( List<Characteristic> userCharacteristics )
  {
    this.userCharacteristics = userCharacteristics;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }
}
