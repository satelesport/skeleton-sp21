package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
import gitlet.Repository;

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
                // TODO: handle the `init` command
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                break;
            case "comment":

                break;
            case "rm":

                break;
            case "log":

                break;
            case "global-log":

                break;
            case "find":

                break;
            case "status":

                break;
            case "checkout":

                break;
            case "branch":

                break;
            case "rm-branch":

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
}
