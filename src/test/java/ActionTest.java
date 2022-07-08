import me.gmx.parser.CCSParser;
import me.gmx.process.nodes.*;
import me.gmx.process.process.Process;
import me.gmx.thread.ProcessContainer;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ActionTest {

    @Test
    public static void testBasicActions() {
        Label a,b,c;
        a = LabelFactory.createDebugLabel("a");
        b = LabelFactory.createDebugLabel("b");
        c = LabelFactory.createDebugLabel("c");
        ProcessContainer p = new ProcessContainer(CCSParser.parseLine("a.b").export());
        p.act(a);
        assert p.canAct(b);
    }

    @Test
    public void testAdvancedActions() {
        Label a,b,c,d ;
        a = LabelFactory.createDebugLabel("a");
        b = LabelFactory.createDebugLabel("b");
        c = LabelFactory.createDebugLabel("c");
        d = LabelFactory.createDebugLabel("d");

        ProcessContainer p = new ProcessContainer(CCSParser.parseLine("a.b+c.d").export());
        assert p.canAct(a) && p.canAct(c);
        assert !p.canAct(b) && !p.canAct(d);
        p.act(a);
        assert p.canAct(b) && !p.canAct(c);
        p.reverseLastAction();

    }

}
