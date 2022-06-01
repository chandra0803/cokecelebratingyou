
package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;

/**
 * Product.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * </tr>
 * <tr>
 * <td>dudyala</td>
 * <td>Oct 6, 2016</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
@SuppressWarnings( "serial" )
public class PublicRecognitionUserConnections extends BaseDomain
{
  private Long senderId;
  private Long receiverId;

  public Long getSenderId()
  {
    return senderId;
  }

  public void setSenderId( Long senderId )
  {
    this.senderId = senderId;
  }

  public Long getReceiverId()
  {
    return receiverId;
  }

  public void setReceiverId( Long receiverId )
  {
    this.receiverId = receiverId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( receiverId == null ? 0 : receiverId.hashCode() );
    result = prime * result + ( senderId == null ? 0 : senderId.hashCode() );
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    PublicRecognitionUserConnections other = (PublicRecognitionUserConnections)obj;
    if ( receiverId == null )
    {
      if ( other.receiverId != null )
      {
        return false;
      }
    }
    else if ( !receiverId.equals( other.receiverId ) )
    {
      return false;
    }
    if ( senderId == null )
    {
      if ( other.senderId != null )
      {
        return false;
      }
    }
    else if ( !senderId.equals( other.senderId ) )
    {
      return false;
    }
    return true;
  }
}
