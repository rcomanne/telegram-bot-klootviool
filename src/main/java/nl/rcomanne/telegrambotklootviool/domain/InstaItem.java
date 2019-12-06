package nl.rcomanne.telegrambotklootviool.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "instagram")
public class InstaItem implements Serializable {

    @Transient
    private static final long serialVersionUID = 7411852493041644393L;

    @Id
    private long id;
    private String link;
    private boolean isVideo;
    private String caption;
    private String fromUser;
    private int likes;

}
