package stream_demoTerminal;

import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Scanner;

/*
	 			|-	FileInputStream					
	 			|-	PipedInputStream			|-  LineNumberInputStream
	 			|-	FilterInputStream	--------|-	DataInputStream
InputStream	----|-	ByteArrayInputStream		|-	BufferedInputStream
	 			|-	SequenceInputStream			|-	PushbackInputStream
	 			|-	StringBufferInputStream
	 			|-	ObjectInputStream
	 			
	 			
		 		|-	FileOutputStream					
	 			|-	PipedOutputStream			|-  DataOutputStream
OutputStream ---|-	FilterOutputStream	--------|-	BufferedOutputStream
				|-	ByteArrayOutputStream		|-	PrintStream
	 			|-	ObjectOutputStream
	 		
	 
	 			|-	BufferedReader		--------	LineNumberReader		
	 			|-	CharArrayReader		
	 			|-	InputStreamReader	--------	FileReader
Reader	--------|-	FilerReader			--------	PushbackReader
	 			|-	PipeReader
	 			|-	StringReader
	 				 
	 				 
				|-	BufferedWriter
	 			|-	CharArrayWriter		
	 			|-	OutputStreamReader	--------	FileWriter
Writer	--------|-	FilerWriter	
	 			|-	PipeWriter
	 			|-	StringWriter
	 			|-	FilterWriter
	 			
	 			
File, Files
 */
/*
 *  java beginners: how to code basic linux commands running on console windows
	Linux commands: 
	pwd: prints the full name (the full path) of current/working directory
	ls and ls dirPath: list the files and directories in a directory
	cd: change the current directory of the terminal
		+) cd
		+) cd path
		+) cd ..
	mkdir path: creates directories
	rmdir path: remove empty directories
	touch path: create empty files
	rm path: remove (delete) files
	rm -r path: recursively remove directories and their contents
	cp Spath Dpath: copy files and directories from one location to another.
	mv Spath Dpath: move or rename files and directories
	cat path: display the contents of file
*/
public class MyTerminal {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public String username = "pblues";
	public final String defaultDirectory = "src";
	File temp = new File(defaultDirectory);

	public void openTerminal() {

		Scanner sc = new Scanner(System.in);
		String cm = "";

		while (!cm.equals("exit")) {
			System.out.print(
					ANSI_GREEN + username + "@Computer: " + ANSI_BLUE + temp.getAbsolutePath() + ANSI_RESET + "$ ");
			cm = sc.nextLine();
			cm.trim();
			/*
			 * pwd:
			 */
			if (cm.equals("pwd")) {
				System.out.println(temp.getAbsolutePath());
			}
			// ls and ls filePath
			else if (cm.length() >= 2 && cm.substring(0, 2).equals("ls")) {
				if (cm.equals("ls"))
					for (String e : temp.list())
						System.out.println(e);
				else {
					String filePath = validateAndNormalizePath(cm.substring(3, cm.length()));
					if (checkFileExists(filePath))
						for (String e : new File(filePath).list())
							System.out.println(e);
					else
						System.out.println("ls: cannot access '" + filePath + "': No such file or directory");
				}
			}
			// exit
			else if (cm.equals("exit")) {
				System.out.println("Bye");
				System.exit(0);
			}
			/*
			 * cd cd .. cd pathFolder
			 */
			else if (cm.length() >= 2 && cm.substring(0, 2).equals("cd")) {
				if (cm.equals("cd")) {
					temp = new File(defaultDirectory);
				} else if (cm.equals("cd ..")) {
					String workingDirectory = (temp.getAbsolutePath().substring(0,
							temp.getAbsolutePath().lastIndexOf("/")));
					temp = new File(workingDirectory);
				} else if (cm.length() > 3 && cm.substring(0, 3).equals("cd ")) {
					String dirPath = validateAndNormalizePath(cm.substring(3, cm.length()));

					if (checkFileExists(dirPath)) {
						String workingDirectory = (temp.getPath() + "/" + dirPath);
						temp = new File(workingDirectory);
					} else
						System.out.println("cd: " + dirPath + ": No such file or directory");
				}
			}
			// touch:
			else if (cm.length() > 6 && cm.substring(0, 6).equals("touch ")) {
				String filePath = validateAndNormalizePath(cm.substring(6, cm.length()));
				System.out.println(filePath);
				createFile(filePath);
			}
			// mkdir:
			else if (cm.length() > 6 && cm.substring(0, 6).equals("mkdir ")) {
				String dirPath = validateAndNormalizePath(cm.substring(6, cm.length()));
				File myFile = new File(dirPath);
				myFile.mkdir();
			}
			// rmdir:
			else if (cm.length() > 6 && cm.substring(0, 6).equals("rmdir ")) {
				String dirPath = validateAndNormalizePath(cm.substring(6, cm.length()));
				File myFile = new File(dirPath);
				if (myFile.exists()) {
					if (myFile.isDirectory()) {
						if (myFile.list().length == 0)
							myFile.delete();
						else if (myFile.isDirectory() && myFile.list().length != 0)
							System.out.println("rmdir: failed to remove '" + cm.substring(6, cm.length())
									+ "': Directory not empty");
					} else {
						System.out.println(
								"rmdir: failed to remove '" + cm.substring(6, cm.length()) + "': Not a directory");
					}
				}
			}
			// rm, rm -r
			else if (cm.length() > 3 && cm.substring(0, 3).equals("rm ")) {
				if (cm.length() > 6 && cm.substring(0, 6).equals("rm -r ")) {
					String filePath = validateAndNormalizePath(cm.substring(6, cm.length()));
					deleteFile(filePath);
				} else {
					// rm
					String filePath = validateAndNormalizePath(cm.substring(3, cm.length()));
					File myFile = new File(filePath);
					if (myFile.exists()) {
						if (!myFile.isDirectory()) {
							myFile.delete();
						} else
							System.out.println("rm: cannot remove '" + cm.substring(cm.lastIndexOf(" "), cm.length())
									+ "': Is a directory");
					}
				}
			}
			// cp
			else if (cm.length() > 3 && cm.substring(0, 3).equals("cp ")) {
				int indexSpace2 = cm.lastIndexOf(" ");
				String s = validateAndNormalizePath(cm.substring(3, indexSpace2));
				String d = validateAndNormalizePath(cm.substring(indexSpace2 + 1, cm.length()));
				copy(s, d);
			}
			// mv
			else if (cm.length() > 3 && cm.substring(0, 3).equals("mv ")) {
				int indexSpace2 = cm.lastIndexOf(" ");
				String s = validateAndNormalizePath(cm.substring(3, indexSpace2));
				String d = validateAndNormalizePath(cm.substring(indexSpace2 + 1, cm.length()));
				move(s, d);
			}
			// cat file
			else if (cm.length() > 4 && cm.substring(0, 4).equals("cat ")) {
				String filePath = validateAndNormalizePath(cm.substring(4, cm.length()));
//				System.out.println(filePath);
				displayLines(filePath);
			}
			// command not found:
			else {
				System.out.println(cm + ": command not found");
			}
		}
	}

