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

/**
 * Tue, May 27, 2003 .
 * @author: Tuan Nguyen
 * @version: $Id: PortletRequestMonitorData.java,v 1.2 2004/05/06 12:19:00 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class PortletRequestMonitorData {

  /**
   * Ignore 5 first request to make sure the jvm optimize the class.
   */
  private static final int REQUESTS_TO_SKIP = -5;

  /**
   * Min range.
   */
  private final long minRange;

  /**
   * Max range.
   */
  private final long maxRange;

  /**
   * Action request counter.
   */
  private int actionRequestCounter;

  /**
   * Min action execution time.
   */
  private long minActionExecutionTime;

  /**
   * Max action execution time.
   */
  private long maxActionExecutionTime;

  /**
   * Total action execution time.
   */
  private long sumActionExecutionTime = 0;

  /**
   * Render request counter.
   */
  private int renderRequestCounter;

  /**
   * Min render execution time.
   */
  private long minRenderExecutionTime;

  /**
   * Max render execution time.
   */
  private long maxRenderExecutionTime;

  /**
   * Total render execution time.
   */
  private long sumRenderExecutionTime = 0;

  /**
   * Event request counter.
   */
  private int eventRequestCounter;

  /**
   * Min event execution time.
   */
  private long minEventExecutionTime;

  /**
   * Max event execution time.
   */
  private long maxEventExecutionTime;

  /**
   * Total event execution time.
   */
  private long sumEventExecutionTime = 0;

  /**
   * Resource request counter.
   */
  private int resourceRequestCounter;

  /**
   * Min resource execution time.
   */
  private long minResourceExecutionTime;

  /**
   * Max resource execution time.
   */
  private long maxResourceExecutionTime;

  /**
   * Total resource execution time.
   */
  private long sumResourceExecutionTime = 0;

  /**
   * Cache hit counter.
   */
  private int cacheHitCounter;

  /**
   * @param minRange min range
   * @param maxRange max range
   */
  public PortletRequestMonitorData(final long minRange, final long maxRange) {
    this.minRange = minRange;
    this.maxRange = maxRange;

    this.actionRequestCounter = REQUESTS_TO_SKIP; // ignore 5 first request to make sure the jvm
                                  // optimize the class
    this.minActionExecutionTime = 0;
    this.maxActionExecutionTime = 0;

    this.renderRequestCounter = REQUESTS_TO_SKIP; // ignore 5 first request to make sure the jvm
                                  // optimize the class
    this.minRenderExecutionTime = 0;
    this.maxRenderExecutionTime = 0;

    this.eventRequestCounter = REQUESTS_TO_SKIP; // ignore 5 first request to make sure the jvm
                                // optimize the class
    this.minEventExecutionTime = 0;
    this.maxEventExecutionTime = 0;

    this.resourceRequestCounter = REQUESTS_TO_SKIP; // ignore 5 first request to make sure the
                                    // jvm optimize the class
    this.minResourceExecutionTime = 0;
    this.maxResourceExecutionTime = 0;

    this.cacheHitCounter = 0;
  }

  /**
   * @return min range
   */
  public final long minRange() {
    return minRange;
  }

  /**
   * @return max range
   */
  public final long maxRange() {
    return maxRange;
  }

  /**
   * @return action request counter
   */
  public final int getActionRequestCounter() {
    return actionRequestCounter;
  }

  /**
   * @return render request counter
   */
  public final int getRenderRequestCounter() {
    return renderRequestCounter;
  }

  /**
   * @return event request counter
   */
  public final int getEventRequestCounter() {
    return eventRequestCounter;
  }

  /**
   * @return resource request counter
   */
  public final int getResourceRequestCounter() {
    return resourceRequestCounter;
  }

  /**
   * @return cache hit counter
   */
  public final int getCacheHitCounter() {
    return cacheHitCounter;
  }

  /**
   * @return min action execution time
   */
  public final long getMinActionExecutionTime() {
    return minActionExecutionTime;
  }

  /**
   * @return min render execution time
   */
  public final long getMinRenderExecutionTime() {
    return minRenderExecutionTime;
  }

  /**
   * @return min event execution time
   */
  public final long getMinEventExecutionTime() {
    return minEventExecutionTime;
  }

  /**
   * @return min resource execution time
   */
  public final long getMinResourceExecutionTime() {
    return minResourceExecutionTime;
  }

  /**
   * @return max action execution time
   */
  public final long getMaxActionExecutionTime() {
    return maxActionExecutionTime;
  }

  /**
   * @return max render execution time
   */
  public final long getMaxRenderExecutionTime() {
    return maxRenderExecutionTime;
  }

  /**
   * @return max event execution time
   */
  public final long getMaxEventExecutionTime() {
    return maxEventExecutionTime;
  }

  /**
   * @return max resource execution time
   */
  public final long getMaxResourceExecutionTime() {
    return maxResourceExecutionTime;
  }

  /**
   * @return average action execution time
   */
  public final long getAvgActionExecutionTime() {
    if (actionRequestCounter <= 0)
      return 0;
    return sumActionExecutionTime / actionRequestCounter;
  }

  /**
   * @return average render execution time
   */
  public final long getAvgRenderExecutionTime() {
    if (renderRequestCounter <= 0)
      return 0;
    return sumRenderExecutionTime / renderRequestCounter;
  }

  /**
   * @return average event execution time
   */
  public final long getAvgEventExecutionTime() {
    if (eventRequestCounter <= 0)
      return 0;
    return sumEventExecutionTime / eventRequestCounter;
  }

  /**
   * @return average resource execution time
   */
  public final long getAvgResourceExecutionTime() {
    if (resourceRequestCounter <= 0)
      return 0;
    return sumResourceExecutionTime / resourceRequestCounter;
  }

  /**
   * @return action execution time
   */
  public final long sumActionExecutionTime() {
    return sumActionExecutionTime;
  }

  /**
   * @return render execution time
   */
  public final long sumRenderExecutionTime() {
    return sumRenderExecutionTime;
  }

  /**
   * @return event execution time
   */
  public final long sumEventExecutionTime() {
    return sumEventExecutionTime;
  }

  /**
   * @return resource execution time
   */
  public final long sumResourceExecutionTime() {
    return sumResourceExecutionTime;
  }

  /**
   * @param executionTime execution time
   */
  public final void logActionRequest(final long executionTime) {
    actionRequestCounter++;
    if (actionRequestCounter > 0) {
      sumActionExecutionTime += executionTime;
      if (executionTime < minActionExecutionTime)
        minActionExecutionTime = executionTime;
      if (executionTime > maxActionExecutionTime)
        maxActionExecutionTime = executionTime;
    } else {
      minActionExecutionTime = executionTime;
      maxActionExecutionTime = executionTime;
    }
  }

  /**
   * @param executionTime execution time
   * @param cacheHit cache hit
   */
  public final void logRenderRequest(final long executionTime, final boolean cacheHit) {
    renderRequestCounter++;
    if (renderRequestCounter > 0) {
      sumRenderExecutionTime += executionTime;
      if (cacheHit)
        cacheHitCounter++;
      if (executionTime < minRenderExecutionTime)
        minRenderExecutionTime = executionTime;
      if (executionTime > maxRenderExecutionTime)
        maxRenderExecutionTime = executionTime;
    } else {
      minRenderExecutionTime = executionTime;
      maxRenderExecutionTime = executionTime;
    }
  }

  /**
   * @param executionTime execution time
   */
  public final void logEventRequest(final long executionTime) {
    eventRequestCounter++;
    if (eventRequestCounter > 0) {
      sumEventExecutionTime += executionTime;
      if (executionTime < minEventExecutionTime)
        minEventExecutionTime = executionTime;
      if (executionTime > maxEventExecutionTime)
        maxEventExecutionTime = executionTime;
    } else {
      minEventExecutionTime = executionTime;
      maxEventExecutionTime = executionTime;
    }
  }

  /**
   * @param executionTime execution time
   * @param cacheHit cache hit
   */
  public final void logResourceRequest(final long executionTime, final boolean cacheHit) {
    resourceRequestCounter++;
    if (resourceRequestCounter > 0) {
      sumResourceExecutionTime += executionTime;
      if (cacheHit)
        cacheHitCounter++;
      if (executionTime < minResourceExecutionTime)
        minResourceExecutionTime = executionTime;
      if (executionTime > maxResourceExecutionTime)
        maxResourceExecutionTime = executionTime;
    } else {
      minResourceExecutionTime = executionTime;
      maxResourceExecutionTime = executionTime;
    }
  }
}
