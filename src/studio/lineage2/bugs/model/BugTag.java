package studio.lineage2.bugs.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 Eanseen
 24.06.2016
 */
@Entity @Table(name = "bug_tags") public class BugTag
{
	@Id @GeneratedValue(generator = "increment") @GenericGenerator(name = "increment", strategy = "increment") @Column(name = "id") private @Getter @Setter long id;
	@Column(name = "project_id") private @Getter @Setter int projectId;
	@Column(name = "type_id") private @Getter @Setter int typeId;
	@Column(name = "name") private @Getter @Setter String name;
}