	public static void main(String[] args) {
		new MyTerminal().openTerminal();
		;
	}

	private boolean checkFileExists(String path) {
		return new File(path).exists() ? true : false;
	}

	private String validateAndNormalizePath(String path) {
		String tempPath = checkFileExists(path) ? path : Paths.get(temp.getAbsolutePath(), path).toString();
		// nếu +"/"+path thì /Dir hay Dir thì mệt hơn
		return checkFileExists(tempPath) ? tempPath : path;
	}

	private static void createFile(String filePath) {
		File newFile = new File(filePath);
		// check exist
		if (newFile.exists()) {
			System.out.println("Another file with the same name already exists in "
					+ newFile.getPath().substring(0, newFile.getPath().lastIndexOf("/")));
		} else {
			try {
				newFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void deleteFile(String path) {
		File myFile = new File(path);
		// cần delete file con trước
		if (myFile.isDirectory()) {
			for (File subFile : myFile.listFiles()) {
				deleteFile(subFile.getAbsolutePath());
			}
		}
		myFile.delete();
	}

	private static void rename(String path, String newNameFile) {
		File myFile = new File(path);
		if (myFile.exists()) {
			if (myFile.isDirectory()) {
				myFile.renameTo(new File(myFile.getParent().concat("/" + newNameFile)));
				System.out.println("The folder is renamed");
			} else {
				String end = myFile.getPath().substring(myFile.getPath().lastIndexOf("."), myFile.getPath().length());
				myFile.renameTo(new File(myFile.getParent().concat("/" + newNameFile + end)));
				System.out.println("The file is renamed");
			}
		}
	}

	private static void copy(String sourcePath, String destinationPath) {
		try {
			Path s = Path.of(sourcePath);
			Files.copy(s, Path.of(destinationPath).resolve(s.getFileName()), StandardCopyOption.COPY_ATTRIBUTES);
			File myFile = new File(sourcePath);
			if (myFile.isDirectory()) {
				for (File f : myFile.listFiles()) {
					copy(f.getAbsolutePath(), destinationPath + "/" + s.getFileName());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void move(String sourcePath, String destinationPath) {
		try {
			Path s = Path.of(sourcePath);
			Files.move(s, Path.of(destinationPath).resolve(s.getFileName()), StandardCopyOption.COPY_ATTRIBUTES);
			File myFile = new File(sourcePath);
			if (myFile.isDirectory()) {
				for (File f : myFile.listFiles()) {
					copy(f.getAbsolutePath(), destinationPath + "/" + s.getFileName());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void displayLines(String filePath) {
		try {
			Scanner scan = new Scanner((new FileReader(new File(filePath))));
			while (scan.hasNext()) {
				// Read the next name.
				String familyName = scan.nextLine();
				// Display the last name read.
				System.out.println(familyName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
