package fr.jmini.clockify.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class WeekHolderTest {

    @Test
    void testConstructor() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> new WeekHolder(2021, 53));
    }

    @Test
    void testSort() throws Exception {
        WeekHolder w202105 = new WeekHolder(2021, 5);
        WeekHolder w202211 = new WeekHolder(2022, 11);
        WeekHolder w202145 = new WeekHolder(2021, 45);
        List<WeekHolder> list = Arrays.asList(w202105, w202211, w202145);
        List<WeekHolder> result = list.stream()
                .sorted()
                .collect(Collectors.toList());
        assertThat(result).containsExactly(w202105, w202145, w202211);
    }

    @Test
    void testName() throws Exception {
        assertThat(new WeekHolder(2022, 3).getName())
                .as("name")
                .isEqualTo("2022-03");
        assertThat(new WeekHolder(2022, 11).getName())
                .as("name")
                .isEqualTo("2022-11");
    }
}
