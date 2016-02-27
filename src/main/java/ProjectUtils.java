import Scrum.Task;
import Scrum.UserStory;
import com.google.api.services.script.Script;
import com.google.api.services.script.model.ExecutionRequest;
import com.google.api.services.script.model.Operation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by rbech on 2/17/2016.
 */
public class ProjectUtils {


    public static final String SCRIPT_ID = "M-U-JJbsD3ADEzkqkBzkAYUWzQRt5QScL";
    public static final String READ_DOCUMENT_FUNCTION = "readDocument";

    public static ArrayList<UserStory> readUserStories(String txt) {
        ArrayList<String> splitted = new ArrayList<>(Arrays.asList(txt.split("@UserStory")));




        ArrayList<UserStory> stories = new ArrayList<>();

        System.out.println(splitted.size() + " USER STORIES DETECTED");
        for (String s : splitted) {
            ArrayList<String> userStoryInfo = new ArrayList<>(Arrays.asList(s.split("\\r?\\n")));
            String description = "";
            ArrayList<Task> tasks = new ArrayList<>();
            for (String str : userStoryInfo) {

                if (!str.contains("@")&&str.trim().length()>0) {{
                    description = description + "\n" + str;
                }

                }
            }

            UserStory story = new UserStory(description);
            tasks = getTasksFromString(s, story.getId());
            if (tasks != null)
                story.addTasks(tasks);
            stories.add(story);


        }

        return stories;
    }


    public static ArrayList<Task> getTasksFromString(String s, String userStoryId) { // format: @Task id=1, points=10, title= askdaskdlasjdlkasjd
        ArrayList<Task> tasks = new ArrayList<>();
        if (!s.contains("@Task"))
            return null;
        ArrayList<String> tasksInfo = new ArrayList<>(Arrays.asList(s.split("\\r?\\n")));
        for (String str : tasksInfo) {
            String taskTitle = "";
            int taskPoints;
            if (str.contains("@Task")) {
                String[] splittedTask = str.replace("@Task", "").trim().split(",");
                taskPoints = Integer.parseInt(splittedTask[0].replaceAll("\\s", "").replace("points=", ""));
                for (int i = 1; i < splittedTask.length; i++) {
                    taskTitle = taskTitle + splittedTask[i].replace("description=", "").replace("title =", "");
                }
                tasks.add(new Task(taskTitle, taskPoints, userStoryId));
            }
        }
        return tasks;
    }


    public static String readDocument(String documentId, Script scriptService) throws IOException {
        List<Object> params = new ArrayList<>();
        params.add(documentId);

        ExecutionRequest request = new ExecutionRequest()
                .setParameters(params)
                .setFunction(READ_DOCUMENT_FUNCTION);

        Operation op = scriptService.scripts().run(SCRIPT_ID, request).execute();


        if (op.getError() != null) {
            // The API executed, but the script returned an error.
            Map<String, Object> detail = op.getError().getDetails().get(0);
            System.err.println(
                    "Script error! Message: " + detail.get("errorMessage"));
            return null;
        } else {
            // Here, the function returns an array of strings, so the
            // result must be cast into a Java List<String>.
            String documentText = (String) op.getResponse().get("result");
            return documentText;

        }
    }

}
