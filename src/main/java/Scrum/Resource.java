package Scrum;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

/**
 * Created by rbech on 3/14/2016.
 */
public class Resource {
    String resourceId;
    String resourceType;
    String text ="";
    int startPos;
    int endPos;

    public static String RESOURCE_US="UserStory";
    public static String RESOURCE_TASK="Task";
    public static String RESOURCE_SPRINT="Sprint";

    public Resource(String resourceId, String resourceType) {
        this.resourceId = resourceId;
        this.resourceType = resourceType;
    }



    public Resource() {
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String generateTag(){
        String tag="[rid=\'"+resourceId+"\' type=\'" + resourceType+"\']";
        return tag;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }


    @JsonIgnore
    public static ArrayList<Resource> generateResourcesFromString(String s){

        String finalString = s.toString();
        String[] array = s.split("\\["); // 3
        ArrayList<Resource> resources = new ArrayList<>();
        int lastIndex =0;
        for (int i = 1; i < array.length; i++) {
            String initial = "["+array[i];
            array[i] = array[i].split("]")[0];
            array[i] = array[i].replace("'", "");
            String[] values = array[i].split(" ");
            Resource resource = new Resource(values[0].replace("rid=", ""), values[1].replace("type=", ""));

            System.err.println(initial);
            finalString = finalString.replace(initial.split("]")[0],resource.getResourceType()).replace("]","");


            int startPos = finalString.indexOf(resource.getResourceType(),lastIndex);
            lastIndex=startPos+resource.getResourceType().length();
            int endPos = startPos+resource.getResourceType().length();
            resource.setStartPos(startPos);
            resource.setEndPos(endPos);
            resources.add(resource);

            System.out.println(resource.getStartPos() + "  " + resource.getEndPos() + "   " +finalString);

        }
        return resources;
    }


    @JsonIgnore
    public static String parseFinalText(String s){

        String finalString = s.toString();
        String[] array = s.split("\\["); // 3
        ArrayList<Resource> resources = new ArrayList<>();
        int lastIndex =0;
        for (int i = 1; i < array.length; i++) {
            String initial = "["+array[i];
            array[i] = array[i].split("]")[0];
            array[i] = array[i].replace("'", "");
            String[] values = array[i].split(" ");
            Resource resource = new Resource(values[0].replace("rid=", ""), values[1].replace("type=", ""));

            System.err.println(initial);
            finalString = finalString.replace(initial.split("]")[0],resource.getResourceType()).replace("]","");


            int startPos = finalString.indexOf(resource.getResourceType(),lastIndex);
            lastIndex=startPos+resource.getResourceType().length();
            int endPos = startPos+resource.getResourceType().length();
            resource.setStartPos(startPos);
            resource.setEndPos(endPos);
            resources.add(resource);

            System.out.println(resource.getStartPos() + "  " + resource.getEndPos() + "   " +finalString);

        }
        return finalString;
    }
}
