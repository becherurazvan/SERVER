package GCM;

/**
 * Created by rbech on 2/10/2016.
 */
public class GCMmessenger {

    public final static String apiKey = "AIzaSyA297StHOIwom6jK-CuW3YSobYBLqFHo7g";
    public static Sender sender;

    public GCMmessenger() {
        sender = new Sender(apiKey);
    }


    public static void sendSimpleNotification(String msg, String title, String receiver, boolean notification) {

        Message message;

        if (notification) {
            message = new Message.Builder()
                    .addData("message", msg)
                    .addData("title", title)
                    .addData("bannerNotification", "true")
                    .build();
        } else {
            message = new Message.Builder()
                    .addData("message", msg)
                    .addData("title", title)
                    .build();
        }

        try {
            Result result = sender.send(message, receiver, 1);
            System.out.println("Result:" + result.getErrorCodeName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendBacklogUpdateRequest(String receiver){
        Message message = new Message.Builder()
                .addData("message","BACKLOG_UPDATE_AVAILABLE")
                .build();
        try {
            Result result = sender.send(message, receiver, 1);
            System.out.println("Result:" + result.getErrorCodeName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void sendMessage(Message msg, String receiver) {
        try {
            Result result = sender.send(msg, receiver, 1);
            System.out.println("Result:" + result.getErrorCodeName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
