package java_cup;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import java.util.Vector;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * <code>JavaCUPTask</code> is an <a
 * href="http://jakarta.apache.org/ant/">ANT</a> task to invoke <a
 * href="http://www.cs.princeton.edu/~appel/modern/java/CUP/">JavaCUP</a>.
 *
 * Not all options are exposed yet.
 *
 * @author jhyde
 * @since 7 February, 2002
 * @version $Id$
 *
 * <hr/>
 *
 * <h2><a name="javacup">JavaCup</a></h2>
 * <h3>Description</h3> 
 * <p>
 * 	 Invokes the <a HREF="http://www.cs.princeton.edu/~appel/modern/java/CUP/" target="_top">CUP
 * Parser Generator for Java</a> on a grammar file.
 * </p>
 * <p>
 * 	 This task only invokes JavaCUP if the grammar file is newer than the
 *   generated Java files.
 * </p>
 * 
 * <h3>Parameters</h3>
 * <table border="1" cellpadding="2" cellspacing="0">
 * 	 <tr>
 * 	   <td valign="top"><b>Attribute</b></td>
 * 	   <td valign="top"><b>Description</b></td>
 * 	   <td align="center" valign="top"><b>Required</b></td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="input">input</a></td>
 * 	   <td valign="top">The name of the file from which to read the JavaCUP
 * 		 specification.</td>
 * 	   <td valign="top" align="center">Yes</td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="srcdir">srcdir</a></td>
 * 	   <td valign="top">The name of the root folder of the java class hierarchy.
 * 		 Default is the current folder.</td>
 * 	   <td valign="top" align="center">No</td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="package">package</a></td>
 * 	   <td valign="top">The name of the package generated classes go in. If not
 * 		 specified, this is deduced from the path of <code> input</code>, relative
 * 		 to <code>srcdir</code>.</td>
 * 	   <td valign="top" align="center">No</td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="parserClass">parserClass</a></td>
 * 	   <td valign="top">The full name of the parser class.
 * 		 If not specified, the class is derived from the input file: for example,
 * 		 if <code>input</code> is &quot;src/com/foo/MyParser.cup&quot;, and <code>srcdir</code>
 * 		 is &quot;src&quot;, the class will be &quot;com.foo.MyParser&quot;.</td>
 * 	   <td valign="top" align="center">No</td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="symbolsClass">symbolsClass</a></td>
 * 	   <td valign="top">The name of the class which holds the symbols constants. If not specified, this is the
 * 		 parser class name with "Sym" appended, for example &quot;com.foo.MyParserSym&quot;.</td>
 * 	   <td valign="top" align="center">No</td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="interface">interface</a></td>
 * 	   <td valign="top">If true, emit the symbol constant <i>interface</i>, rather
 * 		 than class.</td>
 * 	   <td valign="top" align="center">No</td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="nonterms">nonterms</a></td>
 * 	   <td valign="top">If true, put non-terminals in symbol constant class.</td>
 * 	   <td valign="top" align="center">No</td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="expect">expect</a></td>
 * 	   <td valign="top"><b>Number of conflicts expected/allowed.
 * 		 Default is 0.</td>
 * 	   <td valign="top" align="center">No</td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="compactRed">compactRed</a></td>
 * 	   <td valign="top">If true, compact tables by defaulting to most frequent reduce.</td>
 * 	   <td valign="top" align="center">No</td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="warn">warn</a></td>
 * 	   <td valign="top">If true, warn about useless productions, etc. Default is
 * 		 true.</td>
 * 	   <td valign="top" align="center">No</td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="summary">summary</a></td>
 * 	   <td valign="top">If true, print a summary of parser states, etc. Default is
 * 		 true.</td>
 * 	   <td valign="top" align="center">No</td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="progress">progress</a></td>
 * 	   <td valign="top">If true, print messages to indicate progress of the system.
 * 		 Default is false.</td>
 * 	   <td valign="top" align="center">No</td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="time">time</a></td>
 * 	   <td valign="top">If true, print time usage summary.</td>
 * 	   <td valign="top" align="center">No</td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="dumpGrammar">dumpGrammar</a></td>
 * 	   <td valign="top">If true, produce a dump of the symbols and grammar. Default
 * 		 is false.</td>
 * 	   <td valign="top" align="center">No</td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="dumpStates">dumpStates</a></td>
 * 	   <td valign="top">If true, produce a dump of the parse state machine. Default
 * 		 is false.</td>
 * 	   <td valign="top" align="center">No</td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="dumpTables">dumpTables</a></td>
 * 	   <td valign="top">If true, produce a dump of the parse tables. Default is
 * 		 false.</td>
 * 	   <td valign="top" align="center">No</td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="dump">dump</a></td>
 * 	   <td valign="top">If true, switch on <code>dumpGrammar</code>, <code>dumpStates</code>,
 * 		 and <code>dumpTables</code>. Default is false.</td>
 * 	   <td valign="top" align="center">No</td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="debug">debug</a></td>
 * 	   <td valign="top">If true, turn on debugging messages within JavaCUP. Default
 * 		 is false.</td>
 * 	   <td valign="top" align="center">No</td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="positions">positions</a></td>
 * 	   <td valign="top">If true, generate positions code. Default is true.</td>
 * 	   <td valign="top" align="center">No</td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="scanner">scanner</a></td>
 * 	   <td valign="top">If false, don't refer to <code> java_cup.runtime.Scanner</code> in the parser
 * 		 (for compatibility with old runtimes). Default is true.</td>
 * 	   <td valign="top" align="center">No</td>
 * 	 </tr>
 * 	 <tr>
 * 	   <td valign="top"><a name="version">version</a></td>
 * 	   <td valign="top">If true, print version information for JavaCUP and halt.</td>
 * 	   <td valign="top" align="center">No</td>
 * 	 </tr>
 * </table>
 * 
 * <h3>Example</h3>
 * <blockquote><pre>&lt;javacup
 * 	   input=&quot;src/com/foo/Parser.cup&quot;
 * 	   warn=&quot;false&quot;
 * 	   summary=&quot;false&quot;/&gt;</pre></blockquote>
 * <p>
 * 	 This invokes JavaCUP on grammar file src/com/foo/Parser.cup, generating
 * src/com/foo/Parser.java and src/com/foo/ParserSym.java.
 * </p>
 * 
 * <hr/>
 **/
