package org.exoplatform.services.wsrp.consumer;

import java.util.Iterator;

/**
 * Defines a registry which can be used to administer
 * producer objects.
 *
 * @author Stephan Laertz
 * @author Benjamin Mestrallet
 */
public interface ProducerRegistry {

  /**
   * Add a producer to the registry
   *
   * @param producer The producer to add
   */
  public void addProducer(Producer producer);

  /**
   * Get the producer for the given URL
   *
   * @param id The ID of the producer
   * @return The producer with the given ID
   */
  public Producer getProducer(String id);

  /**
   * Get all producer in the registry
   *
   * @return Iterator with all producers
   */
  public Iterator getAllProducers();

  /**
   * Remove the producer with the given ID from the registry
   *
   * @param id The ID of the producer
   * @return The producer which had been mapped to this id or
   *         null if no producer was found with this id
   */
  public Producer removeProducer(String id);

  /**
   * Remove all producer objects from the registry
   */
  public void removeAllProducers() throws Exception ;

  /**
   * Check if a producer with the given ID exists in the registry.
   *
   * @param id The ID of the producer
   * @return True if producer exists with this ID
   */
  public boolean existsProducer(String id);

  /**
   * Create a new producer instance according to the implementation
   *
   * @return return a new Producer instance
   */
  public Producer createProducerInstance() ;

  /**
   * Get the last time that a producer is added or removed
   *
   * @return return long value
   */
  public long getLastModifiedTime() ;
}
