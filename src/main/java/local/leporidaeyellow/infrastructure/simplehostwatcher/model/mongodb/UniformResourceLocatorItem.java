package local.leporidaeyellow.infrastructure.simplehostwatcher.model.mongodb;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document("${mongodb-document}")
public final class UniformResourceLocatorItem {

    @Id
    private String id;

    private String type;
    private String url;
    private Boolean approved;

    public UniformResourceLocatorItem(String id, String type, String url, Boolean approved) {
        super();
        this.id = id;
        this.type = type;
        this.url = url;
        this.approved = approved;
    }
}
