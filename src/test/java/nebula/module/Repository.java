package nebula.module;

import java.util.List;

/**
 * Database access for pets.
 */
public interface Repository<T> {
  /**
   * List pets using start/max limits.
   *
   * @param start Start offset.
   * @param max Max number of rows.
   * @return Available pets.
   */
  List<T> list(int start, int max);

  /**
   * Find a pet by ID.
   *
   * @param id T ID.
   * @return T or null.
   */
  T findById(long id);

  /**
   * Insert a pet and returns the generated PK.
   *
   * @param pet T to insert.
   * @return Primary key.
   */
  boolean insert(T pet);

  /**
   * Update a pet and returns true if the pets was updated.
   *
   * @param pet T to update.
   * @return True if the pet was updated.
   */
  boolean update(T pet);

  /**
   * Delete a pet by ID.
   *
   * @param id T ID.
   * @return True if the pet was deleted.
   */
  boolean delete(long id);
}
