import java.nio.file.Path;
import java.util.List;

public class FileListMessage extends Message {
    private List<String> files;

    public FileListMessage(List<String> files) {
        this.files = files;
    }

    public List<String> getFiles() {
        return files;
    }
}
