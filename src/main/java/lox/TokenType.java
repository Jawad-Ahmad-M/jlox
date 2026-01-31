package lox;

public enum TokenType {

    //Single character Tokens
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT , MINUS, PLUS,  STAR, SLASH, SEMICOLON,

    // One or two character tokens
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    // Literals
    IDENTIFIER, NUMBER, STRING,

    // KEYWORDS
    VAR, CLASS, THIS, SUPER, FUN,
    TRUE, FALSE,
    WHILE, FOR,
    IF, ELSE,
    NIL,
    AND, OR,
    PRINT, RETURN,

    EOF
}
