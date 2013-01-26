Console Component
=================

Console eases the creation of beautiful and testable command line interfaces.

It is a port from [Symfony2's Console component](https://github.com/symfony/Console).

The Application object manages the command-line application:

    import org.nanocom.console.Application;

    console = new Application();
    console.run(new ArgsInput(args));

The ``run()`` method parses the arguments and options passed on the command
line and executes the right command.

Registering a new command can easily be done via the ``register()`` method,
which returns a ``Command`` instance:

    import org.nanocom.console.Input.InputInterface;
    import org.nanocom.console.Input.InputArgument;
    import org.nanocom.console.Input.InputOption;
    import org.nanocom.console.Output.OutputInterface;

    console
        .register("ls")
        .setDefinition(new InputParameterInterface[] {
            new InputArgument('dir', InputArgument.REQUIRED, "Directory name"),
        })
        .setDescription("Displays the files in the given directory")
        .setCode(new Executable() {

            @Override
            protected int execute(InputInterface input, OutputInterface output);
                String dir = input.getArgument("dir");
                output.writeln(String.format("Dir listing for <info>%s</info>", dir));
                return 0;
            }
        })
    ;

You can also register new commands via classes.

The component provides a lot of features like output coloring, input and
output abstractions (so that you can easily unit-test your commands),
validation, automatic help messages, ...

License
-------

The license of this package can be found in the "LICENSE" file.
The MIT license allows you to do pretty much everything you want with this software.

The original license of Symfony2's component can be found in the "ORIGINAL_LICENSE" file.
