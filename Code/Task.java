public class Task
{
  private String taskCode;
  private String taskDescription;

  public Task(String _taskCode, String _taskDescription)
  {
    this.taskCode = _taskCode;
    this.taskDescription = _taskDescription;
  }

  public String getTaskCode()
  {
    return taskCode;
  }

  public String getTaskDescription()
  {
    return taskDescription;
  }
}
