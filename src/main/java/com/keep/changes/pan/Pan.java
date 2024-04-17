package com.keep.changes.pan;

import java.util.Set;

import com.keep.changes.fundraiser.Fundraiser;
import com.keep.changes.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(columnDefinition = "varchar(20)", nullable = false, unique = true)
	private String panNumber;

	@Column(columnDefinition = "varchar(30)", nullable = false)
	private String nameOnPan;

	@Column(columnDefinition = "varchar(100)", nullable = false)
	private String panImage;

	@OneToOne
	private User panHolder;

	@OneToMany(mappedBy = "pan")
	private Set<Fundraiser> fundraisers;

}
