package nl.rcomanne.telegrambotklootviool.domain;

import lombok.*;
import org.springframework.data.annotation.Transient;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class InstaItem implements Serializable {

    @Transient
    private static final long serialVersionUID = 7411852493041644393L;

    @Id
    private long id;
    private String link;
    private boolean isVideo;
    private String caption;
    private int likes;

    @ManyToOne
    private InstaAccount account;

}
