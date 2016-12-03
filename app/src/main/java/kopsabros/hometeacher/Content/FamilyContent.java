package kopsabros.hometeacher.Content;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample familyName for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class FamilyContent implements Serializable {
    public static final List<Family> FAMILIES = new ArrayList<Family>();
    public static final Map<String, Family> FAMILY_MAP = new HashMap<String, Family>();

    public static void addFamily(Family family) {
        FAMILIES.add(family);
        FAMILY_MAP.put(String.valueOf(family.id), family);
    }

    public static void removeFamily(Family family) {
        FAMILIES.remove(family);
        FAMILY_MAP.remove(family.familyName);
    }

    public static String[] getFamilyNames() {
        List<String> familyNames = new ArrayList<String>();
        for (Family family : FAMILIES) {
            familyNames.add(family.familyName);
        }
        return familyNames.toArray(new String[familyNames.size()]);
    }

    public static Family getFamily(int familyID) {
        return FAMILY_MAP.get(String.valueOf(familyID));
    }

    public static class Family implements Serializable {
        private int id;
        public String familyName;
        public String phoneNumber;
        public String emailAddress;
        public String postalAddress;
        public List<Appointment> appointments;
        public List<FamilyMember> familyMembers;

        public Family(String familyName) {
            this.id = System.identityHashCode(this);
            this.familyName = familyName;
            this.familyMembers = new ArrayList<>();
            this.phoneNumber = null;
            this.emailAddress = null;
            this.postalAddress = null;
            this.appointments = null;
        }

        @Override
        public String toString() {
            return familyName;
        }

        /*
         * Family Member Methods
         */

        public void addMember(FamilyMember familyMember) {
            familyMembers.add(familyMember);
        }

        /*
         * Family Appointment Methods
         */
        public void addAppointment(int year, int month, int day, int hourOfDay, int minute) {
            if (appointments == null) {
                appointments = new ArrayList<>();
            }
            appointments.add(new Appointment(year, month, day, hourOfDay, minute, id));
        }

        public Appointment getNextAppointment() {
            if (appointments != null) {
                for (Appointment appointment :
                        appointments) {
                    if (isGoodAppointment(appointment))
                        return appointment;
                }
            }
            return null;
        }

        private boolean isGoodAppointment(Appointment appointment) {
            Calendar calendar = Calendar.getInstance();
            return calendar.get(Calendar.MONTH) + 1 <= appointment.getMonth() &&
                    calendar.get(Calendar.YEAR) <= appointment.getYear() &&
                    appointment.getCompleted() == false;
        }

        /*
         * Setters & Getters
         */

        public String getFamilyName() {
            return familyName;
        }

        public void setFamilyName(String familyName) {
            this.familyName = familyName;
            if (familyMembers != null) {
                for (FamilyMember member :
                        getMemberList()) {
                    member.setFamilyID(id);
                }
            }

            if (appointments != null) {
                for (Appointment appointment :
                        appointments) {
                    appointment.familyID = id;
                }
            }
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        public String getPostalAddress() {
            return postalAddress;
        }

        public void setPostalAddress(String postalAddress) {
            this.postalAddress = postalAddress;
        }

        public String[] getMemberNameArray() {
            if (familyMembers != null) {
                List<String> stringMembers = new ArrayList<>();
                for (FamilyMember member :
                        familyMembers) {
                    stringMembers.add(member.name);
                }
                return stringMembers.toArray(new String[stringMembers.size()]);
            } else
                return null;
        }

        public List<FamilyMember> getMemberList() {
            return familyMembers;
        }

        public boolean deleteNextAppointment() {
            if (getNextAppointment() != null) {
                deleteAppointment(getNextAppointment());
                return true;
            } else
                return false;
        }

        public String getID() {
            return String.valueOf(id);
        }

        public void setID(String id) {
            this.id = Integer.parseInt(id);
        }

        public void addAppointment(Appointment appointment) {
            if (appointments == null) {
                appointments = new ArrayList<>();
            }
            appointments.add(appointment);
        }

        public List<Appointment> getAppointmentList() {
            if (appointments != null) {
                return appointments;
            }
            return null;
        }

        public void deleteAppointment(Appointment appointment) {
            appointments.remove(appointment);
        }

        public Appointment getCurrentMonthAppointment() {
            Calendar calendar = Calendar.getInstance();
            if (appointments != null) {
                for (Appointment appointment :
                        appointments) {
                    if (calendar.get(Calendar.MONTH) + 1 <= appointment.getMonth() &&
                            calendar.get(Calendar.YEAR) <= appointment.getYear())
                        return appointment;
                }
            }
            return null;
        }

        public void removeFamilyMember(FamilyMember familyMember) {
            familyMembers.remove(familyMember);
        }
    }

    public static class Appointment implements Serializable {
        private int id;
        private int year;
        private int month;
        private int day;
        private int hourOfDay;
        private int minute;
        private boolean completed;
        private int familyID;

        public Appointment(int year, int month, int day, int hourOfDay, int minute, int familyID) {
            this.id = System.identityHashCode(this);
            this.year = year;
            this.month = month;
            this.day = day;
            this.hourOfDay = hourOfDay;
            this.minute = minute;
            this.completed = false;
            this.familyID = familyID;
        }

        public Appointment(String id, String date, String time, int familyID, int completed) {
            this.id = Integer.parseInt(id);
            String[] dateArray = date.split("/");
            month = Integer.parseInt(dateArray[0]);
            day = Integer.parseInt(dateArray[1]);
            year = Integer.parseInt(dateArray[2]);

            String[] timeArray = time.split(":|\\ ");
            if (timeArray[2].equals("PM")) {
                if (!timeArray.equals("12")) {
                    this.hourOfDay = 12 + Integer.parseInt(timeArray[0]);
                    this.minute = Integer.parseInt(timeArray[1]);
                }
            } else {
                this.hourOfDay = Integer.parseInt(timeArray[0]);
                this.minute = Integer.parseInt(timeArray[1]);
            }
            this.familyID = familyID;
            this.completed = completed == 1;
        }

        public String getDate() {
            return String.valueOf(month) + "/" + String.valueOf(day) + "/" + String.valueOf(year);
        }

        public String getTime() {
            String time;
            boolean isPM = false;
            // hour of day logic
            if (hourOfDay == 12) {
                time = String.valueOf(hourOfDay) + ":";
                isPM = true;
            } else if (hourOfDay == 0) {
                time = String.valueOf(hourOfDay + 12) + ":";
            } else if (hourOfDay > 12) {
                time = String.valueOf(hourOfDay - 12) + ":";
                isPM = true;
            } else {
                time = String.valueOf(hourOfDay) + ":";
            }
            // minute logic
            if (minute < 10) {
                time += "0" + String.valueOf(minute);
            } else {
                time += String.valueOf(minute);
            }
            // AM/PM logic
            if (isPM) {
                time += " PM";
            } else {
                time += " AM";
            }

            return time;
        }

        public boolean getCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public String getID() {
            return String.valueOf(id);
        }

        public int getFamilyID() {
            return familyID;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public void setHour(int hour) {
            this.hourOfDay = hour;
        }

        public void setId(String id) {
            this.id = Integer.parseInt(id);
        }

        public int getMinute() {
            return minute;
        }

        public int getHour() {
            return hourOfDay;
        }

        public int getYear() {
            return year;
        }

        public int getMonth() {
            return month;
        }

        public int getDay() {
            return day;
        }
    }

    public static class FamilyMember implements Serializable {
        private int id;
        private String name;
        private int familyID;
        private String phoneNumber;
        private String email;
        private String birthday;

        public FamilyMember(String name, int familyID, @Nullable String phoneNumber, @Nullable String email, @Nullable String birthday) {
            this.id = System.identityHashCode(this);
            this.name = name;
            this.familyID = familyID;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.birthday = birthday;
        }

        public FamilyMember(String id, String name, int familyID, @Nullable String phoneNumber, @Nullable String email, @Nullable String birthday) {
            this.id = Integer.parseInt(id);
            this.name = name;
            this.familyID = familyID;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.birthday = birthday;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public int getFamilyID() {
            return familyID;
        }

        public String getID() {
            return String.valueOf(id);
        }

        public void setFamilyID(int familyID) {
            this.familyID = familyID;
        }
    }
}
