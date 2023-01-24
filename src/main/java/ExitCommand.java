public class ExitCommand extends Command {

    ExitCommand() {
        super();
        this.isExit = true;
        this.isSave = false;
    }
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.reply("Alright, goodbye to you too!");
    }
}
