package com.codechallange.argument;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArgumentValidator {
    public boolean checkFirstArgumentName(String argumentName, String[] args) {
        if (args.length == 0) {
            throw new InvalidCommandFormatException();
        }

        return argumentName.equalsIgnoreCase(args[0]);
    }

    public void validateArgumentCount(List<Integer> allowedArgumentCount, int argsCount) {
        if (!allowedArgumentCount.contains(argsCount)) {
            throw new InvalidCommandFormatException();
        }
    }
}
