package com.epam.mjc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class MethodParser {

    /**
     * Parses string that represents a method signature and stores all it's members into a {@link MethodSignature} object.
     * signatureString is a java-like method signature with following parts:
     * 1. access modifier - optional, followed by space: ' '
     * 2. return type - followed by space: ' '
     * 3. method name
     * 4. arguments - surrounded with braces: '()' and separated by commas: ','
     * Each argument consists of argument type and argument name, separated by space: ' '.
     * Examples:
     * accessModifier returnType methodName(argumentType1 argumentName1, argumentType2 argumentName2)
     * private void log(String value)
     * Vector3 distort(int x, int y, int z, float magnitude)
     * public DateTime getCurrentDateTime()
     *
     * @param signatureString source string to parse
     * @return {@link MethodSignature} object filled with parsed values from source string
     */
    public MethodSignature parseFunction(String signatureString) {
        var openingBracketIndex = signatureString.indexOf('(');
        var enclosingBracketIndex = signatureString.indexOf(')');

        if (!(-1 < openingBracketIndex && openingBracketIndex < enclosingBracketIndex)) {
            throw new IllegalArgumentException("Bracket pair was not found");
        }

        var modifiersTokenizer = new StringTokenizer(signatureString.substring(0, openingBracketIndex), " ");
        var tokensCount = modifiersTokenizer.countTokens();
        if (tokensCount != 2 && tokensCount != 3) {
            throw new IllegalArgumentException("There must be either 2 or 3 words before brackets.");
        }

        var accessModifier = tokensCount == 2 ? null : modifiersTokenizer.nextToken();
        var returnType = modifiersTokenizer.nextToken();
        var methodName = modifiersTokenizer.nextToken();

        var argumentsString = signatureString
                .substring(openingBracketIndex + 1, enclosingBracketIndex)
                .replace(", ", ",");

        var arguments = parseArguments(argumentsString);

        var signature = new MethodSignature(methodName, Collections.unmodifiableList(arguments));
        signature.setAccessModifier(accessModifier);
        signature.setReturnType(returnType);
        return signature;
    }

    private List<MethodSignature.Argument> parseArguments(String argumentsString) {
        var arguments = new ArrayList<MethodSignature.Argument>();

        var argumentsTokenizer = new StringTokenizer(argumentsString, ",");
        if (argumentsString.contains(",,")) {
            throw new IllegalArgumentException("Empty argument found.");
        }

        while (argumentsTokenizer.hasMoreTokens()) {
            var twoWords = argumentsTokenizer.nextToken().split(" ");
            if (twoWords.length != 2) {
                throw new IllegalArgumentException("Malformed argument found.");
            }
            var type = twoWords[0];
            var name = twoWords[1];
            arguments.add(new MethodSignature.Argument(type, name));
        }

        return arguments;
    }
}
