package de.kaliburg.morefair.game.ladder.model.generation.steps;

import de.kaliburg.morefair.game.ladder.model.LadderEntity;
import de.kaliburg.morefair.game.ladder.model.generation.LadderGenerationContext;
import de.kaliburg.morefair.game.ladder.model.generation.LadderGenerationStep;
import java.util.List;

public class ContextPopulationStep implements LadderGenerationStep {

  @Override
  public List<LadderEntity> apply(List<LadderEntity> ladders, LadderGenerationContext context) {
    // Could be generated here instead of the LadderGeneration Step
    context.setAssholeLadderNumber(context.getRoundEntity().getAssholeLadderNumber());

    return ladders;
  }
}
