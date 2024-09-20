public class Task {


    //Задача написать запрос в SQL

    SELECT
    rp.familyName,
    rp.givenName,
    rp.middleName,
    rp.birthDate,
    pd.contactRelationship
            FROM
    HPPersonGeneric AS emp
            JOIN
    HPPersonDependant AS pd ON emp.sysId = pd.HPPersonGenericSysId
            JOIN
    HPPersonGeneric AS rp ON pd.HPRelatedPersonSysId = rp.sysId
            WHERE
    emp.personId = 'test';


    //Задача 1

    import java.sql.Date;
import java.util.Calendar;

    public class InsuranceDateCalculator {

        public Date getNextSendDate(Date requestDate) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(requestDate);

            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH);

            Date[] possibleSendDates = {
                    getDate(currentYear, currentMonth, 1),
                    getDate(currentYear, currentMonth, 10),
                    getDate(currentYear, currentMonth, 20)
            };

            for (Date sendDate : possibleSendDates) {
                if (sendDate.after(requestDate)) {

                    return getVacCheck(sendDate);
                }
            }

            currentMonth = (currentMonth + 1) % 12;
            currentYear += (currentMonth == 0) ? 1 : 0;

            possibleSendDates = new Date[]{
                    getDate(currentYear, currentMonth, 1),
                    getDate(currentYear, currentMonth, 10),
                    getDate(currentYear, currentMonth, 20)
            };

            for (Date sendDate : possibleSendDates) {

                return getVacCheck(sendDate);
            }

            return null;
        }

        private Date getDate(int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day, 18, 0, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return new Date(calendar.getTimeInMillis());
        }

        public Date getVacCheck(Date modDate) {

            while (!isWorkingDay(modDate)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(modDate);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                modDate = new Date(calendar.getTimeInMillis());
            }
            return modDate;
        }

        private boolean isWorkingDay(Date date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY;
        }
    }

    //Задача 2

    import java.math.BigDecimal;
import java.math.RoundingMode;

    public class NumberToWords {

        private static final String[] units = {
                "", "один", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять",
                "десять", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать", "пятнадцать",
                "шестнадцать", "семнадцать", "восемнадцать", "девятнадцать"
        };

        private static final String[] tens = {
                "", "", "двадцать", "тридцать", "сорок", "пятьдесят", "шестьдесят", "семьдесят",
                "восемьдесят", "девяносто"
        };

        private static final String[] hundreds = {
                "", "сто", "двести", "триста", "четыреста", "пятьсот", "шестьсот", "семьсот",
                "восемьсот", "девятьсот"
        };

        private static final String[] thousands = {
                "", "тысяча", "две тысячи", "три тысячи", "четыре тысячи", "пять тысяч",
                "шесть тысяч", "семь тысяч", "восемь тысяч", "девять тысяч"
        };

        public static String numberToWords(BigDecimal value) {
            if (value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(new BigDecimal("99999.99")) > 0) {
                throw new IllegalArgumentException("Value must be between 0 and 99999.99");
            }

            value = value.setScale(2, RoundingMode.HALF_UP);
            String[] parts = value.toString().split("\\.");
            int wholePart = Integer.parseInt(parts[0]);
            int fractionalPart = Integer.parseInt(parts[1]);

            StringBuilder words = new StringBuilder();

            int thousand = wholePart / 1000;
            if (thousand > 0) {
                words.append(thousands[thousand]).append(" ");
            }

            int hundred = (wholePart % 1000) / 100;
            if (hundred > 0) {
                words.append(hundreds[hundred]).append(" ");
            }

            int ten = (wholePart % 100) / 10;
            int unit = wholePart % 10;

            if (ten == 1) {
                words.append(units[ten * 10 + unit]).append(" ");
            } else {
                if (ten > 0) {
                    words.append(tens[ten]).append(" ");
                }
                if (unit > 0) {
                    words.append(units[unit]).append(" ");
                }
            }

            words.append("рублей");

            if (fractionalPart > 0) {
                words.append(" ").append(fractionalPart).append(" копеек");
            }

            return words.toString().trim();
        }
    }
}
