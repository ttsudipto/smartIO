package device;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sayantan Majumdar
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
    }

    static boolean isShiftKey(char c) {
        return sShiftKeyMap.containsKey(c);
    }

    static int getShiftCode(char c) {
        if (!sShiftKeyMap.containsKey(c))  return -1;
        return sShiftKeyMap.get(c);
    }

    static int getCodeByChar(char c) {
        if (!sKeyMap.containsKey(c))   return -1;
        return sKeyMap.get(c);
    }
}