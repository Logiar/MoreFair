package de.kaliburg.morefair.game.round.services;

import de.kaliburg.morefair.core.concurrency.CriticalRegion;
import de.kaliburg.morefair.game.round.model.RoundEntity;
import de.kaliburg.morefair.game.round.model.type.RoundType;
import de.kaliburg.morefair.game.season.model.SeasonEntity;
import java.util.Optional;
import java.util.Set;

public interface RoundService {

  CriticalRegion getSemaphore();

  RoundEntity getCurrentRound();

  Optional<RoundEntity> findBySeasonAndNumber(SeasonEntity currentSeason, int number);

  Optional<RoundEntity> findById(long roundId);

  void closeCurrentRound();

  RoundEntity updateCurrentRoundTypes(Set<RoundType> newTypes);

  RoundEntity save(RoundEntity round);
}
