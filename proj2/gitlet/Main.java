package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
import gitlet.Repository.*;

public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */

    public static void main(String[] args) {
        if(args.length == 0){  //when nothing is read
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                if(!validateNumArgs(args, 2)){
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.add(args[1]);
                break;
            case "commit":
                if(!validateNumArgs(args, 2)){
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.commit(args[1]);
                break;
            case "rm":
                if(!validateNumArgs(args, 2)){
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.rm(args[1]);
                break;
            case "log":
                Repository.log();
                break;
            case "global-log":
                Repository.global_log();
                break;
            case "find":
                if(!validateNumArgs(args, 2)){
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.find(args[1]);
                break;
            case "status":
                Repository.status();
                break;
            case "checkout":
                if(validateNumArgs(args, 3)){
                    Repository.checkout_filePath(args[2]);
                }
                else if(validateNumArgs(args, 4)){
                    Repository.checkout_CommitID_filePath(args[1],args[3]);
                }
                else if(validateNumArgs(args, 2)){
                    Repository.checkout_branchName(args[1]);
                }
                else{
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                break;
            case "branch":
                if(!validateNumArgs(args, 2)){
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.branch(args[1]);
                break;
            case "rm-branch":
                if(!validateNumArgs(args, 2)){
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.rm_branch(args[1]);
                break;
            case "reset":

                break;
            case "merge":

                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }

    public static boolean validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            return false;
        }
        else if(!Repository.isCreate()){
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        return true;
    }
}
