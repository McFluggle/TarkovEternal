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
}