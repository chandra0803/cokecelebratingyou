/*
 * Copyright 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.participant.hibernate;

import org.hibernate.Query;
import org.hibernate.Session;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.participant.ParticipantAddressBookDAO;
import com.biperf.core.domain.participant.ParticipantAddressBook;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * ParticipantAddressBookDAO implementation to implement the business logic for the DAO interface.
 * 
 * @author crosenquest Apr 27, 2005
 */
public class ParticipantAddressBookDAOImpl extends BaseDAO implements ParticipantAddressBookDAO
{

  /**
   * Saves the participant information to the database. Overridden from
   * 
   * @see com.biperf.core.dao.participant.ParticipantDAO#saveParticipant(com.biperf.core.domain.participant.Participant)
   * @param participant
   * @return Participant
   */
  public ParticipantAddressBook saveParticipantAddressBook( ParticipantAddressBook participantAddressBook )
  {
    return (ParticipantAddressBook)HibernateUtil.saveOrUpdateOrShallowMerge( participantAddressBook );
  }

  /**
   * Get the participant information from the database corresponding the the id. Overridden from
   * 
   * @see com.biperf.core.dao.participant.ParticipantDAO#getParticipantById(java.lang.Long)
   * @param id
   * @return Participant
   */
  public ParticipantAddressBook getParticipantAddressBookById( Long id )
  {
    return (ParticipantAddressBook)getSession().get( ParticipantAddressBook.class, id );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.participant.ParticipantDAO#deleteParticipant(com.biperf.core.domain.participant.Participant)
   * @param participant
   */
  public void deleteParticipantAddressBook( ParticipantAddressBook participantAddressBook )
  {
    getSession().delete( participantAddressBook );
  }
  
  public java.util.List getParticipantAddressBooksForParticipant( Long id )
  {
    Session session = getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.participant.AllAddressBooksForParticipant" );
    query.setParameter( "id", id ) ;
    return query.list();
  }
}