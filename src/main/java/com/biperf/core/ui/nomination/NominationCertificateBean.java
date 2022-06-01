
package com.biperf.core.ui.nomination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_NULL )
public class NominationCertificateBean
{

  @JsonProperty( "certificate" )
  private NominationsCertificateViewBean certificate;

  public NominationsCertificateViewBean getCertificate()
  {
    return certificate;
  }

  public void setCertificate( NominationsCertificateViewBean certificate )
  {
    this.certificate = certificate;
  }

}
