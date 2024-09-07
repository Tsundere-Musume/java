public class Turnstile{
    enum State{
        LOCKED,
        UNLOCKED,
    }

    public enum Action{
        PUSH, 
        COIN,
    } 
    //attributes
    private State state;

    public Turnstile(){
        state = State.LOCKED;
    }

    public Turnstile(State initial){
        state = initial;
    }

    public static void main(String[] args){
        Turnstile t = new Turnstile();
        System.out.println(t);
        for (String arg: args){
            t.act(arg.equals("push") ? Action.PUSH : Action.COIN);
            System.out.println(t);
        }
    }

    public void  act(Action action){
        if (this.state == State.LOCKED){
            if (action  == Action.COIN){
                this.state = State.UNLOCKED;
            }
        }else{
            if (action  == Action.PUSH){
                this.state = State.LOCKED;
            }
        }
    }

    @Override
    public String toString(){
        if (this.state == State.LOCKED){
            return "Locked";
        }
        return "Unlocked";
    }
}
