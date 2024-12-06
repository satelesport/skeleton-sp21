package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static gitlet.Utils.*;
import gitlet.Commit;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    public static final File OBJECT_DIR = join(GITLET_DIR, "objects");
    public static final File REFS_DIR = join(GITLET_DIR, "refs");

    public static final File HEADS_DIR = join(REFS_DIR, "heads");

    /*
     *   .gitlet
     *      |--objects
     *      |     |--commit and blob
     *      |--refs
     *      |    |--heads
     *      |         |--master
     *      |         |--other branch
     *      |--HEAD
     *      |--addstage
     *      |--removestage
     */

    public static boolean isCreate(){
        return GITLET_DIR.exists();
    }

    public static void init(){
        if(isCreate()){
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        GITLET_DIR.mkdir();
        OBJECT_DIR.mkdir();
        REFS_DIR.mkdir();
        HEADS_DIR.mkdir();

        Commit initCommit = new Commit();
        initCommit.saveCommit();

        Head h = new Head(initCommit.getID());
        h.saveHead(GITLET_DIR, "HEAD");

        Head master = new Head(initCommit.getID());
        master.saveHead(HEADS_DIR, "master");

        AddStage addStage = new AddStage();
        addStage.saveAddStage();

        RemoveStage removeStage = new RemoveStage();
        removeStage.saveRemoveStage();
    }

    /*
        1.create Blob b
        2.add b into addstage
        3.if b is in removestage, remove it from removestage
        4.if b is identical to the current commit, remove it from addstage
        5.save the addstage and removestage
     */
    public static void add(String filePath){
        File f = join(filePath);
        if(!f.exists()){
            System.out.println("File does not exist.");
            System.exit(0);
        }

        Blob b = new Blob(f.getName(),readContents(f),filePath);

        AddStage addstage = AddStage.readAddStage();
        addstage.add(b);
        addstage.saveAddStage();
    }

    /*
        1.find the nearest parent
        2.put all parent's BlobID into newCommit
        3.change newCommit BlobID according to addstage and removestage
        (addstage does not need to rewrite, but removestage need to delete)
        4.save newCommit, clear the stage
        5.HEAD point to newCommit and save
     */
    public static void commit(String message){
        if(message.isEmpty()){
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }

        AddStage addstage = AddStage.readAddStage();
        RemoveStage removestage = RemoveStage.readRemoveStage();
        int size = 0;
        for(String key : addstage.getAddStage().keySet()){
            size++;
        }
        for(String key : removestage.getRemoveStage().keySet()){
            size++;
        }
        if(size == 0){
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }

        Head h = Head.readHead(Repository.GITLET_DIR, "HEAD");
        Commit parentCommit = Commit.readCommit(h.getPointTo());

        Commit newCommit = new Commit(message, parentCommit);
        newCommit.saveCommit();

        h.changePointTo(newCommit.getID());
        h.saveHead(GITLET_DIR, "HEAD");
    }

    /*
        1.if the file is in addstage, remove it
        2.if the file is in the currentCommit, add it to the removestage,
          no need to remove it from currentCommit for now
        3.only care about the file if it is in the currentCommit
        4.save the addstage and removestage if changed
     */
    public static void rm(String filePath){
        File f = join(filePath);
        AddStage addstage = AddStage.readAddStage();
        Map<String, String> addstageMap = addstage.getAddStage();
        RemoveStage removestage = RemoveStage.readRemoveStage();
        Map<String, String> removestageMap = removestage.getRemoveStage();

        Head h = Head.readHead(Repository.GITLET_DIR, "HEAD");
        Commit currentCommit = Commit.readCommit(h.getPointTo());

        if(addstageMap.containsKey(filePath) || currentCommit.getBlobID().containsKey(filePath)){
            addstageMap.remove(filePath);

            if(currentCommit.getBlobID().containsKey(filePath)){
                if(currentCommit.getBlobID().get(filePath) != null){
                    removestageMap.put(filePath, currentCommit.getBlobID().get(filePath));
                }
            }

            addstage.saveAddStage();
            removestage.saveRemoveStage();
            f.delete();
        }
        else{
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
    }
}
