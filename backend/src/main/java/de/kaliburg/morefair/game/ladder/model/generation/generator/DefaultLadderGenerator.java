package de.kaliburg.morefair.game.ladder.model.generation.generator;

import de.kaliburg.morefair.game.ladder.model.generation.LadderGenerationStep;
import de.kaliburg.morefair.game.ladder.model.generation.LadderGenerator;
import de.kaliburg.morefair.game.ladder.model.generation.steps.ContextPopulationStep;
import de.kaliburg.morefair.game.ladder.model.generation.steps.CreateLaddersStep;
import de.kaliburg.morefair.game.ladder.model.generation.steps.ReverseScalingStep;
import de.kaliburg.morefair.game.ladder.model.generation.steps.SetupEndingStep;
import de.kaliburg.morefair.game.ladder.model.generation.steps.SizeTypeGiganticProtectionStep;
import de.kaliburg.morefair.game.ladder.model.generation.steps.SizeTypeStep;
import de.kaliburg.morefair.game.round.model.type.RoundType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultLadderGenerator implements LadderGenerator {

  @Override
  public RoundType getSpecialRoundType() {
    return RoundType.DEFAULT;
  }

  @Override
  public List<LadderGenerationStep> getGenerationSteps() {
    return List.of(
        new ContextPopulationStep(),
        new CreateLaddersStep(),
        new SetupEndingStep(),
        new ReverseScalingStep(),
        new SizeTypeStep(),
        new SizeTypeGiganticProtectionStep()
    );
  }


}
