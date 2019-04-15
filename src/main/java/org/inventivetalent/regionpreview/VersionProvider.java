package org.inventivetalent.regionpreview;

import picocli.CommandLine;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class VersionProvider implements CommandLine.IVersionProvider {
	@Override
	public String[] getVersion() throws Exception {
		URLClassLoader cl = (URLClassLoader) getClass().getClassLoader();
		URL url = cl.findResource("META-INF/MANIFEST.MF");
		Manifest manifest = new Manifest(url.openStream());
		Attributes attributes = manifest.getMainAttributes();
		return new String[] { attributes.getValue("Preview-Version") };
	}
}
