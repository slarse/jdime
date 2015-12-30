package de.fosd.jdime.config;

import java.util.Arrays;
import java.util.Optional;

import de.fosd.jdime.strdump.DumpMode;
import de.uni_passau.fim.seibt.kvconfig.sources.ConfigSource;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineConfigSource extends ConfigSource {

    /*
     * These constants define the (short) parameter names expected on the command line. Corresponding Options
     * are constructed in buildCliOptions().
     */
    public static final String CLI_LOG_LEVEL = "log";
    public static final String CLI_CONSECUTIVE = "c";
    public static final String CLI_DIFFONLY = "d";
    public static final String CLI_FORCE_OVERWRITE = "f";
    public static final String CLI_HELP = "h";
    public static final String CLI_KEEPGOING = "k";
    public static final String CLI_LOOKAHEAD = "lah";
    public static final String CLI_MODE = "m";
    public static final String CLI_DUMP = "dmp";
    public static final String CLI_OUTPUT = "o";
    public static final String CLI_RECURSIVE = "r";
    public static final String CLI_STATS = "s";
    public static final String CLI_PRINT = "p";
    public static final String CLI_QUIET = "q";
    public static final String CLI_VERSION = "v";

    public static final String ARG_LIST = "ARG_LIST";
    private String argListSep = ",";

    private Options options;
    private CommandLine cmdLine;

    public CommandLineConfigSource(String[] args) throws ParseException {
        this(args, DEFAULT_PRIORITY);
    }

    public CommandLineConfigSource(String[] args, int priority) throws ParseException {
        this(args, priority, null, null);
    }

    public CommandLineConfigSource(String[] args, int priority, String prefix, String suffix) throws ParseException {
        super(priority, prefix, suffix);

        this.options = buildCliOptions();
        this.cmdLine = new DefaultParser().parse(options, args);
    }

    /**
     * Builds the <code>Options</code> instance describing the JDime command line configuration options.
     *
     * @return the <code>Options</code> instance
     */
    private Options buildCliOptions() {
        Options options = new Options();
        Option o;

        o = Option.builder(CLI_LOG_LEVEL)
                .longOpt("log-level")
                .desc("Set the logging level to one of (OFF, SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, ALL).")
                .hasArg()
                .argName("level")
                .build();

        options.addOption(o);

        o = Option.builder(CLI_CONSECUTIVE)
                .longOpt("consecutive")
                .desc("Requires diffonly mode. Treats versions as consecutive versions.")
                .hasArg(false)
                .build();

        options.addOption(o);

        o = Option.builder(CLI_DIFFONLY)
                .longOpt("diffonly")
                .desc("Only perform the diff stage.")
                .hasArg(false)
                .build();

        options.addOption(o);

        o = Option.builder(CLI_FORCE_OVERWRITE)
                .longOpt("force-overwrite")
                .desc("Force overwriting of output files.")
                .hasArg(false)
                .build();

        options.addOption(o);

        o = Option.builder(CLI_HELP)
                .longOpt("help")
                .desc("Print this message.")
                .hasArg(false)
                .build();

        options.addOption(o);

        o = Option.builder(CLI_KEEPGOING)
                .longOpt("keepgoing")
                .desc("Keep running after exceptions.")
                .hasArg(false)
                .build();

        options.addOption(o);

        o = Option.builder(CLI_LOOKAHEAD)
                .longOpt("lookahead")
                .desc("Use heuristics for matching. Supply off, full, or a number as argument.")
                .hasArg()
                .argName("level")
                .build();

        options.addOption(o);

        o = Option.builder(CLI_MODE)
                .longOpt("mode")
                .desc("Set the mode to one of (unstructured, structured, autotuning, dumptree, dumpgraph, dumpfile, " +
                        "prettyprint, nway)")
                .hasArg()
                .argName("mode")
                .build();

        options.addOption(o);

        {
            String formats = Arrays.stream(DumpMode.values()).map(Enum::name).reduce("", (s, s2) -> s + " " + s2);

            o = Option.builder(CLI_DUMP)
                    .longOpt("dump")
                    .desc("Dumps the inputs using one of the formats: " + formats)
                    .hasArg()
                    .argName("format")
                    .build();

            options.addOption(o);
        }

        o = Option.builder(CLI_OUTPUT)
                .longOpt("output")
                .desc("Set the output directory/file.")
                .hasArg()
                .argName("file")
                .build();

        options.addOption(o);

        o = Option.builder(CLI_RECURSIVE)
                .longOpt("recursive")
                .desc("Merge directories recursively.")
                .hasArg(false)
                .build();

        options.addOption(o);

        o = Option.builder(CLI_STATS)
                .longOpt("stats")
                .desc("Collect statistical data about the merge.")
                .hasArg(false)
                .build();

        options.addOption(o);

        o = Option.builder(CLI_PRINT)
                .longOpt("print")
                .desc("(print/pretend) Prints the merge result to stdout instead of an output file.")
                .hasArg(false)
                .build();

        options.addOption(o);

        o = Option.builder(CLI_QUIET)
                .longOpt("quiet")
                .desc("Do not print the merge result to stdout.")
                .hasArg(false)
                .build();

        options.addOption(o);

        o = Option.builder(CLI_VERSION)
                .longOpt("version")
                .desc("Print the version information and exit.")
                .hasArg(false)
                .build();

        options.addOption(o);

        return options;
    }

    /**
     * Returns the separator used when concatenating the values from {@link CommandLine#getArgList()} before returning
     * them in {@link #get(String)}.
     *
     * @return the current separator
     */
    public String getArgListSep() {
        return argListSep;
    }

    /**
     * When requesting the left over arguments of the commandline ({@link CommandLine#getArgList()}) using the key {@link #ARG_LIST} they are
     * concatenated into one <code>String</code> using the separator string set by this method.
     *
     * @param argListSep
     *         the new separator
     */
    public void setArgListSep(String argListSep) {
        this.argListSep = argListSep;
    }

    @Override
    protected Optional<String> getMapping(String key) {

        if (ARG_LIST.equals(key)) {
            return Optional.of(String.join(argListSep, cmdLine.getArgList()));
        }

        if (!options.hasOption(key)) {
            return Optional.empty();
        }

        Option opt = options.getOption(key);
        String optName = opt.getOpt();

        if (!cmdLine.hasOption(optName)) {
            return Optional.empty();
        }

        if (opt.hasArg()) {
            return Optional.of(cmdLine.getOptionValue(optName));
        } else {
            return Optional.of("true");
        }
    }
}
