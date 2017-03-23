package sage.iso.transformer;

import org.junit.Test;

import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ToIso19139TransformerTest {

    @Test
    public void given_transformer__when_transform__then_transformer_called_with_streams() throws Exception {

        Transformer mockTransformer = mock(Transformer.class);

        ToIso19139Transformer transformer = new ToIso19139Transformer(mockTransformer);

        StreamSource dummyStreamSource = mock(StreamSource.class);
        StreamResult dummyStreamResult = mock(StreamResult.class);

        transformer.transform(dummyStreamSource, dummyStreamResult);

        verify(mockTransformer).transform(dummyStreamSource, dummyStreamResult);
    }
}
