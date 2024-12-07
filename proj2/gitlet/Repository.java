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
    public static final File COMMIT_DIR = join(OBJECT_DIR, "commitID");
    public static final File BLOB_DIR = join(OBJECT_DIR, "blobID");

    /*
     *   .gitlet
     *      |--objects
     *      |     |--commitID
     *      |     |--blobID
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
        COMMIT_DIR.mkdir();
        BLOB_DIR.mkdir();

        Commit initCommit = new Commit();
        initCommit.saveCommit();

        Head h = new Head(initCommit.getID());
        h.changeCurrentBranch("master");
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
        b.saveBlob();

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
        6.currentBranch point to newCommit and save
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

        String currentBranchName = h.getCurrentBranch();
        Head currentBranch = Head.readHead(Repository.HEADS_DIR, currentBranchName);
        currentBranch.changePointTo(newCommit.getID());
        currentBranch.saveHead(Repository.HEADS_DIR, currentBranchName);
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

    public static void log(){
        Head h = Head.readHead(Repository.GITLET_DIR, "HEAD");
        Commit currentCommit = Commit.readCommit(h.getPointTo());

        while(true){
            currentCommit.printLog();
            if(!currentCommit.hasParent()){
                break;
            }
            String PID = currentCommit.findParentID();
            currentCommit = Commit.readCommit(PID);
        }
    }

    public static void global_log(){
        List<String> commitList = plainFilenamesIn(COMMIT_DIR);
        for(String commitID : commitList){
            Commit selectCommit = Commit.readCommit(commitID);
            selectCommit.printLog();
        }
    }

    public static void find(String message){
        List<String> commitList = plainFilenamesIn(COMMIT_DIR);
        boolean f = false;
        for(String commitID : commitList){
            Commit selectCommit = Commit.readCommit(commitID);
            if(selectCommit.getMessage().equals(message)){
                System.out.println(selectCommit.getID());
                f = true;
            }
        }
        if(!f){
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
    }

    public static void status(){
        System.out.println("=== Branches ===");
        List<String> branchList = plainFilenamesIn(HEADS_DIR);

        Head h = Head.readHead(Repository.GITLET_DIR, "HEAD");
        String currentBranch = h.getCurrentBranch();

        for(String branch : branchList){
            if(branch.equals(currentBranch)){
                System.out.print("*");
            }
            System.out.println(branch);
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        AddStage addstage = AddStage.readAddStage();
        Map<String, String> addstageMap = addstage.getAddStage();
        for(String key : addstageMap.keySet()){
            File f = join(key);
            System.out.println(f.getName());
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        RemoveStage removestage = RemoveStage.readRemoveStage();
        Map<String, String> removestageMap = removestage.getRemoveStage();
        for(String key : removestageMap.keySet()){
            File f = join(key);
            System.out.println(f.getName());
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();

        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    /*
        1.check if the current commit has the file
        2.overwrite the given file with the file in commit
     */
    public static void checkout_filePath(String filePath){
        Head h = Head.readHead(Repository.GITLET_DIR, "HEAD");
        Commit currentCommit = Commit.readCommit(h.getPointTo());

        if(currentCommit.getBlobID().containsKey(filePath)){
            File f = join(filePath);
            String BlobID = currentCommit.getBlobID().get(filePath);
            Blob b = Blob.readBlob(BlobID);
            writeContents(f, b.content);
        }
        else{
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
    }

    /*
        like the one above, but the commit is from the given commitID
     */
    public static void checkout_CommitID_filePath(String commitID, String filePath){
        List<String> commitList = plainFilenamesIn(COMMIT_DIR);
        String ID = "";
        for(String s : commitList){
            if(s.startsWith(commitID)){
                ID = s;
                break;
            }
        }
        if(ID.isEmpty()){
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }

        Commit c = Commit.readCommit(ID);

        if(c.getBlobID().containsKey(filePath)){
            File f = join(filePath);
            String BlobID = c.getBlobID().get(filePath);
            Blob b = Blob.readBlob(BlobID);
            writeContents(f, b.content);
        }
        else{
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
    }


    private static boolean branchExist(String branchName){
        List<String> branchList = plainFilenamesIn(HEADS_DIR);
        if(branchList.isEmpty()) return false;
        for(String s : branchList){
            if(s.equals(branchName)){
                return true;
            }
        }
        return false;
    }

    /*
            1.check if there exists the branch
            2.if the branch is the current, return
            3.(1)if a file in old commit and in new commit, rewrite it
              (2)if a file in old commit and not in new commit, delete it
              (3)if a file not in old commit and in new commit, create it
                 !!!!but if the file already exists, throw error
            4.clear the stage, repoint the HEAD(the branch and commit)
     */
    public static void checkout_branchName(String branchName){
        if(!branchExist(branchName)){
            System.out.println("No such branch exists.");
            System.exit(0);
        }

        Head h = Head.readHead(GITLET_DIR, "HEAD");
        if(branchName.equals(h.getCurrentBranch())){
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }

        Head newBranch = Head.readHead(HEADS_DIR, branchName);
        Commit newCommit = Commit.readCommit(newBranch.getPointTo());
        Commit oldCommit = Commit.readCommit(h.getPointTo());

        Map<String, String> oldMap = oldCommit.getBlobID();
        Map<String, String> newMap = newCommit.getBlobID();

        for(String filePath : newMap.keySet()){
            if(!oldMap.containsKey(filePath)){
                File f = join(filePath);
                if(f.exists()){
                    System.out.println("here is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
        }

        for(String filePath : newMap.keySet()){
            File f = join(filePath);
            String BlobID = newMap.get(filePath);
            Blob b = Blob.readBlob(BlobID);
            writeContents(f, b.content);
        }

        for(String filePath : oldMap.keySet()){
            if(!newMap.containsKey(filePath)){
                File f = join(filePath);
                f.delete();
            }
        }

        AddStage addstage = AddStage.readAddStage();
        RemoveStage removestage = RemoveStage.readRemoveStage();
        addstage.getAddStage().clear();
        removestage.getRemoveStage().clear();
        addstage.saveAddStage();
        removestage.saveRemoveStage();

        h.changeCurrentBranch(branchName);
        h.changePointTo(newCommit.getID());
        h.saveHead(GITLET_DIR, "HEAD");
    }

    public static void branch(String branchName){
        if(branchExist(branchName)){
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }

        Head h = Head.readHead(GITLET_DIR, "HEAD");
        Head newBranch = new Head(h.getPointTo());
        newBranch.saveHead(HEADS_DIR, branchName);
    }

    public static void rm_branch(String branchName){
        if(!branchExist(branchName)){
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }

        Head h = Head.readHead(GITLET_DIR, "HEAD");
        if(h.getCurrentBranch().equals(branchName)){
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }

        File f = join(HEADS_DIR, branchName);
        f.delete();
    }
}
