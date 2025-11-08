package de.kaliburg.morefair.game.ladder.model.generation.steps.types;

import de.kaliburg.morefair.game.ladder.model.LadderType;
import de.kaliburg.morefair.game.round.model.type.RoundType;
import java.util.EnumMap;
import java.util.Map;

public class GrapesPositiveTypeStep extends AbstractWeightedLadderTypeStep {

  @Override
  public Map<LadderType, Double> getWeights() {
    Map<LadderType, Double> weights = new EnumMap<>(LadderType.class);
    weights.put(LadderType.DEFAULT, 100.d);
    return weights;
  }

  @Override
  protected void handleRoundTypes(RoundType roundType, Map<LadderType, Double> weights) {
    super.handleRoundTypes(roundType, weights);
    switch (roundType) {
      case CHAOS -> {
        weights.putIfAbsent(LadderType.BOUNTIFUL, 25.d);
        weights.putIfAbsent(LadderType.GENEROUS, 25.d);
        weights.putIfAbsent(LadderType.CONSOLATION, 25.d);
        weights.putIfAbsent(LadderType.DEFAULT, 25.d);
      }
      case RAILROAD -> {
        weights.put(LadderType.CONSOLATION, 75.d);
        weights.put(LadderType.DEFAULT, 25.d);
      }
      case FARMER -> {
        weights.put(LadderType.BOUNTIFUL, 75.d);
        weights.put(LadderType.DEFAULT, 25.d);
      }
      case RACE -> {
        weights.put(LadderType.GENEROUS, 75.d);
        weights.put(LadderType.DEFAULT, 25.d);
      }
      default -> {
        // do nothing
      }
    }
  }
}
