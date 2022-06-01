
package com.biperf.core.domain.publicrecognition;

import com.biperf.core.domain.promotion.PublicRecognitionComment;
import com.biperf.core.utils.UserManager;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author dudam
 * @since Dec 11, 2012
 * @version 1.0
 */
public class PublicRecognitionCommentViewBean
{
  private Long id;
  private Long recognitionId;
  private String comment;
  private boolean isMine;
  private PublicRecognitionParticipantView commenter;
  private boolean allowTranslate;

  public PublicRecognitionCommentViewBean()
  {

  }

  public PublicRecognitionCommentViewBean( PublicRecognitionComment prc, boolean allowTranslate )
  {
    Long userId = UserManager.getUserId();
    commenter = new PublicRecognitionParticipantView( prc.getUser() );
    this.id = prc.getId();
    this.comment = prc.getComments();
    this.isMine = userId.equals( prc.getUser().getId() ) ? true : false;
    this.allowTranslate = allowTranslate;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public Long getRecognitionId()
  {
    return recognitionId;
  }

  public void setRecognitionId( Long recognitionId )
  {
    this.recognitionId = recognitionId;
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment( String comment )
  {
    this.comment = comment;
  }

  @JsonProperty( "isMine" )
  public boolean isMine()
  {
    return isMine;
  }

  public void setMine( boolean isMine )
  {
    this.isMine = isMine;
  }

  public PublicRecognitionParticipantView getCommenter()
  {
    return commenter;
  }

  public void setCommenter( PublicRecognitionParticipantView commenter )
  {
    this.commenter = commenter;
  }

  public boolean isAllowTranslate()
  {
    return allowTranslate;
  }

  public void setAllowTranslate( boolean allowTranslate )
  {
    this.allowTranslate = allowTranslate;
  }

}
