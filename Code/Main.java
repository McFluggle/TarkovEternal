import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
  static ArrayList<Task> Tasks = new ArrayList<Task>();

  static ArrayList<Command> allCommands = new ArrayList<Command>(
      Arrays.asList(new Command("get", "<type> <code>", 2), new Command("add", "<type>", 1)));

  public static void main(String[] args) throws IOException
  {
    boolean exitCommandGiven = false;
    
    HashMap<String, Command> commandDictionary = new HashMap<String, Command>();
    for (Command cmd : allCommands)
    {
      commandDictionary.put(cmd.getName(), cmd);
    }

    while (!exitCommandGiven)
    {
      System.out.print("> ");
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      String userInput = reader.readLine();
      String[] splitInput = userInput.split(" ");

      String inputCommand = splitInput[0];

      if (commandDictionary.containsKey(inputCommand))
      {
        Command commandToRun = commandDictionary.get(inputCommand);
        if (splitInput.length == commandToRun.getAmountOfArguments() + 1)
        {
          switch (inputCommand)
          {
            case "get":
              String typeToGet = splitInput[1];
              String code = splitInput[2];
              HandleGet(typeToGet, code);
              break;
            default:
              break;
          }
        }
        else 
        {
          if (splitInput.length <= commandToRun.getAmountOfArguments())
          {
            System.out.println("Not enough arguments for " + commandToRun.getName() + " command, correct pattern is: " + commandToRun.getName() + " " + commandToRun.getArgumentPatternAsString());
          }
          else 
          {
            System.out.println("Too many arguments for " + commandToRun.getName() + " command, correct pattern is: " + commandToRun.getName() + " " + commandToRun.getArgumentPatternAsString());
          }
        }
      }
    }
  }

  public static void HandleGet(String typeToGet, String code)
  {
    switch (typeToGet)
    {
      case "task":
        GetTask(code);
        break;
      default:
        break;
    }
  }

  public static void GetTask(String code)
  {
    for (Task task : Tasks)
    {
      if (task.getTaskCode() == code)
      {
        System.out.println("Task code: " + code);
        System.out.println("Task points: ");
        System.out.println("Task Description: " + task.getTaskDescription());
        return;
      }
    }
    System.out.println("No task with code " + code + " was found");
  }
}
