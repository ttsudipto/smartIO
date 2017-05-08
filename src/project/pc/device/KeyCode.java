package project.pc.device;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps the characters to type to the corresponding key codes.
 *
 * <p>
 *     These key codes represent the key(s) to be pressed. The
 *     mapping is done with the help of two <code>HashMap</code>s :
 *     <ol>
 *         <li>
 *             <i>Key Map</i> - Maps characters to key codes
 *             where pressing shift key is not required.
 *         </li>
 *         <li>
 *             <i>Shift Key Map</i> - Maps characters to key
 *             codes where pressing shift key is required. For
 *             example, the character <code>%</code> is mapped
 *             to <code>5</code> since <code>Shift+5</code> is
 *             pressed for typing <code>%</code>.
 *         </li>
 *     </ol>
 * </p>
 */
class KeyCode {

    private static Map<Character, Integer> sKeyMap = new HashMap<>();
    private static Map<Character, Integer> sShiftKeyMap = new HashMap<>();

    static {
        for (int i = 'a', j = KeyEvent.VK_A; i<='z'; ++i, ++j) {
            sKeyMap.put((char)i, j);
        }

        for (int i='A',j=KeyEvent.VK_A; i<='Z'; i++,j++) {
            sKeyMap.put((char)i, j);
        }

        for (int i = '0', j=KeyEvent.VK_0; i <= '9'; i++, j++) {
            sKeyMap.put((char)i, j);
        }

        // special char
        sKeyMap.put('`', KeyEvent.VK_BACK_QUOTE);
        sKeyMap.put('-', KeyEvent.VK_MINUS);
        sKeyMap.put('=', KeyEvent.VK_EQUALS);
        sKeyMap.put('_', KeyEvent.VK_UNDERSCORE);
        sKeyMap.put('+', KeyEvent.VK_PLUS);
        sKeyMap.put('[', KeyEvent.VK_OPEN_BRACKET);
        sKeyMap.put(']', KeyEvent.VK_OPEN_BRACKET);
        sKeyMap.put('\\', KeyEvent.VK_BACK_SLASH);
        sKeyMap.put(';', KeyEvent.VK_SEMICOLON);
        sKeyMap.put('\'', KeyEvent.VK_QUOTE);
        sKeyMap.put(',', KeyEvent.VK_COMMA);
        sKeyMap.put('.', KeyEvent.VK_PERIOD);
        sKeyMap.put('/', KeyEvent.VK_SLASH);
        sKeyMap.put(' ', KeyEvent.VK_SPACE);
        sKeyMap.put('\n', KeyEvent.VK_ENTER);
        sKeyMap.put('\b', KeyEvent.VK_BACK_SPACE);


        // set of character which need to be typed with shift key
        sShiftKeyMap.put('%', KeyEvent.VK_5);
        sShiftKeyMap.put('!', KeyEvent.VK_1);
        sShiftKeyMap.put('@', KeyEvent.VK_2);
        sShiftKeyMap.put('#', KeyEvent.VK_3);
        sShiftKeyMap.put('$', KeyEvent.VK_4);
        sShiftKeyMap.put('^', KeyEvent.VK_6);
        sShiftKeyMap.put('&', KeyEvent.VK_7);
        sShiftKeyMap.put('*', KeyEvent.VK_8);
        sShiftKeyMap.put('(', KeyEvent.VK_9);
        sShiftKeyMap.put(')', KeyEvent.VK_0);
        sShiftKeyMap.put(':', KeyEvent.VK_SEMICOLON);
        sShiftKeyMap.put('"', KeyEvent.VK_QUOTE);
        sShiftKeyMap.put('<', KeyEvent.VK_COMMA);
        sShiftKeyMap.put('>', KeyEvent.VK_PERIOD);
        sShiftKeyMap.put('?', KeyEvent.VK_SLASH);
        sShiftKeyMap.put('_', KeyEvent.VK_MINUS);
        sShiftKeyMap.put('+', KeyEvent.VK_EQUALS);
        sShiftKeyMap.put('{', KeyEvent.VK_OPEN_BRACKET);
        sShiftKeyMap.put('}', KeyEvent.VK_CLOSE_BRACKET);
        sShiftKeyMap.put('|', KeyEvent.VK_BACK_SLASH);
        sShiftKeyMap.put('~', KeyEvent.VK_BACK_QUOTE);
    }

    /**
     * Checks if <code>Shift</code> key needs to be pressed for
     * typing a character.
     *
     * @param c the character to be checked.
     * @return <code>true</code>, if <code>Shift</code> key
     *                            needs to be pressed, <br/>
     *         <code>false</code>, otherwise.
     */
    static boolean isShiftKey(char c) {
        return sShiftKeyMap.containsKey(c);
    }

    /**
     * Checks the <i>Shift Key Map</i> and obtains the shift key code
     * for a character.
     *
     * @param c the character to be checked.
     * @return the shift key code for <code>c</code>, <br/>
     *         <code>-1</code>, if <code>Shift</code> key press
     *                          is not required.
     */
    static int getShiftCode(char c) {
        if (!sShiftKeyMap.containsKey(c))  return -1;
        return sShiftKeyMap.get(c);
    }

    /**
     * Checks the <i>Key Map</i> and obtains the key code for a character
     * (if <code>Shift</code> key press is not required).
     *
     * @param c the character to be checked.
     * @return the key code for <code>c</code>, <br/>
     *         <code>-1</code>, if <code>Shift</code> key press is
     *                          required.
     */
    static int getCodeByChar(char c) {
        if (!sKeyMap.containsKey(c))   return -1;
        return sKeyMap.get(c);
    }
}