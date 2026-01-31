package lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


public class LOX {

    /** Check how many arguments we pass during our command and then work accordingly like
     * for more than 1 we are provided with usage,
     * if equal to 1 then we read that file and then execute it
     * if equal to 0 then we provide things like python REPL, line by line execution.
     */
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox script");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    public static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
    }

    public static void runPrompt() throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        for(;;){
            System.out.print("> ");
            String line = br.readLine();
            if (line == null) break;
            run(line);
        }
    }

    public static void run(String source) throws IOException {
        Scanner scanner = new Scanner(source);
        List<Token>  tokens = scanner.scanTokens();

        for(Token token : tokens){
            System.out.println(token);
        }
    }

}
