package de.kaliburg.morefair.game.ladder.model.generation;

import de.kaliburg.morefair.game.ladder.model.LadderEntity;
import java.util.List;


/**
 * Represents a step in the ladder generation process, allowing for modification or preparation of
 * ladder entities based on a given context.
 *
 * <p>Implementations of this interface are expected to define specific logic for transforming or
 * processing a list of {@link LadderEntity} objects in conjunction with a
 * {@link LadderGenerationContext}.
 */
public interface LadderGenerationStep {


  /**
   * Processes a list of {@link LadderEntity} objects based on the provided
   * {@link LadderGenerationContext}, applying the specific modifications or transformations defined
   * by the implementation.
   *
   * @param ladders the list of {@link LadderEntity} objects to be processed; may be empty
   * @param context the {@link LadderGenerationContext} providing the round-specific details and
   *                additional parameters required for ladder generation or transformation
   * @return a modified list of {@link LadderEntity} objects with the applied changes
   */
  List<LadderEntity> apply(List<LadderEntity> ladders, final LadderGenerationContext context);
}
