/*
 * The MIT License
 *
 * Copyright 2017 Leif Lindbäck <leifl@kth.se>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package Client.View;

/**
 * One line of user input, which should be a command and parameters associated with that command (if
 * any).
 */
class CmdLine {
    private static final String PARAM_DELIMETER = " ";
    private String params;
    private Command cmd;
    private final String enteredLine;

    /**
     * Creates a new instance representing the specified line.
     *
     * @param enteredLine A line that was entered by the user.
     */
    CmdLine(String enteredLine) {
        this.enteredLine = enteredLine;
        this.params = enteredLine;
    }

    /**
     * @return The command represented by this object.
     */
    Command getCmd() {
        return cmd;
    }
    
    /**
     * @return The entire user input, without any modification.
     */
    String getUserInput() {
        return enteredLine;
    }

    /**
     * Returns the parameter with the specified index. The first parameter has index zero.
     * Parameters are separated by a blank character (" "). A Character sequence enclosed in quotes
     * form one single parameter, even if it contains blanks.
     *
     * @param index The index of the searched parameter.
     * @return The parameter with the specified index, or <code>null</code> if there is no parameter
     *         with that index.
     */
    String getParameter() {
        if (params == null) {
            return null;
        }
        return params;
    }

}
