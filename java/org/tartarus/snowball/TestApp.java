
package org.tartarus.snowball;

import java.lang.reflect.Method;
import java.io.Reader;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.FileOutputStream;

public class TestApp {
    private static void usage()
    {
        System.err.println("Usage: TestApp <algorithm> <input file> [-o <output file>]");
    }

    public static void main(String [] args) throws Throwable {
	if (args.length < 2) {
            usage();
            return;
        }

	Class stemClass = Class.forName("org.tartarus.snowball.ext." +
					args[0] + "Stemmer");
        SnowballStemmer stemmer = (SnowballStemmer) stemClass.newInstance();

	Reader reader;
	reader = new InputStreamReader(new FileInputStream(args[1]));
	reader = new BufferedReader(reader);

	StringBuffer input = new StringBuffer();

        OutputStream outstream;

	if (args.length > 2) {
            if (args.length == 4 && args[2].equals("-o")) {
                outstream = new FileOutputStream(args[3]);
            } else {
                usage();
                return;
            }
	} else {
	    outstream = System.out;
	}
	Writer output = new OutputStreamWriter(outstream);
	output = new BufferedWriter(output);

	int character;
	while ((character = reader.read()) != -1) {
	    char ch = (char) character;
	    if (Character.isWhitespace(ch)) {
		stemmer.setCurrent(input.toString());
		stemmer.stem();
		output.write(stemmer.getCurrent());
		output.write('\n');
		input.delete(0, input.length());
	    } else {
		input.append(ch < 127 ? Character.toLowerCase(ch) : ch);
	    }
	}
	output.flush();
    }
}
