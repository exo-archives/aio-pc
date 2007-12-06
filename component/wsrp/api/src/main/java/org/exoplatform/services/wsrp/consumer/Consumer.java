package org.exoplatform.services.wsrp.consumer;

/**
 * The consumer provides access to consumer specific components.
 * 
 * @author <a href='mailto:peter.fischer@de.ibm.com'>Peter Fischer</a>
 * @author Benjamin Mestrallet
 */
public interface Consumer {

  /**
   * Get the portlet registry of the consumer.
   *
   * @return Interface to the consumer specific portlet registry
   */
  public PortletRegistry getPortletRegistry();

  /**
   * Get the portlet driver registry of the consumer.
   *
   * @return Interface to the consumer specific portlet driver registry
   */
  public PortletDriverRegistry getPortletDriverRegistry();

  /**
   * Get the producer registry of the consumer.
   *
   * @return The consumer specific producer registry
   */
  public ProducerRegistry getProducerRegistry();

  /**
   * Get the user registry of the consumer.
   *
   * @return The consumer specific user registry
   */
  public UserRegistry getUserRegistry();

  /**
   * Get the url template composer for template proccessing
   *
   * @return Interface to the consumer specific template composer
   */
  public URLTemplateComposer getTemplateComposer();

  /**
   * Get the url rewriter for consumer url-rewriting
   *
   * @return The consumer specific url rewriter
   */
  public URLRewriter getURLRewriter();

}

