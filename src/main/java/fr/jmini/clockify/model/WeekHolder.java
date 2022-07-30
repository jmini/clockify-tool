package fr.jmini.clockify.model;

import java.util.Comparator;

public class WeekHolder implements Comparable<WeekHolder> {

    private static final Comparator<WeekHolder> COMPARATOR = Comparator.comparingInt(WeekHolder::getYear)
            .thenComparing(WeekHolder::getWeekNumber);

    private int year;
    private int weekNumber;

    public WeekHolder(int year, int weekNumber) {
        super();
        this.year = year;
        if (weekNumber < 1 || weekNumber > 52) {
            throw new IllegalArgumentException("Unexpected week number: " + weekNumber);
        }
        this.weekNumber = weekNumber;
    }

    public String getName() {
        return String.format("%04d-%02d", year, weekNumber);
    }

    public int getYear() {
        return year;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public WeekHolder next() {
        if (weekNumber < 52) {
            return new WeekHolder(year, weekNumber + 1);
        } else {
            return new WeekHolder(year + 1, 1);
        }
    }

    public boolean isAfter(WeekHolder fromWeek) {
        return compareTo(fromWeek) < 0;
    }

    @Override
    public int compareTo(WeekHolder o) {
        return COMPARATOR.compare(this, o);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + weekNumber;
        result = prime * result + year;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WeekHolder other = (WeekHolder) obj;
        if (weekNumber != other.weekNumber)
            return false;
        if (year != other.year)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "WeekHolder [year=" + year + ", weekNumber=" + weekNumber + "]";
    }
}
