package de.kaliburg.morefair.game.ladder.model.generation.steps;

import de.kaliburg.morefair.game.ladder.model.LadderEntity;
import de.kaliburg.morefair.game.ladder.model.LadderType;
import de.kaliburg.morefair.game.ladder.model.generation.LadderGenerationContext;
import de.kaliburg.morefair.game.ladder.model.generation.LadderGenerationStep;
import de.kaliburg.morefair.game.round.model.type.RoundType;
import de.kaliburg.morefair.utils.RandomUtils;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * An abstract implementation of {@link LadderGenerationStep}, providing base functionality for
 * generating and modifying ladder entities by applying weights to ladder types. Subclasses can
 * provide specific behavior by overriding the provided methods.
 */
public abstract class AbstractLadderTypeStep implements LadderGenerationStep {

  /**
   * Retrieves the weights associated with different ladder types. These weights are used to
   * influence ladder generation and behavior in the game. The map's keys represent the ladder
   * types, and the values represent the associated weights as doubles.
   *
   * @return a map containing ladder types as keys and their corresponding weights as values
   */
  public abstract Map<LadderType, Double> getWeights();

  @Override
  public List<LadderEntity> apply(List<LadderEntity> ladders,
      LadderGenerationContext context) {

    Map<LadderType, Double> weights = new EnumMap<>(getWeights());
    for (RoundType roundType : context.getRoundEntity().getTypes().stream().sorted().toList()) {
      handleRoundTypes(roundType, weights);
    }

    for (LadderEntity ladder : ladders) {
      LadderType ladderType = RandomUtils.rollFromLookupTable(weights);
      ladder.getTypes().add(ladderType);
    }

    return ladders;
  }

  /**
   * Handles adjustments or custom behavior for processing specific round types and their associated
   * ladder type weights. This method is designed to be overridden by subclasses to modify the
   * default behavior.
   *
   * @param roundType the type of the current round being processed
   * @param weights   the mapping of ladder types to their corresponding weights, which can be
   *                  adjusted
   */
  protected void handleRoundTypes(RoundType roundType, Map<LadderType, Double> weights) {
    // Do nothing by default
  }
}
