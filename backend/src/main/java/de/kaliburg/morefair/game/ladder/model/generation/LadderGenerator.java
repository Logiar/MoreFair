package de.kaliburg.morefair.game.ladder.model.generation;

import de.kaliburg.morefair.game.ladder.model.LadderEntity;
import de.kaliburg.morefair.game.ladder.model.LadderType;
import de.kaliburg.morefair.game.round.model.type.RoundType;
import java.util.List;

public interface LadderGenerator {

  RoundType getSpecialRoundType();

  List<LadderGenerationStep> getGenerationSteps();

  default List<LadderEntity> generateLadders(LadderGenerationContext context) {
    List<LadderEntity> result = List.of();
    for (LadderGenerationStep step : getGenerationSteps()) {
      result = step.apply(result, context);
    }

    return cleanup(result);
  }

  default List<LadderEntity> cleanup(List<LadderEntity> ladders) {
    // Removing DEFAULT from ladders with modifiers
    ladders.stream().filter(l -> l.getTypes().size() > 1)
        .forEach(l -> l.getTypes().remove(LadderType.DEFAULT));

    // Filling everything that doesn't have a type with DEFAULT
    ladders.stream().filter(l -> l.getTypes().isEmpty())
        .forEach(l -> l.getTypes().add(LadderType.DEFAULT));

    return ladders;
  }
}
