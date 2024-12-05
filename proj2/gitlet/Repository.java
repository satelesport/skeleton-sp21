package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    public static final File ADDSTAGE = join(GITLET_DIR, "addstage");
    public static final File REMOVESTAGE = join(GITLET_DIR, "removestage");

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
        ADDSTAGE.mkdir();
        REMOVESTAGE.mkdir();
        HEADS_DIR.mkdir();

        Commit initCommit = new Commit();
        initCommit.saveCommit();

        Head h = new Head(initCommit.getID());
        h.saveHead(GITLET_DIR, "HEAD");

        Head master = new Head(initCommit.getID());
        master.saveHead(HEADS_DIR, "master");
    }
}
