package de.kaliburg.morefair.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class that provides random-related utility methods. Not using a cryptographically secure
 * random number generator.
 */
@Slf4j
public class RandomUtils {

  /**
   * Rolls and randomly selects an item from a weighted lookup table. Each item in the map
   * represents a potential outcome and its associated weight (probability). The probability of each
   * item being selected is proportional to its weight relative to the total weight of all entries
   * in the map.
   *
   * @param <T>         The type of object to be selected from the lookup table.
   * @param lookupTable A map where the keys represent items to be rolled for, and the values
   *                    represent their corresponding weights. Weights must be positive and
   *                    non-zero.
   * @return A randomly selected item from the lookup table based on the provided weights.
   * @throws IllegalArgumentException if the lookup table is null, empty, or contains invalid
   *                                  weights.
   */
  public static <T> T rollFromLookupTable(Map<T, Double> lookupTable) {
    validateWeightMap(lookupTable);

    Map<Double, T> inverseLookupTable = createInverseLookupTable(lookupTable);
    return randomSelectItemFromInverseLookupTable(inverseLookupTable);
  }

  private static <T> Map<Double, T> createInverseLookupTable(Map<T, Double> map) {
    Map<Double, T> inverseLookupTable = new HashMap<>();
    if (map == null || map.isEmpty()) {
      return inverseLookupTable;
    }

    double currentWeight = 0;
    for (Map.Entry<T, Double> entry : map.entrySet()) {
      validateWeightEntry(entry.getKey(), entry.getValue());
      if (entry.getValue() <= 0) {
        continue;
      }

      currentWeight += entry.getValue();
      inverseLookupTable.put(currentWeight, entry.getKey());
    }
    return inverseLookupTable;
  }

  private static <T> void validateWeightMap(Map<T, Double> map) {
    if (map == null || map.isEmpty()) {
      throw new IllegalArgumentException("Weight map cannot be null or empty");
    }
  }

  private static <T> void validateWeightEntry(T key, Double weight) {
    if (weight == null || weight < 0) {
      throw new IllegalArgumentException(
          "Weight map cannot contain null values or negative weights"
      );
    }
  }

  private static <T> T randomSelectItemFromInverseLookupTable(Map<Double, T> inverseLookupTable) {
    double totalWeight = inverseLookupTable.keySet().stream()
        .max(Comparator.naturalOrder())
        .orElseThrow(() -> new IllegalArgumentException(
            "Failed to select total Weight; probably because of invalid weights"
        ));
    double randomNumber = ThreadLocalRandom.current().nextDouble(totalWeight);

    // Needing to extract the entry first because if we map inside the optional,
    // we will not accept null as an answer
    Entry<Double, T> result = inverseLookupTable.entrySet().stream()
        .sorted(Entry.comparingByKey())
        .filter(entry -> entry.getKey() > randomNumber)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Failed to select item from weight table"));
    return result.getValue();
  }
}
