package de.kaliburg.morefair.game.ladder.model.generation.steps;

import de.kaliburg.morefair.game.ladder.model.LadderType;
import de.kaliburg.morefair.game.round.model.type.RoundType;
import java.util.EnumMap;
import java.util.Map;


/**
 * The SizeTypeStep class is an implementation of the AbstractLadderTypeStep used for generating and
 * managing ladder entities. This class provides specific logic for assigning weights to different
 * ladder types and adapting their behavior based on the round type. The purpose of this
 * implementation is to define the size-related behavior and influence the ladder generation process
 * accordingly.
 *
 * <p>The getWeights method assigns default weights to each LadderType to determine the initial
 * distribution during ladder generation. It applies higher weights to certain ladder types like
 * SMALL and BIG while assigning lower weights to other types.
 *
 * <p>The handleRoundTypes method customizes the ladder weights for specific RoundType modes,
 * modifying the weight mappings to reflect the desired round behavior. For example, the FAST round
 * type focuses more on TINY ladders with doubled weights while excluding BIG, GIGANTIC, and DEFAULT
 * types. The SLOW round type gives preference to the GIGANTIC type with amplified weights and
 * reduces emphasis on smaller types like TINY and SMALL. For CHAOS rounds, all ladder types are
 * normalized with equal weights across the board.
 *
 * <p>This class integrates into the ladder generation step sequence and works in conjunction with
 * the provided context and round types to adaptively influence the ladder outputs.
 */
public class SizeTypeStep extends AbstractLadderTypeStep {

  @Override
  public Map<LadderType, Double> getWeights() {
    Map<LadderType, Double> weights = new EnumMap<>(LadderType.class);
    weights.put(LadderType.TINY, 1.d);
    weights.put(LadderType.SMALL, 20.d);
    weights.put(LadderType.BIG, 20.d);
    weights.put(LadderType.GIGANTIC, 1.d);
    weights.put(LadderType.DEFAULT, 50.d);
    return weights;
  }


  @Override
  protected void handleRoundTypes(RoundType roundType, Map<LadderType, Double> weights) {
    switch (roundType) {
      case FAST -> {
        weights.computeIfPresent(LadderType.TINY, (k, v) -> v * 2);
        weights.put(LadderType.BIG, 0.d);
        weights.put(LadderType.GIGANTIC, 0.d);
        weights.put(LadderType.DEFAULT, 0.d);
      }
      case CHAOS -> {
        weights.put(LadderType.TINY, 1.d);
        weights.put(LadderType.SMALL, 1.d);
        weights.put(LadderType.BIG, 1.d);
        weights.put(LadderType.GIGANTIC, 1.d);
        weights.put(LadderType.DEFAULT, 1.d);
      }
      case SLOW -> {
        weights.put(LadderType.TINY, 0.d);
        weights.put(LadderType.SMALL, 0.d);
        weights.computeIfPresent(LadderType.DEFAULT, (k, v) -> v / 5);
        weights.computeIfPresent(LadderType.GIGANTIC, (k, v) -> v * 2);
      }
      default -> {
        // do nothing
      }
    }

    super.handleRoundTypes(roundType, weights);
  }
}
