
package com.biperf.core.domain.homepage;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

//This annotation is used to not to serialize the null properties.
@JsonInclude( value = Include.NON_NULL )
public class HeroImageDetailsView
{
  // This is for future use to keep additional messages
  private String[] messages = {};
  private List<HeroImageDetailBean> slides;

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  public List<HeroImageDetailBean> getSlides()
  {
    return slides;
  }

  public void setSlides( List<HeroImageDetailBean> slides )
  {
    this.slides = slides;
  }

}
