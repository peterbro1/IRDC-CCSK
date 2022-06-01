package me.gmx.process.process;

import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelNode;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class ProcessImpl extends Process{

    public ProcessImpl(String s) {
        super(s);
    }

    public ProcessImpl(String s, Collection<Label> restrictions){
        super(s, restrictions);
    }

    @Override
    protected Process clone() {
        return new ProcessImpl(origin);
    }

    @Override
    public boolean canAct(Label label) {
        return false;
    }

    @Override
    public Process actOn(Label label) {
        return new NullProcess();
    }

    public String represent() {
        return super.represent(origin());
    }

    @Override
    public Collection<Process> getChildren() {
        return Collections.emptySet();
    }

    @Override
    public Collection<Label> getActionableLabels() {
        return super.getActionableLabels();
    }

    @Override
    public String origin(){
        return origin + ".0";
    }



}
