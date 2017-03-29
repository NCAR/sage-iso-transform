package sage.iso;

import java.net.URISyntaxException;

public class SetLogDirectoryProperty {

    private String resourceLocation = null;

    private static String LOG_DIRECTORY = "log_directory";

    public SetLogDirectoryProperty() {

        try {

            this.resourceLocation = SetLogDirectoryProperty.class.getResource(SetLogDirectoryProperty.class.getSimpleName() + ".class").toURI().toString();

        } catch (URISyntaxException e) {
            throw new ResourceLocationException("Cannot get resource location for SetLogDirectoryProperty class.", e);
        }

    }

    public void setLogDirectoryProperty() {

        if (!isLogDirectoryPropertySet()) {

            if (isRunAsJarExecutable()) {

                System.getProperties().setProperty(LOG_DIRECTORY, getJarBasePath());

            } else {

                // Example output:
                // /Users/nhook/IdeaProjects/sage-iso-transform-command-line/target/classes/
                String logDirectory = ToIso19139Application.class.getProtectionDomain().getCodeSource().getLocation().getPath();

                System.getProperties().setProperty(LOG_DIRECTORY, logDirectory);
            }
        }
    }

    public boolean isLogDirectoryPropertySet() {

        return System.getProperties().containsKey(LOG_DIRECTORY);
    }

    // Example resourceLocation values.
    //
    // When running though an IDE:
    // file:/Users/nhook/IdeaProjects/sage-iso-transform-command-line/target/classes/sage/iso/SetLogDirectoryProperty.class
    //
    // When running as a Jar file:
    // jar:file:/Users/nhook/Downloads/iso-transform/iso-transform-command-line-0.0.1-SNAPSHOT.jar!/BOOT-INF/classes!/sage/iso/SetLogDirectoryProperty.class
    public boolean isRunAsJarExecutable() {

        return this.resourceLocation.startsWith("jar:");
    }

    public String getJarBasePath() {

        String path = this.removeUrlPrefix(this.resourceLocation);

        path = this.truncateFromJarOn(path);

        path = truncateFromLastSlash(path);

        return path;
    }

    private String removeUrlPrefix(String path) {

        return path.replace("jar:file:", "");
    }

    private String truncateFromJarOn(String path) {

        int index = path.indexOf(".jar!");

        return path.substring(0, index);
    }

    private String truncateFromLastSlash(String path) {

        int index = path.lastIndexOf("/");

        return path.substring(0, index);
    }
}
