public class Command 
{
    private String name;
    private String argumentPatternAsString;
    private int amountOfArguments;

    public Command(String name, String argumentPatternAsString, int amountOfArguments)
    {
        this.name = name;
        this.argumentPatternAsString = argumentPatternAsString;
        this.amountOfArguments = amountOfArguments;
    }

    public String getName()
    {
        return name;
    }

    public String getArgumentPatternAsString()
    {
        return argumentPatternAsString;
    }

    public int getAmountOfArguments()
    {
        return amountOfArguments;
    }
}