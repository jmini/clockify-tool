package fr.jmini.clockify;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import fr.jmini.clockify.model.WeekHolder;

class ExportTest {

    @Test
    void testParse() throws Exception {
        WeekHolder w2022_11 = new WeekHolder(2022, 11);
        assertThat(Export.parse(IllegalArgumentException::new, "--test", w2022_11.getName())).isEqualTo(w2022_11);

        WeekHolder w2022_02 = new WeekHolder(2022, 01);
        assertThat(Export.parse(IllegalArgumentException::new, "--test", w2022_02.getName())).isEqualTo(w2022_02);
    }

    @Test
    void testDefaultToWeek() throws Exception {
        DateProvider dateProvider = () -> LocalDate.now()
                .withYear(2022)
                .withMonth(7)
                .withDayOfMonth(21);
        WeekHolder w2022_29 = new WeekHolder(2022, 29);
        assertThat(Export.defaultToWeek(dateProvider)).isEqualTo(w2022_29);
    }

    @Test
    void testCalculateFromToWithFromAfterTo() throws Exception {
        WeekHolder w2022_11 = new WeekHolder(2022, 11);
        WeekHolder w2021_45 = new WeekHolder(2021, 45);
        assertThrows(IllegalArgumentException.class, () -> Export.calculateFromTo(IllegalArgumentException::new, w2022_11, w2021_45));
    }

    @Test
    void testCalculateFromToWithSameValue() throws Exception {
        WeekHolder w2022_11 = new WeekHolder(2022, 11);
        assertThat(Export.calculateFromTo(IllegalArgumentException::new, w2022_11, w2022_11)).containsExactly(w2022_11);
    }

    @Test
    void testCalculateFromTo() throws Exception {
        WeekHolder w2022_11 = new WeekHolder(2022, 11);
        WeekHolder w2022_12 = new WeekHolder(2022, 12);
        WeekHolder w2022_13 = new WeekHolder(2022, 13);
        WeekHolder w2022_14 = new WeekHolder(2022, 14);
        WeekHolder w2022_15 = new WeekHolder(2022, 15);
        assertThat(Export.calculateFromTo(IllegalArgumentException::new, w2022_11, w2022_15)).containsExactly(w2022_11, w2022_12, w2022_13, w2022_14, w2022_15);
    }

    @Test
    void testConvertDateTime() throws Exception {
        assertThat(Export.convertDatetime(new WeekHolder(2022, 11))).isEqualTo("2022-03-14T00:00:00Z");
        assertThat(Export.convertDatetime(new WeekHolder(2021, 52))).isEqualTo("2021-12-27T00:00:00Z");

        assertThat(Export.convertDatetime(new WeekHolder(2022, 01))).isEqualTo("2022-01-03T00:00:00Z");
        assertThat(Export.convertDatetime(new WeekHolder(2021, 52).next())).isEqualTo("2022-01-03T00:00:00Z");
    }

}
