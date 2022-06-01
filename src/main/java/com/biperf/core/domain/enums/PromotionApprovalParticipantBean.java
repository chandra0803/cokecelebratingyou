/**
 * 
 */

package com.biperf.core.domain.enums;

import java.io.Serializable;

import com.biperf.core.domain.promotion.PromotionParticipantApprover;
import com.biperf.core.domain.promotion.PromotionParticipantSubmitter;

/**
 * PromotionApprovalParticipantBean.
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
 * <td>asondgeroth</td>
 * <td>Sep 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionApprovalParticipantBean implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String participantType;
  private Long id;
  private Long promotionId;
  private Long participantId;
  private String firstName;
  private String lastName;

  private Long version;

  private String remove;
  private Long levelId;

  /**
   * @return firstName
   */
  public String getFirstName()
  {
    return firstName;
  }

  /**
   * @param firstName
   */
  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  /**
   * @return id
   */
  public Long getId()
  {
    return id;
  }

  /**
   * @param id
   */
  public void setId( Long id )
  {
    if ( id.longValue() == 0 )
    {
      id = null;
    }
    this.id = id;
  }

  /**
   * @return lastName
   */
  public String getLastName()
  {
    return lastName;
  }

  /**
   * @param lastName
   */
  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  /**
   * @return participantId
   */
  public Long getParticipantId()
  {
    return participantId;
  }

  /**
   * @param participantId
   */
  public void setParticipantId( Long participantId )
  {
    this.participantId = participantId;
  }

  /**
   * @return participantType
   */
  public String getParticipantType()
  {
    return participantType;
  }

  /**
   * @param participantType
   */
  public void setParticipantType( String participantType )
  {
    this.participantType = participantType;
  }

  /**
   * @return promotionId
   */
  public Long getPromotionId()
  {
    return promotionId;
  }

  /**
   * @param promotionId
   */
  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  /**
   * @return version
   */
  public Long getVersion()
  {
    return version;
  }

  /**
   * @param version
   */
  public void setVersion( Long version )
  {
    this.version = version;
  }

  /**
   * @return true if partcipant type is submitter
   */
  public boolean isSubmitter()
  {
    return PromotionParticipantSubmitter.PROMO_PARTICIPANT_TYPE.equals( participantType );
  }

  /**
   * @return true if partcipant type is approver
   */
  public boolean isApprover()
  {
    return PromotionParticipantApprover.PROMO_PARTICIPANT_TYPE.equals( participantType );
  }

  /**
   * @return remove
   */
  public String getRemove()
  {
    return remove;
  }

  /**
   * @param remove
   */
  public void setRemove( String remove )
  {
    this.remove = remove;
  }

  public Long getLevelId()
  {
    return levelId;
  }

  public void setLevelId( Long levelId )
  {
    this.levelId = levelId;
  }

}
