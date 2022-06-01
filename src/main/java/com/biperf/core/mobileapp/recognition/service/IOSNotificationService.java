
package com.biperf.core.mobileapp.recognition.service;

import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.Payload;
import javapns.notification.PushedNotifications;

public interface IOSNotificationService
{
  public PushedNotifications payload( Payload payload, Object keystore, String password, boolean production, Object devices ) throws CommunicationException, KeystoreException;
}
