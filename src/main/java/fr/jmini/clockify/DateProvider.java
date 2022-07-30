package fr.jmini.clockify;

import java.time.LocalDate;

@FunctionalInterface
public interface DateProvider {

    LocalDate get();
}
