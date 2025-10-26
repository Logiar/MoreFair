package de.kaliburg.morefair.game.ladder.model.generation.steps;

import de.kaliburg.morefair.game.ladder.model.LadderEntity;
import de.kaliburg.morefair.game.ladder.model.LadderType;
import de.kaliburg.morefair.game.ladder.model.generation.LadderGenerationContext;
import de.kaliburg.morefair.game.ladder.model.generation.LadderGenerationStep;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class CreateLaddersStep implements LadderGenerationStep {

  @Override
  public List<LadderEntity> apply(List<LadderEntity> ladders, LadderGenerationContext context) {
    List<LadderEntity> result = new ArrayList<>();

    for (int i = 1; i <= context.getAssholeLadderNumber() + 1; i++) {
      LadderEntity ladder = LadderEntity.builder()
          .roundId(context.getRoundEntity().getId())
          .types(EnumSet.noneOf(LadderType.class))
          .number(i)
          .scaling(i)
          .basePointsToPromote(BigInteger.ZERO)
          .build();

      result.add(ladder);
    }
    return result;
  }
}
