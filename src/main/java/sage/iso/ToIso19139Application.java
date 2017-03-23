package sage.iso;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import sage.iso.transformer.ToIso19139Transformer;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class ToIso19139Application implements CommandLineRunner {

    @Value("${input}")
    private String inputFileLocation;

    @Value("${output}")
    private String outputFileLocation;

    public static void main(String[] args) throws Exception {

        SpringApplication.run(ToIso19139Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

        try {

            ToIso19139Transformer transformer = new ToIso19139Transformer(this.createSaxonTransformer());

            StreamSource sourceXml = this.createStreamSource(this.inputFileLocation);
            StreamResult resultXml = this.createStreamResult(this.outputFileLocation);

            transformer.transform(sourceXml, resultXml);

        } catch (Exception e) {

            // TODO log exception.
            System.exit(1);
        }

        System.exit(0);
    }

    public Transformer createSaxonTransformer() throws Exception {

        TransformerFactory transformerFactory = new net.sf.saxon.TransformerFactoryImpl();

        Source xslt = this.createToIso19139Xslt();

        return transformerFactory.newTransformer(xslt);
    }

    public Source createToIso19139Xslt() {

        return new StreamSource(new File(this.getClass().getClassLoader().getResource("toISO19139.xsl").getPath()));
    }

    public StreamSource createStreamSource(String inputFileLocation) {

        return new StreamSource(createFile(inputFileLocation));
    }

    public StreamResult createStreamResult(String outputFileLocation) {

        return new StreamResult(createFile(outputFileLocation));
    }

    public File createFile(String location) {

        return new File(location);
    }
}
