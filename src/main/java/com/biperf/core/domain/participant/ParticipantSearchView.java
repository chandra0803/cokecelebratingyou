/*
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.participant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ParticipantSearchView.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>putta</th>
 * <th>12/03/2012</th>
 * <th>1.0</th>
 *
 */
@JsonInclude( value = Include.NON_NULL )
public class ParticipantSearchView extends ParticipantInfoView
{
  private static final long serialVersionUID = 1L;
  private Boolean selected;
  private Boolean locked;
  private String urlEdit;
  private Boolean canRecognize;
  private boolean purlAllowOutsideDomains;
  
  public ParticipantSearchView( Participant pax )
  {

    super( pax );
  }

  public ParticipantSearchView( Participant pax, String countryCodeValue, String countryNameValue )
  {
    super( pax );
    setCountryCode( countryCodeValue );
    setCountryName( countryNameValue );
  }

  public ParticipantSearchView()
  {

  }

  public ParticipantSearchView( Participant pax, String profileUrl, String countryCodeValue, String countryNameValue )
  {
    super( pax );
    setProfileUrl( profileUrl );
    setCountryCode( countryCodeValue );
    setCountryName( countryNameValue );
  }

  @JsonProperty( "isSelected" )
  public Boolean isSelected()
  {
    return selected;
  }

  public void setSelected( Boolean selected )
  {
    this.selected = selected;
  }

  @JsonProperty( "isLocked" )
  public Boolean isLocked()
  {
    return locked;
  }

  public void setLocked( Boolean locked )
  {
    this.locked = locked;
  }

  public String getUrlEdit()
  {
    return urlEdit;
  }

  public void setUrlEdit( String urlEdit )
  {
    this.urlEdit = urlEdit;
  }

  @JsonProperty( "canRecognize" )
  public Boolean canRecognize()
  {
    return canRecognize;
  }

  public void setCanRecognize( Boolean canRecognize )
  {
    this.canRecognize = canRecognize;
  }
  // Client customizations for wip #26532 starts
  @JsonProperty( "purlAllowOutsideDomains" )
  public boolean isPurlAllowOutsideDomains()
  {
    return purlAllowOutsideDomains;
  }

  public void setPurlAllowOutsideDomains( boolean purlAllowOutsideDomains )
  {
    this.purlAllowOutsideDomains = purlAllowOutsideDomains;
  }
  // Client customizations for wip #26532 ends
}
