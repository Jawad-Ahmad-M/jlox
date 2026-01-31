package lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static lox.TokenType.*;

public class Scanner {
    String  source;
    List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();

        keywords.put("var", VAR);
        keywords.put("class", CLASS);
        keywords.put("this", THIS);
        keywords.put("super",  SUPER);
        keywords.put("fun",FUN);

        keywords.put("true", TRUE);
        keywords.put("false", FALSE);

        keywords.put("if", IF);
        keywords.put("else", ELSE);

        keywords.put("while", WHILE);
        keywords.put("for", FOR);

        keywords.put("nil", NIL);

        keywords.put("and", AND);
        keywords.put("or", OR);

        keywords.put("print",  PRINT);
        keywords.put("return", RETURN);
    }

    public Scanner(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while(!isAtEnd()){
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "",null, line));
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void scanToken() {
        char c = advance();
        switch (c){
            case '(' : addToken(LEFT_PAREN); break;
            case ')' : addToken(RIGHT_PAREN); break;
            case '{' : addToken(LEFT_BRACE); break;
            case '}' : addToken(RIGHT_BRACE); break;
            case ',' : addToken(COMMA); break;
            case '.' : addToken(DOT); break;
            case '+' : addToken(PLUS); break;
            case '-' : addToken(MINUS); break;
            case '*' : addToken(STAR); break;
            case ';' : addToken(SEMICOLON); break;


            case '/' :
                if (match('/')) {
                    // it goes for comment, and comment goes to the last of line
                    while(peek() != '\n' && isAtEnd()) advance();
                } else {
                    addToken(SLASH);
                }
                break;

            case '!' : addToken(match('=') ? BANG_EQUAL : BANG); break;
            case '=' : addToken(match('=') ? EQUAL_EQUAL : EQUAL); break;
            case '<' : addToken(match('=') ? LESS_EQUAL : LESS); break;
            case '>' : addToken(match('=') ? GREATER_EQUAL : GREATER); break;

            case ' ' : case '\t' : case '\r' : // Ignore White Space
                break;

            case '\n' :
                line++;
                break;

            case '"' : string(); break;

            default:
                if (isDigit(c)){
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Lox.error(line, "Unexpected character in expression.");
                }
                break;
        }
    }

    private boolean match(char expected) {
        if(isAtEnd()) return false;
        if(source.charAt(current) != expected) return false;
        current++;
        return true;
    }

    private void string() {
        while(peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        if(isAtEnd()){
            Lox.error(line, "Unterminated String.");
        }

        advance();
        String text =  source.substring(start + 1, current - 1);
        addToken(STRING, text);
    }

    private void number() {
        while(isDigit(peek())) advance();

        if (peek() == '.' && isDigit(peekNext())) {
            do advance();
            while (isDigit(peek()));
        }

        addToken(NUMBER, source.substring(start, current));
    }

    private void identifier() {
        while(isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if  (type == null) type = IDENTIFIER;
        addToken(type);
    }

    private char peek() {
        if(isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c == '_');
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }
    private void addToken(TokenType type, String literal) {
        String text =  source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

}
