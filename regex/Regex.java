import java.util.ArrayList;

public class Regex{
    private String pattern;
    // stored as a state transition table.
    // each element of a list is a new state 
    // and the elements of the array inside it are the events.
    private ArrayList<int[]> compiledPattern;
    
    public Regex(String pattern) {
        this.pattern = pattern;
        compiledPattern = this.compile();
    }

    // should throw some kind of error if not valid
    ArrayList<int[]> compile() {
        ArrayList<int[]> obj = new ArrayList<int[]>();
        obj.add(new int[128]);
        for(int i = 0; i < pattern.length(); ++i){
                char c = pattern.charAt(i);
                if (c >= 65 && c <= 122){
                    int[] state = new int[128];
                    state[c] = obj.size() + 1;
                    obj.add(state);
                }else{
                    obj.clear();
                    return obj;
                }
        }
        return obj;
    }

    boolean full_match(String src){
        if (compiledPattern.size() <= 0){
            return false;
        }

        int stateIdx = 1;
        for(char c : src.toCharArray()){
            if(stateIdx == 0 || stateIdx >= compiledPattern.size()){
                return false;
            }
            stateIdx = compiledPattern.get(stateIdx)[c];
        }
        return stateIdx == 0? false: true;
    }

    public static void main(String[] args){
        Regex r = new Regex("abc");
        System.out.println(r.full_match("abc"));
    }
}