public class JavaCUPTask extends Task {

	String parserClass;
	String symbolsClass;
	String inputFileName;
	File srcDir = new File(System.getProperty("user.dir"));
	boolean newer = true;
	Vector argVector = new Vector();

	public void execute() throws BuildException {
		try {
			String[] args = getArgs();
			Main.main(args, inputFileName);
		} catch (IOException e) {
			throw new BuildException(e.toString());
		} catch (internal_error e) {
			throw new BuildException(e.toString());
		} catch (Exception e) {
			throw new BuildException(e.toString());
		}
	}

	private String[] getArgs()
	{
		if (parserClass == null) {
			parserClass = Main.fileNameToClass(relativeFileName(inputFileName));
		}
		String packageName, className;
		int lastDot = parserClass.lastIndexOf(".");
		if (lastDot < 0) {
			packageName = null;
			className = parserClass;
		} else {
			packageName = parserClass.substring(0, lastDot);
			className = parserClass.substring(lastDot + 1);
		}
		if (packageName != null) {
			argVector.addElement("-package");
			argVector.addElement(packageName);
		}
		argVector.addElement("-parser");
		argVector.addElement(className);
		if (symbolsClass == null) {
			symbolsClass = className + "Sym";
		}
		argVector.addElement("-symbols");
		argVector.addElement(symbolsClass);
		if (inputFileName == null) {
			throw new BuildException("You must specify inputFile.");
		}
		if (newer) {
			argVector.addElement("-newer");
		}
		String[] args = new String[argVector.size()];
		argVector.copyInto(args);
		return args;
	}

	/** See parameter <code><a href="#parserClass">parserClass</a></code>. **/
	public void setParserClass(String parserClass) {
		this.parserClass = parserClass;
	}

	/** See parameter <code><a href="#symbolsClass">symbolsClass</a></code>. **/
	public void setSymbolsClass(String symbolsClass) {
		this.symbolsClass = symbolsClass;
	}

