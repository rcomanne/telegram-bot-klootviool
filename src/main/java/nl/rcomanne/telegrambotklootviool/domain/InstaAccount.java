package nl.rcomanne.telegrambotklootviool.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "InstaAccount")
@Table(name = "insta_account")
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

    @OneToMany(
            mappedBy = "account",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<InstaItem> items;
}
