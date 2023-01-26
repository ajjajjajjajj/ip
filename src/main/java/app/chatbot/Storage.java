package chatbot;

import task.Task;
import task.TaskList;
import task.TaskType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

import java.io.File;

import java.io.BufferedReader;
import java.io.FileReader;

import java.io.BufferedWriter;
import java.io.FileWriter;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

public class Storage {
    public static final String SEPARATOR_REGEX = " \\| ";
    public static final String SEPARATOR = " | ";
    public static final String SUB_SEPARATOR = ":";
    private Path path;
    private final File file;

    Storage(Path path) throws IOException {
        this.path = path;
        this.file = new File(path.toString());

        // creates storage directory or data if they don't exist
        this.file.getParentFile().mkdirs();
        if (!Files.exists(path)) {
            this.file.createNewFile();
        }
    }

    /** Loads a single line in storage into a given task.TaskList.
     *
     *  It is assumed that a line in storage follows the format specified here:
     *  <task symbol> | <isDone> | <desc> | <addtl-arg1>:<values> | <addtl-arg2>:<value> ...
     *
     * For example, a project meeting task.Event from 1pm to 3pm marked done:
     * E | 1 | project meeting | from:1pm | to:3pm
     *
     */
    public int loadIntoTaskList(TaskList tl) throws Exception {


        try {
            BufferedReader br = new BufferedReader(new FileReader(this.file));
            String line = br.readLine();
            int totalTaskCounter = 0;
            int totalSuccess = 0;
            while (line != null) {
                totalTaskCounter++;
                List<String> args = Arrays.asList(line.split(SEPARATOR_REGEX));

                // get task type
                String symbol = args.get(0);
                TaskType.Type taskType = TaskType.symbolToTask.get(symbol);

                // get arg map
                Map<String,String> argValues = new HashMap<>();
                boolean isDone = args.get(1).equals("1");
                argValues.put("Description", args.get(2));

                // remaining named arguments
                if (args.size() > 3) {
                    for (String pair : args.subList(3,args.size())) {
                        String[] split = pair.split(SUB_SEPARATOR,2);
                        argValues.put(split[0], split[1]);
                    }
                }

                try {
                    if (isDone) {
                        tl.addDoneTask(taskType, argValues);
                    } else {
                        tl.addTask(taskType, argValues);
                    }
                    totalSuccess++;
                } catch (Exception e) {
                    throw new Exception("task.Task number " + totalTaskCounter + " failed to load.");
                }
                line = br.readLine();
            }
            return totalSuccess;
        } catch (Exception e) {
            throw e;
        }
    }

    public int saveToStorage(TaskList tl) throws Exception {
        BufferedWriter br = new BufferedWriter(new FileWriter(this.file));
        for (Task task : tl.getAllTasks()) {
            String data = task.asDataFormat();
            br.write(data);
            br.newLine();
        }
        br.close();
        return 1;
    }

    public static class InvalidStorageException extends Exception{
        InvalidStorageException(String msg) {
            super(msg);
        }
    }
}