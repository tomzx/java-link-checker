package com.tomrochette.linkchecker;

import org.apache.commons.cli.*;

public class CommandLine
{
	protected String[] arguments;
	protected org.apache.commons.cli.CommandLine commandLine;

	public boolean run(String[] args)
	{
		Options options = getOptions();
		CommandLineParser parser = new DefaultParser();
		try
		{
			this.commandLine = parser.parse(options, args);
		}
		catch (ParseException e)
		{
			System.err.println("Could not parse command line. Reason: " + e.getMessage());
			return false;
		}

		this.arguments = this.commandLine.getArgs();

		if (this.commandLine.hasOption("help") || args.length == 0)
		{
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("link-checker [url]", options);
			return false;
		}
		return true;
	}

	public String[] getArguments()
	{
		return this.arguments;
	}

	public org.apache.commons.cli.CommandLine getCommandLine()
	{
		return this.commandLine;
	}

	public Options getOptions()
	{
		Options options = new Options();
		options.addOption("h", "help", false, "Displays this help");
		options.addOption("r", "recursive", false, "Follow links on the same domains");
		options.addOption("i", "internal", false, "Test internal links only (within the same domain)");
		return options;
	}
}
