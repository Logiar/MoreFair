package de.kaliburg.morefair.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class RandomUtilsTest {

  @Test
  void testValidLookupTableReturnsItem() {
    // Arrange
    Map<String, Double> lookupTable = new HashMap<>();
    lookupTable.put("A", 10.0);
    lookupTable.put("B", 20.0);
    lookupTable.put("C", 30.0);

    // Act
    String result = RandomUtils.rollFromLookupTable(lookupTable);

    // Assert
    assertTrue(lookupTable.containsKey(result),
        "Result must be one of the keys from the lookup table");
  }

  @Test
  void testEmptyLookupTableThrowsException() {
    // Arrange
    Map<String, Double> lookupTable = new HashMap<>();

    // Act & Assert
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> RandomUtils.rollFromLookupTable(lookupTable)
    );

    assertEquals("Weight map cannot be null or empty", exception.getMessage());
  }

  @Test
  void testNullLookupTableThrowsException() {
    // Act & Assert
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> RandomUtils.rollFromLookupTable(null)
    );

    assertEquals("Weight map cannot be null or empty", exception.getMessage());
  }

  @Test
  void testNegativeWeightThrowsException() {
    // Arrange
    Map<String, Double> lookupTable = new HashMap<>();
    lookupTable.put("A", 10.0);
    lookupTable.put("B", -5.0);

    // Act & Assert
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> RandomUtils.rollFromLookupTable(lookupTable)
    );

    assertEquals("Weight map cannot contain null values or negative weights",
        exception.getMessage());
  }

  @Test
  void testNullWeightThrowsException() {
    // Arrange
    Map<String, Double> lookupTable = new HashMap<>();
    lookupTable.put("A", 10.0);
    lookupTable.put("B", null);

    // Act & Assert
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> RandomUtils.rollFromLookupTable(lookupTable)
    );

    assertEquals("Weight map cannot contain null values or negative weights",
        exception.getMessage());
  }

  @Test
  void testZeroWeightReturnsOtherItem() {
    // Arrange
    Map<String, Double> lookupTable = new HashMap<>();
    lookupTable.put("A", 10.0);
    lookupTable.put("B", 0.0);

    // Act & Assert
    String s = RandomUtils.rollFromLookupTable(lookupTable);

    assertThat(s).isEqualTo("A");
  }

  @Test
  void testAllZeroWeightThrowsException() {
    // Arrange
    Map<String, Double> lookupTable = new HashMap<>();
    lookupTable.put("A", 0.0);
    lookupTable.put("B", 0.0);

    // Act & Assert
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> RandomUtils.rollFromLookupTable(lookupTable)
    );

    assertThat(exception.getMessage()).isEqualTo(
        "Failed to select total Weight; probably because of invalid weights");
  }

  @Test
  public void testNullKeyReturnsNull() {
    // Arrange
    Map<String, Double> lookupTable = new HashMap<>();
    lookupTable.put(null, 10.0);

    // Act
    String s = RandomUtils.rollFromLookupTable(lookupTable);

    // Assert
    assertNull(s);
  }

  @Test
  void testSelectionsRespectWeights() {
    // Arrange
    Map<String, Double> lookupTable = new HashMap<>();
    lookupTable.put("A", 10.0);
    lookupTable.put("B", 90.0);

    int totalRolls = 1000;
    int countA = 0;
    int countB = 0;

    // Act
    for (int i = 0; i < totalRolls; i++) {
      String result = RandomUtils.rollFromLookupTable(lookupTable);
      if (result.equals("A")) {
        countA++;
      } else if (result.equals("B")) {
        countB++;
      }
    }

    // Assert
    double ratioA = (double) countA / totalRolls;
    double ratioB = (double) countB / totalRolls;

    assertTrue(ratioA > 0.05 && ratioA < 0.15,
        "Ratio A should be ~10% (±5%) but was " + (ratioA * 100) + "%");
    assertTrue(ratioB > 0.85 && ratioB < 0.95,
        "Ratio B should be ~90% (±5%) but was " + (ratioA * 100) + "%");
  }
}