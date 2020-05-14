import java.util.*;
import java.util.regex.Pattern;

public class TaskCodeUtility
{
    public static String GetDifficultyCode(String taskCode)
    {
        int separatorIndex = FindIndexOfSeparator(taskCode);
        return taskCode.substring(separatorIndex + 1, separatorIndex + 3);
    }

    public static String GetWeekCode(String taskCode)
    {
        int separatorIndex = FindIndexOfSeparator(taskCode);
        return taskCode.substring(0, separatorIndex);
    }

    public static String GetIndexCode(String taskCode)
    {
        int separatorIndex = FindIndexOfSeparator(taskCode);
        return taskCode.substring(separatorIndex + 3, taskCode.length());
    }

    private static int FindIndexOfSeparator(String taskCode)
    {
        int i;
        for(i = 0; i < taskCode.length(); i++)
        {
            if (taskCode.charAt(i) == '-')
            {
                return i;
            }
        }
        return -1;
    }

    public static boolean IsValidCode(String taskCode)
    {
        if (FindIndexOfSeparator(taskCode) == -1)
        {
            return false;
        }

        if (!Pattern.matches("[A-Z][0-9]", GetWeekCode(taskCode)))
        {
            return false;
        }

        if (!difficultyCodes.containsKey(GetDifficultyCode(taskCode)))
        {
            return false;
        }

        if (!Pattern.matches("[0-9]+", GetIndexCode(taskCode)))
        {
            return false;
        }

        return true;
    }

    static public final String currentWeekCode = "W0";

    static public HashMap<String, Integer> difficultyCodes = new HashMap<>();
    static 
    {
        difficultyCodes.put("EA", 2);
        difficultyCodes.put("MD", 4);
        difficultyCodes.put("HD", 8);
        difficultyCodes.put("EX", 14);
    }

    static public int TaskCodeToPoint(String taskCode)
    {
        String difficultyCode = GetDifficultyCode(taskCode);
        if (difficultyCodes.containsKey(difficultyCode))
        {
            return difficultyCodes.get(difficultyCode);
        }
        return 0;
    }
}