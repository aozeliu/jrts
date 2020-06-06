package org.jrts.maven.plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.List;

@Mojo(name = "select", defaultPhase = LifecyclePhase.PROCESS_TEST_CLASSES)
public class JRTSMojo extends AbstractMojo {

    @Parameter(property="project")
    protected MavenProject project;

    @Parameter(property = "skipTests", defaultValue = "false")
    private boolean skipTests;

    @Parameter(property = "jrts.skip", defaultValue = "false")
    private boolean skip;

    @Parameter(property = "jrts.root.dir", defaultValue = "")
    private String rootDir;

    public void execute() throws MojoExecutionException {
        String agentPath = rootDir + File.separator + "agent.jar";
        project.getProperties().setProperty("argLine", "-javaagent:" + agentPath + "=mode=junit4,root.dir="+rootDir);
    }

    /**
     * Plugin plugin = lookupPlugin("org.apache.maven.plugins:maven-surefire-plugin");
     * @param key groupId:artifactId
     * @return
     */
    public Plugin lookupPlugin(String key){
        List<Plugin> plugins = project.getBuildPlugins();
        for (Plugin plugin : plugins) {
            if(plugin.getKey().equals(key)){
                return plugin;
            }
        }
        return null;
    }
}
