package sage.iso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import sage.iso.transformer.ToIso19139Transformer;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ToIso19139Application implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${input}")
    private String inputFileLocation;

    @Value("${output}")
    private String outputFileLocation;


    public static void main(String[] args) throws Exception {

        LogDirectoryPropertySetter logDirectoryPropertySetter = new LogDirectoryPropertySetter();
        logDirectoryPropertySetter.setLogDirectoryProperty();

        SpringApplication.run(ToIso19139Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

        // TODO All this behavior belongs behind an interface where the first layer logs all the important information.
        // The next layer will take the output of the transformation (last layer) and write it to disk.
        // The last layer actually does the transformation.
        // This method should only call the interface and do the System.exit behavior with the try catch statements.
        try {

            this.logStatus("STARTED");

            ToIso19139Transformer transformer = new ToIso19139Transformer(this.createSaxonTransformer());

            StreamSource sourceXml = this.createStreamSource(this.inputFileLocation);

            ByteArrayOutputStream resultByteArrayOutputStream = new ByteArrayOutputStream();
            StreamResult resultXml = new StreamResult(resultByteArrayOutputStream);

            transformer.transform(sourceXml, resultXml);

            File resultFile = this.createOutputFileWithDirectories(this.outputFileLocation);
            try(OutputStream outputStream = new FileOutputStream(resultFile)) {
                resultByteArrayOutputStream.writeTo(outputStream);
            }

            this.logStatus("COMPLETED");

        } catch (Exception e) {

            this.logStatus("ERROR", e);
            log.error(e.getMessage());
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

        return new StreamSource(this.getClass().getClassLoader().getResourceAsStream("toISO19139.xsl"));
    }

    public StreamSource createStreamSource(String inputFileLocation) {

        return new StreamSource(createFile(inputFileLocation));
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

        log.info("{} {} {}", this.inputFileLocation, this.outputFileLocation, status, throwable);
    }
}
