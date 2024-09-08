import java.util.ArrayList;

public class Regex {
    private String pattern;
    // INFO: stored as a state transition table.
    // each element of a list is a new state
    // and the elements of the array inside it are the events.
    private ArrayList<int[]> compiledPattern;

    public Regex(String pattern) {
        this.pattern = pattern;
        compiledPattern = this.compile();
    }

    private boolean isValidAscii(char c) {
        return c >= 32 && c <= 126;
    }

    private void setRange(int[] arr, int min, int max, int val) {
        for (int i = min; i <= max; ++i) {
            arr[i] = val;
        }
    }

    private void copyState(int[] src, int[] dest) {
        if (src.length != dest.length) {
            return;
        }
        for (int idx = 0; idx < src.length; ++idx) {
            dest[idx] = src[idx];
        }
    }

    // TODO: should throw some kind of error if not valid
    ArrayList<int[]> compile() {
        ArrayList<int[]> obj = new ArrayList<int[]>();
        obj.add(new int[1210]);
        for (int i = 0; i < pattern.length(); ++i) {
            char c = pattern.charAt(i);
            int[] state = new int[128];
            char prev = i > 0 ? pattern.charAt(i - 1) : '\0';
            switch (c) {
                case '.':
                    setRange(state, 32, 126, obj.size() + 1);
                    break;

                case '*':
                case '+':
                    if (i == '\0' || !isValidAscii(prev) || prev == '+') {
                        obj.clear();
                        return obj;
                    }
                    if (c == '*') {
                        setRange(obj.getLast(), 32, 126, obj.size());
                    }
                    if (i < pattern.length() - 1) {
                        setRange(state, 32, 126, obj.size() + 1);
                    }

                    if (prev == '.') {
                        setRange(state, 32, 126, obj.size());
                    } else {
                        state[prev] = obj.size();
                    }

                if (i < pattern.length() - 1){
                        char next = pattern.charAt(i + 1);
                        if (next == '.' || next == '*' || next == '+') {
                            obj.clear();
                            return obj;
                        }
                        state[next] = obj.size() + 1;
                }
                    break;

                default:
                    if (c < 65 || c > 122) {
                        obj.clear();
                        return obj;
                    }
                    state[c] = obj.size() + 1;
                    break;
            }
            obj.add(state);
        }
        return obj;
    }

    boolean match(String src) {
        if (compiledPattern.size() <= 0) {
            return false;
        }

        for (int idx = 0; idx < src.length(); ++idx) {
            if (full_match(src.substring(idx))) {
                return true;
            }
        }
        return false;
    }

    boolean full_match(String src) {
        if (compiledPattern.size() <= 0) {
            return false;
        }

        int stateIdx = 1;
        for (int idx = 0; idx < src.length();) {
            if (stateIdx == 0 || stateIdx >= compiledPattern.size()) {
                break;
            }
            int currIdx = stateIdx;
            for (; idx < src.length() && currIdx == stateIdx; ++idx) {
                char c = src.charAt(idx);
                stateIdx = compiledPattern.get(stateIdx)[c];
            }
            if (currIdx != stateIdx && idx != 1) {
                --idx;
            }
            if (currIdx == stateIdx) {
                stateIdx++;
            }
        }

        if (stateIdx < compiledPattern.size()) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Regex r = new Regex(".b*c");
        System.out.println(r.match("babbbcd"));
        System.out.println(r.full_match("ac"));
        System.out.println(r.full_match("a"));
        System.out.println(r.full_match("bbc"));
        System.out.println(r.full_match("1bc"));

        System.out.println();

        Regex r1 = new Regex(".b+c+");
        System.out.println(r1.full_match("abbbcc"));
        System.out.println(r1.full_match("ac"));
        System.out.println(r1.full_match("bbc"));
        System.out.println(r1.full_match("1bc"));

        System.out.println();
        Regex r2 = new Regex("a.*c");
        System.out.println(r2.match("abbbcc"));

    }
}
