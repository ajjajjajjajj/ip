package app.chatbot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.command.AddCommand;
import app.command.Command;
import app.command.CommandNotFoundException;
import app.command.DeleteCommand;
import app.command.ExitCommand;
import app.command.ListCommand;
import app.command.MarkAsDoneCommand;
import app.command.MarkAsUndoneCommand;
import app.task.TaskTypes;


/**
 * Contains methods for parsing user input into the command line,
 * and converting input into executable Commands.
 */
public class Parser {

    /**
     * Splits a String line of input into its command (the first word), and its subsequent arguments.
     * By default, the key "Command" maps to the name of the command input (the first word).
     * <br>
     * The name of the Command is also a key to its default arguments;
     * for eg: "delete 1" creates a mapping of "delete" -> "1".
     * <br>
     * In the case of adding a new event, the name of the command is mapped to its description;
     * for eg: "event" -> "attend wedding"
     *
     * @param input arguments to be split
     * @return map a mapping of each argument name to its value
     */
    private static Map<String, String> splitArgs(String input) {
        // returns a Map mapping name of arg to arg value based on the raw format
        // raw format according to task is /<name> <value>

        Map<String, String> map = new HashMap<>();
        String[] args = input.split(" /");

        // process main command
        String[] mainCommand = args[0].split(" ", 2);
        map.put("Command", mainCommand[0]);
        String desc = mainCommand.length == 1 ? "" : mainCommand[1].stripTrailing();
        map.put(mainCommand[0], desc); // 0 refers to name of command, 1 refers to its additional desc

        // process additional args, if present
        if (args.length > 1) {
            List<String> addtlArgs = Arrays.asList(args).subList(1, args.length);
            for (String arg : addtlArgs) {
                String[] s = arg.split(" ", 2);
                String name = s[0];
                String value = s.length == 1 ? "" : s[1].stripTrailing();
                map.put(name, value);
            }
        }
        return map;
    }

    /**
     * Parses an untreated string from the command line (entered by the user)
     * and returns a Command to be executed. If the command is not recognised, throws a
     * CommandNotFoundException.
     *
     * @param input untreated String entered into command line by user.
     * @return an executeable Command containing info given by user.
     * @throws CommandNotFoundException
     */
    public static Command parse(String input) throws CommandNotFoundException {
        Map<String, String> argValues = Parser.splitArgs(input);
        String command = argValues.get("Command");
        switch (command) {
        case "list":
            return new ListCommand();
        case "mark":
            return new MarkAsDoneCommand(argValues.get(command)); // pass in index to command
        case "unmark":
            return new MarkAsUndoneCommand(argValues.get(command)); // pass in index to command
        case "delete":
            return new DeleteCommand(argValues.get(command)); // pass in index to command
        case "bye":
            return new ExitCommand();
        default:
            if (TaskTypes.inputToTask.getValue().containsKey(command)) { // check if command is to add a task
                String desc = argValues.get(command);
                argValues.put("Description", desc); // change the for description from the name of Command
                return new AddCommand(TaskTypes.inputToTask.getValue().get(command), argValues);
            }
            throw new CommandNotFoundException("I'm sorry, I don't recognise this command :/");
        }
    }
}
