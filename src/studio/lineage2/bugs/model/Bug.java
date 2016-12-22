package studio.lineage2.bugs.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 Eanseen
 23.06.2016
 */
@Entity @Table(name = "bugs") public class Bug
{
	@Id @GeneratedValue(generator = "increment") @GenericGenerator(name = "increment", strategy = "increment") @Column(name = "id") private @Getter @Setter long id;
	@Column(name = "bug_tag_id") private @Getter @Setter long bugTagId;
	@Lob @Column(name = "old_work", length = 100000) private @Getter @Setter String oldWork;
	@Lob @Column(name = "new_work", length = 100000) private @Getter @Setter String newWork;
	@Lob @Column(name = "other", length = 100000) private @Getter @Setter String other;
	@Lob @Column(name = "ip", length = 100000) private @Getter @Setter String ip;
}