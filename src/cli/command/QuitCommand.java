package cli.command;

public class QuitCommand implements CLICommand{
    @Override
    public String commandName() {
        return "quit";
    }

    @Override
    public void execute(String args) {

    }
}
