public class Command 
{
    private String name;
    private String argumentPatternAsString;
    private int amountOfArguments;

    public Command(String name, String argumentPatternAsString)
    {
        this.name = name;
        this.argumentPatternAsString = argumentPatternAsString;
        if (argumentPatternAsString == "")
        {
            this.amountOfArguments = 0;
        }
        else 
        {
            this.amountOfArguments = argumentPatternAsString.split(" ").length;
        }
        
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