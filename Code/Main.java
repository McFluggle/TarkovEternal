
import java.util.*;

import java.io.*;

public class Main {
  static ArrayList<Task> Tasks = new ArrayList<Task>();

  static ArrayList<Command> allCommands = new ArrayList<Command>(
      Arrays.asList(new Command("get", "<type> <code>", 2), new Command("add", "<type>", 1),
       new Command("exit", "", 0), new Command("delete", "<type> <code>", 2)));

  public static void main(String[] args) throws IOException
  {
    boolean exitCommandGiven = false;
    
    HashMap<String, Command> commandDictionary = new HashMap<String, Command>();
    for (Command cmd : allCommands)
    {
      commandDictionary.put(cmd.getName(), cmd);
    }

    loadTasks();

    while (exitCommandGiven == false)
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
              String codeToGet = splitInput[2];
              HandleGet(typeToGet, codeToGet);
              break;
            case "add":
              String typeToAdd = splitInput[1];
              HandleAdd(typeToAdd);
              break;
            case "exit":
              exitCommandGiven = true;
              break;
            case "delete":
              String typeToDelete = splitInput[1];
              String codeToDelete = splitInput[2];
              HandleDelete(typeToDelete, codeToDelete);
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
      else 
      {
        System.out.println("Command " + inputCommand + " not recognised");
      }
      saveTasks();
    }
  }

  public static void saveTasks() throws IOException
  {
    FileWriter tasksSaveFileWriter = new FileWriter("tasks.csv");

    for (Task tsk : Tasks)
    {
      tasksSaveFileWriter.append(tsk.getTaskCode() + "," + tsk.getTaskDescription() + "\n");
    }

    tasksSaveFileWriter.flush();
    tasksSaveFileWriter.close();
  }

  public static void loadTasks() throws IOException
  {
    BufferedReader tasksSaveFileReader = new BufferedReader(new FileReader("tasks.csv"));
    String row;

    while ((row = tasksSaveFileReader.readLine()) != null)
    {
      String[] dataLine = row.split(",");
      Tasks.add(new Task(dataLine[0], dataLine[1]));
    }

    tasksSaveFileReader.close();
  }

  private static void HandleAdd(String typeToAdd)
  {
    switch (typeToAdd)
    {
      case "task":
        try 
        {
          AddTask();
        } 
        catch (IOException e) 
        {
          System.out.println("An error when attempting to read the data stream, please try again");
        }
        break;
      default:
        System.out.println("Type " + typeToAdd + " unrecognised, accepted types are: task, player");
        break;
    }
  }

  private static void HandleGet(String typeToGet, String code)
  {
    switch (typeToGet)
    {
      case "task":
        switch (code)
        {
          case "all":
            GetAllTasks();
            break;
          default:
            GetTask(code);
            break;
        }
        break;
      default:
        System.out.println("Type " + typeToGet + " unrecognised, accepted types are: task, player");
        break;
    }
  }

  private static void HandleDelete(String typeToDelete, String code)
  {
    switch (typeToDelete)
    {
      case "task":
        try
        {
          DeleteTask(code);
        }
        catch (IOException e) 
        {
          System.out.println("An error when attempting to read the data stream, please try again");
        }
        break;
      default:
        break;
    }
  }

  private static void GetAllTasks()
  {
    for (Task task : Tasks)
    {
      System.out.println("Task code: " + task.getTaskCode());
      System.out.println("Task points: " + TaskCodeUtility.TaskCodeToPoint(task.getTaskCode()));
      System.out.println("Task Description: " + task.getTaskDescription() + "\n");
      
    }
  }

  private static void GetTask(String taskCode)
  {
    if (TaskCodeUtility.IsValidCode(taskCode) == false)
    {
      System.out.println("Invalid task code " + taskCode);
      return;
    }

    for (Task task : Tasks)
    {
      if (task.getTaskCode().equals(taskCode))
      {
        System.out.println("Task code: " + taskCode);
        System.out.println("Task points: " + TaskCodeUtility.TaskCodeToPoint(taskCode));
        System.out.println("Task Description: " + task.getTaskDescription());
        return;
      }
    }
    System.out.println("No task with code " + taskCode + " was found");
  }

  private static void AddTask() throws IOException
  {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("Task code: ");
    String taskCode = reader.readLine();

    if (TaskCodeUtility.IsValidCode(taskCode))
    {
      System.out.print("Task Description: ");
      String taskDescription = reader.readLine();
      Tasks.add(new Task(taskCode, taskDescription));
      System.out.println("Operation successful, task " + taskCode + " created");
    }
    else 
    {
      System.out.println("Operation failed, invalid task code: " + taskCode);
    }
  }

  private static void DeleteTask(String taskCode) throws IOException
  {
    if (TaskCodeUtility.IsValidCode(taskCode) == false)
    {
      System.out.println("Invalid task code " + taskCode);
      return;
    }

    for (Task task : Tasks)
    {
      if (task.getTaskCode().equals(taskCode))
      {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Are you sure you want to delete the following task? This is a permenant action. (Y/N)");
        GetTask(taskCode);
        String response = reader.readLine();
        response = response.toUpperCase();
        switch (response)
        {
          case "Y":
            Tasks.remove(task);
            System.out.println("Task " + taskCode + " removed");
            return;
          default:
            System.out.println("Task " + taskCode + " not deleted");
        }
      }
    }
    System.out.println("No task with code " + taskCode + " was found");
  }
}