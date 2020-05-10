import java.util.*;
import java.io.*;

public class Main {
  static ArrayList<Task> Tasks = new ArrayList<Task>();

  static ArrayList<Command> allCommands = new ArrayList<Command>(
      Arrays.asList(new Command("get", "<type> <code>"), new Command("add", "<type>"),
       new Command("exit", ""), new Command("delete", "<type> <code>"), new Command("recover", "<type> <code>"),
       new Command("edit", "<type> <code>"), new Command("modify", "<type> <code>")));

  public static void main(String[] args) throws IOException 
  {
    //Remove throws from main function and deal with it internally (Don't want program to stop if one occurs)
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
            case "recover":
              String typeToRecover = splitInput[1];
              String codeToRecover = splitInput[2];
              HandleRecover(typeToRecover, codeToRecover);
              break;
            case "modify":
              String typeToModify = splitInput[1];
              String codeToModify = splitInput[2];
              HandleModify(typeToModify, codeToModify);
            default:
              break;
          }
        }
        else 
        {
          if (splitInput.length <= commandToRun.getAmountOfArguments())
          {
            System.out.print("Not enough ");
          }
          else 
          {
            System.out.print("Too many ");
          }
          System.out.println("arguments for " + commandToRun.getName() + " command, correct pattern is: " + commandToRun.getName() + " " + commandToRun.getArgumentPatternAsString());
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
    SaveTasksToFile(Tasks, StringStore.taskSaveFileLocation);
  }

  public static void loadTasks() throws IOException
  {
    Tasks = ReadTaskFile(StringStore.taskSaveFileLocation);
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
        System.out.println(String.format(StringStore.typeUnrecognisedErrorMessage, typeToAdd));
        break;
    }
  }

  private static void HandleModify(String typeToModify, String codeToModify)
  {
    switch (typeToModify)
    {
      case "task":
        try 
        {
          ModifyTask(codeToModify);
        } 
        catch (IOException e) 
        {
          System.out.println("An error when attempting to read the data stream, please try again");
        }
        break;
      default:
        System.out.println(String.format(StringStore.typeUnrecognisedErrorMessage, typeToModify));
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
          case "deleted":
            try
            {
              GetRecentlyDeletedTasks();
            }
            catch (IOException e)
            {
              System.out.println(StringStore.fileReadFailErrorMessage);
            }
            break;
          default:
            GetTask(code);
            break;
        }
        break;
      default:
        System.out.println(String.format(StringStore.typeUnrecognisedErrorMessage, typeToGet));
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

  private static void HandleRecover(String typeToRecover, String code)
  {
    switch (typeToRecover)
    {
      case "task":
        try
        {
          RecoverRecentlyDeletedTasks(code);
        }
        catch (IOException e)
        {
          System.out.println(StringStore.fileReadFailErrorMessage);
        }
        break;
      default:
        break;
    }
  }

  private static void GetAllTasks()
  {
    DisplayTasks(Tasks, true);
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
        DisplayTask(task, true);
        return;
      }
    }
    System.out.println(String.format(StringStore.taskNotFoundMessage, taskCode));
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
        DisplayTask(task, false);
        String response = reader.readLine();
        response = response.toUpperCase();
        switch (response)
        {
          case "Y":
            AddTaskToRecentlyDeleted(task);
            Tasks.remove(task);
            System.out.println("Task " + taskCode + " removed");
            return;
          default:
            System.out.println("Task " + taskCode + " not deleted");
        }
      }
    }
    System.out.println(String.format(StringStore.taskNotFoundMessage, taskCode));
  }

  private static void AddTaskToRecentlyDeleted(Task taskToAdd) throws IOException
  {
    ArrayList<Task> recentlyDeleted = ReadTaskFile(StringStore.recentlyDeletedFilePath);

    if (recentlyDeleted.size() > 4)
    {
      recentlyDeleted.remove(0);
    }

    recentlyDeleted.add(taskToAdd);

    SaveTasksToFile(recentlyDeleted, StringStore.recentlyDeletedFilePath);
  }

  private static void GetRecentlyDeletedTasks() throws IOException
  {

    ArrayList<Task> recentlyDeleted = ReadTaskFile(StringStore.recentlyDeletedFilePath);
    DisplayTasks(recentlyDeleted, false);

  }

  private static void DisplayTasks(ArrayList<Task> toDisplay, boolean displayPoints)
  {
    for (Task task : toDisplay)
    {
      DisplayTask(task, displayPoints);
      System.out.println("\n");
    }
  }

  private static void DisplayTask(Task toDisplay, boolean displayPoints)
  {
      System.out.println("Task code: " + toDisplay.getTaskCode());
      if (displayPoints) 
      { 
        System.out.println("Task points: " + TaskCodeUtility.TaskCodeToPoint(toDisplay.getTaskCode())); 
      }
      System.out.println("Task Description: " + toDisplay.getTaskDescription());
  }

  private static void RecoverRecentlyDeletedTasks(String taskCode) throws IOException
  {

    ArrayList<Task> recentlyDeleted = ReadTaskFile(StringStore.recentlyDeletedFilePath);

    boolean taskRecovered = false;

    ArrayList<Task> putBack = new ArrayList<Task>();

    for (Task task : recentlyDeleted)
    {
      if (task.getTaskCode().equals(taskCode))
      {
        taskRecovered = true;
        Tasks.add(task);
        System.out.println("Task " + taskCode + " succesfully recovered");
      }
      else 
      {
        putBack.add(task);
      }
    }

    SaveTasksToFile(putBack, StringStore.recentlyDeletedFilePath);

    if (!taskRecovered)
    {
      System.out.println(String.format(StringStore.taskNotFoundMessage, taskCode));
      System.out.println("Use the command: *get task deleted* to see a list of the recently deleted tasks");
    }
  }

  private static void ModifyTask(String code) throws IOException
  {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    for (Task task : Tasks)
    {
      if (task.getTaskCode().equals(code))
      {
        System.out.print("New task description: ");
        String newDescription = reader.readLine();

        task.setTaskDescription(newDescription);
        return;
      }
    }

    System.out.println("No task with code " + code + " was found");
  }

  //All task save files can be done with the following 2 functions
  private static void SaveTasksToFile(ArrayList<Task> toSave, String filePath) throws IOException
  {
    FileWriter tasksSaveFileWriter = new FileWriter(filePath);

    for (Task task : toSave)
    {
      tasksSaveFileWriter.append(task.getTaskCode() + "," + task.getTaskDescription() + "\n");
    }

    tasksSaveFileWriter.flush();
    tasksSaveFileWriter.close();
  }

  private static ArrayList<Task> ReadTaskFile(String filePath) throws IOException
  {
    BufferedReader TasksSaveFileReader = new BufferedReader(new FileReader(filePath));
    String row;
    ArrayList<Task> tasksRead = new ArrayList<Task>();

    while ((row = TasksSaveFileReader.readLine()) != null)
    {
      String[] dataLine = row.split(",");
      tasksRead.add(new Task(dataLine[0], dataLine[1]));
    }

    TasksSaveFileReader.close();

    return tasksRead;
  }
}