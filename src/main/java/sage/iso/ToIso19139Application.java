package sage.iso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import sage.iso.transformer.ToIso19139Transformer;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.UUID;

public class ToIso19139Application implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${input}")
    private String inputFileLocation;

    @Value("${output}")
    private String outputFileLocation;

    private UUID uuid = UUID.randomUUID();

    private static String TO_ISO_19139_OLD_ISO_FROM_NEW_ISO = "toISO19139.xsl";

    public static void main(String[] args) {

        SpringApplication.run(ToIso19139Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

        try {

            this.logStatus("STARTED");

            ToIso19139Transformer transformer = new ToIso19139Transformer(this.createSaxonTransformer());

            StreamSource sourceXml = this.createStreamSource(this.inputFileLocation);
            StreamResult resultXml = this.createStreamResult(this.outputFileLocation);

            transformer.transform(sourceXml, resultXml);

            this.logStatus("COMPLETED");

        } catch (Exception e) {

            this.logStatus("ERROR", e);
            System.exit(1);
        }

        System.exit(0);
    }

    public Transformer createSaxonTransformer() throws TransformerConfigurationException {

        TransformerFactory transformerFactory = new net.sf.saxon.TransformerFactoryImpl();

        Source xslt = this.createToIso19139Xslt();

        return transformerFactory.newTransformer(xslt);
    }

    public Source createToIso19139Xslt() {

        return new StreamSource(this.getClass().getClassLoader().getResourceAsStream(TO_ISO_19139_OLD_ISO_FROM_NEW_ISO));
    }

    public StreamSource createStreamSource(String inputFileLocation) {

        return new StreamSource(createFile(inputFileLocation));
    }

    public StreamResult createStreamResult(String outputFileLocation) {

        return new StreamResult(this.createOutputFileWithDirectories(outputFileLocation));
    }

    public File createFile(String location) {

        return new File(location);
    }

    public File createOutputFileWithDirectories(String outputFileLocation) {

        File file = this.createFile(outputFileLocation);
        file.getAbsoluteFile().getParentFile().mkdirs();

        return file;
    }

    public void logStatus(String status) {

        this.logStatus(status, null);
    }

    public void logStatus(String status, Throwable throwable) {

        log.info("{} {} {} {}", uuid.toString(), this.inputFileLocation, this.outputFileLocation, status, throwable);
    }
}
