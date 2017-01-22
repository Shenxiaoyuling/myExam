package bean;

/**
 */
public class StateEvent {

    private String name;
    private String objstate;
    private String arg;
    private String function;
    private Rettype rettype;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjstate() {
        return objstate;
    }

    public void setObjstate(String objstate) {
        this.objstate = objstate;
    }

    public String getArg() {
        return arg;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public Rettype getRettype() {
        return rettype;
    }

    public void setRettype(Rettype rettype) {
        this.rettype = rettype;
    }

}
