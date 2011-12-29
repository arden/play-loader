package play.modules.loader;

import play.Logger;
import play.Play;
import play.PlayPlugin;
import play.vfs.VirtualFile;

import java.io.File;
import java.lang.System;

/**
 * 让Play加载自定义的java类文件
 *
 * @author arden
 */
public class LoaderPlugin extends PlayPlugin {
    /**
     * 加载项目依赖
     */
    public void onLoad() {
        this.loadSources();
        this.loadModules();
        this.loadTemplates();
    }

    /**
     * 加载项目模块
     */
    private void loadModules() {
        VirtualFile appRoot = VirtualFile.open(Play.applicationPath);
        String pattern = (String) Play.configuration.get("app.modules");
        if (pattern != null) {
            File parentFile = appRoot.getRealFile().getParentFile();
            String[] paths = pattern.split(",");
            if (paths != null) {
                for (int i = 0; i < paths.length; i++) {
                    try {
                        int index = paths[i].lastIndexOf("/");
                        String name = paths[i].substring(index + 1);
                        Play.addModule(name, new File(parentFile.getPath() + paths[i]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 加载源代码
     */
    private void loadSources() {
        VirtualFile appRoot = VirtualFile.open(Play.applicationPath);
        String pattern = (String) Play.configuration.get("source.paths");
        String absolutePattern = (String) Play.configuration.get("source.absolute.paths");
        String relativePattern = (String) Play.configuration.get("source.relative.paths");

        //加载基于项目相对路径的源代码
        if (relativePattern != null) {
            File parentFile = appRoot.getRealFile().getParentFile();
            String[] paths = relativePattern.split(",");
            if (paths != null) {
                for (int i = 0; i < paths.length; i++) {
                    Play.javaPath.add(VirtualFile.open(parentFile.getPath() + paths[i]));
                }
            }
        }

        //加载绝对路径的源代码
        if (absolutePattern != null) {
            String[] paths = absolutePattern.split(",");
            if (paths != null) {
                for (int i = 0; i < paths.length; i++) {
                    Play.javaPath.add(VirtualFile.open(new File(paths[i])));
                }
            }
        }

        //加载相对路径的源代码
        if (pattern != null) {
            String[] paths = pattern.split(",");
            if (paths != null) {
                for (int i = 0; i < paths.length; i++) {
                    Play.javaPath.add(appRoot.child(paths[i]));
                }
            }
        }
    }

    /**
     * 加载模版路径
     */
    public void loadTemplates() {
        VirtualFile appRoot = VirtualFile.open(Play.applicationPath);
        String pattern = (String) Play.configuration.get("template.paths");
        if (pattern != null) {
            Play.templatesPath.add(appRoot.child(pattern));
        }
    }
}