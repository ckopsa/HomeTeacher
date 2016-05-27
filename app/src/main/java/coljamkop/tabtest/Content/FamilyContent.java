package coljamkop.tabtest.Content;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class FamilyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Family> FAMILIES = new ArrayList<Family>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Family> FAMILY_MAP = new HashMap<String, Family>();

    private static final int COUNT = 5;

    static {
        // Add some sample items.
        addItem(createDummyItem(1, "Johnson"));
        addItem(createDummyItem(2, "Whitehead"));
        addItem(createDummyItem(3, "Austin"));
        addItem(createDummyItem(4, "Vasquez"));
        addItem(createDummyItem(5, "Storm"));
    }
    public static List<Appointment> getFamilysNextAppointment() {
        List<Appointment> temp = new ArrayList<>();
        for (Family family : FAMILIES) {
            temp.add(family.getNextAppontment());
        }
        return temp;
    }
    private static void addItem(Family item) {
        FAMILIES.add(item);
        FAMILY_MAP.put(item.name, item);
    }

    private static Family createDummyItem(int position, String familyName) {
        Family family = new Family(String.valueOf(position), familyName, makeDetails(position));
        family.addAppointment(2016, 5, 30, 8, 30);
        return family;
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Family {
        public final String name;
        public final String content;
        public final String details;
        public Deque<Appointment> appointments;

        public Family(String id, String content, String details) {
            this.name = id;
            this.content = content;
            this.details = details;
            this.appointments = null;
        }

        @Override
        public String toString() {
            return content;
        }

        public void addAppointment(int year, int month, int day, int hourOfDay, int minute) {
            if (appointments == null) {
                appointments = new ArrayDeque<>();
                appointments.addFirst(new Appointment(year, month, day, hourOfDay, minute));
            } else {
                appointments.addFirst(new Appointment(year, month, day, hourOfDay, minute));
            }
        }

        public Appointment getNextAppontment() {
            return appointments.getFirst();
        }
    }

    public static class Appointment {
        private int year;
        private int month;
        private int day;
        private int hourOfDay;
        private int minute;
        private boolean isPassed;

        public Appointment(int year, int month, int day, int hourOfDay, int minute) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.hourOfDay = hourOfDay;
            this.minute = minute;
        }

        public String getDate() {
            return String.valueOf(month) + "/" + String.valueOf(day) + "/" + String.valueOf(year);
        }

        public String getTime() {
            return String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
        }
    }
}
