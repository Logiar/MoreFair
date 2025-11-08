package de.kaliburg.morefair.game.ladder.model.generation.steps;

import de.kaliburg.morefair.game.ladder.model.LadderEntity;
import de.kaliburg.morefair.game.ladder.model.LadderType;
import de.kaliburg.morefair.game.ladder.model.generation.LadderGenerationContext;
import de.kaliburg.morefair.game.ladder.model.generation.LadderGenerationStep;
import java.util.List;

/**
 * Represents the finalization step in the ladder generation process. This step is responsible for
 * setting the types of the last two ladders in the list to specific predefined configurations:
 *
 * <p>1. For the second-to-last ladder, adds {@link LadderType#ASSHOLE} and
 * {@link LadderType#NO_AUTO}, and removes {@link LadderType#FREE_AUTO}.
 *
 * <p>2. For the last ladder, clears all existing types and
 * sets it to {@link LadderType#END}.
 *
 * <p>Implements the {@link LadderGenerationStep} interface to define specific behavior for this
 * final step in the ladder generation sequence.
 *
 * <p>The method relies on the list of {@link LadderEntity} objects and applies these changes only
 * if there are enough ladders in the list to make the modifications.
 */
public class SetupEndingStep implements LadderGenerationStep {

  @Override
  public List<LadderEntity> apply(List<LadderEntity> ladders,
      final LadderGenerationContext context) {

    int size = ladders.size();
    if (size >= 2) {
      ladders.get(ladders.size() - 2).getTypes().add(LadderType.ASSHOLE);
      ladders.get(ladders.size() - 2).getTypes().add(LadderType.NO_AUTO);
      ladders.get(ladders.size() - 2).getTypes().remove(LadderType.FREE_AUTO);
    }
    if (size >= 1) {
      ladders.get(ladders.size() - 1).getTypes().clear();
      ladders.get(ladders.size() - 1).getTypes().add(LadderType.END);
    }
    return ladders;
  }

}
