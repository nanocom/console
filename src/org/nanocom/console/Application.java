/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console;

import java.io.InputStream;
import java.util.Map.Entry;
import java.util.*;
import org.nanocom.console.command.Command;
import org.nanocom.console.command.HelpCommand;
import org.nanocom.console.command.ListCommand;
import org.nanocom.console.helper.DialogHelper;
import org.nanocom.console.helper.FormatterHelper;
import org.nanocom.console.helper.HelperSet;
import org.nanocom.console.input.*;
import org.nanocom.console.output.ConsoleOutput;
import org.nanocom.console.output.ConsoleOutputInterface;
import org.nanocom.console.output.OutputInterface;
import static org.nanocom.console.Util.*;

/**
 * An Application is the container for a collection of commands.
 *
 * It is the main entry point of a Console application.
 *
 * This class is optimized for a standard CLI environment.
 *
 * Usage:
 *
 *     Application app = new Application("myapp", "1.0 (stable)");
 *     app.add(new SimpleCommand());
 *     app.run();
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class Application {

    private Map<String, Command> commands;
    private boolean wantHelps = false;
    private Command runningCommand;
    private String name;
    private String version;
    private boolean catchExceptions;
    private boolean autoExit;
    private InputDefinition definition;
    private HelperSet helperSet;

    /**
     * @param name    The name of the application
     * @param version The version of the application
     */
    public Application(final String name, final String version) throws Exception {
        init(name, version);
    }

    public Application(final String name) throws Exception {
        init(name, "UNKNOWN");
    }

    public Application() throws Exception {
        init("UNKNOWN", "UNKNOWN");
    }

    private void init(final String name, final String version) throws Exception {
        this.name = name;
        this.version = version;
        catchExceptions = true;
        autoExit = true;
        commands = new HashMap<>();
        helperSet = getDefaultHelperSet();
        definition = getDefaultInputDefinition();

        for (Command command : getDefaultCommands()) {
            add(command);
        }
    }

    /**
     * Runs the current application.
     *
     * @param input  An Input instance
     * @param output An Output instance
     *
     * @return 0 if everything went fine, or an error code
     *
     * @throws Exception When doRun returns Exception
     */
    public int run(InputInterface input, OutputInterface output) throws Exception {
        if (null == input) {
            input = new ArgvInput();
        }

        if (null == output) {
            output = new ConsoleOutput();
        }

        int statusCode;

        try {
            statusCode = doRun(input, output);
        } catch (Exception e) {
            if (!catchExceptions) {
                throw e;
            }

            if (output instanceof ConsoleOutputInterface) {
                renderException(e, ((ConsoleOutputInterface) output).getErrorOutput());
            } else {
                renderException(e, output);
            }

            statusCode = e.hashCode();
        }

        if (autoExit) {
            if (statusCode > 255) {
                statusCode = 255;
            }

            System.exit(statusCode);
        }

        return statusCode;
    }

    /**
     * Runs the current application.
     *
     * @param input  An Input instance
     * @param output An Output instance
     *
     * @return 0 if everything went fine, or an error code
     */
    public int doRun(InputInterface input, OutputInterface output) throws Exception {
        name = getCommandName(input);

        if (true == input.hasParameterOption(Arrays.asList("--ansi"))) {
            output.setDecorated(true);
        } else if (true == input.hasParameterOption(Arrays.asList("--no-ansi"))) {
            output.setDecorated(false);
        }

        if (true == input.hasParameterOption(Arrays.asList("--help", "-h"))) {
            if (null == name) {
                name = "help";
                Map<String, String> arrayInputParams = new HashMap<>();
                arrayInputParams.put("command", "help");
                input = new ArrayInput(arrayInputParams);
            } else {
                wantHelps = true;
            }
        }

        if (true == input.hasParameterOption(Arrays.asList("--no-interaction", "-n"))) {
            input.setInteractive(false);
        }

        if (null != System.console() && getHelperSet().has("dialog")) {
            InputStream inputStream = ((DialogHelper) getHelperSet().get("dialog")).getInputStream();
            //if (!posix_isatty(inputStream)) {
                input.setInteractive(false);
            //}
        }

        if (true == input.hasParameterOption(Arrays.asList("--quiet", "-q"))) {
            output.setVerbosity(OutputInterface.VERBOSITY_QUIET);
        } else if (true == input.hasParameterOption(Arrays.asList("--verbose", "-v"))) {
            output.setVerbosity(OutputInterface.VERBOSITY_VERBOSE);
        }

        if (true == input.hasParameterOption(Arrays.asList("--version", "-V"))) {
            output.writeln(this.getLongVersion());

            return 0;
        }

        if (null == name) {
            name = "list";
            Map<String, String> arrayInputParams = new HashMap<>();
            arrayInputParams.put("command", "list");
            input = new ArrayInput(arrayInputParams);
        }

        // The command name MUST be the first element of the input
        Command command = find(name);

        runningCommand = command;
        int statusCode = command.run(input, output);
        runningCommand = null;

        return statusCode;
    }

    /**
     * Set a helper set to be used with the command.
     *
     * @param helperSet The helper set
     */
    public void setHelperSet(final HelperSet helperSet) {
        this.helperSet = helperSet;
    }

    /**
     * Get the helper set associated with the command.
     *
     * @return The HelperSet instance associated with this command
     */
    public HelperSet getHelperSet() {
        return helperSet;
    }

    /**
     * Gets the InputDefinition related to this Application.
     *
     * @return The InputDefinition instance
     */
    public InputDefinition getDefinition() {
        return definition;
    }

    /**
     * Gets the help message.
     *
     * @return A help message.
     */
    public String getHelp() {
        List<String> messages = Arrays.asList(
            getLongVersion(),
            "",
            "<comment>Usage:</comment>",
            String.format("  [options] command [arguments]\n"),
            "<comment>Options:</comment>"
        );

        for (final InputOption option : getDefinition().getOptions().values()) {
            messages.add(String.format("  %-29s %s %s",
                "<info>--" + option.getName() + "</info>",
                null != option.getShortcut() ? "<info>-" + option.getShortcut() + "</info>" : "  ",
                option.getDescription()
            ));
        }

        return Util.implode(System.getProperty("line.separator"), messages);
    }

    /**
     * Sets whether to catch exceptions or not during commands execution.
     *
     * @param catchExceptions Whether to catch exceptions or not during commands execution
     */
    public void setCatchExceptions(final boolean catchExceptions) {
        this.catchExceptions = catchExceptions;
    }

    /**
     * Sets whether to automatically exit after a command execution or not.
     *
     * @param autoExit Whether to automatically exit after a command execution or not
     */
    public void setAutoExit(final boolean autoExit) {
        this.autoExit = autoExit;
    }

    /**
     * Gets the name of the application.
     *
     * @return The application name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the application name.
     *
     * @param name The application name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the application version.
     *
     * @return The application version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the application version.
     *
     * @param version The application version
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * Returns the long version of the application.
     *
     * @return The long application version
     */
    public String getLongVersion() {
        if (!"UNKNOWN".equals(getName()) && !"UNKNOWN".equals(this.getVersion())) {
            return String.format("<info>%s</info> version <comment>%s</comment>", getName(), getVersion());
        }

        return "<info>Console Tool</info>";
    }

    /**
     * Registers a new command.
     *
     * @param name The command name
     *
     * @return The newly created command
     */
    public Command register(final String name) throws Exception {
        return add(new Command(name));
    }

    /**
     * Adds an array of command objects.
     *
     * @param commands An array of commands
     */
    public void addCommands(final List<Command> commands) {
        for (final Command command : commands) {
            add(command);
        }
    }

    /**
     * Adds a command object.
     *
     * If a command with the same name already exists, it will be overridden.
     *
     * @param command A Command object
     *
     * @return The registered command
     */
    public Command add(final Command command) {
        command.setApplication(this);

        if (!command.isEnabled()) {
            command.setApplication(null);

            return null;
        }

        commands.put(command.getName(), command);

        for (final String alias : command.getAliases()) {
            commands.put(alias, command);
        }

        return command;
    }

    /**
     * Returns a registered command by name or alias.
     *
     * @param name The command name or alias
     *
     * @return A Command object
     *
     * @throws Exception When command name given does not exist
     */
    public Command get(final String name) throws Exception {
        if (!commands.containsKey(name)) {
            throw new Exception(String.format("The command \"%s\" does not exist.", name));
        }

        Command command = commands.get(name);

        if (wantHelps) {
            wantHelps = false;

            HelpCommand helpCommand = (HelpCommand) get("help");
            helpCommand.setCommand(command);

            return helpCommand;
        }

        return command;
    }

    /**
     * Returns true if the command exists, false otherwise.
     *
     * @param name The command name or alias
     *
     * @return True if the command exists, false otherwise
     */
    public boolean has(final String name) {
        return commands.containsKey(name);
    }

    /**
     * Returns an array of all unique namespaces used by currently registered commands.
     *
     * It does not returns the global namespace which always exists.
     *
     * @return An array of namespaces
     */
    public List<String> getNamespaces() {
        List<String> namespaces = new ArrayList<>();
        for (final Command command : commands.values()) {
            namespaces.add(extractNamespace(command.getName()));

            for (final String alias : command.getAliases()) {
                namespaces.add(extractNamespace(alias));
            }
        }

        return namespaces; // TODO array_unique and array_filter
    }

    /**
     * Finds a registered namespace by a name or an abbreviation.
     *
     * @param namespace A namespace or abbreviation to search for
     *
     * @return A registered namespace
     *
     * @throws Exception When namespace is incorrect or ambiguous
     */
    public String findNamespace(final String namespace) throws Exception {
        Map<String, String[]> allNamespaces = new HashMap<>();
        for (final String n : getNamespaces()) {
            allNamespaces.put(n, n.split(":"));
        }

        List<String> found = new ArrayList<>();
        int i = 0;
        for (String part : namespace.split(":")) {
            // TODO array_unique(array_values(array_filter(array_map(void (p) use (i) { return isset(p[i]) ? p[i] : ""; }
            Map<String, List<String>> abbrevs = new HashMap<>(); // = getAbbreviations(allNamespaces);

            if (!abbrevs.containsKey(part)) {
                String message = String.format("There are no commands defined in the \"%s\" namespace.", namespace);

                if (1 <= i) {
                    part = Util.implode(":", found) + ":" + part;
                }

                List<String> alternatives = new ArrayList<>(); // = findAlternativeNamespace(part, abbrevs);

                if (null != alternatives) {
                    message += "\n\nDid you mean one of these?\n    ";
                    message += Util.implode("\n    ", alternatives);
                }

                throw new Exception(message);
            }

            if (abbrevs.get(part).size() > 1) {
                throw new Exception(String.format("The namespace \"%s\" is ambiguous.", namespace /*, getAbbreviationSuggestions(abbrevs.get(part))*/));
            }

            found.add(abbrevs.get(part).get(0));
        }

        return Util.implode(":", found);
    }

    /**
     * Finds a command by name or alias.
     *
     * Contrary to get, this command tries to find the best
     * match if you give it an abbreviation of a name or alias.
     *
     * @param name A command name or a command alias
     *
     * @return A Command instance
     *
     * @throws Exception When command name is incorrect or ambiguous
     */
    public Command find(final String name) throws Exception {
        // Namespace
        String namespace = "";
        String searchName = name;
        int pos = name.indexOf(':');
        if (pos >= 0) {
            namespace = findNamespace(name.substring(0, pos));
            searchName = namespace + name.substring(pos);
        }

        // Name
        HashMap<String, Command> locCommands = new HashMap<>();
        for (final Command command : commands.values()) {
            if (extractNamespace(command.getName()).equals(namespace)) {
                locCommands.put(command.getName(), command);
            }
        }

        Map<String, List<String>> abbrevs = getAbbreviations(commands.keySet()); // TODO array_unique on commands
        if (abbrevs.containsKey(searchName) && 1 == abbrevs.get(searchName).size()) {
            return get(abbrevs.get(searchName).get(0));
        }

        if (abbrevs.containsKey(searchName) && abbrevs.get(searchName).size() > 1) {
            String suggestions = getAbbreviationSuggestions(abbrevs.get(searchName));

            throw new Exception(String.format("Command \"%s\" is ambiguous (%s).", name, suggestions));
        }

        // Aliases
        List<String> aliases = new ArrayList<>();
        for (final Command command : commands.values()) {
            for (final String alias : command.getAliases()) {
                if (extractNamespace(alias).equals(namespace)) {
                    aliases.add(alias);
                }
            }
        }

        Map<String, List<String>> aliasesMap = getAbbreviations(aliases); // TODO array_unique on aliases
        if (!aliasesMap.containsKey(searchName)) {
            String message = String.format("Command \"%s\" is not defined.", name);

            Set<String> alternatives = new HashSet<>(); // TODO findAlternativeCommands(searchName, abbrevs);
            if (!alternatives.isEmpty()) {
                message += "\n\nDid you mean one of these?\n    ";
                // message += Util.implode("\n    ", alternatives.);
            }

            throw new Exception(message);
        }

        if (aliasesMap.get(searchName).size() > 1) {
            throw new Exception(String.format("Command \"%s\" is ambiguous (%s).", name, getAbbreviationSuggestions(aliasesMap.get(searchName))));
        }

        return get(aliasesMap.get(searchName).get(0));
    }

    /**
     * Gets the commands (registered in the given namespace if provided).
     *
     * The array keys are the full names and the values the command instances.
     *
     * @param namespace A namespace name
     *
     * @return An array of Command instances
     */
    public Map<String, Command> all(final String namespace) {
        Map<String, Command> returnedCommands = new HashMap<>();
        for (final Command command : commands.values()) {
            if (namespace.equals(extractNamespace(command.getName(), Integer.valueOf(Util.substr_count(namespace, ":") + 1)))) {
                returnedCommands.put(command.getName(), command);
            }
        }

        return returnedCommands;
    }

    public Map<String, Command> all() {
        return commands;
    }

    /**
     * Returns an array of possible abbreviations given a set of names.
     *
     * @param names An array of names
     *
     * @return An array of abbreviations
     */
    static public Map<String, List<String>> getAbbreviations(final Collection<String> names) {
        Map<String, List<String>> abbrevs = new HashMap<>();
        for (final String name : names) {
            for (int len = name.length() - 1; len > 0; --len) {
                String abbrev = name.substring(0, len);
                if (!abbrevs.containsKey(abbrev)) {
                    abbrevs.put(abbrev, new ArrayList<>(Arrays.asList(name)));
                } else {
                    abbrevs.get(abbrev).add(name);
                }
            }
        }

        // Non-abbreviations always get entered, even if they aren't unique
        for (final String name : names) {
            abbrevs.put(name, new ArrayList<>(Arrays.asList(name)));
        }

        return abbrevs;
    }

    /**
     * Returns a text representation of the Application.
     *
     * @param namespace An optional namespace name
     * @param raw       Whether to return raw command list
     *
     * @return A string representing the Application
     */
    public String asText(final String namespace, final boolean raw) throws Exception {
        Map<String, Command> locCommands = null != namespace ? all(findNamespace(namespace)) : commands;

        int width = 0;
        for (final Command command : locCommands.values()) {
            width = command.getName().length() > width ? command.getName().length() : width;
        }
        width += 2;

        if (raw) {
            List<String> messages = new ArrayList<>();
            for (final Map<String, Command> commandsMap : sortCommands(locCommands).values()) {
                for (final Command command : commandsMap.values()) {
                    messages.add(String.format("%-{width}s %s", name, command.getDescription()));
                }
            }

            return implode(System.getProperty("line.separator"), messages);
        }

        List<String> messages = Arrays.asList(getHelp(), "");
        if (null != namespace) {
            messages.add(String.format("<comment>Available commands for the \"%s\" namespace:</comment>", namespace));
        } else {
            messages.add("<comment>Available commands:</comment>");
        }

        // Add commands by namespace
        for (Entry<String, Map<String, Command>> commandsMap : sortCommands(locCommands).entrySet()) {
            if (null == namespace && !"_global".equals(commandsMap.getKey())) {
                messages.add("<comment>" + commandsMap.getKey() + "</comment>");
            }

            for (final Command command : commandsMap.getValue().values()) {
                messages.add(String.format("  <info>%-{width}s</info> %s", name, command.getDescription()));
            }
        }

        return implode(System.getProperty("line.separator"), messages);
    }

    public String asText(final String namespace) throws Exception {
        return asText(namespace, false);
    }

    public String asText() throws Exception {
        return asText(null, false);
    }

    /**
     * Returns an XML representation of the Application.
     *
     * @param namespace An optional namespace name
     * @param asDom     Whether to return a DOM or an XML string
     *
     * @return string|DOMDocument An XML string representing the Application
     */
    /*public void asXml(fnal String namespace = null, final boolean asDom = false) {
        commands = namespace ? this.all(this.findNamespace(namespace)) : this.commands;

        dom = new \DOMDocument("1.0", "UTF-8");
        dom.formatOutput = true;
        dom.appendChild(xml = dom.createElement("symfony"));

        xml.appendChild(commandsXML = dom.createElement("commands"));

        if (namespace) {
            commandsXML.setAttribute("namespace", namespace);
        } else {
            namespacesXML = dom.createElement("namespaces");
            xml.appendChild(namespacesXML);
        }

        // add commands by namespace
        foreach (this.sortCommands(commands) as space => commands) {
            if (!namespace) {
                namespaceArrayXML = dom.createElement("namespace");
                namespacesXML.appendChild(namespaceArrayXML);
                namespaceArrayXML.setAttribute("id", space);
            }

            foreach (commands as name => command) {
                if (name != command.getName()) {
                    continue;
                }

                if (!namespace) {
                    commandXML = dom.createElement("command");
                    namespaceArrayXML.appendChild(commandXML);
                    commandXML.appendChild(dom.createTextNode(name));
                }

                node = command.asXml(true).getElementsByTagName("command").item(0);
                node = dom.importNode(node, true);

                commandsXML.appendChild(node);
            }
        }

        return asDom ? dom : dom.saveXml();
    }*/

    /**
     * Renders a catched exception.
     *
     * @param e      An exception instance
     * @param output An OutputInterface instance
     */
    public void renderException(final Exception e, final OutputInterface output) throws Exception {
        Throwable t = (Throwable) e;
        /*strlen = void (string) {
            if (!void_exists("mb_strlen")) {
                return strlen(string);
            }

            if (false == encoding = mb_detect_encoding(string)) {
                return strlen(string);
            }

            return mb_strlen(string, encoding);
        };*/

        do {
            String title = String.format("  [%s]  ", e.getClass().toString());
            int len = title.length();
            int width = null != getTerminalWidth() ? getTerminalWidth() - 1 : Integer.MAX_VALUE;
            List<String> lines = new ArrayList<>();
            /*for (preg_split("{\r?\n}", e.getMessage()) as line) {
                for (str_split(line, width - 4) as line) {
                    lines.add(String.format("  %s  ", line));
                    len = Math.max(line.length() + 4, len);
                }
            }

            List<String> messages = Arrays.asList(str_repeat(" ", len), title + str_repeat(" ", Math.max(0, len - title.length())));

            for (lines as line) {
                messages.add(line + str_repeat(" ", len - line.length()));
            }*/

            // messages.add(str_repeat(" ", len));

            output.writeln("");
            output.writeln("");
            /*for (messages as message) {
                output.writeln("<error>" + message + "</error>");
            }*/
            output.writeln("");
            output.writeln("");

            if (OutputInterface.VERBOSITY_VERBOSE == output.getVerbosity()) {
                output.writeln("<comment>Exception trace:</comment>");

                // Exception related properties
                /*trace = e.getTrace();
                array_unshift(trace, array(
                    "void" => "",
                    "file"     => e.getFile() != null ? e.getFile() : "n/a",
                    "line"     => e.getLine() != null ? e.getLine() : "n/a",
                    "args"     => array(),
                ));

                for (i = 0, count = count(trace); i < count; i++) {
                    class = isset(trace[i]["class"]) ? trace[i]["class"] : "";
                    type = isset(trace[i]["type"]) ? trace[i]["type"] : "";
                    void = trace[i]["void"];
                    file = isset(trace[i]["file"]) ? trace[i]["file"] : "n/a";
                    line = isset(trace[i]["line"]) ? trace[i]["line"] : "n/a";

                    output.writeln(String.format(" %s%s%s() at <info>%s:%s</info>", class, type, void, file, line));
                }*/

                output.writeln("");
                output.writeln("");
            }
            t = t.getCause();
        } while (null != t);

        if (null != runningCommand) {
            output.writeln(String.format("<info>%s</info>", String.format(runningCommand.getSynopsis(), getName())));
            output.writeln("");
            output.writeln("");
        }
    }

    /**
     * Tries to figure out the terminal width in which this application runs
     *
     * @return
     */
    protected Integer getTerminalWidth() {
        /*if (defined("PHP_WINDOWS_VERSION_BUILD") && ansicon = getenv("ANSICON")) {
            return preg_replace("{^(\d+)x.*}", "1", ansicon);
        }

        if (preg_match(\"{rows.(\d+);.columns.(\d+);}i\", this.getSttyColumns(), match)) {
            return match[1];
        }*/

        return null;
    }

    /**
     * Tries to figure out the terminal height in which this application runs
     *
     * @return
     */
    protected Integer getTerminalHeight() {
        /* if (defined("PHP_WINDOWS_VERSION_BUILD") && ansicon = getenv("ANSICON")) {
            return preg_replace("{^\d+x\d+ \(\d+x(\d+)\)}", "1", trim(ansicon));
        }

        if (preg_match("{rows.(\d+);.columns.(\d+);}i", getSttyColumns(), match)) {
            return match[2];
        }*/

        return null;
    }

    /**
     * Gets the name of the command based on input.
     *
     * @param input The input interface
     *
     * @return The command name
     */
    protected String getCommandName(final InputInterface input) {
        return input.getFirstArgument(/*"command"*/);
    }

    /**
     * Gets the default input definition.
     *
     * @return An InputDefinition instance
     */
    protected InputDefinition getDefaultInputDefinition() throws Exception {
        return new InputDefinition(Arrays.asList((Object)
            new InputArgument("command", InputArgument.REQUIRED, "The command to execute"),
            new InputOption("--help",           "-h", InputOption.VALUE_NONE, "Display this help message."),
            new InputOption("--quiet",          "-q", InputOption.VALUE_NONE, "Do not output any message."),
            new InputOption("--verbose",        "-v", InputOption.VALUE_NONE, "Increase verbosity of messages."),
            new InputOption("--version",        "-V", InputOption.VALUE_NONE, "Display this application version."),
            new InputOption("--ansi",           "",   InputOption.VALUE_NONE, "Force ANSI output."),
            new InputOption("--no-ansi",        "",   InputOption.VALUE_NONE, "Disable ANSI output."),
            new InputOption("--no-interaction", "-n", InputOption.VALUE_NONE, "Do not ask any interactive question.")
        ));
    }

    /**
     * Gets the default commands that should always be available.
     *
     * @return An array of default Command instances
     */
    protected List<Command> getDefaultCommands() throws Exception {
        return Arrays.asList((Command) new HelpCommand(), new ListCommand());
    }

    /**
     * Gets the default helper set with the helpers that should always be available.
     *
     * @return A HelperSet instance
     */
    protected HelperSet getDefaultHelperSet() {
        return new HelperSet(Arrays.asList(
            new FormatterHelper(),
            new DialogHelper()
        ));
    }

    /**
     * Runs and parses stty -a if it's available, suppressing any error output
     *
     * @return
     */
    private String getSttyColumns() {
        /*descriptorspec = array(1 => array("pipe", "w"), 2 => array("pipe", "w"));
        process = proc_open("stty -a | grep columns", descriptorspec, pipes, null, null, array("suppress_errors" => true));
        if (is_resource(process)) {
            info = stream_get_contents(pipes[1]);
            fclose(pipes[1]);
            fclose(pipes[2]);
            proc_close(process);

            return info;
        }*/
        return ""; // Not implemented
    }

    /**
     * Sorts commands in alphabetical order.
     *
     * @param commands An associative array of commands to sort
     *
     * @return A sorted array of commands
     */
    private Map<String, Map<String, Command>> sortCommands(Map<String, Command> commands) {
        Map<String, Map<String, Command>> namespacedCommands = new HashMap<>();
        for (final Command command : commands.values()) {
            String key = extractNamespace(command.getName(), 1);
            if (null == key) {
                key = "_global";
            }

            Map<String, Command> mapToInsert = new HashMap<>();
            mapToInsert.put(name, command);
            namespacedCommands.put(key, mapToInsert);
        }
        // ksort(namespacedCommands); TODO

        /*for (Command commandsMap : namespacedCommands.values()) {
            ksort(commandsMap);
        }*/

        return namespacedCommands;
    }

    /**
     * Returns abbreviated suggestions in string format.
     *
     * @param abbrevs Abbreviated suggestions to convert
     *
     * @return A formatted string of abbreviated suggestions
     */
    private String getAbbreviationSuggestions(final Collection<String> abbrevs) {
        return String.format("%s, %s%s", abbrevs.toArray()[0], abbrevs.toArray()[1], abbrevs.size() > 2 ? String.format(" and %d more", abbrevs.size() - 2) : "");
    }

    /**
     * Returns the namespace part of the command name.
     *
     * @param name  The full name of the command
     * @param limit The maximum number of parts of the namespace
     *
     * @return The namespace of the command
     */
    private String extractNamespace(final String name, final Integer limit) {
        String[] arrayParts = name.split(":");
        List<String> parts = new ArrayList<>(Arrays.asList(arrayParts));
        parts.remove(parts.size() - 1);

        return implode(":", null == limit ? parts : Util.array_slice(parts, 0, limit));
    }

    private String extractNamespace(final String name) {
        return extractNamespace(name, null);
    }

    /**
     * Finds alternative commands of name
     *
     * @param name      The full name of the command
     * @param abbrevs   The abbreviations
     *
     * @return A sorted array of similar commands
     */
    private Set<String> findAlternativeCommands(final String name, final List<String> abbrevs) {
        /*callback = void(item) {
            return item.getName();
        };*/

        // TODO return findAlternatives(name, commands.values(), abbrevs/*, callback*/);
        return new HashSet<>();
    }

    /**
     * Finds alternative namespace of name
     *
     * @param name    The full name of the namespace
     * @param abbrevs The abbreviations
     *
     * @return array A sorted array of similar namespace
     */
    private Set<String> findAlternativeNamespace(final String name, final List<String> abbrevs) {
        return findAlternatives(name, getNamespaces(), abbrevs);
    }

    /**
     * Finds alternative of name among collection,
     * if nothing is found in collection, try in abbrevs
     *
     * @param name       The string
     * @param array|Traversable     collection The collection
     * @param array                 abbrevs    The abbreviations
     * @param Closure|string|array  callback   The callable to transform collection item before comparison
     *
     * @return A sorted set of similar string
     */
    private Set<String> findAlternatives(final String name, List<String> collection, List<String> abbrevs/*, callback*/) {
        Map<String, Integer> alternatives = new HashMap<>();

        for (final Object item : collection) {
            /*if (null != callback) {
                item = call_user_func(callback, item);
            }*/

            // lev = levenshtein(name, item);
            /*if (lev <= name.length() / 3 || -1 != name.indexOf(item)) {
                alternatives.put(item, lev);
            }*/
        }

        /*if (alternatives.isEmpty()) {
            for (abbrevs as key => values) {
                lev = levenshteinarray_keys(alternatives);(name, key);
                if (lev <= strlen(name) / 3 || false != strpos(key, name)) {
                    foreach (values as value) {
                        alternatives[value] = lev;
                    }
                }
            }
        }

        asort(alternatives);*/

        return alternatives.keySet();
    }

}
