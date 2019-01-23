package shyn.zyot.mytravels.utils;

public class MyString {

    /**
     * If s is empty, return true
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        if (s == null || s.trim().length() == 0) return true;
        return false;
    }

    /**
     * Is s is not empty, return true
     *
     * @param s
     * @return
     */
    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    /**
     * Returns signed money text with thousand comma.
     *
     * @param amount
     * @return
     */
    public static String getMoneyText(double amount) {
        return String.format("%,.2f", amount).replace(".00", "");
    }
}
