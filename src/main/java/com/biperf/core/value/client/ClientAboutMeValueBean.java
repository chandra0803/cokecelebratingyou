
package com.biperf.core.value.client;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude( JsonInclude.Include.NON_NULL )
@JsonPropertyOrder( { "contributor", "recipient", "iaminfo" } )
public class ClientAboutMeValueBean
{

  @JsonProperty( "contributor" )
  private Contributor contributor;
  @JsonProperty( "recipient" )
  private Recipient recipient;
  @JsonProperty( "iaminfo" )
  private List<Iaminfo> iaminfo = new ArrayList<Iaminfo>();

  @JsonProperty( "contributor" )
  public Contributor getContributor()
  {
    return contributor;
  }

  @JsonProperty( "contributor" )
  public void setContributor( Contributor contributor )
  {
    this.contributor = contributor;
  }

  public ClientAboutMeValueBean withContributor( Contributor contributor )
  {
    this.contributor = contributor;
    return this;
  }

  @JsonProperty( "recipient" )
  public Recipient getRecipient()
  {
    return recipient;
  }

  @JsonProperty( "recipient" )
  public void setRecipient( Recipient recipient )
  {
    this.recipient = recipient;
  }

  public ClientAboutMeValueBean withRecipient( Recipient recipient )
  {
    this.recipient = recipient;
    return this;
  }

  @JsonProperty( "iaminfo" )
  public List<Iaminfo> getIaminfo()
  {
    return iaminfo;
  }

  @JsonProperty( "iaminfo" )
  public void setIaminfo( List<Iaminfo> iaminfo )
  {
    this.iaminfo = iaminfo;
  }

  public ClientAboutMeValueBean withIaminfo( List<Iaminfo> iaminfo )
  {
    this.iaminfo = iaminfo;
    return this;
  }

}
