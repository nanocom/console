Console
=======

Console eases the creation of beautiful and testable command line interfaces.
It is a port from Symfony2's Console component.

The Application object manages the CLI application:

    import com.nanocom.console.Application;

    public static void main(String[] args) {
        Application console = new Application(args);
        console.run();
    }

The ``run()`` method parses the arguments and options passed on the command
line and executes the right command.

Registering a new command can easily be done via the ``register()`` method,
which returns a ``Command`` instance:

    import com.nanocom.console.Input.InputInterface;
    import com.nanocom.console.Input.InputArgument;
    import com.nanocom.console.Input.InputOption;
    import com.nanocom.console.Output.OutputInterface;

    console
        .register("ls")
        .setDefinition(Arrays.asList((Object)
            new InputArgument("dir", InputArgument.REQUIRED, "Directory name"),
        ))
        .setDescription("Displays the files in the given directory")
        .setCode(function (InputInterface input, OutputInterface output) {
            dir = input.getArgument("dir");

            output.writeln(String.format("Dir listing for <info>%s</info>", dir));
        })
    ;

You can also register new commands via classes.

The component provides a lot of features like output coloring, input and
output abstractions (so that you can easily unit-test your commands),
validation, automatic help messages, ...