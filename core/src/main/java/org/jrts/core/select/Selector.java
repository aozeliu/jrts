package org.jrts.core.select;

import lombok.extern.slf4j.Slf4j;
import org.jrts.core.check.Checker;
import org.jrts.core.hash.Hasher;
import org.jrts.core.store.Storer;
import org.jrts.core.util.Constants;

import java.io.File;
import java.util.List;
import java.util.Map;

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

    public boolean isAffect(String test){
        Map<String, String> data = storer.load(new File(Constants.JRTS_DEFAULT_DATA_PATH,
                test + Constants.DEPENDENCY_FILE_EXTENSION));
        return checker.check(data);
    }
}
