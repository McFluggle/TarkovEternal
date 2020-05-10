public class Task
{
  private String taskCode;
  private String taskDescription;

  public Task(String taskCode, String taskDescription)
  {
    this.taskCode = taskCode;
    this.taskDescription = taskDescription;
  }

  public String getTaskCode()
  {
    return taskCode;
  }

  public String getTaskDescription()
  {
    return taskDescription;
  }

  public void setTaskDescription(String taskDescription)
  {
    this.taskDescription = taskDescription;
  }
}
