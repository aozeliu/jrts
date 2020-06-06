## Introduction
jrts is a regression test selection tool to reduce redundant tests and improve test efficiency.


## Usage
1. build and install
 ```bash
 mvn -DskipTests install
 ```
2. An example
Switch to the **sample** directory, execute "*mvn test*" the first execution will run two test cases.
Then execute again, no test will be executed, because the code in the sample has not changed.
If you change the BService.java, then run *mvn test* again, it will run AServiceTest, but will not run BServiceTest.

## maven configuration
```xml
<plugin>
    <groupId>org.jrts</groupId>
    <artifactId>jrts-maven-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <executions>
        <execution>
            <goals>
                <goal>select</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Note

1. At present, this tool is only integrated with JUnit4.12, other versions of the test framework are not yet integrated
2. If the **argLine** parameter is set when using the surefire plugin, the tool will not take effect.

