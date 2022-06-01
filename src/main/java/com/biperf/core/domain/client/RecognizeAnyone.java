
package com.biperf.core.domain.client;

import com.biperf.core.domain.BaseDomain;

public class RecognizeAnyone extends BaseDomain {
	
	private static final long serialVersionUID = 1L;

	private Long userId;
	private String receiverFirstName;
	private String receiverLastName;
	private String receiverEmail;
	private String comments;
	private Long cardId;
	private String cardImageURL;
	
	public Long getUserId() 
	{
		return userId;
	}

	public void setUserId(Long userId) 
	{
		this.userId = userId;
	}

	public String getReceiverFirstName() 
	{
		return receiverFirstName;
	}

	public void setReceiverFirstName(String receiverFirstName) 
	{
		this.receiverFirstName = receiverFirstName;
	}

	public String getReceiverLastName() 
	{
		return receiverLastName;
	}

	public void setReceiverLastName(String receiverLastName) 
	{
		this.receiverLastName = receiverLastName;
	}

	public String getReceiverEmail() 
	{
		return receiverEmail;
	}

	public void setReceiverEmail(String receiverEmail) 
	{
		this.receiverEmail = receiverEmail;
	}

	public String getComments() 
	{
		return comments;
	}

	public void setComments(String comments) 
	{
		this.comments = comments;
	}

	public Long getCardId() 
	{
		return cardId;
	}

	public void setCardId(Long cardId) 
	{
		this.cardId = cardId;
	}

	public String getCardImageURL() 
	{
		return cardImageURL;
	}

	public void setCardImageURL(String cardImageURL) 
	{
		this.cardImageURL = cardImageURL;
	}

	@Override
	public boolean equals(Object object) 
	{
		if (this == object) {
			return true;
		}
	
		if (!(object instanceof RecognizeAnyone)) {
			return false;
		}
	
		RecognizeAnyone recognizeAnyone = (RecognizeAnyone) object;
	
		if (this.getId() != null && this.getId().equals(recognizeAnyone.getId())) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() 
	{
		  return ( this.getId() != null ? this.getId().hashCode() : 0 );
	}

}

