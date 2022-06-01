/*
 * Copyright 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.participant;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.participant.ParticipantAddressBook;

/**
 * @author crosenquest Apr 27, 2005
 */
public interface ParticipantAddressBookDAO extends DAO
{
  public static final String BEAN_NAME = "participantAddressBookDAO";

  /**
   * Deletes the participant param.
   * 
   * @param participant
   */
  public void deleteParticipantAddressBook( ParticipantAddressBook addressBook );

  /**
   * Saves the participant address book information to the database.
   * 
   * @param ParticipantAddressBook
   * @return ParticipantAddressBook
   */
  public ParticipantAddressBook saveParticipantAddressBook( ParticipantAddressBook addressBook );

  /**
   * Retreieves a particular ParticipantAddressBook
   * 
   * @param id
   * @return ParticipantAddressBook
   */
  public ParticipantAddressBook getParticipantAddressBookById( Long id );
  
  /**
   * Retreieves a set ParticipantAddressBooks for a participant
   * 
   * @param id the participant
   * @return ParticipantAddressBook
   */
  public java.util.List getParticipantAddressBooksForParticipant( Long id );
}