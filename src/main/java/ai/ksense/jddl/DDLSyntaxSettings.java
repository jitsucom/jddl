package ai.ksense.jddl;

import java.sql.Connection;

public class DDLSyntaxSettings {
    private boolean upperCaseIdentifiers = true;
    private char identifiesQuoteChar = '\"';

    public boolean isUpperCaseIdentifiers() {
        return upperCaseIdentifiers;
    }

    public DDLSyntaxSettings setUpperCaseIdentifiers(boolean upperCaseIdentifiers) {
        this.upperCaseIdentifiers = upperCaseIdentifiers;
        return this;
    }

    public char getIdentifiesQuoteChar() {
        return identifiesQuoteChar;
    }

    public DDLSyntaxSettings setIdentifiesQuoteChar(char identifiesQuoteChar) {
        this.identifiesQuoteChar = identifiesQuoteChar;
        return this;
    }

    public String id(String id) {
        if (upperCaseIdentifiers) {
            id = id.toUpperCase();
        }
        return identifiesQuoteChar + id + identifiesQuoteChar;
    }
}
