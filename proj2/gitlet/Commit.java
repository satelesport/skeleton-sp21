package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     *
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private List<String> parentsID;
    private Date date;
    private Map<String,String> BlobID;
    private String ID;

    private String createID(){
        return Utils.sha1(dateToTimeStamp(date), message, parentsID.toString(), BlobID.toString());
    }

    private static String dateToTimeStamp(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }

    public Commit(){
        message = "initial commit";
        date = new Date(0);
        BlobID = new TreeMap<>();
        parentsID = new ArrayList<>();
        ID = createID();
    }

    public String getID(){
        return ID;
    }

    public void saveCommit(){
        File f = join(Repository.OBJECT_DIR, ID);
        writeObject(f, this);
    }

    public static Commit readCommit(String wantedID){
        File f = join(Repository.OBJECT_DIR, wantedID);
        return readObject(f, Commit.class);
    }

    public void addBlob(Blob b){
        BlobID.put(b.getID(), b.getFilePath());
    }

    public boolean checkBlob(Blob b){
        return BlobID.containsKey(b.getID());
    }
}
