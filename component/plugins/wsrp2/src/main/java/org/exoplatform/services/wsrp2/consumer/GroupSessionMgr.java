package org.exoplatform.services.wsrp2.consumer;

/**
 * Interface for a consumer based group session.A group session is used to hold
 * portlet session objects of portlet instances which belong to the same group
 * of the same producer according to their portlet description.
 * 
 * @author <a href='mailto:peter.fischer@de.ibm.com'>Peter Fischer</a>
 * @author Benjamin Mestrallet
 */
public interface GroupSessionMgr extends GroupSession, InitCookieInfo {

}
