/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.command;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import static org.apache.commons.lang3.StringUtils.*;
import org.apache.commons.lang3.SystemUtils;
import org.nanocom.console.Application;
import org.nanocom.console.exception.LogicException;
import org.nanocom.console.helper.HelperSet;
import org.nanocom.console.input.InputArgument;
import org.nanocom.console.input.InputDefinition;
import org.nanocom.console.input.InputInterface;
import org.nanocom.console.input.InputOption;
import org.nanocom.console.output.OutputInterface;

/**
 * Base class for all commands.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class Command extends Executable {

    private Application application;
    private String name;
    private List<String> aliases;
    private InputDefinition definition;
    private String help;
    private String description;
    private Boolean ignoreValidationErrors;
    private Boolean applicationDefinitionMerged;
    private Executable code;
    private String synopsis;
    private HelperSet helperSet;

    /**
     * Constructor.
     *
     * @param name The name of the command
     *
     * @throws LogicException When the command name is empty
     */
    public Command(String name) {
        init(name);
    }

    /**
     * Constructor.
     *
     * @throws LogicException When the command name is empty
     */
    public Command() {
        init(null);
    }

    private void init(String name) {
        definition = new InputDefinition();
        ignoreValidationErrors = false;
        applicationDefinitionMerged = false;
        aliases = new ArrayList<String>();

        if (null != name) {
            setName(name);
        }

        configure();

        if (null == this.name || this.name.isEmpty()) {
            throw new LogicException("The command name cannot be empty.");
        }
    }

    /**
     * Ignores validation errors.
     *
     * This is mainly useful for the help command.
     */
    public void ignoreValidationErrors() {
        ignoreValidationErrors = true;
    }

    /**
     * Sets the application instance for this command.
     *
     * @param application An Application instance
     */
    public void setApplication(Application application) {
        this.application = application;
        if (null != application) {
            setHelperSet(application.getHelperSet());
        } else {
            helperSet = null;
        }
    }

    /**
     * Sets the helper set.
     *
     * @param helperSet A HelperSet instance
     */
    public void setHelperSet(HelperSet helperSet) {
        this.helperSet = helperSet;
    }

    /**
     * Gets the helper set.
     *
     * @return A HelperSet instance
     */
    public HelperSet getHelperSet() {
        return helperSet;
    }

    /**
     * Gets the application instance for this command.
     *
     * @return An Application instance
     */
    public Application getApplication() {
        return application;
    }

    /**
     * Checks whether the command is enabled or not in the current environment.
     *
     * Override this to check for x or y and return false if the command can not
     * run properly under the current conditions.
     *
     * @return
     */
    public boolean isEnabled() {
        return true;
    }

    /**
     * Configures the current command.
     */
    protected void configure() throws RuntimeException
    {
    }

    /**
     * Executes the current command.
     *
     * This method is not abstract because you can use this class
     * as a concrete class. In this case, instead of defining the
     * execute() method, you set the code to execute by passing
     * an Executable instance to the setCode() method.
     *
     * @param input  An InputInterface instance
     * @param output An OutputInterface instance
     *
     * @return 0 if everything went fine, or an error code
     *
     * @throws LogicException When this abstract method is not implemented
     * @see    setCode()
     */
    @Override
    protected int execute(InputInterface input, OutputInterface output) {
        throw new LogicException("You must override the execute() method in the concrete command class.");
    }

    /**
     * Interacts with the user.
     *
     * @param input  An InputInterface instance
     * @param output An OutputInterface instance
     */
    protected void interact(InputInterface input, OutputInterface output) {
    }

    /**
     * Initializes the command just after the input has been validated.
     *
     * This is mainly useful when a lot of commands extends one main command
     * where some things need to be initialized based on the input arguments and options.
     *
     * @param input  An InputInterface instance
     * @param output An OutputInterface instance
     */
    protected void initialize(InputInterface input, OutputInterface output) {
    }

    /**
     * Runs the command.
     *
     * The code to execute is either defined directly with the
     * setCode() method or by overriding the execute() method
     * in a sub-class.
     *
     * @param input  An InputInterface instance
     * @param output An OutputInterface instance
     *
     * @see setCode()
     * @see execute()
     */
    public int run(InputInterface input, OutputInterface output) {
        // Force the creation of the synopsis before the merge with the app definition
        getSynopsis();

        // Add the application arguments and options
        mergeApplicationDefinition();

        // Bind the input against the command specific arguments/options
        try {
            input.bind(definition);
        } catch (RuntimeException e) {
            if (!ignoreValidationErrors) {
                throw e;
            }
        }

        initialize(input, output);

        if (input.isInteractive()) {
            interact(input, output);
        }

        input.validate();

        if (null != code) {
            return code.execute(input, output);
        }

        return execute(input, output);
    }


    /**
     * Sets the code to execute when running this command.
     *
     * If this method is used, it overrides the code defined
     * in the execute() method.
     *
     * @param Executable code An Executable instance
     *
     * @return Command The current instance
     *
     * @see #execute
     */
    public Command setCode(Executable code) {
        this.code = code;

        return this;
    }

    /**
     * Merges the application definition with the command definition.
     */
    private void mergeApplicationDefinition() {
        if (null == application || true == applicationDefinitionMerged) {
            return;
        }

        List<InputArgument> currentArguments = new ArrayList<InputArgument>(definition.getArguments().values());
        List<InputArgument> applicationArguments = new ArrayList<InputArgument>(application.getDefinition().getArguments().values());
        definition.setArguments(applicationArguments);
        definition.addArguments(currentArguments);

        List<InputOption> applicationOptions = new ArrayList<InputOption>(application.getDefinition().getOptions().values());
        definition.addOptions(applicationOptions);

        applicationDefinitionMerged = true;
    }

    /**
     * Sets an array of argument and option instances.
     *
     * @param definition An array of argument and option instances or a definition instance
     *
     * @return The current instance
     */
    @SuppressWarnings("unchecked")
    public Command setDefinition(Object definition) {
        if (definition instanceof InputDefinition) {
            this.definition = (InputDefinition) definition;
        } else if (definition instanceof List) {
            this.definition.setDefinition((List<Object>) definition);
        }

        applicationDefinitionMerged = false;

        return this;
    }

    /**
     * Gets the InputDefinition attached to this Command.
     *
     * @return An InputDefinition instance
     */
    public InputDefinition getDefinition() {
        return definition;
    }

    /**
     * Gets the InputDefinition to be used to create XML and Text representations of this Command.
     *
     * Can be overridden to provide the original command representation when it would otherwise
     * be changed by merging with the application InputDefinition.
     *
     * @return An InputDefinition instance
     */
    protected InputDefinition getNativeDefinition() {
        return getDefinition();
    }

    /**
     * Adds an argument.
     *
     * @param name         The argument name
     * @param mode         The argument mode: InputArgument::REQUIRED or InputArgument::OPTIONAL
     * @param description  A description text
     * @param defaultValue The default value (for InputArgument::OPTIONAL mode only)
     */
    public Command addArgument(String name, int mode, String description, Object defaultValue) {
        definition.addArgument(new InputArgument(name, mode, description, defaultValue));
        return this;
    }

    /**
     * Adds an argument.
     *
     * @param name        The argument name
     * @param mode        The argument mode: InputArgument.REQUIRED or InputArgument.OPTIONAL
     * @param description A description text
     */
    public Command addArgument(String name, int mode, String description) {
        return addArgument(name, mode, description, null);
    }

    /**
     * Adds an argument.
     *
     * @param name The argument name
     * @param mode The argument mode: InputArgument.REQUIRED or InputArgument.OPTIONAL
     */
    public Command addArgument(String name, int mode) {
        return addArgument(name, mode, EMPTY, null);
    }

    /**
     * Adds an argument.
     *
     * @param name The argument name
     */
    public Command addArgument(String name) {
        return addArgument(name, InputArgument.OPTIONAL, EMPTY, null);
    }

    /**
     * Adds an option.
     *
     * @param name         The option name
     * @param shortcut     The shortcut (can be null)
     * @param mode         The option mode: One of the InputOption.VALUE_* constants
     * @param description  A description text
     * @param defaultValue The default value (must be null for InputOption.VALUE_REQUIRED or InputOption.VALUE_NONE)
     */
    public void addOption(String name, String shortcut, int mode, String description, Object defaultValue) {
        definition.addOption(new InputOption(name, shortcut, mode, description, defaultValue));
    }

    /**
     * Adds an option.
     *
     * @param name        The option name
     * @param shortcut    The shortcut (can be null)
     * @param mode        The option mode: One of the InputOption.VALUE_* constants
     * @param description A description text
     */
    public void addOption(String name, String shortcut, int mode, String description) {
        addOption(name, shortcut, mode, description, null);
    }

    /**
     * Adds an option.
     *
     * @param name     The option name
     * @param shortcut The shortcut (can be null)
     * @param mode     The option mode: One of the InputOption.VALUE_* constants
     */
    public void addOption(String name, String shortcut, int mode) {
        addOption(name, shortcut, mode, EMPTY);
    }

    /**
     * Adds an option.
     *
     * @param name     The option name
     * @param shortcut The shortcut (can be null)
     */
    public void addOption(String name, String shortcut) {
        addOption(name, shortcut, InputOption.VALUE_NONE);
    }

    /**
     * Adds an option.
     *
     * @param name The option name
     */
    public void addOption(String name) {
        addOption(name, null);
    }

    /**
     * Sets the name of the command.
     *
     * This method can set both the namespace and the name if
     * you separate them by a colon (:)
     *
     *     command.setName("foo:bar");
     *
     * @param name The command name
     *
     * @return The current instance
     *
     * @throws IllegalArgumentException When command name given is empty
     */
    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    /**
     * Returns the command name.
     *
     * @return The command name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the description for the command.
     *
     * @param description The description for the command
     *
     * @return The current instance
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the description for the command.
     *
     * @return The description for the command
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the help for the command.
     *
     * @param help The help for the command
     *
     * @return The current instance
     */
    public void setHelp(String help) {
        this.help = help;
    }

    /**
     * Returns the help for the command.
     *
     * @return The help for the command
     */
    public String getHelp() {
        return help;
    }

    /**
     * Returns the processed help for the command replacing the %command.name% pattern
     * with the real value dynamically.
     *
     * @return The processed help for the command
     */
    public String getProcessedHelp() {
        String jar;
        try {
            jar = Command.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString();
        } catch (URISyntaxException e) {
            jar = "my-jar.jar";
        }

        String[] path = split(jar, SystemUtils.FILE_SEPARATOR);
        jar = path[path.length - 1];

        String[] placeholders = new String[] {
            "%command.name%",
            "%command.full_name%"
        };
        String[] replacements = new String[] {
            name,
            String.format("%s %s", jar, name)
        };

        return replaceEach(getHelp(), placeholders, replacements);
    }

    /**
     * Sets the aliases for the command.
     *
     * @param aliases An array of aliases for the command
     *
     * @return The current instance
     */
    public void setAliases(List<String> aliases) {
        for (String alias : aliases) {
            validateName(alias);
        }

        this.aliases = aliases;
    }

    /**
     * Returns the aliases for the command.
     *
     * @return An array of aliases for the command
     */
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * Returns the synopsis for the command.
     *
     * @return The synopsis
     */
    public String getSynopsis() {
        if (null == synopsis) {
            synopsis = String.format("%s %s", name, definition.getSynopsis()).trim();
        }

        return this.synopsis;
    }

    /**
     * Gets a helper instance by name.
     *
     * @param name The helper name
     *
     * @return The helper value
     *
     * @throws IllegalArgumentException if the helper is not defined
     */
    public Object getHelper(String name) {
        return helperSet.get(name);
    }

    /**
     * Returns a text representation of the command.
     *
     * @return A string representing the command
     */
    public String asText() {
        List<String> messages = new ArrayList<String>();
        messages.add("<comment>Usage:</comment>");
        messages.add(' ' + getSynopsis());
        messages.add(EMPTY);

        List<String> commandAliases = getAliases();
        if (null != commandAliases && !commandAliases.isEmpty()) {
            messages.add("<comment>Aliases:</comment> <info>" + join(commandAliases, ", ") + "</info>");
        }

        messages.add(getNativeDefinition().asText());

        String processedHelp = getProcessedHelp();
        if (null != processedHelp && !processedHelp.isEmpty()) {
            messages.add("<comment>Help:</comment>");
            messages.add(' ' + processedHelp.replace("\n", "\n ") + "\n");
        }

        return join(messages, "\n");
    }

    private void validateName(String name) throws IllegalArgumentException {
        if (isEmpty(name) || !name.matches("^[^\\:]+(\\:[^\\:]+)*$")) {
            throw new IllegalArgumentException(String.format("Command name \"%s\" is invalid.", name));
        }
    }
}
