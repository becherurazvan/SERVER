package ScrumTests;

import Scrum.Resource;

import java.util.ArrayList;

/**
 * Created by rbech on 3/15/2016.
 */
public class Parsetest {
    public static void main(String[] args) {

        String s = "has finished working on [rid='US_1-T_25' type='TASK'] and on [rid='US_1-T_25' type='TASK']";

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

        System.out.println(finalString);


    }


}
