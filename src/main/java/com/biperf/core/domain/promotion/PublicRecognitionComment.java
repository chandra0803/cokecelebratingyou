
package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.participant.Participant;

/**
 * Product.
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
 * <td>veeramas</td>
 * <td>July 23, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class PublicRecognitionComment extends BaseDomain
{
  private String comments;
  private LanguageType commentsLanguageType;
  private Participant user;
  private Claim claim;
  private Long teamId;

  public Participant getUser()
  {
    return user;
  }

  public void setUser( Participant user )
  {
    this.user = user;
  }

  public Claim getClaim()
  {
    return claim;
  }

  public void setClaim( Claim claim )
  {
    this.claim = claim;
  }

  public Long getTeamId()
  {
    return teamId;
  }

  public void setTeamId( Long teamId )
  {
    this.teamId = teamId;
  }

  public String getComments()
  {
    return comments;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public LanguageType getCommentsLanguageType()
  {
    return commentsLanguageType;
  }

  public void setCommentsLanguageType( LanguageType commentsLanguageType )
  {
    this.commentsLanguageType = commentsLanguageType;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( getId() == null ? 0 : getId().hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( ! ( obj instanceof PublicRecognitionLike ) )
    {
      return false;
    }

    final PublicRecognitionLike comment = (PublicRecognitionLike)obj;

    if ( getId() != null ? !getId().equals( comment.getId() ) : comment.getId() != null )
    {
      return false;
    }

    if ( getUser() != null ? !getUser().equals( comment.getUser() ) : comment.getUser() != null )
    {
      return false;
    }

    if ( getClaim() != null ? !getClaim().equals( comment.getClaim() ) : comment.getClaim() != null )
    {
      return false;
    }

    return true;
  }

}
