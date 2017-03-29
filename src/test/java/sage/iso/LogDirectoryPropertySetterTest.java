package sage.iso;

import org.junit.After;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LogDirectoryPropertySetterTest {

    private static final String LOG_DIRECTORY = "log_directory";
    @After
    public void tearDown() {

        System.getProperties().remove(LOG_DIRECTORY);
    }

    @Test
    public void given_system_property_already_set__when_setLogDirectoryProperty__then_system_property_stays_same_value() {

        System.setProperty("log_directory", "log_directory_already_set");

        this.createAndSetProperty("resourceLocation", "protectedDomainLocation");

        assertThat(System.getProperty(LOG_DIRECTORY), is("log_directory_already_set"));
    }

    @Test
    public void given_running_as_jar_file__when_setLogDirectoryProperty__then_use_resource_location() {

        this.createAndSetProperty("jar:file:resourceLocation", "protectedDomainLocation");

        assertThat(System.getProperty(LOG_DIRECTORY), is("resourceLocation"));
    }

    @Test
    public void given_running_as_jar_file__when_setLogDirectory_property__then_resource_location_properly_parsed() {

        this.createAndSetProperty("jar:file:/Users/nhook/Downloads/iso-transform/iso-transform-command-line-0.0.1-SNAPSHOT.jar!/BOOT-INF/classes!/sage/iso/LogDirectoryPropertySetter.class", null);

        assertThat(System.getProperty(LOG_DIRECTORY), is("/Users/nhook/Downloads/iso-transform"));
    }

    @Test
    public void given_running_in_ide__when_setLogDirectoryProperty__then_use_protected_domain_location() {

        this.createAndSetProperty("file:resourceLocation", "protectedDomainLocation");

        assertThat(System.getProperty(LOG_DIRECTORY), is("protectedDomainLocation"));
    }

    private void createAndSetProperty(String resourceLocation, String protectedDomainLocation) {

        LogDirectoryPropertySetter setter = new LogDirectoryPropertySetter(resourceLocation, protectedDomainLocation);

        setter.setLogDirectoryProperty();
    }
}
