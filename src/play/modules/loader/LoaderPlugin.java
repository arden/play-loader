package play.modules.loader;

import play.Logger;
import play.Play;
import play.PlayPlugin;
import play.vfs.VirtualFile;

/**
 * 让Play加载自定义的java类文件
 *
 * @author arden
 */
public class LoaderPlugin extends PlayPlugin {

    public void onLoad() {
        String pattern = (String) Play.configuration.get("loader.path");
        if (pattern != null) {
			VirtualFile appRoot = VirtualFile.open(Play.applicationPath);
			String[] paths = pattern.split(",");
            if (paths != null) {
                for (int i = 0; i < paths.length; i++) {
                    Play.javaPath.add(appRoot.child(paths[i]));
                }
            }
        } else {
            Logger.info("Missing configuration 'loader.path', loader will be ignored!");
        }
    }
}