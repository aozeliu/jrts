package org.jrts.core.select;

import lombok.extern.slf4j.Slf4j;
import org.jrts.core.check.Checker;
import org.jrts.core.hash.Hasher;
import org.jrts.core.store.Storer;

import java.io.File;
import java.util.List;

@Slf4j
public class Selector {

    private Hasher hasher;
    private Storer storer;
    private Checker checker;


    public Selector() {
        hasher = new Hasher();
        storer = new Storer();
        checker = new Checker(hasher);
    }

    public List<String> selectTest(String path){
        return checker.checkAffected(storer.loadAll(new File(path)));
    }

    public List<String> nonSelectedTest(String path){
        return checker.checkNonAffected(storer.loadAll(new File(path)));
    }
}
