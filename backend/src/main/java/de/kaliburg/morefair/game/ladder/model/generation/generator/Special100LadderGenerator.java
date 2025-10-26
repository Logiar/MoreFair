package de.kaliburg.morefair.game.ladder.model.generation.generator;

import de.kaliburg.morefair.game.ladder.model.generation.LadderGenerationStep;
import de.kaliburg.morefair.game.ladder.model.generation.LadderGenerator;
import de.kaliburg.morefair.game.ladder.model.generation.steps.ContextPopulationStep;
import de.kaliburg.morefair.game.ladder.model.generation.steps.CreateLaddersStep;
import de.kaliburg.morefair.game.ladder.model.generation.steps.ReverseScalingStep;
import de.kaliburg.morefair.game.ladder.model.generation.steps.Special100Step;
import de.kaliburg.morefair.game.round.model.type.RoundType;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class Special100LadderGenerator implements LadderGenerator {

  @Override
  public RoundType getSpecialRoundType() {
    return RoundType.SPECIAL_100;
  }

  @Override
  public List<LadderGenerationStep> getGenerationSteps() {
    return List.of(
        new ContextPopulationStep(),
        new CreateLaddersStep(),
        new Special100Step(),
        new ReverseScalingStep()
    );
  }
}
