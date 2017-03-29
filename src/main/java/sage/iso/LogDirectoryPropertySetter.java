package sage.iso;

import java.net.URISyntaxException;

public class LogDirectoryPropertySetter {

    // Example resourceLocation values.
    //
    // When running though an IDE:
    // file:/Users/nhook/IdeaProjects/sage-iso-transform-command-line/target/classes/sage/iso/LogDirectoryPropertySetter.class
    //
    // When running as a Jar file:
    // jar:file:/Users/nhook/Downloads/iso-transform/iso-transform-command-line-0.0.1-SNAPSHOT.jar!/BOOT-INF/classes!/sage/iso/LogDirectoryPropertySetter.class
    private String resourceLocation = null;

    // Example output:
    // /Users/nhook/IdeaProjects/sage-iso-transform-command-line/target/classes/
    private String protectedDomainLocation = null;

    // This value is the same as in the logback-spring.xml file.
    private static final String LOG_DIRECTORY = "log_directory";

    public LogDirectoryPropertySetter() {

        try {

            this.resourceLocation = LogDirectoryPropertySetter.class.getResource(LogDirectoryPropertySetter.class.getSimpleName() + ".class").toURI().toString();
            this.protectedDomainLocation = LogDirectoryPropertySetter.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        } catch (URISyntaxException e) {

            throw new ResourceLocationException("Cannot get resource location for LogDirectoryPropertySetter class.", e);
        }
    }

    // For testing purposes only.
    LogDirectoryPropertySetter(String resourceLocation, String protectedDomainLocation) {

        this.resourceLocation = resourceLocation;
        this.protectedDomainLocation = protectedDomainLocation;
    }

    public void setLogDirectoryProperty() {

        if (!isPropertyAlreadySet()) {

            if (isRunAsJarExecutable()) {

                System.getProperties().setProperty(LOG_DIRECTORY, getJarBasePath());

            } else {

                System.getProperties().setProperty(LOG_DIRECTORY, this.protectedDomainLocation);
            }
        }
    }

    private boolean isPropertyAlreadySet() {

        return System.getProperties().containsKey(LOG_DIRECTORY);
    }

    // Example resourceLocation values.
    //
    // When running though an IDE:
    // file:/Users/nhook/IdeaProjects/sage-iso-transform-command-line/target/classes/sage/iso/LogDirectoryPropertySetter.class
    //
    // When running as a Jar file:
    // jar:file:/Users/nhook/Downloads/iso-transform/iso-transform-command-line-0.0.1-SNAPSHOT.jar!/BOOT-INF/classes!/sage/iso/LogDirectoryPropertySetter.class
    private boolean isRunAsJarExecutable() {

        return this.resourceLocation.startsWith("jar:");
    }

    private String getJarBasePath() {

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

        if (index != -1) {

            path = path.substring(0, index);
        }

        return path;
    }

    private String truncateFromLastSlash(String path) {

        int index = path.lastIndexOf("/");

        if (index != -1) {

            path = path.substring(0, index);
        }

        return path;
    }
}