	/** See parameter <code><a href="#dump">dump</a></code>. **/
	public void setDump(boolean dump) {
		if (dump) {
			argVector.addElement("-dump");
		}
	}

	/** See parameter <code><a href="#dumpGrammar">dumpGrammar</a></code>. **/
	public void setDumpGrammar(boolean dumpGrammar) {
		if (dumpGrammar) {
			argVector.addElement("-dump_grammar");
		}
	}

	/** See parameter <code><a href="#dumpStates">dumpStates</a></code>. **/
	public void setDumpStates(boolean dumpStates) {
		if (dumpStates) {
			argVector.addElement("-dump_states");
		}
	}

	/** See parameter <code><a href="#dumpTables">dumpTables</a></code>. **/
	public void setDumpTables(boolean dumpTables) {
		if (dumpTables) {
			argVector.addElement("-dump_tables");
		}
	}

	/** See parameter <code><a href="#nonterms">nonterms</a></code>. **/
	public void setNonterms(boolean nonterms) {
		if (nonterms) {
			argVector.addElement("-nonterms");
		}
	}

	/** See parameter <code><a href="#expect">expect</a></code>. **/
	public void setExpect(int expect) {
		argVector.addElement("-expect");
		argVector.addElement(expect + "");
	}

	/** See parameter <code><a href="#compactRed">compactRed</a></code>. **/
	public void setCompactRed(boolean compactRed) {
		if (compactRed) {
			argVector.addElement("-compact_red");
		}
	}

	/** See parameter <code><a href="#warn">warn</a></code>. **/
	public void setWarn(boolean warn) {
		if (!warn) {
			argVector.addElement("-nowarn");
		}
	}

	/** See parameter <code><a href="#summary">summary</a></code>. **/
	public void setSummary(boolean summary) {
		if (!summary) {
			argVector.addElement("-nosummary");
		}
	}

	/** See parameter <code><a href="#positions">positions</a></code>. **/
	public void setPositions(boolean positions) {
		if (positions) {
			argVector.addElement("-positions");
		}
	}

	/** See parameter <code><a href="#scanner">scanner</a></code>. **/
	public void setScanner(boolean scanner) {
		if (!scanner) {
			argVector.addElement("-noscanner");
		}
	}

	/** See parameter <code><a href="#progress">progress</a></code>. **/
	public void setProgress(boolean progress) {
		if (progress) {
			argVector.addElement("-progress");
		}
	}

	/** See parameter <code><a href="#time">time</a></code>. **/
	public void setTime(boolean time) {
		if (time) {
			argVector.addElement("-time");
		}
	}

	/** See parameter <code><a href="#debug">debug</a></code>. **/
	public void setDebug(boolean debug) {
		if (debug) {
			argVector.addElement("-debug");
		}
	}

	/** See parameter <code><a href="#version">version</a></code>. **/
	public void setVersion(boolean version) {
		if (version) {
			argVector.addElement("-version");
		}
	}

	/** See parameter <code><a href="#newer">newer</a></code>. **/
	public void setNewer(boolean newer) {
		this.newer = newer;
	}

	/** See parameter <code><a href="#interface">interface</a></code>. **/
	public void setInterface(boolean _interface) {
		if (_interface) {
			argVector.addElement("-interface");
		}
	}

	/** See parameter <code><a href="#input">input</a></code>. **/
	public void setInput(File inputFile) {
		inputFileName = inputFile.toString();
	}

	/** See parameter <code><a href="#srcdir">srcdir</a></code>. **/
	public void setSrcDir(File srcDir) {
		this.srcDir = srcDir;
		argVector.addElement("-srcdir");
		argVector.addElement(srcDir.toString());
	}

    /**
     * Makes a filename relative, by subtracting off the current directory, if
     * possible.
     **/
	String relativeFileName(String fileName)
    {
		char fileSep = System.getProperty("file.separator").charAt(0); // e.g. '/'
		String srcDirName = srcDir.toString() + fileSep; // e.g. 'E:/foo/'
		if (fileName.toUpperCase().startsWith(srcDirName.toUpperCase())) {
			fileName = fileName.substring(srcDirName.length());
		}
		return fileName;
    }
}


// End JavaCUPTask.java
