package com.tomrochette.linkchecker;

import java.net.MalformedURLException;
import java.net.URL;

public class URLHelper
{
	public URL anchorToAbsoluteURL(String baseURL, String anchor)
	{
		try
		{
			URL baseUrl = new URL(baseURL);
			return new URL(baseUrl, anchor);
		}
		catch (MalformedURLException e)
		{
			return null;
		}
	}

	public boolean isSameDomain(String source, String destination)
	{
		try
		{
			URL sourceUrl = new URL(source);
			URL destinationUrl = new URL(destination);
			return isSameDomain(sourceUrl, destinationUrl);
		}
		catch (MalformedURLException e)
		{
			return false;
		}
	}

	public boolean isSameDomain(URL sourceUrl, URL destinationUrl)
	{
		return sourceUrl.getHost().equals(destinationUrl.getHost());
	}
}
