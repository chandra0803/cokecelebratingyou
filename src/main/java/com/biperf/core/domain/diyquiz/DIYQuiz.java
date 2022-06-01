
package com.biperf.core.domain.diyquiz;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionCert;
import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.user.User;
import com.biperf.core.utils.DateUtils;

/**
 * 
 * DIYQuiz domain object.
 * 
 * @author kandhi
 * @since Jul 9, 2013
 * @version 1.0
 */
public class DIYQuiz extends Quiz
{
  private static final long serialVersionUID = 1L;
  private User owner;
  private Promotion promotion;
  private BadgeRule badgeRule;
  private PromotionCert certificate;
  private Date startDate;
  private Date endDate;
  private boolean allowUnlimitedAttempts;
  private int maximumAttempts;
  private String quizStatus;
  private String notificationText;
  private String introductionText;
  private Set<DIYQuizParticipant> participants = new LinkedHashSet<DIYQuizParticipant>();

  public User getOwner()
  {
    return owner;
  }

  public void setOwner( User owner )
  {
    this.owner = owner;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public BadgeRule getBadgeRule()
  {
    return badgeRule;
  }

  public void setBadgeRule( BadgeRule badgeRule )
  {
    this.badgeRule = badgeRule;
  }

  public PromotionCert getCertificate()
  {
    return certificate;
  }

  public void setCertificate( PromotionCert certificate )
  {
    this.certificate = certificate;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public void setStartDate( Date startDate )
  {
    this.startDate = startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void setEndDate( Date endDate )
  {
    this.endDate = endDate;
  }

  public boolean isAllowUnlimitedAttempts()
  {
    return allowUnlimitedAttempts;
  }

  public void setAllowUnlimitedAttempts( boolean allowUnlimitedAttempts )
  {
    this.allowUnlimitedAttempts = allowUnlimitedAttempts;
  }

  public int getMaximumAttempts()
  {
    return maximumAttempts;
  }

  public void setMaximumAttempts( int maximumAttempts )
  {
    this.maximumAttempts = maximumAttempts;
  }

  public Set<DIYQuizParticipant> getParticipants()
  {
    return participants;
  }

  public void setParticipants( Set<DIYQuizParticipant> participants )
  {
    this.participants = participants;
  }

  public void addParticipant( DIYQuizParticipant diyQuizParticipant )
  {
    diyQuizParticipant.setQuiz( this );
    this.participants.add( diyQuizParticipant );
  }

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
    DIYQuiz other = (DIYQuiz)obj;
    if ( owner == null )
    {
      if ( other.owner != null )
      {
        return false;
      }
    }
    else if ( !owner.equals( other.owner ) )
    {
      return false;
    }
    if ( promotion == null )
    {
      if ( other.promotion != null )
      {
        return false;
      }
    }
    else if ( !promotion.equals( other.promotion ) )
    {
      return false;
    }
    if ( badgeRule == null )
    {
      if ( other.badgeRule != null )
      {
        return false;
      }
    }
    else if ( !badgeRule.equals( other.badgeRule ) )
    {
      return false;
    }
    if ( certificate == null )
    {
      if ( other.certificate != null )
      {
        return false;
      }
    }
    else if ( !certificate.equals( other.certificate ) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( owner == null ? 0 : owner.hashCode() );
    result = prime * result + ( promotion == null ? 0 : promotion.hashCode() );
    result = prime * result + ( badgeRule == null ? 0 : badgeRule.hashCode() );
    result = prime * result + ( certificate == null ? 0 : certificate.hashCode() );
    return result;
  }

  public String getDisplayStartDate()
  {
    return DateUtils.toDisplayString( this.getStartDate() );
  }

  public String getDisplayEndDate()
  {
    return DateUtils.toDisplayString( this.getEndDate() );
  }

  public String getDisplayLastUpdatedDate()
  {
    return DateUtils.toDisplayString( this.getAuditUpdateInfo().getDateModified() );
  }

  public String getQuizStatus()
  {
    return quizStatus;
  }

  public void setQuizStatus( String quizStatus )
  {
    this.quizStatus = quizStatus;
  }

  public String getNotificationText()
  {
    return notificationText;
  }

  public void setNotificationText( String notificationText )
  {
    this.notificationText = notificationText;
  }

  public String getIntroductionText()
  {
    return introductionText;
  }

  public void setIntroductionText( String introductionText )
  {
    this.introductionText = introductionText;
  }

}
