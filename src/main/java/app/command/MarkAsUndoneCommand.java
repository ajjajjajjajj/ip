package command;

import chatbot.Storage;
import chatbot.Ui;
import task.TaskList;

public class MarkAsUndoneCommand extends Command {
    private String unmarkAtIndex;

    public MarkAsUndoneCommand(String index) {
        this.isExit = false;
        this.isSave = true;
        this.unmarkAtIndex = index;
    }

    @Override
    public void execute(TaskList tl, Ui ui, Storage storage) throws Exception {
        boolean alreadyMarked;
        try {
            int i = Integer.valueOf(this.unmarkAtIndex) - 1;
            alreadyMarked = tl.unmarkDone(i);
            if (alreadyMarked) {
                ui.reply(tl.getTask(i).getDesc() + " is already undone!");
            } else {
                ui.reply("Unmarked " + tl.getTask(i).getDesc() + ".");
            }
        } catch (NumberFormatException e) {
            throw new Exception("Please specify the task by its index number.");
        } catch (IndexOutOfBoundsException e) {
            throw new Exception("Seems like this task doesn't exist.");
        }
    }
}