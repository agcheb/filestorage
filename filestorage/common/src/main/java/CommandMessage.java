
public class CommandMessage extends Message {
    private int cmd;
    private Object[] attachment;

    public static final int AUTH_OK = 87989434;
    public static final int DOWNLOAD_FILE = 12232334;
    public static final int FILES_LIST_REQUEST = 1685674;

    public CommandMessage(int cmd, Object... attachment) {
        this.cmd = cmd;
        this.attachment = attachment;
    }

    public int getCmd() {
        return cmd;
    }

    public Object[] getAttachment() {
        return attachment;
    }
}
