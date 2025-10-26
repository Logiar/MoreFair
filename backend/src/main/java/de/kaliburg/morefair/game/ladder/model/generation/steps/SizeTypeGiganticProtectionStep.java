package de.kaliburg.morefair.game.ladder.model.generation.steps;

import de.kaliburg.morefair.game.ladder.model.LadderEntity;
import de.kaliburg.morefair.game.ladder.model.LadderType;
import de.kaliburg.morefair.game.ladder.model.generation.LadderGenerationContext;
import de.kaliburg.morefair.game.ladder.model.generation.LadderGenerationStep;
import java.util.List;

public class SizeTypeGiganticProtectionStep implements LadderGenerationStep {

  @Override
  public List<LadderEntity> apply(List<LadderEntity> ladders,
      LadderGenerationContext context) {
    for (int i = 0; i < ladders.size() - 1; i++) {
      LadderEntity currentLadder = ladders.get(i);
      LadderEntity nextLadder = ladders.get(i + 1);

      if (currentLadder.getTypes().contains(LadderType.GIGANTIC)) {
        nextLadder.getTypes().remove(LadderType.BIG);
      }
    }
    return ladders;
  }

}
