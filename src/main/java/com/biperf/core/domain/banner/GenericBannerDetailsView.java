
package com.biperf.core.domain.banner;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

//This annotation is used to not to serialize the null properties.
@JsonInclude( value = Include.NON_NULL )
public class GenericBannerDetailsView
{
  // This is for future use to keep additional messages
  private String[] messages = {};
  private List<GenericBannerDetailBean> slides;

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  public List<GenericBannerDetailBean> getSlides()
  {
    return slides;
  }

  public void setSlides( List<GenericBannerDetailBean> slides )
  {
    this.slides = slides;
  }

}
