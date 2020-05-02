package nl.rcomanne.telegrambotklootviool.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class InstaAccount {

    @Id
    private String username;
    private LocalDateTime lastUpdated;

    @OneToMany
    private List<InstaItem> items;
}
