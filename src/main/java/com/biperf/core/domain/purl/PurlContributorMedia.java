
package com.biperf.core.domain.purl;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.PurlContributorMediaStatus;
import com.biperf.core.domain.enums.PurlContributorMediaType;
import com.biperf.core.domain.enums.PurlMediaState;

public class PurlContributorMedia extends BaseDomain
{
  private PurlContributor purlContributor;
  private String caption;
  private String url;
  private String urlThumb;

  private PurlContributorMediaType type;
  private PurlContributorMediaStatus status;
  private PurlMediaState state;

  public PurlContributor getPurlContributor()
  {
    return purlContributor;
  }

  public void setPurlContributor( PurlContributor purlContributor )
  {
    this.purlContributor = purlContributor;
  }

  public PurlContributorMediaType getType()
  {
    return type;
  }

  public void setType( PurlContributorMediaType type )
  {
    this.type = type;
  }

  public String getCaption()
  {
    return caption;
  }

  public void setCaption( String caption )
  {
    this.caption = caption;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public String getUrlThumb()
  {
    return urlThumb;
  }

  public void setUrlThumb( String urlThumb )
  {
    this.urlThumb = urlThumb;
  }

  public PurlContributorMediaStatus getStatus()
  {
    return status;
  }

  public void setStatus( PurlContributorMediaStatus status )
  {
    this.status = status;
  }

  public PurlMediaState getState()
  {
    return state;
  }

  public void setState( PurlMediaState state )
  {
    this.state = state;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( caption == null ? 0 : caption.hashCode() );
    result = prime * result + ( type == null ? 0 : type.hashCode() );
    result = prime * result + ( purlContributor == null ? 0 : purlContributor.hashCode() );
    result = prime * result + ( status == null ? 0 : status.hashCode() );
    result = prime * result + ( state == null ? 0 : state.hashCode() );
    result = prime * result + ( url == null ? 0 : url.hashCode() );
    result = prime * result + ( urlThumb == null ? 0 : urlThumb.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( ! ( obj instanceof PurlContributorMedia ) )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    PurlContributorMedia other = (PurlContributorMedia)obj;
    if ( caption == null )
    {
      if ( other.caption != null )
      {
        return false;
      }
    }
    else if ( !caption.equals( other.caption ) )
    {
      return false;
    }
    if ( type == null )
    {
      if ( other.type != null )
      {
        return false;
      }
    }
    else if ( !type.equals( other.type ) )
    {
      return false;
    }
    if ( purlContributor == null )
    {
      if ( other.purlContributor != null )
      {
        return false;
      }
    }
    else if ( !purlContributor.equals( other.purlContributor ) )
    {
      return false;
    }
    if ( status == null )
    {
      if ( other.status != null )
      {
        return false;
      }
    }
    else if ( !status.equals( other.status ) )
    {
      return false;
    }
    if ( state == null )
    {
      if ( other.state != null )
      {
        return false;
      }
    }
    else if ( !state.equals( other.state ) )
    {
      return false;
    }
    if ( url == null )
    {
      if ( other.url != null )
      {
        return false;
      }
    }
    else if ( !url.equals( other.url ) )
    {
      return false;
    }
    if ( urlThumb == null )
    {
      if ( other.urlThumb != null )
      {
        return false;
      }
    }
    else if ( !urlThumb.equals( other.urlThumb ) )
    {
      return false;
    }
    return true;
  }
}
