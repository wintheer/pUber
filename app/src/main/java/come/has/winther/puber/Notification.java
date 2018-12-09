package come.has.winther.puber;

public class Notification {

    private boolean messageWaiting;
    private String usernameWhoRequestedToilet;

    public Notification(boolean notificationWaiting, String usernameWhoSentNotification) {
        this.messageWaiting = notificationWaiting;
        this.usernameWhoRequestedToilet = usernameWhoSentNotification;
    }

    public boolean isMessageWaiting() {
        return messageWaiting;
    }

    public void setMessageWaiting(boolean messageWaiting) {
        this.messageWaiting = messageWaiting;
    }

    public String getUsernameWhoRequestedToilet() {
        return usernameWhoRequestedToilet;
    }

    public void setUsernameWhoRequestedToilet(String usernameWhoRequestedToilet) {
        this.usernameWhoRequestedToilet = usernameWhoRequestedToilet;
    }
}
