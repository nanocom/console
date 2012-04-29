package com.nanocom.console.command;

import com.nanocom.console.Application;
import com.nanocom.console.helper.HelperSet;
import com.nanocom.console.input.InputArgument;
import com.nanocom.console.input.InputDefinition;
import com.nanocom.console.input.InputInterface;
import com.nanocom.console.input.InputOption;
import com.nanocom.console.output.OutputInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Base class for all commands.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public abstract class Command {

//    private Application application;
//    private String name;
//    private List<String> aliases;
//    private InputDefinition definition;
//    private String help;
//    private String description;
//    private Boolean ignoreValidationErrors;
//    private Boolean applicationDefinitionMerged;
//    private String synopsis;
//    private HelperSet helperSet;
//
//    /**
//     * @param name The name of the command
//     *
//     * @throws Exception When the command name is empty
//     */
//    public Command(final String name) throws Exception {
//        init(name);
//    }
//
//    public Command() throws Exception {
//        init(null);
//    }
//
//    private void init(final String name) throws Exception {
//        definition = new InputDefinition();
//        ignoreValidationErrors = false;
//        applicationDefinitionMerged = false;
//        aliases = new ArrayList<String>();
//
//        if (null != name) {
//            setName(name);
//        }
//
//        configure();
//
//        if (null == name || name.isEmpty()) {
//            throw new Exception("The command name cannot be empty.");
//        }
//    }
//
//    /**
//     * Ignores validation errors.
//     *
//     * This is mainly useful for the help command.
//     */
//    public void ignoreValidationErrors() {
//        ignoreValidationErrors = true;
//    }
//
//    /**
//     * Sets the application instance for this command.
//     *
//     * @param application An Application instance
//     */
//    public void setApplication(Application application) {
//        this.application = application;
//        if (null != application) {
//            setHelperSet(application.getHelperSet());
//        } else {
//            helperSet = null;
//        }
//    }
//
//    /**
//     * Sets the helper set.
//     *
//     * @param helperSet A HelperSet instance
//     */
//    public void setHelperSet(HelperSet helperSet) {
//        this.helperSet = helperSet;
//    }
//
//    /**
//     * Gets the helper set.
//     *
//     * @return A HelperSet instance
//     */
//    public HelperSet getHelperSet() {
//        return helperSet;
//    }
//
//    /**
//     * Gets the application instance for this command.
//     *
//     * @return An Application instance
//     */
//    public Application getApplication() {
//        return application;
//    }
//
//    /**
//     * Configures the current command.
//     */
//    protected void configure()
//    {
//    }
//
//    /**
//     * Executes the current command.
//     *
//     * This method is not abstract because you can use this class
//     * as a concrete class. In this case, instead of defining the
//     * execute() method, you set the code to execute by passing
//     * a Closure to the setCode() method.
//     *
//     * @param input  An InputInterface instance
//     * @param output An OutputInterface instance
//     *
//     * @return 0 if everything went fine, or an error code
//     *
//     * @throws Exception When this abstract method is not implemented
//     * @see    setCode()
//     */
//    protected abstract int execute(InputInterface input, OutputInterface output) throws Exception;
//
//    /**
//     * Interacts with the user.
//     *
//     * @param input  An InputInterface instance
//     * @param output An OutputInterface instance
//     */
//    protected void interact(InputInterface input, OutputInterface output)
//    {
//    }
//
//    /**
//     * Initializes the command just after the input has been validated.
//     *
//     * This is mainly useful when a lot of commands extends one main command
//     * where some things need to be initialized based on the input arguments and options.
//     *
//     * @param input  An InputInterface instance
//     * @param output An OutputInterface instance
//     */
//    protected void initialize(InputInterface input, OutputInterface output)
//    {
//    }
//
//    /**
//     * Runs the command.
//     *
//     * The code to execute is either defined directly with the
//     * setCode() method or by overriding the execute() method
//     * in a sub-class.
//     *
//     * @param input  An InputInterface instance
//     * @param output An OutputInterface instance
//     *
//     * @see setCode()
//     * @see execute()
//     */
//    public int run(InputInterface input, OutputInterface output) throws Exception {
//        // Force the creation of the synopsis before the merge with the app definition
//        getSynopsis();
//
//        // Add the application arguments and options
//        mergeApplicationDefinition();
//
//        // Bind the input against the command specific arguments/options
//        try {
//            input.bind(this.definition);
//        } catch (Exception e) {
//            if (!ignoreValidationErrors) {
//                throw e;
//            }
//        }
//
//        initialize(input, output);
//
//        if (input.isInteractive()) {
//            this.interact(input, output);
//        }
//
//        input.validate();
//
//        return execute(input, output);
//    }
//
//    /**
//     * Merges the application definition with the command definition.
//     */
//    private void mergeApplicationDefinition() throws Exception {
//        if (null == application || true == applicationDefinitionMerged) {
//            return;
//        }
//
//        Map<String, InputArgument> mergedArguments = definition.getArguments();
//        mergedArguments.putAll(definition.getArguments());
//        definition.setArguments(mergedArguments);
//
//        definition.addOptions(application.getDefinition().getOptions());
//
//        applicationDefinitionMerged = true;
//    }
//
//    /**
//     * Sets an array of argument and option instances.
//     *
//     * @param definition An array of argument and option instances or a definition instance
//     *
//     * @return The current instance
//     */
//    public Command setDefinition(final Object definition) throws Exception {
//        if (definition instanceof InputDefinition) {
//            this.definition = (InputDefinition)definition;
//        } else if (definition instanceof List) {
//            this.definition.setDefinition((List<Object>)definition);
//        }
//
//        applicationDefinitionMerged = false;
//
//        return this;
//    }
//
//    /**
//     * Gets the InputDefinition attached to this Command.
//     *
//     * @return An InputDefinition instance
//     */
//    public InputDefinition getDefinition() {
//        return definition;
//    }
//
//    /**
//     * Gets the InputDefinition to be used to create XML and Text representations of this Command.
//     *
//     * Can be overridden to provide the original command representation when it would otherwise
//     * be changed by merging with the application InputDefinition.
//     *
//     * @return An InputDefinition instance
//     */
//    protected InputDefinition getNativeDefinition() throws Exception {
//        return getDefinition();
//    }
//
//    /**
//     * Adds an argument.
//     *
//     * @param name         The argument name
//     * @param mode         The argument mode: InputArgument::REQUIRED or InputArgument::OPTIONAL
//     * @param description  A description text
//     * @param defaultValue The default value (for InputArgument::OPTIONAL mode only)
//     *
//     * @return The current instance
//     */
//    public Command addArgument(final String name, final Integer mode, final String description, final Object defaultValue) throws Exception {
//        definition.addArgument(new InputArgument(name, mode, description, defaultValue));
//
//        return this;
//    }
//
//    public Command addArgument(final String name, final Integer mode, final String description) throws Exception {
//        return addArgument(name, mode, description, null);
//    }
//
//    public Command addArgument(final String name, final Integer mode) throws Exception {
//        return addArgument(name, mode, "", null);
//    }
//
//    public Command addArgument(final String name) throws Exception {
//        return addArgument(name, null, "", null);
//    }
//
//    /**
//     * Adds an option.
//     *
//     * @param name         The option name
//     * @param shortcut     The shortcut (can be null)
//     * @param mode         The option mode: One of the InputOption::VALUE_* constants
//     * @param description  A description text
//     * @param defaultValue The default value (must be null for InputOption::VALUE_REQUIRED or InputOption::VALUE_NONE)
//     *
//     * @return The current instance
//     */
//    public Command addOption(final String name, final String shortcut, final Integer mode, final String description, final Object defaultValue) throws Exception {
//        definition.addOption(new InputOption(name, shortcut, mode, description, defaultValue));
//
//        return this;
//    }
//
//    public Command addOption(final String name, final String shortcut, final Integer mode, final String description) throws Exception {
//        return addOption(name, shortcut, mode, description, null);
//    }
//
//    public Command addOption(final String name, final String shortcut, final Integer mode) throws Exception {
//        return addOption(name, shortcut, mode, "");
//    }
//
//    public Command addOption(final String name, final String shortcut) throws Exception {
//        return addOption(name, shortcut, null);
//    }
//
//    public Command addOption(final String name) throws Exception {
//        return addOption(name, null);
//    }
//
//    /**
//     * Sets the name of the command.
//     *
//     * This method can set both the namespace and the name if
//     * you separate them by a colon (:)
//     *
//     *     command.setName('foo:bar');
//     *
//     * @param name The command name
//     *
//     * @return The current instance
//     *
//     * @throws Exception When command name given is empty
//     */
//    public Command setName(final String name) {
//        validateName(name);
//
//        this.name = name;
//
//        return this;
//    }
//
//    /**
//     * Returns the command name.
//     *
//     * @return The command name
//     */
//    public String getName() {
//        return name;
//    }
//
//    /**
//     * Sets the description for the command.
//     *
//     * @param description The description for the command
//     *
//     * @return The current instance
//     */
//    public Command setDescription(final String description) {
//        this.description = description;
//
//        return this;
//    }
//
//    /**
//     * Returns the description for the command.
//     *
//     * @return The description for the command
//     */
//    public String getDescription() {
//        return description;
//    }
//
//    /**
//     * Sets the help for the command.
//     *
//     * @param help The help for the command
//     *
//     * @return The current instance
//     */
//    public Command setHelp(final String help) {
//        this.help = help;
//
//        return this;
//    }
//
//    /**
//     * Returns the help for the command.
//     *
//     * @return The help for the command
//     */
//    public String getHelp() {
//        return help;
//    }
//
//    /**
//     * Returns the processed help for the command replacing the %command.name% pattern
//     * with the real value dynamically.
//     *
//     * @return The processed help for the command
//     */
//    public String getProcessedHelp() {
//        return getHelp().replaceAll("%command.name%", name);
//    }
//
//    /**
//     * Sets the aliases for the command.
//     *
//     * @param aliases An array of aliases for the command
//     *
//     * @return The current instance
//     */
//    public Command setAliases(final List<String> aliases) {
//        for (String alias : aliases) {
//            validateName(alias);
//        }
//
//        this.aliases = aliases;
//
//        return this;
//    }
//
//    /**
//     * Returns the aliases for the command.
//     *
//     * @return An array of aliases for the command
//     */
//    public List<String> getAliases() {
//        return aliases;
//    }
//
//    /**
//     * Returns the synopsis for the command.
//     *
//     * @return The synopsis
//     */
//    public String getSynopsis() {
//        if (null == synopsis) {
//            synopsis = String.format("%s %s", name, definition.getSynopsis()).trim();
//        }
//
//        return this.synopsis;
//    }
//
//    /**
//     * Gets a helper instance by name.
//     *
//     * @param name The helper name
//     *
//     * @return The helper value
//     *
//     * @throws Exception if the helper is not defined
//     */
//    public Object getHelper(final String name) throws Exception {
//        return helperSet.get(name);
//    }
//
//    /**
//     * Returns a text representation of the command.
//     *
//     * @return A string representing the command
//     */
//    public String asText() throws Exception {
//        List<String> messages = Arrays.asList(
//            "<comment>Usage:</comment>",
//            " " + this.getSynopsis(),
//            ""
//        );
//
//        if (null != getAliases() && !getAliases().isEmpty()) {
//            StringBuilder sb = new StringBuilder();
//            sb.append(getAliases().get(0));
//
//            for (int i = 1; i < getAliases().size(); i++) {
//                sb.append(", ");
//                sb.append(getAliases().get(i));
//            }
//
//            messages.add("<comment>Aliases:</comment> <info>" + sb.toString() + "</info>");
//        }
//
//        messages.add(getNativeDefinition().asText());
//
//        String processedHelp = getProcessedHelp();
//        if (null != processedHelp && !processedHelp.isEmpty()) {
//            messages.add("comment>Help:</comment>");
//            // TODO
//            // essages.append(" " + implode("\n ", explode("\n", help)) + "\n";
//        }
//
//        return messages.toString(); // TODO Implode messages with "\n"
//    }
//
//    /**
//     * Returns an XML representation of the command.
//     *
//     * @param asDom Whether to return a DOM or an XML string
//     *
//     * @return An XML string representing the command
//     */
//    public String asXml(final boolean asDom) throws Exception {
//        throw new Exception("Not yet implemented.");
//        /*
//        dom = new \DOMDocument('1.0', 'UTF-8');
//        dom.formatOutput = true;
//        dom.appendChild(commandXML = dom.createElement('command'));
//        commandXML.setAttribute('id', this.name);
//        commandXML.setAttribute('name', this.name);
//
//        commandXML.appendChild(usageXML = dom.createElement('usage'));
//        usageXML.appendChild(dom.createTextNode(sprintf(this.getSynopsis(), '')));
//
//        commandXML.appendChild(descriptionXML = dom.createElement('description'));
//        descriptionXML.appendChild(dom.createTextNode(implode("\n ", explode("\n", this.getDescription()))));
//
//        commandXML.appendChild(helpXML = dom.createElement('help'));
//        help = this.help;
//        helpXML.appendChild(dom.createTextNode(implode("\n ", explode("\n", help))));
//
//        commandXML.appendChild(aliasesXML = dom.createElement('aliases'));
//        foreach (this.getAliases() as alias) {
//            aliasesXML.appendChild(aliasXML = dom.createElement('alias'));
//            aliasXML.appendChild(dom.createTextNode(alias));
//        }
//
//        definition = this.getNativeDefinition().asXml(true);
//        commandXML.appendChild(dom.importNode(definition.getElementsByTagName('arguments').item(0), true));
//        commandXML.appendChild(dom.importNode(definition.getElementsByTagName('options').item(0), true));
//
//        return asDom ? dom : dom.saveXml();*/
//    }
//
//    private void validateName(final String name) {
//        // TODO
////        if (!preg_match('/^[^\:]+(\:[^\:]+)*/', name)) {
////            throw new Exception("Command name \"" + name + "\" is invalid.");
////        }
//    }

}
