package sg.obj;

import java.io.File;

public class StatedVP extends VP {

    public File newstate;

    public StatedVP(VP vp, File newState) {
        super(vp.province, vp.value);
        this.newstate = newState;
    }
}
