package com.tomrochette.linkchecker;

import com.mashape.unirest.http.Unirest;
import org.apache.commons.cli.*;

import java.io.IOException;

public class LinkCheckerMain
{
	public static final String VERSION = "0.1.0";

	public static void main(String[] args)
	{
		CommandLine cmd = new CommandLine();
		if (!cmd.run(args)) {
			return;
		}
		args = cmd.getArguments();

		setUp();
		try
		{
			new LinkChecker().crawl(args[0]);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		tearDown();
	}

	protected static void setUp()
	{
		Unirest.setDefaultHeader("user-agent", "com.tomrochette.linkchecker/" + VERSION);
	}

	protected static void tearDown()
	{
		System.out.println(CrawlDB.getInstance().getUniqueUrlCount() + " unique url(s) crawled.");
		try
		{
			Unirest.shutdown();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static Options getOptions()
	{
		Options options = new Options();
		options.addOption("h", "help", false, "Displays this help");
		options.addOption("r", "recursive", false, "Follow links on the same domains");
		options.addOption("i", "internal", false, "Test internal links only (within the same domain)");
		return options;
	}
}
