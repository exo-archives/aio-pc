/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.monitor;

/*
 * Tue, May 27, 2003 @
 * @author: Tuan Nguyen
 * @version: $Id: PortletRequestMonitorData.java,v 1.2 2004/05/06 12:19:00 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class PortletRequestMonitorData {
  private long minRange_ ;
  private long maxRange_ ;
  private int  actionRequestCounter_ ;
  private long minActionExecutionTime_ ;
  private long maxActionExecutionTime_ ;
  private long sumActionExecutionTime_ = 0 ;

  private int  renderRequestCounter_ ;
  private long minRenderExecutionTime_ ;
  private long maxRenderExecutionTime_ ;
  private long sumRenderExecutionTime_ = 0 ;

  private int  eventRequestCounter_ ;
  private long minEventExecutionTime_ ;
  private long maxEventExecutionTime_ ;
  private long sumEventExecutionTime_ = 0 ;

  private int  resourceRequestCounter_ ;
  private long minResourceExecutionTime_ ;
  private long maxResourceExecutionTime_ ;
  private long sumResourceExecutionTime_ = 0 ;

  private int cacheHitCounter_ ;

  public PortletRequestMonitorData(long minRange, long maxRange) {
    minRange_ = minRange ;
    maxRange_ = maxRange ;

    actionRequestCounter_ = -5 ; //ignore 5 first request to make sure the jvm optimize the class
    minActionExecutionTime_  = 0 ;
    maxActionExecutionTime_ = 0 ;

    renderRequestCounter_ = -5 ; //ignore 5 first request to make sure the jvm optimize the class
    minRenderExecutionTime_  = 0 ;
    maxRenderExecutionTime_ = 0 ;

    eventRequestCounter_ = -5 ; //ignore 5 first request to make sure the jvm optimize the class
    minEventExecutionTime_  = 0 ;
    maxEventExecutionTime_ = 0 ;

    resourceRequestCounter_ = -5 ; //ignore 5 first request to make sure the jvm optimize the class
    minResourceExecutionTime_  = 0 ;
    maxResourceExecutionTime_ = 0 ;

    cacheHitCounter_ = 0 ;
  }

  public long minRange() { return minRange_ ; }
  public long maxRange() { return maxRange_ ; }

  public int getActionRequestCounter() { return actionRequestCounter_  ; }
  public int getRenderRequestCounter() { return renderRequestCounter_  ; }
  public int getEventRequestCounter() { return eventRequestCounter_  ; }
  public int getResourceRequestCounter() { return resourceRequestCounter_  ; }

  public int getCacheHitCounter() { return cacheHitCounter_  ; }

  public long getMinActionExecutionTime() { return minActionExecutionTime_ ; }

  public long getMinRenderExecutionTime() { return minRenderExecutionTime_ ; }

  public long getMinEventExecutionTime() { return minEventExecutionTime_ ; }

  public long getMinResourceExecutionTime() { return minResourceExecutionTime_ ; }

  public long getMaxActionExecutionTime() { return maxActionExecutionTime_ ; }

  public long getMaxRenderExecutionTime() { return maxRenderExecutionTime_ ; }

  public long getMaxEventExecutionTime() { return maxEventExecutionTime_ ; }

  public long getMaxResourceExecutionTime() { return maxResourceExecutionTime_ ; }

  public long getAvgActionExecutionTime() {
    if(actionRequestCounter_ <= 0) return 0 ;
    return sumActionExecutionTime_/actionRequestCounter_ ;
  }

  public long getAvgRenderExecutionTime() {
    if(renderRequestCounter_ <= 0) return 0 ;
    return sumRenderExecutionTime_/renderRequestCounter_ ;
  }

  public long getAvgEventExecutionTime() {
    if(eventRequestCounter_ <= 0) return 0 ;
    return sumEventExecutionTime_/eventRequestCounter_ ;
  }

  public long getAvgResourceExecutionTime() {
    if(resourceRequestCounter_ <= 0) return 0 ;
    return sumResourceExecutionTime_/resourceRequestCounter_ ;
  }

  public long sumActionExecutionTime() { return sumActionExecutionTime_ ; }

  public long sumRenderExecutionTime() { return sumRenderExecutionTime_ ; }

  public long sumEventExecutionTime() { return sumEventExecutionTime_ ; }

  public long sumResourceExecutionTime() { return sumResourceExecutionTime_ ; }

  public void logActionRequest(long executionTime) {
    actionRequestCounter_++ ;
    if(actionRequestCounter_ > 0) {
      sumActionExecutionTime_  += executionTime;
      if(executionTime < minActionExecutionTime_) minActionExecutionTime_ = executionTime ;
      if(executionTime > maxActionExecutionTime_) maxActionExecutionTime_ = executionTime ;
    } else {
      minActionExecutionTime_ = executionTime ;
      maxActionExecutionTime_ = executionTime ;
    }
  }

  public void logRenderRequest(long executionTime, boolean cacheHit) {
    renderRequestCounter_++ ;
    if(renderRequestCounter_ > 0) {
      sumRenderExecutionTime_  += executionTime;
      if(cacheHit) cacheHitCounter_++ ;
      if(executionTime < minRenderExecutionTime_) minRenderExecutionTime_ = executionTime ;
      if(executionTime > maxRenderExecutionTime_) maxRenderExecutionTime_ = executionTime ;
    } else {
      minRenderExecutionTime_ = executionTime ;
      maxRenderExecutionTime_ = executionTime ;
    }
  }

  public void logEventRequest(long executionTime) {
    eventRequestCounter_++ ;
    if(eventRequestCounter_ > 0) {
      sumEventExecutionTime_  += executionTime;
      if(executionTime < minEventExecutionTime_) minEventExecutionTime_ = executionTime ;
      if(executionTime > maxEventExecutionTime_) maxEventExecutionTime_ = executionTime ;
    } else {
      minEventExecutionTime_ = executionTime ;
      maxEventExecutionTime_ = executionTime ;
    }
  }

  public void logResourceRequest(long executionTime, boolean cacheHit) {
    resourceRequestCounter_++ ;
    if(resourceRequestCounter_ > 0) {
      sumResourceExecutionTime_  += executionTime;
      if(cacheHit) cacheHitCounter_++ ;
      if(executionTime < minResourceExecutionTime_) minResourceExecutionTime_ = executionTime ;
      if(executionTime > maxResourceExecutionTime_) maxResourceExecutionTime_ = executionTime ;
    } else {
      minResourceExecutionTime_ = executionTime ;
      maxResourceExecutionTime_ = executionTime ;
    }
  }
}