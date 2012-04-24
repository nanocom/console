package com.nanocom;

import com.nanocom.command.Command;
import com.nanocom.command.HelpCommand;
import com.nanocom.helper.HelperSet;
import com.nanocom.input.ArgvInput;
import com.nanocom.input.ArrayInput;
import com.nanocom.input.InputDefinition;
import com.nanocom.input.InputInterface;
import com.nanocom.output.ConsoleOutput;
import com.nanocom.output.ConsoleOutputInterface;
import com.nanocom.output.OutputInterface;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * An Application is the container for a collection of commands.
 * It is the main entry point of a Console application.
 * This class is optimized for a standard CLI environment.
 *
 * Usage:
 *     Application app = new Application("myapp", "1.0 (stable)");
 *     app.add(new SimpleCommand());
 *     app.run();
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public final class Application {

    private Map<String, Command> commands;
    private boolean              wantHelps = false;
    private Command              runningCommand;
    private String               name;
    private String               version;
    private boolean              catchExceptions;
    private boolean              autoExit;
    private InputDefinition      definition;
    private HelperSet            helperSet;

    /**
     * @param name    The name of the application
     * @param version The version of the application
     */
    public Application(final String name, final String version) {
        init(name, version);
    }
    
    public Application(final String name) {
        init(name, "UNKNOWN");
    }
    
    public Application() {
        init("UNKNOWN", "UNKNOWN");
    }
    
    private void init(final String name, String version) {
        this.name = name;
        this.version = version;
        catchExceptions = true;
        autoExit = true;
        commands = new HashMap<String, Command>();
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
                renderException(e, ((ConsoleOutputInterface)output).getErrorOutput());
            } else {
                renderException(e, output);
            }

            statusCode = e.hashCode();

            statusCode = statusCode != 0 ? statusCode : 1;
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
        // TODO Check this method
        String locName = getCommandName(input);

        List<String> options = new ArrayList<String>();
        options.add("--ansi");
        options.add("--no-ansi");
        options.add("--help");
        options.add("-h");
        options.add("--no-interaction");
        options.add("-n");

        if (true == input.hasParameterOption(options.subList(0, 0))) {
            output.setDecorated(true);
        } else if (true == input.hasParameterOption(options.subList(1, 1))) {
            output.setDecorated(false);
        }

        if (true == input.hasParameterOption(options.subList(2, 3))) {
            if (null == locName) {
                Map<String, String> arrayInputParam = new HashMap<String, String>();
                arrayInputParam.put("command", "help");
                input = new ArrayInput(arrayInputParam);
            } else {
                wantHelps = true;
            }
        }

        if (true == input.hasParameterOption(options.subList(4, 5))) {
            input.setInteractive(false);
        }

        if (getHelperSet().has("dialog")) {
            InputStream inputStream = getHelperSet().get("dialog").getInputStream();
            /*if (!posix_isatty(inputStream)) {
                input.setInteractive(false);
            }*/
        }

        if (true == input.hasParameterOption(Arrays.asList("--quiet", "-q"))) {
            output.setVerbosity(OutputInterface::VERBOSITY_QUIET);
        } else if (true == input.hasParameterOption(array('--verbose', '-v'))) {
            output.setVerbosity(OutputInterface::VERBOSITY_VERBOSE);
        }

        if (true == input.hasParameterOption(Arrays.asList("--version", "-V"))) {
            output.writeln(getLongVersion());

            return 0;
        }

        if (null == locName) {
            locName = 'list';
            input = new ArrayInput(array('command' => 'list'));
        }

        // the command name MUST be the first element of the input
        command = this.find(locName);

        this.runningCommand = command;
        statusCode = command.run(input, output);
        this.runningCommand = null;

        return is_numeric(statusCode) ? statusCode : 0;
    }

    /**
     * Set a helper set to be used with the command.
     *
     * @param helperSet The helper set
     */
    public void setHelperSet(HelperSet helperSet) {
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
        messages = array(
            this.getLongVersion(),
            '',
            '<comment>Usage:</comment>',
            sprintf("  [options] command [arguments]\n"),
            '<comment>Options:</comment>',
        );

        foreach (this.getDefinition().getOptions() as option) {
            messages[] = sprintf('  %-29s %s %s',
                '<info>--'.option.getName().'</info>',
                option.getShortcut() ? '<info>-'.option.getShortcut().'</info>' : '  ',
                option.getDescription()
            );
        }

        return implode(PHP_EOL, messages);
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
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Returns the long version of the application.
     *
     * @return The long application version
     */
    public String getLongVersion() {
        if ("UNKNOWN".equals(getName()) && !"UNKNOWN".equals(getVersion())) {
            return "<info>" + getName() + "</info> version <comment>" + getVersion() + "</comment>";
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
    public Command register(final String name) {
        try {
            Command c = new Command(name) {

                @Override
                protected int execute(InputInterface input, OutputInterface output) throws Exception {
                    return 1;
                }
            };
            return add(c);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Adds an array of command objects.
     *
     * @param commands An array of commands
     */
    public void addCommands(List<Command> commands) {
        for (Command command : commands) {
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
    public Command add(Command command) {
        command.setApplication(this);

        commands.put(command.getName(), command);

        for (String alias : command.getAliases()) {
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
            throw new Exception("The command " + name + " does not exist.");
        }

        Command command = commands.get(name);

        if (wantHelps) {
            wantHelps = false;

            HelpCommand helpCommand = (HelpCommand)get("help");
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
        List<String> namespaces = new ArrayList<String>();
        for (Entry<String, Command> command : commands.entrySet()) {
            namespaces.add(extractNamespace(command.getValue().getName()));

            for (String alias : command.getValue().getAliases()) {
                namespaces.add(extractNamespace(alias));
            }
        }

        // return array_values(array_unique(array_filter(namespaces))); TODO
        return namespaces;
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
    public String findNamespace(final String namespace) {
        Map<String, List<String>> allNamespaces = new HashMap<String, List<String>>();
        for (String n : getNamespaces()) {
            allNamespaces.put(n, Arrays.asList(n.split(":")));
        }

        found = array();
        foreach (explode(':', namespace) as i => part) {
            abbrevs = static::getAbbreviations(array_unique(array_values(array_filter(array_map(function (p) use (i) { return isset(p[i]) ? p[i] : ''; }, allNamespaces)))));

            if (!isset(abbrevs[part])) {
                message = sprintf('There are no commands defined in the "%s" namespace.', namespace);

                if (1 <= i) {
                    part = implode(':', found).':'.part;
                }

                if (alternatives = this.findAlternativeNamespace(part, abbrevs)) {
                    message .= "\n\nDid you mean one of these?\n    ";
                    message .= implode("\n    ", alternatives);
                }

                throw new Exception(message);
            }

            if (count(abbrevs[part]) > 1) {
                throw new Exception("The namespace \"" + namespace + "\" is ambiguous (" + getAbbreviationSuggestions(abbrevs[part]) + ").");
            }

            found[] = abbrevs[part][0];
        }

        return implode(':', found);
    }

    /**
     * Finds a command by name or alias.
     *
     * Contrary to get, this command tries to find the best
     * match if you give it an abbreviation of a name or alias.
     *
     * @param  string name A command name or a command alias
     *
     * @return Command A Command instance
     *
     * @throws \InvalidArgumentException When command name is incorrect or ambiguous
     *
     * @api
     */
    public find(final String name) {
        // Namespace
        namespace = '';
        searchName = name;
        if (false !== pos = strrpos(name, ':')) {
            namespace = this.findNamespace(substr(name, 0, pos));
            searchName = namespace.substr(name, pos);
        }

        // Name
        commands = array();
        foreach (this.commands as command) {
            if (this.extractNamespace(command.getName()) == namespace) {
                commands[] = command.getName();
            }
        }

        abbrevs = static::getAbbreviations(array_unique(commands));
        if (isset(abbrevs[searchName]) && 1 == count(abbrevs[searchName])) {
            return this.get(abbrevs[searchName][0]);
        }

        if (isset(abbrevs[searchName]) && count(abbrevs[searchName]) > 1) {
            suggestions = this.getAbbreviationSuggestions(abbrevs[searchName]);

            throw new \InvalidArgumentException(sprintf('Command "%s" is ambiguous (%s).', name, suggestions));
        }

        // Aliases
        aliases = array();
        foreach (this.commands as command) {
            foreach (command.getAliases() as alias) {
                if (this.extractNamespace(alias) == namespace) {
                    aliases[] = alias;
                }
            }
        }

        aliases = static::getAbbreviations(array_unique(aliases));
        if (!isset(aliases[searchName])) {
            message = sprintf('Command "%s" is not defined.', name);

            if (alternatives = this.findAlternativeCommands(searchName, abbrevs)) {
                message .= "\n\nDid you mean one of these?\n    ";
                message .= implode("\n    ", alternatives);
            }

            throw new \InvalidArgumentException(message);
        }

        if (count(aliases[searchName]) > 1) {
            throw new \InvalidArgumentException(sprintf('Command "%s" is ambiguous (%s).', name, this.getAbbreviationSuggestions(aliases[searchName])));
        }

        return this.get(aliases[searchName][0]);
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
    public List<Command> all(final String namespace) {
        if (null == namespace) {
            return this.commands;
        }

        commands = array();
        foreach (this.commands as name => command) {
            if (namespace == this.extractNamespace(name, substr_count(namespace, ':') + 1)) {
                commands[name] = command;
            }
        }

        return commands;
    }

    /**
     * Returns an array of possible abbreviations given a set of names.
     *
     * @param names An array of names
     *
     * @return An array of abbreviations
     */
    static public List<String> getAbbreviations(final List<String> names) {
        abbrevs = array();
        foreach (names as name) {
            for (len = strlen(name) - 1; len > 0; --len) {
                abbrev = substr(name, 0, len);
                if (!isset(abbrevs[abbrev])) {
                    abbrevs[abbrev] = array(name);
                } else {
                    abbrevs[abbrev][] = name;
                }
            }
        }

        // Non-abbreviations always get entered, even if they aren't unique
        foreach (names as name) {
            abbrevs[name] = array(name);
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
    public String asText(final String namespace, final boolean raw)
    {
        commands = namespace ? this.all(this.findNamespace(namespace)) : this.commands;

        width = 0;
        foreach (commands as command) {
            width = strlen(command.getName()) > width ? strlen(command.getName()) : width;
        }
        width += 2;

        if (raw) {
            messages = array();
            foreach (this.sortCommands(commands) as space => commands) {
                foreach (commands as name => command) {
                    messages[] = sprintf("%-{width}s %s", name, command.getDescription());
                }
            }

            return implode(PHP_EOL, messages);
        }

        messages = array(this.getHelp(), '');
        if (namespace) {
            messages[] = sprintf("<comment>Available commands for the \"%s\" namespace:</comment>", namespace);
        } else {
            messages[] = '<comment>Available commands:</comment>';
        }

        // add commands by namespace
        foreach (this.sortCommands(commands) as space => commands) {
            if (!namespace && '_global' !== space) {
                messages[] = '<comment>'.space.'</comment>';
            }

            foreach (commands as name => command) {
                messages[] = sprintf("  <info>%-{width}s</info> %s", name, command.getDescription());
            }
        }

        return implode(PHP_EOL, messages);
    }

    /**
     * Returns an XML representation of the Application.
     *
     * @param namespace An optional namespace name
     * @param asDom     Whether to return a DOM or an XML string
     *
     * @return An XML string representing the Application
     */
    public asXml(final String namespace, final boolean asDom) {
        commands = namespace ? this.all(this.findNamespace(namespace)) : this.commands;

        dom = new \DOMDocument('1.0', 'UTF-8');
        dom.formatOutput = true;
        dom.appendChild(xml = dom.createElement('symfony'));

        xml.appendChild(commandsXML = dom.createElement('commands'));

        if (namespace) {
            commandsXML.setAttribute('namespace', namespace);
        } else {
            namespacesXML = dom.createElement('namespaces');
            xml.appendChild(namespacesXML);
        }

        // add commands by namespace
        foreach (this.sortCommands(commands) as space => commands) {
            if (!namespace) {
                namespaceArrayXML = dom.createElement('namespace');
                namespacesXML.appendChild(namespaceArrayXML);
                namespaceArrayXML.setAttribute('id', space);
            }

            foreach (commands as name => command) {
                if (name !== command.getName()) {
                    continue;
                }

                if (!namespace) {
                    commandXML = dom.createElement('command');
                    namespaceArrayXML.appendChild(commandXML);
                    commandXML.appendChild(dom.createTextNode(name));
                }

                node = command.asXml(true).getElementsByTagName('command').item(0);
                node = dom.importNode(node, true);

                commandsXML.appendChild(node);
            }
        }

        return asDom ? dom : dom.saveXml();
    }

    /**
     * Renders a catched exception.
     *
     * @param Exception       e      An exception instance
     * @param OutputInterface output An OutputInterface instance
     */
    public renderException(e, output)
    {
        strlen = function (string) {
            if (!function_exists('mb_strlen')) {
                return strlen(string);
            }

            if (false == encoding = mb_detect_encoding(string)) {
                return strlen(string);
            }

            return mb_strlen(string, encoding);
        };

        do {
            title = sprintf('  [%s]  ', get_class(e));
            len = strlen(title);
            width = this.getTerminalWidth() ? this.getTerminalWidth() - 1 : PHP_INT_MAX;
            lines = array();
            foreach (preg_split("{\r?\n}", e.getMessage()) as line) {
                foreach (str_split(line, width - 4) as line) {
                    lines[] = sprintf('  %s  ', line);
                    len = max(strlen(line) + 4, len);
                }
            }

            messages = array(str_repeat(' ', len), title.str_repeat(' ', max(0, len - strlen(title))));

            foreach (lines as line) {
                messages[] = line.str_repeat(' ', len - strlen(line));
            }

            messages[] = str_repeat(' ', len);

            output.writeln("");
            output.writeln("");
            foreach (messages as message) {
                output.writeln('<error>'.message.'</error>');
            }
            output.writeln("");
            output.writeln("");

            if (OutputInterface::VERBOSITY_VERBOSE == output.getVerbosity()) {
                output.writeln('<comment>Exception trace:</comment>');

                // exception related properties
                trace = e.getTrace();
                array_unshift(trace, array(
                    'function' => '',
                    'file'     => e.getFile() != null ? e.getFile() : 'n/a',
                    'line'     => e.getLine() != null ? e.getLine() : 'n/a',
                    'args'     => array(),
                ));

                for (i = 0, count = count(trace); i < count; i++) {
                    class = isset(trace[i]['class']) ? trace[i]['class'] : '';
                    type = isset(trace[i]['type']) ? trace[i]['type'] : '';
                    function = trace[i]['function'];
                    file = isset(trace[i]['file']) ? trace[i]['file'] : 'n/a';
                    line = isset(trace[i]['line']) ? trace[i]['line'] : 'n/a';

                    output.writeln(sprintf(' %s%s%s() at <info>%s:%s</info>', class, type, function, file, line));
                }

                output.writeln("");
                output.writeln("");
            }
        } while (e = e.getPrevious());

        if (null !== this.runningCommand) {
            output.writeln(sprintf('<info>%s</info>', sprintf(this.runningCommand.getSynopsis(), this.getName())));
            output.writeln("");
            output.writeln("");
        }
    }

    /**
     * Tries to figure out the terminal width in which this application runs
     *
     * @return int|null
     */
    protected function getTerminalWidth()
    {
        if (defined('PHP_WINDOWS_VERSION_BUILD') && ansicon = getenv('ANSICON')) {
            return preg_replace('{^(\d+)x.*}', '1', ansicon);
        }

        if (preg_match("{rows.(\d+);.columns.(\d+);}i", exec('stty -a | grep columns'), match)) {
            return match[1];
        }
    }

    /**
     * Tries to figure out the terminal height in which this application runs
     *
     * @return int|null
     */
    protected function getTerminalHeight()
    {
        if (defined('PHP_WINDOWS_VERSION_BUILD') && ansicon = getenv('ANSICON')) {
            return preg_replace('{^\d+x\d+ \(\d+x(\d+)\)}', '1', trim(ansicon));
        }

        if (preg_match("{rows.(\d+);.columns.(\d+);}i", exec('stty -a | grep columns'), match)) {
            return match[2];
        }
    }

    /**
     * Gets the name of the command based on input.
     *
     * @param InputInterface input The input interface
     *
     * @return string The command name
     */
    protected function getCommandName(InputInterface input)
    {
        return input.getFirstArgument('command');
    }

    /**
     * Gets the default input definition.
     *
     * @return InputDefinition An InputDefinition instance
     */
    protected InputDefinition getDefaultInputDefinition() {
        return new InputDefinition(/*array(
            new InputArgument('command', InputArgument::REQUIRED, 'The command to execute'),

            new InputOption('--help',           '-h', InputOption::VALUE_NONE, 'Display this help message.'),
            new InputOption('--quiet',          '-q', InputOption::VALUE_NONE, 'Do not output any message.'),
            new InputOption('--verbose',        '-v', InputOption::VALUE_NONE, 'Increase verbosity of messages.'),
            new InputOption('--version',        '-V', InputOption::VALUE_NONE, 'Display this application version.'),
            new InputOption('--ansi',           '',   InputOption::VALUE_NONE, 'Force ANSI output.'),
            new InputOption('--no-ansi',        '',   InputOption::VALUE_NONE, 'Disable ANSI output.'),
            new InputOption('--no-interaction', '-n', InputOption::VALUE_NONE, 'Do not ask any interactive question.'),
        )*/);
    }

    /**
     * Gets the default commands that should always be available.
     *
     * @return List<Command> An array of default Command instances
     */
    protected List<Command> getDefaultCommands() {
        return new ArrayList<Command>(new HelpCommand(), new ListCommand());
    }

    /**
     * Gets the default helper set with the helpers that should always be available.
     *
     * @return HelperSet A HelperSet instance
     */
    protected HelperSet getDefaultHelperSet() {
        return new HelperSet(/*array(
            new FormatterHelper(),
            new DialogHelper(),
        )*/);
    }

    /**
     * Sorts commands in alphabetical order.
     *
     * @param array commands An associative array of commands to sort
     *
     * @return array A sorted array of commands
     */
    private function sortCommands(commands)
    {
        namespacedCommands = array();
        foreach (commands as name => command) {
            key = this.extractNamespace(name, 1);
            if (!key) {
                key = '_global';
            }

            namespacedCommands[key][name] = command;
        }
        ksort(namespacedCommands);

        foreach (namespacedCommands as &commands) {
            ksort(commands);
        }

        return namespacedCommands;
    }

    /**
     * Returns abbreviated suggestions in string format.
     *
     * @param array abbrevs Abbreviated suggestions to convert
     *
     * @return string A formatted string of abbreviated suggestions
     */
    private function getAbbreviationSuggestions(abbrevs)
    {
        return sprintf('%s, %s%s', abbrevs[0], abbrevs[1], count(abbrevs) > 2 ? sprintf(' and %d more', count(abbrevs) - 2) : '');
    }

    /**
     * Returns the namespace part of the command name.
     *
     * @param string name  The full name of the command
     * @param string limit The maximum number of parts of the namespace
     *
     * @return string The namespace of the command
     */
    private function extractNamespace(name, limit = null)
    {
        parts = explode(':', name);
        array_pop(parts);

        return implode(':', null == limit ? parts : array_slice(parts, 0, limit));
    }

    /**
     * Finds alternative commands of name
     *
     * @param string name      The full name of the command
     * @param array  abbrevs   The abbreviations
     *
     * @return array A sorted array of similar commands
     */
    private function findAlternativeCommands(name, abbrevs)
    {
        callback = function(item) {
            return item.getName();
        };

        return this.findAlternatives(name, this.commands, abbrevs, callback);
    }

    /**
     * Finds alternative namespace of name
     *
     * @param string name      The full name of the namespace
     * @param array  abbrevs   The abbreviations
     *
     * @return array A sorted array of similar namespace
     */
    private function findAlternativeNamespace(name, abbrevs)
    {
        return this.findAlternatives(name, this.getNamespaces(), abbrevs);
    }

    /**
     * Finds alternative of name among collection,
     * if nothing is found in collection, try in abbrevs
     *
     * @param string                name       The string
     * @param array|Traversable     collection The collecion
     * @param array                 abbrevs    The abbreviations
     * @param Closure|string|array  callback   The callable to transform collection item before comparison
     *
     * @return array A sorted array of similar string
     */
    private function findAlternatives(name, collection, abbrevs, callback = null) {
        alternatives = array();

        foreach (collection as item) {
            if (null !== callback) {
                item = call_user_func(callback, item);
            }

            lev = levenshtein(name, item);
            if (lev <= strlen(name) / 3 || false !== strpos(item, name)) {
                alternatives[item] = lev;
            }
        }

        if (!alternatives) {
            foreach (abbrevs as key => values) {
                lev = levenshtein(name, key);
                if (lev <= strlen(name) / 3 || false !== strpos(key, name)) {
                    foreach (values as value) {
                        alternatives[value] = lev;
                    }
                }
            }
        }

        asort(alternatives);

        return array_keys(alternatives);
    }

}
