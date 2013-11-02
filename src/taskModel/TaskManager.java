package taskModel;
import java.util.HashMap;

public class TaskManager
{
  private HashMap<String, Task> tasks;

  public TaskManager()
  {
    tasks = new HashMap<String, Task>();
  }

  public HashMap<String, Task> getTasksMap()
  {
    return tasks;
  }

  public Task getTask(String taskName)
  {
    return tasks.get(taskName);
  }

  /**
   * Creates a new task while also creating a new task for every named
   * dependency and adding it as a dependecy to the original task.
   *
   * If current task (taskName) exists already, it will add the deps as
   * dependencies to the task. In other words, call this with every Task you
   * need to create and even if it was created already, you will still be able
   * to add dependencies correctly. Returns the task it creates.
   */
  public Task createNewTask(String taskName, String[] deps)
  {
    Task t = createNewTask(taskName);

    Task d;
    for (String str: deps)
    {
      if (!tasks.containsKey(str))
      {
        d = new Task(str);
        tasks.put(str, d);
      }
      else
      {
        d = tasks.get(str);
      }
      t.addDependency(d);
    }

    return t;
  }
  /**
   * Creates new task that doesn't have any dependencies
   */
  public Task createNewTask(String taskName)
  {
    Task t;
    if (!tasks.containsKey(taskName))
    {
      t = new Task(taskName);
      tasks.put(taskName, t);
    }
    else
    {
      t = tasks.get(taskName);
    }

    return t;
  }
}
